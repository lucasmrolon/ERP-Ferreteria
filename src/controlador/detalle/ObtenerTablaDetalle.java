package controlador.detalle;

import java.awt.*;
import java.awt.event.*;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import javax.swing.*;
import javax.swing.table.*;

import modelo.consultas.*;
import modelo.objetos.*;
import vista.detalle.PanelDetalle;
import modelo.objetos.CuentaCorriente;

//CLASE QUE SE ENCARGA DE OBTENER LOS MODELOS CORRESPONDIENTES A CADA TABLA, CADA VEZ QUE UNA DE ESTAS ES SELECCIONADA EN EL COMBOBOX
public class ObtenerTablaDetalle implements ActionListener{

	public ObtenerTablaDetalle(PanelDetalle panel,JTable tabla,DefaultTableCellRenderer cellrenderer){
		
		tamanio_pantalla = Toolkit.getDefaultToolkit().getScreenSize();
		this.tabla = tabla;
		tcr = cellrenderer;
		formatea = new FormateaTablas();
		this.panel = panel;
		contenedorTabla = (JScrollPane)tabla.getParent().getParent();
	}
	
	//PERMITE QUE UNA TABLA SE SELECCIONE 
	@Override
	public void actionPerformed(ActionEvent e) {
		
		//OCULTA TODOS LOS BOTONES
			panel.imprimir_informe_venta_anual.setVisible(false);
			panel.imprimir_reporte_venta.setVisible(false);
			panel.actualizar.setVisible(false);
			panel.aniadir.setVisible(false);
			panel.modif.setVisible(false);
			panel.elim.setVisible(false);
			panel.guardar.setVisible(false);
			panel.cancelar.setVisible(false);
			panel.guardarcambios.setVisible(false);
			panel.abrir_ctacte.setVisible(false);
			panel.cerrar_ctacte.setVisible(false);
			
			DefaultTableModel modelo = (DefaultTableModel)tabla.getModel();
			String seleccion = (String)((JComboBox<?>)e.getSource()).getSelectedItem();
		
			//SI SELECCIONO LA TABLA VENTAS
			if(seleccion.equals("VENTAS")){
				consulta = "ventas";
				
				//OBTIENE LA TABLA DE VENTAS
				consultarVenta = new EjecutaConsultasVenta();
				ArrayList<Object[]> ventas = consultarVenta.obtenerTodasLasVentas();
				if(ventas!=null){
					String[] columnasTabla={"CÓDIGO","FECHA","MONTO","MODIFICACIÓN","EMPLEADO","CÓDIGO CTA. CTE.","FORMA DE PAGO"};
					modelo = new DefaultTableModel(null,columnasTabla){
						/**
						 * 
						 */
						private static final long serialVersionUID = 7472404482152180393L;

						public boolean isCellEditable(int rowIndex,int columnIndex){
							return false;
						}
					};
					for(Object[] v:ventas){
						
						v[0]= String.format("%07d", v[0]);
						SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy hh:mm a");
						v[1]=sdf.format((Date)v[1]);
						v[2]=Math.round((Double)v[2]*100d)/100d;
						if((double)v[3]==0.0){
							v[3]="";
						}
						if((int)v[5]==0){
							v[5] = "";
						}
				
						modelo.addRow(v);
					}
				
					//REDIMENSIONA LA TABLA
					if(modelo.getRowCount()<10){
						contenedorTabla.setSize(1000,modelo.getRowCount()*tabla.getRowHeight()+23);
					}
					else
						contenedorTabla.setSize(1000,tabla.getRowHeight()*10+23);
					contenedorTabla.setLocation((tamanio_pantalla.width/2)-500, 130);
			
					if(tabla.getKeyListeners().length!=0){
						tabla.removeKeyListener(tabla.getKeyListeners()[0]);
					}
					
					//CREA VENTANA PARA MOSTRAR LINEAS DE VENTA
					ObtenerLineasDeVenta obtenerLineas = new ObtenerLineasDeVenta(tabla,tcr);
					tabla.addKeyListener(obtenerLineas);
				
					//MUESTRA LOS BOTONES CORRESPONDIENTES 
					Point ubicacion_tabla = contenedorTabla.getLocation();
					Dimension tamanio_tabla = contenedorTabla.getSize();
					panel.imprimir_informe_venta_anual.setLocation((tamanio_pantalla.width/2)-500,ubicacion_tabla.y+tamanio_tabla.height+20);
					panel.imprimir_reporte_venta.setLocation((tamanio_pantalla.width/2)-250,ubicacion_tabla.y+tamanio_tabla.height+20);
					panel.imprimir_informe_venta_anual.setVisible(true);
					panel.imprimir_reporte_venta.setVisible(true);
				}else{
					JOptionPane.showMessageDialog(null,"<html><font size=4>No se pudo obtener la tabla de Ventas. Error de conexión.</font></html>",
							"¡Error!", JOptionPane.WARNING_MESSAGE);
				}
			}
			//SI SELECCIONÓ LA TABLA DE REPARTOS
			else if(seleccion.equals("REPARTOS")){
				consulta="repartos";
				
				//OBTIENE LA TABLA DE REPARTOS Y SUS FILAS
				consultarReparto = new EjecutaConsultasReparto();
				ArrayList<Object[]> repartos = consultarReparto.obtenerTodosLosRepartos();
				if(repartos!=null){
					String[] columnasTabla ={"CÓDIGO","FECHA CREACIÓN","FECHA CONFIRMACIÓN","EMPLEADO"};
					modelo = new DefaultTableModel(null,columnasTabla){
						/**
						 * 
						 */
						private static final long serialVersionUID = 7455062988172517232L;

						public boolean isCellEditable(int rowIndex,int columnIndex){
							return false;
						}
					};
					for(Object[] r:repartos){
						r[0]=String.format("%05d", r[0]);
						SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
						r[1]=sdf.format((Date)r[1]);
						r[2]=sdf.format((Date)r[2]);
				
						modelo.addRow(r);
					}
					if(modelo.getRowCount()<10){
						contenedorTabla.setSize(500,modelo.getRowCount()*tabla.getRowHeight()+23);
					}
					else
						contenedorTabla.setSize(500,tabla.getRowHeight()*10+23);
					contenedorTabla.setLocation((tamanio_pantalla.width/2)-250, 130);
			
					if(tabla.getKeyListeners().length!=0){
						tabla.removeKeyListener(tabla.getKeyListeners()[0]);
					}
					//CREA VENTANA AUXILIAR PARA MOSTRAR LINEAS DE REPARTO
					ObtenerLineasDeReparto obtenerLineas = new ObtenerLineasDeReparto(tabla,tcr);
					tabla.addKeyListener(obtenerLineas);
				}else{
					JOptionPane.showMessageDialog(null,"<html><font size=4>No se pudo obtener la tabla de Repartos. Error de conexión.</font></html>",
							"¡Error!", JOptionPane.WARNING_MESSAGE);
				}
			}
			//SI SELECCIONÓ LA TABLA DE EMPLEADOS 
			else if(seleccion.equals("EMPLEADOS")){
				consulta="empleados";
				
				//OBTIENE LA TABLA DE EMPLEADOS Y SUS FILAS
				consultarEmpleado = new EjecutaConsultasEmpleado();
				ArrayList<Object[]> empleados = consultarEmpleado.obtenerEmpleados();
				if(empleados!=null){
					String[] columnasTabla = {"ID","TIPO","NOMBRE","APELLIDO","DNI","DOMICILIO","E-MAIL","TELÉFONO","USUARIO","CONTRASEÑA"};
					modelo = new DefaultTableModel(null,columnasTabla){
						/**
						 * 
						 */
						private static final long serialVersionUID = 8735135548441285292L;

						public boolean isCellEditable(int rowIndex,int columnIndex){
							return false;
						}
					};
					for(Object[] em:empleados){
						modelo.addRow(em);
					}
					
					//REUBICA LA TABLA Y EDITA LOS BOTONES
					if(modelo.getRowCount()<10){
						contenedorTabla.setSize(1200,modelo.getRowCount()*tabla.getRowHeight()+23);
					}
					else
						contenedorTabla.setSize(1200,tabla.getRowHeight()*10+23);
					contenedorTabla.setLocation((tamanio_pantalla.width/2)-620, 130);	
				
					Point ubicacion_tabla = contenedorTabla.getLocation();
					Dimension tamanio_tabla = contenedorTabla.getSize();
					panel.aniadir.setText("Añadir Empleado");
					panel.aniadir.setLocation((tamanio_pantalla.width/2)-265,ubicacion_tabla.y+tamanio_tabla.height+20);
					panel.aniadir.setVisible(true);
					panel.aniadir.setEnabled(true);
					panel.modif.setText("Modificar Empleado");
					panel.modif.setLocation((tamanio_pantalla.width/2)-95,ubicacion_tabla.y+tamanio_tabla.height+20);
					panel.modif.setVisible(true);
					panel.modif.setEnabled(true);
					panel.elim.setText("Eliminar Empleado");
					panel.elim.setLocation((tamanio_pantalla.width/2)+75,ubicacion_tabla.y+tamanio_tabla.height+20);
					panel.elim.setVisible(true);

				}else{
					JOptionPane.showMessageDialog(null,
							"<html><font size=4>No se pudo obtener la tabla de Empleados. Error de conexión</font></html>",
							"¡Error!", JOptionPane.WARNING_MESSAGE);
				}
			}

			//SI SELECCIONA LA TABLA DE FORMAS DE PAGO
			else if(seleccion.equals("FORMAS DE PAGO")){
				consulta="formasdepago";
				
				//OBTIENE LA TABLA DE FORMAS DE PAGO Y SUS FILAS
				consultarVenta = new EjecutaConsultasVenta();
				ArrayList<FormaDePago> formas_de_pago = consultarVenta.obtener_formas_de_pago();

				if(formas_de_pago!=null){
					String[] columnasTabla={"CÓDIGO","NOMBRE","% RECARGO","% DESCUENTO"};
					modelo = new DefaultTableModel(null,columnasTabla){
						/**
						 * 
						 */
						private static final long serialVersionUID = 258575796010921040L;

						public boolean isCellEditable(int rowIndex,int columnIndex){
							if((columnIndex==0) || (columnIndex==1)){
								return false;
							}else{
								return true;
							}
						}
					};
					for(FormaDePago f:formas_de_pago){
						Object[] fila = {f.getCodigo(),f.getTipo(),f.getRecargo(),f.getDescuento()};
						modelo.addRow(fila);
					}
					
					//REDIMENSIONA LA TABLA
					if(modelo.getRowCount()<10){
						contenedorTabla.setSize(500,modelo.getRowCount()*tabla.getRowHeight()+23);
					}
					else
						contenedorTabla.setSize(500,tabla.getRowHeight()*10+23);
					contenedorTabla.setLocation((tamanio_pantalla.width/2)-250, 130);
				
					panel.guardarcambios.setLocation((tamanio_pantalla.width/2)-75, 
							contenedorTabla.getLocation().y+contenedorTabla.getSize().height+20);
					panel.guardarcambios.setVisible(true);
				}
				else{
					String mensaje = "<html><Font size=5>¡No se pudo obtener la tabla de Formas de pago! Error de conexión.</Font></html>";
					JOptionPane.showMessageDialog(null, mensaje, "¡Error!", JOptionPane.WARNING_MESSAGE);
				}
			
			}
			
			//SI SELECCIONA LA TABLA DE CLIENTES
			else if(seleccion.equals("CLIENTES")){
				consulta="clientes";
				//OBTIENE LA TABLA Y SUS FILAS
				ArrayList<Object[]> clientes_tabla = new ArrayList<Object[]>();
				DecimalFormat df = new DecimalFormat("###,###.00");
				consultarVenta = new EjecutaConsultasVenta();
				ArrayList<Object[]> clientes = consultarVenta.obtieneClientes();
				for(Object[] cliente:clientes){
					CuentaCorriente cuentacte = consultarVenta.obtieneCuentaCorriente((String)cliente[0]); 
					Object[] cliente_tabla = {cliente[0],cliente[1],cliente[2],cliente[3],cliente[4],cliente[5],0,0.00};
					if(cuentacte!=null){
						cliente_tabla[6]=cuentacte.getCodigo_cuenta();
						cliente_tabla[7]=cuentacte.getEstado();
					}
					clientes_tabla.add(cliente_tabla);
				}
				if(!clientes_tabla.isEmpty()){
					String[] columnasTabla = {"DNI","NOMBRE","APELLIDO","DOMICILIO","TELÉFONO","EMAIL","CTA. CTE.","ESTADO"};
					modelo = new DefaultTableModel(null,columnasTabla){
						/**
						 * 
						 */
						private static final long serialVersionUID = -1791529943276479357L;

						public boolean isCellEditable(int rowIndex,int columnIndex){
							return false;
						}
					};
					for(Object[] c:clientes_tabla){
						if((int)c[6]==0){
							c[6] = "";
							c[7] = "";
						}
						else{
							String signo = "+ ";
							String aux = "0";
							if((double)c[7]<0){
								signo = "- ";
								aux = df.format(-1*(double)c[7]);
								aux = signo + aux;
								aux="<html><b><p style='color:red';>"+aux+"</style></b></html>";
							}else if((double)c[7]>0){
								aux = df.format((double)c[7]);
								aux = signo + aux;
							}
							c[7]=aux;
						}
						
						
						modelo.addRow(c);
					}
					
					//REDIMENSIONA LA TABLA
					if(modelo.getRowCount()<10){
						contenedorTabla.setSize(1000,modelo.getRowCount()*tabla.getRowHeight()+23);
					}
					else
						contenedorTabla.setSize(1000,tabla.getRowHeight()*10+23);
					contenedorTabla.setLocation((tamanio_pantalla.width/2)-500, 130);
					
					//EDITA LOS BOTONES
					Point ubicacion_tabla = contenedorTabla.getLocation();
					Dimension tamanio_tabla = contenedorTabla.getSize();
					panel.actualizar.setText("Abonar Cuenta Corriente");
					panel.actualizar.setLocation((tamanio_pantalla.width/2)-320,ubicacion_tabla.y+tamanio_tabla.height+60);
					panel.actualizar.setVisible(true);
					panel.aniadir.setText("Añadir cliente");
					panel.aniadir.setLocation((tamanio_pantalla.width/2)-245,ubicacion_tabla.y+tamanio_tabla.height+20);
					panel.aniadir.setVisible(true);
					panel.aniadir.setEnabled(true);
					panel.modif.setText("Modificar Cliente");
					panel.modif.setLocation((tamanio_pantalla.width/2)-75,ubicacion_tabla.y+tamanio_tabla.height+20);
					panel.modif.setVisible(true);
					panel.modif.setEnabled(true);
					panel.elim.setText("Eliminar Cliente");
					panel.elim.setLocation((tamanio_pantalla.width/2)+95,ubicacion_tabla.y+tamanio_tabla.height+20);
					panel.elim.setVisible(true);
					panel.abrir_ctacte.setLocation((tamanio_pantalla.width/2)-100,ubicacion_tabla.y + tamanio_tabla.height+60);
					panel.abrir_ctacte.setVisible(true);
					panel.cerrar_ctacte.setLocation((tamanio_pantalla.width/2)+120,ubicacion_tabla.y + tamanio_tabla.height+60);
					panel.cerrar_ctacte.setVisible(true);
				}else{
					String mensaje = "<html><Font size=5>¡No se pudo obtener la tabla de Clientes! Error de conexión.</Font></html>";
					JOptionPane.showMessageDialog(null, mensaje, "¡Error!", JOptionPane.WARNING_MESSAGE);
				}
			}
			
			//SELECCIONA LA TABLA DE PROVEEDORES
			else if(seleccion.equals("PROVEEDORES")){
				consulta="proveedores";
				//OBTIENE LA TABLA Y SUS FILAS
				consultarProveedor = new EjecutaConsultasProveedor();
				ArrayList<Proveedor> proveedores = consultarProveedor.consultaProveedores();
				
				if(proveedores!=null){
					String[] columnasTabla = {"CODIGO","NOMBRE","DOMICILIO","TELÉFONO","EMAIL","LISTA PRECIOS"};
					modelo = new DefaultTableModel(null,columnasTabla){
						/**
						 * 
						 */
						private static final long serialVersionUID = -4754028726479467779L;

						public boolean isCellEditable(int rowIndex,int columnIndex){
							return false;
						}
					};
					
					for(Proveedor p:proveedores){
						Object[] fila = {p.getCodigo(),p.getNombre(),p.getDomicilio(),p.getTelefono(),p.getEmail(),null};
						
						String ruta_listaprecios = p.getListaprecios();
						
						if(ruta_listaprecios!=null){
							int index = p.getListaprecios().lastIndexOf('.');
						
							if(index!=-1){
								String extension = p.getListaprecios().substring(index+1);
								if(extension.equals("pdf")){
								
									ImageIcon icono_pdf = new ImageIcon(getClass().getResource("/img/icono_pdf.png"));
									JLabel paraIcono = new JLabel(icono_pdf);
								
									fila[5] = paraIcono;
								}
								else if(extension.equals("xls") || extension.equals("xlsx")){
								
									ImageIcon icono_excel = new ImageIcon(getClass().getResource("/img/icono_excel.png"));
									JLabel paraIcono = new JLabel(icono_excel);
									fila[5] = paraIcono;
								}
							}
						}
						else{
							fila[5]=new JLabel();
						}
						modelo.addRow(fila);
					}
					
					//REDIMENSIONA LA TABLA
					if(modelo.getRowCount()<10){
						contenedorTabla.setSize(830,modelo.getRowCount()*tabla.getRowHeight()+23);
					}
					else
						contenedorTabla.setSize(830,tabla.getRowHeight()*10+23);
				
					contenedorTabla.setLocation((tamanio_pantalla.width/2)-415, 130);
				
					//EDITA LOS BOTONES
					Point ubicacion_tabla = contenedorTabla.getLocation();
					Dimension tamanio_tabla = contenedorTabla.getSize();
					panel.actualizar.setText("Actualizar lista de precios");
					panel.actualizar.setLocation((tamanio_pantalla.width/2)-355,ubicacion_tabla.y+tamanio_tabla.height+20);
					panel.actualizar.setVisible(true);
					panel.aniadir.setText("Añadir Proveedor");
					panel.aniadir.setLocation((tamanio_pantalla.width/2)-135,ubicacion_tabla.y+tamanio_tabla.height+20);
					panel.aniadir.setVisible(true);
					panel.aniadir.setEnabled(true);
					panel.modif.setText("Modificar Proveedor");
					panel.modif.setLocation((tamanio_pantalla.width/2)+35,ubicacion_tabla.y+tamanio_tabla.height+20);
					panel.modif.setVisible(true);
					panel.modif.setEnabled(true);
					panel.elim.setText("Eliminar Proveedor");
					panel.elim.setLocation((tamanio_pantalla.width/2)+205,ubicacion_tabla.y+tamanio_tabla.height+20);
					panel.elim.setVisible(true);
				}else{
					String mensaje = "<html><Font size=5>¡No se pudo obtener la tabla de Proveedores! Error de conexión.</Font></html>";
					JOptionPane.showMessageDialog(null, mensaje, "¡Error!", JOptionPane.WARNING_MESSAGE);
				}
			}
			
			//SI SELECCIONA LA TABLA DE RUBROS
			else if(seleccion.equals("RUBROS")){
				consulta="rubros";
				//OBTIENE LA TABLA Y SUS FILAS
				consultarProductos = new EjecutaConsultas();
				ArrayList<Rubro> rubros = consultarProductos.consultaRubros();
				String[] columnasTabla = {"CÓDIGO","NOMBRE"};
				modelo = new DefaultTableModel(null,columnasTabla){
					/**
					 * 
					 */
					private static final long serialVersionUID = -544803118026232307L;

					public boolean isCellEditable(int rowIndex,int columnIndex){
						return false;
					}
				};
				if(rubros!=null){
					for(Rubro r:rubros){
						Object[] rubro = {r.getCodigoRubro(),r.getNombre()};
						modelo.addRow(rubro);
					}
					//REDIMENSIONA LA TABLA
					if(modelo.getRowCount()<10){
						contenedorTabla.setSize(300,modelo.getRowCount()*tabla.getRowHeight()+23);
					}
					else
						contenedorTabla.setSize(300,tabla.getRowHeight()*10+23);
					contenedorTabla.setLocation((tamanio_pantalla.width/2)-150, 130);
				
					//EDITA LOS BOTONES
					Point ubicacion_tabla = contenedorTabla.getLocation();
					Dimension tamanio_tabla = contenedorTabla.getSize();
					panel.aniadir.setText("Añadir Rubro");
					panel.aniadir.setLocation((tamanio_pantalla.width/2)-245,ubicacion_tabla.y+tamanio_tabla.height+20);
					panel.aniadir.setVisible(true);
					panel.aniadir.setEnabled(true);
					panel.modif.setText("Cambiar Nombre");
					panel.modif.setLocation((tamanio_pantalla.width/2)-75,ubicacion_tabla.y+tamanio_tabla.height+20);
					panel.modif.setVisible(true);
					panel.modif.setEnabled(true);
					panel.elim.setText("Eliminar Rubro");
					panel.elim.setLocation((tamanio_pantalla.width/2)+95,ubicacion_tabla.y+tamanio_tabla.height+20);
					panel.elim.setVisible(true);
				}
				else{
					JOptionPane.showMessageDialog(null,
							"<html><font size=4>No se pudo obtener la tabla de Rubros. Error de conexión</font></html>",
							"¡Error!", JOptionPane.WARNING_MESSAGE);
				}
			}
			//SI SELECCIONA LA TABLA DE PEDIDOS
			else if(seleccion.equals("PEDIDOS")){
				consulta="pedidos";
				//OBTIENE LA TABLA Y SUS FILAS
				consultarProveedor = new EjecutaConsultasProveedor();
				ArrayList<Object[]> pedidos = consultarProveedor.obtenerPedidosRecibidos();
				if(pedidos!=null){
					String[] columnasTabla ={"CÓDIGO","PROVEEDOR","MONTO","FECHA CREACIÓN","FECHA CONFIRMACIÓN","EMPLEADO"};
					modelo = new DefaultTableModel(null,columnasTabla){
						/**
						 * 
						 */
						private static final long serialVersionUID = 1927911913547428743L;

						public boolean isCellEditable(int rowIndex,int columnIndex){
							return false;
						}
					};
					for(Object[] p:pedidos){
						p[0]=String.format("%05d", p[0]);
						SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy hh:mm a");
						p[3]=sdf.format((Date)p[3]);
						p[4]=sdf.format((Date)p[4]);
				
						modelo.addRow(p);
					}
					//REDIMENSIONA LA TABLA
					if(modelo.getRowCount()<10){
						contenedorTabla.setSize(870,modelo.getRowCount()*tabla.getRowHeight()+23);
					}
					else
						contenedorTabla.setSize(870,tabla.getRowHeight()*10+23);
					contenedorTabla.setLocation((tamanio_pantalla.width/2)-435, 130);
			
					if(tabla.getKeyListeners().length!=0){
						tabla.removeKeyListener(tabla.getKeyListeners()[0]);
					}
					ObtenerLineasDePedido obtenerLineas = new ObtenerLineasDePedido(tabla,tcr);
					tabla.addKeyListener(obtenerLineas);
				}else{
					JOptionPane.showMessageDialog(null,"<html><font size=4>No se pudo obtener la tabla de Pedidos. Error de conexión.</font></html>",
							"¡Error!", JOptionPane.WARNING_MESSAGE);
				}
			}
			
			//CARGA EL CONTENIDO DE LA TABLA
			tabla.setModel(modelo);
			
			//DA FORMATO A LA TABLA
			int columnas = tabla.getColumnCount();
			for(int i=0;i<columnas;i++){
				tabla.getColumnModel().getColumn(i).setCellRenderer(tcr);
			}
			formatea.formatearColumnas(tabla, consulta);
			
			//SI ES LA TABLA DE PROVEEDORES, PREPARA LA COLUMNA PARA MOSTRAR ICONO DE LISTA DE PRECIOS
			if(consulta.equals("proveedores")){
				TableColumn columna_listaprecios = tabla.getColumnModel().getColumn(5);
				columna_listaprecios.setCellRenderer(new TableCellRenderer(){

					@Override
					public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
							boolean hasFocus, int row, int column) {
						if(value instanceof JLabel){
							JLabel label = ((JLabel)value);
							return label;
						}
						else{
							return null;
						}
					}
					
				});
			}
			//SI ES LA TABLA DE PEDIDOS, VENTAS O REPARTOS, SELECCIONA LA ULTIMA FILA
			if(consulta.equals("pedidos") || consulta.equals("ventas") || consulta.equals("repartos")){
				tabla.requestFocus();
				tabla.changeSelection(modelo.getRowCount()-1, 0, false, false);
			}
		//}
	}
	DefaultTableCellRenderer tcr;
	Dimension tamanio_pantalla;
	FormateaTablas formatea;
	JTable tabla;
	String consulta;
	PanelDetalle panel;
	JScrollPane contenedorTabla;
	EjecutaConsultas consultarProductos;
	EjecutaConsultasVenta consultarVenta;
	EjecutaConsultasReparto consultarReparto;
	EjecutaConsultasEmpleado consultarEmpleado;
	EjecutaConsultasProveedor consultarProveedor;
}

