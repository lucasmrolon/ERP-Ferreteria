package controlador.stock;

import java.awt.event.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import modelo.consultas.EjecutaConsultas;

//CLASE QUE CARGA LAS OBSERVACIONES DE LOS PRODUCTOS
public class MostrarObs extends MouseAdapter {
	
	public MostrarObs(JTable tabla){
		this.tabla = tabla;
	}

	//SE ACTIVA AL HACER CLIC EN LA COLUMNA DE COMENTARIOS DE UNA FILA
	@Override
	public void mouseClicked(MouseEvent e) {
		
		//SE OBTIENE EL VALOR DE FILA Y COLUMNA SOBRE LA QUE SE HIZO CLICK
		int seleccionado = tabla.rowAtPoint(e.getPoint());
	    int columna = tabla.columnAtPoint(e.getPoint());
	    String nombre_columna = tabla.getColumnName(columna);
	    
		int seleccionado_real = tabla.convertRowIndexToModel(seleccionado);
		DefaultTableModel modelo=(DefaultTableModel)tabla.getModel();
		
		//SI ES LA COLUMNA DE COMENTARIOS...
		if(nombre_columna.equals("OBSERV.")){
			//OBTIENE EL VALOR ALMACENADO EN LA CELDA
			String valor = (String)modelo.getValueAt(seleccionado_real, 8);
			int cod = Integer.parseInt((String)modelo.getValueAt(seleccionado_real, 0));
		
			//SI CONTIENE UNA "X", SE BUSCA EL COMENTARIO EN LA BASE DE DATOS
			if(valor.equals("X")){
			
				EjecutaConsultas nuevaConsulta=new EjecutaConsultas();
				String coment = nuevaConsulta.consultaComent(cod);
				if(coment!=null){
					JOptionPane.showMessageDialog(tabla, coment);
				}
			}
		}
	}

	JTable tabla;
}
