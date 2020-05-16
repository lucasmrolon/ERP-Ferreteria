package vista;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import controlador.IniciarSesion;
import controlador.RecibirTeclado;
import modelo.consultas.EjecutaConsultasEmpleado;

//PANTALLA DE LOGIN
public class PanelLogin extends JPanel{
	/**
	 * 
	 */
	private static final long serialVersionUID = 8060298850947661145L;

	public PanelLogin(){
		
		setLayout(null);
		Dimension tamanio_pantalla = Toolkit.getDefaultToolkit().getScreenSize();
		
		LogoEmpresa logo = new LogoEmpresa(375,225);
		logo.setLocation((tamanio_pantalla.width/2)-187, 150);
		add(logo);
		
		
		//AÑADE EL CUADRO DE LOGIN
		CuadroLogin nuevoCuadro = new CuadroLogin();
		nuevoCuadro.setLocation((tamanio_pantalla.width/2)-250, (tamanio_pantalla.height/2)+50);//-75
		nuevoCuadro.setVisible(true);
		nuevoCuadro.setSize(new Dimension(500,150));
		add(nuevoCuadro);
		
		PanelLogin panel = this;
		JButton resetear_conexiones = new JButton("Resetear Conexiones");
		resetear_conexiones.setBounds(tamanio_pantalla.width-220, 10, 200, 20);
		resetear_conexiones.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				new ResetearConexiones(panel,resetear_conexiones);
			}
		});
		add(resetear_conexiones);
		
		ActionMap mapaAccion = getActionMap();
		InputMap mapa = getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
		
		KeyStroke esc = KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE,0);
		
		mapa.put(esc, "esc");
		mapaAccion.put("esc", new AbstractAction(){
			/**
			 * 
			 */
			private static final long serialVersionUID = -4434401628936496772L;

			@Override
			public void actionPerformed(ActionEvent arg0) {
				int elegido = JOptionPane.showConfirmDialog(null,"<html><font size=5>¿Confirma que desea salir de la aplicación?</font></html>",
						"¿Salir?", JOptionPane.OK_CANCEL_OPTION,JOptionPane.INFORMATION_MESSAGE);
				if(elegido==JOptionPane.OK_OPTION){
					System.exit(0);
				}
			}
		});
	}
	
	//CAMBIA EL FONDO DE LA PANTALLA
	public void paintComponent(Graphics g){
		
		Dimension tamanio = getSize();
		ImageIcon fondo = new ImageIcon(getClass().getResource("/img/fondo_pantalla_login.jpg"));
		g.drawImage(fondo.getImage(),0,0,tamanio.width,tamanio.height,null);
		setOpaque(false);
		super.paintComponent(g);
	}
}




//CUADRO DE LOGIN
class CuadroLogin extends JPanel{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 3758233871883532925L;
	public CuadroLogin(){
		
		//ESTABLECE CARACTERÍSTICAS DEL CUADRO
		setBackground(Color.LIGHT_GRAY);
		setLayout(new BorderLayout());
		
		//AGREGA BORDES OSCUROS
		setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, Color.BLACK));
		
		//COLOCA UN TÍTULO
		JPanel paraTitulo = new JPanel(); 
		JLabel titulo = new JLabel("Inicio de Sesión");
		titulo.setFont(new Font(Font.DIALOG,Font.ITALIC,16));
		titulo.setForeground(Color.WHITE);
		paraTitulo.add(titulo);
		paraTitulo.setBackground(Color.DARK_GRAY);
		paraTitulo.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.GRAY));
		add(paraTitulo,BorderLayout.NORTH);
		
		//CREA PANEL DE PARA INTRODUCIR LOS DATOS
		JPanel paraDatos = new JPanel(); 
		paraDatos.setLayout(null);	
		
		botonIngresar = new JButton("Ingresar");
		
		//PARA INGRESAR USUARIO
		JLabel usuario = new JLabel("Usuario:"); usuario.setBounds(82,10,70,50);
		usuario.setFont(new Font(Font.DIALOG,Font.ITALIC,14));
		paraDatos.add(usuario); 
		nombre_usuario = new JTextField(10); nombre_usuario.setBounds(150, 20, 150, 30);
		nombre_usuario.setFont(new Font(Font.DIALOG,Font.BOLD,14));
		nombre_usuario.addKeyListener(new RecibirTeclado(botonIngresar));
		paraDatos.add(nombre_usuario);
		
		//PARA INGRESAR CONTRASEÑA
		JLabel pass = new JLabel("Contraseña:"); pass.setBounds(56, 50, 80, 50);
		pass.setFont(new Font(Font.DIALOG,Font.ITALIC,14));
		paraDatos.add(pass);
		pass_usuario = new JPasswordField(10); pass_usuario.setBounds(150, 60, 150, 30);
		pass_usuario.setFont(new Font(Font.DIALOG,Font.PLAIN,14));
		pass_usuario.addKeyListener(new RecibirTeclado(botonIngresar));
		paraDatos.add(pass_usuario);
		
		//CREA EL BOTÓN DE INICIO DE SESIÓN Y LE AÑADE FUNCIONALIDAD
	    botonIngresar.setBounds(330, 40, 100, 30);
		botonIngresar.addActionListener(new IniciarSesion(nombre_usuario,pass_usuario,this));
		botonIngresar.addKeyListener(new RecibirTeclado(botonIngresar));
		botonIngresar.setFocusable(false);
		paraDatos.add(botonIngresar);
		
		//AÑADE EL PANEL AL CUADRO
		add(paraDatos,BorderLayout.CENTER);
		
		
		
	}
	
	JTextField nombre_usuario,pass_usuario;
	JButton botonIngresar;
}

