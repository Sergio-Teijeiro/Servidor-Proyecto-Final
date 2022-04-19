package Controlador;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.imageio.ImageIO;

import Modelo.*;

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
				
				coleccion = new Coleccion(rs.getInt(1),rs.getString(2),data);
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
				byte[] data = null;
				
				if (blob != null) {
					data = blob.getBytes(1, (int)blob.length());
					
					BufferedImage img = null;
					try {
						img = ImageIO.read(new ByteArrayInputStream(data));
					} catch (IOException ex) {
						Logger.getLogger(gestionConsultas.class.getName()).log(Level.SEVERE, null, ex);
					}
				}

				Numero n = new Numero(rs.getInt(1),rs.getString(2),rs.getDate(3),rs.getString(4),rs.getString(5),rs.getString(6),data,rs.getInt(8));
				
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

	public static ArrayList<Numero> cargarComicsPorColeccion(String nombreColeccion) {
		ArrayList<Numero> comics = new ArrayList<>();
		String consulta = "SELECT * FROM comics.numeros\n"
				+ "WHERE id_coleccion IN (SELECT id FROM colecciones\n"
				+ "						WHERE nombre LIKE '%"+nombreColeccion+"%');";
		
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
				
				Numero n = new Numero(rs.getInt(1),rs.getString(2),rs.getDate(3),rs.getString(4),rs.getString(5),rs.getString(6),data,rs.getInt(8));
				
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

	public static ArrayList<Numero> cargarComicsPorTitulo(String titulo) {
		ArrayList<Numero> comics = new ArrayList<>();
		String consulta = "SELECT * FROM comics.numeros\n"
				+ "WHERE titulo LIKE '%"+titulo+"%';";
		
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
				
				Numero n = new Numero(rs.getInt(1),rs.getString(2),rs.getDate(3),rs.getString(4),rs.getString(5),rs.getString(6),data,rs.getInt(8));
				
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

	public static ArrayList<Coleccion> cargarColecciones() {
		ArrayList<Coleccion> colecciones = new ArrayList<>();
		String consulta = "SELECT * FROM comics.colecciones\n"
				+ "ORDER BY nombre;";
		
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
				
				Coleccion c = new Coleccion(rs.getInt(1),rs.getString(2),data);
				
				colecciones.add(c);
			}
			
			rs.close();
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}  finally {
			Pool.Cerrar();
		}
		
		return colecciones;
	}

	public static Numero existeTituloNumero(String titulo) {
		Numero numero = null;
		String consulta = "SELECT * FROM comics.numeros\n"
				+ "WHERE titulo = ?";
		
		try {
			PreparedStatement ps = Pool.getConexion().prepareStatement(consulta);
			
			ps.setString(1, titulo);
			
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
				
				numero = new Numero(rs.getInt(1),rs.getString(2),rs.getDate(3),rs.getString(4),rs.getString(5),rs.getString(6),data,rs.getInt(8));
			}
			
			rs.close();
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}  finally {
			Pool.Cerrar();
		}		
		
		return numero;
	}

}
