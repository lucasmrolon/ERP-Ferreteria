package vista.compras;

import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;

import javax.swing.*;
import javax.swing.event.*;
import javax.swing.table.*;
import javax.swing.text.JTextComponent;

import controlador.CrearTabla;
import controlador.compras.ConsultarDatosDePedidos;
import controlador.compras.RegistrarPedido;
import modelo.consultas.EjecutaConsultasProveedor;
import vista.stock.PanelStock;

//VENTANA DE CREACION DE PEDIDO
public class CrearPedido extends JDialog{
	/**
	 * 
	 */
	private static final long serialVersionUID = 6085135493451798354L;

	public CrearPedido(JTable tabla,PanelStock panelStock){
		
		Dimension pantalla = Toolkit.getDefaultToolkit().getScreenSize();
		
		setModal(true);
		setUndecorated(true);
		setSize(pantalla.width/2,pantalla.height);
		setResizable(false);
		setLocation(0,0);	
		setTitle("Nuevo Pedido");
		
		getRootPane().setBorder(BorderFactory.createLineBorder(Color.BLACK));
		String nom_proveedor = (String)tabla.getValueAt(tabla.getSelectedRow(), 1);
		
		//OBTIENE NUMERO DE PEDIDO
		ConsultarDatosDePedidos nuevaConsulta = new ConsultarDatosDePedidos();
		int n_pedido = nuevaConsulta.obtener_n_prox_pedido();
		
		//AÑADE LOS PANELES COMPONENTES
		add(new PanelEncabezado(nom_proveedor,n_pedido),BorderLayout.NORTH);
		add(new PanelCreacion(nom_proveedor,n_pedido,this,panelStock),BorderLayout.CENTER);
		
		//OBTIENE LA LISTA DE PRECIOS Y LA ABRE
		EjecutaConsultasProveedor paraObtenerLista = new EjecutaConsultasProveedor();
		String ruta_archivo_precios = paraObtenerLista.obtenerListaPrecios((String)tabla.getValueAt(tabla.getSelectedRow(), 0));
		System.out.println(ruta_archivo_precios);
		
		CrearPedido ventana = this;
		try {
			Desktop.getDesktop().open(new File(ruta_archivo_precios));
			setVisible(true);
		} catch (IllegalArgumentException e) {
			String mensaje = "<html><Font size=5>¡Este equipo no está autorizado para realizar la operación seleccionada!</Font></html>";
			JOptionPane.showMessageDialog(null, mensaje, "¡Error!", JOptionPane.WARNING_MESSAGE);
			ventana.dispose();
		}
		catch (IOException e) {
			String mensaje = "<html><Font size=5>¡No se pudo abrir el archivo! Intente nuevamente.</Font></html>";
			JOptionPane.showMessageDialog(null, mensaje, "¡Error!", JOptionPane.WARNING_MESSAGE);
		}
		
	}
		
}

//PANEL QUE MUESTRA NUMERO DE PEDIDO Y NOMBRE DEL PROVEEDOR
class PanelEncabezado extends JPanel{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -5857097415139994038L;

	public PanelEncabezado(String nom_proveedor, int n_pedido){
		
		setLayout(null);
		setBackground(new Color(160,160,233));
		setPreferredSize(new Dimension(800,70));
		
		JLabel pedido = new JLabel("<html><font size=5>PEDIDO N° " + String.format("%05d", n_pedido) + "</font></html>");
		pedido.setSize(400,20);
		pedido.setLocation(20,10);
		JLabel proveedor = new JLabel("<html><font size=5>PROVEEDOR: <strong>"+ nom_proveedor  +"</strong></font></html>");
		proveedor.setSize(400,20);
		proveedor.setLocation(20,40);
		add(pedido);
		add(proveedor);
		
	}
}

