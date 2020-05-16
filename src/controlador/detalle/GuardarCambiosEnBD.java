package controlador.detalle;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;

import modelo.consultas.EjecutaConsultas;
import modelo.consultas.EjecutaConsultasCtasCtes;
import modelo.consultas.EjecutaConsultasEmpleado;
import modelo.consultas.EjecutaConsultasProveedor;
import modelo.consultas.EjecutaConsultasVenta;
import modelo.objetos.Cliente;
import modelo.objetos.CuentaCorriente;
import modelo.objetos.Empleado;
import modelo.objetos.FormaDePago;
import modelo.objetos.Proveedor;
import modelo.objetos.Rubro;
import vista.detalle.PanelDetalle;

//CLASE QUE CAPTURA LOS CAMBIOS EN LAS TABLAS Y ACTUALIZA LA BASE DE DATOS
public class GuardarCambiosEnBD implements ActionListener {

	//COSNTRUCTOR
	public GuardarCambiosEnBD(PanelDetalle panel,JComboBox<String> seleccion, JTable tabla){
		this.seleccion = seleccion;
		this.tabla = tabla;
		this.panel = panel;
	}
	@Override
	public void actionPerformed(ActionEvent arg0) {
		
		TableCellRenderer tcr = tabla.getColumnModel().getColumn(1).getCellRenderer();
		//SI SELECCIONA LA TABLA DE CLIENTES...
		if(seleccion.getSelectedItem().equals("CLIENTES")){
			
			//SI ESTÁ CREANDO...
			if(!panel.aniadir.isEnabled()){
				int ultima_fila = tabla.getRowCount()-1;
				
				String dni = (String)tabla.getValueAt(ultima_fila,0);
				String nombre = (String)tabla.getValueAt(ultima_fila,1);
				String apellido = (String)tabla.getValueAt(ultima_fila,2);
				String domicilio = (String)tabla.getValueAt(ultima_fila,3);
				String telefono = (String)tabla.getValueAt(ultima_fila,4);
				String mail = (String)tabla.getValueAt(ultima_fila,5);
				//double estado = 0;
				
				Cliente nuevo_cliente = new Cliente(dni,nombre,apellido,domicilio,telefono,mail);
				//CuentaCorriente nueva_cuenta = new CuentaCorriente(estado,dni);
				
				consultaCuentas = new EjecutaConsultasCtasCtes();
				Object[] cliente = consultaCuentas.obtenerDatosCliente(dni);
				String resultado="fallo";
				if(cliente[0]!=null){
					if(cliente[6].equals(0)){
						String mensaje = "<html><Font size=5>¡Ya existe un cliente con ese DNI! Intente nuevamente.</Font></html>";
						JOptionPane.showMessageDialog(null, mensaje, "¡Error!", JOptionPane.WARNING_MESSAGE);
						panel.cancelar.doClick();
					}else{
						resultado = consultaCuentas.recuperarCliente(nuevo_cliente);
						if(resultado.equals("ok")){
							panel.guardar.setVisible(false);
							panel.aniadir.setEnabled(true);
							panel.actualizar.setVisible(true);
							panel.abrir_ctacte.setVisible(true);
							panel.cerrar_ctacte.setVisible(true);
							panel.modif.setVisible(true);
							panel.elim.setVisible(true);
							panel.cancelar.setVisible(false);
						}else if(resultado.equals("fallo")){
							String mensaje = "<html><Font size=5>¡No se pudo añadir el Cliente! Intente nuevamente.</Font></html>";
							JOptionPane.showMessageDialog(null, mensaje, "¡Error!", JOptionPane.WARNING_MESSAGE);
							
						}
					}
				}	
				else{
					resultado = consultaCuentas.crearNuevoCliente(nuevo_cliente);
					if(resultado.equals("ok")){
						panel.guardar.setVisible(false);
						panel.aniadir.setEnabled(true);
						panel.actualizar.setVisible(true);
						panel.abrir_ctacte.setVisible(true);
						panel.cerrar_ctacte.setVisible(true);
						panel.modif.setVisible(true);
						panel.elim.setVisible(true);
						panel.cancelar.setVisible(false);
					}else if(resultado.equals("fallo")){
						String mensaje = "<html><Font size=5>¡No se pudo añadir el Cliente! Intente nuevamente.</Font></html>";
						JOptionPane.showMessageDialog(null, mensaje, "¡Error!", JOptionPane.WARNING_MESSAGE);
						
					}
				}
				
				
			}
			//SI ESTÁ MODIFICANDO...
			else if(!panel.modif.isEnabled()){
				int fila_selec = tabla.getSelectedRow();
				
				String dni = (String)tabla.getValueAt(fila_selec,0);
				String nombre = (String)tabla.getValueAt(fila_selec,1);
				String apellido = (String)tabla.getValueAt(fila_selec,2);
				String domicilio = (String)tabla.getValueAt(fila_selec,3);
				String telefono = (String)tabla.getValueAt(fila_selec,4);
				String mail = (String)tabla.getValueAt(fila_selec,5);
				
				Cliente cliente = new Cliente(dni,nombre,apellido,domicilio,telefono,mail);
				
				consultaCuentas = new EjecutaConsultasCtasCtes();
				boolean ok = consultaCuentas.modificarCliente(cliente);
				if(ok){
					panel.guardar.setVisible(false);
					panel.modif.setEnabled(true);
					panel.actualizar.setVisible(true);
					panel.abrir_ctacte.setVisible(true);
					panel.cerrar_ctacte.setVisible(true);
					panel.aniadir.setVisible(true);
					panel.elim.setVisible(true);
					panel.cancelar.setVisible(false);
				}
				else{
					String mensaje = "<html><Font size=5>¡No se pudo modificar el Cliente! Intente nuevamente.</Font></html>";
					JOptionPane.showMessageDialog(null, mensaje, "¡Error!", JOptionPane.WARNING_MESSAGE);
				}
			}
			int n_filas = tabla.getRowCount();
			int n_columnas = tabla.getColumnCount();
		
			Object[][] datos = new Object[n_filas][n_columnas];
			for(int i=0;i<n_filas;i++){
				for(int j=0;j<n_columnas;j++){
					datos[i][j]=tabla.getModel().getValueAt(i, j);
				}
			}
			Object[] columnas = new Object[n_columnas];
			for(int i=0;i<n_columnas;i++){
				columnas[i]=tabla.getModel().getColumnName(i);
			}
			
			FormateaTablas paraFormatear = new FormateaTablas();
			
			DefaultTableModel modeloNuevo = new DefaultTableModel(datos,columnas){
				/**
				 * 
				 */
				private static final long serialVersionUID = -4858129653005027125L;

				public boolean isCellEditable(int rowIndex,int columnIndex){
					return false;
				}
			};
			tabla.setModel(modeloNuevo);
			paraFormatear.formatearColumnas(tabla, "clientes");
			for(int i=0;i<n_columnas;i++){
				tabla.getColumnModel().getColumn(i).setCellRenderer(tcr);
			}
		}
		//SI SELECCIONA LA TABLA DE FORMAS DE PAGO...
		else if(seleccion.getSelectedItem().equals("FORMAS DE PAGO")){
			int numero_filas = tabla.getRowCount();
			
			consultaVentas = new EjecutaConsultasVenta();
			
			int codigo = 0;
			String tipo="";
			double valorrecargo = 0;
			double valordescuento = 0;
			FormaDePago forma = null;
			for(int i=0;i<numero_filas;i++){
				codigo = (int)tabla.getValueAt(i, 0);
				tipo = (String)tabla.getValueAt(i, 1);
				try{
					valorrecargo = (double)tabla.getValueAt(i, 2);
				}catch(ClassCastException e){
					String recargo = (String)tabla.getValueAt(i, 2);
					valorrecargo = Double.parseDouble(recargo);
				}
				try{
					valordescuento = (double)tabla.getValueAt(i, 3);
				}catch(ClassCastException e){
					String descuento = (String)tabla.getValueAt(i, 3);
					valordescuento = Double.parseDouble(descuento);
				}
				forma = new FormaDePago(codigo,tipo,valorrecargo,valordescuento);
				boolean ok = consultaVentas.modificaFormasDePago(forma);
				if(!ok){
					String mensaje = "<html><Font size=5>¡No se modificar la información de la Forma de pago! Intente nuevamente.</Font></html>";
					JOptionPane.showMessageDialog(null, mensaje, "¡Error!", JOptionPane.WARNING_MESSAGE);
				}
			}
		}

		//SI SELECCIONÓ LA TABLA DE RUBROS...
		else if(seleccion.getSelectedItem().equals("RUBROS")){
			//SI ESTÁ CREANDO...
			if(!panel.aniadir.isEnabled()){
				
				int ultima_fila = tabla.getRowCount()-1;
				
				int codigo = (int)tabla.getValueAt(ultima_fila,0);
				String nombre = (String)tabla.getValueAt(ultima_fila,1);
				
				Rubro nuevo_rubro = new Rubro(codigo,nombre,null);
				
				consultaRubros = new EjecutaConsultas();
				boolean ok = consultaRubros.crearNuevoRubro(nuevo_rubro);
				if(ok){
					panel.cancelar.setVisible(false);
					panel.guardar.setVisible(false);
					panel.aniadir.setEnabled(true);
					panel.modif.setVisible(true);
					panel.elim.setVisible(true);
					
				}else{
					JOptionPane.showMessageDialog(null,"<html><font size=4>¡No se pudo registrar el Rubro! Intente nuevamente.</font></html>",
							"Eliminar Línea", JOptionPane.WARNING_MESSAGE);
				}
			}
			//SI ESTÁ MODIFICANDO...
			else if(!panel.modif.isEnabled()){
				
				int fila_selec = tabla.getSelectedRow();
				
				int codigo_cuenta = (int)tabla.getValueAt(fila_selec, 0);
				String nombre = (String)tabla.getValueAt(fila_selec,1);
				
				Rubro rubro = new Rubro(codigo_cuenta,nombre,null);
				
				consultaRubros = new EjecutaConsultas();				
				boolean ok = consultaRubros.modificarRubro(rubro);
				if(ok){
					panel.guardar.setVisible(false);
					panel.modif.setEnabled(true);
					panel.aniadir.setVisible(true);
					panel.elim.setVisible(true);
					panel.cancelar.setVisible(false);
				}
				else
					JOptionPane.showMessageDialog(null,"<html><font size=4>¡No se pudo modificar el Rubro! Intente nuevamente.</font></html>",
							"Eliminar Línea", JOptionPane.WARNING_MESSAGE);
			}
		}
		
		//SI SELECCIONÓ LA TABLA DE PROVEEDORES...
		else if(seleccion.getSelectedItem().equals("PROVEEDORES")){
			//SI ESTÁ CREANDO...
			if(!panel.aniadir.isEnabled()){
				
				int ultima_fila = tabla.getRowCount()-1;
				
				String codigo = (String)tabla.getValueAt(ultima_fila,0);
				String nombre = (String)tabla.getValueAt(ultima_fila,1);
				String domicilio = (String)tabla.getValueAt(ultima_fila,2);
				String telefono = (String)tabla.getValueAt(ultima_fila,3);
				String email = (String)tabla.getValueAt(ultima_fila,4);
				
				Proveedor nuevo_proveedor = new Proveedor(codigo,nombre,domicilio,telefono,email);
				
				consultaProveedores = new EjecutaConsultasProveedor();
				String resultado = consultaProveedores.crearProveedor(nuevo_proveedor);
				if(resultado.equals("ok")){
					panel.guardar.setVisible(false);
					panel.actualizar.setVisible(true);
					panel.aniadir.setEnabled(true);
					panel.modif.setVisible(true);
					panel.elim.setVisible(true);
					panel.cancelar.setVisible(false);
				}
				else if(resultado.equals("existente")){
					String mensaje = "<html><Font size=5>¡El código ingresado ya está en uso!</Font></html>";
					JOptionPane.showMessageDialog(null, mensaje, "¡Error!", JOptionPane.WARNING_MESSAGE);
				}else if(resultado.equals("fallo")){
					JOptionPane.showMessageDialog(null,"<html><font size=4>¡No se pudo registrar el Proveedor! Intente nuevamente.</font></html>",
							"¡Error!", JOptionPane.WARNING_MESSAGE);
				}
			}
			
			//SI ESTÁ MODIFICANDO...
			else if(!panel.modif.isEnabled()){
				
				int fila_selec = tabla.getSelectedRow();
				
				String codigo = (String)tabla.getValueAt(fila_selec,0);
				String nombre = (String)tabla.getValueAt(fila_selec,1);
				String domicilio = (String)tabla.getValueAt(fila_selec,2);
				String telefono = (String)tabla.getValueAt(fila_selec,3);
				String email = (String)tabla.getValueAt(fila_selec,4);
				
				Proveedor proveedor = new Proveedor(codigo,nombre,domicilio,telefono,email);
				
				consultaProveedores = new EjecutaConsultasProveedor();
								
				boolean ok = consultaProveedores.modificarProveedor(proveedor);
				if(ok){
					panel.guardar.setVisible(false);
					panel.actualizar.setVisible(true);
					panel.modif.setEnabled(true);
					panel.aniadir.setVisible(true);
					panel.elim.setVisible(true);
					panel.cancelar.setVisible(false);
				}
				else
					JOptionPane.showMessageDialog(null,"<html><font size=4>¡No se pudo modificar el Proveedor! Intente nuevamente.</font></html>",
							"¡Error!", JOptionPane.WARNING_MESSAGE);
			}
		}
		
		//SI SELECCIONÓ LA TABLA DE EMPLEADOS
		else if(seleccion.getSelectedItem().equals("EMPLEADOS")){
			int fila=0;
			//SI ESTÁ CREANDO...
			if(!panel.aniadir.isEnabled()){
				fila = tabla.getRowCount()-1;
				int id = (int)tabla.getValueAt(fila,0);
				String tipo = (String)tabla.getValueAt(fila,1);
				String nombre = (String)tabla.getValueAt(fila,2);
				String apellido = (String)tabla.getValueAt(fila,3);
				String dni = (String)tabla.getValueAt(fila,4);
				String domicilio = (String)tabla.getValueAt(fila,5);
				String email = (String)tabla.getValueAt(fila,6);
				String telefono = (String)tabla.getValueAt(fila,7);
				String usuario = (String)tabla.getValueAt(fila,8);
				String pass = (String)tabla.getValueAt(fila,9);
				
				consultaEmpleados = new EjecutaConsultasEmpleado();
				
				Object[] aux = consultaEmpleados.obtienePasswordTipoyEstado(usuario);
				
				if(aux[0]==null){
				
					Empleado empleado = new Empleado(id, tipo, nombre, apellido, dni, domicilio, email, telefono,
						usuario, pass);
				
					String resultado = consultaEmpleados.crearEmpleado(empleado);
					if(resultado.equals("ok")){
						panel.guardar.setVisible(false);
						panel.cancelar.setVisible(false);
						panel.aniadir.setEnabled(true);
						panel.modif.setVisible(true);
						panel.elim.setVisible(true);
					}else{
						String mensaje = "<html><Font size=5>¡No se pudo registrar el Empleado! Intente nuevamente.</Font></html>";
						JOptionPane.showMessageDialog(null, mensaje, "¡Error!", JOptionPane.WARNING_MESSAGE);
					}
				}else{
					String mensaje = "<html><Font size=5>¡Ya existe el usuario ingresado! Ingrese uno diferente.</Font></html>";
					JOptionPane.showMessageDialog(null, mensaje, "¡Error!", JOptionPane.WARNING_MESSAGE);
				}
			}
			//SI ESTÁ MODIFICANDO
			else if(!panel.modif.isEnabled()){
				fila = tabla.getSelectedRow();
				int id = (int)tabla.getValueAt(fila,0);
				String tipo = (String)tabla.getValueAt(fila,1);
				String nombre = (String)tabla.getValueAt(fila,2);
				String apellido = (String)tabla.getValueAt(fila,3);
				String dni = (String)tabla.getValueAt(fila,4);
				String domicilio = (String)tabla.getValueAt(fila,5);
				String email = (String)tabla.getValueAt(fila,6);
				String telefono = (String)tabla.getValueAt(fila,7);
				String usuario = (String)tabla.getValueAt(fila,8);
				String pass = (String)tabla.getValueAt(fila,9);
				
				Empleado empleado = new Empleado(id, tipo, nombre, apellido, dni, domicilio, email, telefono,
						usuario, pass);
				
				consultaEmpleados = new EjecutaConsultasEmpleado();
				
				String resultado = consultaEmpleados.modificarEmpleado(empleado);
				if(resultado.equals("ok")){
					panel.guardar.setVisible(false);
					panel.cancelar.setVisible(false);
					panel.aniadir.setVisible(true);
					panel.modif.setEnabled(true);
					panel.elim.setVisible(true);
				}else{
					String mensaje = "<html><Font size=5>¡No se pudo modificar el Empleado! Intente nuevamente.</Font></html>";
					JOptionPane.showMessageDialog(null, mensaje, "¡Error!", JOptionPane.WARNING_MESSAGE);
				}
			}
			
		}
	}

	PanelDetalle panel;
	JTable tabla;
	JComboBox<String> seleccion;
	
	EjecutaConsultasCtasCtes consultaCuentas;
	EjecutaConsultasVenta consultaVentas;
	EjecutaConsultas consultaRubros;
	EjecutaConsultasProveedor consultaProveedores;
	EjecutaConsultasEmpleado consultaEmpleados;
}
