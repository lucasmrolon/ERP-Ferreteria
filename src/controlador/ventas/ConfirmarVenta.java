package controlador.ventas;

import java.awt.event.*;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import modelo.*;
import modelo.consultas.*;
import modelo.objetos.*;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.view.JasperViewer;
import vista.ventas.GestionVenta;
import vista.ventas.PanelFacturacion;

//CLASE QUE REGISTRA LA VENTA EN LA BASE DE DATOS, ASI COMO LAS LINEAS DE VENTA CORRESPONDIENTES, Y LAS LÍNEAS ASIGNADAS A REPARTOS
public class ConfirmarVenta implements ActionListener{
	
	//CONSTRUCTOR PARA VENTAS SIN REPARTO
	public ConfirmarVenta(int cod_venta,JTable compra,String usuario,JComboBox<String> formas,PanelFacturacion panel, GestionVenta ventana){
		this.compra=compra;
		this.cod_venta=cod_venta;
		this.usuario=usuario;
		formasdepago=formas;
		consultasVenta = new EjecutaConsultasVenta();
		this.panel = panel;
		this.ventana=ventana;

	}

	//CONSTRUCTOR PARA VENTAS CON REPARTO
	public ConfirmarVenta(int cod_venta,JTable compra2, DefaultTableModel tablaListaRepartos, String usuario2, JComboBox<String> formas2,JTextField paraNombre,JTextField paraDireccion,
			JComboBox<String> paraTurno, JTextField paraComentarios, PanelFacturacion panel, GestionVenta ventana) {
		this.compra=compra2;
		this.cod_venta=cod_venta;
		this.tablaListaReparto = tablaListaRepartos;
		this.usuario=usuario2;
		formasdepago=formas2;
		consultasVenta = new EjecutaConsultasVenta();
		this.paraNombre = paraNombre;
		this.paraDireccion = paraDireccion;
		this.paraTurno = paraTurno;
		this.paraComentarios = paraComentarios;
		this.panel=panel;
		this.ventana=ventana;
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {

		//CALCULA EL MONTO TOTAL DE VENTA
		double monto_total=0;		
		for(int i=0;i<compra.getRowCount();i++){
			monto_total += (double)compra.getValueAt(i, 4);
		}
		
		//OBTIENE LA HORA Y FECHA ACTUAL
		Date fecha = new Date();
			
		//OBTIENE EL CÓDIGO DE LA FORMA DE PAGO SELECCIONADA
		FormaDePago formadepago = (FormaDePago)formasdepago.getSelectedItem();
		int cod_forma_de_pago = formadepago.getCodigo();
			
		//OBTIENE EL VALOR DEL RECARGO O DESCUENTO, SEGUN CORRESPONDA
		double recargo = formadepago.getRecargo();
		double descuento = formadepago.getDescuento();
		double recdesc = 0;
		if(recargo!=0) recdesc = Math.round(monto_total*recargo)/100d;
		else if(descuento!=0) recdesc=Math.round(-monto_total*descuento)/100d;
			
		//CREA EL OBJETO VENTA
		int cod_cuenta = 0;
		if(formadepago.getTipo().equals("Cuenta corriente")){
			cod_cuenta=ventana.devuelveCodCtaCte();
		}
		Venta nuevaVenta = new Venta(fecha,Math.round(monto_total*100d)/100d,recdesc,0,cod_cuenta,cod_forma_de_pago);
			
		//VERIFICA SI EL STOCK SATISFACE LA VENTA
		boolean[] hay_stock = new boolean[compra.getRowCount()];
		boolean venta_permitida = true;
		
		for(int i=0;i<compra.getRowCount();i++){
			hay_stock[i] = consultasVenta.verificarStock(Integer.parseInt((String)compra.getValueAt(i, 0)),(double)compra.getValueAt(i, 3));
		}
		
		for(int i=0;i<compra.getRowCount();i++){
			if(!hay_stock[i]){
				venta_permitida=false;
			}
		}
		
		//SI HAY STOCK, REALIZA LA FACTURACION, ENVIANDO A REPARTO LOS PRODUCTOS SELECCIONADOS PARA ELLO
		if(venta_permitida==true){
		
			ArrayList<Object[]> paraReparto = consultasVenta.facturarVenta(cod_venta,nuevaVenta);
			if(paraReparto.size()!=0){
				nombre = paraNombre.getText();
				direccion = paraDireccion.getText();
				turno = (String)paraTurno.getSelectedItem();
				observ = paraComentarios.getText();
				for(Object[] lineareparto : paraReparto){
					LineaReparto nuevaLineaReparto = new LineaReparto(nombre,direccion, turno, observ, (int)lineareparto[0], true);
					int cod_linea_reparto = consultasVenta.aniadirLineaReparto(nuevaLineaReparto);
					EjecutaConsultas consulta = new EjecutaConsultas();
					Producto producto = consulta.obtieneProducto((int)lineareparto[1]);
					if(cod_linea_reparto!=0){
						String desc_producto;
						if(producto!=null){
							desc_producto = producto.getDescripcion();
						}else{
							desc_producto = "Producto cod: "+ (int)lineareparto[1];
						}
						SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss a");
						Object[] fila = {cod_linea_reparto,desc_producto,(double)lineareparto[2],direccion,turno,nombre,observ,sdf.format(fecha)};
						tablaListaReparto.addRow(fila);
					}
				}
			}
		
			panel.actualizarModelo();
			
			ventana.dispose();
		
			Thread hiloImpresion = new Thread(new ImprimirReporte());
			hiloImpresion.run();
			
		}	
	}
	
	//MÉTODO QUE OBTIENE UN COMPROBANTE DE VENTA IMPRIMIBLE
	private class ImprimirReporte implements Runnable{

		@Override
		public void run() {
			
			JasperReport reporte = null;
			Conexion conexion = new Conexion();
			try{	
				reporte = (JasperReport)JRLoader.loadObjectFromFile("reportes/ticket_venta.jasper");
		
				Connection miconexion = conexion.dameConexion();
				Map<String, Object> parametros = new HashMap<String, Object>();
				parametros.put("nfactura", cod_venta);
		    
				JasperPrint jasperprint = JasperFillManager.fillReport(reporte,parametros,miconexion);
				//JasperPrintManager.printReport(jasperprint, false);
						
				try {
					miconexion.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
				JasperViewer.viewReport(jasperprint, false);
			}catch(JRException e){
				e.printStackTrace();
			}			
			
		}
		
	}
	
	String nombre,direccion,turno,observ;
	boolean hay_reparto;
	JTable compra;
	int cod_venta;
	DefaultTableModel tablaListaReparto;
	String usuario;
	JComboBox<String> formasdepago;
	EjecutaConsultasVenta consultasVenta;
	JTextField paraNombre,paraDireccion,paraComentarios;
	JComboBox<String> paraTurno;
	PanelFacturacion panel;
	GestionVenta ventana;
}