//PANEL DE EDICION
class PanelCreacion extends JPanel{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1623413101954814136L;
	public PanelCreacion(String nom_proveedor,int n_pedido,CrearPedido ventanaCreacion,PanelStock panelStock){
		
		monto_pedido=0;
		DecimalFormat df = new DecimalFormat("###,##0.00");
		setBackground(new Color(191,191,240));
		setLayout(null);
		
		JLabel mensaje = new JLabel("<html><font size=4>A continuación, añada los productos que desee incluir en el pedido. "
				+ "Al finalizar, presione ENTER.</font></html>");
		mensaje.setBounds(20, 10, 600, 50);
		add(mensaje);

		JLabel total_pedido = new JLabel("<html><font size=5>TOTAL PEDIDO: $ 0,00</font></html>",JLabel.CENTER);
		total_pedido.setBounds(10, 350, 700, 30);
		add(total_pedido);
		
		//MUESTRA LA LISTA DE PRODUCTOS AÑADIDOS AL PEDIDO
		String[] columnas = {"CODIGO","DESCRIPCIÓN","PRECIO U.","CANTIDAD","SUBTOTAL"};
		DefaultTableModel modelopedido = new DefaultTableModel(null,columnas){
			/**
			 * 
			 */
			private static final long serialVersionUID = 2209045568466783867L;

			public boolean isCellEditable(int rowIndex,int columnIndex){
				return false;
			}
		};
		tablaPedido = new JTable(modelopedido);
		tablaPedido.getModel().addTableModelListener(new TableModelListener(){
			@Override
			public void tableChanged(TableModelEvent arg0) {
				monto_pedido=0;
				for(int i=0;i<tablaPedido.getRowCount();i++){
					monto_pedido+=(double)tablaPedido.getValueAt(i, 4);
				}
				total_pedido.setText("<html><font size=5>TOTAL PEDIDO:&nbsp;&nbsp;&nbsp;<font color=blue>$ "+df.format(monto_pedido)+"</font></font></html>");
			}
		});
		tablaPedido.addKeyListener(new KeyAdapter(){
			public void keyPressed(KeyEvent e){
				if(e.getKeyChar()==KeyEvent.VK_DELETE){
					int seleccionado = tablaPedido.getSelectedRow();
					((DefaultTableModel)tablaPedido.getModel()).removeRow(seleccionado);
				}
			}
		});
		formatearTabla();
		JScrollPane paraTabla = new JScrollPane(tablaPedido);
		paraTabla.setBounds(65, 120, 600, tablaPedido.getRowHeight()*10+23);
		add(paraTabla);
		
		JLabel mensaje2 = new JLabel("<html><font size=4>Nota: Si desea eliminar una línea del pedido actual, selecciónela y presione la tecla SUPRIMIR</font></html>");
		mensaje2.setBounds(20, 450, 600, 50);
		add(mensaje2);
		
		//BOTON PARA AÑADIR PRODUCTOS A LA LISTA
		JButton agregar_producto = new JButton("");
		agregar_producto.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				new VentanaAdicionProductos(modelopedido,nom_proveedor,panelStock);
			}
		});
		agregar_producto.setBounds(10, 118, 50, 50);
		agregar_producto.setIcon(new ImageIcon(getClass().getResource("/img/sumar_al_carrito.png")));
		add(agregar_producto);
		
		
		//ESTABLECE ATAJOS DE TECLADO
		ActionMap mapaAccion = this.getActionMap();
		InputMap mapa = this.getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
		
		KeyStroke espacio = KeyStroke.getKeyStroke(KeyEvent.VK_SPACE,0);
		KeyStroke enter = KeyStroke.getKeyStroke(KeyEvent.VK_ENTER,0);
		KeyStroke escape = KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE,0);
		
		mapa.put(espacio,"accion barra_esp");
		mapaAccion.put("accion barra_esp", new AbstractAction(){
			/**
			 * 
			 */
			private static final long serialVersionUID = -1368634158243175966L;

			public void actionPerformed(ActionEvent e){
				agregar_producto.doClick();
			}
		});
		mapa.put(enter,"enter");
		mapaAccion.put("enter", new AbstractAction(){
			/**
			 * 
			 */
			private static final long serialVersionUID = -6350906902517603588L;

			public void actionPerformed(ActionEvent e){
				if(tablaPedido.getRowCount()!=0){
					int elegido = JOptionPane.showConfirmDialog(null, "<html><font size=5>¿Confirma registro del pedido creado?</font></html>", 
							"Mensaje de confirmación", JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE);
					if(elegido==JOptionPane.YES_OPTION){
						ventanaCreacion.dispose();
						new RegistrarPedido(tablaPedido.getModel(),monto_pedido,nom_proveedor,n_pedido);
					}
				}
				else{
					JOptionPane.showMessageDialog(null, "<html><font size=5>¡Todavía no ha añadido productos al pedido!</font></html>", 
							"¡Error!", JOptionPane.QUESTION_MESSAGE);
				}
			}
		});
		mapa.put(escape,"accion escape");
		mapaAccion.put("accion escape", new AbstractAction(){
			/**
			 * 
			 */
			private static final long serialVersionUID = -1368634158243175966L;

			public void actionPerformed(ActionEvent e){

				int elegido = JOptionPane.showConfirmDialog(null, "<html><font size=5>Si abandona ahora no se guardarán los cambios realizados. "
						+ "¿Cancela la creación del pedido?</font></html>", 
						"Mensaje de confirmación", JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE);
				if(elegido==JOptionPane.YES_OPTION){
					ventanaCreacion.dispose();
				}
				
			}
		});
		
	}
	
	//FORMATEA LAS TABLAS
	public void formatearTabla(){
		tablaPedido.setRowHeight(20);
		DefaultTableCellRenderer tcr = new DefaultTableCellRenderer();
		tcr.setHorizontalAlignment(SwingConstants.CENTER);
		TableColumnModel modeloColumnas = tablaPedido.getColumnModel();
		modeloColumnas.getColumn(0).setMinWidth(70);
		modeloColumnas.getColumn(1).setMinWidth(300);
		modeloColumnas.getColumn(2).setMinWidth(80);
		modeloColumnas.getColumn(3).setMinWidth(70);
		modeloColumnas.getColumn(4).setMinWidth(80);
		
		for(int i=0;i<5;i++){
			if(i!=1){
				modeloColumnas.getColumn(i).setCellRenderer(tcr);
			}
		}
	}
	
	JTable tablaPedido;
	double monto_pedido;
}

