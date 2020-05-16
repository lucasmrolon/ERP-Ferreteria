package controlador.detalle;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;

import modelo.consultas.EjecutaConsultas;
import modelo.consultas.EjecutaConsultasCtasCtes;
import modelo.consultas.EjecutaConsultasEmpleado;
import vista.detalle.PanelDetalle;

//CLASE QUE PERMITE AÑADIR REGISTROS A LAS RESPECTIVAS TABLAS DE DATOS
public class CrearRegistro implements ActionListener{

	//CONSTRUCTOR
	public CrearRegistro(JComboBox<String> tipoTabla, JTable tablaCuentas){
		tabla = tablaCuentas;
		this.tipoTabla = tipoTabla;
	}
	
	@Override
	public void actionPerformed(ActionEvent arg0) {
	
		//OBTIENE LA FILA SELECCIONADA
		String tabla_seleccionada = (String)tipoTabla.getSelectedItem();
		
		//CREA UN NUEVO MODELO PARA SUSTITUIR EL ANTERIOR
		DefaultTableModel modelo = (DefaultTableModel)tabla.getModel();
		int n_filas_anterior = modelo.getRowCount();
		JScrollPane contenedor = (JScrollPane)tabla.getParent().getParent();
		PanelDetalle panel = (PanelDetalle)contenedor.getParent();
		
		//SI LA TABLA TIENE MENOS DE 10 FILAS, AUMENTA SU ALTURA EN 1 FILA
		if(tabla.getRowCount()<10){
			contenedor.setSize(contenedor.getSize().width, contenedor.getSize().height+tabla.getRowHeight());
			panel.actualizar.setLocation(panel.actualizar.getLocation().x, panel.actualizar.getLocation().y+tabla.getRowHeight());
			panel.abrir_ctacte.setLocation(panel.abrir_ctacte.getLocation().x, panel.abrir_ctacte.getLocation().y+tabla.getRowHeight());
			panel.cerrar_ctacte.setLocation(panel.cerrar_ctacte.getLocation().x, panel.cerrar_ctacte.getLocation().y+tabla.getRowHeight());
			panel.aniadir.setLocation(panel.aniadir.getLocation().x, panel.aniadir.getLocation().y+tabla.getRowHeight());
			panel.modif.setLocation(panel.modif.getLocation().x, panel.modif.getLocation().y+tabla.getRowHeight());
			panel.elim.setLocation(panel.elim.getLocation().x, panel.elim.getLocation().y+tabla.getRowHeight());
		}
		
		//PREPARA LA TABLA PARA SU EDICION
		panel.aniadir.setEnabled(false);
		panel.elim.setVisible(false);
		panel.modif.setVisible(false);
		panel.actualizar.setVisible(false);
		panel.abrir_ctacte.setVisible(false);
		panel.cerrar_ctacte.setVisible(false);
		
		TableCellRenderer tcr = tabla.getColumnModel().getColumn(1).getCellRenderer();
		
		//OBTIENE NUMERO DE FILAS Y COLUMNAS
		int n_filas = modelo.getRowCount();
		int n_columnas = modelo.getColumnCount();
		
		//OBTIENE LOS DATOS DEL MODELO ANTERIOR
		Object[][] datos = new Object[n_filas][n_columnas];
		for(int i=0;i<n_filas;i++){
			for(int j=0;j<n_columnas;j++){
				datos[i][j]=modelo.getValueAt(i, j);
			}
		}
		Object[] columnas = new Object[n_columnas];
		for(int i=0;i<n_columnas;i++){
			columnas[i]=modelo.getColumnName(i);
		}
		
		//SI SELECCIONÓ LA TABLA DE CUENTAS CORRIENTES...
		if(tabla_seleccionada.equals("CLIENTES")){
			
			//CREA UN NUEVO MODELO CON LA ÚLTIMA FILA EDITABLE, EXCEPTO LA PRIMER Y ÚLTIMA COLUMNA
			modeloNuevo = new DefaultTableModel(datos,columnas){
				/**
				 * 
				 */
				private static final long serialVersionUID = -6200829894858231022L;

				public boolean isCellEditable(int rowIndex,int columnIndex){
					if((rowIndex==modelo.getRowCount())&&(columnIndex!=7)&&columnIndex!=6){
						return true;
					}
					else return false;
				}
			};
			
			//CREA UNA FILA VACÍA Y LE APLICA FORMATO
			Object[] fila ={"","","","","","","",""};
			modeloNuevo.addRow(fila);
			
			modeloNuevo.addTableModelListener(new TableModelListener(){
				@Override
				public void tableChanged(TableModelEvent e) {
					if(e.getType() == TableModelEvent.UPDATE){
						int fila = e.getFirstRow();
						int columna = e.getColumn();
						if(fila==modeloNuevo.getRowCount()-1 && columna == 0){
							
							String dni_ingresado = (String)modeloNuevo.getValueAt(fila, columna);
							
							if(!dni_ingresado.trim().equals("")){
								EjecutaConsultasCtasCtes paraConsultas = new EjecutaConsultasCtasCtes();

								Object[] cliente = paraConsultas.obtenerDatosCliente(dni_ingresado);
														
								if(cliente[0]!=null && (int)cliente[6]==1){
									Object[] opciones = {"Recuperar","Descartar"}; 
									int elegido=JOptionPane.showOptionDialog(null, "<html><font size=4>¡El cliente ya ha sido registrado en el pasado!¿Desea recuperar los datos guardados o descartarlos?</font></html>",
											"Aviso",JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE, null, opciones, opciones[0]);
									if(elegido==0){
										modeloNuevo.setValueAt(cliente[1], fila, 1);
										modeloNuevo.setValueAt(cliente[2], fila, 2);
										modeloNuevo.setValueAt(cliente[3], fila, 3);
										modeloNuevo.setValueAt(cliente[4], fila, 4);
										modeloNuevo.setValueAt(cliente[5], fila, 5);
									}
								}
							}
						}
					}
				}
			});
			
			tabla.setModel(modeloNuevo);
		
			FormateaTablas paraFormatear = new FormateaTablas();
			paraFormatear.formatearColumnas(tabla, "clientes");
		
			for(int i=0;i<n_columnas;i++){
				tabla.getColumnModel().getColumn(i).setCellRenderer(tcr);
			}
		
			
		}
	
		//SI SELECCIONÓ LA TABLA DE RUBROS
		else if(tabla_seleccionada.equals("RUBROS")){
		
			//CREA UN MODELO NUEVO CON LA ULTIMA FILA EDITABLE, EXCEPTO LA PRIMER COLUMNA
			modeloNuevo = new DefaultTableModel(datos,columnas){
				/**
				 * 
				 */
				private static final long serialVersionUID = 5925815321275273333L;

				public boolean isCellEditable(int rowIndex,int columnIndex){
					if((rowIndex==modelo.getRowCount())&&(columnIndex!=0)){
						return true;
					}
					else return false;
				}
			};
			
			//CREA UNA FILA VACÍA PARA SER EDITADA Y LE APLICA FORMATO
			
			EjecutaConsultas paraObtenerDatos = new EjecutaConsultas();
			
			int n_rubros = paraObtenerDatos.consultaRubrosAlmacenados();
			
			Object[] fila ={n_rubros+1,""};
			modeloNuevo.addRow(fila);
			tabla.setModel(modeloNuevo);
			
			FormateaTablas paraFormatear = new FormateaTablas();
			paraFormatear.formatearColumnas(tabla, "rubros");
		
			for(int i=0;i<n_columnas;i++){
				tabla.getColumnModel().getColumn(i).setCellRenderer(tcr);
			}
		
		}
		
		//SI SELECCIONÓ LA TABLA DE PROVEEDORES...
		else if(tabla_seleccionada.equals("PROVEEDORES")){
			
			//CREA UN MODELO NUEVO CON LA ULTIMA FILA EDITABLE, EXCEPTO LA ÚLTIMA COLUMNA
			modeloNuevo = new DefaultTableModel(datos,columnas){
				/**
				 * 
				 */
				private static final long serialVersionUID = -5756100822007940127L;

				public boolean isCellEditable(int rowIndex,int columnIndex){
					if((rowIndex==modelo.getRowCount())&&(columnIndex!=5)){
						return true;
					}
					else return false;
				}
			};
			
			//CREA UNA FILA VACÍA, LISTA PARA SER EDITADA Y LE APLICA FORMATO
			Object[] fila ={"","","","","",""};
			modeloNuevo.addRow(fila);
			tabla.setModel(modeloNuevo);
		
			FormateaTablas paraFormatear = new FormateaTablas();
			paraFormatear.formatearColumnas(tabla, "proveedores");
		
			for(int i=0;i<n_columnas;i++){
				tabla.getColumnModel().getColumn(i).setCellRenderer(tcr);
			}
		
		}
		
		//SI SELECCIONÓ LA TABLA DE EMPLEADOS...
		else if(tabla_seleccionada.equals("EMPLEADOS")){
			
			//CREA UN MODELO NUEVO CON LA ÚLTIMA FILA EDITABLE, EXCEPTO LA PRIMER COLUMNA
			modeloNuevo = new DefaultTableModel(datos,columnas){
				/**
				 * 
				 */
				private static final long serialVersionUID = 3381910709255882680L;

				public boolean isCellEditable(int rowIndex,int columnIndex){
					if((rowIndex==modelo.getRowCount())&&(columnIndex!=0)){
						return true;
					}
					else return false;
				}
			};
			
			//CREA UNA FILA VACÍA LISTA PARA SER EDITADA Y LE APLICA FORMATO
			JComboBox<String> paraTipo = new JComboBox<String>();
			paraTipo.addItem("administrador");
			paraTipo.addItem("normal");
			
			EjecutaConsultasEmpleado paraObtenerDatos = new EjecutaConsultasEmpleado();
			
			int n_empleados = paraObtenerDatos.obtener_n_empleados();
			
			Object[] fila ={n_empleados+1,"","","","","","","","",""};
			modeloNuevo.addRow(fila);
			tabla.setModel(modeloNuevo);
		
			tabla.getColumnModel().getColumn(1).setCellEditor(new DefaultCellEditor(paraTipo));
			
			FormateaTablas paraFormatear = new FormateaTablas();
			paraFormatear.formatearColumnas(tabla, "empleados");
		
			for(int i=0;i<n_columnas;i++){
				tabla.getColumnModel().getColumn(i).setCellRenderer(tcr);
			}
		
		}
		
		int x = contenedor.getLocation().x+contenedor.getSize().width;
		int y = contenedor.getLocation().y+tabla.getRowHeight()*(tabla.getRowCount()-1)+21;
		
		if(tabla_seleccionada.equals("RUBROS")){
			panel.guardar.setLocation(x, y);
			panel.cancelar.setLocation(x+105,y);
		}else{
			panel.cancelar.setLocation(contenedor.getLocation().x+contenedor.getWidth()-100,contenedor.getLocation().y+contenedor.getHeight()+20);
			panel.guardar.setLocation(panel.cancelar.getLocation().x-110, contenedor.getLocation().y+contenedor.getHeight()+20);
		}
		panel.guardar.setVisible(true);
		panel.cancelar.setVisible(true);
		panel.repaint();
		panel.revalidate();
	}
	
	JTable tabla;
	JComboBox<String> tipoTabla;
	DefaultTableModel modeloNuevo;
}
