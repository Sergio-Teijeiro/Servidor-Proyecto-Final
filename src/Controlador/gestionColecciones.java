package Controlador;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.sql.*;

import Modelo.Coleccion;

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
		String insercion = "UPDATE colecciones SET nombre = ?, img = ? WHERE id = ?;";
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

}
