package controlador.detalle;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.sql.*;
import java.util.*;

import javax.swing.*;

import com.toedter.calendar.JDateChooser;

import modelo.Conexion;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.view.JasperViewer;

//CLASE QUE GENERA UN REPORTE DE VENTAS POR PERÍODO
public class Generar_reporte_ventas implements ActionListener{

	@Override
	public void actionPerformed(ActionEvent arg0) {
		new VentanaSeleccionPeriodo();
	}
}

//VENTANA PARA SELECCIONAR EL PERÍODO ELEGIDO
class VentanaSeleccionPeriodo extends JDialog{
	/**
	 * 
	 */
	private static final long serialVersionUID = 2392263539855251685L;

	public VentanaSeleccionPeriodo(){
		setSize(400, 270);
		setLocationRelativeTo(null);
		setModal(true);
		setLayout(null);
		setUndecorated(true);
		getRootPane().setBorder(BorderFactory.createLineBorder(Color.BLACK));
		setTitle("Generar Reporte de Ventas");
		
		JLabel mensaje = new JLabel("<html><font size=4>Seleccione el período:</font></html>");
		mensaje.setBounds(10, 20, 300, 20);
		add(mensaje);
		
		JLabel inicio = new JLabel("<html><font size=4>Desde</font></html>",JLabel.CENTER);
		inicio.setBounds(50, 70, 100, 30);
		add(inicio);
		JDateChooser paraInicio = new JDateChooser();
		paraInicio.getJCalendar().setPreferredSize(new Dimension(300,300));
		paraInicio.setBounds(50, 100, 100, 30);
		add(paraInicio);
		
		JLabel fin = new JLabel("<html><font size=4>Hasta</font></html>",JLabel.CENTER);
		fin.setBounds(250, 70, 100, 30);
		add(fin);
		JDateChooser paraFin = new JDateChooser();
		paraFin.getJCalendar().setPreferredSize(new Dimension(300,300));
		paraFin.setBounds(250, 100, 100, 30);
		add(paraFin);
		
		JButton generarReporte = new JButton("Generar Reporte");
		generarReporte.setBounds(100, 190, 200, 30);
		add(generarReporte);
		
		
		generarReporte.addActionListener(new ActionListener(){
		
			@Override
			public void actionPerformed(ActionEvent e) {
				JasperReport reporte = null;
				try {
					//ESTABLECE CONEXIÓN CON LA BASE DE DATOS
					Conexion conexion = new Conexion();			
					reporte = (JasperReport)JRLoader.loadObjectFromFile("reportes/reporte_ventas.jasper");
					
					//OBTIENE FECHA INICIAL Y FINAL DEL PERÍODO
					long desde = paraInicio.getDate().getTime();
					long hasta = paraFin.getDate().getTime();
					
					//COMPRUEBA QUE LAS FECHAS SEAN VÁLIDAS
					if(desde<hasta){
						//AGREGA LAS FECHAS COMO PARÁMETROS
						Connection miconexion = conexion.dameConexion();
						Map<String, Object> parametros = new HashMap<String, Object>();
						parametros.put("desde", new java.sql.Timestamp(desde));
						parametros.put("hasta", new java.sql.Timestamp(hasta));
				    
						//CREA EL REPORTE Y LO MUESTRA EN PANTALLA
						JasperPrint jasperprint = JasperFillManager.fillReport(reporte,parametros,miconexion);
						JasperViewer.viewReport(jasperprint, false);
						//JasperPrintManager.printReport(jasperprint, false);
						
						dispose();
						miconexion.close();
						
					}else{
						JOptionPane.showMessageDialog(null,"<html><font size=4>El período indicado es inválido.<br>Seleccione nuevamente</font></html>",
								"¡Error!", JOptionPane.WARNING_MESSAGE);
					}
				} catch(JRException ex){
					ex.printStackTrace();
					JOptionPane.showMessageDialog(null,"<html><font size=4>¡Error al imprimir el reporte! Intente nuevamente.</font></html>",
							"Imprimir reparto",JOptionPane.WARNING_MESSAGE);
				} catch(SQLException ex2){
					ex2.printStackTrace();
				}
				catch(NullPointerException ex3){
					JOptionPane.showMessageDialog(null,"<html><font size=4>El período indicado es inválido.<br>Seleccione nuevamente</font></html>",
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

