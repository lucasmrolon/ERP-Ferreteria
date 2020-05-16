package controlador.compras;

import java.util.ArrayList;

import javax.swing.table.DefaultTableModel;

import modelo.consultas.EjecutaConsultas;
import modelo.consultas.EjecutaConsultasProveedor;
import modelo.objetos.LineaPedido;
import modelo.objetos.Producto;

//CLASE QUE PERMITE CONSULTAR UN PEDIDO
public class ConsultarDatosDePedidos {
	
	public ConsultarDatosDePedidos(){
		
		paraConsultas = new EjecutaConsultasProveedor();
		
	}

	public int obtener_n_prox_pedido(){
		
		int n_prox = paraConsultas.obtenerNProxPedido();
		
		return n_prox;
		
	}
	
	//OBTIENE LA TABLA DE LINEAS DE PEDIDO CORRESPONDIENTE AL PEDIDO SELECCIONADO
	public DefaultTableModel obtenerModeloLineasDePedido(int cod_pedido){
		
		ArrayList<LineaPedido> lineas_pedido = paraConsultas.obtenerLineasDePedido(cod_pedido);
		
		EjecutaConsultas consultaProducto = new EjecutaConsultas();
		
		//ESTABLECE LOS NOMBRES DE LAS COLUMNAS
		String[] columnas = {"COD. LINEA","COD. PRODUCTO","DESCRIPCIÓN","PRECIO U.","CANTIDAD","SUBTOTAL"};
		DefaultTableModel modeloTabla= new DefaultTableModel(null,columnas){
			/**
			 * 
			 */
			private static final long serialVersionUID = 7174176482412643724L;

			public boolean isCellEditable(int rowIndex,int columnIndex){
				return false;	
			}
		};
		
		//CARGA LAS LÍNEAS DE PEDIDO
		for(LineaPedido l:lineas_pedido){
			int cod_producto = l.getCodigo_producto();
			Producto producto = consultaProducto.obtieneProducto(cod_producto);
			String desc_producto = producto.getDescripcion();
			
			String cod_linea = String.format("%05d", l.getCodigo_linea());
			String cod_prod = String.format("%05d", l.getCodigo_producto());
			
			Object[] fila = {cod_linea,cod_prod,desc_producto,l.getPrecio_u(),l.getCantidad(),l.getSubtotal()};
			
			modeloTabla.addRow(fila);
		}
		
		return modeloTabla;
	}
	
	EjecutaConsultasProveedor paraConsultas;
	
}
