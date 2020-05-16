package controlador.ventas;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import vista.ventas.PanelListaVenta;

//CLASE PARA ELIMINAR LINEAS DE LA VENTA ACTUAL
public class EliminarLineasVenta implements ActionListener{

	public EliminarLineasVenta(PanelListaVenta panel,JTable tabla, DefaultTableModel modeloTablaProductos){
		this.panel=panel;
		this.tabla=tabla;
		modeloProductos=modeloTablaProductos;
	}
	
	@Override
	public void actionPerformed(ActionEvent arg0) {

		int n_lineas = tabla.getRowCount();
		
		//VERIFICA LAS LÍNEAS MARCADAS
		for(int i=n_lineas;i>=1;i--){
			boolean seleccionado = (boolean)tabla.getValueAt(i-1, 6);
			if(seleccionado){
				int j=0;
				while(Integer.parseInt((String)modeloProductos.getValueAt(j, 0)) != Integer.parseInt((String)tabla.getValueAt(i-1, 0))){
					j++;
				}
				
				//ELIMINA LA LÍNEA Y ACTUALIZA EL STOCK
				modeloProductos.setValueAt((double)modeloProductos.getValueAt(j, 4)+(double)tabla.getValueAt(i-1,3),j, 4);
				panel.eliminarLinea(i-1);
			}
		}
	}

	JTable tabla;
	PanelListaVenta panel;
	DefaultTableModel modeloProductos;
}