//CLASE QUE MUESTRA LA VENTANA DE LAS LINEAS DE REPARTO O DE VENTA, SEGUN CORRESPONDA
class MuestraLineas extends JDialog{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 499478787996297028L;

	public MuestraLineas(JTable tablaLineas, String titulo){
		
		setModal(true);
		setSize(1100, 250);
		setLocationRelativeTo(null);
		setLayout(null);
		setUndecorated(true);
		getRootPane().setBorder(BorderFactory.createLineBorder(Color.BLACK,2));

		JLabel paraTitulo = new JLabel("<html><font size=5>"+titulo.toUpperCase()+"</font></html>",JLabel.CENTER);
		paraTitulo.setBounds(0, 15, 1100, 30);
		add(paraTitulo);
		JScrollPane paraTabla = new JScrollPane(tablaLineas);
		if(tablaLineas.getRowCount()>4){
			paraTabla.setBounds(50,60,1000,150);
		}
		else{
			paraTabla.setBounds(50, 60, 1000, tablaLineas.getRowCount()*tablaLineas.getRowHeight()+24);
		}
		paraTabla.setBorder(BorderFactory.createLineBorder(Color.BLACK,2));
		add(paraTabla);
		
		ActionMap mapaAccion = this.getRootPane().getActionMap();
		InputMap mapa = this.getRootPane().getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
		
		KeyStroke escape = KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE,0);		
		
