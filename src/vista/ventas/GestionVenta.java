package vista.ventas;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.*;
import java.text.DecimalFormat;
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.table.DefaultTableModel;

import controlador.ventas.ConfirmarVenta;
import modelo.consultas.EjecutaConsultasVenta;
import modelo.objetos.FormaDePago;

//VENTANA PARA CONFIGURAR LA VENTA ANTES DE REGISTRARLA
public class GestionVenta extends JDialog{
	
	public GestionVenta(JTable compra,DefaultTableModel tablaListaReparto, String usuario, int cod_venta, PanelFacturacion panel){
		setTitle("Facturar Venta");
		setLayout(null);
		setModal(true);
		setUndecorated(true);
		getRootPane().setBorder(BorderFactory.createLineBorder(Color.BLACK));
		getContentPane().setBackground(new Color(225,240,240));
		monto_total = 0;
		hay_reparto=false;
		formatea = new DecimalFormat("###,###.##");
		
		Font fuenteVentana = new Font(Font.DIALOG,Font.BOLD,14);
		
		//VERIFICA SI HAY LÍNEAS ASIGNADAS A REPARTO Y CALCULA EL MONTO TOTAL DE LA VENTA
		for(int i=0;i<compra.getRowCount();i++){
			monto_total+=(double)compra.getValueAt(i, 4);
			if(((String)compra.getValueAt(i, 5)).equals("SI")){
				hay_reparto=true;
			}
		}
		
		if(hay_reparto){
			setSize(600,500);
		}else{
			setSize(600,400);
		}
		setLocationRelativeTo(null);
		
		//MUESTRA MONTO TOTAL DE VENTA
		JLabel venta = new JLabel("<html><font size=5>TOTAL VENTA: $ " + formatea.format(monto_total) + "</font></html>",JLabel.CENTER);
		venta.setBounds(0, 10, 600, 40);
		venta.setOpaque(true);
		venta.setBackground(new Color(79,79,255));
		venta.setForeground(Color.WHITE);
		add(venta);
	
		//MUESTRA CANTIDAD DE LÍNEAS DE VENTA
		JLabel cant_productos = new JLabel("<html><font size=4>CANTIDAD DE PRODUCTOS: " + compra.getRowCount()+ "</font></html>",JLabel.CENTER);
		cant_productos.setBounds(0, 50, 600, 30);
		cant_productos.setOpaque(true);
		cant_productos.setBackground(new Color(145,145,255));
		cant_productos.setForeground(Color.WHITE);
		add(cant_productos);
		
		//MUESTRA FORMAS DE PAGO DISPONIBLES
		JLabel paraformasdepago = new JLabel("<html><font size=4>Forma de pago: </font></html>");
		paraformasdepago.setBounds(10, 110, 200, 40);
		add(paraformasdepago);
		JComboBox muestra_formas_pago = new JComboBox();
		EjecutaConsultasVenta consulta1 = new EjecutaConsultasVenta();
		ArrayList<FormaDePago> formasdepago = consulta1.obtener_formas_de_pago();
				
		muestra_formas_pago.setFont(new Font(Font.DIALOG,Font.PLAIN,14));
		
		if(formasdepago!=null){
			for(FormaDePago forma : formasdepago){
				muestra_formas_pago.addItem(forma);
			}
		}
		muestra_formas_pago.addActionListener(new AdministraFormasDePago(this,muestra_formas_pago,monto_total));
		muestra_formas_pago.setBounds(60, 150, 200, 30);
		add(muestra_formas_pago);
		
		//SI HAY REPARTO, MUESTRA CAMPOS PARA RELLENAR 
		if(hay_reparto){
			
			JLabel nombre = new JLabel("Nom. y Ap.:");
			nombre.setBounds(30, 330, 100, 25);
			nombre.setFont(fuenteVentana);
			add(nombre);
			paraNombre = new JTextField(40);
			paraNombre.setBounds(110,330,150,25);
			paraNombre.setFont(fuenteVentana);
			add(paraNombre);
			
			JLabel direccion = new JLabel("Direccion:");
			direccion.setBounds(30, 370, 100, 25);
			direccion.setFont(fuenteVentana);
			add(direccion);
			paraDireccion = new JTextField(30);
			paraDireccion.setBounds(110, 370, 150, 25);
			paraDireccion.setFont(fuenteVentana);
			add(paraDireccion);
			
			JLabel turno = new JLabel("Turno preferido:");
			turno.setBounds(300, 330, 150, 25);
			turno.setFont(fuenteVentana);
			add(turno);
			paraTurno = new JComboBox();
			paraTurno.addItem("Mañana");
			paraTurno.addItem("Tarde");
			paraTurno.setBounds(420, 330, 100, 25);
			paraTurno.setFont(fuenteVentana);
			add(paraTurno);
			
			JLabel comentarios = new JLabel("Observaciones:");
			comentarios.setBounds(300, 370, 150, 25);
			comentarios.setFont(fuenteVentana);
			add(comentarios);
			paraComentarios = new JTextField(50);
			paraComentarios.setBounds(420, 370, 150, 25);
			add(paraComentarios);
		}
		JSeparator separadorVertical = new JSeparator();
		separadorVertical.setLocation(300, 60); separadorVertical.setSize(1, 220); separadorVertical.setOrientation(SwingConstants.VERTICAL);
		add(separadorVertical);
		//BOTÓN PARA CONFIRMAR Y REGISTRAR LA VENTA
		JButton registrarVenta = new JButton("Confirmar Venta");
		JButton cancelarVenta = new JButton("Cancelar");
		
		if(hay_reparto){
			registrarVenta.setBounds(225, 410, 150, 30);
			cancelarVenta.setBounds(225, 450, 150, 30);
			registrarVenta.addActionListener(new ConfirmarVenta(cod_venta,compra,tablaListaReparto,usuario,muestra_formas_pago,
					paraNombre,paraDireccion,paraTurno,paraComentarios,panel,this));
			JSeparator separadorHorizontal = new JSeparator();
			separadorHorizontal.setLocation(0,280); separadorHorizontal.setSize(600, 1); 
			add(separadorHorizontal);
			JLabel infoReparto = new JLabel("<html><font size=3>INFORMACIÓN PARA REPARTO</font></html>",JLabel.CENTER);
			infoReparto.setBounds(0, 280, 600, 25);
			infoReparto.setOpaque(true);
			infoReparto.setBackground(new Color(145,145,255));
			infoReparto.setForeground(Color.WHITE);
			add(infoReparto);
		}else{
			registrarVenta.setBounds(225, 310, 150, 30);
			cancelarVenta.setBounds(225, 350, 150, 30);
			registrarVenta.addActionListener(new ConfirmarVenta(cod_venta,compra,usuario,muestra_formas_pago,panel,this));
		}
		cancelarVenta.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				dispose();	
			}
		});
		add(cancelarVenta);
		add(registrarVenta);
		
		//MUESTRA CÓDIGO Y ESTADO ANTERIOR Y ACTUAL DE LA CUENTA CORRIENTE
		paraCodCtaCte = new JLabel();
		paraCodCtaCte.setBounds(320, 120, 200, 20);
		paraCodCtaCte.setFont(fuenteVentana);
		paraCodCtaCte.setVisible(false);
		add(paraCodCtaCte);
		paraEstadoCtaCte = new JLabel();
		paraEstadoCtaCte.setBounds(320, 150, 200, 20);
		paraEstadoCtaCte.setFont(fuenteVentana);
		paraEstadoCtaCte.setVisible(false);
		add(paraEstadoCtaCte);
		paraSumaACuenta = new JLabel();
		paraSumaACuenta.setBounds(320, 180, 200, 20);
		paraSumaACuenta.setFont(fuenteVentana);
		paraSumaACuenta.setVisible(false);
		add(paraSumaACuenta);
		paraEstadoCtaCteNuevo = new JLabel();
		paraEstadoCtaCteNuevo.setBounds(320, 210, 200, 20);
		paraEstadoCtaCteNuevo.setFont(fuenteVentana);
		paraEstadoCtaCteNuevo.setVisible(false);
		add(paraEstadoCtaCteNuevo);
		
		
		//MUESTRA EL PORCENTAJE DE AUMENTO O RECARGO A APLICAR
		paraModif=new JLabel();
		paraModif.setFont(fuenteVentana);
		paraModif.setBounds(300,110,160,20);
		paraModif.setOpaque(true);
		paraModif.setBackground(new Color(160,160,233));
		paraModif.setForeground(Color.WHITE);
		paraModif.setVisible(false);
		add(paraModif);
		//MUESTRA EL TOTAL ANTES DE APLICAR EL RECARGO O DESCUENTO
		subtotal = new JLabel("Subtotal:                      $");
		subtotal.setBounds(320, 140, 200, 20);
		subtotal.setFont(fuenteVentana);
		subtotal.setVisible(false);
		add(subtotal);
		paraSubtotal = new JLabel();
		paraSubtotal.setFont(fuenteVentana);
		paraSubtotal.setBounds(450,140,100,20);
		paraSubtotal.setHorizontalAlignment(JLabel.RIGHT);
		paraSubtotal.setVisible(false);
		add(paraSubtotal);
		//MUESTRA EL VALOR EN PESOS DEL RECARGO O DESCUENTO
		descuentoorecargo = new JLabel();
		descuentoorecargo.setBounds(320, 170, 200, 20);
		descuentoorecargo.setFont(fuenteVentana);
		descuentoorecargo.setVisible(false);
		add(descuentoorecargo);
        paraDescuentoORecargo = new JLabel();
        paraDescuentoORecargo.setFont(fuenteVentana);
        paraDescuentoORecargo.setBounds(450,170,100,20);
		paraDescuentoORecargo.setHorizontalAlignment(JLabel.RIGHT);
        paraDescuentoORecargo.setVisible(false);
		add(paraDescuentoORecargo);
		//MUESTRA EL MONTO FINAL DE LA VENTA
		total = new JLabel("Total a pagar:              $");
		total.setBounds(320, 200, 200, 20);
		total.setFont(fuenteVentana);
		total.setVisible(false);
		add(total);
		paraTotal = new JLabel();
		paraTotal.setFont(fuenteVentana);
		paraTotal.setBounds(450, 200, 100, 20);
		paraTotal.setHorizontalAlignment(JLabel.RIGHT);
		paraTotal.setVisible(false);
		add(paraTotal);
		//PERMITE ELEGIR EL NÚMERO DE CUOTAS A ESTABLECER
		paraNumeroCuotas = new JSpinner(new SpinnerNumberModel(1,1,24,1));
		paraNumeroCuotas.setBounds(345,240,40,30);
		paraNumeroCuotas.setVisible(false);
		add(paraNumeroCuotas);
		//MUESTRA EL MONTO DE CADA CUOTA
		paraCuotas = new JLabel();
		paraCuotas.setFont(fuenteVentana);
		paraCuotas.setBounds(400, 240, 200, 30);
		paraCuotas.setVisible(false);
		add(paraCuotas);
		
		paga_con = new JLabel("Paga con: ");
		paga_con.setBounds(320, 120, 100, 30);
		paga_con.setFont(fuenteVentana);
		add(paga_con);
		paraPago = new JTextField(20);
		paraPago.setHorizontalAlignment(JTextField.CENTER);
		paraPago.setFont(fuenteVentana);
		paraPago.setBounds(420, 120, 100, 30);
		paraPago.setText("0");
		paraPago.addKeyListener(new KeyAdapter(){
			public void keyTyped(KeyEvent e){
				if(e.getKeyChar()>='0' && e.getKeyChar()<='9'){
					Double actual;
					if(paraPago.getText().equals("")){
						actual=0.00;
					}else{
						actual = Double.parseDouble(paraPago.getText());
					}
					actual = actual*10+e.getKeyChar()/100;
					paraPago.setText(((Double)(Math.round(actual*100d)/100d)).toString());
				}else{
					e.consume();
				}
			}
		});
		add(paraPago);
		
		su_vuelto = new JLabel("Su vuelto: ");
		su_vuelto.setFont(fuenteVentana);
		su_vuelto.setBounds(320, 160, 100, 30);
		add(su_vuelto);
		paraVuelto = new JTextField(20);
		paraVuelto.setBounds(420, 160, 100, 30);
		paraVuelto.setHorizontalAlignment(JTextField.CENTER);
		paraVuelto.setFont(fuenteVentana);
		paraVuelto.setText(String.format("-"));
		paraVuelto.setEditable(false);
		add(paraVuelto);
		
		paraPago.addKeyListener(new KeyAdapter(){
			@Override
			public void keyReleased(KeyEvent arg0) {
				if(!paraPago.getText().trim().equals("")){
					double vuelto = Double.parseDouble(paraPago.getText()) - monto_total;
					if(vuelto>0){
						paraVuelto.setText(String.format("%.2f", vuelto));
					}else{
						paraVuelto.setText("-");
					}
				}else{
					paraVuelto.setText(String.format("-"));
				}
			}
		});
		paraPago.addFocusListener(new FocusAdapter(){
			@Override
			public void focusGained(FocusEvent arg0) {
				paraPago.setText("");
			}
		});
		setVisible(true);
		if(formasdepago==null){
			registrarVenta.setEnabled(false);
			String mensaje = "<html><Font size=5>¡No se pudo obtener formas de pago! Intente nuevamente.</Font></html>";
			JOptionPane.showMessageDialog(null, mensaje, "¡Error!", JOptionPane.WARNING_MESSAGE);
			dispose();
		}
		
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
	
	//METODO QUE ASIGNA UNA CUENTA CORRIENTE A LA VENTA ACTUAL
	public void asignaCuentaCorriente(int codigo_cuenta,double estado){
		
		codigo_CtaCte = codigo_cuenta;
		estado_CtaCte = estado;
		
		if(estado<0){
			paraEstadoCtaCte.setForeground(Color.RED);
		}else{
			paraEstadoCtaCte.setForeground(Color.GREEN);
		}
		if(estado-monto_total<0){
			paraEstadoCtaCteNuevo.setForeground(Color.RED);
		}else{
			paraEstadoCtaCteNuevo.setForeground(Color.GREEN);
		}
		
		paraCodCtaCte.setText("Cuenta N°: " + String.format("%05d",codigo_cuenta));
		paraCodCtaCte.setVisible(true);
		
		paraEstadoCtaCte.setText("Estado actual:   $ " + String.format("%.2f", estado));
		paraEstadoCtaCte.setVisible(true);
		
		paraSumaACuenta.setText("Venta:   $ " + String.format("%.2f",monto_total));
		paraSumaACuenta.setVisible(true);
		
		paraEstadoCtaCteNuevo.setText("Estado nuevo:   $ " + String.format("%.2f", estado - monto_total));
		paraEstadoCtaCteNuevo.setVisible(true);
		
		repaint();
		validate();
		
	}
	
	//MÉTODO QUE DEVUELVE EL CODIGO DE CUENTA CORRIENTE ASOCIADA A LA VENTA
	public int devuelveCodCtaCte(){
		return codigo_CtaCte;
	};

	boolean hay_reparto;
	double monto_total;
	double monto_total_modificado;
	JLabel paraModif,subtotal,paraSubtotal,descuentoorecargo,paraDescuentoORecargo,total,paraTotal,
		paraCodCtaCte,paraEstadoCtaCte,paraSumaACuenta,paraEstadoCtaCteNuevo,paraCuotas,paga_con,su_vuelto;
	FormaDePago forma;
	JTextField paraNombre,paraDireccion,paraComentarios,paraPago,paraVuelto;
	JComboBox paraTurno;
	double estado_CtaCte;
	int codigo_CtaCte;
	JSpinner paraNumeroCuotas;
	DecimalFormat formatea;
}

