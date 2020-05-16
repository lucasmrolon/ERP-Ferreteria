package controlador.repartos;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JOptionPane;
import javax.swing.JTable;

import modelo.Conexion;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.view.JasperViewer;

//CLASE QUE PERMITE OBTENER UNA HOJA DE REPARTOS IMPRIMIBLE
public class ImprimirHojaReparto implements ActionListener{
	
	public ImprimirHojaReparto(JTable tablaRepartosPendientes, JTable tablaInfoReparto){
		tabla = tablaRepartosPendientes;
		tabla_lineas = tablaInfoReparto;
	}
	
	@Override
	public void actionPerformed(ActionEvent arg0) {
		
		
		try{	
			
			int seleccionado = tabla.getSelectedRow();
			int cod_reparto = (int)tabla.getValueAt(seleccionado, 0);

			String turno = (String)tabla_lineas.getValueAt(0, 4);
			
			JasperReport reporte = null;
			Conexion conexion = new Conexion();
			
			//OBTIENE EL MODELO DE REPORTE
			reporte = (JasperReport)JRLoader.loadObjectFromFile("reportes/lista_reparto.jasper");
	
			//ENVÍA LOS PARAMETROS DE CREACION
			Connection miconexion = conexion.dameConexion();
			Map<String, Object> parametros = new HashMap<String, Object>();
			parametros.put("n_reparto", cod_reparto);
			parametros.put("turno_reparto", turno);
    
			JasperPrint jasperprint = JasperFillManager.fillReport(reporte,parametros,miconexion);
			//JasperPrintManager.printReport(jasperprint, false);

			try {
				miconexion.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			JasperViewer.viewReport(jasperprint, false);
		}catch(JRException e){
			e.printStackTrace();
			JOptionPane.showMessageDialog(null,"<html><font size=4>¡Error al imprimir el reporte! Intente nuevamente.</font></html>",
					"Imprimir reparto",JOptionPane.WARNING_MESSAGE);
		}catch(ArrayIndexOutOfBoundsException e){
			e.printStackTrace();
			JOptionPane.showMessageDialog(null,"<html><font size=4>¡Error! No ha seleccionado ningún reparto, o el reparto es inválido</font></html>",
					"Imprimir reparto",JOptionPane.WARNING_MESSAGE);
		}
		
	}
	
	JTable tabla,tabla_lineas;
}
