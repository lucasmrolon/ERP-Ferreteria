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
import org.jfree.chart.labels.StandardPieSectionLabelGenerator;
import org.jfree.chart.plot.PiePlot;
import org.jfree.data.general.DefaultPieDataset;

import modelo.consultas.EjecutaConsultasEstadisticas;

//MAXIMIZA GRAFICO DE TORTAS DE PERIODO EDITABLE
public class VentanaTortaEditable extends VentanaTortas{
	/**
	 * 
	 */
	private static final long serialVersionUID = -4629993361130569338L;

	public VentanaTortaEditable(ArrayList<Object[]> categorias, JFreeChart chart, String unidad, SimpleDateFormat sdf){
		super(chart);
		setLayout(new BorderLayout());
		this.getRootPane().setBorder(BorderFactory.createSoftBevelBorder(BevelBorder.RAISED, Color.GRAY, Color.GRAY));
		JComboBox<String> paraUnidades = new JComboBox<String>();
		for(Object[] o:categorias){
			paraUnidades.addItem(sdf.format((Date)o[0]));
		}
		paraUnidades.setSelectedItem(sdf.format(new Date()));
		paraUnidades.addActionListener(new CambiarMesFormasDePago(paraUnidades,this,sdf,unidad));
		add(paraUnidades,BorderLayout.NORTH);
		paraUnidades.addKeyListener(new KeyAdapter(){
			public void keyPressed(KeyEvent e2){
				if(e2.getKeyChar()==KeyEvent.VK_ESCAPE){
					dispose();
				}
			}
		});
	}
}

//PERMITE SELECCIONAR MES DE ANALISIS
class CambiarMesFormasDePago implements ActionListener{
	public CambiarMesFormasDePago(JComboBox<String> seleccion,ChartFrame ventana,SimpleDateFormat sdf, String unidad){
		this.seleccion=seleccion;
		this.ventana=ventana;
		this.sdf = sdf;
		this.unidad = unidad;
	}
	
	public void actionPerformed(ActionEvent e){
		String seleccionado = (String)seleccion.getSelectedItem();
		Date fecha_selec=null;
		try {
			fecha_selec = sdf.parse(seleccionado);
		} catch (ParseException ex) {
			ex.printStackTrace();
		}
		Calendar calendario = Calendar.getInstance();
		calendario.setTime(fecha_selec);
		
		if(unidad.equals("mes")){
			calendario.add(Calendar.MONTH, 1);
		}else if(unidad.equals("año")){
			calendario.add(Calendar.YEAR, 1);
		}
		EjecutaConsultasEstadisticas nuevaConsulta = new EjecutaConsultasEstadisticas();
		mas_usadas = nuevaConsulta.consultaFormasDePagoUsadas(fecha_selec, calendario.getTime());

		DefaultPieDataset dataset = new DefaultPieDataset();
		 
		for(Object[] f:mas_usadas){
			dataset.setValue((String)f[1],(int)f[2]);
		}
		JFreeChart chart = ChartFactory.createPieChart("Formas de Pago más usadas", dataset, true, true, false);
		PiePlot plot = (PiePlot)chart.getPlot();
		plot.setLabelGenerator(new StandardPieSectionLabelGenerator("{0} \n({2})"));
		//ChartPanel grafico = new ChartPanel(chart);
		//VentanaTortas nueva = new VentanaTortas(chart);
		//nueva.setVisible(true);
		ventana.getChartPanel().setChart(chart);
	}

	ArrayList<Object[]> mas_usadas;
	JComboBox<String> seleccion;
	ChartFrame ventana;
	SimpleDateFormat sdf;
	String unidad;
}
