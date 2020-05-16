package vista.estadisticas;

import java.awt.*;
import java.awt.event.*;
import java.text.SimpleDateFormat;
import java.util.Date;

import controlador.estadisticas.*;
import javax.swing.*;

//PANEL QUE MUESTRA GRAFICOS ESTADISTICOS
public class PanelEstadisticas extends JPanel {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 4932732813375016776L;
	public PanelEstadisticas(){
		
		setBackground(new Color(224,224,248));
		int ancho_pantalla = Toolkit.getDefaultToolkit().getScreenSize().width;
		Font fuente1 = new Font(Font.DIALOG,Font.ITALIC+Font.BOLD,20);
		paraActualizar = new ParaActualizar(this);
		
		add(paraActualizar,BorderLayout.NORTH);
		
		graficas = new JPanel();
		graficas.setBackground(new Color(224,224,248));
		graficas.setPreferredSize(new Dimension(ancho_pantalla, 900));
		
		generarPrimerGrupo(graficas,ancho_pantalla,fuente1);
			
		generarSegundoGrupo(graficas,ancho_pantalla,fuente1);
				
		generarTercerGrupo(graficas,ancho_pantalla,fuente1);
				
		generarCuartoGrupo(graficas);
		
		add(graficas);
		
		ActionMap mapaAccion = getActionMap();
		InputMap mapa = getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
		
		KeyStroke ctrl_n = KeyStroke.getKeyStroke(KeyEvent.VK_F5,0);

		
		mapa.put(ctrl_n, "ctrl+n");
		mapaAccion.put("ctrl+n", new AbstractAction(){
			/**
			 * 
			 */
			private static final long serialVersionUID = -5226785606913092369L;

			@Override
			public void actionPerformed(ActionEvent arg0) {
				paraActualizar.actualizar.doClick();
			}
		});
		
	}
	
	//GENERA ESTADISTICAS DIARIAS
	public void generarPrimerGrupo(JPanel panelDatos,int ancho_pantalla, Font fuente1){

		JLabel titulo1 = new JLabel("Últimos 30 Días");
		titulo1.setFont(fuente1);
		titulo1.setPreferredSize(new Dimension(ancho_pantalla,40));
		titulo1.setHorizontalAlignment(SwingConstants.CENTER);
		panelDatos.add(titulo1);
		
		new Generar_estadisticas_dia(panelDatos);
		
		JSeparator separador1 = new JSeparator();
		separador1.setPreferredSize(new Dimension(ancho_pantalla-100,5));separador1.setOrientation(SwingConstants.HORIZONTAL);
		separador1.setBackground(Color.RED);
		panelDatos.add(separador1);
		
	}
	
	//GENERA ESTADISTICAS MENSUALES
	public void generarSegundoGrupo(JPanel panelDatos,int ancho_pantalla, Font fuente1){
		
		JLabel titulo2 = new JLabel("Mensuales");
		titulo2.setFont(fuente1);
		titulo2.setPreferredSize(new Dimension(ancho_pantalla,40));
		titulo2.setHorizontalAlignment(SwingConstants.CENTER);
		panelDatos.add(titulo2);
		
		new Generar_estadisticas_mes(panelDatos);
		
		JSeparator separador2 = new JSeparator();
		separador2.setPreferredSize(new Dimension(ancho_pantalla-100,5));separador2.setOrientation(SwingConstants.HORIZONTAL);
		separador2.setBackground(Color.RED);
		panelDatos.add(separador2);
		
	}

	//GENERA ESTADISTICAS ANUALES
	public void generarTercerGrupo(JPanel panelDatos,int ancho_pantalla, Font fuente1){
		
		JLabel titulo3 = new JLabel("Anuales");
		titulo3.setFont(fuente1);
		titulo3.setPreferredSize(new Dimension(ancho_pantalla,40));
		titulo3.setHorizontalAlignment(SwingConstants.CENTER);
		panelDatos.add(titulo3);
		
		new Generar_estadisticas_anual(panelDatos);

		JSeparator separador3 = new JSeparator();
		separador3.setPreferredSize(new Dimension(ancho_pantalla-100,5));separador3.setOrientation(SwingConstants.HORIZONTAL);
		separador3.setBackground(Color.RED);
		panelDatos.add(separador3);
	}
	//GENERA ESTADISTICAS HISTORICAS
	public void generarCuartoGrupo(JPanel panelDatos){
		new Generar_estadisticas_hist(panelDatos);
	}
	
	//ESTABLECE FUNCIONALIDAD DEL BOTON DE ACTUALIZACION
	public class ParaActualizar extends JPanel{
		
		/**
		 * 
		 */
		private static final long serialVersionUID = -2365501056799759498L;
		public ParaActualizar(PanelEstadisticas panel){
			setBackground(new Color(224,224,248));
			SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss a");
			this.setPreferredSize(new Dimension(Toolkit.getDefaultToolkit().getScreenSize().width-20,40));
			setLayout(new FlowLayout(FlowLayout.LEFT));
			
			actualizar = new JButton("Actualizar");
			actualizar.setIcon(new ImageIcon(getClass().getResource("/img/icono_actualizar.png")));
			actualizar.setPreferredSize(new Dimension(150,35));
			actualizar.setAlignmentX(LEFT_ALIGNMENT);
			add(actualizar);
			
			actualizar.addActionListener(new ActionListener(){

				@Override
				public void actionPerformed(ActionEvent arg0) {
					int ancho_pantalla = Toolkit.getDefaultToolkit().getScreenSize().width;
					Font fuente1 = new Font(Font.DIALOG,Font.ITALIC+Font.BOLD,20);
					
					panel.graficas.removeAll();
					panel.graficas=null;
					System.gc();
					
					panel.remove(1);
					panel.graficas = new JPanel();
					panel.graficas.setBackground(new Color(224,224,248));
					panel.graficas.setPreferredSize(new Dimension(ancho_pantalla, 900));
					panel.generarPrimerGrupo(panel.graficas,ancho_pantalla,fuente1);
					panel.generarSegundoGrupo(panel.graficas,ancho_pantalla,fuente1);
					panel.generarTercerGrupo(panel.graficas,ancho_pantalla,fuente1);
					panel.generarCuartoGrupo(panel.graficas);
					panel.add(panel.graficas);
					
					panel.repaint();
					panel.validate();
					
				//	requestFocus();
					ult_actualizacion=new Date();
					ultima_act.setText("Ultima actualización:   "+sdf.format(ult_actualizacion));
				}
				
			});
			
			
			ult_actualizacion=new Date();
			
			ultima_act = new JLabel("Ultima actualización:   "+sdf.format(ult_actualizacion));
			add(ultima_act);
		}

		public JButton actualizar;
		Date ult_actualizacion;
		JLabel ultima_act;
	}
	
	public ParaActualizar paraActualizar;
	Date ult_actualizacion;
	JPanel graficas;
}