		mapa.put(escape,"accion escape");
		mapaAccion.put("accion escape", new AbstractAction(){
			/**
			 * 
			 */
			private static final long serialVersionUID = -2909008160592790508L;

			public void actionPerformed(ActionEvent e){
				dispose();
			}
		});
		
		setVisible(true);	
	}
}

//CLASE QUE DA FORMATO A LAS COLUMNAS DE LA TABLA, SEGÚN LA TABLA SELECCIONADA
class FormateaTablas{
	
	public void formatearColumnas(JTable tabla,String tipo){
		
		TableColumnModel modeloColumnas= tabla.getColumnModel(); 
		
		if(tipo.equals("formasdepago")){
			modeloColumnas.getColumn(0).setMaxWidth(70);
		}
		else if(tipo.equals("lineasdeventa")){		//"LÍNEA N°","CÓDIGO PRODUCTO","DESCRIPCIÓN","PRECIO UNITARIO","CANTIDAD","SUBTOTAL","REPARTE"};
			modeloColumnas.getColumn(0).setMaxWidth(70);
			modeloColumnas.getColumn(1).setMaxWidth(140);
			modeloColumnas.getColumn(2).setMaxWidth(390);
			modeloColumnas.getColumn(3).setMaxWidth(120);
			modeloColumnas.getColumn(4).setMaxWidth(70);
			modeloColumnas.getColumn(5).setMaxWidth(120);
			modeloColumnas.getColumn(6).setMaxWidth(70);
		}
		else if(tipo.equals("lineasdereparto")){    //"LÍNEA N°","N° VENTA","PRODUCTO","NOMBRE Y APELLIDO","DIRECCIÓN","TURNO","OBSERVACIONES"};
			modeloColumnas.getColumn(0).setMaxWidth(70);
			modeloColumnas.getColumn(1).setMaxWidth(70);
			modeloColumnas.getColumn(2).setMaxWidth(330);
			modeloColumnas.getColumn(3).setMaxWidth(140);
			modeloColumnas.getColumn(4).setMaxWidth(140);
			modeloColumnas.getColumn(5).setMaxWidth(70);
			modeloColumnas.getColumn(6).setMaxWidth(160);
		}	
		else if(tipo.equals("empleados")){ //{"ID","TIPO","NOMBRE","APELLIDO","DNI","DOMICILIO","E-MAIL","TELÉFONO","USUARIO","CONTRASEÑA"};
			modeloColumnas.getColumn(0).setMaxWidth(30);
			modeloColumnas.getColumn(1).setMaxWidth(110);
			modeloColumnas.getColumn(2).setMaxWidth(140);
			modeloColumnas.getColumn(3).setMaxWidth(110);
			modeloColumnas.getColumn(4).setMaxWidth(110);
			modeloColumnas.getColumn(5).setMaxWidth(170);
			modeloColumnas.getColumn(6).setMaxWidth(170);
			modeloColumnas.getColumn(7).setMaxWidth(120);
			modeloColumnas.getColumn(8).setMaxWidth(120);
			modeloColumnas.getColumn(9).setMaxWidth(120);
		}
		else if(tipo.equals("clientes")){ //{"DNI","NOMBRE","APELLIDO","DOMICILIO","TELÉFONO","EMAIL","CTA. CTE.",,"ESTADO"};
			modeloColumnas.getColumn(0).setMaxWidth(90);
			modeloColumnas.getColumn(1).setMaxWidth(140);
			modeloColumnas.getColumn(2).setMaxWidth(110);
			modeloColumnas.getColumn(3).setMaxWidth(180);
			modeloColumnas.getColumn(4).setMaxWidth(120);
			modeloColumnas.getColumn(5).setMaxWidth(170);
			modeloColumnas.getColumn(6).setMaxWidth(80);
			modeloColumnas.getColumn(7).setMaxWidth(110);
		}
		else if(tipo.equals("proveedores")){ //{"CODIGO","NOMBRE","DOMICILIO","TELÉFONO","EMAIL","LISTAPRECIOS"};
			modeloColumnas.getColumn(0).setMaxWidth(60);
			modeloColumnas.getColumn(1).setMaxWidth(170);
			modeloColumnas.getColumn(2).setMaxWidth(170);
			modeloColumnas.getColumn(3).setMaxWidth(110);
			modeloColumnas.getColumn(4).setMaxWidth(200);
			modeloColumnas.getColumn(5).setMaxWidth(120);
		}
		else if(tipo.equals("rubros")){ //{"CODIGO","NOMBRE"};
			modeloColumnas.getColumn(0).setMaxWidth(80);
			modeloColumnas.getColumn(1).setMaxWidth(220);
		}
		else if(tipo.equals("pedidos")){ //{"CODIGO","NOMBRE","DOMICILIO","TELÉFONO","EMAIL","LISTAPRECIOS"};
			modeloColumnas.getColumn(0).setMaxWidth(60);
			modeloColumnas.getColumn(1).setMaxWidth(200);
			modeloColumnas.getColumn(2).setMaxWidth(100);
			modeloColumnas.getColumn(3).setMaxWidth(170);
			modeloColumnas.getColumn(4).setMaxWidth(170);
			modeloColumnas.getColumn(5).setMaxWidth(170);
		}
		else if(tipo.equals("lineasdepedido")){ //{"LÍNEA N°","PRODUCTO","PRECIO UNITARIO","CANTIDAD","SUBTOTAL"}
			modeloColumnas.getColumn(0).setMaxWidth(70);
			modeloColumnas.getColumn(1).setMaxWidth(420);
			modeloColumnas.getColumn(2).setMaxWidth(170);
			modeloColumnas.getColumn(3).setMaxWidth(170);
			modeloColumnas.getColumn(4).setMaxWidth(170);
		}	
	}	
}

