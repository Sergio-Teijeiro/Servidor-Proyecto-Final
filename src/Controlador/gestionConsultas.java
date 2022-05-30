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

/**
 * Clase encargada de gestionar las diferentes consultas que realiza el servidor a la base de datos
 * @author admin
 *
 */
public class gestionConsultas {

	/**
	 * Obtiene la colecci�n cuyo identificador coincida con el identificador de colecci�n del n�mero enviado
	 * @param numero N�mero enviado, necesario para obtener el identificador de colecci�n
	 * @return Colecci�n correspondiente
	 */
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

	/**
	 * Obtiene una lista de c�mics en un determinado rango, de 100 en 100
	 * @param offset Rango en el que se empiezan a recuperar los c�mics
	 * @return Lista de c�mics correspondiente
	 */
	public static ArrayList<Numero> cargarComics(int offset) {
		ArrayList<Numero> comics = new ArrayList<>();
		String consulta = "SELECT * FROM numeros ORDER BY titulo LIMIT "+offset+",100";
		
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

	/**
	 * Obtiene una lista de c�mics de la colecci�n con el nombre enviado
	 * @param nombreColeccion Nombre de la colecci�n a buscar
	 * @return Lista de c�mics correspondiente
	 */
	public static ArrayList<Numero> cargarComicsPorColeccion(String nombreColeccion) {
		ArrayList<Numero> comics = new ArrayList<>();
		String consulta = "SELECT * FROM comics.numeros\n"
				+ "WHERE id_coleccion IN (SELECT id FROM colecciones\n"
				+ "						WHERE nombre LIKE '%"+nombreColeccion+"%') ORDER BY titulo;";
		
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

	/**
	 * Obtiene una lista de c�mics cuyo t�tulo contenga la cadena enviada
	 * @param titulo Cadena enviada para comparar con el t�tulo
	 * @return Lista de c�mics correspondiente
	 */
	public static ArrayList<Numero> cargarComicsPorTitulo(String titulo) {
		ArrayList<Numero> comics = new ArrayList<>();
		String consulta = "SELECT * FROM comics.numeros\n"
				+ "WHERE titulo LIKE '%"+titulo+"%' ORDER BY titulo;";
		
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

	/**
	 * Obtiene todas las colecciones correspondientes ordenadas alfab�ticamente por nombre
	 * @return Lista de todas las colecciones
	 */
	public static ArrayList<Coleccion> cargarColecciones() {
		ArrayList<Coleccion> colecciones = new ArrayList<>();
		String consulta = "SELECT * FROM comics.colecciones\n"
				+ "ORDER BY nombre;";
		
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

	/**
	 * Devuelve el n�mero con el t�tulo enviado
	 * @param titulo T�tulo del c�mic a buscar
	 * @return N�mero correspondiente
	 */
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

	/**
	 * Devuelve el n�mero con el identificador enviado
	 * @param id Identificador del c�mic a buscar
	 * @return N�mero correspondiente
	 */
	public static Numero existeIDNumero(int id) {
		Numero numero = null;
		String consulta = "SELECT * FROM comics.numeros\n"
				+ "WHERE id = ?";
		
		try {
			PreparedStatement ps = Pool.getConexion().prepareStatement(consulta);
			
			ps.setInt(1, id);
			
			ResultSet rs = ps.executeQuery();
			
			if (rs.next()) {
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

	/**
	 * Devuelve la colecci�n con el nombre enviado
	 * @param nombre Nombre de la colecci�n a buscar
	 * @return Colecci�n correspondiente
	 */
	public static Coleccion existeColeccionPorNombre(String nombre) {
		Coleccion coleccion = null;
		String consulta = "SELECT * FROM comics.colecciones\n"
				+ "WHERE nombre = ?;";
		
		try {
			PreparedStatement ps = Pool.getConexion().prepareStatement(consulta);
			
			ps.setString(1, nombre);
			
			ResultSet rs = ps.executeQuery();
			
			if (rs.next()) {
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
				
				coleccion = new Coleccion(rs.getInt(1),rs.getString(2),data);
			}
			
			rs.close();
			
		} catch (SQLException e) {
			e.printStackTrace();
		}  finally {
			Pool.Cerrar();
		}		
		
		return coleccion;
	}

	/**
	 * Devuelve la colecci�n con el identificador enviado
	 * @param id Identificador de la colecci�n a buscar
	 * @return Colecci�n correspondiente
	 */
	public static Coleccion existeIDColeccion(int id) {
		Coleccion coleccion = null;
		String consulta = "SELECT * FROM comics.colecciones\n"
				+ "WHERE id = ?;";
		
		try {
			PreparedStatement ps = Pool.getConexion().prepareStatement(consulta);
			
			ps.setInt(1, id);
			
			ResultSet rs = ps.executeQuery();
			
			if (rs.next()) {
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
				
				coleccion = new Coleccion(rs.getInt(1),rs.getString(2),data);
			}
			
			rs.close();
			
		} catch (SQLException e) {
			e.printStackTrace();
		}  finally {
			Pool.Cerrar();
		}		
		
		return coleccion;
	}

	/**
	 * Obtiene una lista de los c�mics de la colecci�n especificada, ordenados alfab�ticamente por t�tulo
	 * @param coleccion Colecci�n a buscar
	 * @return Lista de c�mics correspondiente
	 */
	public static ArrayList<Numero> buscarComicsPorColeccion(Coleccion coleccion) {
		ArrayList<Numero> comics = new ArrayList<>();
		String consulta = "SELECT * FROM comics.numeros\n"
				+ "WHERE id_coleccion = "+coleccion.getId()+" ORDER BY titulo;";
		
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

}
