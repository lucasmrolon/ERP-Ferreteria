package controlador.estadisticas;

import java.awt.Dimension;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import javax.swing.JPanel;

import org.jfree.chart.*;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.CategoryLabelPositions;
import org.jfree.chart.labels.*;
import org.jfree.chart.plot.*;
import org.jfree.data.category.*;
import org.jfree.data.general.DefaultPieDataset;

import modelo.consultas.EjecutaConsultasEstadisticas;
import vista.estadisticas.*;

//CLASE QUE PERMITE GENERAR LAS ESTADISTICAS DIARIAS
public class Generar_estadisticas_dia {

	public Generar_estadisticas_dia(JPanel panelDatos){
		
		Calendar calendario = Calendar.getInstance();
		
		ArrayList<Object[]> dias = new ArrayList<Object[]>();
		for(int i=29;i>=0;i--){
			calendario.setTime(new Date());
			calendario.add(Calendar.DAY_OF_YEAR, -i);
			Date dia = calendario.getTime();
			dias.add(new Object[]{dia,null});
		}
		
		//CREA EL GRÁFICO DE MONTO DIARIO DE VENTAS
		EjecutaConsultasEstadisticas estadisticasVentas = new EjecutaConsultasEstadisticas();
		ArrayList<Object[]> monto_por_dia = estadisticasVentas.consultarVentasPorDia("monto");
		DefaultCategoryDataset dataset = new DefaultCategoryDataset();
		SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
		
		for(Object[] d:dias){			
			Date dia_actual = ((Date)d[0]);
			String d1 = sdf.format(dia_actual);
			for(Object[] m:monto_por_dia){
				Date dia = ((Date)m[0]);
				String d2 = sdf.format(dia);				
				if(d1.equals(d2)){
					d[1]=m[1];
				}
			}
			if(d[1]==null){
				d[1]=0.00;
			}
			dataset.addValue((double)d[1], "Monto de Ventas", sdf.format(d[0]));
		}
		crearGraficoDeBarras("Ingresos por día - Últimos 30 días",PlotOrientation.VERTICAL,dataset,panelDatos);
		
		//CREA EL GRÁFICO DE NUMERO DIARIO DE VENTAS
		ArrayList<Object[]> nventas_por_dia = estadisticasVentas.consultarVentasPorDia("cantidad");
		DefaultCategoryDataset dataset2 = new DefaultCategoryDataset();
		
		for(Object[] d:dias){			
			Date dia_actual = ((Date)d[0]);
			String d1 = sdf.format(dia_actual);
			for(Object[] n:nventas_por_dia){
				Date dia = ((Date)n[0]);
				String d2 = sdf.format(dia);
				if(d1.equals(d2)){
					d[1]=n[1];
				}
			}
			if(d[1]==null){
				d[1]=0;
			}
			dataset2.addValue((double)d[1], "Numero de Ventas", sdf.format(d[0]));
		}
		crearGraficoDeBarras("Ventas por día - Últimos 30 días",PlotOrientation.VERTICAL,dataset2,panelDatos);
		

		calendario = Calendar.getInstance();
		calendario.setTime(new Date());
		calendario.add(Calendar.DAY_OF_YEAR, 1);
		Date hoy = calendario.getTime();
		calendario.add(Calendar.DAY_OF_YEAR, -30);
		
		//CREA EL GRAFICO DE FORMAS DE PAGO MAS USADAS
		ArrayList<Object[]> uso_formasdepago = estadisticasVentas.consultaFormasDePagoUsadas(calendario.getTime(),hoy);
		DefaultPieDataset dataset3 = new DefaultPieDataset();
		for(Object[] u:uso_formasdepago){
			dataset3.setValue((String)u[1],(int)u[2]);
		}
		JFreeChart chart3 = ChartFactory.createPieChart("Formas de Pago más usadas - Últimos 30 días", dataset3, true, true, false);
		PiePlot plot = (PiePlot)chart3.getPlot();
		plot.setLabelGenerator(new StandardPieSectionLabelGenerator("{0} \n({2})"));
		ChartPanel grafico3 = new ChartPanel(chart3);
		panelDatos.add(grafico3);
		grafico3.setPreferredSize(new Dimension(200,100));
		grafico3.addMouseListener(new MouseAdapter(){
			public void mouseClicked(MouseEvent e){
					new VentanaTortas(chart3);
			}
		});
		
		//CREA EL GRAFICO DE PRODUCTOS MAS VENDIDOS DE LOS ULTIMOS 30 DIAS
		ArrayList<Object[]> productos_mas_vendidos_ultimo_mes = estadisticasVentas.consultarProductosMasVendidos(calendario.getTime(),hoy);
		DefaultCategoryDataset dataset4 = new DefaultCategoryDataset();
		
		for(Object[] p:productos_mas_vendidos_ultimo_mes){
			dataset4.addValue((double)p[2], "Producto", String.format("%05d", (int)p[0]) + " - " + (String)p[1]);
		}
		int ncategorias = dataset4.getColumnCount();
		if(ncategorias<10){
			while(ncategorias<10){
				ncategorias++;
				dataset4.addValue(0.00, "Producto", ncategorias + "°");
			}
		}
		crearGraficoDeBarras("Productos más vendidos - Últimos 30 días",PlotOrientation.HORIZONTAL,dataset4,panelDatos);
		
		
		
		//CREA EL GRAFICO DE RUBROS MAS SOLICITADOS DE LOS ULTIMOS 30 DIAS
		ArrayList<Object[]> rubros_mas_vendidos_ultimo_mes = estadisticasVentas.consultarRubrosMasVendidos(calendario.getTime(),hoy);
		DefaultCategoryDataset dataset5 = new DefaultCategoryDataset();
		for(Object[] p:rubros_mas_vendidos_ultimo_mes){
			dataset5.addValue((double)p[2], "Rubro", (String)p[1]);
		}		
		ncategorias = dataset5.getColumnCount();
		if(ncategorias<5){
			while(ncategorias<5){
				ncategorias++;
				dataset5.addValue(0.00, "Rubro", ncategorias + "°");
			}
		}
		crearGraficoDeBarras("Rubros más solicitados - Últimos 30 días",PlotOrientation.HORIZONTAL,dataset5,panelDatos);
		
	}
	
	//METODO QUE CREA LOS GRÁFICOS DE BARRAS
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
}
