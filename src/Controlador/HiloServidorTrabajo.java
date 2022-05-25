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

public class HiloServidorTrabajo extends Thread {
    Socket socketCliente;
    ArrayList<Socket> listaSockets;

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
                    case "altaNumero": int offset = (int) objeto_entrada.readObject();
                    	numero = (Numero) objeto_entrada.readObject();
						numAux = gestionConsultas.existeTituloNumero(numero.getTitulo());
                    
						if (numAux != null) {
							flujo_salida.writeUTF("Ya existe un n�mero con ese t�tulo");
						} else {
							gestionNumeros.insertarNumero(numero);
							flujo_salida.writeUTF("Se ha insertado correctamente el n�mero "+numero.getTitulo());
							
							ArrayList<Numero> numeros = gestionConsultas.cargarComics(offset);
		                    
                    		objeto_salida.writeObject(numeros);
						}
                        break;
                    case "bajaNumero": offset = (int) objeto_entrada.readObject();
                    	numero = (Numero) objeto_entrada.readObject();
						numAux = gestionConsultas.existeIDNumero(numero.getId());
                    
						if (numAux == null) {
							flujo_salida.writeUTF("No existe ning�n n�mero con ese ID");
						} else {
							gestionNumeros.borrarNumero(numero);
							flujo_salida.writeUTF("Se ha eliminado correctamente el n�mero "+numero.getTitulo());
					
							ArrayList<Numero> numeros = gestionConsultas.cargarComics(offset);
                    
							objeto_salida.writeObject(numeros);
						}
                        break; 
                    case "modificarNumero": offset = (int) objeto_entrada.readObject();
                    	numero = (Numero) objeto_entrada.readObject();
						numAux = gestionConsultas.existeIDNumero(numero.getId());
                    
						if (numAux == null) {
							flujo_salida.writeUTF("No existe ning�n n�mero con ese ID");
						} else {
							gestionNumeros.modificarNumero(numero);
							flujo_salida.writeUTF("Se ha modificado correctamente el n�mero "+numero.getTitulo());
						
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
                    case "altaColeccion": coleccion = (Coleccion) objeto_entrada.readObject();
                    	colAux = gestionConsultas.existeColeccionPorNombre(coleccion.getNombre());
                    
                    	if (colAux != null) {
                    		flujo_salida.writeUTF("Ya existe una colecci�n con ese nombre");
                    	} else {
                    		gestionColecciones.insertarColeccion(coleccion);
                    		flujo_salida.writeUTF("Se ha insertado correctamente la colecci�n "+coleccion.getNombre());

                    		colecciones = gestionConsultas.cargarColecciones();
	                    
                    		objeto_salida.writeObject(colecciones);
                    	}
                    	break;
                    case "modificarColeccion": coleccion = (Coleccion) objeto_entrada.readObject();
						colAux = gestionConsultas.existeIDColeccion(coleccion.getId());
	                    
						if (colAux == null) {
							flujo_salida.writeUTF("No existe ninguna colecci�n con ese ID");
						} else {
							gestionColecciones.modificarColeccion(coleccion);
							flujo_salida.writeUTF("Se ha modificado correctamente la colecci�n "+coleccion.getNombre());
						
							colecciones = gestionConsultas.cargarColecciones();
	                    
							objeto_salida.writeObject(colecciones);
						}
                    	break;
                    case "bajaColeccion": coleccion = (Coleccion) objeto_entrada.readObject();
						colAux = gestionConsultas.existeIDColeccion(coleccion.getId());
	                    
						if (colAux == null) {
							flujo_salida.writeUTF("No existe ninguna colecci�n con ese ID");
						} else {
							ArrayList<Numero> comics = gestionConsultas.buscarComicsPorColeccion(coleccion);
							
							if (!comics.isEmpty()) {
								flujo_salida.writeUTF("Hay n�meros relacionados");
								objeto_salida.writeObject(comics);
							} else {
								gestionColecciones.borrarColeccion(coleccion);
								flujo_salida.writeUTF("Se ha eliminado correctamente la colecci�n "+coleccion.getNombre());
						
								colecciones = gestionConsultas.cargarColecciones();
		                
								objeto_salida.writeObject(colecciones);
							}
						}
                    	break;
                    case "bajaColeccionYNumeros": coleccion = (Coleccion) objeto_entrada.readObject();
                    	ArrayList<Numero> comicsRelacionados = (ArrayList<Numero>) objeto_entrada.readObject();
							
                    	gestionColecciones.borrarColeccionConNumeros(coleccion,comicsRelacionados);
                    	flujo_salida.writeUTF("Se ha eliminado correctamente la colecci�n "+coleccion.getNombre() + " y sus n�meros");

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
                    case "informeComics": is=this.getClass().getResourceAsStream("/plantillas/informeComics.jrxml");
	                
	                	informe = generarInformes.generarInformeComics(is);
	            	
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
