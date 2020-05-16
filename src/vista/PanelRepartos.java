package vista;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;
import javax.swing.table.*;

import controlador.ActualizarModelo;
import controlador.CrearTabla;
import controlador.repartos.*;
import modelo.consultas.EjecutaConsultasReparto;

public class PanelRepartos extends JPanel{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 4407232231678243822L;
	public PanelRepartos(DefaultTableModel modeloTablaLineasPendientes, DefaultTableModel repartosPendientes,String usuario,String tipo){
		
		setBackground(new Color(224,224,248));
		setLayout(null);
		Dimension tamanio_pantalla = Toolkit.getDefaultToolkit().getScreenSize();
		DefaultTableCellRenderer tcr = new DefaultTableCellRenderer();
		tcr.setHorizontalAlignment(SwingConstants.CENTER);
		
		JLabel paraFlecha = new JLabel();
		Image flecha = (new ImageIcon(getClass().getResource("/img/flecha_abajo.png"))).getImage();
		ImageIcon iconoEscalado = new ImageIcon(flecha.getScaledInstance(50, 50, Image.SCALE_SMOOTH));
		paraFlecha.setIcon(iconoEscalado);
		paraFlecha.setBounds(100,265,50,50);
		add(paraFlecha);
		JLabel paraFlecha2 = new JLabel();
		paraFlecha2.setIcon(iconoEscalado);
		paraFlecha2.setBounds(890, 265, 50, 50);
		add(paraFlecha2);
		JLabel paraFlecha3 = new JLabel();
		flecha = (new ImageIcon(getClass().getResource("/img/flecha_derecha.png"))).getImage();
		iconoEscalado= new ImageIcon(flecha.getScaledInstance(50, 50, Image.SCALE_SMOOTH));
		paraFlecha3.setIcon(iconoEscalado);
		paraFlecha3.setBounds(1040, 430, 50, 50);
		add(paraFlecha3);
		JLabel paraFlecha4 = new JLabel();
		flecha = (new ImageIcon(getClass().getResource("/img/flecha_izquierda.png"))).getImage();
		iconoEscalado= new ImageIcon(flecha.getScaledInstance(50, 50, Image.SCALE_SMOOTH));
		paraFlecha4.setIcon(iconoEscalado);
		paraFlecha4.setBounds(1040, 430, 50, 50);
		paraFlecha4.setVisible(false);
		add(paraFlecha4);
		JLabel paraFlecha5 = new JLabel();
		flecha = (new ImageIcon(getClass().getResource("/img/flecha_arriba.png"))).getImage();
		iconoEscalado= new ImageIcon(flecha.getScaledInstance(50, 50, Image.SCALE_SMOOTH));
		paraFlecha5.setIcon(iconoEscalado);
		paraFlecha5.setBounds(100,265,50,50);
		paraFlecha5.setVisible(false);
		add(paraFlecha5);
		JLabel paraFlecha6 = new JLabel();
		paraFlecha6.setIcon(iconoEscalado);
		paraFlecha6.setBounds(890, 265, 50, 50);
		paraFlecha6.setVisible(false);
		add(paraFlecha6);
		
		
	
		JLabel titulo_tabla2 = new JLabel("<html><font size=4>NUEVO REPARTO</font></html>",JLabel.CENTER);
		titulo_tabla2.setBounds(420, 300, 300, 30);
		add(titulo_tabla2);
		
		tablaParaReparto = new JTable();
		String[] columnas = {"N°","PRODUCTO","CANT.","DIRECCIÓN","TURNO","NOMBRE","OBSERVACIONES","FECHA COMPRA"};
		tablaParaReparto.setModel(new DefaultTableModel(null,columnas){
			/**
			 * 
			 */
			private static final long serialVersionUID = 5627607622158860436L;

			public boolean isCellEditable(int rowIndex,int columnIndex){
				return false;
			}
		});
		tablaParaReparto.setRowHeight(20);
		tablaParaReparto.setShowHorizontalLines(false);
		darFormatoATabla(tablaParaReparto);
			
		JScrollPane paraTabla2 = new JScrollPane(tablaParaReparto);
		paraTabla2.setBounds(30,330,980,200);
		paraTabla2.setBorder(BorderFactory.createDashedBorder(Color.BLUE,5,10));
		add(paraTabla2);
		
		if(tipo.equals("administrador")){
		
			//OBTIENE EL CONTENIDO DE LA TABLA DE PENDIENTES
			model = modeloTablaLineasPendientes;
		
			tablaPendientes = new JTable(model);
			tablaPendientes.setRowHeight(20);
			tablaPendientes.setShowHorizontalLines(false);
			darFormatoATabla(tablaPendientes);
		
			JScrollPane paraTabla = new JScrollPane(tablaPendientes);
			paraTabla.setBounds(30,50,980,200);
			paraTabla.setBorder(BorderFactory.createDashedBorder(Color.BLUE,5,10));
			add(paraTabla);
		
			nuevoReparto = true;
		
			JLabel titulo_tabla1 = new JLabel("<html><font size=4>PRODUCTOS PENDIENTES DE REPARTO</font></html>");
			titulo_tabla1.setBounds(350, 20, 300, 30);
			add(titulo_tabla1);
		
			tablaPendientes.addKeyListener(new KeyAdapter(){
				public void keyPressed(KeyEvent e){
					if(e.getKeyChar()==KeyEvent.VK_ENTER){
						if(nuevoReparto==false){
							nuevoReparto=true;
							for(int i=tablaParaReparto.getModel().getRowCount();i>0;i--){
								((DefaultTableModel)tablaParaReparto.getModel()).removeRow(i-1);
							}
						}
						paraFlecha.setVisible(true);
						paraFlecha2.setVisible(true);
						paraFlecha3.setVisible(true);
						paraFlecha4.setVisible(false);
						paraFlecha5.setVisible(false);
						paraFlecha6.setVisible(false);
						titulo_tabla2.setText("<html><font size=4>NUEVO REPARTO</font></html>");
						int linea = tablaPendientes.getSelectedRow();
						Object[] fila_a_copiar = new Object[model.getColumnCount()];
						for(int j=0;j<fila_a_copiar.length;j++){
							fila_a_copiar[j]=model.getValueAt(linea, j);
						}
						((DefaultTableModel)tablaParaReparto.getModel()).addRow(fila_a_copiar);
						model.removeRow(linea);
					
					}
				}
			});
			tablaPendientes.addMouseListener(new MouseAdapter(){
				public void mousePressed(MouseEvent e){
					if(e.getClickCount()==2){
						if(nuevoReparto==false){
							nuevoReparto=true;
							for(int i=tablaParaReparto.getModel().getRowCount();i>0;i--){
								((DefaultTableModel)tablaParaReparto.getModel()).removeRow(i-1);
							}
						}
						paraFlecha.setVisible(true);
						paraFlecha2.setVisible(true);
						paraFlecha3.setVisible(true);
						paraFlecha4.setVisible(false);
						paraFlecha5.setVisible(false);
						paraFlecha6.setVisible(false);
						titulo_tabla2.setText("<html><font size=4>NUEVO REPARTO</font></html>");
						int linea = tablaPendientes.getSelectedRow();
						Object[] fila_a_copiar = new Object[model.getColumnCount()];
						for(int j=0;j<fila_a_copiar.length;j++){
							fila_a_copiar[j]=model.getValueAt(linea, j);
						}
						((DefaultTableModel)tablaParaReparto.getModel()).addRow(fila_a_copiar);
						model.removeRow(linea);
					
					}
				}
			});
		
			tablaParaReparto.addMouseListener(new MouseAdapter(){
				public void mouseClicked(MouseEvent e) {
					if(e.getButton()==MouseEvent.BUTTON3){
						int linea = tablaParaReparto.rowAtPoint(e.getPoint());
						Object[] fila_a_copiar = new Object[tablaParaReparto.getModel().getColumnCount()];
						for(int j=0;j<fila_a_copiar.length;j++){
							fila_a_copiar[j]=tablaParaReparto.getModel().getValueAt(linea, j);
						}
						if(nuevoReparto==false){
							int elegido = JOptionPane.showConfirmDialog(null,"<html><font size=4>¿Está seguro que desea eliminar la linea de reparto seleccionada?</font></html>",
									"Eliminar Línea", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
							if(elegido==JOptionPane.YES_OPTION){
								EjecutaConsultasReparto paraLiberarLineas = new EjecutaConsultasReparto();
								boolean ok = paraLiberarLineas.liberarLineaReparto((int)fila_a_copiar[0]);
								if(ok){
									model.addRow(fila_a_copiar);
									((DefaultTableModel)tablaParaReparto.getModel()).removeRow(linea);
								}else{
									JOptionPane.showMessageDialog(null,"<html><font size=4>¡No se pudo desasignar la linea de Reparto. Intente nuevamente.</font></html>",
											"Eliminar Línea",JOptionPane.WARNING_MESSAGE);
								}
							}
						}else{
							model.addRow(fila_a_copiar);
							((DefaultTableModel)tablaParaReparto.getModel()).removeRow(linea);
						}
					}	
				}
			});
		
		}
		
		tablaRepartosPendientes = new JTable(repartosPendientes);
		tablaRepartosPendientes.setRowHeight(20);
		tablaRepartosPendientes.setShowHorizontalLines(false);
		TableColumn columnaCodigo = tablaRepartosPendientes.getColumnModel().getColumn(0);
		columnaCodigo.setMaxWidth(70);
		columnaCodigo.setCellRenderer(tcr);
		tablaRepartosPendientes.getColumnModel().getColumn(1).setCellRenderer(tcr);
		
		tablaRepartosPendientes.addMouseListener(new MouseAdapter(){
			public void mouseClicked(MouseEvent e) {
				if(e.getButton()==MouseEvent.BUTTON1){
					if(nuevoReparto==true){
						nuevoReparto=false;
						for(int i=tablaParaReparto.getModel().getRowCount();i>0;i--){
							Object[] fila_a_copiar = new Object[tablaParaReparto.getModel().getColumnCount()];
							for(int j=0;j<fila_a_copiar.length;j++){
								fila_a_copiar[j]=tablaParaReparto.getModel().getValueAt(i-1, j);
							}
							model.addRow(fila_a_copiar);
							((DefaultTableModel)tablaParaReparto.getModel()).removeRow(i-1);
							
						}
					}
					
					int linea = tablaRepartosPendientes.rowAtPoint(e.getPoint());
					int n_reparto = (int)tablaRepartosPendientes.getValueAt(linea, 0);
					titulo_tabla2.setText("<html><font size=4>INFORMACIÓN DE REPARTO</font></html>");
					if(tipo.equals("administrador")){
						paraFlecha.setVisible(false);
						paraFlecha2.setVisible(false);
						paraFlecha3.setVisible(false);
						paraFlecha4.setVisible(true);
						paraFlecha5.setVisible(true);
						paraFlecha6.setVisible(true);
					}
					CrearTabla nueva = new CrearTabla();
					DefaultTableModel modelo = nueva.obtenerTablaLineasReparto(n_reparto);
					tablaParaReparto.setModel(modelo);
					darFormatoATabla(tablaParaReparto);
				}
			}
		});
		JScrollPane paraTabla3 = new JScrollPane(tablaRepartosPendientes);
		paraTabla3.setBounds(1130,330,200,200);
		paraTabla3.setBorder(BorderFactory.createDashedBorder(Color.BLUE,5,10));
		add(paraTabla3);
		
		Runnable r = new ActualizarModelo(tablaRepartosPendientes,this,"repartos");
		Thread t = new Thread(r);
		t.start();	
		
		eliminarLinea = new JButton("<html>Eliminar<br>Producto</html>");
		eliminarLinea.setBounds(1030, 130, 80, 40);
		eliminarLinea.addActionListener(new EliminarLineaReparto(tablaPendientes,usuario));
		add(eliminarLinea);
		
		crearReparto = new JButton("<html>Crear<br>Reparto</html>");
		crearReparto.setBounds(1030, 380, 80, 40);
		crearReparto.addActionListener(new CrearReparto(tablaParaReparto,tablaRepartosPendientes,titulo_tabla2));
		add(crearReparto);
		
		crearReparto = new JButton("<html>Crear<br>Reparto</html>");
		crearReparto.setBounds(1030, 380, 80, 40);
		crearReparto.addActionListener(new CrearReparto(tablaParaReparto,tablaRepartosPendientes,titulo_tabla2));
		add(crearReparto);
		
		imprimirReparto = new JButton("<html>Imprimir&nbsp;&nbsp;<br>Reparto</html>");
		imprimirReparto.setBounds(1165,220,130,40);
		imprimirReparto.addActionListener(new ImprimirHojaReparto(tablaRepartosPendientes,tablaParaReparto));
		imprimirReparto.setIcon(new ImageIcon(getClass().getResource("/img/imprimir.png")));
		add(imprimirReparto);
		
		confirmarReparto = new JButton("<html>Confirmar<br>Reparto</html>");
		confirmarReparto.setBounds(1165, 270, 130, 40);
		confirmarReparto.addActionListener(new ConfirmarReparto(tablaRepartosPendientes,tablaParaReparto,usuario));
		confirmarReparto.setIcon(new ImageIcon(getClass().getResource("/img/ok.png")));
		add(confirmarReparto);
		
		
		if(tipo.equals("normal")){
			
			paraFlecha.setVisible(false);
			paraFlecha2.setVisible(false);
			paraFlecha3.setVisible(false);
			eliminarLinea.setVisible(false);
			crearReparto.setVisible(false);
			
			titulo_tabla2.setText("<html><font size=4>INFORMACIÓN DE REPARTO</font></html>");
			paraTabla3.setLocation((tamanio_pantalla.width/2)-100, 50);
			paraTabla2.setLocation((tamanio_pantalla.width/2)-490, 330);
			titulo_tabla2.setLocation((tamanio_pantalla.width/2)-150, 295);
			
			confirmarReparto.setLocation(paraTabla3.getLocation().x + 250, paraTabla3.getLocation().y + 100);
			imprimirReparto.setLocation(paraTabla3.getLocation().x + 250, paraTabla3.getLocation().y + 50);
			JLabel titulo_tabla3 = new JLabel("<html><font size=5>REPARTOS PENDIENTES</font></html>",JLabel.CENTER);
			titulo_tabla3.setBounds(paraTabla3.getLocation().x - 280, paraTabla3.getLocation().y + 70, 250, 50);
			add(titulo_tabla3);
		}
		
	}
	
	public void darFormatoATabla(JTable tabla){
		TableColumnModel modeloColumnas = tabla.getColumnModel();
		
		DefaultTableCellRenderer tcr = new DefaultTableCellRenderer();
		tcr.setHorizontalAlignment(SwingConstants.CENTER);
		
		TableColumn num_linea = modeloColumnas.getColumn(0);
		num_linea.setMinWidth(50);
		num_linea.setMaxWidth(50);
		num_linea.setCellRenderer(tcr);
		
		TableColumn producto = modeloColumnas.getColumn(1);
		producto.setMinWidth(200);
		producto.setMaxWidth(200);
		
		TableColumn cantidad = modeloColumnas.getColumn(2);
		cantidad.setMinWidth(50);
		cantidad.setMaxWidth(50);
		cantidad.setCellRenderer(tcr);
		
		TableColumn direccion = modeloColumnas.getColumn(3);
		direccion.setMinWidth(150);
		direccion.setMaxWidth(150);
		direccion.setCellRenderer(tcr);
		
		TableColumn turno = modeloColumnas.getColumn(4);
		turno.setMinWidth(70);
		turno.setMaxWidth(70);
		turno.setCellRenderer(tcr);
		
		TableColumn nombre = modeloColumnas.getColumn(5);
		nombre.setMinWidth(100);
		nombre.setMaxWidth(100);
		nombre.setCellRenderer(tcr);
		
		TableColumn observ = modeloColumnas.getColumn(6);
		observ.setMinWidth(210);
		observ.setMaxWidth(210);
		observ.setCellRenderer(tcr);
		
		TableColumn fecha = modeloColumnas.getColumn(7);
		fecha.setMaxWidth(150);
		fecha.setMinWidth(150);
		fecha.setCellRenderer(tcr);
		
		
	}

	String usuario;
	boolean nuevoReparto;
	DefaultTableModel model;
	public JTable tablaPendientes,tablaParaReparto,tablaRepartosPendientes;
	public JButton eliminarLinea,crearReparto,confirmarReparto, imprimirReparto;
}