//VENTANA PARA SELECCIONAR UN PRODUCTO DE LA BD O CREAR UNO NUEVO
class VentanaAdicionProductos extends JDialog{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 6535525052373793665L;

	public VentanaAdicionProductos(DefaultTableModel modelopedido, String nomb_proveedor, PanelStock panelStock){
		
		setLayout(null);
		getContentPane().setBackground(new Color(191,191,240));
		setModal(true);
		setUndecorated(true);
		getRootPane().setBorder(BorderFactory.createLineBorder(Color.BLACK));
		
		setSize(800,300);
		setLocationRelativeTo(null);
		
		EjecutaConsultasProveedor paraObtenerDatosProveedor = new EjecutaConsultasProveedor();
		nomb_prov = nomb_proveedor;
		cod_prov = paraObtenerDatosProveedor.consultaCodProveedor(nomb_proveedor);
		
		setTitle("Selección de Producto");
		JLabel instrucciones = new JLabel("<html><font size=4>Busque y seleccione el producto requerido. Luego presione ENTER.</font></html>");
		instrucciones.setBounds(20, 10, 700, 30);
		add(instrucciones);
		
		paraBuscarProductos = new JComboBox<Object>();
		
		paraBuscarProductos.setEditable(true);
		paraBuscarProductos.setMaximumRowCount(10);
		//paraBuscarProductos.removeAllItems();
		
		String[] columnasTabla = {"CÓD.","DESC.","PRECIO VENTA","STOCK","UNIDAD","OBS."};
		
		//SE BLOQUEA LA EDICIÓN DE CELDAS DE LA TABLA
		DefaultTableModel modeloseleccion=new DefaultTableModel(null,columnasTabla){
			/**
			 * 
			 */
			private static final long serialVersionUID = 5414682773264554504L;

			public boolean isCellEditable(int rowIndex,int columnIndex){
				return false;
			}
		};
		
		muestraProductoSeleccionado = new JTable(modeloseleccion);
		muestraProductoSeleccionado.setFocusable(false);
		darFormatoATabla("productoseleccionado");
		JScrollPane paraTabla = new JScrollPane(muestraProductoSeleccionado);
		paraTabla.setBounds(40,130,720,43);
		paraTabla.setFocusable(false);
		add(paraTabla);
		
		CrearTabla paraObtenerModeloProductos = new CrearTabla();
		DefaultTableModel modeloProductos = paraObtenerModeloProductos.obtenerTablaProductos();
		
		JLabel desc = new JLabel("DESCRIPCIÓN PRODUCTO:");
		desc.setBounds(50,60,150,30);
		add(desc);
		
		paraBuscarProductos.getEditor().getEditorComponent().addKeyListener(new KeyAdapter(){
			@Override
			public void keyReleased(KeyEvent e) {
				if(e.getKeyCode()!=KeyEvent.VK_UP && e.getKeyCode()!=KeyEvent.VK_DOWN){
					if(e.getKeyCode()==KeyEvent.VK_ENTER){
							paraBuscarProductos.setPopupVisible(false);
							if(modeloseleccion.getRowCount()!=0 && modeloseleccion.getValueAt(0, 1).equals(paraBuscarProductos.getSelectedItem())){
								
								String cod_producto = (String)modeloseleccion.getValueAt(0, 0);
								String desc = (String)modeloseleccion.getValueAt(0, 1);
							
								Object[] lineapedido = {cod_producto,desc,0,0,0};
								
								dispose();
								
								new VentanaSolicitaPrecioYCantidad(modeloseleccion,lineapedido,modelopedido);
								
								
								
							}
							else{
								if(modeloseleccion.getRowCount()!=0){
									modeloseleccion.removeRow(0);
								}
								for(int i=0;i<modeloProductos.getRowCount();i++){
									if(((String)modeloProductos.getValueAt(i, 1)).equals(paraBuscarProductos.getSelectedItem()) && 
											((String)modeloProductos.getValueAt(i, 6)).equals(cod_prov)){
										Object[] fila = {modeloProductos.getValueAt(i, 0),modeloProductos.getValueAt(i, 1),
												modeloProductos.getValueAt(i, 3),modeloProductos.getValueAt(i, 4),modeloProductos.getValueAt(i, 5),
												modeloProductos.getValueAt(i, 8)};
									
										modeloseleccion.addRow(fila);
									}
								}
							}
						
						//}
					}
					else{
						String a_buscar = (String)(((JTextComponent)paraBuscarProductos.getEditor().getEditorComponent()).getText());
						
						a_buscar = a_buscar.toUpperCase();
				
						if(!a_buscar.trim().equals("")){
						
							paraBuscarProductos.removeAllItems();
							paraBuscarProductos.setSelectedItem(a_buscar);
				
							for(int i=0;i<modeloProductos.getRowCount();i++){
								if(((String)modeloProductos.getValueAt(i, 1)).contains(a_buscar) && 
										((String)modeloProductos.getValueAt(i, 6)).equals(cod_prov)){
									paraBuscarProductos.addItem(modeloProductos.getValueAt(i, 1));
									paraBuscarProductos.setPopupVisible(true);
								}
							}
						}
					}	
				}
			}			
		});

		paraBuscarProductos.setBounds(220, 60, 300, 30);
		add(paraBuscarProductos);
		
		VentanaAdicionProductos ventana = this;
		paraNuevoProducto = new JButton("Crear Nuevo Producto");
		paraNuevoProducto.setBounds(550, 60, 200, 30);
		paraNuevoProducto.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				VentanaCrearProducto ventanaCreacion = new VentanaCrearProducto(ventana,panelStock);
				ventanaCreacion.setVisible(true);
			}
			
		});
		add(paraNuevoProducto);
		
		ActionMap mapaAccion = this.getRootPane().getActionMap();
		InputMap mapa = this.getRootPane().getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
		
		KeyStroke escape = KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE,0);		
		
		mapa.put(escape,"accion escape");
		mapaAccion.put("accion escape", new AbstractAction(){
			/**
			 * 
			 */
			private static final long serialVersionUID = -290900816092790508L;

			public void actionPerformed(ActionEvent e){
				dispose();
			}
		});
	
		setVisible(true);
		
	}
	
	//DA FORMATO A TABLA QUE MUESTRA PRODUCTO SELECCIONADO
	public void darFormatoATabla(String nombre_tabla){
		
		if(nombre_tabla.equals("productoseleccionado")){
			
			muestraProductoSeleccionado.setRowHeight(20);
			
			TableColumnModel modeloColumnas = muestraProductoSeleccionado.getColumnModel();
			DefaultTableCellRenderer tcr = new DefaultTableCellRenderer();
			tcr.setHorizontalAlignment(SwingConstants.CENTER);
			
			TableColumn codigo= modeloColumnas.getColumn(0);
			codigo.setMinWidth(80);
			codigo.setCellRenderer(tcr);
			
			TableColumn descripcion= modeloColumnas.getColumn(1);
			descripcion.setMinWidth(300);
			
			TableColumn precio_venta= modeloColumnas.getColumn(2);
			precio_venta.setMinWidth(100);
			precio_venta.setCellRenderer(tcr);
			
			TableColumn cantidad=modeloColumnas.getColumn(3);
			cantidad.setMinWidth(80);
			cantidad.setCellRenderer(tcr);
			
			TableColumn unidad= modeloColumnas.getColumn(4);
			unidad.setMinWidth(70);
			unidad.setCellRenderer(tcr);
			
			TableColumn obs= modeloColumnas.getColumn(5);
			obs.setMinWidth(90);
			obs.setCellRenderer(tcr);
			
		}
		
	}
	
	public JTable muestraProductoSeleccionado;
	public String cod_prov,nomb_prov;
	public JComboBox<Object> paraBuscarProductos;
	JButton paraNuevoProducto;
	
}

