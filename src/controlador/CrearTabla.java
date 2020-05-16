package controlador;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;

import javax.swing.JOptionPane;
import javax.swing.event.TableModelEvent;
import javax.swing.table.DefaultTableModel;

import modelo.consultas.*;
import modelo.objetos.*;

//CLASE QUE CREA EL MODELO DE LAS TABLAS
public class CrearTabla {

	//GENERA LA TABLA DE PRODUCTOS DESDE LA BASE DE DATOS
	public DefaultTableModel obtenerTablaProductos(){
		
		//SE ESTABLECE LOS TÍTULOS DE LAS COLUMNAS
		String[] columnas = {"CÓDIGO","DESCRIPCION","RUBRO","PRECIO","STOCK","UNIDAD","PROVEEDOR","ESTADO","OBSERV.","ALERTA"};
		
		//SE BLOQUEA LA EDICIÓN DE CELDAS DE LA TABLA
		modelo=new DefaultTableModel(null,columnas){
			/**
			 * 
			 */
			private static final long serialVersionUID = 6901343763489846127L;
			public boolean isCellEditable(int rowIndex,int columnIndex){
				return false;
			}
			public Class<?> getColumnClass(int columnIndex){
				if(columnIndex==3 || columnIndex==4){
					return Double.class;
				}
				return String.class;
			}
			
		};
		
		//SE OBTIENE LA LISTA DE PRODUCTOS DE LA BASE DE DATOS
		EjecutaConsultas nuevaConsulta = new EjecutaConsultas();
		listaProductos = nuevaConsulta.consultaProductos();
		
		//SE AÑADE LOS PRODUCTOS
		for(Producto p:listaProductos){			
			int codigo=p.getCodigo();
			String desc = p.getDescripcion();
			int codrubro = p.getCodrubro();
			double precio = p.getPrecio();
			double cant = p.getCantidad();
			String unidad = p.getUnidad();
			String codprov = p.getCodproveedor();
			String estado = p.getEstado();
			String comentarios = p.getComentarios();
			
			//SI HAY OBSERVACIONES, SE AÑADE UNA "X" A LA CELDA CORRESPONDIENTE
			String c="";
			if(!comentarios.trim().equals("")){
				c="X";
			}
			
			double alerta = p.getCant_min();
			
			//SE CREA LA FILA Y SE AÑADE AL MODELO
			Object[] fila = {String.format("%05d",codigo),desc,codrubro,precio,cant,unidad,codprov,estado,c,alerta};
			modelo.addRow(fila);		
		}
		return modelo;
	}
	
	//GENERA LAS TABLAS DE LINEAS DE REPARTO PENDIENTES O NO, DESDE LA BASE DE DATOS
	public DefaultTableModel obtenerTablaLineasReparto(int n_reparto){
		
		//SE ESTABLECE LOS TÍTULOS DE LAS COLUMNAS
		String[] columnas = {"N°","PRODUCTO","CANT.","DIRECCIÓN","TURNO","NOMBRE","OBSERVACIONES","FECHA COMPRA"};
				
		//SE BLOQUEA LA EDICIÓN DE CELDAS DE LA TABLA				
		modelo=new DefaultTableModel(null,columnas){
			/**
			 * 
			 */
			private static final long serialVersionUID = 6108693015328830036L;

			public boolean isCellEditable(int rowIndex,int columnIndex){
				return false;
			}
		};
		
		EjecutaConsultasReparto nuevaConsulta = new EjecutaConsultasReparto();
		if(n_reparto==0){
			//SE OBTIENE LA LISTA DE LINEAS DE REPARTO PENDIENTES
			listaLineasReparto = nuevaConsulta.consultaLineasRepartoPendientes();
		}
		else{
			//SE OBTIENE LAS LINEAS DE REPARTO ASOCIADAS AL REPARTO
			listaLineasReparto = nuevaConsulta.obtenerLineasdeReparto(n_reparto);
		}
		
		//SE CREAN LAS FILAS Y SE AÑADEN AL MODELO
		if(listaLineasReparto!=null){
			for(LineaReparto linea:listaLineasReparto){
				int num_linea = linea.getCodigo_linea();
			
				int cod_linea_venta = linea.getCod_linea_venta();
				EjecutaConsultasVenta consulta = new EjecutaConsultasVenta();
				LineaVenta linea_de_venta = consulta.consultaLineaVenta(cod_linea_venta);
				if(linea_de_venta!=null){
					int cod_producto = linea_de_venta.getCodProducto();
					EjecutaConsultas consulta2 = new EjecutaConsultas();
					Producto producto = consulta2.obtieneProducto(cod_producto);
					String desc_producto;
					if(producto!=null){
						desc_producto = producto.getDescripcion();
					}else
						desc_producto = cod_producto+"";
					double cantidad = linea_de_venta.getCantidad();
			
					String direccion = linea.getDireccion();
					String turno = linea.getTurno();
					String nombre = linea.getNombre();
					String observaciones = linea.getObservaciones();
					
					int codigo_venta = linea_de_venta.getCodVenta();
					Venta venta = consulta.consultaVenta(codigo_venta);
					
					if(venta!=null){
						Date fecha_venta = venta.getFecha();
			
						SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss a");
						String fecha = df.format(fecha_venta);
			
						Object[] fila = {num_linea,desc_producto,cantidad,direccion,turno,nombre,observaciones,fecha};
						modelo.addRow(fila);
					}else{
						JOptionPane.showMessageDialog(null,
								"<html><font size=4>¡Error al obtener información de lineas de reparto! Intente nuevamente</font></html>",
								"¡Error!", JOptionPane.WARNING_MESSAGE);
					}
				}else{
					JOptionPane.showMessageDialog(null,
							"<html><font size=4>¡Error al obtener información de lineas de reparto! Intente nuevamente</font></html>",
							"¡Error!", JOptionPane.WARNING_MESSAGE);
				}
			}
		}else{
			JOptionPane.showMessageDialog(null,
					"<html><font size=4>¡Error al obtener información de lineas de reparto! Intente nuevamente</font></html>",
					"¡Error!", JOptionPane.WARNING_MESSAGE);
		}
		return modelo;
	}
	
