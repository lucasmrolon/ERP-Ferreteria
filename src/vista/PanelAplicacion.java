package vista;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import controlador.*;
import vista.compras.PanelCompras;
import vista.detalle.PanelDetalle;
import vista.estadisticas.PanelEstadisticas;
import vista.stock.PanelStock;
import vista.ventas.PanelFacturacion;
import vista.ventas.PanelVentas;

//VENTANA PRINCIPAL DE LA APLICACIÓN
public class PanelAplicacion extends JPanel{

	/**
	 * 
	 */
	private static final long serialVersionUID = -7906940046069859769L;

	public PanelAplicacion(String usuario,String tipo){
		
		setLayout(new BorderLayout());
		
		Dimension tamanio_pantalla = Toolkit.getDefaultToolkit().getScreenSize();
		
		//CREA PANELES CABECERA Y PRINCIPAL
		PanelCabecera cabecera = new PanelCabecera();
		add(cabecera,BorderLayout.NORTH);
		
		PanelPrincipal principal = new PanelPrincipal(usuario,tipo);
		add(principal,BorderLayout.CENTER);
		
		
		PanelPie pie = new PanelPie(tamanio_pantalla,usuario);
		add(pie,BorderLayout.SOUTH);
		
		
		
		ActionMap mapaAccion = this.getActionMap();
		InputMap mapa = this.getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
		
		KeyStroke f12 = KeyStroke.getKeyStroke(KeyEvent.VK_F12,0);

		mapa.put(f12,"accion f12");
		mapaAccion.put("accion f12", new AbstractAction(){
			/**
			 * 
			 */
			private static final long serialVersionUID = -1229964756034622311L;

			public void actionPerformed(ActionEvent e){
				//CIERRA LA APLICACIÓN
				pie.cerrarSesion.doClick();
			}
		});
	}
}

//PANEL EN BLANCO DONDE SE CARGARÁN LOS PANELES SELECCIONADOS
class PanelPrincipal extends JPanel{

