package vista.estadisticas;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import javax.swing.BorderFactory;
import javax.swing.JComboBox;
import javax.swing.border.BevelBorder;

import org.jfree.chart.*;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;

import modelo.consultas.EjecutaConsultasEstadisticas;

//MAXIMIZA GRAFICO DE BARRAS DE PERIODO SELECCIONABLE
public class VentanaBarrasEditable extends VentanaBarras{
	/**
	 * 
	 */
	private static final long serialVersionUID = 6345861130954783027L;

	public VentanaBarrasEditable(ArrayList<Object[]> categorias, JFreeChart chart, String modo, PlotOrientation orientacion, String unidad, SimpleDateFormat sdf){
		super(chart,orientacion);
		setLayout(new BorderLayout());
		this.getRootPane().setBorder(BorderFactory.createSoftBevelBorder(BevelBorder.RAISED, Color.GRAY, Color.GRAY));
		JComboBox<String> paraUnidades = new JComboBox<String>();
		for(Object[] o:categorias){
			paraUnidades.addItem(sdf.format((Date)o[0]));
		}
		paraUnidades.setSelectedItem(sdf.format(new Date()));
		paraUnidades.addActionListener(new CambiarMesProductosORubros(paraUnidades,this,modo,sdf,unidad));
		add(paraUnidades,BorderLayout.NORTH);
		CategoryAxis axis = chart.getCategoryPlot().getDomainAxis();
		axis.setVisible(true);
		paraUnidades.addKeyListener(new KeyAdapter(){
			public void keyPressed(KeyEvent e2){
				if(e2.getKeyChar()==KeyEvent.VK_ESCAPE){
					axis.setVisible(false);
					dispose();
				}
			}
		});
	}
	
	//PERMITE ELEGIR MES DE ANALISIS
	class CambiarMesProductosORubros implements ActionListener{
		
		public CambiarMesProductosORubros(JComboBox<String> seleccion,ChartFrame ventana, String modo, SimpleDateFormat sdf,String unidad){
			this.seleccion=seleccion;	
			this.ventana=ventana;
			this.modo=modo;
			this.sdf=sdf;
			this.unidad=unidad;
		}

		@Override
		public void actionPerformed(ActionEvent arg0) {
			String seleccionado = (String)seleccion.getSelectedItem();
			Date fecha_selec=null;
			try {
				fecha_selec = sdf.parse(seleccionado);
			} catch (ParseException e) {
				e.printStackTrace();
			}
			Calendar calendario = Calendar.getInstance();
			calendario.setTime(fecha_selec);
			if(unidad.equals("mes")){
				calendario.add(Calendar.MONTH, 1);
			}else if(unidad.equals("año")){
				calendario.add(Calendar.YEAR, 1);
			}
			EjecutaConsultasEstadisticas nuevaConsulta = new EjecutaConsultasEstadisticas();
			String nombre_serie="";
			if(modo.equals("rubros")){
				mas_vendidos_por_periodo = nuevaConsulta.consultarRubrosMasVendidos(fecha_selec, calendario.getTime());
				nombre_serie="Rubro";
				maxcateg=5;
			}else if(modo.equals("productos")){
				mas_vendidos_por_periodo = nuevaConsulta.consultarProductosMasVendidos(fecha_selec, calendario.getTime());
				nombre_serie="Producto";
				maxcateg=10;
			}
			DefaultCategoryDataset dataset = new DefaultCategoryDataset();
			
			for(Object[] v:mas_vendidos_por_periodo){
				if(modo.equals("productos")){
					dataset.addValue((double)v[2], nombre_serie, (String)v[1]);
				}else if(modo.equals("rubros")){
					dataset.addValue((double)v[2], nombre_serie, (String)v[1]);
				}
			}
			int ncategorias = dataset.getColumnCount();
			if(ncategorias<maxcateg){
				while(ncategorias<maxcateg){
					ncategorias++;
					dataset.addValue(0,nombre_serie, ncategorias + "°");
				}
			}
			
			JFreeChart chart = ChartFactory.createBarChart("MÁS VENDIDOS", null, null, dataset,PlotOrientation.HORIZONTAL,true,true,false);
			CategoryAxis axis = chart.getCategoryPlot().getDomainAxis();
			axis.setVisible(false);
			new ChartPanel(chart);
			ventana.getChartPanel().setChart(chart);
			chart.getCategoryPlot().getDomainAxis().setVisible(true);
			
		}
		ArrayList<Object[]> mas_vendidos_por_periodo;
		JComboBox<String> seleccion;
		ChartFrame ventana;
		String modo;
		int maxcateg;
		String nombrecat,unidad;
		SimpleDateFormat sdf;
	}
}
