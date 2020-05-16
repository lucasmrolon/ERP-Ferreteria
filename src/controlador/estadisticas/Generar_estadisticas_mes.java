package controlador.estadisticas;

import java.awt.Dimension;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import javax.swing.JPanel;

import org.jfree.chart.*;
import org.jfree.chart.axis.*;
import org.jfree.chart.labels.StandardPieSectionLabelGenerator;
import org.jfree.chart.plot.*;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;

import modelo.consultas.EjecutaConsultasEstadisticas;
import vista.estadisticas.*;

//CLASE QUE PERMITE GENERAR LAS ESTADISTICAS MENSUALES
public class Generar_estadisticas_mes {

	public Generar_estadisticas_mes(JPanel panelDatos){
		
		Calendar calendario = Calendar.getInstance();
		
		SimpleDateFormat sdf2 = new SimpleDateFormat("MM-yyyy");
		ArrayList<Object[]> meses = new ArrayList<Object[]>();
		for(int i=12;i>=0;i--){
			calendario.setTime(new Date());
			calendario.add(Calendar.MONTH, -i);
			Date mes = calendario.getTime();
			meses.add(new Object[]{mes,null});			
		}
		
		//CREA GRAFICA DE MONTO MENSUAL DE VENTAS
		EjecutaConsultasEstadisticas estadisticasVentas = new EjecutaConsultasEstadisticas();
		ArrayList<Object[]> montoventas_por_mes = estadisticasVentas.consultarVentasPorMes("monto");
		DefaultCategoryDataset dataset6 = new DefaultCategoryDataset();
		
		for(Object[] m:meses){			
			Date mes_actual = ((Date)m[0]);
			calendario.setTime(mes_actual);
			int mes = calendario.get(Calendar.MONTH)+1;
			int anio = calendario.get(Calendar.YEAR);
			for(Object[] m2:montoventas_por_mes){
				if(mes==(int)m2[0] && anio==(int)m2[1]){
					m[1]=m2[2];
				}
			}
			if(m[1]==null){
				m[1]=0.00;
			}
			dataset6.addValue((double)m[1], "Monto de Ventas", sdf2.format(m[0]));
		}
		
		crearGraficoDeBarras("Ingresos por mes",PlotOrientation.VERTICAL,dataset6,panelDatos);
		
		//CREA GRAFICA DE NUMERO MENSUAL DE VENTAS
		ArrayList<Object[]> nventas_por_mes = estadisticasVentas.consultarVentasPorMes("cantidad");
		DefaultCategoryDataset dataset7 = new DefaultCategoryDataset();
		
		for(Object[] m:meses){			
			Date mes_actual = ((Date)m[0]);
			calendario.setTime(mes_actual);
			int mes = calendario.get(Calendar.MONTH)+1;
			int anio = calendario.get(Calendar.YEAR);
			for(Object[] m2:nventas_por_mes){
				if(mes==(int)m2[0] && anio==(int)m2[1]){
					m[1]=m2[2];
				}
			}
			if(m[1]==null){
				m[1]=0.00;
			}
			dataset7.addValue((double)m[1], "Número de Ventas", sdf2.format(m[0]));
		}
		crearGraficoDeBarras("Ventas por mes",PlotOrientation.VERTICAL,dataset7,panelDatos);
	
		
		Date mes_actual=null;
		try {
			mes_actual = sdf2.parse(sdf2.format(new Date()));
		} catch (ParseException e1) {
			
			e1.printStackTrace();
		} 
		calendario.setTime(mes_actual);
		calendario.add(Calendar.MONTH, 1);
		
		//CREA GRAFICO DE FORMAS DE PAGO MAS USADAS POR MES
		ArrayList<Object[]> uso_formasdepago_por_mes = estadisticasVentas.consultaFormasDePagoUsadas(mes_actual,calendario.getTime());
		DefaultPieDataset dataset8 = new DefaultPieDataset();
		for(Object[] u:uso_formasdepago_por_mes){
			dataset8.setValue((String)u[1],(int)u[2]);
		}
		JFreeChart chart8 = ChartFactory.createPieChart("Formas de Pago más usadas - Mensual", dataset8, true, true, false);
		PiePlot plot = (PiePlot)chart8.getPlot();
		plot.setLabelGenerator(new StandardPieSectionLabelGenerator("{0} \n({2})"));
		ChartPanel grafico8 = new ChartPanel(chart8);
		panelDatos.add(grafico8);
		grafico8.setPreferredSize(new Dimension(200,100));
		grafico8.addMouseListener(new MouseAdapter(){
			public void mouseClicked(MouseEvent e){
					new VentanaTortaEditable(meses,chart8,"mes",sdf2);
			}
		});
		
		//CREA EL GRAFICO DE PRODUCTOS MAS VENDIDOS POR MES
		ArrayList<Object[]> productos_mas_vendidos_por_periodo = estadisticasVentas.consultarProductosMasVendidos(mes_actual,calendario.getTime());
		DefaultCategoryDataset dataset9 = new DefaultCategoryDataset();
		
		for(Object[] p:productos_mas_vendidos_por_periodo){
			dataset9.addValue((double)p[2], "Producto", String.format("%05d", (int)p[0]) + " - " + (String)p[1]);
		}
		int ncategorias = dataset9.getColumnCount();
		if(ncategorias<10){
			while(ncategorias<10){
				ncategorias++;
				dataset9.addValue(0, "Producto", ncategorias + "°");
			}
		}
		crearGraficoBarrasEditable(meses,"Productos más vendidos - Mensual",PlotOrientation.HORIZONTAL,dataset9,panelDatos,sdf2);
		
		//CREA EL GRÁFICO DE RUBROS MAS SOLICITADOS POR MES
		ArrayList<Object[]> rubros_mas_vendidos_por_periodo = estadisticasVentas.consultarRubrosMasVendidos(mes_actual, calendario.getTime());
		DefaultCategoryDataset dataset10 = new DefaultCategoryDataset();

		for(Object[] p:rubros_mas_vendidos_por_periodo){
			dataset10.addValue((double)p[2], "Rubro", (String)p[1]);
		}		
		ncategorias = dataset10.getColumnCount();
		if(ncategorias<5){
			while(ncategorias<5){
				ncategorias++;
				dataset10.addValue(0, "Rubro", ncategorias + "°");
			}
		}
		crearGraficoBarrasEditable(meses,"Rubros más solicitados - Mensual",PlotOrientation.HORIZONTAL,dataset10,panelDatos,sdf2);
	}
	
