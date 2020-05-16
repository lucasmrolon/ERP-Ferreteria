package controlador.detalle;

import java.awt.Color;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;
import javax.swing.table.*;

import vista.detalle.PanelDetalle;

//CLASE QUE HACE EDITABLE LA FILA SELECCIONADA Y DA AL USUARIO LA POSIBILIDAD DE GUARDAR LOS CAMBIOS
public class ModificarRegistro implements ActionListener{

	public ModificarRegistro(JComboBox<String>  tipoTabla,JTable tabla){
		this.tabla = tabla;
		this.tipoTabla=tipoTabla;
	}
	
	@Override
	public void actionPerformed(ActionEvent arg0) {
		
		//OBTIENE TABLA Y FILA SELECCIONADA
		String tabla_seleccionada = (String)tipoTabla.getSelectedItem();
		
		DefaultTableModel modelo = (DefaultTableModel)tabla.getModel();
		JScrollPane contenedor = (JScrollPane)tabla.getParent().getParent();
		PanelDetalle panel = (PanelDetalle)contenedor.getParent();
		
		seleccionado = tabla.getSelectedRow();
		panel.seleccionado=seleccionado;
		DefaultTableCellRenderer tcr = formatoTabla();
		
		if(seleccionado!=-1){
		
			//CAMBIA ESTADO DE LOS BOTONES Y OBTIENE LOS VALORES ALMACENADOS EN LA TABLA 
			panel.modif.setEnabled(false);
			panel.actualizar.setVisible(false);
			panel.aniadir.setVisible(false);
			panel.elim.setVisible(false);
			panel.abrir_ctacte.setVisible(false);
			panel.cerrar_ctacte.setVisible(false);
			
			int n_filas = tabla.getRowCount();
			int n_columnas = tabla.getColumnCount();
		
			panel.fila_auxiliar = new Object[n_columnas];
			
			int cancelado=0;
			
			Object[][] datos = new Object[n_filas][n_columnas];
			for(int i=0;i<n_filas;i++){
				for(int j=0;j<n_columnas;j++){
					datos[i][j]=modelo.getValueAt(i, j);
					if(i==seleccionado){
						panel.fila_auxiliar[j] = modelo.getValueAt(i, j);
					}
				}
			}
			Object[] columnas = new Object[n_columnas];
			for(int i=0;i<n_columnas;i++){
				columnas[i]=modelo.getColumnName(i);
			}
			
			FormateaTablas paraFormatear = new FormateaTablas();
		
			//SI SELECCIONO LA TABLA DE CLIENTES
			if(tabla_seleccionada.equals("CLIENTES")){
			
				modeloNuevo = new DefaultTableModel(datos,columnas){
					/**
					 * 
					 */
					private static final long serialVersionUID = 5522693835571334120L;

					public boolean isCellEditable(int rowIndex,int columnIndex){
						if((rowIndex==seleccionado)&&(columnIndex!=0)&&(columnIndex!=7)&&(columnIndex!=6)){
							return true;
						}
						else return false;
					}
				};
				tabla.setModel(modeloNuevo);
				
				paraFormatear.formatearColumnas(tabla, "clientes");
		
			}
			//SI SELECCIONÓ LA TABLA DE RUBROS
			else if(tabla_seleccionada.equals("RUBROS")){
				
				modeloNuevo = new DefaultTableModel(datos,columnas){
					/**
					 * 
					 */
					private static final long serialVersionUID = -1049088422519030254L;

					public boolean isCellEditable(int rowIndex,int columnIndex){
						if((rowIndex==seleccionado)&&(columnIndex!=0)){
							return true;
						}
						else return false;
					}
				};
				tabla.setModel(modeloNuevo);
		
				paraFormatear.formatearColumnas(tabla, "rubros");
		

			}
			//SI SELECCIONÓ LA TABLA DE PROVEEDORES
			else if(tabla_seleccionada.equals("PROVEEDORES")){
				
				modeloNuevo = new DefaultTableModel(datos,columnas){
					/**
					 * 
					 */
					private static final long serialVersionUID = -8582996301533177136L;

					public boolean isCellEditable(int rowIndex,int columnIndex){
						if((rowIndex==seleccionado)&&(columnIndex!=0)){
							return true;
						}
						else return false;
					}
				};
				tabla.setModel(modeloNuevo);
		
				paraFormatear.formatearColumnas(tabla, "proveedores");
		

			}
			//SI SELECCIONÓ LA TABLA DE EMPLEADOS
			else if(tabla_seleccionada.equals("EMPLEADOS")){
				
				if(seleccionado!=0){
					JComboBox<String> paraTipo = new JComboBox<String>();
					paraTipo.addItem("administrador");
					paraTipo.addItem("normal");
				
					modeloNuevo = new DefaultTableModel(datos,columnas){
						/**
						 * 
						 */
						private static final long serialVersionUID = -7412321882272882639L;

						public boolean isCellEditable(int rowIndex,int columnIndex){
							if((rowIndex==seleccionado)&&(columnIndex!=0)){
								return true;
							}
							else return false;
						}
						
					};
					tabla.setModel(modeloNuevo);
				
					tabla.getColumnModel().getColumn(1).setCellEditor(new DefaultCellEditor(paraTipo));
		
					paraFormatear.formatearColumnas(tabla, "empleados");
				}else{
					
					String mensaje = "<html><Font size=5>¡No está permitido modificar el usuario 'admin'!</Font></html>";
					JOptionPane.showMessageDialog(null, mensaje, "¡Error!", JOptionPane.WARNING_MESSAGE);
					panel.cancelar.doClick();
					cancelado=1;
				}
			}
			
			//UBICA Y MUESTRA LOS BOTONES DE GUARDAR Y CANCELAR
			tabla.changeSelection(seleccionado, 0, false, false);
			
			int x = contenedor.getLocation().x+contenedor.getSize().width;
			int y = contenedor.getLocation().y+tabla.getRowHeight()*seleccionado+21;
			
			if(tabla_seleccionada.equals("RUBROS")){
				panel.guardar.setLocation(x, y);
				panel.cancelar.setLocation(x+105,y);
			}else{
				panel.cancelar.setLocation(contenedor.getLocation().x+contenedor.getWidth()-100,contenedor.getLocation().y+contenedor.getHeight()+20);
				panel.guardar.setLocation(panel.cancelar.getLocation().x-110, contenedor.getLocation().y+contenedor.getHeight()+20);
			}
			if(cancelado==0){
				for(int i=0;i<n_columnas;i++){
					tabla.getColumnModel().getColumn(i).setCellRenderer(tcr);
				}
				panel.guardar.setVisible(true);
				panel.cancelar.setVisible(true);
				
			}	
			panel.repaint();
			panel.revalidate();
		}else{
			String mensaje=null;
			
			//SI NO SE SELECCIONÓ NINGUNA FILA, MUESTRA MENSAJE DE ERROR, DEPENDIENDO DE LA TABLA
			if(tabla_seleccionada.equals("CLIENTES")){
				mensaje = "<html><Font size=5>¡No ha seleccionado ningún cliente!</Font></html>";
			}
			else if(tabla_seleccionada.equals("RUBROS")){
				mensaje = "<html><Font size=5>¡No ha seleccionado ningún rubro!</Font></html>";
			}
			else if(tabla_seleccionada.equals("PROVEEDORES")){
				mensaje = "<html><Font size=5>¡No ha seleccionado ningún proveedor!</Font></html>";
			}
			else if(tabla_seleccionada.equals("EMPLEADOS")){
				mensaje = "<html><Font size=5>¡No ha seleccionado ningún empleado!</Font></html>";
			}
			JOptionPane.showMessageDialog(null, mensaje, "¡Error!", JOptionPane.WARNING_MESSAGE);
		}
		
		
	}
	
	//MÉTODO PARA FORMATEAR LAS TABLAS
	private DefaultTableCellRenderer formatoTabla(){
		
		DefaultTableCellRenderer dtcr = new DefaultTableCellRenderer(){
			
			/**
			 * 
			 */
			private static final long serialVersionUID = -3247820577011293712L;

			public Component getTableCellRendererComponent(JTable table,Object value, boolean isSelected, boolean hasFocus, int row, int column){
							
				//CENTRA DATOS EN SUS COLUMNAS
				setHorizontalAlignment(SwingConstants.CENTER);
				
				super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
				if(isSelected){
					setBackground(Color.WHITE);
				}
				if(row==seleccionado){
					setBackground(new Color(160,160,233));
					this.setEnabled(true);
				}else{
					setBackground(Color.WHITE);
					this.setEnabled(false);
				}
				return this;
			}
		};
		return dtcr;
	}
	
	int seleccionado;
	JComboBox<String> tipoTabla;
	JTable tabla;
	DefaultTableModel modeloNuevo;
}
