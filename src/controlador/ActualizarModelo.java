package controlador;

import java.awt.event.*;

import javax.swing.*;
import javax.swing.table.*;

import vista.PanelRepartos;
import vista.stock.PanelStock;
import vista.ventas.PanelBusqueda;
import vista.ventas.PanelFacturacion;

//CLASE QUE ACTUALIZA LAS TABLAS 
public class ActualizarModelo implements Runnable{

	public ActualizarModelo(JTable tabla,JPanel panel,String nombre_tabla){
		this.tabla=tabla;
		this.panel_padre=panel;
		this.nombre_tabla = nombre_tabla;
	}
	@Override
	public void run() {
		
		//SE ESTABLECE EL TIMER CORRESPONDIENTE A LA TABLA
		Timer nuevoTimer = null;
		//1 MINUTO PARA LA TABLA DE PRODUCTOS EN VENTA
		if(nombre_tabla.equals("productos")){
			ActualizarProductos actualizarProductos = new ActualizarProductos(tabla,panel_padre);
			nuevoTimer = new Timer(60000,actualizarProductos);
		}
		//10 SEGUNDOS PARA LA TABLA DE PRODUCTOS PARA REPARTO
		else if(nombre_tabla.equals("repartos")){
			ActualizarRepartos actualizarRepartos = new ActualizarRepartos(tabla,panel_padre);
			nuevoTimer = new Timer(10000,actualizarRepartos);
		}
		//3 SEGUNDOS PARA LA TABLA DE STOCK
		else if(nombre_tabla.equals("stock")){
			ActualizarStock actualizarStock = new ActualizarStock(tabla,panel_padre);
			nuevoTimer = new Timer(3000,actualizarStock);
		}
		//3 SEGUNDOS PARA LA TABLA DE VENTAS SIN FACTURAR
		else if(nombre_tabla.equals("ventas_sin_facturar")){
			ActualizarVentas actualizarVentas= new ActualizarVentas(tabla,panel_padre);
			nuevoTimer = new Timer(3000,actualizarVentas);
		}
		nuevoTimer.start();
	}
	
	JTable tabla;
	JPanel panel_padre;
	String nombre_tabla;
	
}

//ACTUALIZA LA TABLA DE VENTA
class ActualizarProductos implements ActionListener{
	
	public ActualizarProductos(JTable tabla,JPanel panel_padre){
		this.tabla=tabla;
		paraActualizar = new CrearTabla();
		panel_busqueda = (PanelBusqueda)panel_padre;
	}
	
	@Override
	public void actionPerformed(ActionEvent arg0) {
		modelo = paraActualizar.obtenerTablaProductos();
		int seleccion = tabla.getSelectedRow();
		tabla.setModel(modelo);
		tabla.changeSelection(seleccion, 0, false, false);
		panel_busqueda.darFormatoATabla();
	}
	
	DefaultTableModel modelo;
	CrearTabla paraActualizar;
	JTable tabla;
	PanelBusqueda panel_busqueda;
}

//ACTUALIZA LA TABLA DE STOCK
class ActualizarStock implements ActionListener{
	
	public ActualizarStock(JTable tabla,JPanel panel_padre){
		this.tabla=tabla;
		paraActualizar = new CrearTabla();
		panel_stock = (PanelStock)panel_padre;
	}
	
	@Override
	public void actionPerformed(ActionEvent arg0) {
		modelo = paraActualizar.obtenerTablaProductos();
		int seleccion = tabla.getSelectedRow();
		tabla.setModel(modelo);
		tabla.changeSelection(seleccion, 0, false, false);
		panel_stock.darFormatoATabla();
	}
	
	DefaultTableModel modelo;
	CrearTabla paraActualizar;
	JTable tabla;
	PanelStock panel_stock;
}

//ACTUALIZA LA TABLA DE REPARTOS
class ActualizarRepartos implements ActionListener{
	
	public ActualizarRepartos(JTable tabla,JPanel panel_padre){
		this.tabla=tabla;
		paraActualizar = new CrearTabla();
		panel_repartos = (PanelRepartos)panel_padre;
	}
	@Override
	public void actionPerformed(ActionEvent arg0) {
		modelo = paraActualizar.obtenerTablaRepartos();
		int seleccion = tabla.getSelectedRow();
		tabla.setModel(modelo);
		tabla.changeSelection(seleccion, 0, false, false);
		
		tcr = new DefaultTableCellRenderer();
		tcr.setHorizontalAlignment(SwingConstants.CENTER);
		
		TableColumn columnaCodigo = tabla.getColumnModel().getColumn(0);
		columnaCodigo.setMaxWidth(70);
		columnaCodigo.setCellRenderer(tcr);
		tabla.getColumnModel().getColumn(1).setCellRenderer(tcr);
	}
	
	
	DefaultTableModel modelo;
	CrearTabla paraActualizar;
	JTable tabla;
	PanelRepartos panel_repartos;
	DefaultTableCellRenderer tcr;
}

//ACTUALIZA LA TABLA DE VENTAS SIN FACTURAR
class ActualizarVentas implements ActionListener{
	
	public ActualizarVentas(JTable tabla,JPanel panel_padre){
		this.tabla=tabla;
		paraActualizar = new CrearTabla();
		panel_facturacion = (PanelFacturacion)panel_padre;
	}
	
	@Override
	public void actionPerformed(ActionEvent arg0) {
		modelo = paraActualizar.obtenerVentas();
		int seleccion = tabla.getSelectedRow();
		tabla.setModel(modelo);
		tabla.changeSelection(seleccion, 0, false, false);
		panel_facturacion.darFormatoATabla(tabla);
	}
	
	DefaultTableModel modelo;
	CrearTabla paraActualizar;
	JTable tabla;
	PanelFacturacion panel_facturacion;
}