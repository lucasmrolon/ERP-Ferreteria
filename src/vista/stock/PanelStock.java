package vista.stock;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.RowFilter.ComparisonType;
import javax.swing.border.*;
import javax.swing.table.*;

import controlador.ActualizarModelo;
import controlador.CrearTabla;
import controlador.FiltrarTabla;
import controlador.stock.GestionarProductos;
import controlador.stock.MostrarObs;
import modelo.objetos.Producto;
import vista.ventas.PanelVentas;

//PANEL PARA ADMINISTRAR LOS PRODUCTOS Y SU STOCK
public class PanelStock extends JPanel {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -3709942334969246628L;
	public PanelStock(DefaultTableModel contenidoTabla,PanelVentas panelVentas){
		
		setBackground(new Color(224,224,248));
		setLayout(null);
		this.paraVenta = panelVentas;
		
		//OBTIENE EL CONTENIDO DE LA TABLA DE PRODUCTOS
		model=contenidoTabla;
		
		//ESTABLECE FORMATO DE LA TABLA
		tablaProductos = new JTable();
		tablaProductos.setModel(contenidoTabla);
		tablaProductos.setRowSorter(new TableRowSorter<TableModel>(contenidoTabla));/*
		tablaProductos.getRowSorter().toggleSortOrder(0);*/
		tablaProductos.setRowHeight(20);
		tablaProductos.setShowHorizontalLines(false);
		darFormatoATabla();
	
		//AÑADE LA TABLA
		JScrollPane paraTabla = new JScrollPane(tablaProductos);
		paraTabla.setBounds(300,50,1000,400);
		paraTabla.setBorder(BorderFactory.createDashedBorder(Color.BLUE,5,10));
		add(paraTabla);
		
		//BOTON PARA MOSTRAR/OCULTAR LA TABLA DE PRODUCTOS
		JButton muestraTabla = new JButton("Mostrar Tabla");
		muestraTabla.setBounds(50, 50, 150, 30);
		muestraTabla.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if(paraTabla.isVisible()){
					paraTabla.setVisible(false);
				}else{
					paraTabla.setVisible(true);
				}
			}
			
		});
		add(muestraTabla);
		
		//BOTÓN PARA AÑADIR PRODUCTOS NUEVOS
		nuevoProducto = new JButton("<html><font size=1>CTRL+N</font> |<br><font size=4>Nuevo Producto</font></html>");
		nuevoProducto.setBounds(50,100,220,40);
		nuevoProducto.setHorizontalAlignment(SwingConstants.LEFT);
		nuevoProducto.setIcon(new ImageIcon(getClass().getResource("/img/aniadir_producto.png")));
		nuevoProducto.addActionListener(new GestionarProductos(this,tablaProductos));
		add(nuevoProducto);
		
		//BOTÓN PARA ELIMINAR PRODUCTOS
		eliminarProducto = new JButton("<html><font size=1>CTRL+E</font> |<br><font size=4>Eliminar Producto</font></html>");
		eliminarProducto.setBounds(50, 160, 220, 40);
		eliminarProducto.setHorizontalAlignment(SwingConstants.LEFT);
		eliminarProducto.setIcon(new ImageIcon(getClass().getResource("/img/descartar.png")));
		eliminarProducto.addActionListener(new GestionarProductos(this,tablaProductos));
		add(eliminarProducto);
		
		//BOTÓN PARA MODIFICAR PRODUCTOS
		modificarProducto = new JButton("<html><font size=1>CTRL+M</font> |<br><font size=4>Modificar Producto</font></html>");
		modificarProducto.setBounds(50, 220, 220, 40);
		modificarProducto.setHorizontalAlignment(SwingConstants.LEFT);
		modificarProducto.setIcon(new ImageIcon(getClass().getResource("/img/modificar.png")));
		modificarProducto.addActionListener(new GestionarProductos(this,tablaProductos));
		add(modificarProducto);

		//CUADRO DE BÚSQUEDA DE PRODUCTOS
		JLabel busqueda= new JLabel("BUSQUEDA:");
		busqueda.setBounds(50,270,200,30);
		add(busqueda);
		paraBusqueda = new JTextField(30);
		paraBusqueda.setBounds(50,300,220,30);
		paraBusqueda.addKeyListener(new FiltrarTabla(tablaProductos));
		paraBusqueda.addFocusListener(new FocusAdapter(){
			public void focusGained(FocusEvent e){
				tablaProductos.clearSelection();
			}
		});
		add(paraBusqueda);
		
		//BOTÓN PARA MOSTRAR SÓLO LOS PRODUCTOS QUE PRESENTAN UN STOCK BAJO
		JButton filtrarStockBajo = new JButton("Stock Bajo");
		filtrarStockBajo.setBounds(300, 530, 150, 30);
		filtrarStockBajo.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				TableRowSorter<?> orden = (TableRowSorter<?>)tablaProductos.getRowSorter();
				orden.setRowFilter(RowFilter.regexFilter("BAJO",7));
			}
		});
		add(filtrarStockBajo);
		
		//BOTÓN PARA MOSTRAR SÓLO LOS PRODUCTOS CON STOCK CERO
		JButton filtrarSinStock = new JButton("Sin Stock");
		filtrarSinStock.setBounds(450, 530, 150, 30);
		filtrarSinStock.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				TableRowSorter<?> orden = (TableRowSorter<?>)tablaProductos.getRowSorter();
				orden.setRowFilter(RowFilter.numberFilter(ComparisonType.EQUAL,0.0,4));			
			}
		});
		add(filtrarSinStock);
		
		//BOTÓN PARA MOSTRAR TODOS LOS PRODUCTOS
		JButton mostrarTodos = new JButton("Mostrar Todos");
		mostrarTodos.setBounds(650, 530, 150, 30);
		mostrarTodos.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				
				TableRowSorter<?> orden = (TableRowSorter<?>)tablaProductos.getRowSorter();
				orden.setRowFilter(RowFilter.regexFilter("",1));
			}
			
		});
		add(mostrarTodos);
		
		//BOTÓN PARA ACTUALIZAR PRECIOS
		actualizaStock = new JButton("<html><font size=1>CTRL+S</font> |<br><font size=4>Actualizar Stock</font></html>");
		actualizaStock.setBounds(50, 350, 220, 40);
		actualizaStock.setIcon(new ImageIcon(getClass().getResource("/img/icono_actualizar.png")));
		actualizaStock.setHorizontalAlignment(SwingConstants.LEFT);
		actualizaStock.addActionListener(new GestionarProductos(this,tablaProductos));
		add(actualizaStock);
		
		//BOTÓN PARA ACTUALIZAR PRECIOS
		actualizaPrecios = new JButton("<html><font size=1>CTRL+U</font> |<br><font size=4>Actualizar Precios</font></html>");
		actualizaPrecios.setBounds(50, 410, 220, 40);
		actualizaPrecios.setIcon(new ImageIcon(getClass().getResource("/img/icono_actualizar.png")));
		actualizaPrecios.setHorizontalAlignment(SwingConstants.LEFT);
		actualizaPrecios.addActionListener(new GestionarProductos(this,null));
		add(actualizaPrecios);
		
		JButton generarReporte = new JButton("Generar Reporte de inventario");
		generarReporte.setBounds(1060, 530, 240, 30);
		generarReporte.setIcon(new ImageIcon(getClass().getResource("/img/imprimir.png")));
		generarReporte.addActionListener(new GenerarReporteInventario());
		add(generarReporte);
		
		ActionMap mapaAccion = getActionMap();
		InputMap mapa = getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
		
		KeyStroke ctrl_n = KeyStroke.getKeyStroke(KeyEvent.VK_N,Event.CTRL_MASK);
		KeyStroke ctrl_m = KeyStroke.getKeyStroke(KeyEvent.VK_M,Event.CTRL_MASK);
		KeyStroke ctrl_e = KeyStroke.getKeyStroke(KeyEvent.VK_E,Event.CTRL_MASK);
		KeyStroke ctrl_u = KeyStroke.getKeyStroke(KeyEvent.VK_U,Event.CTRL_MASK);
		KeyStroke ctrl_s = KeyStroke.getKeyStroke(KeyEvent.VK_S,Event.CTRL_MASK);
		
		mapa.put(ctrl_n, "ctrl+n");
		mapaAccion.put("ctrl+n", new AbstractAction(){
			/**
			 * 
			 */
			private static final long serialVersionUID = 2748391871400302992L;

			@Override
			public void actionPerformed(ActionEvent arg0) {
				nuevoProducto.doClick();
			}
		});
		mapa.put(ctrl_m, "ctrl+m");
		mapaAccion.put("ctrl+m", new AbstractAction(){
			/**
			 * 
			 */
			private static final long serialVersionUID = -5445845279102067449L;

			@Override
			public void actionPerformed(ActionEvent arg0) {
				modificarProducto.doClick();
			}
		});
		mapa.put(ctrl_e, "ctrl+e");
		mapaAccion.put("ctrl+e", new AbstractAction(){
			/**
			 * 
			 */
			private static final long serialVersionUID = 2664329258891855593L;

			@Override
			public void actionPerformed(ActionEvent arg0) {
				eliminarProducto.doClick();
			}
		});
		mapa.put(ctrl_u, "ctrl+u");
		mapaAccion.put("ctrl+u", new AbstractAction(){
			/**
			 * 
			 */
			private static final long serialVersionUID = 3274315916277601972L;

			@Override
			public void actionPerformed(ActionEvent arg0) {
				actualizaPrecios.doClick();
			}
		});
		mapa.put(ctrl_s, "ctrl+s");
		mapaAccion.put("ctrl+s", new AbstractAction(){
			/**
			 * 
			 */
			private static final long serialVersionUID = 7513634186532482939L;

			@Override
			public void actionPerformed(ActionEvent arg0) {
				actualizaStock.doClick();
			}
		});
		
		Runnable r = new ActualizarModelo(tablaProductos,this,"stock");
		Thread t = new Thread(r);
		t.start();
		
	}

	//MÉTODO PARA DAR FORMATO CONDICIONAL A LAS COLUMNAS Y FILAS DE LA TABLA
	private DefaultTableCellRenderer formatoTabla(){
		
		DefaultTableCellRenderer dtcr = new DefaultTableCellRenderer(){
			
			/**
			 * 
			 */
			private static final long serialVersionUID = 5600527576513566580L;

			public Component getTableCellRendererComponent(JTable table,Object value, boolean isSelected, boolean hasFocus, int row, int column){
				
				//OBTIENE LOS VALORES DE STOCK Y STOCK MÍNIMO DE LA FILA 
				String valor_cant = table.getValueAt(row, 4).toString();
				String valor_min = table.getValueAt(row, 9).toString();
				
				//FORMATO PARA PRODUCTOS CON STOCK BAJO
				if(Double.parseDouble(valor_cant) <= Double.parseDouble(valor_min)){
					setForeground(new Color(213,106,0));
					table.setValueAt("BAJO", row, 7);
				}
				
				//FORMATO PARA PRODUCTOS CON STOCK NORMAL
				else{
					setForeground(Color.BLACK);
					table.setValueAt("NORMAL", row, 7);
					setBackground(Color.WHITE);
				}
				
				//FORMATO PARA PRODUCTOS SIN STOCK
				if(Double.parseDouble(valor_cant)==0.0){
					setForeground(Color.RED);
					table.setValueAt("AGOTADO", row, 7);
				}
				
				//CENTRA DATOS EN SUS COLUMNAS
				if(column==1){
				    setHorizontalAlignment(SwingConstants.LEFT);
				}else
					setHorizontalAlignment(SwingConstants.CENTER);
					
				
				//AÑADE MENSAJE EMERGENTE EN PRODUCTOS CON COMENTARIOS
				if(column==8 && (table.getValueAt(row, column)).equals("X")){
					this.setToolTipText("Mostrar comentarios");
				}else{
					this.setToolTipText(null);
				}
				
				//MANTIENE EL FONDO Y COLOR DE LETRA POR DEFECTO
				Color fondoactual = this.getBackground();
				Color colorfuente = this.getForeground();
				super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
				
				//FORMATO PARA LA FILA SELECCIONADA
				if(isSelected){
					setBackground(fondoactual);
					setForeground(colorfuente);
					setBorder(BorderFactory.createBevelBorder(EtchedBorder.RAISED,Color.BLACK,Color.BLACK));
					setFont(new Font(null,Font.BOLD,13));
				}
				return this;
			}
		};
		return dtcr;
	}
	
	public void darFormatoATabla(){
	
		//ESTABLECE VALORES DE ANCHO MÍNIMO DE LAS COLUMNAS
		TableColumn codigo= tablaProductos.getColumnModel().getColumn(0);
		codigo.setMinWidth(35);
		
		TableColumn descripcion= tablaProductos.getColumnModel().getColumn(1);
		descripcion.setMinWidth(300);
		
		TableColumn rubro= tablaProductos.getColumnModel().getColumn(2);
		rubro.setMinWidth(35);
		
		TableColumn precio= tablaProductos.getColumnModel().getColumn(3);
		precio.setMinWidth(50);
		
		TableColumn cantidad= tablaProductos.getColumnModel().getColumn(4);
		cantidad.setMinWidth(50);
		
		TableColumn unidad= tablaProductos.getColumnModel().getColumn(5);
		unidad.setMinWidth(70);
		
		TableColumn codprov= tablaProductos.getColumnModel().getColumn(6);
		codprov.setMinWidth(70);
		
		TableColumn estado= tablaProductos.getColumnModel().getColumn(7);
		estado.setMinWidth(70);
		
		TableColumn obs= tablaProductos.getColumnModel().getColumn(8);
		obs.setMinWidth(40);
		
		//OCULTA LA COLUMNA DE ALERTA
		TableColumn alerta = tablaProductos.getColumnModel().getColumn(9);
		alerta.setMaxWidth(0);alerta.setMinWidth(0);
		alerta.setPreferredWidth(0);
		
		tablaProductos.setDefaultRenderer(Object.class, formatoTabla());
		
		//AÑADE FUNCIONALIDAD PARA MOSTRAR COMENTARIOS
		tablaProductos.addMouseListener(new MostrarObs(tablaProductos));
		
	}
	
	//DEVUELVE LA TABLA DE PRODUCTOS
	public JTable getTablaProductos() {
		
		return tablaProductos;
	
	}

	//ACTUALIZA LA TABLA TRAS AÑADIR, MODIFICAR O ELIMINAR UN PRODUCTO
	public void actualizarTabla(Producto producto,char operacion){
		
		//SI SE AÑADIÓ UN PRODUCTO, SE AÑADE LA FILA CORRESPONDIENTE
		if(operacion=='a'){
			String comentarios="";
			
			//SI EL PRODUCTO POSEE COMENTARIOS, AÑADE UN "X" A LA COLUMNA CORRESPONDIENTE
			if(!producto.getComentarios().trim().equals("")){
				comentarios="X";
			};
			
			//CREA LA FILA Y LA AÑADE
			Object[] nuevaFila = {String.format("%05d",producto.getCodigo()),producto.getDescripcion(),producto.getCodrubro(),
					producto.getPrecio(),producto.getCantidad(),producto.getUnidad(),producto.getCodproveedor(),
					producto.getEstado(),comentarios,producto.getCant_min()};
			model.addRow(nuevaFila);
			tablaProductos.setModel(model);
		
		//SI SE ELIMINÓ UN PRODUCTO, SE ELIMINA LA FILA CORRESPONDIENTE
		}else if(operacion=='c'){
			int seleccionado=tablaProductos.convertRowIndexToModel(tablaProductos.getSelectedRow());
			model.removeRow(seleccionado);
			tablaProductos.setModel(model);
		}

		//SI SE MODIFICÓ UN PRODUCTO, SE MODIFICA LA FILA CORRESPONDIENTE
		else if(operacion=='m'){
			int seleccionado=tablaProductos.convertRowIndexToModel(tablaProductos.getSelectedRow());
			
			//SE ESTABLECE LOS VALORES DE LAS COLUMNAS
			model.setValueAt(producto.getDescripcion(), seleccionado, 1);
			model.setValueAt(producto.getCodrubro(), seleccionado, 2);
			model.setValueAt(producto.getPrecio(), seleccionado, 3);
			model.setValueAt(producto.getCantidad(), seleccionado, 4);
			model.setValueAt(producto.getUnidad(), seleccionado, 5);
			model.setValueAt(producto.getCodproveedor(), seleccionado, 6);
			model.setValueAt(producto.getEstado(),seleccionado, 7);
			
			//SI EL PRODUCTO POSEE COMENTARIOS, SE AÑADE UNA "X" A LA COLUMNA CORRESPONDIENTE
			String marca_coment = "";
			if(!producto.getComentarios().equals("")){
				marca_coment="X";
			}			
			
			model.setValueAt(marca_coment, seleccionado, 8);
			model.setValueAt(producto.getCant_min(), seleccionado, 9);
			//tablaProductos.setModel(model);
		}
		
		//SI SE ACTUALIZÓ LOS PRECIOS DE TODOS DE LOS PRODUCTOS, SE ACTUALIZA LA TABLA
		else if(operacion=='u'){
			CrearTabla paraProductos = new CrearTabla();
			model=paraProductos.obtenerTablaProductos();
			tablaProductos.setModel(model);
			paraVenta.busqueda.tablaProductos.setModel(model);
			paraVenta.busqueda.darFormatoATabla();
			darFormatoATabla();
		}
	}
	
	public JButton nuevoProducto,eliminarProducto,modificarProducto,actualizaPrecios,actualizaStock;
	public JTextField paraBusqueda;
	DefaultTableModel model;
	public JTable tablaProductos;
	PanelVentas paraVenta;
}

class GenerarReporteInventario implements ActionListener{

	@Override
	public void actionPerformed(ActionEvent e) {
		new VentanaReporteInventario();
	}
}