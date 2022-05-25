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
        
        //insertarComics();
	}
	
	private void insertarColeccion() {
		try {
            InputStream is = new FileInputStream("C:\\Users\\admin\\Downloads\\response.json");
            try {
				String jsonTxt = IOUtils.toString(is, "UTF-8");
				JSONObject jsonObj = new JSONObject(jsonTxt);
				JSONObject obj = (JSONObject) jsonObj.get("data");
				
				JSONArray array = obj.getJSONArray("results");
				
				for (int i=0;i<array.length();i++) {
					JSONObject col = array.getJSONObject(i);
					
					if (col.getString("title").equals("The Invincible Iron Man (2004 - 2007)")) {
						System.out.println(col.getString("title"));
						
						JSONObject thumbnail = col.getJSONObject("thumbnail");
						
						System.out.println(thumbnail.getString("path")+"."+thumbnail.getString("extension"));
						
						URL url = new URL(thumbnail.getString("path")+"."+thumbnail.getString("extension"));
						InputStream in = url.openStream();
						
						byte[] imgBytes = IOUtils.toByteArray(in);
						
						Coleccion cole = new Coleccion (0,col.getString("title"),imgBytes);
						
						gestionColecciones.insertarColeccion(cole);
						
						break;
					}
				}
				
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
	}

	private void insertarComics() {
		try {
            InputStream is = new FileInputStream("C:\\Users\\admin\\Downloads\\response.json");
            try {
				String jsonTxt = IOUtils.toString(is, "UTF-8");
				JSONObject jsonObj = new JSONObject(jsonTxt);
				JSONObject obj = (JSONObject) jsonObj.get("data");
				
				JSONArray array = obj.getJSONArray("results");
				String[] tapas = {"Dura","Blanda"};
				String[] estados = {"Últimas páginas dobladas","Ya vino con una página gastada","Uso ligero","Tres páginas con manchas",
						"Tiene un par de páginas rotas por las esquinas","Tapas como del primer día y contenido bien",
						"Tapas arrugadas","Solo tiene una página mal","Solo tiene mal la última página","Sin ningún defecto","Sin defectos",
						"Segunda mano","Regular","Recién comprado","Pésimo","Pérdida de color en algunas páginas","Página 3 rota","Página 2 un poco rota","Probablemente con poco uso",
						"Primeras páginas ligeramente gastadas","Perfecto estado","Perfecto","Perdió un poco los colores","Nuevo","No tiene todas las páginas",
						"No tiene defectos","No muy bueno","No está mal","Muy usado","Muy bueno, materiales muy resistentes","Muy bueno, de los mejores","Muy bueno",
						"Muy buena calidad","Muy bien cuidado","Muy bien conservado","Malo","Lomos ligeramente gastados","Lomos gastados","Lomos descoloridos",
						"Ligero rasguño","Ligero desgaste en algunas páginas","Ligeramente gastado","Ligera mancha en portada","Genial","Gastado","Fatal",
						"Excelente para el uso dado","Excelente","Está bastante bien","El mejor de la colección","Con manchas al principio","Como nuevo",
						"Calidad excelente","Buenísimo","Bueno, plastificado","Bueno, con tapas muy resistentes","Bueno","Bordes gastados por un ligero uso","Bien cuidado",
						"Bastante bueno, aunque se nota que tiene uso","Bastante bueno","Bastante bien cuidado","Bastante bien conservado a pesar del uso","Bastante aceptable","Aceptable"};
				byte[] imgBytes = null;
				String resenha = null, titulo;
				ArrayList<String> listaTitulos = new ArrayList<String>();
				int contador = 2;
				Date fecha;
				
				for (int i=0;i<array.length();i++) {
					JSONObject col = array.getJSONObject(i);
					
					if (!col.isNull("title")) {
						if (col.getString("title").equals("")) {
							titulo = "";
						} else {
							titulo = col.getString("title");
						}
					} else {
						titulo = "";
					}
					
					if (listaTitulos.contains(titulo)) {
						titulo += " " + contador;
						listaTitulos.add(titulo);
						contador++;
					} else {
						listaTitulos.add(titulo);
					}
					
					System.out.println((i+1)+" - "+titulo);
					
					if (col.getString("modified").startsWith("-000")) {
						fecha = new java.sql.Date(Calendar.getInstance().getTime().getTime());
					} else {
						fecha = Date.valueOf(col.getString("modified").substring(0, 10));
					}
					
					if (!col.isNull("description")) {
						if (col.getString("description").equals("") ) {
							resenha = null;
						} else {
							resenha = col.getString("description");
						}
						
					} else {
						resenha = null;
					}
					
					String tapa = tapas[(int) (Math.random() * tapas.length)];
					String estado = estados[(int) (Math.random() * estados.length)];
					
					JSONObject thumbnail = col.getJSONObject("thumbnail");
					
					if (!thumbnail.getString("path").endsWith("image_not_available")) {
						URL url = new URL(thumbnail.getString("path")+"."+thumbnail.getString("extension"));
						InputStream in = url.openStream();
							
						imgBytes = IOUtils.toByteArray(in);
					} else {
						imgBytes = null;
					}
						
					Numero numero = new Numero (0,titulo,fecha,tapa,estado,resenha,imgBytes,172);

					gestionNumeros.insertarNumero(numero);

				}
				
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}		
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

