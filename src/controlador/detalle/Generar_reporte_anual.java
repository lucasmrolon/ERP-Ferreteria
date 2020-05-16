package controlador.detalle;

import java.awt.Color;
import java.awt.event.*;
import java.sql.Connection;
import java.util.*;
import javax.swing.*;
import modelo.Conexion;
import modelo.consultas.EjecutaConsultasReporte;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.view.JasperViewer;

//CLASE QUE GENERA UN REPORTE DE VENTAS ANUAL
public class Generar_reporte_anual implements ActionListener{

	@Override
	public void actionPerformed(ActionEvent arg0) {
		new VentanaSeleccionAnio();
	}
}

//VENTANA PARA CONFIGURAR EL REPORTE
class VentanaSeleccionAnio extends JDialog{
	/**
	 * 
	 */
	private static final long serialVersionUID = -9065594049111437609L;

	public VentanaSeleccionAnio(){
		setSize(300, 220);
		setLocationRelativeTo(null);
		setModal(true);
		setLayout(null);
		setTitle("Generar Reporte Anual");
		setUndecorated(true);
		getRootPane().setBorder(BorderFactory.createLineBorder(Color.BLACK));
		
		JLabel mensaje = new JLabel("<html><font size=4>Seleccione el año:</font></html>");
		mensaje.setBounds(10, 20, 300, 20);
		add(mensaje);
		
		//CARGA EL COMBOBOX CON LOS AÑOS EN QUE SE REALIZÓ AL MENOS UNA VENTA
		EjecutaConsultasReporte nuevaConsulta = new EjecutaConsultasReporte();
		ArrayList<Integer> anios = nuevaConsulta.obtenerAnios();
		JComboBox<Integer> paraAnio = new JComboBox<Integer>();
		if(anios!=null){
			for(Integer a:anios){
				paraAnio.addItem(a);
			}
		}else{
			JOptionPane.showMessageDialog(null,"<html><font size=4>No se pudo obtener años para seleccionar. Error de conexión.</font></html>",
					"¡Error!", JOptionPane.WARNING_MESSAGE);
		}
		paraAnio.setBounds(100,80,100, 30);
		add(paraAnio);
		
		//BOTÓN PARA GENERAR EL REPORTE
		JButton generarReporte = new JButton("Generar Reporte");
		generarReporte.setBounds(50, 140, 200, 30);
		add(generarReporte);
		
		
		generarReporte.addActionListener(new ActionListener(){
		
			@Override
			public void actionPerformed(ActionEvent e) {
				dispose();
				JasperReport reporte = null;
				try {
					//ESTABLECE UNA CONEXIÓN CON LA BASE DE DATOS
					Conexion conexion = new Conexion();
					Connection miconexion = conexion.dameConexion();
					
					//OBTIENE EL ARCHIVO MODELO DEL REPORTE
					reporte = (JasperReport)JRLoader.loadObjectFromFile("reportes/reporte_ventas_anual.jasper");
					
					//CREA Y ENVÍA PARÁMETROS AL REPORTE
					Map<String, Object> parametros = new HashMap<String, Object>();
				    parametros.put("anio", String.valueOf(paraAnio.getSelectedItem()));
				    
				    //GENERA EL REPORTE Y LO MUESTRA EN PANTALLA
				    JasperPrint jasperprint = JasperFillManager.fillReport(reporte,parametros,miconexion);
				    JasperViewer.viewReport(jasperprint, false);
			
				} catch (JRException ex) {
					JOptionPane.showMessageDialog(null,"<html><font size=4>No se pudo generar el reporte. Intente nuevamente.</font></html>",
							"¡Error!", JOptionPane.WARNING_MESSAGE);
				}
				
				
			}
			
		});
		
		ActionMap mapaAccion = this.getRootPane().getActionMap();
		InputMap mapa = this.getRootPane().getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
		
		KeyStroke escape = KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE,0);		
		
		mapa.put(escape,"accion escape");
		mapaAccion.put("accion escape", new AbstractAction(){
			/**
			 * 
			 */
			private static final long serialVersionUID = -290900816092790508L;

			public void actionPerformed(ActionEvent e){
				dispose();
			}
		});
		
		setVisible(true);
	}
}