	/**
	 * 
	 */
	private static final long serialVersionUID = 63237027407653215L;
	public PanelPrincipal(String usuario,String tipo){
		
		setLayout(new BorderLayout());

		//CREA LA TABLA DE PRODUCTOS Y LOS ENVÍA A LOS PANELES CORRESPONDIENTES
		
		CrearTabla tablas = new CrearTabla();
		modeloTablaProductos = tablas.obtenerTablaProductos();
		modeloTablaLineasReparto =  tablas.obtenerTablaLineasReparto(0);
		modeloTablaRepartos = tablas.obtenerTablaRepartos();
		modeloTablaVentasSinFacturar = tablas.obtenerVentas();
		
		panelVentas = new PanelVentas(modeloTablaProductos,modeloTablaLineasReparto,usuario);
		panelRepartos = new PanelRepartos(modeloTablaLineasReparto,modeloTablaRepartos,usuario,tipo);
		
		if(tipo.equals("administrador")){
			panelStock = new PanelStock(modeloTablaProductos,panelVentas);
			panelDetalle = new PanelDetalle();
			panelEstadisticas = new PanelEstadisticas();
			panelFacturacion = new PanelFacturacion(modeloTablaVentasSinFacturar,modeloTablaProductos,modeloTablaLineasReparto);
			
			
		}
		
		panelCompras = new PanelCompras(usuario,panelStock);///////////////////////////////
		
		
		
		//CREA EL PANEL DE BOTONES
		if(tipo.equals("administrador")){	
			panelBotones = new PanelBotones(this,panelVentas,panelStock,panelRepartos,panelEstadisticas,
				panelDetalle,panelFacturacion,panelCompras);
		
		}
		else{
			panelBotones = new PanelBotones(this,panelVentas,panelRepartos,panelCompras);
		}
		add(panelBotones,BorderLayout.NORTH);
		
		
		
		//AÑADE EL PANEL A LA APLICACIÓN
		
		add(panelVentas,BorderLayout.CENTER);
		
		
		//ESTABLECE ATAJOS DE TECLADO PARA LOS PANELES
		ActionMap mapaAccion = this.getActionMap();
		InputMap mapa = this.getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
		
		KeyStroke f1 = KeyStroke.getKeyStroke(KeyEvent.VK_F1,0);
		KeyStroke f2 = KeyStroke.getKeyStroke(KeyEvent.VK_F2,0);
		KeyStroke f3 = KeyStroke.getKeyStroke(KeyEvent.VK_F3,0);
		KeyStroke f4 = KeyStroke.getKeyStroke(KeyEvent.VK_F4,0);
		KeyStroke f6 = KeyStroke.getKeyStroke(KeyEvent.VK_F6,0);
		KeyStroke f7 = KeyStroke.getKeyStroke(KeyEvent.VK_F7,0);
		KeyStroke f8 = KeyStroke.getKeyStroke(KeyEvent.VK_F8,0);
		
		
		mapa.put(f1,"accion f1");
		mapaAccion.put("accion f1", new AbstractAction(){
			/**
			 * 
			 */
			private static final long serialVersionUID = -2909008160592790508L;

			public void actionPerformed(ActionEvent e){
				panelBotones.aPanelStock.doClick();
			}
		});
		mapa.put(f2,"accion f2");
		mapaAccion.put("accion f2", new AbstractAction(){
			/**
			 * 
			 */
			private static final long serialVersionUID = 4753621879513549195L;

			public void actionPerformed(ActionEvent e){
				panelBotones.aPanelVentas.doClick();
			}
		});
		mapa.put(f3,"accion f3");
		mapaAccion.put("accion f3", new AbstractAction(){
			/**
			 * 
			 */
			private static final long serialVersionUID = -790248155908919406L;

			public void actionPerformed(ActionEvent e){
				panelBotones.aPanelFacturacion.doClick();
			}
		});
		mapa.put(f4,"accion f4");
		mapaAccion.put("accion f4", new AbstractAction(){
			/**
			 * 
			 */
			private static final long serialVersionUID = 4487670041248548718L;

			public void actionPerformed(ActionEvent e){
				panelBotones.aPanelRepartos.doClick();
			}
		});
		mapa.put(f6,"accion f6");
		mapaAccion.put("accion f6", new AbstractAction(){
			/**
			 * 
			 */
			private static final long serialVersionUID = -6400321878735168795L;

			public void actionPerformed(ActionEvent e){
				panelBotones.aPanelEstadisticas.doClick();
			}
		});
		mapa.put(f7,"accion f7");
		mapaAccion.put("accion f7", new AbstractAction(){
			/**
			 * 
			 */
			private static final long serialVersionUID = 5695011213997819701L;

			public void actionPerformed(ActionEvent e){
				panelBotones.aPanelDetalle.doClick();
			}
		});
		mapa.put(f8,"accion f8");
		mapaAccion.put("accion f8", new AbstractAction(){
			/**
			 * 
			 */
			private static final long serialVersionUID = 2201567719912598236L;

			public void actionPerformed(ActionEvent e){
				panelBotones.aPanelCompras.doClick();
			}
		});
		
	}
	
	DefaultTableModel modeloTablaProductos,modeloTablaLineasReparto,modeloTablaRepartos,modeloTablaVentasSinFacturar;
	PanelVentas panelVentas;
	PanelStock panelStock;
	PanelRepartos panelRepartos;
	PanelBotones panelBotones;
	PanelEstadisticas panelEstadisticas;
	PanelDetalle panelDetalle;
	PanelFacturacion panelFacturacion;
	PanelCompras panelCompras;
}

//PANEL DONDE APARECEN LOS BOTONES PARA CAMBIAR DE PANELES
class PanelBotones extends JPanel{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1584525663763867124L;
	
