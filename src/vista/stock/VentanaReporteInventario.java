package vista.stock;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.sql.Connection;
import java.util.*;

import javax.swing.*;

import modelo.Conexion;
import modelo.consultas.EjecutaConsultas;
import modelo.objetos.Rubro;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.view.JasperViewer;

//PERMITE CREAR UN REPORTE DE INVENTARIO
public class VentanaReporteInventario extends JDialog{

	/**
	 * 
	 */
	private static final long serialVersionUID = 353707840645383496L;

	public VentanaReporteInventario(){
		getContentPane().setBackground(new Color(225,240,240));
		setSize(300, 200);
		setLocationRelativeTo(null);
		setModal(true);
		setLayout(null);
		setUndecorated(true);
		getRootPane().setBorder(BorderFactory.createLineBorder(Color.BLACK));
		setTitle("Generar Reporte de Inventario");
		
		JLabel mensaje = new JLabel("<html><font size=4>Seleccione el rubro:</font></html>");
		mensaje.setBounds(10, 20, 300, 20);
		add(mensaje);
		
		EjecutaConsultas nuevaConsulta = new EjecutaConsultas();
		ArrayList<Rubro> rubros = nuevaConsulta.consultaRubros();
		JComboBox<String> paraRubros = new JComboBox<String>();
		paraRubros.addItem("TODOS");
		
		if(rubros!=null){
			for(Rubro r:rubros){
				paraRubros.addItem(r.getNombre());
			}
		}
		else{
			JOptionPane.showMessageDialog(null,"<html><font size=4>No se pudo obtener la lista de rubros. Intente nuevamente</font></html>",
					"¡Error!", JOptionPane.WARNING_MESSAGE);
		}
		paraRubros.setBounds(50,60,200, 30);
		add(paraRubros);
		
		JButton generarReporte = new JButton("Generar Reporte");
		generarReporte.setBounds(50, 120, 200, 30);
		add(generarReporte);          
		
		generarReporte.addActionListener(new ActionListener(){
		
			@Override
			public void actionPerformed(ActionEvent e) {
				dispose();
				JasperReport reporte = null;
				try {
					
					Conexion conexion = new Conexion();
					Connection miconexion = conexion.dameConexion();
					
					String seleccion = String.valueOf(paraRubros.getSelectedItem());
					Map<String, Object> parametros = null;
					
					if(seleccion.equals("TODOS")){
						reporte = (JasperReport)JRLoader.loadObjectFromFile("reportes/reporte_inventario.jasper");
					}
					else{
					
						reporte = (JasperReport)JRLoader.loadObjectFromFile("reportes/reporte_inventario_rubro.jasper");
						parametros = new HashMap<String, Object>();
					    parametros.put("rubro", seleccion);
					}
					
				    
				    JasperPrint jasperprint = JasperFillManager.fillReport(reporte,parametros,miconexion);
				    JasperViewer.viewReport(jasperprint, false);
			
				} catch(JRException ex){
					ex.printStackTrace();
					JOptionPane.showMessageDialog(null,"<html><font size=4>¡Error al imprimir el reporte! Intente nuevamente.</font></html>",
							"Imprimir reparto",JOptionPane.WARNING_MESSAGE);
				}
				
				
			}
			
		});
		
		//ESTABLECE CANCELACION CON TECLA ESCAPE
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
