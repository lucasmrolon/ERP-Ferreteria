package vista.compras;

import java.awt.Color;
import java.awt.event.*;

import javax.swing.*;
import javax.swing.table.*;

import controlador.CrearTabla;

//VENTANA QUE MUESTRA PRODUCTOS PENDIENTES DE RECEPCION
public class VerProductosPendientesDeRecepcion extends JDialog{

	private static final long serialVersionUID = -1383637706135410053L;

	public VerProductosPendientesDeRecepcion(String usuario){
		
		setLayout(null);
		this.getContentPane().setBackground(new Color(224,224,248));
		setSize(1200,600);
		setLocationRelativeTo(null);
		setUndecorated(true);
		getRootPane().setBorder(BorderFactory.createLineBorder(Color.BLACK));
		setTitle("Productos Pendientes de Recepción");
		
		//MUESTRA MENSAJE DESCRIPTIVO
		JLabel mensaje = new JLabel("<html><font size=4>Para confirmar la recepción de un producto atrasado, haga doble clic sobre el mismo."
				+ "</font></size>");
		mensaje.setBounds(60, 30, 1080, 30);
		add(mensaje);
		
		//CREA TABLA
		CrearTabla paraObtenerListaPendientes = new CrearTabla();
		
		DefaultTableModel modelo = paraObtenerListaPendientes.obtenerProductosPendientesDeRecepcion();
		
		muestraProductos = new JTable(modelo);
		formatearTabla();
		JScrollPane paraTabla = new JScrollPane(muestraProductos);
		paraTabla.setBounds(60, 90, 1080, muestraProductos.getRowHeight()*15+23);
		add(paraTabla);
		setVisible(true);
		
		muestraProductos.addMouseListener(new RecibirProductos());
		
		//ESTABLECE ATAJOS DE TECLADO
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
	
	//DA FORMATO A LA TABLA
	public void formatearTabla(){
		
		muestraProductos.setRowHeight(25);
		DefaultTableCellRenderer tcr = new DefaultTableCellRenderer();
		tcr.setHorizontalAlignment(SwingConstants.CENTER);
		TableColumnModel modeloColumnas = muestraProductos.getColumnModel();
		
		modeloColumnas.getColumn(0).setMaxWidth(120);
		modeloColumnas.getColumn(1).setMaxWidth(80);
		modeloColumnas.getColumn(1).setCellRenderer(tcr);
		modeloColumnas.getColumn(2).setMaxWidth(130);
		modeloColumnas.getColumn(2).setCellRenderer(tcr);
		modeloColumnas.getColumn(3).setMaxWidth(80);
		modeloColumnas.getColumn(3).setCellRenderer(tcr);
		modeloColumnas.getColumn(4).setMaxWidth(430);
		modeloColumnas.getColumn(5).setMaxWidth(80);
		modeloColumnas.getColumn(5).setCellRenderer(tcr);
		modeloColumnas.getColumn(6).setMaxWidth(80);
		modeloColumnas.getColumn(6).setCellRenderer(tcr);
		modeloColumnas.getColumn(7).setMaxWidth(80);
		modeloColumnas.getColumn(7).setCellRenderer(tcr);	
		
	}
	
	//HABILITA LA EDICION DE LA LINEA DE PEDIDO
	private class RecibirProductos extends MouseAdapter{

		@Override
		public void mousePressed(MouseEvent e) {
			if(e.getClickCount()==2){
				int fila_seleccionada = muestraProductos.rowAtPoint(e.getPoint());
				String nomb_proveedor = (String)muestraProductos.getValueAt(fila_seleccionada, 0);
				String n_pedido = (String)muestraProductos.getValueAt(fila_seleccionada, 1);

				new EditarRecibido(muestraProductos,fila_seleccionada,nomb_proveedor,n_pedido,2);
			}
		}
	}
	
	
	
	JTable muestraProductos;
	JTable muestraProductoSeleccionado;
}

