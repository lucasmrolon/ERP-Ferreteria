package vista.detalle;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.*;

import controlador.detalle.AbonarCuentaCorriente;

//VENTANA PARA ABONAR UNA CUENTA CORRIENTE Y ACTUALIZAR EL ESTADO
public class VentanaAbonarCuenta extends JDialog{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -7170555799645478892L;
	public VentanaAbonarCuenta(int fila,JTable tablaResultado){
	
		setSize(300, 220);
		setLocationRelativeTo(null);
		setModal(true);
		setLayout(null);
		setTitle("Abonar Cuenta Corriente");
		getRootPane().setBorder(BorderFactory.createLineBorder(Color.BLACK));
		setUndecorated(true);
		
		mensaje = new JLabel("<html><font size=4>Ingrese monto a abonar:</font></html>");
		mensaje.setBounds(10, 20, 300, 20);
		add(mensaje);
		Object cuenta = tablaResultado.getValueAt(fila, 6);
		//VERIFICA QUE EL CLIENTE POSEA UNA CUENTA CORRIENTE
		if(cuenta==""){
			JOptionPane.showMessageDialog(null,
					"<html><font size=5>¡El cliente seleccionado no posee una Cuenta Corriente!</font></html>",
					"¡Error!", JOptionPane.WARNING_MESSAGE);
		}else{
			int n_cuenta = (int)cuenta;
			
			JLabel pesos = new JLabel("$");
			pesos.setBounds(85, 70, 15, 30);
			add(pesos);
			paraMonto = new JTextField();
			paraMonto.setHorizontalAlignment(SwingConstants.CENTER);
			paraMonto.setBounds(100, 70, 100, 30);
			paraMonto.addKeyListener(new KeyAdapter(){
				public void keyTyped(KeyEvent e){
					if(e.getKeyChar()>='0' && e.getKeyChar()<='9'){
						Double actual;
						if(paraMonto.getText().equals("")){
							actual=0.00;
						}else{
							actual = Double.parseDouble(paraMonto.getText());
						}
						actual = actual*10+e.getKeyChar()/100;
						paraMonto.setText(((Double)(Math.round(actual*100d)/100d)).toString());
					}else{
						e.consume();
					}
				}
			});
			add(paraMonto);
		
			//BOTON PARA REALIZAR EL ABONO
			abonar = new JButton("Abonar");
			abonar.setBounds(50, 140, 200, 30);
			abonar.addActionListener(new AbonarCuentaCorriente(tablaResultado,fila,this,n_cuenta,paraMonto));
			add(abonar);
		
			//ESTABLECE CANCELACION CON TECLA ESCAPE
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
	}

	JLabel mensaje;
	JTextField paraMonto;
	JButton abonar;
}
