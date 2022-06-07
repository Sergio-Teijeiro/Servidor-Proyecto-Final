package Controlador;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.imageio.ImageIO;

import Modelo.Numero;

/**
 * Clase encargada de realizar operaciones con los números y consultar datos necesarios para ello.
 * @author admin
 *
 */
public class gestionNumeros {

	/**
	 * Inserta el número enviado en la base de datos, estableciendo como identificador el número siguiente al último identificador de cómic en la base de datos.
	 * @param numero Número a insertar
	 */
	public static void insertarNumero(Numero numero) {
		String insercion = "INSERT INTO numeros (titulo,fecha_adquisicion,tapa,estado,resenha,img,id_coleccion) VALUES (?,?,?,?,?,?,?);", increment = "ALTER TABLE comics.numeros AUTO_INCREMENT = ?;";
		InputStream input = null;
		
		if (numero.getImg() != null) {
			input = new ByteArrayInputStream(numero.getImg());
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
			
			ps.setString(1, numero.getTitulo());
			ps.setDate(2, numero.getFechaAdquisicion());
			ps.setString(3, numero.getTapa());
			ps.setString(4, numero.getEstado());
			ps.setString(5, numero.getResenha());
			
			if (input != null) {
				ps.setBinaryStream(6, input, (int)(numero.getImg().length));
			} else {
				ps.setObject(6, null);
			}
			
			ps.setInt(7, numero.getIdColeccion());
			 
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
	 * Modifica el número enviado en la base de datos
	 * @param numero Número a modificar
	 */
	public static void modificarNumero(Numero numero) {
		String modificacion = "UPDATE numeros SET titulo = ?, fecha_adquisicion = ?, tapa = ?, estado = ?,resenha = ?,img = ?,id_coleccion = ? WHERE id = ?;";
		InputStream input = null;
		
		if (numero.getImg() != null) {
			input = new ByteArrayInputStream(numero.getImg());
		}
		
		Connection con = null;
		
		try {
			con = Pool.getConexion();
			PreparedStatement ps = con.prepareStatement(modificacion);
			
			ps.setString(1, numero.getTitulo());
			ps.setDate(2, numero.getFechaAdquisicion());
			ps.setString(3, numero.getTapa());
			ps.setString(4, numero.getEstado());
			ps.setString(5, numero.getResenha());
			
			if (input != null) {
				ps.setBinaryStream(6, input, (int)(numero.getImg().length));
			} else {
				ps.setObject(6, null);
			}
			
			ps.setInt(7, numero.getIdColeccion());
			ps.setInt(8, numero.getId());
			 
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
	 * Borra el número enviado de la base de datos
	 * @param numero Número a borrar
	 */
	public static void borrarNumero(Numero numero) {
		String borrado = "DELETE FROM numeros WHERE id = ?;";
		
		Connection con = null;
		
		try {
			con = Pool.getConexion();
			PreparedStatement ps = con.prepareStatement(borrado);
			
			ps.setInt(1, numero.getId());
			 
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
	 * Devuelve el número total de cómics disponibles en la base de datos
	 * @return Total de cómics
	 */
	public static int getNumComics() {
		int numComics = 0;
		String consulta = "SELECT COUNT(*) FROM comics.numeros";
		
		try {
			Statement ps = Pool.getConexion().createStatement();
			
			ResultSet rs = ps.executeQuery(consulta);
			
			if (rs.next()) {
				numComics = rs.getInt(1);
			}
			
			rs.close();
			
		} catch (SQLException e) {
			//e.printStackTrace();
		}  finally {
			Pool.Cerrar();
		}				
		
		
		return numComics;
	}
	
	/**
	 * Obtiene el último identificador de cómic disponible en la base de datos
	 * @param con Conexión con la base de datos actual
	 * @return último ID de cómic
	 */
	private static int getUltimoId(Connection con) {
		int id = 0;
		String query = "SELECT MAX(id) from comics.numeros";
		
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

}
