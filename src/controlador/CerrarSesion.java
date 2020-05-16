package controlador;

import java.awt.event.*;
import javax.swing.*;

import modelo.consultas.EjecutaConsultasEmpleado;
import vista.PanelLogin;

//CLASE PARA CERRAR LA SESI�N ACTIVA
public class CerrarSesion implements ActionListener{

	public CerrarSesion(JPanel panel,String usuario){
		this.panel=panel;
		this.usuario = usuario;
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		
		int elegido = JOptionPane.showConfirmDialog(null,"<html><font size=5>�Cerrar Sesi�n?</font></html>",
				"Salir de la aplicaci�n", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
		
		//SI CONFIRMA, SE CIERRA LA APLICACI�N
		if(elegido==JOptionPane.YES_OPTION){
		//REMUEVE EL PANEL ACTUAL Y REGRESA A LA PANTALLA DE LOGIN
			EjecutaConsultasEmpleado paraDesconectar = new EjecutaConsultasEmpleado();
			paraDesconectar.desconectarUsuario(usuario);
			JFrame ventana = (JFrame)SwingUtilities.getWindowAncestor(panel);	
			ventana.getContentPane().removeAll();
			ventana.repaint();
			PanelLogin nuevoLogin = new PanelLogin();
			ventana.add(nuevoLogin);
			ventana.repaint();
			ventana.validate();
			nuevoLogin.requestFocus();
		}
	}
	
	JPanel panel;
	String usuario;
}
