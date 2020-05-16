package controlador.stock;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import modelo.consultas.EjecutaConsultas;
import modelo.consultas.EjecutaConsultasVenta;
import modelo.objetos.LineaVenta;

//CLASE QUE PERMITE LA DEVOLUCIÓN DE UN PRODUCTO Y SU CORRESPONDIENTE MODIFICACION EN EL STOCK
public class DevolverProducto implements ActionListener{

	public DevolverProducto(LineaVenta linea, JTable tabla, DefaultTableModel tablaProductos){
		this.linea = linea;
		this.tabla = tabla;
		this.tablaProductos = tablaProductos;
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		new SeleccionCantidad();
	}
	
	private class SeleccionCantidad extends JDialog{
		
		/**
		 * 
		 */
		private static final long serialVersionUID = -3467986881108839209L;

		public SeleccionCantidad(){
			
			setModal(true);
			setLayout(null);
			setSize(350,170);
			setLocationRelativeTo(null);
			setUndecorated(true);
			getContentPane().setBackground(new Color(225,240,240));
			getRootPane().setBorder(BorderFactory.createLineBorder(Color.BLACK));
			
			//OBTIENE DATOS DEL PRODUCTO
			double cant_max = (double)tabla.getValueAt(0, 3);
			Font fuente = new Font(Font.DIALOG,Font.BOLD,16);
			
			JLabel mensaje = new JLabel("Seleccione la cantidad a devolver:",JLabel.CENTER);
			mensaje.setBounds(0, 30, 350, 20);
			mensaje.setFont(fuente);
			add(mensaje);
			
			EjecutaConsultas paraObtenerUnidad = new EjecutaConsultas();
			String unidad = paraObtenerUnidad.consultaUnidad(linea.getCodProducto());
			
			double paso;
			if(unidad.equals("KILOGRAMOS")){
				paso = 0.1;
			}else{
				paso = 1;
			}
			
			//SOLICITA CANTIDAD A DEVOLVER
			JSpinner selec_cant = new JSpinner(new SpinnerNumberModel(0,0,cant_max,paso));
			selec_cant.setBounds(120,65,100,30);
			selec_cant.setFont(fuente);
			add(selec_cant);
			
			JButton aceptar = new JButton("Aceptar");
			aceptar.setBounds(120, 115, 100, 30);
			aceptar.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent arg0) {
					//SOLICITA CONFIRMACIÓN DE ACCION
					if((double)selec_cant.getValue()!=0){
						String mensaje="<html><Font size=5>¿Está seguro que desea editar la linea de venta seleccionada?<br> "
								+ "No podrá deshacer esta acción luego.</Font></html>";
						int elegido = JOptionPane.showConfirmDialog(null,mensaje, "Confirmación", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
					
						//AL CONFIRMAR LA ACCIÓN, SE LLEVA A CABO LA ACTUALIZACIÓN
						if(elegido==JOptionPane.YES_OPTION){
							EjecutaConsultasVenta paraEliminarLinea = new EjecutaConsultasVenta();
							boolean ok = paraEliminarLinea.cancelarLinea(linea,selec_cant);
							if(ok){
								double nueva_cantidad = (double)tabla.getValueAt(0, 3) - (double)selec_cant.getValue();
								if(nueva_cantidad>=0){
									double nuevo_subtotal = linea.getSubtotal() - ((double)selec_cant.getValue())*linea.getPrecio_u();
									tabla.setValueAt(nueva_cantidad, 0, 3);
									tabla.setValueAt(nuevo_subtotal, 0, 4);
									dispose();
						
									int n = tablaProductos.getRowCount();
									int codigo;
									for(int i=0;i<n;i++){
										codigo = Integer.parseInt((String)tablaProductos.getValueAt(i, 0));
										if(codigo == linea.getCodProducto()){
											double cantidad = (Double)tablaProductos.getValueAt(i, 4);
											tablaProductos.setValueAt(cantidad+(double)selec_cant.getValue(), i, 4);
										}
									}
								}else{
									mensaje="<html><Font size=5>No se puede restar la cantidad seleccionada de la cantidad actual</Font></html>";
									JOptionPane.showMessageDialog(null,mensaje, "Error", JOptionPane.WARNING_MESSAGE);
								}
							}else{
								mensaje="<html><Font size=5>Error al eliminar la línea. Intente nuevamente</Font></html>";
								JOptionPane.showMessageDialog(null,mensaje, "Error", JOptionPane.WARNING_MESSAGE);	
							}
						}
					}else{
						String mensaje2="<html><Font size=5>El valor ingresado debe ser mayor a 0 (cero)</Font></html>";
						JOptionPane.showMessageDialog(null,mensaje2, "Error", JOptionPane.WARNING_MESSAGE);	
					}
				}
			});
			aceptar.setFont(fuente);
			add(aceptar);
			
			ActionMap mapaAccion = this.getRootPane().getActionMap();
			InputMap mapa = this.getRootPane().getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
			
			KeyStroke escape = KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE,0);		
			
			mapa.put(escape,"accion escape");
			mapaAccion.put("accion escape", new AbstractAction(){
				/**
				 * 
				 */
				private static final long serialVersionUID = -2909008160592790508L;

				public void actionPerformed(ActionEvent e){
					dispose();
				}
			});
			
			setVisible(true);
			
		}
	}
	
	LineaVenta linea;
	JTable tabla;
	DefaultTableModel tablaProductos;
}


