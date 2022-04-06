package Vista;

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
                    case "alta": 
                        break;
                    case "baja":
                        break; 
                    case "modificar":
                        break; 
                    case "consultar":
                        break;                      
                    default:
                        break;
                }
            }

            // Se cierra la conexión
            socketCliente.close();

        } catch (IOException ex) {
            Logger.getLogger(Servidor.class.getName()).log(Level.SEVERE, null, ex);
        }
    }    
}
