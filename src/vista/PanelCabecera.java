package vista;

import javax.swing.*;
import controlador.ControladorReloj;
import java.awt.*;

//PANEL ENCABEZADO DE LA APLICACIÓN
public class PanelCabecera extends JPanel{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1470101039653325379L;

	public PanelCabecera(){
		
		setBackground(new Color(160,160,233));
		setLayout(new BorderLayout());
	
		JPanel paraLogo = new JPanel();
		paraLogo.setLayout(null);
		
		LogoEmpresa logo = new LogoEmpresa(167,87);
		logo.setPreferredSize(new Dimension(100,50));
		paraLogo.setBackground(new Color(160,160,233));
		paraLogo.add(logo);
		JLabel nombre_sistema = new JLabel("<html><font size=5>SISTEMA INTEGRADO DE ADMINISTRACIÓN DE REPARTOS, VENTAS, STOCK Y COMPRAS</font></html>");
		nombre_sistema.setBounds(200, 30, 1000, 30);
		nombre_sistema.setFont(new Font(Font.DIALOG,Font.ITALIC+Font.BOLD,25));
		paraLogo.add(nombre_sistema);
		add(paraLogo,BorderLayout.CENTER);
		
		//AÑADE EL PANEL QUE MUESTRA LA FECHA
		add(new panelHoraFecha(),BorderLayout.EAST);
	}
	
}

//PANEL QUE MUESTRA LA FECHA Y HORA
class panelHoraFecha extends JPanel{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 8798251321509345301L;

	public panelHoraFecha(){
		
		setPreferredSize(new Dimension(300,85));
		setBackground(new Color(160,160,233));
		setLayout(new GridLayout(3,1));
		
		//LABEL DE LA HORA
		JLabel hora = new JLabel("Hora"); 
		hora.setFont(new Font(Font.DIALOG,Font.ITALIC,20));
		hora.setHorizontalAlignment(0);


		//LABEL DE LA FECHA
		JLabel fecha = new JLabel("Fecha"); 
		fecha.setFont(new Font(Font.DIALOG,Font.ITALIC,20));
		fecha.setHorizontalAlignment(0);
		
		//SINCRONIZA CON EL RELOJ DEL SISTEMA
		ControladorReloj actualizarReloj = new ControladorReloj(hora,fecha);
		Timer nuevoTimer = new Timer(1000,actualizarReloj);
		nuevoTimer.start();
		
		add(new JLabel(""));
		
		add(fecha);add(hora);
	}
}