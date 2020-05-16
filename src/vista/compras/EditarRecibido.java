package vista.compras;

import java.awt.Color;

import javax.swing.*;
import javax.swing.event.*;
import javax.swing.table.*;

import controlador.compras.RegistrarProductoRetrasado;

//CLASE QUE PERMITE MODIFICAR CANTIDAD O PRECIO DE PRODUCTO RECIBIDO
public class EditarRecibido extends JDialog{
	/**
	 * 
	 */
	private static final long serialVersionUID = 769652791965236058L;

	public EditarRecibido(JTable muestraProductos,
			int fila_seleccionada,String nomb_proveedor,String n_pedido,int columna_inicial){
		setSize(900,300);
		this.getContentPane().setBackground(new Color(224,224,248));
		setModal(true);
		setLocationRelativeTo(null);
		setLayout(null);
		setUndecorated(true);
		getRootPane().setBorder(BorderFactory.createLineBorder(Color.BLACK));

		this.tabla=muestraProductos;
		
		//MUESTRA MENSAJE DE EDICION
		JLabel mensaje = new JLabel("<html><font size=4>Edite de ser necesario el precio y/o la cantidad. "
				+ "<br>Luego haga clic en 'Registrar Recepción de Producto'</font></html>");
		mensaje.setBounds(40, 20, 880, 40);
		add(mensaje);
		
		//MUESTRA DATOS DEL PEDIDO
		JLabel muestraDatosLineaPendiente = 
				new JLabel("<html><font size=4>PROVEEDOR: </font><font size=5>" + nomb_proveedor.toUpperCase()+"</font>"
						+ "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"
						+ "<font size=4> N° PEDIDO: </font><font size=5>"+n_pedido+"</font></html>",JLabel.CENTER);
		muestraDatosLineaPendiente.setVerticalAlignment(SwingConstants.TOP);
		muestraDatosLineaPendiente.setBounds(10, 70, 880, 30);
		add(muestraDatosLineaPendiente);
		
		//MUESTRA LINEAS DE PEDIDO
		String[] columnas = {"FECHA PEDIDO","LÍNEA N°","DESCRIPCIÓN","PRECIO U.","CANTIDAD","SUBTOTAL"};
		DefaultTableModel modelo=new DefaultTableModel(null,columnas){
		
			private static final long serialVersionUID = -2886483162368923195L;

			public boolean isCellEditable(int rowIndex,int columnIndex){
				if(columnIndex==3 || columnIndex==4){
					return true;
				}
				else{
					return false;
				}
			}
		};
		Object[] fila = new Object[6];
		
		for(int i=0;i<6;i++){
			fila[i]=muestraProductos.getValueAt(fila_seleccionada, i+columna_inicial);
		}
		modelo.addRow(fila);
		
		muestraProductoSeleccionado = new JTable(modelo);
		formatearTabla();
		
		JScrollPane paraTabla = new JScrollPane(muestraProductoSeleccionado);
		muestraProductoSeleccionado.setRowHeight(25);
		paraTabla.setBounds(40, 95, 820, 48);
		add(paraTabla);
		
		JButton registrarRecepcion = new JButton("Registrar Recepción de Producto");
		registrarRecepcion.setBounds(300,200,300,30);
		registrarRecepcion.addActionListener(new RegistrarProductoRetrasado(this,tabla,fila_seleccionada,muestraProductoSeleccionado));
		
		add(registrarRecepcion);
		
		setVisible(true);
	}
	//DA FORMATO A LA TABLA
	public void formatearTabla(){
		TableColumnModel modeloColumnas = muestraProductoSeleccionado.getColumnModel();
		DefaultTableCellRenderer tcr = new DefaultTableCellRenderer();
		tcr.setHorizontalAlignment(SwingConstants.CENTER);
		modeloColumnas.getColumn(0).setMinWidth(130);
		modeloColumnas.getColumn(0).setCellRenderer(tcr);
		modeloColumnas.getColumn(1).setMinWidth(80);
		modeloColumnas.getColumn(1).setCellRenderer(tcr);
		modeloColumnas.getColumn(2).setMinWidth(350);
		modeloColumnas.getColumn(3).setMinWidth(90);
		modeloColumnas.getColumn(3).setCellRenderer(tcr);
		modeloColumnas.getColumn(4).setMinWidth(90);
		modeloColumnas.getColumn(4).setCellRenderer(tcr);
		modeloColumnas.getColumn(5).setMinWidth(80);
		modeloColumnas.getColumn(5).setCellRenderer(tcr);
		
		
		
		//SI SE MODIFICA PRECIO O CANTIDAD, SE ACTUALIZA EL SUBTOTAL
		muestraProductoSeleccionado.getModel().addTableModelListener(new TableModelListener(){

			@Override
			public void tableChanged(TableModelEvent e) {
				if(e.getType()==TableModelEvent.UPDATE){
					int fila = e.getFirstRow();
					int columna = e.getColumn();
					if(columna==5){
						return;
					}
					double precio_u=0;
					double cant=0;
					try{
						precio_u = (Double)muestraProductoSeleccionado.getValueAt(fila, 3);
					}catch(Exception ex1){
						precio_u = Double.parseDouble((String)muestraProductoSeleccionado.getValueAt(fila, 3));
					}
					try{
						cant = (Double)muestraProductoSeleccionado.getValueAt(fila, 4);
					}catch(Exception ex2){
						cant = Double.parseDouble((String)muestraProductoSeleccionado.getValueAt(fila, 4));
					}
					
					muestraProductoSeleccionado.getModel().setValueAt(Math.round(precio_u*cant*100d)/100d,fila, 5);
				}
			}
		});
		
		
	}
	
	JTable tabla,muestraProductoSeleccionado;
}
