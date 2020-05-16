package vista.detalle;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.*;

import controlador.detalle.AbrirCuentaCorriente;
import modelo.consultas.EjecutaConsultasCtasCtes;

//VENTANA PARA ABRIR UNA NUEVA CUENTA CORRIENTE
public class VentanaAbrirCuenta extends JDialog{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -3458553099555801134L;
	public VentanaAbrirCuenta(int fila,JTable tablaResultado){
		
		setSize(300, 270);
		setLocationRelativeTo(null);
		setModal(true);
		setLayout(null);
		setTitle("Abrir Cuenta Corriente");
		setUndecorated(true);
		getRootPane().setBorder(BorderFactory.createLineBorder(Color.BLACK));
		
		int nueva_cuenta = obtener_numero_de_cuenta();
		
		//MUESTRA INFORMACION DE LA NUEVA CUENTA A CREAR
		mensaje = new JLabel("<html><font size=4>Cuenta Nº: " + String.format("%05d", nueva_cuenta) + "</font></html>");
		mensaje.setBounds(10, 30, 150, 20);
		add(mensaje);
		
		//PERMITE INGRESAR EL MONTO INICIAL DE LA CUENTA
		monto_inicial = new JLabel("<html><font size=4>Ingrese monto inicial:</font></html>");
		monto_inicial.setBounds(10, 70, 300, 20);
		add(monto_inicial);
			
			JLabel pesos = new JLabel("$");
			pesos.setBounds(85, 120, 15, 30);
			add(pesos);
			paraMonto = new JTextField();
			paraMonto.setHorizontalAlignment(SwingConstants.CENTER);
			paraMonto.setBounds(100, 120, 100, 30);
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
		
			//ABRE LA CUENTA
			abrir = new JButton("Abrir Cuenta");
			abrir.setBounds(50, 170, 200, 30);
			abrir.addActionListener(new AbrirCuentaCorriente(tablaResultado,fila,this,nueva_cuenta,paraMonto));
			add(abrir);
		
			//ESTABLECE CANCELAR CON TECLA ESCAPE
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

	public int obtener_numero_de_cuenta(){
		
		EjecutaConsultasCtasCtes nueva_consulta = new EjecutaConsultasCtasCtes();
		int n_cuenta = nueva_consulta.obtenerNuevo_n_cuenta();
		return n_cuenta;
	}
	JLabel mensaje,monto_inicial;
	JTextField paraMonto;
	JButton abrir;

}
