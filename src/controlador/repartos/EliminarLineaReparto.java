package controlador.repartos;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import modelo.consultas.EjecutaConsultasReparto;

//CLASE QUE PERMITE ELIMINAR UNA LINEA DE REPARTO
public class EliminarLineaReparto implements ActionListener {

	public EliminarLineaReparto(JTable tabla,String usuario){
		tablaLineasReparto = tabla;
		this.usuario = usuario;
	}
	@Override
	public void actionPerformed(ActionEvent arg0) {
		
		try{
			//MUESTRA MENSAJE DE CONFIRMACI�N DE ACCI�N
			int opcion = JOptionPane.showConfirmDialog(null,"<html><font size=4>�Confirma la eliminaci�n de la linea "
					+ "de reparto seleccionada? No podr� deshacer esta acci�n luego</font></html>",
					"�Error!", JOptionPane.YES_NO_OPTION,JOptionPane.WARNING_MESSAGE);
			//OBTIENE LA FILA SELECCIONADA
			if(opcion==JOptionPane.YES_OPTION){
				int seleccion = tablaLineasReparto.getSelectedRow();
				int n_linea = (int)tablaLineasReparto.getValueAt(seleccion, 0);

				String comentario ="ELIMINADO POR " + usuario;
			
				//ELIINA LINEA DE REPARTO SELECCIONADA
				EjecutaConsultasReparto paraEliminar = new EjecutaConsultasReparto();
				boolean ok = paraEliminar.eliminarLineaRepartoPendiente(n_linea,comentario);
				if(ok){
					((DefaultTableModel)tablaLineasReparto.getModel()).removeRow(seleccion);
				}else{
					String mensaje = "<html><Font size=5>�No se pudo eliminar la l�nea! Intente nuevamente.</Font></html>";
					JOptionPane.showMessageDialog(null, mensaje, "�Error!", JOptionPane.WARNING_MESSAGE);
				}
			}
		}catch(Exception e){
			String mensaje = "<html><Font size=5>�No ha seleccionado ninguna l�nea!</Font></html>";
			JOptionPane.showMessageDialog(null, mensaje, "�Error!", JOptionPane.WARNING_MESSAGE);
		}
		
	}
	String usuario;
	JTable tablaLineasReparto;
}