	//GENERA LA TABLA DE REPARTOS PENDIENTES DE CONFIRMACIÓN
	public DefaultTableModel obtenerTablaRepartos(){

		//SE ESTABLECE LOS TÍTULOS DE LAS COLUMNAS
		String[] columnas = {"REPARTO","FECHA CREACIÓN"};
		modelo=new DefaultTableModel(null,columnas){
			/**
			 * 
			 */
			private static final long serialVersionUID = -244312853522917761L;

			public boolean isCellEditable(int rowIndex,int columnIndex){
				return false;
			}
		};
		
		EjecutaConsultasReparto nuevaConsulta = new EjecutaConsultasReparto();
		listaRepartos = nuevaConsulta.consultaRepartosPendientes();

		//SE CREAN LAS FILAS Y SE AÑADEN AL MODELO
		if(listaRepartos!=null){
			for(Reparto r:listaRepartos){
				int n_reparto = r.getCodigo();
				Date creado = r.getCreado();
			
				SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
				String fecha = df.format(creado);
			
				Object[] fila = {n_reparto,fecha};
				modelo.addRow(fila);
			}
		}else{
			JOptionPane.showMessageDialog(null,
					"<html><font size=4>¡No se pudo obtener los Repartos pendientes! Error de conexión</font></html>",
					"¡Error!", JOptionPane.WARNING_MESSAGE);
		}
		return modelo;
		
	}
	
	public DefaultTableModel obtenerVentas(){

		//SE ESTABLECE LOS TÍTULOS DE LAS COLUMNAS
		String[] columnas = {"VENTA N°","MONTO","VENDEDOR"};
		modelo=new DefaultTableModel(null,columnas){
			/**
			 * 
			 */
			private static final long serialVersionUID = -2886483162968923195L;

			public boolean isCellEditable(int rowIndex,int columnIndex){
				return false;
			}
		};
		
		EjecutaConsultasVenta nuevaConsulta = new EjecutaConsultasVenta();
		EjecutaConsultasEmpleado paraConsultarVendedor = new EjecutaConsultasEmpleado();
		listaVentasSinFacturar = nuevaConsulta.obtenerVentasSinFacturar();
		DecimalFormat df = new DecimalFormat("###,###.00");
		
		//SE CREAN LAS FILAS Y SE AÑADEN AL MODELO
		if(listaVentasSinFacturar!=null){
			for(Object[] o:listaVentasSinFacturar){
				o[0]=String.format("%07d", o[0]);
				o[1]=df.format(o[1]);
				o[2]=paraConsultarVendedor.obtieneNombre((int)o[2]);
				
				modelo.addRow(o);
			}
		}else{
			JOptionPane.showMessageDialog(null,
					"<html><font size=4>¡No se pudo obtener las Ventas para Facturar! Error de conexión</font></html>",
					"¡Error!", JOptionPane.WARNING_MESSAGE);
		}
		return modelo;
		
	}
	
	public DefaultTableModel obtenerProductosPendientesDeRecepcion(){

		//SE ESTABLECE LOS TÍTULOS DE LAS COLUMNAS
		String[] columnas = {"PROVEEDOR","PEDIDO N°","FECHA PEDIDO","LÍNEA N°","DESCRIPCIÓN","PRECIO U.","CANTIDAD","SUBTOTAL"};
		modelo=new DefaultTableModel(null,columnas){
			/**
			 * 
			 */
			private static final long serialVersionUID = -2886483162368923195L;

			public boolean isCellEditable(int rowIndex,int columnIndex){
				return false;
			}
		};
		
		EjecutaConsultasProveedor nuevaConsulta = new EjecutaConsultasProveedor();
		listaProductosPendientes = nuevaConsulta.obtenerTodosProductosPendientes();
		DecimalFormat df = new DecimalFormat("###,##0.00");
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy hh:mm a");
		
		//SE CREAN LAS FILAS Y SE AÑADEN AL MODELO
		if(listaProductosPendientes!=null){
			for(Object[] o:listaProductosPendientes){
				o[1]=String.format("%05d", o[1]);
				o[2]=sdf.format(o[2]);
				o[3]=String.format("%06d", o[3]);
				
				modelo.addRow(o);
			}
		}else{
			JOptionPane.showMessageDialog(null,
					"<html><font size=4>¡No se pudo obtener la lista de productos pendientes! Error de conexión</font></html>",
					"¡Error!", JOptionPane.WARNING_MESSAGE);
		}
		return modelo;
		
	}
	ArrayList<Producto> listaProductos;
	ArrayList<LineaReparto> listaLineasReparto;
	ArrayList<Reparto> listaRepartos;
	ArrayList<Object[]> listaVentasSinFacturar;
	ArrayList<Object[]> listaProductosPendientes;
	DefaultTableModel modelo;
}


