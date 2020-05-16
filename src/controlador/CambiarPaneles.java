package controlador;

import java.awt.BorderLayout;
import java.awt.event.*;
import javax.swing.JPanel;
import vista.*;
import vista.compras.PanelCompras;
import vista.detalle.PanelDetalle;
import vista.estadisticas.PanelEstadisticas;
import vista.stock.PanelStock;
import vista.ventas.PanelFacturacion;
import vista.ventas.PanelVentas;

//ESTA CLASE CONTROLA LOS PANELES PRINCIPALES DE LA APLICACIÓN
public class CambiarPaneles implements ActionListener{
	
	public CambiarPaneles(JPanel contenedor,PanelVentas ventas,PanelStock stock,PanelRepartos repartos,
			PanelEstadisticas estadisticas, PanelDetalle detalle, PanelFacturacion facturacion, PanelCompras compras){
		
		this.contenedor=contenedor;
		this.ventas=ventas;		//PANEL DE VENTAS
		this.stock=stock;		//PANEL DE STOCK
		this.repartos=repartos; //PANEL DE REPARTOS
		this.estadisticas=estadisticas;   //PANEL DE ESTADISTICAS
		this.detalle = detalle; //PANEL DE DETALLE
		this.facturacion = facturacion;  //PANEL DE FACTURACIÓN
		this.compras = compras; //PANEL DE COMPRAS
		
	}
	public CambiarPaneles(JPanel contenedor,PanelVentas ventas,PanelRepartos repartos, PanelCompras compras){
		
		this.contenedor=contenedor;
		this.ventas=ventas;		//PANEL DE VENTAS
		this.repartos=repartos; //PANEL DE REPARTOS
		this.compras = compras; //PANEL DE COMPRAS
		
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		
		//REMUEVE EL PANEL ACTUAL Y MUESTRA EL QUE SE ACABA DE SELECCIONAR
		contenedor.remove(1);
		
		if(e.getActionCommand().equals("F1 | STOCK")){
			contenedor.add(stock, BorderLayout.CENTER);
			stock.paraBusqueda.requestFocus();
			stock.tablaProductos.clearSelection();
		}else if(e.getActionCommand().equals("F2 | VENTAS")){
			contenedor.add(ventas, BorderLayout.CENTER);
			ventas.busqueda.paraBusqueda.requestFocus();
		}else if(e.getActionCommand().equals("F4 | REPARTOS")){
			contenedor.add(repartos, BorderLayout.CENTER);
			repartos.confirmarReparto.requestFocus();
		}else if(e.getActionCommand().equals("F6 | ESTADÍSTICAS")){
			contenedor.add(estadisticas, BorderLayout.CENTER);
			estadisticas.paraActualizar.actualizar.requestFocus();
		}else if(e.getActionCommand().equals("F7 | DETALLE")){
			contenedor.add(detalle, BorderLayout.CENTER);
			detalle.copia_seguridad.requestFocus();
		}else if(e.getActionCommand().equals("F3 | FACTURACIÓN")){
			contenedor.add(facturacion, BorderLayout.CENTER);
			facturacion.facturarVenta.requestFocus();
		}else if(e.getActionCommand().equals("F8 | COMPRAS")){
			contenedor.add(compras, BorderLayout.CENTER);
			compras.tablaProveedores.requestFocus();
		}
		
		contenedor.repaint();
		contenedor.validate();
		
	}

	JPanel contenedor;
	PanelVentas ventas;
	PanelStock stock;
	PanelRepartos repartos;
	PanelEstadisticas estadisticas;
	PanelDetalle detalle;
	PanelFacturacion facturacion;
	PanelCompras compras;
	
}
