package controlador.repartos;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import modelo.consultas.EjecutaConsultasReparto;

//CLASE QUE PERMITE CREAR UN NUEVO REPARTO
public class CrearReparto implements ActionListener{

	public CrearReparto(JTable repartoNuevo, JTable paraRepartosPendientes,JLabel titulo_tabla){
		this.repartoNuevo = repartoNuevo;
		this.paraRepartosPendientes = paraRepartosPendientes;
		this.titulo_tabla = titulo_tabla;
		
	}
	
	public void actionPerformed(ActionEvent arg0) {
		
		if(titulo_tabla.getText().equals("<html><font size=4>NUEVO REPARTO</font></html>")){
			int n = repartoNuevo.getRowCount();
		
			//OBTIENE LOS DATOS DEL NUEVO REPARTO, INCLUIDO SUS LINEAS
			if (n>0){
				int[] lineasdereparto = new int[n];
		
				for(int i=0;i<n;i++){
					lineasdereparto[i] = (int)repartoNuevo.getValueAt(i, 0);
				}
		
				//CREA EL REPARTO Y ASIGNA LAS LINEAS CORRESPONDIENTES
				EjecutaConsultasReparto nuevaConsulta = new EjecutaConsultasReparto();
				Object[] codigoyfechacreado = nuevaConsulta.creaRepartoyAsignaLineas(lineasdereparto);
				if(codigoyfechacreado!=null){
					int n_reparto = (int)codigoyfechacreado[0];
					SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
					String fecha = df.format(codigoyfechacreado[1]);

					((DefaultTableModel)paraRepartosPendientes.getModel()).addRow(new Object[]{n_reparto,fecha});
		
					for(int i=repartoNuevo.getModel().getRowCount();i>0;i--){
						((DefaultTableModel)repartoNuevo.getModel()).removeRow(i-1);	
					}
				}
				else{
					JOptionPane.showMessageDialog(null,
							"<html><font size=4>¡No se pudo crear el reparto! Intente nuevamente.</font></html>",
							"¡Error!", JOptionPane.WARNING_MESSAGE);
				}
			}else{
				String mensaje = "<html><Font size=5>¡No ha añadido productos al reparto!</Font></html>";
				JOptionPane.showMessageDialog(null, mensaje, "¡Error! No se puede crear", JOptionPane.WARNING_MESSAGE);
			}
	
		}
		else{
			String mensaje = "<html><Font size=5>¡El reparto ya existe!</Font></html>";
			JOptionPane.showMessageDialog(null, mensaje, "¡Error! No se puede crear", JOptionPane.WARNING_MESSAGE);
		}
	}
	JTable repartoNuevo,paraRepartosPendientes;
	JLabel titulo_tabla;
}
