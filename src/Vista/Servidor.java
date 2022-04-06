package Vista;

import java.awt.EventQueue;

import javax.swing.JFrame;

public class Servidor extends JFrame {
	
	ServerSocket skServidor;
    ArrayList<Socket> listaSockets = new ArrayList<>();

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
		setBounds(100, 100, 450, 300);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

	}

}
