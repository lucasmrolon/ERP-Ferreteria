package vista.ventas;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

import controlador.ActualizarModelo;
import controlador.FiltrarTabla;
import controlador.stock.MostrarObs;

//PANEL PARA REALIZAR LA BÚSQUEDA DE PRODUCTOS
public class PanelBusqueda extends JPanel{
	
	public PanelBusqueda(DefaultTableModel contenidoTabla,PanelListaVenta venta_actual){
	
		//CARACTERÍSTICAS GENERALES DEL PANEL
		setLayout(null);
		setBackground(new Color(224,224,248));
		
		//ESTABLECE EL FORMATO DE LA TABLA
		tablaProductos=new JTable(contenidoTabla);
		darFormatoATabla();
		
		//ESTABLECE LOS EVENTOS DE LA TABLA
		tablaProductos.setRowSorter(new TableRowSorter<TableModel>(contenidoTabla));
		tablaProductos.addKeyListener(new KeyAdapter(){
			public void keyPressed(KeyEvent e){
				//SI PRESIONA LA TECLA ENTER, ABRE VENTANA DE SELECCIÓN DE CANTIDAD
				if(e.isControlDown() && e.getKeyCode()==KeyEvent.VK_SPACE){
					VentanaSeleccionCantidad nuevaVentana = new VentanaSeleccionCantidad(tablaProductos,venta_actual);
					nuevaVentana.setVisible(true);	
				}
			}
		});
		tablaProductos.addMouseListener(new MouseAdapter(){
			public void mousePressed(MouseEvent e){
				//SI HACE DOBLE CLIC EN UNA FILA, ABRE VENTANA DE SELECCIÓN DE CANTIDAD
				if(e.getClickCount()==2){		
					VentanaSeleccionCantidad nuevaVentana = new VentanaSeleccionCantidad(tablaProductos,venta_actual);
					nuevaVentana.setVisible(true);	
				}
			}
		});
		tablaProductos.addMouseListener(new MostrarObs(tablaProductos));
		
		//CUADRO DE BÚSQUEDA
		JLabel busqueda= new JLabel("<html><font size=4>BUSQUEDA:</font></html>");
		busqueda.setBounds(30,20,150,30);
		add(busqueda);
		paraBusqueda = new JTextField(30);
		paraBusqueda.setBounds(50,50,150,30);
		paraBusqueda.addKeyListener(new FiltrarTabla(tablaProductos));
		paraBusqueda.addKeyListener(new KeyAdapter(){	
			public void keyPressed(KeyEvent e){
				//SI PRESIONA LA TECLA ENTER, ABRE VENTANA DE SELECCIÓN DE CANTIDAD
				if(e.isControlDown() && e.getKeyCode()==KeyEvent.VK_SPACE){
					VentanaSeleccionCantidad nuevaVentana = new VentanaSeleccionCantidad(tablaProductos,venta_actual);
					nuevaVentana.setVisible(true);	
				}
			}
		});
		add(paraBusqueda);
		
		//CONTENEDOR DE LA TABLA
		contenedor = new JScrollPane(tablaProductos);
		contenedor.setSize(900,250);
		contenedor.setPreferredSize(new Dimension(600,250));
		contenedor.setLocation(250,25);
		add(contenedor);
		
		Runnable r = new ActualizarModelo(tablaProductos,this,"productos");
		Thread t = new Thread(r);
		t.start();		
		
	}
	
	public void darFormatoATabla(){
		TableColumnModel modeloColumnas = tablaProductos.getColumnModel();
		DefaultTableCellRenderer tcr = new DefaultTableCellRenderer();
		tcr.setHorizontalAlignment(SwingConstants.CENTER);
		tablaProductos.setRowHeight(20);
		
		//ESTABLECE ANCHO MÁXIMO DE LAS COLUMNAS
		TableColumn codigo= modeloColumnas.getColumn(0);
		codigo.setMaxWidth(70);
		codigo.setCellRenderer(tcr);
		
		TableColumn precio= modeloColumnas.getColumn(3);
		precio.setMaxWidth(70);
		precio.setCellRenderer(tcr);
		
		TableColumn cantidad= modeloColumnas.getColumn(4);
		cantidad.setMaxWidth(70);
		cantidad.setCellRenderer(tcr);
		
		TableColumn unidad= modeloColumnas.getColumn(5);
		unidad.setMaxWidth(130);
		unidad.setCellRenderer(tcr);
		
		TableColumn obs= modeloColumnas.getColumn(8);
		obs.setMaxWidth(70);
		obs.setCellRenderer(tcr);
		
		//OCULTA LAS COLUMNAS DE ESTADO, PROVEEDOR Y RUBRO
		
		TableColumn alerta = modeloColumnas.getColumn(9);
		tablaProductos.removeColumn(alerta);
		
		TableColumn estado = modeloColumnas.getColumn(7);
		tablaProductos.removeColumn(estado);
		
		TableColumn codprov = modeloColumnas.getColumn(6);
		tablaProductos.removeColumn(codprov);
		
		TableColumn rubro = modeloColumnas.getColumn(2);
		tablaProductos.removeColumn(rubro);
		
	}

	public JTextField paraBusqueda;
	JScrollPane contenedor;
	public JTable tablaProductos;
	DefaultTableModel modelo;
}
