package controlador.stock;

import java.awt.Window;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import vista.stock.*;

//CLASE QUE GESTIONA LAS ACCIONES SOBRE LOS PRODUCTOS
public class GestionarProductos implements ActionListener{

	public GestionarProductos(PanelStock panel,JTable tablaProductos){

		this.panel = panel;					
		this.tablaProductos=tablaProductos;		
		
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		
		//SI DESEA CREAR NUEVO PRODUCTO
		if(e.getSource()==panel.nuevoProducto){
			VentanaCrearProducto ventanaCreacion = new VentanaCrearProducto(panel);
			ventanaCreacion.setVisible(true);
		}
		
		//SI DESEA ACTUALIZAR LOS PRECIOS
		else if(e.getSource()==panel.actualizaPrecios){	
			new VentanaActualizarPrecios(panel);	
		}
		
		//SI DESEA ACTUALIZAR EL STOCK DE UN PRODUCTO
		else if(e.getSource()==panel.actualizaStock){
			try{
				int seleccionado = tablaProductos.getSelectedRow();
			
				int codigo = Integer.parseInt((String)tablaProductos.getValueAt(seleccionado, 0));
				String desc = (String)tablaProductos.getValueAt(seleccionado, 1);
				double cantidad = (double)tablaProductos.getValueAt(seleccionado, 4);
				String unidad = (String)tablaProductos.getValueAt(seleccionado, 5);
			
				new VentanaActualizarStock(tablaProductos,codigo,desc,cantidad,unidad);
			}catch(Exception ex){
				String mensaje = "<html><Font size=5>¡Debe seleccionar un producto!</Font></html>";
				JOptionPane.showMessageDialog(null, mensaje, "¡Error!", JOptionPane.WARNING_MESSAGE);
			}
		}
		else{
			try{	
				seleccionado = tablaProductos.getSelectedRow();
				int seleccionado_real = tablaProductos.convertRowIndexToModel(seleccionado);
				DefaultTableModel modelo = (DefaultTableModel)tablaProductos.getModel();
			
				//SI DESEA ELIMINAR UN PRODUCTO
				if(e.getSource()==panel.eliminarProducto){
					VentanaEliminarProducto ventanaEliminacion = new VentanaEliminarProducto(panel,modelo,seleccionado_real);
					ventanaEliminacion.setVisible(true);
					
				//SI DESEA MODIFICAR LOS ATRIBUTOS DE UN PRODUCTO
				}else if(e.getSource()==panel.modificarProducto){
					VentanaModificarProducto ventanaModificacion = new VentanaModificarProducto(panel,modelo,seleccionado_real);
					ventanaModificacion.setVisible(true);
				}
				
			
			//SI SOLICITA MODIFICAR O ELIMINAR SIN HABER SELECCIONADO UN PRODUCTO, SE MUESTRA UN MENSAJE DE ERROR
			}catch(Exception ex){
				String mensaje = "<html><Font size=5>¡Debe seleccionar un producto!</Font></html>";
				JOptionPane.showMessageDialog(null, mensaje, "¡Error!", JOptionPane.WARNING_MESSAGE);
			}
		}
	}
	
	int seleccionado;
	PanelStock panel;
	Window ventana;
	JTable tablaProductos;
	Window window;
}
