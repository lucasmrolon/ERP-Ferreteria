package vista.estadisticas;

import java.awt.Color;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.BorderFactory;
import javax.swing.border.BevelBorder;

import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.plot.PlotOrientation;

//MAXIMIZA EL GRAFICO DE BARRAS
public class VentanaBarras extends VentanaTortas{
		/**
	 * 
	 */
	private static final long serialVersionUID = 1365855683700040365L;

		public VentanaBarras(JFreeChart chart,PlotOrientation orientacion){
			super(chart);
			this.setTitle("Ventas por Día");
			this.getRootPane().setBorder(BorderFactory.createSoftBevelBorder(BevelBorder.RAISED, Color.GRAY, Color.GRAY));
			CategoryAxis axis = chart.getCategoryPlot().getDomainAxis();
			axis.setVisible(true);
			addKeyListener(new KeyAdapter(){
				public void keyPressed(KeyEvent e2){
					if(e2.getKeyChar()==KeyEvent.VK_ESCAPE){
						axis.setVisible(false);
						dispose();
					}
				}
			});
			
		}
	}