//CLASE QUE OBTIENE LAS LINEAS DE VENTA DE LA VENTA SELECCIONADA
class ObtenerLineasDeVenta extends KeyAdapter{
	
	public ObtenerLineasDeVenta(JTable tabla,DefaultTableCellRenderer tcr){
		
		this.tcr = tcr;
		this.tabla = tabla;
		formatea = new FormateaTablas();
		consultarVenta = new EjecutaConsultasVenta();
	}
	
	public void keyPressed(KeyEvent k){
	
		//CAPTURA LA TECLA ENTER Y CARGA LAS LINEAS DE VENTA
		if(k.getKeyCode()==KeyEvent.VK_ENTER){
			int codigo_venta = Integer.parseInt((String)tabla.getValueAt(tabla.getSelectedRow(), 0));
			
			ArrayList<LineaVenta> lineas = consultarVenta.obtenerLineasDeVenta(codigo_venta);
			if(lineas!=null){		
				JTable paraLineas = new JTable();
				paraLineas.setRowHeight(30);
				String[] columnasTabla={"LÍNEA N°","CÓDIGO PRODUCTO","DESCRIPCIÓN","PRECIO UNITARIO","CANTIDAD","SUBTOTAL","REPARTE"};
				DefaultTableModel modelo = new DefaultTableModel(null,columnasTabla){
					/**
					 * 
					 */
					private static final long serialVersionUID = -3190813595424359610L;

					public boolean isCellEditable(int rowIndex,int columnIndex){
						return false;
					}
				};
			
				for(LineaVenta l:lineas){
					int cod = l.getCodigo();
					int cod_producto = l.getCodProducto();
					double precio_u = l.getPrecio_u();
					double cantidad = l.getCantidad();
					double subtotal = l.getSubtotal();
					boolean reparte = l.isReparte();
			
					String c1 = String.format("%07d", cod);
					String c2 = String.format("%05d", cod_producto);
					String c7;
					if(reparte){
						c7="SI";
					}else{
						c7="NO";
					}
					EjecutaConsultas consulta = new EjecutaConsultas();
					Producto producto = consulta.obtieneProducto(cod_producto);
					String desc;
					if(producto!=null){
						desc = producto.getDescripcion();
					}else{
						desc="Producto desconocido";
					}
					Object[] fila = {c1,c2,desc,precio_u,cantidad,subtotal,c7};
					modelo.addRow(fila);
				}
			
				paraLineas.setModel(modelo);
				int columnas = paraLineas.getColumnCount();
				for(int i=0;i<columnas;i++){
					paraLineas.getColumnModel().getColumn(i).setCellRenderer(tcr);
				}
			
				formatea.formatearColumnas(paraLineas, "lineasdeventa");
			
				new MuestraLineas(paraLineas, "Venta N° " + String.format("%07d",codigo_venta));
			}else{
				JOptionPane.showMessageDialog(null,
						"<html><font size=4>¡Error de conexión con la base de datos! Intente nuevamente.</font></html>",
						"¡Error!", JOptionPane.WARNING_MESSAGE);
			}
		}
	}
	
