package controlador.compras;

import java.util.ArrayList;
import java.util.Date;

import javax.swing.table.TableModel;

import modelo.consultas.EjecutaConsultasProveedor;
import modelo.objetos.LineaPedido;
import modelo.objetos.Pedido;

//CLASE QUE REGISTRA LA CREACIÓN DE UN PEDIDO
public class RegistrarPedido {
	
	public RegistrarPedido(TableModel modelopedido,double monto_pedido,String proveedor,int n_pedido){
		
		Date emision = new Date();
		
		EjecutaConsultasProveedor paraConsultas = new EjecutaConsultasProveedor();
		String cod_proveedor = paraConsultas.consultaCodProveedor(proveedor);
		
		Pedido nuevo_pedido = new Pedido(n_pedido,emision,cod_proveedor);
		
		ArrayList<LineaPedido> lineasdepedido = new ArrayList<LineaPedido>();
		
		//OBTIENE LOS DATOS DE CADA LINEA DE PEDIDO
		for(int i=0;i<modelopedido.getRowCount();i++){
			
			int cod_producto = Integer.parseInt((String)modelopedido.getValueAt(i, 0));
			double precio_u = (Double)modelopedido.getValueAt(i, 2);
			double cantidad = (Double)modelopedido.getValueAt(i, 3);
			double subtotal = (Double)modelopedido.getValueAt(i, 4);
			
			LineaPedido linea = new LineaPedido(n_pedido,0,cod_producto,precio_u,cantidad,subtotal);
			lineasdepedido.add(linea);
		}
		
		//REGISTRA EL PEDIDO
		paraConsultas.registrarNuevoPedido(nuevo_pedido, lineasdepedido);
		
	}

}
