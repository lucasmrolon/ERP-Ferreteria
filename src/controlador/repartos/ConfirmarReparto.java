package controlador.repartos;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Date;

import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import modelo.consultas.EjecutaConsultasEmpleado;
import modelo.consultas.EjecutaConsultasReparto;

//CLASE QUE PERMITE REALIZAR LA CONFIRMACIÓN DE UN REPARTO
public class ConfirmarReparto implements ActionListener{

	public ConfirmarReparto(JTable tablaRepartosPendientes,JTable tablaLineas,String usuario){
		tabla = tablaRepartosPendientes;
		this.tablaLineas = tablaLineas;
		this.usuario = usuario;
	}
	@Override
	public void actionPerformed(ActionEvent arg0) {
		try{
			//OBTIENE DATOS DEL REPARTO SELECCIONADO
			int seleccion = tabla.getSelectedRow();
			int n_reparto = (int)tabla.getValueAt(seleccion, 0);
			Date confirmacion = new Date();
			EjecutaConsultasEmpleado nuevaConsulta = new EjecutaConsultasEmpleado();
			int cod_empleado = nuevaConsulta.obtieneId(usuario);
		
			//CONFIRMA EL REPARTO
			if(cod_empleado!=0){
				EjecutaConsultasReparto paraConfirmar = new EjecutaConsultasReparto();
				boolean ok = paraConfirmar.confirmarReparto(n_reparto, cod_empleado, confirmacion);
		
				//ACTUALIZA LA TABLA DE REPARTOS PENDIENTES
				if(ok){
					((DefaultTableModel)tabla.getModel()).removeRow(seleccion);
					for(int i=tablaLineas.getModel().getRowCount();i>0;i--){
						((DefaultTableModel)tablaLineas.getModel()).removeRow(i-1);	
					}
				}else{
					JOptionPane.showMessageDialog(null,"<html><font size=4>¡No se pudo confirmar el reparto! Intente nuevamente.</font></html>",
							"¡Error!", JOptionPane.WARNING_MESSAGE);
				}
			}else{
				JOptionPane.showMessageDialog(null,
					"<html><font size=4>No se pudo obtener el Id de empleado. Intente nuevamente.</font></html>",
					"¡Error!", JOptionPane.WARNING_MESSAGE);
			}
		}catch(Exception e){
			String mensaje = "<html><Font size=5>¡No ha seleccionado ningún reparto!</Font></html>";
			JOptionPane.showMessageDialog(null, mensaje, "¡Error! No se puede confirmar", JOptionPane.WARNING_MESSAGE);
		}
	}
	
	JTable tabla,tablaLineas;
	String usuario;
}
