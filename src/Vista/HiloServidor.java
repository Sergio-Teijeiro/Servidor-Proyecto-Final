package Vista;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class HiloServidor extends Thread {
    ServerSocket socketServidor;
    ArrayList<Socket> listaSockets;

    HiloServidor(ServerSocket skServidor, ArrayList<Socket> listaSockets) {
        this.socketServidor = skServidor;
        this.listaSockets = listaSockets;
    }
    
    @Override
    public void run() {
        
        while (!socketServidor.isClosed()) {
            try {
                //Se crea una nueva peticion
                Socket skCliente = socketServidor.accept();
                
                //Se inserta el cliente en la lista de clientes conectados
                listaSockets.add(skCliente);
                
                //Atendemos al cliente mediante un thread
                new HiloServidorTrabajo(skCliente,listaSockets).start();

            } catch (IOException ex) {
                //Logger.getLogger(HiloServidor.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }     
}
