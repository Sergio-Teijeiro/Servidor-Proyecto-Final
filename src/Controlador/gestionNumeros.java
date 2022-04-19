package Controlador;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import Modelo.Numero;

public class gestionNumeros {

	public static void insertarNumero(Numero numero) {
		String insercion = "INSERT INTO numeros (titulo,fecha_adquisicion,tapa,estado,resenha,img,id_coleccion VALUES (?,?,?,?,?,?,?);)";
		InputStream input = new ByteArrayInputStream(numero.getImg());
		
		
		try {
			PreparedStatement ps = Pool.getConexion().prepareStatement(insercion);
			
			ps.setString(1, numero.getTitulo());
			ps.setDate(2, numero.getFechaAdquisicion());
			ps.setString(3, numero.getTapa());
			ps.setString(4, numero.getEstado());
			ps.setString(5, numero.getResenha());
			ps.setBinaryStream(5, input, (int)(numero.getImg().length));
			ps.setInt(6, numero.getIdColeccion());
			 
			ps.executeUpdate();
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}  finally {
			Pool.Cerrar();
		}
		
		
	}

}
