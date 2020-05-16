package controlador.estadisticas;

import java.awt.*;
import java.text.DecimalFormat;

import javax.swing.*;

import modelo.consultas.EjecutaConsultasEstadisticas;

//CLASE QUE GENERA ESTADISTICAS HISTÓRICAS
public class Generar_estadisticas_hist {

	public Generar_estadisticas_hist(JPanel panelDatos){
		
		Font fuente = new Font(Font.DIALOG,Font.ITALIC,17);
		EjecutaConsultasEstadisticas paraConsultas = new EjecutaConsultasEstadisticas();
		double[] totales = paraConsultas.obtenerTotales();
		int ancho_pantalla = Toolkit.getDefaultToolkit().getScreenSize().width;
		Dimension dim_label = new Dimension(ancho_pantalla/5,30);
		
		//MUESTRA LA CANTIDAD DE PRODUCTOS REGISTRADOS
		JLabel totalproductos = new JLabel("<html>Productos registrados: <b>" + (int)totales[3] + "</b></html>");
		totalproductos.setFont(fuente);
		totalproductos.setPreferredSize(dim_label);
		totalproductos.setHorizontalAlignment(SwingConstants.CENTER);
		
		//MUESTRA EL NUMERO DE VENTAS REGISTRADAS
		JLabel totalventas = new JLabel("<html>Ventas registradas: <b>" + (int)totales[0] + "</b></html>");
		totalventas.setFont(fuente);
		totalventas.setPreferredSize(dim_label);
		totalventas.setHorizontalAlignment(SwingConstants.CENTER);
		
		//MUESTRA EL TOTAL DE REPARTOS REALIZADOS
		JLabel totalrepartos = new JLabel("<html>Repartos realizados: <b>" + (int)totales[1] + "</b></html>");
		totalrepartos.setFont(fuente);
		totalrepartos.setPreferredSize(dim_label);
		totalrepartos.setHorizontalAlignment(SwingConstants.CENTER);
		
		//MUESTRA EL TOTAL DE PRODUCTOS LLEVADOS A DOMICILIO
		JLabel totalprodrepart = new JLabel("<html>Productos llevados a domicilio: <b>" + (int)totales[2] + "</b></html>");
		totalprodrepart.setFont(fuente);
		totalprodrepart.setPreferredSize(dim_label);
		totalprodrepart.setHorizontalAlignment(SwingConstants.CENTER);
		
		panelDatos.add(totalproductos);
		panelDatos.add(totalventas);
		panelDatos.add(totalrepartos);
		panelDatos.add(totalprodrepart);
		
		Dimension dim_label2 = new Dimension(ancho_pantalla/4,30);
		
		DecimalFormat formatea = new DecimalFormat("###,###,###.00");
		
		//MUESTRA EL CAPITAL TOTAL DE PRODUCTOS EN ALMACÉN
		JLabel capi_almacen = new JLabel("<html>Capital en almacén: <b>$ " + formatea.format(totales[5]) + "</b></html>",JLabel.CENTER);
		capi_almacen.setFont(fuente);
		capi_almacen.setPreferredSize(dim_label2);
		capi_almacen.setHorizontalAlignment(SwingConstants.CENTER);
		panelDatos.add(capi_almacen);
		
		//MUESTRA EL TOTAL DE INGRESOS POR VENTAS
		JLabel ing_ventas = new JLabel("<html>Total ingresos por ventas: <b>$ " + formatea.format(totales[4]) + "</b></html>",JLabel.CENTER);
		ing_ventas.setFont(fuente);
		ing_ventas.setPreferredSize(dim_label2);
		ing_ventas.setHorizontalAlignment(SwingConstants.CENTER);
		panelDatos.add(ing_ventas);
		
		//MUESTRA EL TOTAL DE EGRESOS POR COMPRAS
		JLabel egr_compras = new JLabel("<html>Total egresos por compras: <b>$ " + formatea.format(totales[6]) + "</b></html>",JLabel.CENTER);
		egr_compras.setFont(fuente);
		egr_compras.setPreferredSize(dim_label2);
		egr_compras.setHorizontalAlignment(SwingConstants.CENTER);
		panelDatos.add(egr_compras);
	}
}