class AdministraFormasDePago implements ActionListener{
	
	public AdministraFormasDePago(GestionVenta ventana,JComboBox formas,double monto){
		
		this.ventana=ventana;
		this.formas = formas;
		this.monto_total=monto;
		
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {

		//OBTIENE LOS VALORES DE RECARGO O DESCUENTO A APLICAR
		FormaDePago forma = (FormaDePago)formas.getSelectedItem();
		double descuento = forma.getDescuento()/100;
		double recargo = forma.getRecargo()/100;
		
		if(descuento!=0 || recargo!=0){
			double modificar = 0;
			//SI SE DEBE APLICAR RECARGO, CALCULA LOS MONTOS
			//MUESTRA EL PORCENTAJE DE RECARGO O DESCUENTO QUE DEBE APLICARSE
			if(recargo!=0){
				modificar = recargo;
				ventana.paraModif.setText("   Recargo:    " + Math.round(recargo*100) + " %");
				ventana.descuentoorecargo.setText("A recargar:                  $");
				monto_total_modificado=Math.round((monto_total + monto_total*recargo)*100d)/100d;
			//SI SE DEBE APLICAR DESCUENTO, CALCULA LOS MONTOS
			}else if(descuento!=0){
				modificar=descuento;
				ventana.paraModif.setText("   Descuento:    " + Math.round(descuento*100) + " %");
				ventana.descuentoorecargo.setText("A descontar:               $");
				monto_total_modificado=Math.round((monto_total - monto_total*descuento)*100d)/100d;
			}
			ventana.paraDescuentoORecargo.setText(String.format("%.2f", (Math.round(monto_total*modificar*100d)/100d)));
			ventana.paraSubtotal.setText(String.format("%.2f", Math.round(monto_total*100d)/100d));
			ventana.paraTotal.setText(String.format("%.2f", monto_total_modificado));
			ventana.paraModif.setVisible(true);
			ventana.paraSubtotal.setVisible(true);
			ventana.paraDescuentoORecargo.setVisible(true);
			ventana.paraTotal.setVisible(true);
			ventana.subtotal.setVisible(true);
			ventana.descuentoorecargo.setVisible(true);
			ventana.total.setVisible(true);

			
		}
		else{
			//SI NO HAY RECARGOS NI DESCUENTOS, EL MONTO FINAL ES EL ORIGINAL
			monto_total_modificado = monto_total;
			ventana.paraModif.setVisible(false);
			ventana.paraSubtotal.setVisible(false);
			ventana.paraDescuentoORecargo.setVisible(false);
			ventana.paraTotal.setVisible(false);
			ventana.subtotal.setVisible(false);
			ventana.descuentoorecargo.setVisible(false);
			ventana.total.setVisible(false);
		}

		//SI SE ELIGIÓ LA FORMA DE PAGO "Efectivo", SE OCULTAN LOS COMPONENTES CORRESPONDIENTES A LAS OTRAS FORMAS DE PAGO
		if(forma.getTipo().equals("Efectivo")){

			
			ventana.paraCodCtaCte.setVisible(false);
			ventana.paraEstadoCtaCte.setVisible(false);
			ventana.paraSumaACuenta.setVisible(false);
			ventana.paraEstadoCtaCteNuevo.setVisible(false);
			ventana.paraNumeroCuotas.setVisible(false);
			ventana.paraCuotas.setVisible(false);
			ventana.paga_con.setVisible(true);
			ventana.su_vuelto.setVisible(true);
			ventana.paraPago.setVisible(true);
			ventana.paraVuelto.setVisible(true);
		}
		
		//SI SE ELIGIÓ LA FORMA DE PAGO "Cuenta corriente", SE ABRE UNA VENTANA PARA INGRESAR DATOS DE CUENTA
		else if(forma.getTipo().equals("Cuenta corriente")){
			ventana.paraNumeroCuotas.setVisible(false);
			ventana.paraCuotas.setVisible(false);
			ventana.paga_con.setVisible(false);
			ventana.su_vuelto.setVisible(false);
			ventana.paraPago.setVisible(false);
			ventana.paraVuelto.setVisible(false);
			VentanaSelecCtaCte nuevaVentana = new VentanaSelecCtaCte(ventana);
			nuevaVentana.setVisible(true);
		}
		
		//SI SE ELIGIO LA FORMA DE PAGO "Tarjeta de crédito", DA LA POSIBILIDAD DE CALCULAR EL NUMERO Y MONTO DE LAS CUOTAS
		else if(forma.getTipo().equals("Tarjeta de crédito")){
			ventana.paraCuotas.setText("pagos de $ " + String.format("%.2f", monto_total_modificado));
			ventana.paraCuotas.setVisible(true);
			ventana.paraCodCtaCte.setVisible(false);
			ventana.paraEstadoCtaCte.setVisible(false);
			ventana.paraSumaACuenta.setVisible(false);
			ventana.paraEstadoCtaCteNuevo.setVisible(false);
			ventana.paga_con.setVisible(false);
			ventana.su_vuelto.setVisible(false);
			ventana.paraPago.setVisible(false);
			ventana.paraVuelto.setVisible(false);
			
			//SPINNER PARA ELEGIR EL NÚMERO DE CUOTAS
			ventana.paraNumeroCuotas.addChangeListener(new ChangeListener(){
				@Override
				public void stateChanged(ChangeEvent arg0) {
					double monto_cuota = Math.round((monto_total_modificado/(int)ventana.paraNumeroCuotas.getValue())*100d)/100d;
					ventana.paraCuotas.setText("pagos de $ " + String.format("%.2f", monto_cuota));
				}
			});
			ventana.paraNumeroCuotas.setVisible(true);
		}
		
		//SI SE ELIGIÓ LA FORMA DE PAGO "Tarjeta de débito", MUESTRA LA INFORMACIÓN CORRESPONDIENTE
		else if(forma.getTipo().equals("Tarjeta de débito")){
			ventana.paraNumeroCuotas.setVisible(false);
			ventana.paraCuotas.setVisible(false);
			ventana.paraCodCtaCte.setVisible(false);
			ventana.paraEstadoCtaCte.setVisible(false);
			ventana.paraSumaACuenta.setVisible(false);
			ventana.paraEstadoCtaCteNuevo.setVisible(false);
			ventana.paga_con.setVisible(false);
			ventana.su_vuelto.setVisible(false);
			ventana.paraPago.setVisible(false);
			ventana.paraVuelto.setVisible(false);
		}
		
		ventana.repaint();
		ventana.validate();
	}
	
	GestionVenta ventana;
	JComboBox formas = new JComboBox();
	double monto_total_modificado;
	double monto_total;
}