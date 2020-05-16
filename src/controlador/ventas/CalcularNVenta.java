package controlador.ventas;

import java.awt.event.*;
import javax.swing.JLabel;

import modelo.consultas.EjecutaConsultasVenta;

//CLASE QUE CALCULA EL NÚMERO DE VENTA ACTUAL
public class CalcularNVenta implements ActionListener{
	
	public CalcularNVenta(JLabel muestra_n_venta){
		muestra_n=muestra_n_venta;
	}

	//CONSULTA LA BASE DE DATOS DE VENTAS PARA OBTENER EL NÚMERO DE VENTAS ALMACENADAS
	@Override
	public void actionPerformed(ActionEvent arg0) {
		EjecutaConsultasVenta nueva_consulta = new EjecutaConsultasVenta();
		int n = nueva_consulta.consultaN_venta();
		if(n!=0){
			muestra_n.setText("   Venta N° "+ String.format("%07d", n+1));
		}
	}
	
	JLabel muestra_n;

}
