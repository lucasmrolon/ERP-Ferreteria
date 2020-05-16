package vista.compras;

import java.awt.*;
import java.awt.event.*;
import java.text.SimpleDateFormat;
import java.util.*;

import javax.swing.*;
import javax.swing.table.*;

import controlador.compras.ConsultarDatosDePedidos;
import modelo.consultas.EjecutaConsultasProveedor;

//VENTANA QUE MUESTRA PEDIDOS PENDIENTES DE RECEPCION ASOCIADOS AL PROVEEDOR SELECCIONADO
public class VerPedidos extends JDialog{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -6169543081977348187L;

	public VerPedidos(JTable tabla,String usuario){
		setModal(true);
		setSize(1000,600);
		setLocationRelativeTo(null);	
		setUndecorated(true);
		getRootPane().setBorder(BorderFactory.createLineBorder(Color.BLACK));
		
		String nom_proveedor = (String)tabla.getValueAt(tabla.getSelectedRow(), 1);
		
		new ConsultarDatosDePedidos();
		
		add(new PanelSuperior(nom_proveedor),BorderLayout.NORTH);
		add(new PanelEdicion(nom_proveedor,this,usuario),BorderLayout.CENTER);
		setVisible(true);
	}
}

//PANEL QUE MUESTRA NOMBRE DEL PROVEEDOR
class PanelSuperior extends JPanel{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 4221074163265613178L;

	public PanelSuperior(String nom_proveedor){
		
		setLayout(null);
		setBackground(new Color(160,160,233));
		setPreferredSize(new Dimension(1000,70));
		
		JLabel proveedor = new JLabel("<html><font size=5>PROVEEDOR: <strong>"+ nom_proveedor  +"</strong></font></html>",JLabel.CENTER);
		proveedor.setSize(1000,20);
		proveedor.setLocation(10,25);
		add(proveedor);
		
	}
}

//PANEL QUE MUESTRA LA LISTA DE PEDIDOS PENDIENTES
class PanelEdicion extends JPanel{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -1302370621614328414L;
	public PanelEdicion(String nombre_proveedor,VerPedidos ventana,String usuario){
		
		ventana.setTitle("Pedidos Realizados al Proveedor aun No Recibidos");
		setBackground(new Color(191,191,240));
		setLayout(null);
		PanelEdicion panel = this;
		
		this.nomb_proveedor = nombre_proveedor;
		JLabel mensaje = new JLabel("<html><font size=4>Pedidos realizados al proveedor seleccionado que aún no han sido recibidos:</font>"
				+ "<br><font size=3>Para una vista detallada seleccione un pedido y presione la barra espaciadora.</font></html>");
		mensaje.setBounds(10, 10, 780, 50);
		add(mensaje);
		
		String[] columnas = {"PEDIDO N°","FECHA CREACIÓN","FECHA RECEPCIÓN","MONTO","RECIBIDO POR"};
		
		EjecutaConsultasProveedor paraObtenerPedidos = new EjecutaConsultasProveedor();
		String cod_prov = paraObtenerPedidos.consultaCodProveedor(nomb_proveedor);
		ArrayList<Object[]> pedidos = paraObtenerPedidos.obtenerPedidosSinRecibir(cod_prov);
		
		DefaultTableModel modelopedido = new DefaultTableModel(null,columnas){
			/**
			 * 
			 */
			private static final long serialVersionUID = 5600438897153362818L;

			public boolean isCellEditable(int rowIndex,int columnIndex){
				return false;
			}
		};
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yy hh:mm a");
		for(Object[] pedido: pedidos){
			pedido[0]=String.format("%05d", pedido[0]);
			pedido[1]=sdf.format((Date)pedido[1]);
			modelopedido.addRow(pedido);
		}
		tablaPedidos = new JTable(modelopedido);
		darFormatoATabla();
		JScrollPane paraTabla = new JScrollPane(tablaPedidos);
		paraTabla.setBounds(200, 70, 600, tablaPedidos.getRowHeight()*5+23);
		add(paraTabla);
		
		
		
		//CARGA EL PANEL DE DETALLE DE PEDIDO
		tablaPedidos.addKeyListener(new KeyAdapter(){
			public void keyTyped(KeyEvent e){
				if(e.getKeyChar()==KeyEvent.VK_SPACE){
					
					int cod_pedido = Integer.parseInt((String)tablaPedidos.getValueAt(tablaPedidos.getSelectedRow(), 0));
					String fecha_creacion = (String)tablaPedidos.getValueAt(tablaPedidos.getSelectedRow(), 1);
					
					JPanel panel_padre = (JPanel)panel.getParent();
					panel_padre.remove(panel);
					MuestraDetallePedido panelDetalle = new MuestraDetallePedido(cod_pedido,nomb_proveedor,fecha_creacion,ventana,usuario);
					panel_padre.add(panelDetalle);
					panel_padre.repaint();
					panel_padre.validate();
				}
			}
		});
		
		//AÑADE DETECCION DE DOBLE CLIC PARA HABILITAR LA EDICION DE LAS LINEAS DE PEDIDO
		tablaPedidos.addMouseListener(new MouseAdapter(){
			public void mouseClicked(MouseEvent e){
				if(e.getClickCount()==2){
					
					int cod_pedido = Integer.parseInt((String)tablaPedidos.getValueAt(tablaPedidos.getSelectedRow(), 0));
					String fecha_creacion = (String)tablaPedidos.getValueAt(tablaPedidos.getSelectedRow(), 1);
					
					JPanel panel_padre = (JPanel)panel.getParent();
					panel_padre.remove(panel);
					MuestraDetallePedido panelDetalle = new MuestraDetallePedido(cod_pedido,nomb_proveedor,fecha_creacion,ventana,usuario);
					panel_padre.add(panelDetalle);
					panel_padre.repaint();
					panel_padre.validate();
				}
			}
		});
		
		JLabel mensaje2 = new JLabel("<html><font size=4>Productos pendientes de recepción:</font>"
				+ "<br><font size=3>Productos incluidos en un pedido cuya entrega quedó pendiente.</font></html>");
		mensaje2.setBounds(10, 275, 780, 50);
		add(mensaje2);
		
		//CREA LA TABLA DE PEDIDOS
		String[] columnasTablaPendientes = {"PEDIDO N°","FECHA PEDIDO","LÍNEA N°","DESCRIPCIÓN","PRECIO U.","CANTIDAD","SUBTOTAL"};
		
		DefaultTableModel modelopendientes = new DefaultTableModel(null,columnasTablaPendientes){
			/**
			 * 
			 */
			private static final long serialVersionUID = -1043320147402118894L;

			public boolean isCellEditable(int rowIndex,int columnIndex){
				return false;
			}
		};
		ArrayList<Object[]> listaProductos = paraObtenerPedidos.obtenerProductosPendientes(cod_prov);
		SimpleDateFormat sdf2 = new SimpleDateFormat("dd/MM/yyyy");
		for(Object[] linea: listaProductos){
			linea[0]=String.format("%05d", linea[0]);
			linea[1]=sdf2.format(linea[1]);
			linea[2]=String.format("%06d", linea[2]);
			modelopendientes.addRow(linea);
		}
		
		tablaPendientes = new JTable(modelopendientes);
		tablaPendientes.addMouseListener(new RecibirProductos());
		darFormatoATablaPendientes();
		JScrollPane paraTablaPendientes = new JScrollPane(tablaPendientes);
		paraTablaPendientes.setBounds(85, 325, 830, tablaPendientes.getRowHeight()*5+23);
		add(paraTablaPendientes);
		
		ActionMap mapaAccion = this.getActionMap();
		InputMap mapa = this.getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
		
		KeyStroke escape = KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE,0);		
		
