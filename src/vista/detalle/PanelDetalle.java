package vista.detalle;

import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;


import controlador.detalle.*;

//PANEL QUE PERMITE MOSTRAR TABLAS DETALLADAS
public class PanelDetalle extends JPanel{
	/**
	 * 
	 */
	private static final long serialVersionUID = -6410961042228821223L;
	public PanelDetalle(){
		
		setBackground(new Color(224,224,248));
		setLayout(null);
		Dimension tamanio_pantalla = Toolkit.getDefaultToolkit().getScreenSize();
		Font fuente = new Font(Font.MONOSPACED,Font.PLAIN,18);
		
		JLabel instrucciones = new JLabel("Seleccione la tabla que desea consultar:");
		instrucciones.setBounds((tamanio_pantalla.width/2)-335, 50, 500, 50);
		instrucciones.setFont(fuente);
		add(instrucciones);
		
		//COMBOBOX PARA SELECCIONAR TABLA
		String[] elementos = {"VENTAS","FORMAS DE PAGO","REPARTOS","CLIENTES","EMPLEADOS","RUBROS","PROVEEDORES","PEDIDOS"};
		tipoBusqueda = new JComboBox<String>(elementos);
		tipoBusqueda.setSelectedIndex(-1);
		tipoBusqueda.setFont(fuente);
		tipoBusqueda.setSize(200, 40);
		tipoBusqueda.setLocation((tamanio_pantalla.width/2)+135, 50);
		add(tipoBusqueda);
		
		tablaResultado = new JTable();
		tablaResultado.setRowHeight(30);
		JScrollPane paraMostrarTabla = new JScrollPane(tablaResultado);
		add(paraMostrarTabla);
		
		DefaultTableCellRenderer tcr = formatoTabla();
		
		tipoBusqueda.addActionListener(new ObtenerTablaDetalle(this,tablaResultado,tcr));
		
		//BOTON PARA IMPRIMIR REPORTE DE VENTA ANUAL
		imprimir_informe_venta_anual = new JButton("Generar reporte anual");
		imprimir_informe_venta_anual.setSize(200, 30);
		imprimir_informe_venta_anual.setIcon(new ImageIcon(getClass().getResource("/img/imprimir.png")));
		imprimir_informe_venta_anual.addActionListener(new Generar_reporte_anual());
		imprimir_informe_venta_anual.setVisible(false);
		add(imprimir_informe_venta_anual);
		
		//BOTON PARA IMPRIMIR REPORTE DE VENTA POR PERIODO
		imprimir_reporte_venta = new JButton("Generar reporte");
		imprimir_reporte_venta.setSize(200, 30);
		imprimir_reporte_venta.setIcon(new ImageIcon(getClass().getResource("/img/imprimir.png")));
		imprimir_reporte_venta.addActionListener(new Generar_reporte_ventas());
		imprimir_reporte_venta.setVisible(false);
		add(imprimir_reporte_venta);
		
		//BOTON PARA ACTUALIZAR ESTADO DE CUENTA CORRIENTE O LISTA DE PRECIOS DE PROVEEDOR
		actualizar = new JButton("Abonar Cuenta Corriente");
		actualizar.setSize(200, 30);
		actualizar.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				try{
					if(actualizar.getText().equals("Abonar Cuenta Corriente")){
						int cuenta_seleccionada = tablaResultado.getSelectedRow();
						new VentanaAbonarCuenta(cuenta_seleccionada,tablaResultado);
					}else if(actualizar.getText().equals("Actualizar lista de precios")){
						int proveedor_seleccionado = tablaResultado.getSelectedRow();
						new ActualizarListaPrecios(proveedor_seleccionado,tablaResultado);
					}
				}catch(ArrayIndexOutOfBoundsException ex){
					String mensaje = "<html><Font size=5>¡Debe seleccionar un Cliente!</Font></html>";
					JOptionPane.showMessageDialog(null, mensaje, "¡Error!", JOptionPane.WARNING_MESSAGE);
				}
			}
		});
		actualizar.setVisible(false);
		add(actualizar);
		
		//BOTON PARA AÑADIR REGISTROS
		aniadir= new JButton("Crear Cuenta");
		aniadir.setSize(150, 30);
		aniadir.addActionListener(new CrearRegistro(tipoBusqueda,tablaResultado));
		aniadir.setVisible(false);
		add(aniadir);
		
		//BOTON PARA MODIFICAR REGISTROS
		modif = new JButton("Modificar Cuenta");
		modif.setSize(150, 30);
		modif.addActionListener(new ModificarRegistro(tipoBusqueda,tablaResultado));
		modif.setVisible(false);
		add(modif);
		
		//BOTON PARA ELIMINAR REGISTROS
		elim = new JButton("Eliminar Cuenta");
		elim.setSize(150, 30);
		elim.addActionListener(new EliminarRegistro(tipoBusqueda,tablaResultado));
		elim.setVisible(false);
		add(elim);
		
		GuardarCambiosEnBD para_guardar = new GuardarCambiosEnBD(this,tipoBusqueda,tablaResultado);
		CancelarCambios para_cancelar = new CancelarCambios(this,tipoBusqueda,tablaResultado,tcr);
		
		//BOTON PARA GUARDAR CAMBIOS
		guardar = new JButton("Guardar");
		guardar.setSize(100, 30);
		guardar.addActionListener(para_guardar);
		guardar.setVisible(false);
		add(guardar);
		
		guardarcambios = new JButton("Guardar Cambios");
		guardarcambios.setSize(150, 30);
		guardarcambios.addActionListener(para_guardar);
		guardarcambios.setVisible(false);
		add(guardarcambios);
		
		//BOTON PARA CANCELAR EDICION DE TABLAS
		cancelar = new JButton("Cancelar");
		cancelar.setSize(100, 30);
		cancelar.addActionListener(para_cancelar);
		cancelar.setVisible(false);
		add(cancelar);
		
		//BOTON PARA ABRIR UNA NUEVA CUENTA CORRIENTE
		abrir_ctacte = new JButton("Abrir Cuenta Corriente");
		abrir_ctacte.setSize(200,30);
		abrir_ctacte.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				int cliente_seleccionado = tablaResultado.getSelectedRow();
				if(cliente_seleccionado!=-1){
					if(tablaResultado.getValueAt(cliente_seleccionado,6)==""){
						new VentanaAbrirCuenta(cliente_seleccionado,tablaResultado);
					}else{
						String mensaje = "<html><Font size=5>El Cliente seleccionado ya posee una Cuenta Corriente.</Font></html>";
						JOptionPane.showMessageDialog(null, mensaje, "¡Error!", JOptionPane.WARNING_MESSAGE);
					}
				}
				else{
					String mensaje = "<html><Font size=5>¡Debe seleccionar un Cliente!</Font></html>";
					JOptionPane.showMessageDialog(null, mensaje, "¡Error!", JOptionPane.WARNING_MESSAGE);
				}
			}
		});
		abrir_ctacte.setVisible(false);
		add(abrir_ctacte);
		
		//BOTON PARA CERRAR UNA CUENTA CORRIENTE
		cerrar_ctacte = new JButton("Cerrar Cuenta Corriente");
		cerrar_ctacte.setSize(200,30);
		cerrar_ctacte.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				try{
					int cliente_seleccionado = tablaResultado.getSelectedRow();
					new CerrarCuenta(cliente_seleccionado,tablaResultado);
				}catch(ArrayIndexOutOfBoundsException ex){
					String mensaje = "<html><Font size=5>¡Debe seleccionar un cliente!</Font></html>";
					JOptionPane.showMessageDialog(null, mensaje, "¡Error!", JOptionPane.WARNING_MESSAGE);
				}
			}
		});
		cerrar_ctacte.setVisible(false);
		add(cerrar_ctacte);
		
		//ESTABLECE TECLA ESCAPE PARA CANCELAR CAMBIOS
		ActionMap mapaAccion = getActionMap();
		InputMap mapa = getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
		
		KeyStroke escape = KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE,0);

		
		mapa.put(escape, "escape");
		mapaAccion.put("escape", new AbstractAction(){
			/**
			 * 
			 */
			private static final long serialVersionUID = 8965082529417400405L;

			@Override
			public void actionPerformed(ActionEvent arg0) {
				cancelar.doClick();
			}
		});
		
		//BOTON PARA CREAR UNA COPIA DE SEGURIDAD DE LA BASE DE DATOS
		copia_seguridad = new JButton("Crear copia de respaldo");
		copia_seguridad.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				try {
				      
				      
				      JFileChooser paraGuardar = new JFileChooser();
				      paraGuardar.setApproveButtonText("Guardar");
				      paraGuardar.setDialogType(JFileChooser.SAVE_DIALOG);
				      SimpleDateFormat sdf = new SimpleDateFormat("dd_MM_yyyy__hh_mm_ss_a");
				      paraGuardar.setSelectedFile(new File("copia_de_seguridad_"+sdf.format(new Date())+".sql"));
				     
				      int seleccion = paraGuardar.showSaveDialog(null);
				      File a_guardar;
				      String ruta;
				      byte[] buffer = new byte[1000];
				      if(seleccion==JFileChooser.APPROVE_OPTION){
				    	  Process p = Runtime
						            .getRuntime()
						            .exec("C:/wwamp64/bin/mysql/mysql5.7.14/bin/mysqldump -u root mydb");
						      InputStream is = p.getInputStream();
				    	  a_guardar=paraGuardar.getSelectedFile();
				    	  ruta=a_guardar.getAbsolutePath();
				    	  
				    	  FileOutputStream fos = new FileOutputStream(ruta);
				    	  int leido = is.read(buffer);
					      
					      while (leido > 0) {
					         fos.write(buffer, 0, leido);
					         leido = is.read(buffer);
					      }
					      fos.close();
				      }
				      
				   } catch (IOException e) {
					   String mensaje = "<html><Font size=5>¡Este equipo no está autorizado para realizar la operación seleccionada!</Font></html>";
						JOptionPane.showMessageDialog(null, mensaje, "¡Error!", JOptionPane.WARNING_MESSAGE);
				   
				   } catch (Exception e) {
				      e.printStackTrace();
				   }
				
			}
			
		});
		copia_seguridad.setBounds(1155, 550, 200, 30);
		add(copia_seguridad);
		
		
	}
	
	//DA FORMATO A LAS TABLAS
	private DefaultTableCellRenderer formatoTabla(){
		
		DefaultTableCellRenderer dtcr = new DefaultTableCellRenderer(){
			
			/**
			 * 
			 */
			private static final long serialVersionUID = 484853710934038357L;

			public Component getTableCellRendererComponent(JTable table,Object value, boolean isSelected, boolean hasFocus, int row, int column){
							
				//CENTRA DATOS EN SUS COLUMNAS
				setHorizontalAlignment(SwingConstants.CENTER);
				setBackground(Color.WHITE);
				super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
				if(isSelected){
					setBackground(new Color(160,160,233));
				}
				return this;
			}
		};
		return dtcr;
	}
	public JButton imprimir_informe_venta_anual,imprimir_reporte_venta;
	public JButton actualizar,aniadir,modif,elim,abrir_ctacte,cerrar_ctacte,copia_seguridad;
	public JButton guardar,guardarcambios,cancelar;
	public JComboBox<String> tipoBusqueda;
	public JTable tablaResultado;
	public Object[] fila_auxiliar;
	public int seleccionado;
}




