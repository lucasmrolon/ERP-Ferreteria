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

//CLASE QUE GENERA LAS ESTADISTICAS ANUALES
public class Generar_estadisticas_anual {

	public Generar_estadisticas_anual(JPanel panelDatos){

		Date hoy = new Date();
		Calendar calendario = Calendar.getInstance();
		calendario.setTime(hoy);
		SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy");

		ArrayList<Object[]> anios = new ArrayList<Object[]>();
		for(int i=10;i>=0;i--){
			calendario.setTime(hoy);
			calendario.add(Calendar.YEAR, -i);
			Date anio = calendario.getTime();
			anios.add(new Object[]{anio,null});		
		}
		
		//CREA EL GRÁFICO DE MONTO ANUAL DE VENTAS Y LO CARGA
		EjecutaConsultasEstadisticas estadisticasVentas = new EjecutaConsultasEstadisticas();
		ArrayList<Object[]> montoventas_por_anio = estadisticasVentas.consultarVentasPorAnio("monto");
		DefaultCategoryDataset dataset1 = new DefaultCategoryDataset();
		
		for(Object[] a:anios){			
			Date anio_actual = ((Date)a[0]);
			calendario.setTime(anio_actual);
			int anio = calendario.get(Calendar.YEAR);
			for(Object[] m2:montoventas_por_anio){
				if(anio==(int)m2[0]){
					a[1]=m2[1];
				}
			}
			if(a[1]==null){
				a[1]=0.00;
			}
			dataset1.addValue((double)a[1], "Monto de Ventas", sdf2.format(a[0]));
		}
		crearGraficoDeBarras("Ingresos por año",PlotOrientation.VERTICAL,dataset1,panelDatos);
		
		//CREA EL GRAFICO DE NUMERO ANUAL DE VENTAS Y LO CARGA
		ArrayList<Object[]> nventas_por_anio = estadisticasVentas.consultarVentasPorAnio("cantidad");
		DefaultCategoryDataset dataset2 = new DefaultCategoryDataset();
		
		for(Object[] a:anios){			
			Date anio_actual = ((Date)a[0]);
			calendario.setTime(anio_actual);
			int anio = calendario.get(Calendar.YEAR);
			for(Object[] n:nventas_por_anio){
				if(anio==(int)n[0]){
					a[1]=n[1];
				}
			}
			if(a[1]==null){
				a[1]=0;
			}
			
			dataset2.addValue((double)a[1], "Número de Ventas", sdf2.format(a[0]));
		}
		crearGraficoDeBarras("Ventas por año",PlotOrientation.VERTICAL,dataset2,panelDatos);
		
		
		
		
		try {
			calendario.setTime(sdf2.parse(sdf2.format(hoy)));
		} catch (ParseException e1) {
			e1.printStackTrace();
		} 
		Date inicio_anio = calendario.getTime();
		calendario.add(Calendar.YEAR, 1);
		
		//CREA EL GRÁFICO DE FORMAS DE PAGO MAS USADAS Y LA CARGA
		ArrayList<Object[]> uso_formasdepago_por_anio = estadisticasVentas.consultaFormasDePagoUsadas(inicio_anio,calendario.getTime());
		DefaultPieDataset dataset8 = new DefaultPieDataset();
		for(Object[] u:uso_formasdepago_por_anio){
			dataset8.setValue((String)u[1],(int)u[2]);
		}
		JFreeChart chart8 = ChartFactory.createPieChart("Formas de Pago más usadas - Anual", dataset8, true, true, false);
		PiePlot plot = (PiePlot)chart8.getPlot();
		plot.setLabelGenerator(new StandardPieSectionLabelGenerator("{0} \n({2})"));
		ChartPanel grafico8 = new ChartPanel(chart8);
		panelDatos.add(grafico8);
		grafico8.setPreferredSize(new Dimension(200,100));
		grafico8.addMouseListener(new MouseAdapter(){
			public void mouseClicked(MouseEvent e){
					new VentanaTortaEditable(anios,chart8,"año",sdf2);
			}
		});
		
		
		//CREA EL GRÁFICO DE PRODUCTOS MAS VENDIDOS Y LO CARGA
		ArrayList<Object[]> productos_mas_vendidos_por_periodo = estadisticasVentas.consultarProductosMasVendidos(inicio_anio,calendario.getTime());
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
		crearGraficoBarrasEditable(anios,"Productos más vendidos - Anual",PlotOrientation.HORIZONTAL,dataset9,panelDatos,sdf2);
		
		
		//CREA EL GRÁFICO DE RUBROS MAS SOLICITADOS Y LO CARGA
		ArrayList<Object[]> rubros_mas_vendidos_por_periodo = estadisticasVentas.consultarRubrosMasVendidos(inicio_anio,calendario.getTime());
		DefaultCategoryDataset dataset10 = new DefaultCategoryDataset();
		
		for(Object[] r:rubros_mas_vendidos_por_periodo){
			dataset10.addValue((double)r[2], "Rubro", (String)r[1]);
		}
		ncategorias = dataset10.getColumnCount();
		if(ncategorias<5){
			while(ncategorias<5){
				ncategorias++;
				dataset10.addValue(0, "Rubro", ncategorias + "°");
			}
		}
		crearGraficoBarrasEditable(anios,"Rubros más solicitados - Anual",PlotOrientation.HORIZONTAL,dataset10,panelDatos,sdf2);
		
	}
	
	//CREA LOS GRAFICOS DE BARRAS
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
	
	//CREA GRAFICOS DE BARRA SELECCIONABLES
	private void crearGraficoBarrasEditable(ArrayList<Object[]> anios,String elemento, PlotOrientation orientacion, DefaultCategoryDataset dataset, JPanel panelDatos, SimpleDateFormat sdf){

		JFreeChart chart = ChartFactory.createBarChart(elemento, null, null, dataset,PlotOrientation.HORIZONTAL,true,true,false);
		CategoryAxis axis = chart.getCategoryPlot().getDomainAxis();
		axis.setVisible(false);
		ChartPanel grafico = new ChartPanel(chart);
		grafico.setPreferredSize(new Dimension(200,100));
		panelDatos.add(grafico);
		grafico.addMouseListener(new MouseAdapter(){
			public void mouseClicked(MouseEvent e){
				if(elemento.equals("Rubros más solicitados - Anual")){
					new VentanaBarrasEditable(anios, chart, "rubros",PlotOrientation.HORIZONTAL,"año",sdf);
				}else if(elemento.equals("Productos más vendidos - Anual")){
					new VentanaBarrasEditable(anios, chart, "productos",PlotOrientation.HORIZONTAL,"año",sdf);
				}
			}
		});
	}
}