	DefaultTableCellRenderer tcr;
	FormateaTablas formatea;
	JTable tabla;

	EjecutaConsultasVenta consultarVenta;
}

//CLASE QUE OBTIENE LAS LINEAS DE REPARTO CORRESPONDIENTES AL REPARTO SELECCIONADO
class ObtenerLineasDeReparto extends KeyAdapter{
	
	public ObtenerLineasDeReparto(JTable tabla,DefaultTableCellRenderer tcr){
		
		this.tcr = tcr;
		this.tabla = tabla;
		formatea = new FormateaTablas();
		consultarReparto = new EjecutaConsultasReparto();
	}
	
	public void keyPressed(KeyEvent k){
		//CAPTURA LA TECLA ENTER Y CARGA LAS LINEAS DEL REPARTO SELECCIONADO
		if(k.getKeyCode()==KeyEvent.VK_ENTER){
			int codigo_reparto = Integer.parseInt((String)tabla.getValueAt(tabla.getSelectedRow(), 0));
			
			ArrayList<Object[]> lineas = consultarReparto.obtenerDetalleLineas(codigo_reparto);
			if(lineas!=null){
				JTable paraLineas = new JTable();
				paraLineas.setRowHeight(30);
				String[] columnasTabla={"LÍNEA N°","N° VENTA","PRODUCTO","NOMBRE Y APELLIDO","DIRECCIÓN","TURNO","OBSERVACIONES"};
				DefaultTableModel modelo = new DefaultTableModel(null,columnasTabla){
					/**
					 * 
					 */
					private static final long serialVersionUID = -2858481906812851375L;

					public boolean isCellEditable(int rowIndex,int columnIndex){
						return false;
					}
				};
			
				for(Object[] l:lineas){
				
					l[0] = String.format("%07d", l[0]);
					l[1] = String.format("%07d", l[1]);
				
					modelo.addRow(l);
				}
			
				paraLineas.setModel(modelo);
				int columnas = paraLineas.getColumnCount();
				for(int i=0;i<columnas;i++){
					paraLineas.getColumnModel().getColumn(i).setCellRenderer(tcr);
				}
				formatea.formatearColumnas(paraLineas, "lineasdereparto");
			
				new MuestraLineas(paraLineas,"Reparto N° " + String.format("%05d",codigo_reparto));
			}
			else{
				JOptionPane.showMessageDialog(null,
					"<html><font size=4>¡Error de conexión con la base de datos! Intente nuevamente.</font></html>",
					"¡Error!", JOptionPane.WARNING_MESSAGE);
			}
		}
	}
	
