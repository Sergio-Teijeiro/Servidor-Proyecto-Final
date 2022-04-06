package Controlador;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.imageio.ImageIO;

import Modelo.Coleccion;
import Modelo.Numero;

public class gestionConsultas {

	public static Coleccion getColeccionPorNumero(Numero numero) {
		Coleccion coleccion = null;
		String consulta = "SELECT * FROM colecciones WHERE id = ?";
		PreparedStatement ps;
		try {
			ps = Pool.getConexion().prepareStatement(consulta);
			
			ps.setInt(1, numero.getIdColeccion());
			
			ResultSet rs = ps.executeQuery();
			
			if (rs.next()) {
				Blob blob = rs.getBlob("img");
				byte[] data = blob.getBytes(1, (int)blob.length());
				BufferedImage img = null;
				try {
					img = ImageIO.read(new ByteArrayInputStream(data));
				} catch (IOException ex) {
					Logger.getLogger(gestionConsultas.class.getName()).log(Level.SEVERE, null, ex);
				}
				
				coleccion = new Coleccion(rs.getInt(1),rs.getString(1),img);
			}
			
			rs.close();
			
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} finally {
			Pool.Cerrar();
		}
		
		return coleccion;
	}

	public static ArrayList<Numero> cargarComics() {
		ArrayList<Numero> comics = new ArrayList<>();
		String consulta = "SELECT * FROM numeros";
		
		try {
			Statement st = Pool.getConexion().createStatement();
			ResultSet rs = st.executeQuery(consulta);
			
			while (rs.next()) {
				Blob blob = rs.getBlob("img");
				byte[] data = blob.getBytes(1, (int)blob.length());
				BufferedImage img = null;
				try {
					img = ImageIO.read(new ByteArrayInputStream(data));
				} catch (IOException ex) {
					Logger.getLogger(gestionConsultas.class.getName()).log(Level.SEVERE, null, ex);
				}
				
				Numero n = new Numero(rs.getInt(1),rs.getString(2),rs.getDate(3),rs.getString(4),rs.getString(5),rs.getString(6),img,rs.getInt(8));
				
				comics.add(n);
			}
			
			rs.close();
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}  finally {
			Pool.Cerrar();
		}
		
		return comics;
	}

}
