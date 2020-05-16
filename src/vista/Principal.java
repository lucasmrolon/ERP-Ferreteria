package vista;

import javax.swing.JFrame;

//CLASE PRINCIPAL E INICIAL DE LA APLICACIÓN
public class Principal {

	//static GraphicsDevice device = 
	//		GraphicsEnvironment.getLocalGraphicsEnvironment().getScreenDevices()[0];
	
	public static void main(String[] args) {
		
		VentanaPrincipal nuevaVentana = new VentanaPrincipal();
		nuevaVentana.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		nuevaVentana.setUndecorated(true);
		nuevaVentana.setVisible(true);	 
	}
}
