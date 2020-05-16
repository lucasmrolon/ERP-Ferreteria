package controlador.stock;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JOptionPane;

import modelo.consultas.EjecutaConsultas;
import vista.stock.PanelStock;
import vista.stock.VentanaEliminarProducto;

//CLASE QUE ELIMINA UN PRODUCTO DE LA BASE DE DATOS
public class EliminarProducto implements ActionListener{
	
	public EliminarProducto(PanelStock padre, VentanaEliminarProducto dialogo,int codigo) {
		
		this.padre=padre;  			//PANEL DE STOCK, PARA ACTUALIZAR TABLA
		this.ventana=dialogo;		//VENTANA DE ELIMINACIÓN
		this.a_eliminar=codigo;		//CODIGO DEL PRODUCTO A ELIMINAR
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		
		//SE ELIMINA EL PRODUCTO
		EjecutaConsultas nueva_consulta = new EjecutaConsultas();
		boolean ok = nueva_consulta.eliminarProducto(a_eliminar);
		if(ok){
			//SE ACTUALIZA LA TABLA
			padre.actualizarTabla(null,'c');
			ventana.dispose();
		}else{
			JOptionPane.showMessageDialog(null,"<html><font size=4>¡Error al eliminar el producto! Vuelta a intentar.</font></html>",
					"¡Error!", JOptionPane.WARNING_MESSAGE);
		}
	}
	
	PanelStock padre;
	VentanaEliminarProducto ventana;
	int a_eliminar;
}