	//CONSTRUCTOR PARA USUARIO ADMINISTRADOR
	public PanelBotones(PanelPrincipal contenedor,PanelVentas ventas,PanelStock stock,PanelRepartos repartos, 
			PanelEstadisticas estadisticas, PanelDetalle detalle, PanelFacturacion facturacion, PanelCompras compras){
	
		setBackground(new Color(191,191,240));
		
		//CREA BOTONES Y SUS OYENTES
		CambiarPaneles cambio = new CambiarPaneles(contenedor,ventas,stock,repartos,estadisticas,detalle,facturacion,compras);
		
		//AÑADE ICONOS A LOS BOTONES
		aPanelStock = new JButton("F1 | STOCK");
		aPanelStock.setIcon(new ImageIcon(getClass().getResource("/img/icono_stock.png")));
		aPanelStock.addActionListener(cambio);
		add(aPanelStock);
		
		aPanelVentas = new JButton("F2 | VENTAS");
		aPanelVentas.setIcon(new ImageIcon(getClass().getResource("/img/icono_venta.png")));
		aPanelVentas.addActionListener(cambio);
		add(aPanelVentas);
		
		aPanelFacturacion = new JButton("F3 | FACTURACIÓN");
		aPanelFacturacion.setIcon(new ImageIcon(getClass().getResource("/img/icono_facturacion.png")));
		aPanelFacturacion.addActionListener(cambio);
		add(aPanelFacturacion);
		
		aPanelRepartos = new JButton("F4 | REPARTOS");
		aPanelRepartos.setIcon(new ImageIcon(getClass().getResource("/img/icono_reparto.png")));
		aPanelRepartos.addActionListener(cambio);
		add(aPanelRepartos);
		
		aPanelEstadisticas = new JButton("F6 | ESTADÍSTICAS");
		aPanelEstadisticas.setIcon(new ImageIcon(getClass().getResource("/img/icono_estadisticas.png")));
		aPanelEstadisticas.addActionListener(cambio);
		add(aPanelEstadisticas);
		
		aPanelDetalle = new JButton("F7 | DETALLE");
		aPanelDetalle.setIcon(new ImageIcon(getClass().getResource("/img/icono_detalle.png")));
		aPanelDetalle.addActionListener(cambio);
		add(aPanelDetalle);
		
		aPanelCompras = new JButton("F8 | COMPRAS");
		aPanelCompras.setIcon(new ImageIcon(getClass().getResource("/img/icono_compras.png")));
		aPanelCompras.addActionListener(cambio);
		add(aPanelCompras);
		
		
		
	}
	//CONSTRUCTOR PARA USUARIO NORMAL
	public PanelBotones(PanelPrincipal contenedor, PanelVentas ventas, PanelRepartos repartos, PanelCompras compras){
	
		setBackground(new Color(191,191,240));
		
		//CREA BOTONES Y SUS OYENTES
		CambiarPaneles cambio = new CambiarPaneles(contenedor,ventas,repartos,compras);
		
		aPanelVentas = new JButton("F2 | VENTAS");		
		aPanelVentas.setIcon(new ImageIcon(getClass().getResource("/img/icono_venta.png")));		
		aPanelVentas.addActionListener(cambio);
		add(aPanelVentas);
		
		aPanelRepartos = new JButton("F4 | REPARTOS");
		aPanelRepartos.setIcon(new ImageIcon(getClass().getResource("/img/icono_reparto.png")));
		aPanelRepartos.addActionListener(cambio);
		add(aPanelRepartos);
		
		aPanelCompras = new JButton("F8 | COMPRAS");
		aPanelCompras.setIcon(new ImageIcon(getClass().getResource("/img/icono_compras.png")));
		aPanelCompras.addActionListener(cambio);
		add(aPanelCompras);
		
	}

	JButton aPanelStock,aPanelVentas,aPanelRepartos,aPanelEstadisticas,aPanelDetalle,aPanelFacturacion,aPanelCompras;
	String usuario;
}

//PANEL INFERIRO QUE MUESTRA INFORMACION DE SESION Y BOTON DE CERRAR SESION
class PanelPie extends JPanel{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1082035060802621035L;
	public PanelPie(Dimension tamanio_pantalla, String usuario){
		setBackground(new Color(160,160,233));
		setPreferredSize(new Dimension(tamanio_pantalla.width,30));
		setLayout(null);
		
		//AÑADE EL BOTON DE CIERRE DE SESIÓN Y SU FUNCIONALIDAD
		cerrarSesion = new JButton("Cerrar sesión (F12)");
		cerrarSesion.addActionListener(new CerrarSesion(this,usuario));
		cerrarSesion.setBounds(tamanio_pantalla.width-160,0,150, 30);
		SimpleDateFormat sdf = new SimpleDateFormat("hh:mm a");
		JLabel infousuario = new JLabel("<html>Has iniciado sesión como <u>"+usuario+"</u> a las "+sdf.format(new Date())+"</html>");
		infousuario.setBounds(cerrarSesion.getLocation().x-300, 0, 300, 30);
		infousuario.setForeground(Color.WHITE);
		add(infousuario);
		add(cerrarSesion);			
	}
	public JButton cerrarSesion;
}