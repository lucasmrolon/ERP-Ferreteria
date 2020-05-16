package vista.compras;

import java.awt.*;
import java.awt.event.*;
import java.text.DecimalFormat;

import javax.swing.*;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.*;

import controlador.compras.ConfirmarRecepcion;
import controlador.compras.ConsultarDatosDePedidos;

//CLASE PARA MOSTRAR LINEAS DE PEDIDO Y CONFIRMAR SU RECEPCION
public class MuestraDetallePedido extends JPanel{

	/**
	 * 
	 */
	private static final long serialVersionUID = -1203962303918994864L;
	public MuestraDetallePedido(int cod_pedido,String nombre_proveedor, String fecha_creacion,VerPedidos ventana,String usuario){
		
		setLayout(null);
		setBackground(new Color(191,191,240));
		
		ventana.setTitle("Detalle de Pedido");
		
		//MUESTRA NUMERO DE PEDIDO Y FECHA CREACION
		JLabel muestra_n_pedido = 
				new JLabel("<html><font size=5>PEDIDO N° "+String.format("%05d",cod_pedido)+
						"</font><font size=4>&nbsp;&nbsp;&nbsp;-&nbsp;&nbsp;&nbsp; Creado: "
						+ fecha_creacion +"</font></html>",JLabel.CENTER);
		muestra_n_pedido.setBounds(0,0,1000,60);
		muestra_n_pedido.setVerticalAlignment(SwingConstants.CENTER);
		add(muestra_n_pedido);
		
		//MUESTRA INSTRUCCIONES
		JLabel instrucciones = 
				new JLabel("<html><font size=4>Si desea confirmar recepción, haga clic en 'Editar Pedido'.</font></html>");
		instrucciones.setBounds(150,50,700,60);
		add(instrucciones);
		
		ConsultarDatosDePedidos paraConsultas = new ConsultarDatosDePedidos();
		
		//OBTIENE LINEAS DEP PEDIDO Y DA FORMATO A LA TABLA
		muestraLineasPedido = new JTable(paraConsultas.obtenerModeloLineasDePedido(cod_pedido));
		formatearTabla();
		JScrollPane paraTabla = new JScrollPane(muestraLineasPedido);
		paraTabla.setBounds(150, 100, 700, muestraLineasPedido.getRowHeight()*8+23);
		add(paraTabla);
		
		double monto_pedido=0;
		for(int i=0;i<muestraLineasPedido.getRowCount();i++){
			monto_pedido+=(double)muestraLineasPedido.getValueAt(i, 5);
		}
		
		DecimalFormat df = new DecimalFormat("###,##0.00");
		JLabel total_pedido = 
				new JLabel("<html><font size=5>TOTAL PEDIDO:&nbsp;&nbsp;&nbsp;<font color=blue>$ "+df.format(monto_pedido)+"</font></font></html>",JLabel.CENTER);
		total_pedido.setBounds(150, 300, 700, 30);
		add(total_pedido);
		
		boolean[] error = {false};
		
		//BOTON PARA HABILITAR LA EDICION DE LAS LINEAS DE PEDIDO
		JButton paraConfirmar = new JButton("Editar Pedido");
		paraConfirmar.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				muestraLineasPedido.setModel(obtenerModeloParaEdicion());
				formatearTablaEditable(total_pedido,df,error);
				paraTabla.setSize(paraTabla.getWidth()+40, paraTabla.getHeight());
				paraTabla.setLocation(paraTabla.getLocation().x-20,paraTabla.getLocation().y);
				paraConfirmar.setVisible(false);
				
				instrucciones.setText("<html><font size=4>Edite y/o seleccione los productos deseados y presione el boton 'Confirmar Recepción'.</font></html>");
				total_pedido.setText("<html><font size=5>TOTAL PEDIDO:&nbsp;&nbsp;&nbsp;<font color=blue>$ "+df.format(0)+"</font></font></html>");
				JButton paraVolcarAStock = new JButton("Confirmar Recepción");
				paraVolcarAStock.addActionListener(new ConfirmarRecepcion(error,muestraLineasPedido,cod_pedido,usuario,ventana));
				
				paraVolcarAStock.setBounds(505, 350, 200, 30);
				add(paraVolcarAStock);
				
				ventana.setTitle("Confirmación de Pedido");
			}
		});
		paraConfirmar.setBounds(505, 350, 200, 30);
		add(paraConfirmar);
		
		MuestraDetallePedido panel = this;
		//BOTON PARA DESHACER LA EDICION
		JButton paraRegresar = new JButton("Regresar");
		paraRegresar.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				JPanel panel_padre = (JPanel)panel.getParent();
				panel_padre.remove(panel);
				PanelEdicion panelPedidos = new PanelEdicion(nombre_proveedor,ventana,usuario);
				panel_padre.add(panelPedidos);
				panel_padre.repaint();
				panel_padre.validate();
			}
		});
		paraRegresar.setBounds(255, 350, 200, 30);
		add(paraRegresar);
	}
	//DA FORMATO A LA TABLA
	public void formatearTabla(){
		muestraLineasPedido.setRowHeight(20);
		DefaultTableCellRenderer tcr = new DefaultTableCellRenderer();
		tcr.setHorizontalAlignment(SwingConstants.CENTER);
		TableColumnModel modeloColumnas = muestraLineasPedido.getColumnModel();
		modeloColumnas.getColumn(0).setMinWidth(80);
		modeloColumnas.getColumn(1).setMinWidth(80);
		modeloColumnas.getColumn(2).setMinWidth(280);
		modeloColumnas.getColumn(3).setMinWidth(90);
		modeloColumnas.getColumn(4).setMinWidth(90);
		modeloColumnas.getColumn(5).setMinWidth(80);
		
		for(int i=0;i<6;i++){
			if(i!=2){
				modeloColumnas.getColumn(i).setCellRenderer(tcr);
			}
		}
	}
	
	//CAMBIA LA TABLA A MODO EDITABLE
	public DefaultTableModel obtenerModeloParaEdicion(){
		
		modeloTabla = (DefaultTableModel)muestraLineasPedido.getModel();
		
		String[] columnas = {"RECIBIDO","COD. LINEA","COD. PRODUCTO","DESCRIPCIÓN","PRECIO U.","CANTIDAD","SUBTOTAL"};
		modeloEditable= new DefaultTableModel(null,columnas){
			/**
			 * 
			 */
			private static final long serialVersionUID = 678526894151676923L;

			public boolean isCellEditable(int rowIndex,int columnIndex){
				if(columnIndex==0||columnIndex==4||columnIndex==5){
					return true;
				}else{
					return false;	
				}
			}
		};
		
		Object[] fila = new Object[7]; 
		for(int i=0;i<modeloTabla.getRowCount();i++){
			fila[0] = false;
			fila[1] = modeloTabla.getValueAt(i, 0);
			fila[2] = modeloTabla.getValueAt(i, 1);
			fila[3] = modeloTabla.getValueAt(i, 2);
			fila[4] = modeloTabla.getValueAt(i, 3);
			fila[5] = modeloTabla.getValueAt(i, 4);
			fila[6] = modeloTabla.getValueAt(i, 5);
			
			modeloEditable.addRow(fila);
		}
		
		return modeloEditable;
		
	}
	
	public void formatearTablaEditable(JLabel totalPedido,DecimalFormat df, boolean[] error){
		muestraLineasPedido.setDefaultRenderer(Object.class, formatoTablaEditable());
		
		TableColumnModel modeloColumnas = muestraLineasPedido.getColumnModel();
		
		//AÑADE Y DA FORMATO A COLUMNA DE CHECKBOX
		JCheckBox paraMarcar = new JCheckBox();
		TableColumn columnaCheck = modeloColumnas.getColumn(0);
		columnaCheck.setCellEditor(new DefaultCellEditor(paraMarcar));
		modeloColumnas.getColumn(0).setMinWidth(20);
		modeloColumnas.getColumn(0).setCellRenderer(paraCheck());
		modeloColumnas.getColumn(1).setMinWidth(80);
		modeloColumnas.getColumn(2).setMinWidth(80);
		modeloColumnas.getColumn(3).setMinWidth(280);
		modeloColumnas.getColumn(4).setMinWidth(90);
		modeloColumnas.getColumn(5).setMinWidth(90);
		modeloColumnas.getColumn(6).setMinWidth(80);
		
		muestraLineasPedido.getModel().addTableModelListener(new TableModelListener(){

			@Override
			public void tableChanged(TableModelEvent e) {
				if(e.getType()==TableModelEvent.UPDATE){
					try{
						int fila = e.getFirstRow();
						int columna = e.getColumn();
						if(columna==6){
							return;
						}
						double precio_u=0;
						double cant=0;
						try{
							precio_u = (Double)muestraLineasPedido.getValueAt(fila, 4);
						}catch(Exception ex1){
							precio_u = Double.parseDouble((String)muestraLineasPedido.getValueAt(fila, 4));
						}
						try{
							cant = (Double)muestraLineasPedido.getValueAt(fila, 5);
						}catch(Exception ex2){
							cant = Double.parseDouble((String)muestraLineasPedido.getValueAt(fila, 5));
						}
					
						muestraLineasPedido.getModel().setValueAt(Math.round(precio_u*cant*100d)/100d,fila, 6);
						error[0]=false;
						double monto_pedido=0;
						for(int i=0;i<muestraLineasPedido.getRowCount();i++){
							if((boolean)muestraLineasPedido.getValueAt(i, 0)){
								try{
									monto_pedido+=Double.parseDouble((String)muestraLineasPedido.getValueAt(i, 6));
								}catch(Exception ex3){
									monto_pedido+=(Double)muestraLineasPedido.getValueAt(i, 6);
								}
							}
						}
						totalPedido.setText("<html><font size=5>TOTAL PEDIDO:&nbsp;&nbsp;&nbsp;<font color=blue>$ "+df.format(monto_pedido)+"</font></font></html>");
					}catch(NumberFormatException ex){
						JOptionPane.showMessageDialog(null,"<html><font size=4>¡El valor ingresado es inválido!</font></html>",
								"¡Error!", JOptionPane.WARNING_MESSAGE);
						error[0]=true;
					}
				}
			}
			
		});
	}
	
	
	private DefaultTableCellRenderer formatoTablaEditable(){
		
		DefaultTableCellRenderer dtcr = new DefaultTableCellRenderer(){
			
			/**
			 * 
			 */
			private static final long serialVersionUID = 4605800746537162347L;

			public Component getTableCellRendererComponent(JTable table,Object value, boolean isSelected, boolean hasFocus, int row, int column){
				if(column==3){
				    setHorizontalAlignment(SwingConstants.LEFT);
				}else
					setHorizontalAlignment(SwingConstants.CENTER);
				super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
				return this;
			}
		};
		return dtcr;
	}
	
	private DefaultTableCellRenderer paraCheck(){
		
		DefaultTableCellRenderer dtcr = new DefaultTableCellRenderer(){
			
			/**
			 * 
			 */
			private static final long serialVersionUID = -3772589523959776363L;

			//DA FORMATO A LA COLUMNA DE CHECKBOX
			public Component getTableCellRendererComponent(JTable table,Object value, boolean isSelected, boolean hasFocus, int row, int column){
				JCheckBox check = new JCheckBox();
				check.setVisible(true);
				if(value instanceof Boolean){
					if ((Boolean)value==true)
						check.setSelected(true);
					else if ((Boolean)value==false){
						check.setSelected(false);
					}
					return check;
				}else 
					return null;
			}
		};
		return dtcr;
	}
	
	DefaultTableModel modeloTabla,modeloEditable;
	JTable muestraLineasPedido;
	double monto_total;
}
