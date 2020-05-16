package controlador;

import java.awt.event.*;
import javax.swing.*;
import javax.swing.table.TableRowSorter;

//CLASE PARA FILTRAR Y RECORRER LA TABLA DESDE EL CUADRO DE BÚSQUEDA
public class FiltrarTabla extends KeyAdapter{

	public FiltrarTabla(JTable tabla){
		this.tabla=tabla;
	}
	
	public void keyTyped(KeyEvent e){
		char c = e.getKeyChar();
		if(Character.isLowerCase(c)){
			String cad = (""+c).toUpperCase();
			c=cad.charAt(0);
			e.setKeyChar(c);
		}
	}
	
	public void keyReleased(KeyEvent e){
		
		//SE CAPTURA EL TEXTO INGRESADO
		JTextField buscar = (JTextField)e.getSource();
		String busqueda = buscar.getText();
		
		//SE FILTRA LA TABLA
		TableRowSorter<?> orden = (TableRowSorter<?>)tabla.getRowSorter();
		orden.setRowFilter(RowFilter.regexFilter("^" + busqueda , 1));
		
		//PARA RECORRER LA TABLA HACIA ARRIBA Y HACIA ABAJO CON LAS TECLAS CORRESPONDIENTES
		try{
			if(e.getKeyCode()==KeyEvent.VK_UP){
				if(tabla.getSelectedRow()>0){
					tabla.setRowSelectionInterval(tabla.getSelectedRow()-1,tabla.getSelectedRow()-1);
				}
			}
			else if(e.getKeyCode()==KeyEvent.VK_DOWN){
				if(tabla.getSelectedRow()<tabla.getRowCount()){
					tabla.setRowSelectionInterval(tabla.getSelectedRow()+1, tabla.getSelectedRow()+1);
				}
			}
			
		}catch(IllegalArgumentException ex){
			ex.printStackTrace();
		}
	}
	
	JTable tabla;
	
	RowFilter<Object, Object> filtro;
}
