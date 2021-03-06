package Vista;

import java.awt.Color;
import java.awt.EventQueue;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URL;
import java.sql.*;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import Controlador.*;
import Modelo.*;

import org.apache.commons.io.IOUtils;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Interfaz gráfica del servidor. Muestra su estado, el puerto al que se conecta y permite conectarlo o desconectarlo. Lo primero que hace es conectar con la base de datos.
 * @author admin
 *
 */
public class Servidor extends JFrame {
	
	ServerSocket skServidor;
    ArrayList<Socket> listaSockets = new ArrayList<>();
    private javax.swing.JButton btnDetener;
    private javax.swing.JButton btnIniciar;
    private javax.swing.JLabel lblEstadoPuerto;
    private javax.swing.JLabel lblPuerto;
    private javax.swing.JTextField txtPuerto;

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Servidor frame = new Servidor();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public Servidor() {
		try {
			Pool.IniciaPool();
		} catch (SQLException e) {
			//e.printStackTrace();
		}
		
        lblEstadoPuerto = new javax.swing.JLabel();
        lblPuerto = new javax.swing.JLabel();
        txtPuerto = new javax.swing.JTextField();
        btnIniciar = new javax.swing.JButton();
        btnDetener = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("SERVIDOR");

        lblEstadoPuerto.setFont(new java.awt.Font("Tahoma", 0, 36));
        lblEstadoPuerto.setForeground(new java.awt.Color(255, 0, 0));
        lblEstadoPuerto.setText("APAGADO");

        lblPuerto.setText("Puerto");

        txtPuerto.setEditable(false);
        txtPuerto.setText("2000");

        btnIniciar.setText("Iniciar");
        btnIniciar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnIniciarActionPerformed(evt);
            }
        });

        btnDetener.setText("Detener");
        btnDetener.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDetenerActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(171, 171, 171)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                            .addComponent(lblPuerto)
                            .addGap(40, 40, 40)
                            .addComponent(txtPuerto, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addComponent(lblEstadoPuerto, javax.swing.GroupLayout.PREFERRED_SIZE, 222, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(btnIniciar)
                        .addGap(94, 94, 94)
                        .addComponent(btnDetener)
                        .addGap(25, 25, 25)))
                .addContainerGap(200, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblEstadoPuerto, javax.swing.GroupLayout.PREFERRED_SIZE, 57, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblPuerto)
                    .addComponent(txtPuerto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(82, 82, 82)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnIniciar)
                    .addComponent(btnDetener))
                .addContainerGap(135, Short.MAX_VALUE))
        );


        pack();
        setLocationRelativeTo(null);
	}
	
	private void btnIniciarActionPerformed(java.awt.event.ActionEvent evt) {
        //Inicia la escucha en el puerto indicado

        if (txtPuerto.getText().isBlank()) {
            JOptionPane.showMessageDialog(rootPane, "Debes indicar un puerto", "Error", JOptionPane.ERROR_MESSAGE);
        } else {
            if (skServidor != null) {
                if (!skServidor.isClosed()) {
                    JOptionPane.showMessageDialog(rootPane, "El servidor ya est� iniciado", "Error", JOptionPane.ERROR_MESSAGE);
                } else {
                    lblEstadoPuerto.setText("ENCENDIDO");
                    lblEstadoPuerto.setForeground(Color.GREEN);

                    int puerto = Integer.valueOf(txtPuerto.getText().trim());

                    try {
                        skServidor = new ServerSocket(puerto);
                        new HiloServidor(skServidor, listaSockets).start();

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            } else {
                lblEstadoPuerto.setText("ENCENDIDO");
                lblEstadoPuerto.setForeground(Color.GREEN);

                int puerto = Integer.valueOf(txtPuerto.getText().trim());

                try {
                    skServidor = new ServerSocket(puerto);

                    new HiloServidor(skServidor, listaSockets).start();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        }
		
	}
	
	private void btnDetenerActionPerformed(java.awt.event.ActionEvent evt) {
        if (skServidor == null) {
            JOptionPane.showMessageDialog(rootPane, "El servidor no se ha iniciado", "Error", JOptionPane.ERROR_MESSAGE);
        } else if (skServidor.isClosed()) {
            JOptionPane.showMessageDialog(rootPane, "El servidor ya est� cerrado", "Error", JOptionPane.ERROR_MESSAGE);
        } else {
            try {
                
                //se cierra el servidor y todos sus clientes
                skServidor.close();

                for (Socket s : listaSockets) {
                    s.close();
                }

                lblEstadoPuerto.setText("APAGADO");
                lblEstadoPuerto.setForeground(new java.awt.Color(255, 0, 0));
            } catch (IOException ex) {
                Logger.getLogger(Servidor.class.getName()).log(Level.SEVERE, null, ex);
            }
        } 		
	}
	
}

