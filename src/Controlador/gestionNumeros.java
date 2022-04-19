package Controlador;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.sql.*;

import Modelo.Numero;

public class gestionNumeros {

	public static void insertarNumero(Numero numero) {
		//String insercion = "INSERT INTO numeros (titulo,fecha_adquisicion,tapa,estado,resenha,img,id_coleccion) VALUES (?,?,?,?,?,?,?);";
		String insercion = "INSERT INTO numeros (titulo,fecha_adquisicion,tapa,estado,resenha,id_coleccion) VALUES (?,?,?,?,?,?);";
		//InputStream input = new ByteArrayInputStream(numero.getImg());
		Connection con = null;
		
		try {
			con = Pool.getConexion();
			PreparedStatement ps = con.prepareStatement(insercion);
			
			ps.setString(1, numero.getTitulo());
			ps.setDate(2, numero.getFechaAdquisicion());
			ps.setString(3, numero.getTapa());
			ps.setString(4, numero.getEstado());
			ps.setString(5, numero.getResenha());
			//ps.setBinaryStream(6, input, (int)(numero.getImg().length));
			ps.setInt(6, numero.getIdColeccion());
			 
			ps.executeUpdate();
			
			con.commit();
			
		} catch (SQLException e) {
			try {
				con.rollback();
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			e.printStackTrace();
		}  finally {
			Pool.Cerrar();
		}
		
		
	}

}
