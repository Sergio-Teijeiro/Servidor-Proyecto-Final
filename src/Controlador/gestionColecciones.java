package Controlador;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.sql.*;
import java.util.ArrayList;

import Modelo.Coleccion;
import Modelo.Numero;

public class gestionColecciones {

	public static void insertarColeccion(Coleccion coleccion) {
		String insercion = "INSERT INTO colecciones (nombre,img) VALUES (?,?);";
		InputStream input = null;
		
		if (coleccion.getImg() != null) {
			input = new ByteArrayInputStream(coleccion.getImg());
		}
		
		Connection con = null;

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
				e1.printStackTrace();
			}
			e.printStackTrace();
		}  finally {
			Pool.Cerrar();
		}
		
	}

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
				e1.printStackTrace();
			}
			e.printStackTrace();
		}  finally {
			Pool.Cerrar();
		}
	}

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
				e1.printStackTrace();
			}
			e.printStackTrace();
		}  finally {
			Pool.Cerrar();
		}
	}

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
				e1.printStackTrace();
			}
			e.printStackTrace();
		}  finally {
			Pool.Cerrar();
		}
		
	}

}
