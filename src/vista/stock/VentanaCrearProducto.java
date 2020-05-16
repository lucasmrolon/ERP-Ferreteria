package vista.stock;

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

import javax.swing.*;
import javax.swing.border.Border;

import controlador.stock.CrearProducto;
import modelo.consultas.EjecutaConsultas;
import modelo.consultas.EjecutaConsultasProveedor;
import modelo.objetos.Proveedor;
import modelo.objetos.Rubro;

public class VentanaCrearProducto extends JDialog{

	/**
	 * 
	 */
	private static final long serialVersionUID = -630174781211965423L;
	public VentanaCrearProducto(PanelStock panelStock){
		
		//ESTABLECE FONDO, TAMAÑO Y UBICACIÓN DE LA VENTANA
		getContentPane().setBackground(new Color(225,240,240));
		this.setForeground(Color.BLACK);
		setModal(true);
	    setSize(400,750);
	    setUndecorated(true);
	    getRootPane().setBorder(BorderFactory.createLineBorder(Color.BLACK));
    	setLocationRelativeTo(panelStock.getRootPane());

	        
		setResizable(false);
		
		//ESTABLECE LAS FUENTES
		setFont(new Font(Font.SANS_SERIF,Font.ITALIC,14));
		Font fuente_errores = new Font(Font.DIALOG,Font.ITALIC,10);
		
		setLayout(null);
		
		//ARRAY DE POSIBLES ERRORES
		errores= new boolean[]{true,true,true,true,true};
	
		//TÍTULO DE LA VENTANA
		titulo = new JLabel("NUEVO PRODUCTO");
		titulo.setFont(new Font(Font.SANS_SERIF,Font.BOLD,24));
		titulo.setBounds(90, 10, 300, 50);
		add(titulo);
		
		//CÓDIGO
		codigo = new JLabel("CÓDIGO");
		codigo.setBounds(170,70,100,20);
		add(codigo);
		paraCodigo = new JTextField(10);
		paraCodigo.setBounds(150, 90, 100, 20);
		paraCodigo.setText(this.codigoSiguiente());
		paraCodigo.setHorizontalAlignment(SwingConstants.CENTER);
		err_cod=new JLabel();
		err_cod.setBounds(150, 110, 100, 20);
		err_cod.setFont(fuente_errores);
		err_cod.setForeground(Color.RED);
		add(err_cod);
		paraCodigo.addFocusListener(new CuadroActivo(paraCodigo,err_cod));
		add(paraCodigo);
		
		//DESCRIPCIÓN
		desc = new JLabel("DESCRIPCIÓN");
		desc.setBounds(170,130,100,20);
		add(desc);
		paraDesc = new JTextField(30);
		paraDesc.setBounds(60,150,300,20);
		err_desc=new JLabel();
		err_desc.setBounds(60, 170, 300, 20);
		err_desc.setFont(fuente_errores);
		err_desc.setForeground(Color.RED);
		add(err_desc);
		paraDesc.addFocusListener(new CuadroActivo(paraDesc,err_desc));
		add(paraDesc);
		
		//RUBRO
		rubro = new JLabel("RUBRO");
		rubro.setBounds(170, 190, 100, 20);
		add(rubro);
		paraRubro = new JComboBox<String>();
		EjecutaConsultas consulta1 = new EjecutaConsultas();
		rubros = consulta1.consultaRubros();
		if(rubros!=null){
			for(Rubro rubro : rubros){
				paraRubro.addItem(rubro.getNombre());
			}
		}else{
			JOptionPane.showMessageDialog(null,"<html><font size=4>No se pudo obtener la lista de rubros. Intente nuevamente</font></html>",
					"¡Error!", JOptionPane.WARNING_MESSAGE);
		}
		paraRubro.setBounds(100, 210, 200, 20);
		paraRubro.addFocusListener(new CuadroActivo(paraRubro,null));
		add(paraRubro);
		
		//PRECIO
		precio = new JLabel("PRECIO");
		precio.setBounds(170, 250, 100, 20);
		add(precio);
		
		paraPrecio = new JTextField(8);
		paraPrecio.setBounds(140, 270, 100, 20);
		paraPrecio.setHorizontalAlignment(SwingConstants.CENTER);
		err_prec=new JLabel();
		err_prec.setBounds(140, 290, 100, 20);
		err_prec.setFont(fuente_errores);
		err_prec.setForeground(Color.RED);
		add(err_prec);
		
		paraPrecio.setText("0.00");
		paraPrecio.addKeyListener(new KeyAdapter(){
			public void keyTyped(KeyEvent e){
				if(e.getKeyChar()>='0' && e.getKeyChar()<='9'){
					Double actual;
					if(paraPrecio.getText().equals("")){
						actual=0.00;
					}else{
						actual = Double.parseDouble(paraPrecio.getText());
					}
					actual = actual*10+e.getKeyChar()/100;
					paraPrecio.setText(((Double)(Math.round(actual*100d)/100d)).toString());
				}else{
					e.consume();
				}
			}
		});
		paraPrecio.addFocusListener(new CuadroActivo(paraPrecio,err_prec));
		add(paraPrecio);
		
		//CANTIDAD
		cantidad = new JLabel("CANTIDAD");
		cantidad.setBounds(80, 310, 100, 20);
		add(cantidad);
		paraCantidad = new JTextField(5);
		paraCantidad.setBounds(75, 330, 100, 20);
		paraCantidad.setHorizontalAlignment(SwingConstants.CENTER);
		err_cant=new JLabel();
		err_cant.setBounds(75, 350, 100, 20);
		err_cant.setFont(fuente_errores);
		err_cant.setForeground(Color.RED);
		add(err_cant);
		paraCantidad.addFocusListener(new CuadroActivo(paraCantidad,err_cant));
		add(paraCantidad);
		
		//TIPO DE UNIDAD
		tipo = new JLabel("TIPO UNIDAD");
		tipo.setBounds(225, 310, 100, 20);
		add(tipo);
		paraTipo = new JComboBox<String>();
		paraTipo.addItem("UNIDAD");
		paraTipo.addItem("METROS");
		paraTipo.addItem("LITROS");
		paraTipo.addItem("KILOGRAMOS");
		paraTipo.setBounds(225, 330, 100, 20);
		paraTipo.addFocusListener(new CuadroActivo(paraTipo,null));
		add(paraTipo);
		
		//PROVEEDOR
		proveedor = new JLabel("PROVEEDOR");
		proveedor.setBounds(80, 370, 100, 20);
		add(proveedor);
		paraProveedor = new JComboBox<String>();
		EjecutaConsultasProveedor consulta2 = new EjecutaConsultasProveedor();
		proveedores = consulta2.consultaProveedores();
		if(proveedores!=null){
			for(Proveedor proveedor : proveedores){
				paraProveedor.addItem(proveedor.getNombre());
			}
		}else{
			JOptionPane.showMessageDialog(null,"<html><font size=4>No se pudo obtener la lista de proveedores. Intente nuevamente</font></html>",
					"¡Error!", JOptionPane.WARNING_MESSAGE);
		}
		paraProveedor.setBounds(105, 390, 200, 20);
		paraProveedor.addFocusListener(new CuadroActivo(paraProveedor,null));
		add(paraProveedor);	
		
		//COMENTARIOS
		coment = new JLabel("OBSERVACIONES");
		coment.setBounds(170, 430, 90, 20);
		add(coment);
		paraComent = new JTextPane();
		paraComent.setBounds(60, 450, 300, 100);
		paraComent.setBorder(BorderFactory.createDashedBorder(Color.BLACK,5,1));
		paraComent.addFocusListener(new CuadroActivo(paraComent,null));
		add(paraComent); 
		
		//ALERTA
		JLabel alerta = new JLabel("Alertar cuando haya una cantidad menor a:");
		alerta.setBounds(60, 570, 300, 20);
		add(alerta);
		paraAlerta=new JTextField(10);
		paraAlerta.setBounds(110, 590, 100, 20);
		paraAlerta.setHorizontalAlignment(SwingConstants.CENTER);
		add(paraAlerta);
		err_alerta=new JLabel();
		err_alerta.setBounds(110, 610, 100, 20);
		err_alerta.setFont(fuente_errores);
		err_alerta.setForeground(Color.RED);
		add(err_alerta);
		paraAlerta.addFocusListener(new CuadroActivo(paraAlerta,err_alerta));
		
		//DA LA OPCIÓN DE CREAR O NO LA ALERTA
		no_alertar = new JCheckBox();
		no_alertar.setText("No añadir alerta");
		no_alertar.setBounds(220, 590, 120, 20);
		no_alertar.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if(no_alertar.isSelected()){
					paraAlerta.setEnabled(false);
					paraAlerta.setText("");
					err_alerta.setText("");
					paraAlerta.setBorder(por_defecto);	
					errores[4]=false;
				}else{
					paraAlerta.setEnabled(true);
					paraAlerta.setBorder(por_defecto);
					errores[4]=true;
					err_alerta.setText("¡Debe ser un número!");
				}
			}
		});
		add(no_alertar);
		
		//MENSAJE DE ERROR EN EL FORMULARIO
		con_errores= new JLabel();
		con_errores.setBounds(80, 630, 300, 20);
		con_errores.setFont(new Font(Font.DIALOG,Font.ITALIC,14));
		con_errores.setForeground(Color.RED);
		add(con_errores);
		
		//BOTÓN PARA AÑADIR EL PRODUCTO
		agregar_producto = new JButton("Añadir");
		agregar_producto.setBounds(80, 650, 100, 30);
		oyente = new CrearProducto(panelStock,this,errores,con_errores,
				paraCodigo,paraDesc,paraRubro,paraPrecio,paraCantidad,paraTipo,paraProveedor,paraComent,paraAlerta,no_alertar);
		agregar_producto.addActionListener(oyente);
		add(agregar_producto);
		
		//BOTÓN PARA CANCELAR LA OPERACIÓN
		JButton cancelar = new JButton("Cancelar");
		cancelar.setBounds(200, 650, 100, 30);
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
	}
	
	//OBTIENE AUTOMÁTICAMENTE UN CÓDIGO LIBRE
	public String codigoSiguiente(){
		EjecutaConsultas consulta = new EjecutaConsultas();
		int actual=0;
		String libre = "no";

		//while(i<tabla.getRowCount()){
		while(libre.equals("no")){
			actual++;
			libre = consulta.consultaCodigo(actual);
		}
		return actual+"";
		
	}
	
	//DA FORMATO A LOS CAMPOS QUE SE VAN COMPLETANDO
	private class CuadroActivo implements FocusListener{

		public CuadroActivo(JComponent campo,JLabel mens_err){
			
			this.campo = campo;
			this.mens_err=mens_err;
			
		}
		
		//CUANDO OBTIENE EL FOCO
		public void focusGained(FocusEvent e) {
				campo.setBorder(BorderFactory.createLineBorder(Color.GREEN,2));
		}

		//CUANDO PIERDE EL FOCO, COMPRUEBA ERRORES
		@Override
		public void focusLost(FocusEvent e) {
			
			//VERIFICA LOS VALORES
			if(campo==paraCodigo||campo==paraDesc||campo==paraPrecio||campo==paraCantidad||campo==paraAlerta){
				JTextField a_verificar = (JTextField)campo;
				if(a_verificar==paraCodigo){
					try{
						int cod=Integer.parseInt(a_verificar.getText());
						if(cod<1||cod>99999){
							a_verificar.setBorder(BorderFactory.createLineBorder(Color.RED,2));
							mens_err.setText("¡Código Inválido!");
							errores[0]=true;
						}else{
							mens_err.setText("");
							errores[0]=false;
						}
						EjecutaConsultas consulta = new EjecutaConsultas();
						
						//VERIFICA QUE EL CÓDIGO INGRESADO NO ESTÉ YA OCUPADO
						if(!errores[0]){
							String codigo_libre = consulta.consultaCodigo(cod);
							if(codigo_libre.equals("no")){
								a_verificar.setBorder(BorderFactory.createLineBorder(Color.RED,2));
								mens_err.setText("¡Código ya existente!");
								errores[0]=true;
							}
							else if(codigo_libre.equals("si")){
								mens_err.setText("");
								errores[0]=false;
							}
							else if(codigo_libre.equals("error")){
								a_verificar.setBorder(BorderFactory.createLineBorder(Color.RED,2));
								errores[0]=true;
								JOptionPane.showMessageDialog(null,"<html><font size=4>¡No se pudo conectar con la base de datos!</font></html>",
										"¡Error!", JOptionPane.WARNING_MESSAGE);
							}
						}
					}catch(NumberFormatException ex){
						a_verificar.setBorder(BorderFactory.createLineBorder(Color.RED,2));
						mens_err.setText("¡Código Inválido!");
						errores[0]=true;
					}
				}
				
				//VERIFICA QUE LA DESCRIPCIÓN NO ESTÉ EN BLANCO
				else if(a_verificar==paraDesc){
					String sinespacios = a_verificar.getText().trim();
					if(sinespacios.length()==0){
						a_verificar.setBorder(BorderFactory.createLineBorder(Color.RED,2));
						mens_err.setText("La descripción no puede estar en blanco");
						errores[1]=true;
					} else{
						errores[1]=false;
						mens_err.setText("");
					}
				}
				
				//VERIFICA QUE LOS CAMPOS DE PRECIO Y CANTIDAD TENGAN VALORES VÁLIDOS
				else if(a_verificar==paraPrecio||a_verificar==paraCantidad){
					int i;
					if(a_verificar==paraPrecio) i=2;
					else i=3;
					try{
						double valor = Double.parseDouble(a_verificar.getText());
						if(valor<0){
							a_verificar.setBorder(BorderFactory.createLineBorder(Color.RED,2));
							mens_err.setText("¡Debe ser mayor a 0!");
							errores[i]=true;
						}else{
							mens_err.setText("");
							errores[i]=false;
						}
					}catch(Exception ex){
						a_verificar.setBorder(BorderFactory.createLineBorder(Color.RED,2));
						mens_err.setText("¡Debe ser un número!");
						errores[i]=true;
					}
				}
				
				//VERIFICA QUE EL VALOR DE CANTIDAD MÍNIMA SEA VÁLIDO
				else if(a_verificar==paraAlerta){
					if(a_verificar.isEnabled()){
						try{
							double valor = Double.parseDouble(a_verificar.getText());
							if(valor<0){
								a_verificar.setBorder(BorderFactory.createLineBorder(Color.RED,2));
								mens_err.setText("¡Debe ser mayor a 0!");
								errores[4]=true;
							}else{
								mens_err.setText("");
								errores[4]=false;
							}
						}catch(Exception ex){
							a_verificar.setBorder(BorderFactory.createLineBorder(Color.RED,2));
							mens_err.setText("¡Debe ser un número!");
							errores[4]=true;
						}
					}
				}
			}
			else{
				campo.setBorder(por_defecto);				
			}	
		}

		JComponent campo;
		JLabel mens_err;
	}

	PanelStock panel;
	JLabel titulo,codigo,desc,rubro,precio,cantidad,tipo,proveedor,codProv,coment;
	public JLabel err_cod,err_desc,err_prec,err_cant,err_alerta,con_errores;
	public JTextField paraCodigo,paraDesc,paraPrecio,paraCantidad,paraAlerta;
	public JComboBox<String> paraRubro,paraTipo,paraProveedor;
	public JTextPane paraComent;
	public JCheckBox no_alertar;
	public JButton agregar_producto;
	ActionListener oyente;
	boolean[] errores;
	Border por_defecto;
	ArrayList<Rubro> rubros;
	ArrayList<Proveedor> proveedores;
}
