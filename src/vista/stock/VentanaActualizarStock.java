package vista.stock;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.*;

import controlador.stock.ActualizarStock;

//VENTANA PARA ACTUALIZAR EL STOCK DE UN PRODUCTO
public class VentanaActualizarStock extends JDialog{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -7705048624562071561L;

	public VentanaActualizarStock(JTable tablaProductos,int cod,String desc,double cantidad,String unidad){
		
		getContentPane().setBackground(new Color(225,240,240));
		setLayout(null);
		setTitle("Actualizar Stock");
		setSize(400,280);
		setLocationRelativeTo(null);
		setUndecorated(true);
	    getRootPane().setBorder(BorderFactory.createLineBorder(Color.BLACK));
		setVisible(true);
		
		//OBTIENE DATOS ACTUALES DEL PRODUCTO EN STOCK
		JLabel producto_seleccionado = new JLabel("<html><font size=5>Codigo: " + String.format("%05d",cod) + " - "+desc+"</font></html>");
		producto_seleccionado.setBounds(20, 20, 150, 50);
		producto_seleccionado.setVerticalAlignment(SwingConstants.TOP);
		add(producto_seleccionado);
		JLabel cantidad_actual = new JLabel("<html><font size=5>Cantidad actual: " + cantidad + " " + unidad + "</font></html>");
		cantidad_actual.setBounds(20, 70, 330, 20);
		add(cantidad_actual);
		
		JLabel mensaje = new JLabel("<html><font size=4>Ingrese la cantidad a añadir o quitar</font></html>",JLabel.CENTER);
		mensaje.setBounds(10, 115, 380, 20);
		add(mensaje);
		
		//CARGA EL SELECTOR DE CANTIDAD Y LO AÑADE A LA VISTA
		double salto;
		if(unidad.equals("KILOGRAMOS")){
			salto=0.1;
		}
		else{
			salto=1;
		}
		
		JSpinner paraCantidad = new JSpinner(new SpinnerNumberModel(0.0,(-1.0)*cantidad,null,salto));
		paraCantidad.setFont(new Font(Font.DIALOG,Font.BOLD,20));
		paraCantidad.setBounds(150,145,100,40);
		
		add(paraCantidad);
		paraCantidad.addKeyListener(new KeyAdapter(){
			public void keyTyped(KeyEvent e){
				if(e.getKeyCode()>KeyEvent.VK_9 || e.getKeyCode()>KeyEvent.VK_0){
					e.consume();
				}
			}
		});
		
		//BOTON PARA CONFIRMAR ACTUALIZACION DEL STOCK
		JButton actualizarStock = new JButton("Actualizar stock");
		actualizarStock.setBounds(125, 210, 150, 30);
		actualizarStock.addActionListener(new ActualizarStock(this,tablaProductos,cod,paraCantidad,cantidad));
		add(actualizarStock);
		
		//ESTABLECE CANCELAR CON TECLA ESCAPE
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

}
