package controlador.detalle;

import java.text.DecimalFormat;

import javax.swing.JOptionPane;
import javax.swing.JTable;

import modelo.consultas.EjecutaConsultasCtasCtes;

//CLASE QUE PERMITE CERRAR UNA CUENTA CORRIENTE
public class CerrarCuenta {

	public CerrarCuenta(int cliente, JTable tabla){
		
		Object cuenta = tabla.getValueAt(cliente, 6);
		
		//COMPRUEBA QUE EL CLIENTE POSEA UNA CUENTA
		if(cuenta!=""){
			
			int n_cuenta = (int)tabla.getValueAt(cliente, 6);
		
			EjecutaConsultasCtasCtes paraConsultas = new EjecutaConsultasCtasCtes();
		
			double estado = paraConsultas.obtenerEstado(n_cuenta);
		
			DecimalFormat df = new DecimalFormat("###,###.00");
		
			String mensaje;
			
			//CONSULTA SI SE VA A CERRAR LA CUENTA AUNQUE TENGA UN SALDO POSITIVO O NEGATIVO
			if(estado!=0){
				mensaje = "<html><font size=5>La cuenta seleccionada tiene un saldo de $ " 
						+ df.format(estado)+". ¿Está seguro que desea cerrarla?</font></html>";
			}else{
				//PIDE CONFIRMACIÓN DE LA ELIMINACIÓN
				mensaje = "<html><font size=5>¿Confirma la eliminación de la cuenta seleccionada?</font></html>";
			}
			int elegido = JOptionPane.showConfirmDialog(null, mensaje ,
					"Eliminación de Cuenta Corriente", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
			if(elegido==JOptionPane.YES_OPTION){
				//CIERRA LA CUENTA
				boolean ok = paraConsultas.cerrarCuenta(n_cuenta);
				if(ok){
					tabla.setValueAt("", cliente, 6);
					tabla.setValueAt("", cliente, 7);
				}
				else{
					JOptionPane.showMessageDialog(null,"<html><font size=5>Se produjo un error. No se pudo cerrar la Cuenta.</font></html>",
							"¡Error!", JOptionPane.WARNING_MESSAGE);
				}
			}
		}else{
			//SI EL CLIENTE NO POSEE UNA CUENTA, MUESTRA MENSAJE DE ERROR
			JOptionPane.showMessageDialog(null,"<html><font size=5>¡El cliente seleccionado no posee una Cuenta Corriente!</font></html>",
					"¡Error!", JOptionPane.WARNING_MESSAGE);
		}
	}
	
}
