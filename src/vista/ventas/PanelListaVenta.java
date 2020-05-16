package vista.ventas;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DecimalFormat;
import java.util.ArrayList;

import javax.swing.*;
import javax.swing.table.*;

import controlador.ventas.CalcularNVenta;
import controlador.ventas.EliminarLineasVenta;
import modelo.consultas.EjecutaConsultasEmpleado;
import modelo.consultas.EjecutaConsultasVenta;
import modelo.objetos.LineaVenta;

public class PanelListaVenta extends JPanel{
	
	public PanelListaVenta(DefaultTableModel modeloTablaProductos, DefaultTableModel modeloTablaListaReparto, String usuario){
		
		//ESTABLECE CARACTERÍSTICAS DEL PANEL
		setLayout(null);
		//setBackground(new Color(224,224,248));
		formatea = new DecimalFormat("###,###.##");
		seleccionar_todo=false;
		
		//MUESTRA EL NÚMERO DE VENTA ACTUAL
		JLabel venta_n = new JLabel();
		venta_n.setBounds(30,25,170,30);
		venta_n.setFont(new Font(Font.DIALOG,Font.BOLD,17));
		venta_n.setOpaque(true);
		venta_n.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		actualizarN_venta(venta_n);
		add(venta_n);
		
		//MUESTRA LAS VENTAS REALIZADAS JUNTO CON SUS LINEAS DE VENTA
		verVentas = new JButton("Ver ventas");
		verVentas.setBounds(45, 100, 140, 40);
		verVentas.setIcon(new ImageIcon(getClass().getResource("/img/leer_bd.png")));
		verVentas.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				new VistaDetalleVentas(modeloTablaProductos);
			}
		});
		add(verVentas);

		//CREA LA TABLA DONDE SE AÑADIRÁN LAS LÍNEAS DE VENTA
		compra = new JTable();
		compra.setFont(new Font(Font.DIALOG,Font.ITALIC,12));
		compra.setRowHeight(20);
		Object[] columnas = {"CÓDIGO","DESCRIPCIÓN","PRECIO U.","CANTIDAD","SUBTOTAL","REPARTE","¿ELIMINAR LÍNEA?"};
		compra.setModel(new DefaultTableModel(null,columnas){
			public boolean isCellEditable(int rowIndex,int columnIndex){
				if(columnIndex==5 || columnIndex==6){
					return true;
				}else{
					return false;
				}
			}
		});
		compra.setSize(900,250);
		compra.setLocation(250,25);
		compra.setShowHorizontalLines(false);
		
		//ESTABLECE EL FORMATO DE LA TABLA
		DefaultTableCellRenderer tcr = new DefaultTableCellRenderer();
		tcr.setHorizontalAlignment(SwingConstants.CENTER);
		DefaultTableCellRenderer paraCheck = new DefaultTableCellRenderer(){
			public Component getTableCellRendererComponent(JTable table, Object value,boolean isSelected,boolean hasFocus,int row,int column){
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
		TableColumnModel modeloColumnas = compra.getColumnModel();
		modeloColumnas.getColumn(0).setCellRenderer(tcr);
		modeloColumnas.getColumn(2).setCellRenderer(tcr);
		modeloColumnas.getColumn(3).setCellRenderer(tcr);
		modeloColumnas.getColumn(4).setCellRenderer(tcr);
		TableColumn columnaReparte = modeloColumnas.getColumn(5);
		TableColumn columnaMarca = modeloColumnas.getColumn(6);
		JCheckBox paraReparto = new JCheckBox();
		JCheckBox paraMarcar = new JCheckBox();
		columnaReparte.setCellEditor(new DefaultCellEditor(paraReparto));
		columnaReparte.setCellRenderer(paraCheck);
		columnaMarca.setCellEditor(new DefaultCellEditor(paraMarcar));
		columnaMarca.setCellRenderer(paraCheck);		

		//CONTENEDOR PARA LA TABLA
		JScrollPane paraCompra = new JScrollPane(compra);
		paraCompra.setBounds(250,25,900,250);
		paraCompra.setBorder(BorderFactory.createDashedBorder(Color.BLUE,5,10));
		add(paraCompra);
		
		monto_total=0;		
		
		//MUESTRA EL MONTO TOTAL DE LA VENTA
		JLabel sobre_monto=new JLabel("TOTAL VENTA",SwingConstants.CENTER);
		sobre_monto.setFont(new Font(Font.DIALOG,Font.BOLD,20));
		sobre_monto.setOpaque(true);
		sobre_monto.setBackground(Color.BLUE);
		sobre_monto.setForeground(Color.WHITE);
		sobre_monto.setBounds(1180, 200, 150, 30);
		add(sobre_monto);
		total_venta = new JLabel("$ " + formatea.format(monto_total),SwingConstants.CENTER);
		total_venta.setFont(new Font(Font.DIALOG,Font.BOLD,20));
		total_venta.setBounds(1180, 235, 150, 30);
		add(total_venta);
		
		//BOTON PARA SELECCIONAR TODAS LAS LÍNEAS
		seleccionarTodo = new JButton("<html>Seleccionar&nbsp;&nbsp;&nbsp;&nbsp;<br>Todo</html>");
		seleccionarTodo.setBounds(1180, 30, 150, 40);
		seleccionarTodo.setIcon(new ImageIcon(getClass().getResource("/img/seleccionar_todo.png")));
		seleccionarTodo.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				int n = compra.getModel().getRowCount();
				if(!seleccionar_todo){
					for(int i=0;i<n;i++){
						compra.getModel().setValueAt(true, i, 6);
						seleccionar_todo=true;
					}
				}else{
					for(int i=0;i<n;i++){
						compra.getModel().setValueAt(false, i, 6);
						seleccionar_todo=false;
					}
				}
			}
		});
		add(seleccionarTodo);
		
		//BOTÓN PARA ELIMINAR UNA LÍNEA
		paraEliminarLinea = new JButton("<html>Eliminar&nbsp;&nbsp;&nbsp;&nbsp;<br>seleccionados</html>");
		paraEliminarLinea.setBounds(1180, 80, 150, 40);
		paraEliminarLinea.setIcon(new ImageIcon(getClass().getResource("/img/descartar.png")));
		paraEliminarLinea.addActionListener(new EliminarLineasVenta(this,compra,modeloTablaProductos));
		add(paraEliminarLinea);
		
		//BOTÓN PARA REGISTRAR UNA VENTA
		paraRegistrarVenta = new JButton("<html>Registrar venta</html>");
		paraRegistrarVenta.setBounds(1180, 130, 150, 40);
		paraRegistrarVenta.setIcon(new ImageIcon(getClass().getResource("/img/ok.png")));
		paraRegistrarVenta.addActionListener(new RegistrarVenta(compra,modeloTablaListaReparto,usuario,this));
		add(paraRegistrarVenta);

	}
	
	//ACTUALIZA EL NÚMERO DE VENTA ACTUAL
	public void actualizarN_venta(JLabel muestra_n_venta){
		CalcularNVenta nuevo_calculo = new CalcularNVenta(muestra_n_venta);
		Timer nuevo_timer = new Timer(10000,nuevo_calculo);
		nuevo_timer.start();
	}
	
	//AÑADE LA LÍNEA A LA TABLA
	public void aniadirLinea(int codigo,String desc,double precio_u,double cantidad, double subtotal){
		DefaultTableModel modelo = (DefaultTableModel)compra.getModel();
		Object[] linea = {String.format("%05d",codigo),desc,precio_u,cantidad,subtotal,false,false};
		modelo.addRow(linea);
		monto_total+=subtotal;
		total_venta.setText("$ " + formatea.format(monto_total));
	}
	
	//ELIMINA UNA LÍNEA DE LA TABLA
	public void eliminarLinea(int fila){
		monto_total-=(double)compra.getValueAt(fila, 4);
		((DefaultTableModel)compra.getModel()).removeRow(fila);
		total_venta.setText("$ "+ String.format("%.2f",Math.round(monto_total*100d)/100d));
	}
	
	public void prepararNuevaVenta(){
		monto_total=0;
		do { 
			((DefaultTableModel)compra.getModel()).removeRow(0);
		}while(((DefaultTableModel)compra.getModel()).getRowCount() != 0);
		total_venta.setText("$ 0,00");
	}
	boolean seleccionar_todo;
	public JButton verVentas,paraRegistrarVenta,seleccionarTodo,paraEliminarLinea;
	JTable compra;
	double monto_total;
	JLabel total_venta;
	DecimalFormat formatea;
}

