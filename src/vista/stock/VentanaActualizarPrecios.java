package vista.stock;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import controlador.stock.ActualizarPrecios;

public class VentanaActualizarPrecios extends JDialog{

	/**
	 * 
	 */
	private static final long serialVersionUID = -8611312528840986072L;
	public VentanaActualizarPrecios(PanelStock padre){
	
		//TAMAÑO Y UBICACIÓN DE LA VENTANA
		getContentPane().setBackground(new Color(225,240,240));
		setModal(true);
		setTitle("AUMENTO GLOBAL");
		setSize(500,220);
		this.setLocationRelativeTo(null);
		setUndecorated(true);
	    getRootPane().setBorder(BorderFactory.createLineBorder(Color.BLACK));
		setLayout(null);
	
		texto = new JLabel("<html><Font size=4>Ingrese valor del aumento a aplicar a TODOS los productos: </Font></html>",JLabel.CENTER);
		texto.setBounds(20, 30, 450, 30);
		add(texto);
		
		//SPINNER PARA SELECCIONAR EL PORCENTAJE DE AUMENTO
		seleccionValor = new JSpinner(new SpinnerNumberModel(1,0.01,300,0.01));
		seleccionValor.setFont(new Font(Font.DIALOG,Font.BOLD,20));
		seleccionValor.setBounds(170,85,100,35);
		//((DefaultEditor)seleccionValor.getEditor()).getTextField().setEditable(false);
		SpinnerModel modelo = seleccionValor.getModel();
		seleccionValor.addMouseWheelListener(new MouseAdapter() {
		    @Override
		    public void mouseWheelMoved(MouseWheelEvent e) {
		        try {
		            Object mov = null;
		            if (e.getWheelRotation() == 1) {
		                mov = modelo.getPreviousValue();
		            } else if (e.getWheelRotation() == -1) {
		            	mov = modelo.getNextValue();
		            }
		            if (mov == null) {
		                return;
		            }
		            modelo.setValue(mov);
		        } catch (Exception ex) {
		            ex.printStackTrace();
		        }
		    }
		});
		add(seleccionValor);
		
		//MENSAJE DE ERROR SI SE INGRESA UN VALOR INVÁLIDO
				
		JLabel simbolo = new JLabel("%");
		simbolo.setFont(new Font(Font.DIALOG,Font.BOLD,20));
		simbolo.setBounds(280, 72, 30, 40);
		add(simbolo);
	
		//BOTÓN PARA APLICAR EL AUMENTO
		actualizar = new JButton("Actualizar");
		actualizar.setBounds(100, 150, 100, 30);
		actualizar.addActionListener(new ActualizarPrecios(padre,this,seleccionValor));
		add(actualizar);
		
		//BOTÓN PARA CANCELAR LA OPERACIÓN
		cancelar = new JButton("Cancelar");
		cancelar.setBounds(280, 150, 100, 30);
		cancelar.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {
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
	
		setVisible(true);
	}
	
	JLabel texto;
	JSpinner seleccionValor;
	JButton actualizar,cancelar;
}
