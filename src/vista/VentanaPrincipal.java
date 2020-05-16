package vista;

import java.awt.*;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.*;

import modelo.Conexion;

//VENTANA PRINCIPAL DE LA APLICACIÓN
public class VentanaPrincipal extends JFrame{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -608248755389018674L;

	public VentanaPrincipal(){
		
		
		
		setIconImage(null);
		//ESTABLECE CARACTERÍSTICAS DE LA VENTANA
		setBounds(0,0,1000,700);
		setLayout(new BorderLayout());
		setBackground(Color.LIGHT_GRAY);
		this.setResizable(false);
		setExtendedState(MAXIMIZED_BOTH);
		setUndecorated(true);
		
		//CARGA EL PANEL DE LOGIN
		PanelLogin nuevoPanel = new PanelLogin();		
		add(nuevoPanel,BorderLayout.CENTER);
		
		
		
		
	}
}
