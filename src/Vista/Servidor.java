package Vista;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.*;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import Controlador.Pool;
import javax.swing.JButton;
import javax.swing.GroupLayout.Alignment;
import javax.swing.ImageIcon;
import javax.imageio.ImageIO;
import javax.swing.GroupLayout;
import javax.swing.LayoutStyle.ComponentPlacement;

public class Servidor extends JFrame {
	
	ServerSocket skServidor;
    ArrayList<Socket> listaSockets = new ArrayList<>();
    private javax.swing.JButton btnDetener;
    private javax.swing.JButton btnIniciar;
    private javax.swing.JLabel lblEstadoPuerto;
    private javax.swing.JLabel lblPuerto;
    private javax.swing.JTextField txtPuerto;

	/**
	 * Launch the application.
	 */
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

	/**
	 * Create the frame.
	 */
	public Servidor() {
		try {
			Pool.IniciaPool();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//setBounds(100, 100, 450, 300);
		
        lblEstadoPuerto = new javax.swing.JLabel();
        lblPuerto = new javax.swing.JLabel();
        txtPuerto = new javax.swing.JTextField();
        btnIniciar = new javax.swing.JButton();
        btnDetener = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("SERVIDOR");

        lblEstadoPuerto.setFont(new java.awt.Font("Tahoma", 0, 36)); // NOI18N
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
                    JOptionPane.showMessageDialog(rootPane, "El servidor ya está iniciado", "Error", JOptionPane.ERROR_MESSAGE);
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
		
		/*try {
			Statement st = Pool.getConexion().createStatement();
			ResultSet rs = st.executeQuery("SELECT * FROM numeros WHERE id = 13");
			
			while (rs.next()) {
				Blob blob = rs.getBlob("img");
				byte[] data = blob.getBytes(1, (int)blob.length());
				BufferedImage img = null;
				try {
					img = ImageIO.read(new ByteArrayInputStream(data));
				} catch (IOException ex) {
					Logger.getLogger(Servidor.class.getName()).log(Level.SEVERE, null, ex);
				}
				
				ImageIcon iconoEscala = new ImageIcon(img.getScaledInstance(btnPrueba.getWidth(),
						btnPrueba.getHeight(), java.awt.Image.SCALE_FAST));
				btnPrueba.setIcon(iconoEscala);
			}
			
			st.close();
			rs.close();
			
			Pool.Cerrar();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
		
	}
	
	private void btnDetenerActionPerformed(java.awt.event.ActionEvent evt) {
        if (skServidor == null) {
            JOptionPane.showMessageDialog(rootPane, "El servidor no se ha iniciado", "Error", JOptionPane.ERROR_MESSAGE);
        } else if (skServidor.isClosed()) {
            JOptionPane.showMessageDialog(rootPane, "El servidor ya está cerrado", "Error", JOptionPane.ERROR_MESSAGE);
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