		mapa.put(escape,"accion escape");
		mapaAccion.put("accion escape", new AbstractAction(){
			/**
			 * 
			 */
			private static final long serialVersionUID = -290900816092790508L;

			public void actionPerformed(ActionEvent e){
				ventana.dispose();
			}
		});
	
		setVisible(true);
		
	}
	
	//METODO QUE HABILITA LA EDICION DE LAS LINEAS DE PEDIDO
	private class RecibirProductos extends MouseAdapter{

		@Override
		public void mousePressed(MouseEvent e) {
			if(e.getClickCount()==2){
				int fila_seleccionada = tablaPendientes.rowAtPoint(e.getPoint());
				String n_pedido = (String)tablaPendientes.getValueAt(fila_seleccionada, 0);

				new EditarRecibido(tablaPendientes,fila_seleccionada,nomb_proveedor,n_pedido,1);
			}
		}
	}
	//DA FORMATO A LA TABLA
	public void darFormatoATabla(){
		tablaPedidos.setRowHeight(30);
		DefaultTableCellRenderer tcr = new DefaultTableCellRenderer();
		tcr.setHorizontalAlignment(SwingConstants.CENTER);
		TableColumnModel modeloColumnas = tablaPedidos.getColumnModel();
		TableColumn n_pedido = modeloColumnas.getColumn(0);
		n_pedido.setMinWidth(80);
		TableColumn creacion = modeloColumnas.getColumn(1);
		creacion.setMinWidth(140);
		TableColumn recepcion = modeloColumnas.getColumn(2);
		recepcion.setMinWidth(140);
		TableColumn monto = modeloColumnas.getColumn(3);
		monto.setMinWidth(100);
		TableColumn empleado = modeloColumnas.getColumn(4);
		empleado.setMinWidth(140);
		
		for(int i=0;i<5;i++){
			modeloColumnas.getColumn(i).setCellRenderer(tcr);
		}
	}
	
	public void darFormatoATablaPendientes(){
		tablaPendientes.setRowHeight(20);
		DefaultTableCellRenderer tcr = new DefaultTableCellRenderer();
		tcr.setHorizontalAlignment(SwingConstants.CENTER);
		TableColumnModel modeloColumnas = tablaPendientes.getColumnModel();

		modeloColumnas.getColumn(0).setMaxWidth(80);
		modeloColumnas.getColumn(0).setCellRenderer(tcr);
		modeloColumnas.getColumn(1).setMaxWidth(100);
		modeloColumnas.getColumn(1).setCellRenderer(tcr);
		modeloColumnas.getColumn(2).setMaxWidth(80);
		modeloColumnas.getColumn(2).setCellRenderer(tcr);
		modeloColumnas.getColumn(3).setMaxWidth(430);
		modeloColumnas.getColumn(4).setMaxWidth(80);
		modeloColumnas.getColumn(4).setCellRenderer(tcr);
		modeloColumnas.getColumn(5).setMaxWidth(80);
		modeloColumnas.getColumn(5).setCellRenderer(tcr);
		modeloColumnas.getColumn(6).setMaxWidth(80);
		modeloColumnas.getColumn(6).setCellRenderer(tcr);
	}
	JTable tablaPedidos, tablaPendientes;
	String nomb_proveedor;
}
