package Controlador;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.sql.*;
import java.util.ArrayList;

import Modelo.Coleccion;
import Modelo.Numero;

/**
 * Clase encargada de realizar operaciones con las colecciones y consultar datos necesarios para ello.
 * @author admin
 *
 */
public class gestionColecciones {

	/**
	 * Inserta la colecci�n enviada en la base de datos, estableciendo como identificador el n�mero siguiente al �ltimo identificador de colecci�n en la base de datos.
	 * @param coleccion Colecci�n a insertar
	 */
	public static void insertarColeccion(Coleccion coleccion) {
		String insercion = "INSERT INTO colecciones (nombre,img) VALUES (?,?);", increment = "ALTER TABLE comics.colecciones AUTO_INCREMENT = ?;";
		InputStream input = null;
		
		if (coleccion.getImg() != null) {
			input = new ByteArrayInputStream(coleccion.getImg());
		}
		
		Connection con = null;
		
		try {
			con = Pool.getConexion();
			PreparedStatement ps = con.prepareStatement(increment);
			
			int ultimoId = getUltimoId(con);
			
			ps.setInt(1, ultimoId);
			
			ps.execute();
			
			con.commit();
			
		} catch (SQLException e) {
			try {
				con.rollback();
			} catch (SQLException e1) {
				//e1.printStackTrace();
			}
			//e.printStackTrace();
			
			return;
		}  finally {
			Pool.Cerrar();
		}

		try {
			con = Pool.getConexion();
			PreparedStatement ps = con.prepareStatement(insercion);
			
			ps.setString(1, coleccion.getNombre());
			
			if (input != null) {
				ps.setBinaryStream(2, input, (int)(coleccion.getImg().length));
			} else {
				ps.setObject(2, null);
			}
			 
			ps.executeUpdate();
			
			con.commit();

		} catch (SQLException e) {
			try {
				con.rollback();
			} catch (SQLException e1) {
				//e1.printStackTrace();
			}
			//e.printStackTrace();
		}  finally {
			Pool.Cerrar();
		}
		
	}

	/**
	 * Obtiene el �ltimo identificador de colecci�n disponible en la base de datos
	 * @param con Conexi�n con la base de datos actual
	 * @return �ltimo ID de colecci�n
	 */
	private static int getUltimoId(Connection con) {
		int id = 0;
		String query = "SELECT MAX(id) from comics.colecciones";
		
		try {
			Statement st = con.createStatement();
			
			ResultSet rs = st.executeQuery(query);
			
			if (rs.next()) {
				id = rs.getInt(1);
			}
			
			rs.close();
			st.close();
			
		} catch (SQLException e) {
			//e.printStackTrace();
		}
		
		return id;
	}

	/**
	 * Modifica la colecci�n enviada en la base de datos
	 * @param coleccion Colecci�n a modificar
	 */
	public static void modificarColeccion(Coleccion coleccion) {
		String modificacion = "UPDATE colecciones SET nombre = ?, img = ? WHERE id = ?;";
		InputStream input = null;
		
		if (coleccion.getImg() != null) {
			input = new ByteArrayInputStream(coleccion.getImg());
		}
		
		Connection con = null;

		try {
			con = Pool.getConexion();
			PreparedStatement ps = con.prepareStatement(modificacion);
			
			ps.setString(1, coleccion.getNombre());
			
			if (input != null) {
				ps.setBinaryStream(2, input, (int)(coleccion.getImg().length));
			} else {
				ps.setObject(2, null);
			}
			
			ps.setInt(3, coleccion.getId());
			 
			ps.executeUpdate();
			
			con.commit();

		} catch (SQLException e) {
			try {
				con.rollback();
			} catch (SQLException e1) {
				//e1.printStackTrace();
			}
			//e.printStackTrace();
		}  finally {
			Pool.Cerrar();
		}
	}

	/**
	 * Borra la colecci�n enviada de la base de datos
	 * @param coleccion Colecci�n a borrar
	 */
	public static void borrarColeccion(Coleccion coleccion) {
		String borrado = "DELETE FROM colecciones WHERE id = ?;";
		Connection con = null;

		try {
			con = Pool.getConexion();
			PreparedStatement ps = con.prepareStatement(borrado);
			
			ps.setInt(1, coleccion.getId());
			 
			ps.executeUpdate();
			
			con.commit();

		} catch (SQLException e) {
			try {
				con.rollback();
			} catch (SQLException e1) {
				//e1.printStackTrace();
			}
			//e.printStackTrace();
		}  finally {
			Pool.Cerrar();
		}
	}

	/**
	 * Borra todos los n�meros de una colecci�n primero y luego la colecci�n si no hubo errores. En caso contrario, deshace todos los borrados.
	 * @param coleccion Colecci�n a borrar
	 * @param comicsRelacionados N�meros de la colecci�n a borrar
	 */
	public static void borrarColeccionConNumeros(Coleccion coleccion, ArrayList<Numero> comicsRelacionados) {
		String borrarCol = "DELETE FROM colecciones WHERE id = ?;", borrarNum = "DELETE FROM numeros WHERE id = ?;";
		Connection con = null;
		PreparedStatement ps;

		//borrar primero cada numero y al final coleccion
		try {
			con = Pool.getConexion();
			
			for (Numero numero : comicsRelacionados) {
				ps = con.prepareStatement(borrarNum);

				ps.setInt(1, numero.getId());

				ps.executeUpdate();
			}
			
			ps = con.prepareStatement(borrarCol);
			
			ps.setInt(1, coleccion.getId());
			 
			ps.executeUpdate();
			
			con.commit(); //se efectuan todos los borrados solo si todo va bien

		} catch (SQLException e) {
			try {
				con.rollback(); //deshace todos los borrados de numeros y coleccion
			} catch (SQLException e1) {
				//e1.printStackTrace();
			}
			//e.printStackTrace();
		}  finally {
			Pool.Cerrar();
		}
		
	}

}