//VENTANA QUE SOLICITA PRECIO Y CANTIDAD
class VentanaSolicitaPrecioYCantidad extends JDialog{
	/**
	 * 
	 */
	private static final long serialVersionUID = 6794825123037388173L;

	public VentanaSolicitaPrecioYCantidad(DefaultTableModel modeloseleccion,Object[] lineapedido,DefaultTableModel modelopedido){
		setModal(true);
		setLayout(null);
		setSize(360,170);
		setLocationRelativeTo(null);
		setTitle((String)lineapedido[1]);
		
		//PARA AÑADIR PRECIO
		JLabel pidePrecio = new JLabel("Precio fijado por proveedor:");
		pidePrecio.setBounds(20, 20, 200, 30);
		add(pidePrecio);
		JTextField paraPrecio = new JTextField();
		paraPrecio.setHorizontalAlignment(JTextField.CENTER);
		paraPrecio.setBounds(200, 20, 100, 30);
		paraPrecio.setText("0");
		paraPrecio.addKeyListener(new KeyAdapter(){
			public void keyTyped(KeyEvent e){
				if(e.getKeyChar()>='0' && e.getKeyChar()<='9'){
					Double actual;
					if(paraPrecio.getText().equals("")){
						actual=0.00;
					}else{
						actual = Double.parseDouble(paraPrecio.getText());
					}
					actual = actual*10+e.getKeyChar()/100;
					paraPrecio.setText(((Double)(Math.round(actual*100d)/100d)).toString());
				}else{
					e.consume();
				}
			}
		});
		add(paraPrecio);
		
		//PARA AÑADIR CANTIDAD
		JLabel pideCantidad = new JLabel("Cantidad a pedir:");
		pideCantidad.setBounds(20,70,200,30);
		add(pideCantidad);
		JTextField paraCantidad = new JTextField();
		paraCantidad.setBounds(200, 70, 100, 30);
		paraCantidad.setHorizontalAlignment(JTextField.CENTER);
		paraCantidad.setText("0");
		paraCantidad.addKeyListener(new KeyAdapter(){
			public void keyTyped(KeyEvent e){
				if(e.getKeyChar()<'0' || e.getKeyChar()>'9'){
					e.consume();
				}
			}
		});
		paraCantidad.addFocusListener(new FocusAdapter(){
			public void focusGained(FocusEvent e){
				if(paraCantidad.getText().equals("0")){
					paraCantidad.setText("");
				}
			}
		});
		add(paraCantidad);
		String unidad=(String)modeloseleccion.getValueAt(0, 5);
		if(unidad.equals("UNIDAD")){
			unidad="U.";
		}
		else if(unidad.equals("KILOGRAMOS")){
			unidad="Kg.";
		}
		else if(unidad.equals("LITROS")){
			unidad="L.";
		}
		else if(unidad.equals("METROS")){
			unidad="M.";
		}
		JLabel paraUnidad = new JLabel(unidad); 
		paraUnidad.setBounds(305, 70, 20, 30);
		add(paraUnidad);
		
		ActionMap mapaAccion = this.getRootPane().getActionMap();
		InputMap mapa = this.getRootPane().getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
		
		KeyStroke enter = KeyStroke.getKeyStroke(KeyEvent.VK_ENTER,0);
		
		mapa.put(enter,"accion enter");
		mapaAccion.put("accion enter", new AbstractAction(){
			/**
			 * 
			 */
			private static final long serialVersionUID = 6669360036691284143L;

			public void actionPerformed(ActionEvent e){
				double cantidad = Double.parseDouble(paraCantidad.getText());
				double precio_u = Double.parseDouble(paraPrecio.getText());
				
				if(cantidad!=0){
					lineapedido[2]=precio_u;
					lineapedido[3]=cantidad;
					lineapedido[4]=Math.round(precio_u*cantidad*100d)/100d;
				
					modelopedido.addRow(lineapedido);
					dispose();
				}
			}
		});
		
		setVisible(true);
	}
}
