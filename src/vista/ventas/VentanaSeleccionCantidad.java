package vista.ventas;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.*;
import java.text.DecimalFormat;

import javax.swing.*;
import javax.swing.event.*;
import javax.swing.table.DefaultTableModel;

import controlador.ventas.SeleccionarProducto;

//VENTANA PARA SELECCIONAR LA CANTIDAD DEL PRODUCTO A AÑADIR A LA VENTA
public class VentanaSeleccionCantidad extends JDialog{

	public VentanaSeleccionCantidad(JTable tablaProductos, PanelListaVenta venta_actual){

		//ESTABLECE CARACTERÍSTICAS DE LA VENTANA
		
		setModal(true);
	    setSize(295, 240);
	    setLayout(null);
	    setUndecorated(true);
	    setLocationRelativeTo(tablaProductos);
	    getContentPane().setBackground(new Color(225,240,240));
	    getRootPane().setBorder(BorderFactory.createLineBorder(Color.BLACK));
	    
	    
	    JPanel paraTitulo = new JPanel();
	    paraTitulo.setLayout(null);
	    
	    JLabel titulo = new JLabel("<html><font size=4>AÑADIR ÍTEM</font></html>",JLabel.CENTER);
	    titulo.setBounds(0,0,295, 30);
	    paraTitulo.add(titulo);
	    paraTitulo.setBackground(new Color(191,191,240));
	    paraTitulo.setSize(295, 30);
	    add(paraTitulo);
		
	    //MENSAJE DE LA VENTANA
		JLabel mensaje = new JLabel("<html><font size=5>Cantidad:</font></html>");
		mensaje.setBounds(40, 60, 280, 30);
		add(mensaje);
		
		//OBTIENE EL PRODUCTO SELECCIONADO
		int seleccionado = tablaProductos.getSelectedRow();
		int seleccionado_real = tablaProductos.convertRowIndexToModel(seleccionado);
		DefaultTableModel modelo = (DefaultTableModel)tablaProductos.getModel();
		int cod = Integer.parseInt((String)modelo.getValueAt(seleccionado_real, 0));
		precio = (double)modelo.getValueAt(seleccionado_real, 3);
		double stock = (double)modelo.getValueAt(seleccionado_real, 4);
		String unidad = (String)modelo.getValueAt(seleccionado_real, 5);
		
		//ESTABLECE CARACTERÍSTICAS DEL SPINNER DE SELECCIÓN
		double salto,minimo,valor;
		if(unidad.equals("KILOGRAMOS")){
			salto=0.1;
			minimo=0.1;
			valor=0.1;
		}else{ 
			salto=1;
			minimo=1;
			valor=1;
		}
		paraCantidad = new JSpinner(new SpinnerNumberModel(valor,minimo,stock,salto));
		paraCantidad.setFont(new Font(Font.DIALOG,Font.BOLD,20));
		paraCantidad.setBounds(140,52,100,40);
		paraCantidad.addChangeListener(new CalculaSubtotal());
		
		JLabel muestraSub = new JLabel("<html><font size=4>Subtotal:</font></html>");
		muestraSub.setBounds(40, 110, 280, 30);
		add(muestraSub);
		
		formatea = new DecimalFormat("###,###.00");
		
		//MUESTRA EL VALOR EN PESOS QUE SE AÑADIRÁ A LA VENTA
		paraSubtotal=new JLabel("$ " + formatea.format(precio));
		paraSubtotal.setFont(new Font(Font.DIALOG,Font.BOLD,17));
		paraSubtotal.setBounds(132, 110, 108, 30);
		paraSubtotal.setHorizontalAlignment(SwingConstants.RIGHT);
		add(paraSubtotal);
		
		//BOTÓN PARA AÑADIR EL PRODUCTO A LA LISTA DE VENTA
		aniadirACarrito = new JButton("Añadir a carrito");
		aniadirACarrito.setBounds(70, 170, 140, 30);
		aniadirACarrito.addActionListener(new SeleccionarProducto(this,cod,paraCantidad,venta_actual,modelo,seleccionado_real));
		add(aniadirACarrito);
		((JSpinner.DefaultEditor)paraCantidad.getEditor()).getTextField().addKeyListener(new KeyAdapter(){
			public void keyTyped(KeyEvent e){
				if(e.getKeyChar()==KeyEvent.VK_ENTER){
					aniadirACarrito.doClick();
				}
			}
		});
		add(paraCantidad);
		
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
	}

	//CALCULA EL MONTO EN PESOS A AÑADIR A LA VENTA
	private class CalculaSubtotal implements ChangeListener{
		@Override
		public void stateChanged(ChangeEvent e) {
			double cantidad=(double)paraCantidad.getValue();
			double subtotal = Math.round(cantidad*precio*100d)/100d;
			paraSubtotal.setText("$ " +  formatea.format(subtotal));
		}	
	}
	
	DecimalFormat formatea;
	JButton aniadirACarrito;
	JSpinner paraCantidad;
	JLabel paraSubtotal;
	double precio;
}
