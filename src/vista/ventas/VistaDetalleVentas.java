package vista.ventas;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import javax.swing.AbstractAction;
import javax.swing.ActionMap;
import javax.swing.BorderFactory;
import javax.swing.InputMap;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTree;
import javax.swing.KeyStroke;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;

import com.toedter.calendar.JDateChooser;

import controlador.stock.DevolverProducto;
import controlador.ventas.ObtenerVentasYLineasVenta;
import modelo.consultas.EjecutaConsultas;
import modelo.consultas.EjecutaConsultasVenta;
import modelo.objetos.FormaDePago;
import modelo.objetos.LineaVenta;
import modelo.objetos.Producto;
import modelo.objetos.Venta;

public class VistaDetalleVentas extends JDialog{
	
	public VistaDetalleVentas(DefaultTableModel tablaProductos){
		
		setSize(700,470);
		setModal(true);
		setUndecorated(true);
		getRootPane().setBorder(BorderFactory.createLineBorder(Color.BLACK));
		setLocationRelativeTo(null);
		
		Date hoy = new Date();
		Calendar calendario = Calendar.getInstance();
		calendario.setTime(hoy);
		calendario.add(Calendar.DAY_OF_YEAR, 1);
		hoy = calendario.getTime();
		calendario.add(Calendar.DAY_OF_YEAR, -8);
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		
		ventas = obtenerVentas(calendario.getTime(), hoy);
		
		arbol_fechas = new JTree();
		
		arbol_fechas.setFont(new Font(Font.DIALOG,Font.PLAIN,14));
		
		DefaultTreeModel modelo = generarModelo(ventas);
		
		arbol_fechas.setModel(modelo);
		arbol_fechas.expandRow(0);
		arbol_fechas.addMouseListener(new MouseAdapter(){
			public void mousePressed(MouseEvent e){
				if(e.getClickCount()==2){
					TreePath seleccionado = arbol_fechas.getPathForLocation(e.getX(), e.getY());
					DefaultMutableTreeNode nodo = (DefaultMutableTreeNode)seleccionado.getLastPathComponent();
					LineaVenta linea = (LineaVenta)nodo.getUserObject();
					new MuestraLineaVenta(linea,tablaProductos);
				}
			}
		});
		
		arbol_fechas.addFocusListener(new FocusListener(){

			@Override
			public void focusGained(FocusEvent arg0) {
				Date hasta = antes_de.getDate();
				Date hoy = new Date();
				Calendar calendario2 = Calendar.getInstance();
				calendario2.setTime(hasta);
				int seleccionado = calendario2.get(Calendar.DAY_OF_YEAR);
				calendario2.setTime(hoy);
				int dia_actual = calendario2.get(Calendar.DAY_OF_YEAR);
				//if(seleccionado==dia_actual){
				calendario2.add(Calendar.DAY_OF_YEAR, 1);
					hasta = calendario2.getTime();
			//	}
				ventas = obtenerVentas(despues_de.getDate(),hasta);
				arbol_fechas.setModel(generarModelo(ventas));
			}

			@Override
			public void focusLost(FocusEvent arg0) {
				
			}
		});
		
		JScrollPane paraArbol = new JScrollPane(arbol_fechas);
		add(paraArbol);
		
		JPanel paraFechas = new JPanel(new FlowLayout());
		paraFechas.setPreferredSize(new Dimension(700,50));
		despues_de = new JDateChooser(calendario.getTime());
		despues_de.setPreferredSize(new Dimension(100,30));
		antes_de = new JDateChooser(new Date());
		antes_de.setPreferredSize(new Dimension(100,30));
		paraFechas.setBackground(new Color(225,240,240));
		paraFechas.add(new JLabel("Desde: "));
		paraFechas.add(despues_de);
		paraFechas.add(new JLabel("Hasta: "));
		paraFechas.add(antes_de);
		
		
		JButton buscar = new JButton("Buscar");
		buscar.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				
				Date despues = despues_de.getDate();
				Date antes = antes_de.getDate();
				calendario.setTime(antes);
				calendario.add(Calendar.DAY_OF_YEAR, 1);
				antes=calendario.getTime();
				ventas = obtenerVentas(despues,antes);
				arbol_fechas.setModel(generarModelo(ventas));
			}
			
		});
		
		paraFechas.add(buscar);
		
		add(paraFechas,BorderLayout.NORTH);
		
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
		
		arbol_fechas.grabFocus();
		
		
		
	}
	
	private ArrayList<Venta> obtenerVentas(Date despues_de,Date antes_de){
		
		ObtenerVentasYLineasVenta paraObtenerDatos = new ObtenerVentasYLineasVenta();
		return paraObtenerDatos.obtenerVentas(despues_de, antes_de);
		
	}
	
	private DefaultTreeModel generarModelo(ArrayList<Venta> ventas){
		DefaultMutableTreeNode raiz = new DefaultMutableTreeNode("Ventas");		
		
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy  hh:mm:ss a");
		
		EjecutaConsultasVenta consulta = new EjecutaConsultasVenta();
	
		ArrayList<FormaDePago> formas = consulta.obtener_formas_de_pago();
		
		if(formas!=null){
		
			for(Venta v:ventas){
				int cod_forma_pago = v.getForma_pago();
				String forma_pago="";
				for(FormaDePago f:formas){
					if(cod_forma_pago==f.getCodigo()){
						forma_pago=f.getTipo();
					}
				}
			
				DefaultMutableTreeNode nodo = new DefaultMutableTreeNode("<html><b>Venta N° "+ v.getCodigoventa() + "</b> - " 
						+ sdf.format(v.getFecha()) + " || Monto: $ " + v.getMonto_total() + " || " + forma_pago + "</html>");

				ObtenerVentasYLineasVenta paraObtenerDatos = new ObtenerVentasYLineasVenta();
				ArrayList<LineaVenta> lineas = paraObtenerDatos.obtenerLineas(v.getCodigoventa());
				for(LineaVenta l:lineas){
					DefaultMutableTreeNode nodohijo = new DefaultMutableTreeNode(l);
					nodo.add(nodohijo);
				}
			
				raiz.add(nodo);
			}
		}
		else{
			raiz.add(new DefaultMutableTreeNode("Error al obtener las ventas. Intente cerrando la ventana y volviéndola a abrir."));
		}
		DefaultTreeModel modelo = new DefaultTreeModel(raiz);
		
		return modelo;
	}
	
	ArrayList<Venta> ventas;
	JTree arbol_fechas;
	JDateChooser despues_de,antes_de;
	

}