	//CREA GRAFICOS DE BARRAS
	private void crearGraficoDeBarras(String elemento, PlotOrientation orientacion, DefaultCategoryDataset dataset,JPanel panelDatos){
		
		JFreeChart chart = ChartFactory.createBarChart(elemento, null, null, dataset,orientacion,true,true,false);
		CategoryAxis axis = chart.getCategoryPlot().getDomainAxis();
		axis.setVisible(false);
		if(orientacion==PlotOrientation.VERTICAL){
			axis.setCategoryLabelPositions(CategoryLabelPositions.UP_45);
		}
		ChartPanel grafico = new ChartPanel(chart);
		grafico.setPreferredSize(new Dimension(200,100));
		panelDatos.add(grafico);
		grafico.addMouseListener(new MouseAdapter(){
			public void mouseClicked(MouseEvent e){
					new VentanaBarras(chart,orientacion);
			}
		});
	}
	
	//CREA GRAFICOS DE BARRAS SELECCIONABLES
	private void crearGraficoBarrasEditable(ArrayList<Object[]> meses,String elemento, PlotOrientation orientacion, DefaultCategoryDataset dataset, JPanel panelDatos, SimpleDateFormat sdf){

		JFreeChart chart = ChartFactory.createBarChart(elemento, null, null, dataset,PlotOrientation.HORIZONTAL,true,true,false);
		CategoryAxis axis = chart.getCategoryPlot().getDomainAxis();
		axis.setVisible(false);
		ChartPanel grafico = new ChartPanel(chart);
		grafico.setPreferredSize(new Dimension(200,100));
		panelDatos.add(grafico);
		grafico.addMouseListener(new MouseAdapter(){
			public void mouseClicked(MouseEvent e){
				if(elemento.equals("Rubros más solicitados - Mensual")){
					new VentanaBarrasEditable(meses, chart, "rubros",PlotOrientation.HORIZONTAL,"mes",sdf);
				}else if(elemento.equals("Productos más vendidos - Mensual")){
					new VentanaBarrasEditable(meses, chart, "productos",PlotOrientation.HORIZONTAL,"mes",sdf);
				}
			}
		});
	}
}