//PERMITE RESETEAR LAS CONEXIONES 
class ResetearConexiones extends JDialog{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -6073266649973363961L;

	public ResetearConexiones(PanelLogin panel,JButton resetear){
		
		super((JFrame)panel.getParent().getParent().getParent().getParent());
		setLayout(null);
		setSize(300,200);
		setTitle("Reseteo de conexiones");
		setLocationRelativeTo(resetear);
		
		JLabel usuario = new JLabel("Usuario: ",JLabel.RIGHT);
		usuario.setBounds(10, 30, 100, 20);
		add(usuario);
		JTextField paraUsuario = new JTextField();
		paraUsuario.setBounds(120, 30, 100, 20);
		add(paraUsuario);
		
		JLabel contrasenia = new JLabel("Contraseña: ",JLabel.RIGHT);
		contrasenia.setBounds(10, 60, 100, 20);
		add(contrasenia);
		JTextField paraContrasenia = new JTextField();
		paraContrasenia.setBounds(120, 60, 100, 20);
		add(paraContrasenia);
		
		JButton paraResetear = new JButton("Resetear");
		paraResetear.setBounds(100, 110, 100, 20);
		add(paraResetear);
		paraResetear.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				
				EjecutaConsultasEmpleado paraValidar = new EjecutaConsultasEmpleado();
				Object[] passytipo = paraValidar.obtienePasswordTipoyEstado(paraUsuario.getText());
				
				//VERIFICA QUE EL USUARIO EXISTA
				if(passytipo[0]!=null){
					
					//VERIFICA QUE LA CONTRASEÑA SEA CORRECTA
					if(passytipo[0].equals(paraContrasenia.getText())){
						
						if(((String)passytipo[1]).equals("administrador")){
							
							paraValidar.desconectarTodos();
							dispose();
							JOptionPane.showMessageDialog(null,"<html><font size=5>Las conexiones fueron reseteadas con éxito. <br> "
									+ "Intente iniciar ahora.</font></html>",
									"Mensaje", JOptionPane.INFORMATION_MESSAGE);
						}
						else{
							JOptionPane.showMessageDialog(paraResetear, "El usuario ingresado no tiene privilegios"
									+ " suficientes para la tarea que intenta relizar.",
									"¡Error!",JOptionPane.ERROR_MESSAGE);
						}
					}
					else{
						JOptionPane.showMessageDialog(paraResetear, "El usuario no existe, o bien la contraseña es incorrecta",
								"¡Error! - Datos Inválidos",JOptionPane.ERROR_MESSAGE);
					}
				}
				//SI LOS DATOS SON INCORRECTOS, MUESTRA UN MENSAJE DE ERROR
				else{
					JOptionPane.showMessageDialog(paraResetear, "El usuario no existe, o bien la contraseña es incorrecta",
							"¡Error! - Datos Inválidos",JOptionPane.ERROR_MESSAGE);
				}
				
				
				
			}
			
		});
		
		ActionMap mapaAccion = this.getRootPane().getActionMap();
		InputMap mapa = this.getRootPane().getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
		
		KeyStroke enter = KeyStroke.getKeyStroke(KeyEvent.VK_ENTER,0);

		mapa.put(enter,"accion enter");
		mapaAccion.put("accion enter", new AbstractAction(){
			/**
			 * 
			 */
			private static final long serialVersionUID = -1229964756034622311L;

			public void actionPerformed(ActionEvent e){
				
				paraResetear.doClick();
				
			}
		});
		
		setVisible(true);
	}
}


