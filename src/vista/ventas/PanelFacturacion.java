package vista.ventas;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;

import controlador.ActualizarModelo;
import controlador.CrearTabla;
import controlador.ventas.CancelarVenta;
import modelo.consultas.EjecutaConsultas;
import modelo.consultas.EjecutaConsultasEmpleado;
import modelo.consultas.EjecutaConsultasVenta;
import modelo.objetos.LineaVenta;

//PANEL PARA FACTURAR VENTAS
public class PanelFacturacion extends JPanel{

	
	public PanelFacturacion(DefaultTableModel modeloTablaVentas,DefaultTableModel modeloTablaProductos,DefaultTableModel modeloLineasReparto){
		
		setBackground(new Color(224,224,248));
		tablaventas = new JTable();
		paraActualizar = new CrearTabla();		
		actualizarModelo();
		darFormatoATabla(tablaventas);
		
		JScrollPane paraTabla = new JScrollPane(tablaventas);
		paraTabla.setPreferredSize(new Dimension(350,400));
		add(paraTabla);
		
		Runnable r = new ActualizarModelo(tablaventas,this,"ventas_sin_facturar");
		Thread t = new Thread(r);
		t.start();	
		
		tablalineas = new JTable();
		
		String[] columnasLineas = {"CÓDIGO PRODUCTO","DESCRIPCIÓN","PRECIO UNITARIO","CANTIDAD","SUBTOTAL","PARA REPARTO"};
		
		//SE BLOQUEA LA EDICIÓN DE CELDAS DE LA TABLA
		modeloLineas=new DefaultTableModel(null,columnasLineas){
			public boolean isCellEditable(int rowIndex,int columnIndex){
				return false;
			}
		};
		
		paraObtenerLineas = new EjecutaConsultasVenta();
		paraObtenerProducto = new EjecutaConsultas();
		
		tablaventas.getSelectionModel().addListSelectionListener(new ListSelectionListener(){

			@Override
			public void valueChanged(ListSelectionEvent arg0) {
				
				int seleccionado = tablaventas.getSelectedRow();
				
				if(seleccionado!=-1){
				
					int n_venta = Integer.parseInt((String)tablaventas.getValueAt(seleccionado, 0));
					
					
					ArrayList<LineaVenta> lineas = paraObtenerLineas.obtenerLineasPendientesDeFacturacion(n_venta);
				
					for(int i=modeloLineas.getRowCount();i>0;i--){
						modeloLineas.removeRow(i-1);
					}
				
					for(LineaVenta l : lineas){
					
						int cod_producto = l.getCodProducto();
						double precio_u = l.getPrecio_u();
						double cantidad = l.getCantidad();
						double subtotal = l.getSubtotal();
						boolean reparte = l.isReparte();
						
						String hay_reparto="";
						if(reparte){
							hay_reparto="SI";
						}else{
							hay_reparto="NO";
						}
						
						String desc_prod = paraObtenerProducto.obtieneProducto(cod_producto).getDescripcion();
						
						Object[] linea = {String.format("%05d", cod_producto),desc_prod,precio_u,cantidad,subtotal,hay_reparto};
					
						modeloLineas.addRow(linea);	
					}
				
					DefaultTableCellRenderer tcr = new DefaultTableCellRenderer();
					tcr.setHorizontalAlignment(SwingConstants.CENTER);
					tablalineas.setModel(modeloLineas);
					tablalineas.setRowHeight(25);
					TableColumnModel modeloColumnas = tablalineas.getColumnModel();
					tablalineas.setFocusable(false);
					modeloColumnas.getColumn(0).setCellRenderer(tcr);
					modeloColumnas.getColumn(1).setCellRenderer(tcr);
					modeloColumnas.getColumn(2).setCellRenderer(tcr);
					modeloColumnas.getColumn(3).setCellRenderer(tcr);
					modeloColumnas.getColumn(4).setCellRenderer(tcr);
					modeloColumnas.getColumn(5).setCellRenderer(tcr);
				}else{
					for(int i=modeloLineas.getRowCount();i>0;i--){
						modeloLineas.removeRow(i-1);
					}
				}
			}
		});
		
		JScrollPane paraTabla2 = new JScrollPane(tablalineas);
		paraTabla2.setPreferredSize(new Dimension(950,400));
		add(paraTabla2);
		
		//BOTON PARA REALIZAR DEVOLUCION DE PRODUCTOS
		verVentas = new JButton("Ver ventas");
		verVentas.setIcon(new ImageIcon(getClass().getResource("/img/leer_bd.png")));
		verVentas.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				new VistaDetalleVentas(modeloTablaProductos);
			}
		});
		add(verVentas);
		
		
		facturarVenta = new JButton("Facturar Venta");
		
		PanelFacturacion panel = this;
		facturarVenta.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				if(tablaventas.getSelectedRow()!=-1){
					String usuario = (String)tablaventas.getValueAt(tablaventas.getSelectedRow(),2);
					
					int cod_venta = Integer.parseInt((String)tablaventas.getValueAt(tablaventas.getSelectedRow(), 0));
					new GestionVenta(tablalineas,modeloLineasReparto,usuario,cod_venta,panel);
				}
				else{
					String mensaje = "<html><Font size=5>¡No ha seleccionado ninguna venta!</Font></html>";
					JOptionPane.showMessageDialog(null, mensaje, "¡Error!", JOptionPane.WARNING_MESSAGE);
				}
			}
		});
		facturarVenta.setIcon(new ImageIcon(getClass().getResource("/img/ok.png")));
		add(facturarVenta);
		
		//BOTON PARA CANCELAR VENTAS
		cancelarVenta = new JButton("Cancelar Venta");
		
		cancelarVenta.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				if(tablaventas.getSelectedRow()!=-1){
					String usuario = (String)tablaventas.getValueAt(tablaventas.getSelectedRow(),2);
					
					int cod_venta = Integer.parseInt((String)tablaventas.getValueAt(tablaventas.getSelectedRow(), 0));
					
					String mensaje = "<html><Font size=5>¿Está seguro que desea eliminar la venta seleccionada?</Font></html>";
					int elegido = JOptionPane.showConfirmDialog(null, mensaje, "Eliminar Venta",JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
					
					if(elegido==JOptionPane.YES_OPTION){

						paraObtenerUsuario = new EjecutaConsultasEmpleado();
						int cod_usuario = paraObtenerUsuario.obtieneId(usuario);
						new CancelarVenta(cod_venta,panel,cod_usuario);
						
					}
				}
				else{
					String mensaje = "<html><Font size=5>¡No ha seleccionado ninguna venta!</Font></html>";
					JOptionPane.showMessageDialog(null, mensaje, "¡Error!", JOptionPane.WARNING_MESSAGE);
				}
			}
		});
		cancelarVenta.setIcon(new ImageIcon(getClass().getResource("/img/cancelar.png")));
		add(cancelarVenta);
		
		tablaventas.requestFocus();
		
	}
	
	public void darFormatoATabla(JTable tablaventas){
		
		DefaultTableCellRenderer tcr = new DefaultTableCellRenderer();
		tcr.setHorizontalAlignment(SwingConstants.CENTER);
		tablaventas.setRowHeight(30);
		TableColumnModel columnas = tablaventas.getColumnModel();
		columnas.getColumn(0).setCellRenderer(tcr);
		columnas.getColumn(1).setCellRenderer(tcr);
		columnas.getColumn(2).setCellRenderer(tcr);
		
	}
	
	
	public void actualizarModelo(){
		
		DefaultTableModel modeloTablaVentas = paraActualizar.obtenerVentas();
		tablaventas.setModel(modeloTablaVentas);
		darFormatoATabla(tablaventas);
	}
	EjecutaConsultasVenta paraObtenerLineas;
	EjecutaConsultas paraObtenerProducto;
	EjecutaConsultasEmpleado paraObtenerUsuario;
	public JTable tablaventas;
	JTable tablalineas;
	DefaultTableModel modeloLineas;
	public JButton verVentas,facturarVenta,cancelarVenta;
	CrearTabla paraActualizar;
}
