package vista.ventas;

import java.awt.Color;
import java.awt.TextField;
import java.awt.event.*;

import javax.swing.*;

import modelo.consultas.EjecutaConsultasVenta;
import modelo.objetos.Cliente;
import modelo.objetos.CuentaCorriente;

public class VentanaSelecCtaCte extends JDialog{

	public VentanaSelecCtaCte(GestionVenta ventana){
	
		setLayout(null);
		setSize(400,300);
		setLocationRelativeTo(ventana);
		setModal(true);
		setTitle("Cuenta Corriente");
		setUndecorated(true);
	    getRootPane().setBorder(BorderFactory.createLineBorder(Color.BLACK));		
		
		JButton aceptar = new JButton("Seleccionar Cuenta");
		aceptar.setEnabled(false);

		muestra_numero_cuenta = new JLabel();
		muestra_datos_cliente = new JLabel();
		muestra_estado_cuenta = new JLabel();
		muestra_numero_cuenta.setBounds(30, 70, 300, 30);
		muestra_datos_cliente.setBounds(30, 110, 300, 30);
		muestra_estado_cuenta.setBounds(30, 150, 300, 30);
		add(muestra_numero_cuenta);
		add(muestra_datos_cliente);
		add(muestra_estado_cuenta);
		
		JTextField paraDniCliente = new JTextField(10);
		paraDniCliente.setHorizontalAlignment(JTextField.CENTER);
		paraDniCliente.setBounds(220, 24, 100, 22);
		paraDniCliente.setBorder(null);
		paraDniCliente.addKeyListener(new KeyAdapter(){
			
		
			
			public void keyPressed(KeyEvent e){
				
				if(e.getKeyChar()==KeyEvent.VK_ENTER){
					if(aceptar.isEnabled()){
						aceptar.doClick();
					}
					
					nuevaConsulta = new EjecutaConsultasVenta();
					cuenta = nuevaConsulta.obtieneCuentaCorriente(paraDniCliente.getText());
					cliente = nuevaConsulta.obtieneCliente(paraDniCliente.getText());
					try{
						num_cuenta = cuenta.getCodigo_cuenta();
						muestra_numero_cuenta.setText("<html><font size=3>CUENTA N°: " + String.format("%05d", num_cuenta) + "</font></html>");
					
						nombreyapellido = cliente.getApellido() + ", " + cliente.getNombre();
						muestra_datos_cliente.setText("<html><font size=3>CLIENTE: " + nombreyapellido + "</font></html>");
					
						estado_de_cuenta = cuenta.getEstado();
						muestra_estado_cuenta.setText("<html><font size=3>ESTADO DE CUENTA: " + estado_de_cuenta + "</font></html>");
						if(estado_de_cuenta<0){
							muestra_estado_cuenta.setForeground(Color.RED);
						}else
							muestra_estado_cuenta.setForeground(Color.BLACK);
					
						aceptar.setEnabled(true);
						}catch(NullPointerException ex){
							muestra_numero_cuenta.setText("<html><font size=3>EL CLIENTE NO ESTÁ REGISTRADO</font></html>");
							muestra_estado_cuenta.setText("");
							muestra_datos_cliente.setText("");
						}
						repaint(); validate();

				}
				else
					aceptar.setEnabled(false);
			}
			
		});
		add(paraDniCliente);

		JLabel requiereDniCliente = new JLabel("<html><font size=4>&nbsp;&nbsp;&nbsp;INGRESE DNI DEL CLIENTE</font></html>");
		requiereDniCliente.setBounds(0, 20, 400, 30);
		requiereDniCliente.setOpaque(true);
		requiereDniCliente.setBackground(new Color(145,145,255));
		add(requiereDniCliente);
		
		aceptar.setBounds(50, 200, 150, 30);
		aceptar.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				ventana.asignaCuentaCorriente(num_cuenta, estado_de_cuenta);
				dispose();
			}
		});
		add(aceptar);
		
		
		JButton cancelar = new JButton("Cancelar");
		cancelar.setBounds(200, 200, 150, 30);
		cancelar.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e){
				dispose();
			}
		});
		add(cancelar);
		
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
	}
	
	JLabel muestra_estado_cuenta, muestra_datos_cliente, muestra_numero_cuenta;
	int num_cuenta;
	String nombreyapellido;
	double estado_de_cuenta;
	Cliente cliente;
	CuentaCorriente cuenta;
	EjecutaConsultasVenta nuevaConsulta;
}
