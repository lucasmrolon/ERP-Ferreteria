package controlador.detalle;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import modelo.consultas.*;
import modelo.objetos.*;
import vista.detalle.PanelDetalle;

//CLASE QUE ELIMINA UN REGISTRO DE LA TABLA CORRESPONDIENTE
public class EliminarRegistro implements ActionListener{

	//CONSTRUCTOR
	public EliminarRegistro(JComboBox<String> tipoTabla,JTable tablaCuentas){
		this.tabla = tablaCuentas;
		this.tipoTabla=tipoTabla;
	}
	
	@Override
	public void actionPerformed(ActionEvent arg0) {
		
		//OBTIENE LA FILA SELECCIONADA
		String tabla_seleccionada = (String)tipoTabla.getSelectedItem();
		
		JScrollPane contenedor = (JScrollPane)tabla.getParent().getParent();
		PanelDetalle panel = (PanelDetalle)contenedor.getParent();
		
		int seleccionado = tabla.getSelectedRow();
		
		//VERIFICA QUE HAYA SELECCIONADA UNA FILA
		if(seleccionado!=-1){
			
			//SI SELECCIONA LA TABLA DE CUENTAS CORRIENTES
			if(tabla_seleccionada.equals("CLIENTES")){
			
				//OBTIENE EL CÓDIGO DE LA CUENTA Y EL DNI DEL CLIENTE
				
				String cod_cuenta = String.valueOf(tabla.getValueAt(seleccionado, 6));
				String dni = (String)tabla.getValueAt(seleccionado, 0);
		
				//MUESTRA MENSAJE PARA CONFIRMACIÓN
				if(!cod_cuenta.equals("")){
					String mensaje = "<html><Font size=5>Para eliminar un cliente, elimine primero la cuenta corriente asociada</Font></html>";
					JOptionPane.showMessageDialog(null, mensaje, "Aviso", JOptionPane.WARNING_MESSAGE, null);
					
					
				}
				else{
					String mensaje = "<html><Font size=5>¿Está seguro que desea eliminar el cliente seleccionado?</Font></html>";
					int opcion = JOptionPane.showConfirmDialog(null, mensaje, "Ventana de confirmación", JOptionPane.YES_NO_OPTION,JOptionPane.WARNING_MESSAGE);
			
					//SI CONFIRMA LA OPERACIÓN, ELIMINA EL REGISTRO Y ACTUALIZA LA TABLA, INCLUYENDO LA UBICACIÓN DE LOS BOTONES
					if(opcion==JOptionPane.YES_OPTION){
						consultaCtasCtes = new EjecutaConsultasCtasCtes();
						boolean ok = consultaCtasCtes.eliminarCliente(dni);
		
						if(ok){
							if(tabla.getRowCount()<=10){
								contenedor.setSize(contenedor.getSize().width, contenedor.getSize().height-tabla.getRowHeight());
								panel.actualizar.setLocation(panel.actualizar.getLocation().x, panel.actualizar.getLocation().y-tabla.getRowHeight());
								panel.abrir_ctacte.setLocation(panel.abrir_ctacte.getLocation().x, panel.abrir_ctacte.getLocation().y-tabla.getRowHeight());
								panel.cerrar_ctacte.setLocation(panel.cerrar_ctacte.getLocation().x, panel.cerrar_ctacte.getLocation().y-tabla.getRowHeight());
								panel.aniadir.setLocation(panel.aniadir.getLocation().x, panel.aniadir.getLocation().y-tabla.getRowHeight());
								panel.modif.setLocation(panel.modif.getLocation().x, panel.modif.getLocation().y-tabla.getRowHeight());
								panel.elim.setLocation(panel.elim.getLocation().x, panel.elim.getLocation().y-tabla.getRowHeight());
							}
							((DefaultTableModel)tabla.getModel()).removeRow(seleccionado);
						}else{
							mensaje = "<html><Font size=5>¡No se pudo eliminar la Cuenta corriente. Intente nuevamente.</Font></html>";
							JOptionPane.showMessageDialog(null, mensaje, "¡Error!",JOptionPane.WARNING_MESSAGE);
						}
					}
				}
			}
			
			//SI SELECCIONA LA TABLA DE RUBROS...
			else if(tabla_seleccionada.equals("RUBROS")){
				
				//OBTIENE EL CODIGO Y NOMBRE DEL RUBRO
				int codigo_rubro = (int)tabla.getValueAt(seleccionado, 0);
				String nombre = (String)tabla.getValueAt(seleccionado, 1);
		
				//CREA UN OBJETO RUBRO
				Rubro rubro = new Rubro(codigo_rubro,nombre,null);
				
				//MUESTRA MENSAJE PARA CONFIRMACIÓN
				String mensaje = "<html><Font size=5>¿Está seguro que desea eliminar el rubro seleccionado?</Font></html>";
				int opcion = JOptionPane.showConfirmDialog(null, mensaje, "Ventana de confirmación", JOptionPane.YES_NO_OPTION,JOptionPane.WARNING_MESSAGE);
			
				//SI CONFIRMA LA OPERACIÓN, ELIMINA EL REGISTRO Y ACTUALIZA LA BASE DE DATOS
				if(opcion==JOptionPane.YES_OPTION){
					consultaRubros = new EjecutaConsultas();
					boolean ok = consultaRubros.eliminarRubro(rubro);
		
					if(ok){
						if(tabla.getRowCount()<=10){
							contenedor.setSize(contenedor.getSize().width, contenedor.getSize().height-tabla.getRowHeight());
							panel.aniadir.setLocation(panel.aniadir.getLocation().x, panel.aniadir.getLocation().y-tabla.getRowHeight());
							panel.modif.setLocation(panel.modif.getLocation().x, panel.modif.getLocation().y-tabla.getRowHeight());
							panel.elim.setLocation(panel.elim.getLocation().x, panel.elim.getLocation().y-tabla.getRowHeight());				
						}
						((DefaultTableModel)tabla.getModel()).removeRow(seleccionado);
					}else{
						JOptionPane.showMessageDialog(null,"<html><font size=4>¡No se pudo eliminar el rubro! Intente nuevamente.</font></html>",
								"Eliminar Línea", JOptionPane.WARNING_MESSAGE);
					}
				}
			}
			
			//SI SELECCIONA LA TABLA DE PROVEEDORES
			else if(tabla_seleccionada.equals("PROVEEDORES")){
				
				//OBTIENE LOS DATOS DEL PROVEEDOR
				String codigo = (String)tabla.getValueAt(seleccionado,0);
				String nombre = (String)tabla.getValueAt(seleccionado,1);
				String domicilio = (String)tabla.getValueAt(seleccionado,2);
				String telefono = (String)tabla.getValueAt(seleccionado,3);
				String email = (String)tabla.getValueAt(seleccionado,4);
		
				//CREA UN OBJETO PROVEEDOR
				Proveedor proveedor = new Proveedor(codigo,nombre,domicilio,telefono,email);

				//MUESTRA MENSAJE PARA CONFIRMACIÓN
				String mensaje = "<html><Font size=5>¿Está seguro que desea eliminar el proveedor seleccionado?</Font></html>";
				int opcion = JOptionPane.showConfirmDialog(null, mensaje, "Ventana de confirmación", JOptionPane.YES_NO_OPTION,JOptionPane.WARNING_MESSAGE);
			
				//SI CONFIRMA LA OPERACIÓN, ELIMINA EL REGISTRO Y ACTUALIZA LA BASE DE DATOS
				if(opcion==JOptionPane.YES_OPTION){
					consultaProveedor = new EjecutaConsultasProveedor();
					boolean ok = consultaProveedor.eliminarProveedor(proveedor);
		
					if(ok){
						if(tabla.getRowCount()<=10){
							contenedor.setSize(contenedor.getSize().width, contenedor.getSize().height-tabla.getRowHeight());
							panel.aniadir.setLocation(panel.aniadir.getLocation().x, panel.aniadir.getLocation().y-tabla.getRowHeight());
							panel.modif.setLocation(panel.modif.getLocation().x, panel.modif.getLocation().y-tabla.getRowHeight());
							panel.elim.setLocation(panel.elim.getLocation().x, panel.elim.getLocation().y-tabla.getRowHeight());
						}
						((DefaultTableModel)tabla.getModel()).removeRow(seleccionado);
					}else{
						JOptionPane.showMessageDialog(null,"<html><font size=4>¡No se pudo eliminar el Proveedor! Intente nuevamente.</font></html>",
								"¡Error!", JOptionPane.WARNING_MESSAGE);
					}
				}
			}
			
			//SI SELECCIONA LA TABLA DE EMPLEADOS...
			else if(tabla_seleccionada.equals("EMPLEADOS")){
				
				if(seleccionado!=0){
					//OBTIENE LOS DATOS DEL EMPLEADO
					int codigo = (int)tabla.getValueAt(seleccionado,0);
					String tipo = (String)tabla.getValueAt(seleccionado,1);
					String nombre = (String)tabla.getValueAt(seleccionado,2);
					String apellido = (String)tabla.getValueAt(seleccionado,3);
					String dni = (String)tabla.getValueAt(seleccionado,4);
					String domicilio = (String)tabla.getValueAt(seleccionado,5);
					String email = (String)tabla.getValueAt(seleccionado,6);
					String telefono = (String)tabla.getValueAt(seleccionado,7);
					String usuario = (String)tabla.getValueAt(seleccionado,8);
					String password = (String)tabla.getValueAt(seleccionado,9);
				
					//CREA UN OBJETO EMPLEADO
					Empleado empleado = new Empleado(codigo,tipo,nombre,apellido,dni,domicilio,email,telefono,usuario,password);
				
					//MUESTRA MENSAJE PARA CONFIRMACIÓN
					String mensaje = "<html><Font size=5>¿Está seguro que desea eliminar el empleado seleccionado?</Font></html>";
					int opcion = JOptionPane.showConfirmDialog(null, mensaje, "Ventana de confirmación", JOptionPane.YES_NO_OPTION,JOptionPane.WARNING_MESSAGE);
			
					//SI CONFIRMA LA OPERACIÓN, ELIMINA EL REGISTRO Y ACTUALIZA LA BASE DE DATOS
					if(opcion==JOptionPane.YES_OPTION){
						consultaEmpleado = new EjecutaConsultasEmpleado();
						boolean ok = consultaEmpleado.eliminarEmpleado(empleado);
		
						if(ok){
							if(tabla.getRowCount()<=10){
								contenedor.setSize(contenedor.getSize().width, contenedor.getSize().height-tabla.getRowHeight());
								panel.aniadir.setLocation(panel.aniadir.getLocation().x, panel.aniadir.getLocation().y-tabla.getRowHeight());
								panel.modif.setLocation(panel.modif.getLocation().x, panel.modif.getLocation().y-tabla.getRowHeight());
								panel.elim.setLocation(panel.elim.getLocation().x, panel.elim.getLocation().y-tabla.getRowHeight());
							}
							((DefaultTableModel)tabla.getModel()).removeRow(seleccionado);
						}
						else{
							mensaje = "<html><Font size=5>¡No se pudo eliminar el Empleado! Intente nuevamente.</Font></html>";
							JOptionPane.showMessageDialog(null, mensaje, "¡Error!", JOptionPane.WARNING_MESSAGE);
						}
					}
				}else{
					String mensaje = "<html><Font size=5>¡No está permitido eliminar el usuario 'admin'!</Font></html>";
					JOptionPane.showMessageDialog(null, mensaje, "¡Error!", JOptionPane.WARNING_MESSAGE);
				}
			}
			
		//SI NO HA SELECCIONADO NINGUNA FILA, MUESTRA UN MENSAJE DE ERROR
		}else{
			String mensaje=null;
			if(tabla_seleccionada.equals("CLIENTES")){
				mensaje = "<html><Font size=5>¡No ha seleccionado ningún cliente!</Font></html>";
			}
			else if(tabla_seleccionada.equals("RUBROS")){
				mensaje = "<html><Font size=5>¡No ha seleccionado ningún rubro!</Font></html>";
			}
			else if(tabla_seleccionada.equals("PROVEEDORES")){
				mensaje = "<html><Font size=5>¡No ha seleccionado ningún proveedor!</Font></html>";
			}
			else if(tabla_seleccionada.equals("EMPLEADOS")){
				mensaje = "<html><Font size=5>¡No ha seleccionado ningún empleado!</Font></html>";
			}
			JOptionPane.showMessageDialog(null, mensaje, "¡Error!", JOptionPane.WARNING_MESSAGE);
		}
	}
	
	JComboBox<String> tipoTabla;
	JTable tabla;
	EjecutaConsultasCtasCtes consultaCtasCtes;
	EjecutaConsultas consultaRubros;
	EjecutaConsultasProveedor consultaProveedor;
	EjecutaConsultasEmpleado consultaEmpleado;
}
