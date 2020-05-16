package controlador.compras;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import modelo.consultas.EjecutaConsultas;
import modelo.consultas.EjecutaConsultasEmpleado;
import modelo.objetos.LineaPedido;
import vista.compras.VerPedidos;

//CLASE PARA REGISTRAR LA RECEPCIÓN DE UN PEDIDO
public class ConfirmarRecepcion implements ActionListener{
	
	public ConfirmarRecepcion(boolean[] error,JTable tablaPedido,int cod_pedido,String usuario,VerPedidos ventana){
	
		this.error=error;
		this.tablaPedido=tablaPedido;
		this.cod_pedido=cod_pedido;
		this.usuario=usuario;
		this.ventana=ventana;
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		
		//SE COMPRUEBA QUE NO EXISTAN ERRORES
		if(error[0]==false){
			DefaultTableModel modeloPedido = (DefaultTableModel)tablaPedido.getModel();
		
			ArrayList<LineaPedido> lineas_recibidas = new ArrayList<LineaPedido>();
		
			//PARA CADA LINEA DE PEDIDO, SE OBTIENE CODIGO, STOCK Y CANTIDAD PARA PODER ACTUALIZAR EL STOCK
			for(int i=0;i<modeloPedido.getRowCount();i++){
			
				boolean marcado = (boolean)modeloPedido.getValueAt(i, 0);
				if(marcado){
					int cod_linea = Integer.parseInt((String)modeloPedido.getValueAt(i, 1));
					int cod_producto = Integer.parseInt((String)modeloPedido.getValueAt(i, 2));
				
					double prec_u=0;
					Object columna_prec_u = modeloPedido.getValueAt(i, 4);
					try{
						prec_u = (double)columna_prec_u;
					}catch(ClassCastException e){
						prec_u = Double.parseDouble((String)columna_prec_u);
					}
					double cant=0;
					Object columna_cant = modeloPedido.getValueAt(i, 5);
					try{
						cant = (double)columna_cant;
					}catch(ClassCastException e){
						cant = Double.parseDouble((String)columna_cant);
					}
			
					double subtotal = (double)modeloPedido.getValueAt(i, 6);
				
					
					LineaPedido linea_recibida = new LineaPedido(cod_pedido,cod_linea,cod_producto,prec_u,cant,subtotal);
					lineas_recibidas.add(linea_recibida);	
				}	
			}
		
			para_obtener_id = new EjecutaConsultasEmpleado();
			int id_usuario = para_obtener_id.obtieneId(usuario);
		
			para_consultas = new EjecutaConsultas();
			ventana.dispose();
			//SE ACTUALIZA EL STOCK
			para_consultas.registrarRecepcionYActualizarStock(cod_pedido,lineas_recibidas, id_usuario);
		
		}else{
			JOptionPane.showMessageDialog(null,"<html><font size=4>¡Ha ingresado datos inválidos!</font></html>",
					"¡Error!", JOptionPane.WARNING_MESSAGE);
		}
	}
	
	EjecutaConsultasEmpleado para_obtener_id;
	EjecutaConsultas para_consultas;
	JTable tablaPedido;
	int cod_pedido;
	String usuario;
	VerPedidos ventana;
	boolean[] error;
}
