package vista.ventas;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.table.*;
import controlador.FiltrarTabla;
import controlador.stock.MostrarObs;

//PANEL DE VENTAS
public class PanelVentas extends JPanel{

	public PanelVentas(DefaultTableModel modeloTablaProductos,DefaultTableModel modeloTablaLineasReparto, String usuario){

		//SE ESTABLECE LAS CARACTERÍSTICAS GENERALES DEL PANEL
		setLayout(new GridLayout(2,1));
		
		//SE AÑADE LOS PANELES DE BÚSQUEDA Y VENTA
		lista = new PanelListaVenta(modeloTablaProductos,modeloTablaLineasReparto,usuario);
		busqueda = new PanelBusqueda(modeloTablaProductos,lista);
		add(busqueda);		
		add(lista);
		
		ActionMap mapaAccion = this.getActionMap();
		InputMap mapa = this.getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
		
		KeyStroke ctrl_enter = KeyStroke.getKeyStroke(KeyEvent.VK_ENTER,Event.CTRL_MASK);
		KeyStroke ctrl_t = KeyStroke.getKeyStroke(KeyEvent.VK_T,Event.CTRL_MASK);
		KeyStroke ctrl_e = KeyStroke.getKeyStroke(KeyEvent.VK_E,Event.CTRL_MASK);
		KeyStroke ctrl_b = KeyStroke.getKeyStroke(KeyEvent.VK_B,Event.CTRL_MASK);
		
		mapa.put(ctrl_enter,"accion ctrl+enter");
		mapaAccion.put("accion ctrl+enter", new AbstractAction(){
			public void actionPerformed(ActionEvent e){
					lista.paraRegistrarVenta.doClick();
			}
		});
		mapa.put(ctrl_b,"accion ctrl+b");
		mapaAccion.put("accion ctrl+b", new AbstractAction(){
			public void actionPerformed(ActionEvent e){
					busqueda.paraBusqueda.requestFocus();
			}
		});
		mapa.put(ctrl_t,"accion ctrl+t");
		mapaAccion.put("accion ctrl+t", new AbstractAction(){
			public void actionPerformed(ActionEvent e){
					lista.seleccionarTodo.doClick();
			}
		});
		mapa.put(ctrl_e,"accion ctrl+e");
		mapaAccion.put("accion ctrl+e", new AbstractAction(){
			public void actionPerformed(ActionEvent e){
					lista.paraEliminarLinea.doClick();
			}
		});		
	}
	public PanelBusqueda busqueda;
	public PanelListaVenta lista;
}
