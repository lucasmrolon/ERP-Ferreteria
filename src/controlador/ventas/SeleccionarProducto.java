package controlador.ventas;

import java.awt.event.*;

import javax.swing.JOptionPane;
import javax.swing.JSpinner;
import javax.swing.table.DefaultTableModel;

import modelo.consultas.EjecutaConsultas;
import modelo.objetos.Producto;
import vista.ventas.PanelListaVenta;
import vista.ventas.VentanaSeleccionCantidad;

//CLASE QUE PERMITE SELECCIONAR UN PRODUCTO EN LA PANTALLA DE VENTAS
public class SeleccionarProducto implements ActionListener{
	
	public SeleccionarProducto(VentanaSeleccionCantidad ventana, int cod, JSpinner paraCantidad,PanelListaVenta venta_actual,
			DefaultTableModel tablaProductos, int seleccionado) {

		codigo=cod;
		this.paraCantidad=paraCantidad;
		this.venta_actual=venta_actual;
		this.ventana=ventana;
		this.tablaProductos = tablaProductos;
		this.seleccionado=seleccionado;
		
	}
	
	@Override
	public void actionPerformed(ActionEvent arg0) {
		
		//OBTIENE EL PRODUCTO SELECCIONADO A PARTIR DE SU CODIGO
		EjecutaConsultas nuevaConsulta = new EjecutaConsultas();
		Producto a_aniadir = nuevaConsulta.obtieneProducto(codigo);
		
		if(a_aniadir!=null){
			//OBTIENE DESCRIPCION, PRECIO, CANTIDAD Y SUBTOTAL DEL PRODUCTO
			String desc = a_aniadir.getDescripcion();
			double prec_u = a_aniadir.getPrecio();
			double cantidad = Math.round((double)paraCantidad.getValue()*100d)/100d;
			double subtotal = Math.round(cantidad*prec_u*100d)/100d;
		
			//RESTA LA CANTIDAD AL STOCK ACTUAL DEL PRODUCTO
			double nuevo_stock = (double)tablaProductos.getValueAt(seleccionado, 4) - cantidad;
			tablaProductos.setValueAt(Math.round(nuevo_stock*100d)/100d, seleccionado, 4 );
		
			//AÑADE LA LÍNEA A LA LISTA DE VENTA
			venta_actual.aniadirLinea(codigo,desc,prec_u,cantidad,subtotal);
			ventana.dispose();
		}else{
			JOptionPane.showMessageDialog(null,"<html><font size=4>¡Error al obtener el producto! Intente nuevamente.</font></html>",
					"Eliminar Línea", JOptionPane.WARNING_MESSAGE);
		}
	}
		
	int codigo,seleccionado;
	JSpinner paraCantidad;
	PanelListaVenta venta_actual;
	VentanaSeleccionCantidad ventana;
	DefaultTableModel tablaProductos;
}
