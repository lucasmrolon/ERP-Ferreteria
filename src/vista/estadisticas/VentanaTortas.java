package vista.estadisticas;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.BorderFactory;
import javax.swing.border.BevelBorder;

import org.jfree.chart.ChartFrame;
import org.jfree.chart.JFreeChart;

//MAXIMIZA GRAFICO DE TORTAS
public class VentanaTortas extends ChartFrame{
	/**
	 * 
	 */
	private static final long serialVersionUID = 5906602719078418367L;

	public VentanaTortas(JFreeChart chart){
		super("Uso de Formas de Pago",chart);
		this.setAlwaysOnTop(true);
		setSize(new Dimension(1000,600));
		getRootPane().setBorder(BorderFactory.createSoftBevelBorder(BevelBorder.RAISED, Color.GRAY, Color.GRAY));
		setUndecorated(true);
		setLocationRelativeTo(null);
		setVisible(true);
		addKeyListener(new KeyAdapter(){
			public void keyPressed(KeyEvent e2){
				if(e2.getKeyChar()==KeyEvent.VK_ESCAPE){
					dispose();
				}
			}
		});
	}
}
