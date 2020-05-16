package controlador.detalle;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DecimalFormat;

import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.JTextField;

import modelo.consultas.EjecutaConsultasCtasCtes;
import vista.detalle.PanelDetalle;

//CLASE QUE PERMITE ABRIR UNA NUEVA CUENTA CORRIENTE A UN CLIENTE
public class AbrirCuentaCorriente implements ActionListener{
	
	public AbrirCuentaCorriente(JTable tablaCuentas, int fila_selec,JDialog ventana,int n_cuenta,JTextField contieneMonto){
		this.n_cuenta=n_cuenta;
		this.contieneMonto=contieneMonto;
		enviaConsulta= new EjecutaConsultasCtasCtes();
		this.ventana = ventana;
		this.panel = (vista.detalle.PanelDetalle)panel;
		this.tablaCuentas = tablaCuentas;
		this.fila_selec = fila_selec;
		df = new DecimalFormat("###,###.00");
	}
	@Override
	public void actionPerformed(ActionEvent arg0) {
		
		//OBTIENE EL DNI DEL CLIENTE Y EL MONTO INICIAL DE LA CUENTA
		double monto = Double.parseDouble(contieneMonto.getText());
		String dni_cliente = (String)tablaCuentas.getValueAt(fila_selec, 0);
		
		//ABRE LA CUENTA
		boolean ok = enviaConsulta.abrirCuenta(monto, n_cuenta, dni_cliente);
		
		//ACTUALIZA EL ESTADO DE LA CUENTA
		if(ok){
			String estado=df.format(monto);
			if(monto>0){
				estado = "+ " + estado;
			}
			tablaCuentas.setValueAt(estado, fila_selec, 7);
			tablaCuentas.setValueAt(n_cuenta, fila_selec, 6);
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