class RegistrarVenta implements ActionListener{
	
	public RegistrarVenta(JTable compra, DefaultTableModel modeloLineasReparto, String usuario, PanelListaVenta panel){
		this.compra=compra;
		this.modeloLineasReparto = modeloLineasReparto;
		this.usuario=usuario;
		this.panel=panel;		
	}
	@Override
	public void actionPerformed(ActionEvent e) {

		if(compra.getRowCount()!=0){
		//	GestionVenta nuevaVenta = new GestionVenta(compra,modeloLineasReparto,usuario,panel);
			ArrayList<Object[]> lineasventa = new ArrayList<Object[]>();
	
			for(int i=0;i<compra.getRowCount();i++){
				double subtotal = (double)compra.getValueAt(i, 4);
				double cantidad = (double)compra.getValueAt(i, 3);
				double precio_u = (double)compra.getValueAt(i, 2);
				int cod_producto = Integer.parseInt((String)compra.getValueAt(i, 0));
				boolean reparte = (boolean)compra.getValueAt(i, 5);
		
				//CREA EL OBJETO LÍNEA DE VENTA
				Object[] linea = {cod_producto,precio_u,cantidad,subtotal,reparte};
				lineasventa.add(linea);
			}
			EjecutaConsultasEmpleado nuevaConsulta = new EjecutaConsultasEmpleado();
			int codigo_empleado = nuevaConsulta.obtieneId(usuario);
			EjecutaConsultasVenta para_venta_pendiente = new EjecutaConsultasVenta();
			para_venta_pendiente.registrar_venta_sin_facturar(codigo_empleado,lineasventa);
			panel.prepararNuevaVenta();
			
		}else{
			JOptionPane.showMessageDialog(null,"<html><font size=5>¡No ha añadido productos a la venta!</font></html>",
					"Error", JOptionPane.INFORMATION_MESSAGE);
		}
	}
	JTable compra;
	DefaultTableModel modeloLineasReparto;
	String usuario;
	PanelListaVenta panel;
}