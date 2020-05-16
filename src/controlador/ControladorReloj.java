package controlador;

import java.awt.event.*;
import java.util.*;
import javax.swing.*;

//CLASE QUE MANTIENE ACTUALIZADO EL RELOJ DE LA APLICACIÓN
public class ControladorReloj implements ActionListener{

	public ControladorReloj(JLabel hora,JLabel fecha){
		this.labelHora = hora;		//LABEL PARA LA HORA
		this.labelFecha = fecha;	//LABEL PARA LA FECHA
	}
	
	@Override
	public void actionPerformed(ActionEvent arg0) {
		
		//SE OBTIENE LA HORA Y FECHA ACTUAL
		ahora = Calendar.getInstance();
		
		//SE TRANSFORMA AL FORMATO DESEADO
		anio = ahora.get(Calendar.YEAR);
		mes = meses[ahora.get(Calendar.MONTH)];
		dia = dias[ahora.get(Calendar.DAY_OF_WEEK)-1]+ ", " + ahora.get(Calendar.DATE);
		horas = ahora.get(Calendar.HOUR);
		minutos = ahora.get(Calendar.MINUTE);
		segundos = ahora.get(Calendar.SECOND);
		ampm = ahora.get(Calendar.AM_PM);
		
		String amopm = "";
		if(ampm == Calendar.AM){
			amopm = "a. m.";
		}else{
			if(horas==0){
				horas=12;
			}
			amopm = "p. m.";
		}
		
		//Y SE ACTUALIZAN LOS JLABEL
		labelFecha.setText(dia + " de " + mes + " de " + anio);
		labelHora.setText(horas + ":" 
				+ String.format("%02d", minutos) + ":" 
				+ String.format("%02d", segundos) + "  " + amopm);
	}

	JLabel labelHora,labelFecha;
	
	Calendar ahora;
	
	int horas,minutos,segundos,ampm;
	
	String dia,mes;
	
	int anio;
	
	String[] dias = {"Domingo","Lunes","Martes","Miércoles","Jueves","Viernes","Sábado"};
	
	String[] meses = {"Enero","Febrero","Marzo","Abril","Mayo","Junio",
			"Julio","Agosto","Septiembre","Octubre","Noviembre","Diciembre"};
}