class MuestraLineaVenta extends JDialog{
	
	public MuestraLineaVenta(LineaVenta linea,DefaultTableModel tablaProductos){
		
		setLayout(null);
		setSize(800,200);
		setLocationRelativeTo(null);
		setModal(true);
		setUndecorated(true);
		getContentPane().setBackground(new Color(225,240,240));
		getRootPane().setBorder(BorderFactory.createLineBorder(Color.BLACK));
		
		EjecutaConsultas consulta = new EjecutaConsultas();
		Producto producto = consulta.obtieneProducto(linea.getCodProducto());
		
		if(producto!=null){
		
		
			DefaultTableModel modelo = new DefaultTableModel();
		
			//SE ESTABLECE LOS TÍTULOS DE LAS COLUMNAS
			String[] columnas = {"LINEA","PRODUCTO","PRECIO U.","CANTIDAD","SUBTOTAL","REPARTE"};
		
			//SE BLOQUEA LA EDICIÓN DE CELDAS DE LA TABLA
			modelo=new DefaultTableModel(null,columnas){
				public boolean isCellEditable(int rowIndex,int columnIndex){
					return false;
				}
			};
			String reparte;
			if(linea.isReparte()) reparte = "SI";
			else reparte = "NO";
			Object[] fila = {linea.getCodigo(),String.format("%05d",linea.getCodProducto()) + "-" + producto.getDescripcion(), 
					linea.getPrecio_u(),linea.getCantidad(), linea.getSubtotal(), reparte};
			modelo.addRow(fila);
		
			tabla = new JTable(modelo);
			tabla.setEnabled(false);
			JScrollPane paraTabla = new JScrollPane(tabla);
			paraTabla.setPreferredSize(new Dimension(700,40));
			paraTabla.setBounds(50, 50, 700, 40);
			add(paraTabla);
		
			darFormatoATabla();
		
			int cod_venta = linea.getCodVenta();
			EjecutaConsultasVenta saber_forma_pago = new EjecutaConsultasVenta();
			Venta venta = saber_forma_pago.consultaVenta(cod_venta);
			if(venta!=null){
				aceptar = new JButton("Aceptar");
				aceptar.setPreferredSize(new Dimension(100,30));
				aceptar.addActionListener(new ActionListener(){
					@Override
					public void actionPerformed(ActionEvent arg0) {
						dispose();	
					}
				});
				
				int cod_forma_pago = venta.getForma_pago();
				if(cod_forma_pago==1){
					devolver_producto = new JButton("Registrar devolución");
					devolver_producto.setPreferredSize(new Dimension(200,30));
					devolver_producto.setBounds(240, 100, 200, 30);
					aceptar.setBounds(460, 100, 100, 30);
					devolver_producto.addActionListener(new DevolverProducto(linea,tabla,tablaProductos));
					add(devolver_producto);
				}else{
					aceptar.setBounds(350, 100, 100, 30);
				}
				add(aceptar);
				setVisible(true);
			}else{
				JOptionPane.showMessageDialog(null,
						"<html><font size=4>¡Error al obtener la linea de venta! Intente nuevamente</font></html>",
						"¡Error!", JOptionPane.WARNING_MESSAGE);
			}
		}else{
			JOptionPane.showMessageDialog(null,"<html><font size=4>¡Error al obtener la linea de venta! Intente nuevamente.</font></html>",
					"Eliminar Línea", JOptionPane.WARNING_MESSAGE);
		}
		
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
				aceptar.doClick();
			}
		});
		
		
	}
	
	public void darFormatoATabla(){
		
		TableColumnModel modeloColumnas = tabla.getColumnModel();
		modeloColumnas.getColumn(0).setMaxWidth(60);
		modeloColumnas.getColumn(2).setMaxWidth(80);
		modeloColumnas.getColumn(3).setMaxWidth(80);
		modeloColumnas.getColumn(4).setMaxWidth(90);
		modeloColumnas.getColumn(5).setMaxWidth(90);
		DefaultTableCellRenderer renderer = new DefaultTableCellRenderer();
		renderer.setHorizontalAlignment(SwingConstants.CENTER);
		for(int i=0;i<=5;i++){
			if(i!=1){
				modeloColumnas.getColumn(i).setCellRenderer(renderer);
			}
		}
	}
	
	JTable tabla;
	JButton aceptar,devolver_producto;
}


