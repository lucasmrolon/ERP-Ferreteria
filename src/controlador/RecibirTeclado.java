package controlador;

import java.awt.event.*;
import javax.swing.*;

//CLASE QUE CAPTURA LOS EVENTOS DE TECLADO DE LA PANTALLA DE LOGIN
public class RecibirTeclado extends KeyAdapter{
	
	public RecibirTeclado(JButton boton_ingresar){
		boton=new JButton();
		boton=boton_ingresar;
	}
	
	public void keyPressed(KeyEvent e) {
		
			
		//SI PRESIONA LA TECLA ENTER, SE INTENTA INICIAR SESIÓN
		if(e.getKeyChar()==KeyEvent.VK_ENTER){
			boton.doClick();
		}
	}
	
	JButton boton;
}
