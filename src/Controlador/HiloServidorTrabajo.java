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

import Modelo.Coleccion;
import Modelo.Numero;

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
                    case "altaNumero": numero = (Numero) objeto_entrada.readObject();
						numAux = gestionConsultas.existeTituloNumero(numero.getTitulo());
                    
						if (numAux != null) {
							flujo_salida.writeUTF("Ya existe un número con ese título");
						} else {
							gestionNumeros.insertarNumero(numero);
							flujo_salida.writeUTF("Se ha insertado correctamente el número "+numero.getTitulo());
							
							ArrayList<Numero> numeros = gestionConsultas.cargarComics();
		                    
                    		objeto_salida.writeObject(numeros);
						}
                        break;
                    case "bajaNumero": numero = (Numero) objeto_entrada.readObject();
						numAux = gestionConsultas.existeIDNumero(numero.getId());
                    
						if (numAux == null) {
							flujo_salida.writeUTF("No existe ningún número con ese ID");
						} else {
							gestionNumeros.borrarNumero(numero);
							flujo_salida.writeUTF("Se ha eliminado correctamente el número "+numero.getTitulo());
					
							ArrayList<Numero> numeros = gestionConsultas.cargarComics();
                    
							objeto_salida.writeObject(numeros);
						}
                        break; 
                    case "modificarNumero": numero = (Numero) objeto_entrada.readObject();
						numAux = gestionConsultas.existeIDNumero(numero.getId());
                    
						if (numAux == null) {
							flujo_salida.writeUTF("No existe ningún número con ese ID");
						} else {
							gestionNumeros.modificarNumero(numero);
							flujo_salida.writeUTF("Se ha modificado correctamente el número "+numero.getTitulo());
						
							ArrayList<Numero> numeros = gestionConsultas.cargarComics();
	                    
							objeto_salida.writeObject(numeros);
						}
                    	
                        break;         
                    case "colByComic": numero = (Numero) objeto_entrada.readObject();
                    
                    		coleccion = gestionConsultas.getColeccionPorNumero(numero);
					
                    		objeto_salida.writeObject(coleccion);
            			break;  
                    case "cargarComics": ArrayList<Numero> numeros = gestionConsultas.cargarComics();
                    
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
                    		flujo_salida.writeUTF("Ya existe una colección con ese nombre");
                    	} else {
                    		gestionColecciones.insertarColeccion(coleccion);
                    		flujo_salida.writeUTF("Se ha insertado correctamente la colección "+coleccion.getNombre());

                    		colecciones = gestionConsultas.cargarColecciones();
	                    
                    		objeto_salida.writeObject(colecciones);
                    	}
                    	break;
                    case "modificarColeccion": coleccion = (Coleccion) objeto_entrada.readObject();
						colAux = gestionConsultas.existeIDColeccion(coleccion.getId());
	                    
						if (colAux == null) {
							flujo_salida.writeUTF("No existe ninguna colección con ese ID");
						} else {
							gestionColecciones.modificarColeccion(coleccion);
							flujo_salida.writeUTF("Se ha modificado correctamente la colección "+coleccion.getNombre());
						
							colecciones = gestionConsultas.cargarColecciones();
	                    
							objeto_salida.writeObject(colecciones);
						}
                    	break;
                    case "bajaColeccion": coleccion = (Coleccion) objeto_entrada.readObject();
						colAux = gestionConsultas.existeIDColeccion(coleccion.getId());
	                    
						if (colAux == null) {
							flujo_salida.writeUTF("No existe ninguna colección con ese ID");
						} else {
							gestionColecciones.borrarColeccion(coleccion);
							flujo_salida.writeUTF("Se ha eliminado correctamente la colección "+coleccion.getNombre());
					
							colecciones = gestionConsultas.cargarColecciones();
	                
							objeto_salida.writeObject(colecciones);
						}
                    	break;
                    default:
                        break;
                }
            }

            // Se cierra la conexión
            socketCliente.close();

        } catch (IOException ex) {
            //Logger.getLogger(Servidor.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }    
}
