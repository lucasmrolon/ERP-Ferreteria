package controlador.detalle;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import vista.detalle.PanelDetalle;

//CLASE QUE PERMITE CANCELAR LOS CAMBIOS QUE SE ESTÁN REALIZANDO EN LAS TABLAS DE DETALLE
public class CancelarCambios implements ActionListener {

	public CancelarCambios(PanelDetalle panel,JComboBox<String> tipoTabla, JTable tablaResultado,DefaultTableCellRenderer tcr){
		this.panel = panel;
		this.tipoTabla = tipoTabla;
		this.tablaResultado = tablaResultado;
		tcr_por_defecto = tcr;
	}
	@Override
	public void actionPerformed(ActionEvent arg0) {
		
		DefaultTableModel modelo = (DefaultTableModel)tablaResultado.getModel();
		
		//OBTIENE EL NUMERO ACTUAL DE FILAS Y COLUMNAS
		int n_filas = tablaResultado.getRowCount();
		int n_columnas = tablaResultado.getColumnCount();
	
		//OBTIENE LOS DATOS ALMACENADOS EN LA TABLA
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
		
		//LE DA FORMATO
		FormateaTablas paraFormatear = new FormateaTablas();
		
		//ESTABLECE EL MODELO NUEVO
		modeloNuevo = new DefaultTableModel(datos,columnas){
			/**
			 * 
			 */
			private static final long serialVersionUID = 6670288627845282470L;

			public boolean isCellEditable(int rowIndex,int columnIndex){
				return false;
			}
		};
		
		//SI SE ESTABA MODIFICANDO, SE CAMBIA EL ESTADO DE LA FILA A 'NO EDITABLE'
		if(!panel.modif.isEnabled()){
		
			
			tablaResultado.setModel(modeloNuevo);
			
			for(int j=0;j<n_columnas;j++){
				modeloNuevo.setValueAt(panel.fila_auxiliar[j],panel.seleccionado, j);
			}
			
			panel.modif.setEnabled(true);
			
		}
		//SI SE ESTABA AÑADIENDO UNA NUEVA FILA, LA ELIMINA
		else if(!panel.aniadir.isEnabled()){
			
			JScrollPane contenedor = (JScrollPane)tablaResultado.getParent().getParent();
			
			modeloNuevo.removeRow(tablaResultado.getRowCount()-1);
			
			if(tablaResultado.getRowCount()<11){
				contenedor.setSize(contenedor.getSize().width, contenedor.getSize().height-tablaResultado.getRowHeight());
				panel.actualizar.setLocation(panel.actualizar.getLocation().x, panel.actualizar.getLocation().y-tablaResultado.getRowHeight());
				panel.aniadir.setLocation(panel.aniadir.getLocation().x, panel.aniadir.getLocation().y-tablaResultado.getRowHeight());
				panel.modif.setLocation(panel.modif.getLocation().x, panel.modif.getLocation().y-tablaResultado.getRowHeight());
				panel.elim.setLocation(panel.elim.getLocation().x, panel.elim.getLocation().y-tablaResultado.getRowHeight());
				panel.cerrar_ctacte.setLocation(panel.cerrar_ctacte.getLocation().x, panel.cerrar_ctacte.getLocation().y-tablaResultado.getRowHeight());
				panel.abrir_ctacte.setLocation(panel.abrir_ctacte.getLocation().x, panel.abrir_ctacte.getLocation().y-tablaResultado.getRowHeight());
				
			}
			
			tablaResultado.setModel(modeloNuevo);
			
			panel.aniadir.setEnabled(true);
			
		}
		
		//RESETEA LA VISIBILIDAD DE LOS BOTONES
		panel.guardar.setVisible(false);
		panel.cancelar.setVisible(false);
		panel.elim.setVisible(true);
		panel.modif.setVisible(true);
		panel.aniadir.setVisible(true);
		
		
		//FORMATEA TABLA Y RESETEA BOTONES ESPECIALES
		tabla_seleccionada = (String)tipoTabla.getSelectedItem();
		if(tabla_seleccionada.equals("CLIENTES")){
			paraFormatear.formatearColumnas(tablaResultado, "clientes");
			panel.actualizar.setVisible(true);
			panel.abrir_ctacte.setVisible(true);
			panel.cerrar_ctacte.setVisible(true);
		}else if(tabla_seleccionada.equals("PROVEEDORES")){
			paraFormatear.formatearColumnas(tablaResultado, "proveedores");
			panel.actualizar.setVisible(true);
		}else if(tabla_seleccionada.equals("RUBROS")){
			paraFormatear.formatearColumnas(tablaResultado, "rubros");
		}else if(tabla_seleccionada.equals("EMPLEADOS")){
			paraFormatear.formatearColumnas(tablaResultado, "empleados");
		}
		
		
		for(int i=0;i<n_columnas;i++){
			tablaResultado.getColumnModel().getColumn(i).setCellRenderer(tcr_por_defecto);
		}
		
		panel.repaint();
		panel.validate();
	}

	String tabla_seleccionada;
	PanelDetalle panel;
	JComboBox<String> tipoTabla;
	JTable tablaResultado;
	DefaultTableModel modeloNuevo;
	DefaultTableCellRenderer tcr_por_defecto;
}
