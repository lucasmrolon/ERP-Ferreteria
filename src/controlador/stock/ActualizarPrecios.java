package controlador.stock;

import java.awt.event.*;
import javax.swing.*;

import modelo.consultas.EjecutaConsultas;
import vista.stock.PanelStock;

//CLASE ENCARGADA DE ACTUALIZAR LOS PRECIOS DE TODOS LOS PRODUCTOS A LA VEZ
public class ActualizarPrecios implements ActionListener {

	public ActualizarPrecios(PanelStock panel, JDialog ventanaActualizacionPrecios, JSpinner seleccionValor){
		
		this.seleccionValor = seleccionValor;	//SPINNER CON EL VALOR INGRESADO
		this.panel = panel;						//PANEL PADRE PARA ACTUALIZAR LA TABLA DE PRODUCTOS
		ventana=ventanaActualizacionPrecios;	//VENTANA DE ACTUALIZACIÓN
	
	}
	@Override
	public void actionPerformed(ActionEvent arg0) {

		//SE OBTIENE EL VALOR INGRESADO Y SE VERIFICA
		double valor = (double)seleccionValor.getValue();

		//SE MUESTRA UN CUADRO DE CONFIRMACIÓN DE ACCIÓN
		String mensaje="<html><Font size=5>¿Está seguro que desea aumentar un " + valor + 
				" % los precios de TODOS los productos?<br> No podrá deshacer esta acción luego.</Font></html>";
		int elegido = JOptionPane.showConfirmDialog(ventana, mensaje, "Confirmación", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
			
		//AL CONFIRMAR LA ACCIÓN, SE LLEVA A CABO LA ACTUALIZACIÓN
		if(elegido==JOptionPane.YES_OPTION){
			EjecutaConsultas consulta1 = new EjecutaConsultas();
			boolean ok = consulta1.actualizaPrecios(valor);
			if(ok){
				panel.actualizarTabla(null, 'u');
				ventana.dispose();
			}
		}
	}

	
	JSpinner seleccionValor;
	PanelStock panel;
	JDialog ventana;
	JLabel aviso_errores;
}
