package Controlador;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import Modelo.*;
import net.sf.jasperreports.engine.JasperPrint;

/**
 * Hilo que usa el servidor para atender cada petici�n de un cliente
 * @author admin
 *
 */
public class HiloServidorTrabajo extends Thread {
    Socket socketCliente;
    ArrayList<Socket> listaSockets;

    /**
     * Constructor principal
     * @param skCliente Socket del cliente
     * @param listaSockets Lista de todos los sockets de clientes que ya realizaron alguna petici�n
     */
    HiloServidorTrabajo(Socket skCliente, ArrayList<Socket> listaSockets) {
        this.socketCliente = skCliente;
        this.listaSockets = listaSockets;
    }
    
    @Override
    public void run() {
        try {
            String peticion = "";
            Numero numero, numAux;
            Coleccion coleccion, colAux;

            //Realiza las peticiones mientras se envien
            while (!peticion.equalsIgnoreCase("Fin")) {
                //Creamos los flujos
                InputStream aux = socketCliente.getInputStream();
                DataInputStream flujo_entrada = new DataInputStream(aux);
                OutputStream out = socketCliente.getOutputStream();
                DataOutputStream flujo_salida = new DataOutputStream(out);
                ObjectInputStream objeto_entrada = new ObjectInputStream(aux);
                ObjectOutputStream objeto_salida = new ObjectOutputStream(out);

                peticion = flujo_entrada.readUTF();
                
                switch (peticion) {
                    case "altaNumero": String idioma = flujo_entrada.readUTF();
                    	int offset = (int) objeto_entrada.readObject();
                    	numero = (Numero) objeto_entrada.readObject();
						numAux = gestionConsultas.existeTituloNumero(numero.getTitulo());
                    
						if (numAux != null) {
							if (idioma.equals("es")) {
								flujo_salida.writeUTF("Ya existe un n�mero con ese t�tulo");
							} else {
								flujo_salida.writeUTF("Xa existe un n�mero con ese t�tulo");
							}
						} else {
							gestionNumeros.insertarNumero(numero);
							
							if (idioma.equals("es")) {
								flujo_salida.writeUTF("Se ha insertado correctamente el n�mero "+numero.getTitulo());
							} else {
								flujo_salida.writeUTF("Insertouse correctamente o n�mero "+numero.getTitulo());
							}
							
							ArrayList<Numero> numeros = gestionConsultas.cargarComics(offset);
		                    
                    		objeto_salida.writeObject(numeros);
						}
                        break;
                    case "bajaNumero": idioma = flujo_entrada.readUTF();
                    	offset = (int) objeto_entrada.readObject();
                    	numero = (Numero) objeto_entrada.readObject();
						numAux = gestionConsultas.existeIDNumero(numero.getId());
                    
						if (numAux == null) {
							if (idioma.equals("es")) {
								flujo_salida.writeUTF("No existe ning�n n�mero con ese ID");
							} else {
								flujo_salida.writeUTF("Non existe ning�n n�mero con ese ID");
							}
						} else {
							gestionNumeros.borrarNumero(numero);
							
							if (idioma.equals("es")) {
								flujo_salida.writeUTF("Se ha eliminado correctamente el n�mero "+numero.getTitulo());
							} else {
								flujo_salida.writeUTF("Eliminouse correctamente o n�mero "+numero.getTitulo());
							}
					
							ArrayList<Numero> numeros = gestionConsultas.cargarComics(offset);
                    
							objeto_salida.writeObject(numeros);
						}
                        break; 
                    case "modificarNumero": idioma = flujo_entrada.readUTF();
                    	offset = (int) objeto_entrada.readObject();
                    	numero = (Numero) objeto_entrada.readObject();
						numAux = gestionConsultas.existeIDNumero(numero.getId());
                    
						if (numAux == null) {
							if (idioma.equals("es")) {
								flujo_salida.writeUTF("No existe ning�n n�mero con ese ID");
							} else {
								flujo_salida.writeUTF("Non existe ning�n n�mero con ese ID");
							}
						} else {
							gestionNumeros.modificarNumero(numero);
							
							if (idioma.equals("es")) {
								flujo_salida.writeUTF("Se ha modificado correctamente el n�mero "+numero.getTitulo());
							} else {
								flujo_salida.writeUTF("Modificouse correctamente o n�mero "+numero.getTitulo());
							}
						
							ArrayList<Numero> numeros = gestionConsultas.cargarComics(offset);
	                    
							objeto_salida.writeObject(numeros);
						}
                    	
                        break;         
                    case "colByComic": numero = (Numero) objeto_entrada.readObject();
                    
                    		coleccion = gestionConsultas.getColeccionPorNumero(numero);
					
                    		objeto_salida.writeObject(coleccion);
            			break;  
                    case "cargarComics": offset = (int) objeto_entrada.readObject();
                    		ArrayList<Numero> numeros = gestionConsultas.cargarComics(offset);
                    
                    		objeto_salida.writeObject(numeros);
                    	break;
                    case "cargarComicsPorCol": String nombreColeccion = flujo_entrada.readUTF();
                    	
                    		ArrayList<Numero> numerosComic = gestionConsultas.cargarComicsPorColeccion(nombreColeccion);
                    
            				objeto_salida.writeObject(numerosComic);
            			break;
                    case "cargarComicsPorTitulo": String titulo = flujo_entrada.readUTF();
                	
            				ArrayList<Numero> numerosTitulo = gestionConsultas.cargarComicsPorTitulo(titulo);
            
            				objeto_salida.writeObject(numerosTitulo);
            			break;	
                    case "cargarColecciones": ArrayList<Coleccion> colecciones = gestionConsultas.cargarColecciones();
                    
                    		objeto_salida.writeObject(colecciones);
                    	break;
                    case "altaColeccion": idioma = flujo_entrada.readUTF();
                    	coleccion = (Coleccion) objeto_entrada.readObject();
                    	colAux = gestionConsultas.existeColeccionPorNombre(coleccion.getNombre());
                    
                    	if (colAux != null) {
                    		if (idioma.equals("es")) {
                    			flujo_salida.writeUTF("Ya existe una colecci�n con ese nombre");
                    		} else {
                    			flujo_salida.writeUTF("Xa existe unha colecci�n con ese nome");
                    		}
                    	} else {
                    		gestionColecciones.insertarColeccion(coleccion);
                    		
                    		if (idioma.equals("es")) {
                    			flujo_salida.writeUTF("Se ha insertado correctamente la colecci�n "+coleccion.getNombre());
                    		} else {
                    			flujo_salida.writeUTF("Insertouse correctamente a colecci�n "+coleccion.getNombre());
                    		}

                    		colecciones = gestionConsultas.cargarColecciones();
	                    
                    		objeto_salida.writeObject(colecciones);
                    	}
                    	break;
                    case "modificarColeccion": idioma = flujo_entrada.readUTF();
                    	coleccion = (Coleccion) objeto_entrada.readObject();
						colAux = gestionConsultas.existeIDColeccion(coleccion.getId());
	                    
						if (colAux == null) {
							if (idioma.equals("es")) {
								flujo_salida.writeUTF("No existe ninguna colecci�n con ese ID");
							} else {
								flujo_salida.writeUTF("Non existe ningunha colecci�n con ese ID");
							}
						} else {
							gestionColecciones.modificarColeccion(coleccion);
							
							if (idioma.equals("es")) {
								flujo_salida.writeUTF("Se ha modificado correctamente la colecci�n "+coleccion.getNombre());
							} else {
								flujo_salida.writeUTF("Modificouse correctamente a colecci�n "+coleccion.getNombre());
							}
						
							colecciones = gestionConsultas.cargarColecciones();
	                    
							objeto_salida.writeObject(colecciones);
						}
                    	break;
                    case "bajaColeccion": idioma = flujo_entrada.readUTF();
                    	coleccion = (Coleccion) objeto_entrada.readObject();
						colAux = gestionConsultas.existeIDColeccion(coleccion.getId());
	                    
						if (colAux == null) {
							if (idioma.equals("es")) {
								flujo_salida.writeUTF("No existe ninguna colecci�n con ese ID");
							} else {
								flujo_salida.writeUTF("Non existe ningunha colecci�n con ese ID");
							}
						} else {
							ArrayList<Numero> comics = gestionConsultas.buscarComicsPorColeccion(coleccion);
							
							if (!comics.isEmpty()) {
								if (idioma.equals("es")) {
									flujo_salida.writeUTF("Hay n�meros relacionados");
								} else {
									flujo_salida.writeUTF("Hai n�meros relacionados");
								}
								
								objeto_salida.writeObject(comics);
							} else {
								gestionColecciones.borrarColeccion(coleccion);
								
								if (idioma.equals("es")) {
									flujo_salida.writeUTF("Se ha eliminado correctamente la colecci�n "+coleccion.getNombre());
								} else {
									flujo_salida.writeUTF("Eliminouse correctamente a colecci�n "+coleccion.getNombre());
								}
						
								colecciones = gestionConsultas.cargarColecciones();
		                
								objeto_salida.writeObject(colecciones);
							}
						}
                    	break;
                    case "bajaColeccionYNumeros": idioma = flujo_entrada.readUTF();
                    	coleccion = (Coleccion) objeto_entrada.readObject();
                    	ArrayList<Numero> comicsRelacionados = (ArrayList<Numero>) objeto_entrada.readObject();
							
                    	gestionColecciones.borrarColeccionConNumeros(coleccion,comicsRelacionados);
                    	
                    	if (idioma.equals("es")) {
                    		flujo_salida.writeUTF("Se ha eliminado correctamente la colecci�n "+coleccion.getNombre() + " y sus n�meros");
                    	} else {
                    		flujo_salida.writeUTF("Eliminouse correctamente a colecci�n "+coleccion.getNombre() + " e os seus n�meros");
                    	}

                    	colecciones = gestionConsultas.cargarColecciones();

                    	objeto_salida.writeObject(colecciones);
                    	break;
                    case "informeColecciones": InputStream is=this.getClass().getResourceAsStream("/plantillas/informeColecciones.jrxml");
                    
                    	JasperPrint informe = generarInformes.generarInformeColecciones(is);
                    	
                    	objeto_salida.writeObject(informe);
                    	break;
                    case "informeColPorNombre": coleccion = (Coleccion) objeto_entrada.readObject();
                    	is=this.getClass().getResourceAsStream("/plantillas/informeColPorNombre.jrxml");
                    
                    	informe = generarInformes.generarInformeColPorNombre(is,coleccion);
                	
                    	objeto_salida.writeObject(informe);
                    	break;
                    case "informeComics": offset = (int) objeto_entrada.readObject();
                    	is=this.getClass().getResourceAsStream("/plantillas/informeComics.jrxml");
	                
	                	informe = generarInformes.generarInformeComics(is,offset);
	            	
	                	objeto_salida.writeObject(informe);
	                	break;
                    case "informeComicsPorCol": coleccion = (Coleccion) objeto_entrada.readObject();
	                	is=this.getClass().getResourceAsStream("/plantillas/informeComicsPorCol.jrxml");
	                
	                	informe = generarInformes.generarInformeComicsPorCol(is,coleccion);
	            	
	                	objeto_salida.writeObject(informe);
	                	break;
                    case "getNumeroComics": int numComics = gestionNumeros.getNumComics();
                    
                    	objeto_salida.writeObject(numComics);
                    	break;
                    default:
                        break;
                }
            }

            // Se cierra la conexi�n
            socketCliente.close();

        } catch (IOException ex) {
            //Logger.getLogger(Servidor.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }    
}