	DefaultTableCellRenderer tcr;
	FormateaTablas formatea;
	JTable tabla;
	EjecutaConsultasReparto consultarReparto;
	EjecutaConsultasProveedor consultarProveedor;
}

class ObtenerLineasDePedido extends KeyAdapter{
	
	public ObtenerLineasDePedido(JTable tabla,DefaultTableCellRenderer tcr){
		
		this.tcr = tcr;
		this.tabla = tabla;
		formatea = new FormateaTablas();
		consultarProveedor = new EjecutaConsultasProveedor();
	}
	
	public void keyPressed(KeyEvent k){
		if(k.getKeyCode()==KeyEvent.VK_ENTER){
			//CAPTURA LA TECLA ENTER Y MUESTRA LAS LINEAS DEL PEDIDO SELECCIONADO
			int codigo_pedido = Integer.parseInt((String)tabla.getValueAt(tabla.getSelectedRow(), 0));
			
			ArrayList<Object[]> lineas = consultarProveedor.obtenerLineasPedidoRecibido(codigo_pedido);
			if(lineas!=null){
				JTable paraLineas = new JTable();
				paraLineas.setRowHeight(30);
				String[] columnasTabla={"LÍNEA N°","PRODUCTO","PRECIO UNITARIO","CANTIDAD","SUBTOTAL"};
				DefaultTableModel modelo = new DefaultTableModel(null,columnasTabla){
					/**
					 * 
					 */
					private static final long serialVersionUID = -2304484410529178238L;

					public boolean isCellEditable(int rowIndex,int columnIndex){
						return false;
					}
				};
			
				for(Object[] l:lineas){
					
					l[0] = String.format("%05d", l[0]);
				
					modelo.addRow(l);
				}
			
				paraLineas.setModel(modelo);
				int columnas = paraLineas.getColumnCount();
				for(int i=0;i<columnas;i++){
					paraLineas.getColumnModel().getColumn(i).setCellRenderer(tcr);
				}
				formatea.formatearColumnas(paraLineas, "lineasdepedido");
			
				new MuestraLineas(paraLineas,"Pedido N° " + String.format("%05d",codigo_pedido));
			}
			else{
				JOptionPane.showMessageDialog(null,
					"<html><font size=4>¡Error de conexión con la base de datos! Intente nuevamente.</font></html>",
					"¡Error!", JOptionPane.WARNING_MESSAGE);
			}
		}
	}
	
	DefaultTableCellRenderer tcr;
	FormateaTablas formatea;
	JTable tabla;
	EjecutaConsultasReparto consultarReparto;
	EjecutaConsultasProveedor consultarProveedor;
}
