package controlador.detalle;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DecimalFormat;

import javax.swing.*;

import modelo.consultas.EjecutaConsultasCtasCtes;
import vista.detalle.PanelDetalle;

//CLASE QUE PERMITE REGISTRAR EL PAGO DE UNA CUENTA CORRIENTE PARA ACTUALIZAR SU ESTADO
public class AbonarCuentaCorriente implements ActionListener{

	//CONSTRUCTOR
	public AbonarCuentaCorriente(JTable tablaCuentas, int fila_selec,JDialog ventana,int n_cuenta,JTextField contieneMonto){
		this.n_cuenta=n_cuenta;
		this.contieneMonto=contieneMonto;
		enviaConsulta= new EjecutaConsultasCtasCtes();
		this.ventana = ventana;
		this.panel = (vista.detalle.PanelDetalle)panel;
		this.tablaCuentas = tablaCuentas;
		this.fila_selec = fila_selec;
		df = new DecimalFormat("###,###.00");
	}
	
	//OBTIENE EL MONTO INGRESADO Y ACTUALIZA EL ESTADO
	@Override
	public void actionPerformed(ActionEvent arg0) {
		
		double monto = Double.parseDouble(contieneMonto.getText());
		boolean ok = enviaConsulta.actualizarEstado(monto, n_cuenta);
		if(ok){
			Double nuevo_estado = enviaConsulta.obtenerEstado(n_cuenta);
			String estado = "";
			if(nuevo_estado<0){
				estado = "<html><b><p style='color:red';>- "+df.format(-1*nuevo_estado)+"</style></b></html>";
			}else
				estado = "+ "+df.format(nuevo_estado);
			tablaCuentas.setValueAt(estado, fila_selec, 7);
			ventana.dispose();
		}else{
			JOptionPane.showMessageDialog(null,"<html><font size=4>¡No se pudo actualizar el estado de la cuenta! Intente nuevamente.</font></html>",
					"¡Error!", JOptionPane.WARNING_MESSAGE);
		}
	}

	DecimalFormat df;
	int n_cuenta,fila_selec;
	JTable tablaCuentas;
	JTextField contieneMonto;
	EjecutaConsultasCtasCtes enviaConsulta;
	JDialog ventana;
	PanelDetalle panel;
}
