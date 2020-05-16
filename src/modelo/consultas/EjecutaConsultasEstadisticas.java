package modelo.consultas;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;

import javax.swing.JOptionPane;

import modelo.Conexion;

public class EjecutaConsultasEstadisticas {

	//OBTIENE NUMERO Y MONTO DE VENTAS DIARIAS
	public ArrayList<Object[]> consultarVentasPorDia(String unidad) {
		miconexion = new Conexion();
		conexion = miconexion.dameConexion();
		ArrayList<Object[]> ventas_por_dia = new ArrayList<Object[]>(); 
		rs=null;
		
		try{
			envia_consulta = conexion.createStatement();
			if(unidad.equals("monto")){
				rs = envia_consulta.executeQuery(consulta_montoventas_por_dia);
			}else if(unidad.equals("cantidad")){
				rs = envia_consulta.executeQuery(consulta_nventas_por_dia);
			}
			
			//GUARDA EL NÚMERO DE VENTAS
			while(rs.next()){
				Object[] nuevo_elemento = new Object[2];

				java.util.Date fecha = new java.util.Date(rs.getTimestamp("DATE(Fecha)").getTime());
				nuevo_elemento[0]=fecha;
				if(unidad.equals("monto")){
					nuevo_elemento[1]=rs.getDouble("SUM(MontoTotal + RecDesc)");
				}else if(unidad.equals("cantidad")){
					nuevo_elemento[1]=rs.getDouble("COUNT(CodigoVenta)");
				}

				ventas_por_dia.add(nuevo_elemento);
			}
			return ventas_por_dia;
		}catch(Exception e){
			String mensaje = "<html><Font size=5>¡No se pudieron obtener datos de ventas diarias!</Font></html>";
			JOptionPane.showMessageDialog(null, mensaje, "¡Error!", JOptionPane.WARNING_MESSAGE);
			return ventas_por_dia;
		}
		finally{
			try {
				conexion.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
	}
	
	//OBTIENE EL NUMERO Y MONTO DE VENTAS MENSUALES
	public ArrayList<Object[]> consultarVentasPorMes(String unidad) {
		miconexion = new Conexion();
		conexion = miconexion.dameConexion();
		ArrayList<Object[]> ventas_por_mes = new ArrayList<Object[]>(); 
		rs=null;
		
		try{
			envia_consulta = conexion.createStatement();
			if(unidad.equals("monto")){
				rs = envia_consulta.executeQuery(consulta_montoventas_por_mes);
			}else if(unidad.equals("cantidad")){
				rs = envia_consulta.executeQuery(consulta_nventas_por_mes);
			}
			
			//GUARDA EL NÚMERO DE VENTAS
			while(rs.next()){
				Object[] nuevo_elemento = new Object[3];

				int mes = rs.getInt("MONTH(Fecha)");
				int anio = rs.getInt("YEAR(Fecha)");
				
				nuevo_elemento[0]=mes;
				nuevo_elemento[1]=anio;
				if(unidad.equals("monto")){
					nuevo_elemento[2]=rs.getDouble("SUM(MontoTotal + RecDesc)");
				}else if(unidad.equals("cantidad")){
					nuevo_elemento[2]=rs.getDouble("COUNT(CodigoVenta)");
				}
				ventas_por_mes.add(nuevo_elemento);
			}
			return ventas_por_mes;
		}catch(Exception e){
			e.printStackTrace();
			String mensaje = "<html><Font size=5>¡No se pudieron obtener datos mensuales sobre ventas!</Font></html>";
			JOptionPane.showMessageDialog(null, mensaje, "¡Error!", JOptionPane.WARNING_MESSAGE);
			return ventas_por_mes;
		}
		finally{
			try {
				conexion.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
	}
	
	//OBTIENE EL NUMERO Y MONTO DE VENTAS ANUALES
	public ArrayList<Object[]> consultarVentasPorAnio(String unidad) {
		miconexion = new Conexion();
		conexion = miconexion.dameConexion();
		ArrayList<Object[]> ventas_por_anio = new ArrayList<Object[]>(); 
		rs=null;
		
		try{
			envia_consulta = conexion.createStatement();
			if(unidad.equals("monto")){
				rs = envia_consulta.executeQuery(consulta_montoventas_por_anio);
			}else if(unidad.equals("cantidad")){
				rs = envia_consulta.executeQuery(consulta_nventas_por_anio);
			}
			
			//GUARDA EL NÚMERO DE VENTAS
			while(rs.next()){
				Object[] nuevo_elemento = new Object[2];
														
				nuevo_elemento[0] = rs.getInt("YEAR(Fecha)");
				
				if(unidad.equals("monto")){
					nuevo_elemento[1]=rs.getDouble("SUM(MontoTotal + RecDesc)");
				}else if(unidad.equals("cantidad")){
					nuevo_elemento[1]=rs.getDouble("COUNT(CodigoVenta)");
				}
				ventas_por_anio.add(nuevo_elemento);
			}
			return ventas_por_anio;
		}catch(Exception e){
			e.printStackTrace();
			String mensaje = "<html><Font size=5>¡No se pudieron obtener datos anuales de ventas!</Font></html>";
			JOptionPane.showMessageDialog(null, mensaje, "¡Error!", JOptionPane.WARNING_MESSAGE);
			return ventas_por_anio;
		}
		finally{
			try {
				conexion.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
	}
	
	//OBTIENE LAS FORMAS DE PAGO MAS USADAS
	public ArrayList<Object[]> consultaFormasDePagoUsadas(Date despues_de,Date antes_de){
		
		miconexion = new Conexion();
		conexion = miconexion.dameConexion();
		ArrayList<Object[]> formas_de_pago = new ArrayList<Object[]>(); 
		rs=null;
		try{
			envia_consulta_preparada = conexion.prepareStatement(consulta_formas_de_pago_usadas);
			envia_consulta_preparada.setDate(1, new java.sql.Date(despues_de.getTime()));
			envia_consulta_preparada.setDate(2, new java.sql.Date(antes_de.getTime()));
			rs = envia_consulta_preparada.executeQuery();
			
			while(rs.next()){
				Object[] nuevo_elemento = new Object[3];

				nuevo_elemento[0]=rs.getInt("Codigo");
				nuevo_elemento[1]=rs.getString("Tipo");
				nuevo_elemento[2] = rs.getInt("NVentas");
				formas_de_pago.add(nuevo_elemento);
			}
			return formas_de_pago;
		}catch(Exception e){
			e.printStackTrace();
			String mensaje = "<html><Font size=5>¡No se pudieron obtener estadísticas de formas de pago!</Font></html>";
			JOptionPane.showMessageDialog(null, mensaje, "¡Error!", JOptionPane.WARNING_MESSAGE);
			return formas_de_pago;
		}
		finally{
			try {
				conexion.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	
	//OBTIENE LOS PRODUCTOS MAS VENDIDOS
	public ArrayList<Object[]> consultarProductosMasVendidos(Date despues_de, Date antes_de){
		miconexion = new Conexion();
		conexion = miconexion.dameConexion();
		ArrayList<Object[]> productos_mas_vendidos = new ArrayList<Object[]>(); 
		rs=null;
		try{
			envia_consulta_preparada = conexion.prepareStatement(consulta_mas_vendido);
			envia_consulta_preparada.setDate(1, new java.sql.Date(despues_de.getTime()));
			envia_consulta_preparada.setDate(2, new java.sql.Date(antes_de.getTime()));
			rs = envia_consulta_preparada.executeQuery();
			while(rs.next() && productos_mas_vendidos.size()<10){
				Object[] nuevo_elemento = new Object[3];

				nuevo_elemento[0] = rs.getInt("productos.CodigoProducto");
				nuevo_elemento[1] = rs.getString("productos.Descripcion");
				nuevo_elemento[2] = rs.getDouble("SUM(lineasdeventa.Cantidad)");
				
				productos_mas_vendidos.add(nuevo_elemento);
			}
			return productos_mas_vendidos;
		}catch(Exception e){
			e.printStackTrace();
			String mensaje = "<html><Font size=5>¡No se pudieron obtener estadísticas de productos!</Font></html>";
			JOptionPane.showMessageDialog(null, mensaje, "¡Error!", JOptionPane.WARNING_MESSAGE);
			return productos_mas_vendidos;
		}
		finally{
			try {
				conexion.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	//OBTIENE LOS RUBROS MAS SOLICITADOS
	public ArrayList<Object[]> consultarRubrosMasVendidos(Date despues_de, Date antes_de){
		miconexion = new Conexion();
		conexion = miconexion.dameConexion();
		ArrayList<Object[]> rubros_mas_vendidos = new ArrayList<Object[]>(); 
		rs=null;
		try{
			envia_consulta_preparada = conexion.prepareStatement(consulta_rubro_mas_vendido);
			envia_consulta_preparada.setDate(1, new java.sql.Date(despues_de.getTime()));
			envia_consulta_preparada.setDate(2, new java.sql.Date(antes_de.getTime()));
			rs = envia_consulta_preparada.executeQuery();
			while(rs.next() && rubros_mas_vendidos.size()<5){
				Object[] nuevo_elemento = new Object[3];
				nuevo_elemento[0] = rs.getInt("CodigoRubro");
				nuevo_elemento[1] = rs.getString("Nombre");
				nuevo_elemento[2] = rs.getDouble("SUM(lineasdeventa.Cantidad)");
				
				rubros_mas_vendidos.add(nuevo_elemento);
			}
			return rubros_mas_vendidos;
		}catch(Exception e){
			e.printStackTrace();
			String mensaje = "<html><Font size=5>¡No se pudo obtener estadísticas de rubros!</Font></html>";
			JOptionPane.showMessageDialog(null, mensaje, "¡Error!", JOptionPane.WARNING_MESSAGE);
			return rubros_mas_vendidos;
		}
		finally{
			try {
				conexion.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	
	//OBTIENE TOTALES DE PRODUCTOS, VENTAS, REPARTOS, INGRESOS, EGRESOS
	public double[] obtenerTotales(){
		miconexion = new Conexion();
		conexion = miconexion.dameConexion();
		double[] totales = new double[7];
		rs=null;
		try{
			envia_consulta = conexion.createStatement();
			rs = envia_consulta.executeQuery(tot_venta);
			while(rs.next()){
				totales[0] = rs.getInt("totalventas");
			}
			envia_consulta = conexion.createStatement();
			rs = envia_consulta.executeQuery(tot_rep);
			while(rs.next()){
				totales[1] = rs.getInt("totalrepartos");
			}
			rs = envia_consulta.executeQuery(prod_rep);
			while(rs.next()){
				totales[2] = rs.getInt("prodrepartidos");
			}
			rs = envia_consulta.executeQuery(productos_reg);
			while(rs.next()){
				totales[3] = rs.getInt("COUNT(productos.CodigoProducto)");
			}
			rs = envia_consulta.executeQuery(ingresos_por_venta);
			while(rs.next()){
				totales[4] = rs.getDouble("SUM(ventas.MontoTotal+ventas.RecDesc)");
			}
			rs = envia_consulta.executeQuery(capital_en_almacen);
			while(rs.next()){
				totales[5] = rs.getDouble("SUM(Precio*Cantidad)");
			}
			rs = envia_consulta.executeQuery(egresos_por_compra);
			while(rs.next()){
				totales[6] = rs.getDouble("SUM(lineasdepedido.Subtotal)");
			}
			return totales;
		}catch(Exception e){
			e.printStackTrace();
			String mensaje = "<html><Font size=5>¡No se pudieron obtener algunos datos estadísticos!</Font></html>";
			JOptionPane.showMessageDialog(null, mensaje, "¡Error!", JOptionPane.WARNING_MESSAGE);
			return totales;
		}
		finally{
			try {
				conexion.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	
	
	Conexion miconexion;
	Connection conexion;
	Statement envia_consulta;
	PreparedStatement envia_consulta_preparada;
	ResultSet rs;
	
	
	String consulta_montoventas_por_dia = "SELECT DATE(Fecha), SUM(MontoTotal + RecDesc) FROM ventas "
			+ "WHERE FormasDePago_CodigoForma IS NOT NULL "
			+ "AND Fecha BETWEEN date_sub(NOW(), INTERVAL 30 DAY) AND NOW() GROUP BY DATE(Fecha)";
	String consulta_montoventas_por_mes = "SELECT MONTH(Fecha), YEAR(Fecha), SUM(MontoTotal + RecDesc) FROM ventas "
			+ "WHERE FormasDePago_CodigoForma IS NOT NULL "
			+ "AND Fecha BETWEEN date_sub(NOW(), INTERVAL 12 MONTH) AND NOW() GROUP BY MONTH(Fecha), YEAR(Fecha)";
	String consulta_montoventas_por_anio = "SELECT YEAR(Fecha), SUM(MontoTotal + RecDesc) FROM ventas "
			+ "WHERE FormasDePago_CodigoForma IS NOT NULL "
			+ "AND Fecha BETWEEN date_sub(NOW(), INTERVAL 10 YEAR) AND NOW() GROUP BY YEAR(Fecha)";
	String consulta_nventas_por_dia = "SELECT DATE(Fecha), COUNT(CodigoVenta) FROM ventas "
			+ "WHERE FormasDePago_CodigoForma IS NOT NULL "
			+ "AND Fecha BETWEEN date_sub(NOW(), INTERVAL 30 DAY) AND NOW() GROUP BY DATE(Fecha)";
	String consulta_nventas_por_mes = "SELECT MONTH(Fecha), YEAR(Fecha), COUNT(CodigoVenta) FROM ventas "
			+ "WHERE FormasDePago_CodigoForma IS NOT NULL "
			+ "AND Fecha BETWEEN date_sub(NOW(), INTERVAL 12 MONTH) AND NOW() GROUP BY MONTH(Fecha), YEAR(Fecha)";
	String consulta_nventas_por_anio = "SELECT YEAR(Fecha), COUNT(CodigoVenta) FROM ventas "
			+ "WHERE FormasDePago_CodigoForma IS NOT NULL "
			+ "AND Fecha BETWEEN date_sub(NOW(), INTERVAL 10 YEAR) AND NOW() GROUP BY YEAR(Fecha)";
	String consulta_formas_de_pago_usadas= "SELECT FormasDePago_CodigoForma as Codigo,formasdepago.Tipo as Tipo,COUNT(FormasDePago_CodigoForma) AS NVentas "
			+ "FROM ventas INNER JOIN formasdepago "
			+ "WHERE FormasDePago_CodigoForma=formasdepago.CodigoForma "
			+ "AND ventas.Fecha BETWEEN ? AND ? "
			+ "GROUP BY Codigo "
			+ "ORDER BY NVentas DESC";
	String consulta_mas_vendido = "SELECT productos.CodigoProducto, productos.Descripcion, SUM(lineasdeventa.Cantidad) FROM productos "
			+ "INNER JOIN lineasdeventa INNER JOIN ventas "
			+ "WHERE productos.CodigoProducto=lineasdeventa.Productos_CodigoProducto "
			+ "AND lineasdeventa.Ventas_CodigoVenta=ventas.CodigoVenta "
			+ "AND ventas.Fecha BETWEEN ? AND ? "
			+ "GROUP BY lineasdeventa.Productos_CodigoProducto "
			+ "ORDER BY SUM(lineasdeventa.Cantidad) DESC";
	String consulta_rubro_mas_vendido = "SELECT rubro.CodigoRubro, rubro.Nombre, SUM(lineasdeventa.Cantidad) FROM productos "+
			"INNER JOIN lineasdeventa INNER JOIN ventas INNER JOIN rubro "+
			"WHERE productos.CodigoProducto=lineasdeventa.Productos_CodigoProducto "+
			"AND lineasdeventa.Ventas_CodigoVenta=ventas.CodigoVenta "+
            "AND rubro.CodigoRubro=productos.Rubro_CodigoRubro "+
			"AND ventas.Fecha BETWEEN ? AND ? "+
			"GROUP BY rubro.CodigoRubro "+
			"ORDER BY SUM(lineasdeventa.Cantidad) DESC";
	String tot_venta = "SELECT COUNT(ventas.CodigoVenta) AS totalventas FROM ventas WHERE ventas.Fecha IS NOT NULL "
			+ "AND FormasDePago_CodigoForma IS NOT NULL";
	String tot_rep= "SELECT MAX(repartos.CodigoReparto) AS totalrepartos "
			+ "FROM repartos WHERE "
			+ "repartos.FechaConfirm IS NOT NULL ";
	String prod_rep = "SELECT COUNT(lineasdereparto.CodigoLinea) AS prodrepartidos FROM lineasdereparto INNER JOIN repartos "
			+ "WHERE lineasdereparto.Repartos_CodigoReparto = repartos.CodigoReparto "
			+ "AND lineasdereparto.Pendiente=0 "
			+ "AND lineasdereparto.Observaciones NOT LIKE 'ELIM%' "
			+ "AND repartos.FechaConfirm IS NOT NULL";
	String productos_reg = "SELECT COUNT(productos.CodigoProducto) from productos WHERE Eliminado IS NULL";
	String ingresos_por_venta = "SELECT SUM(ventas.MontoTotal+ventas.RecDesc) from ventas "
			+ "WHERE Fecha IS NOT NULL AND FormasDePago_CodigoForma IS NOT NULL";
	String egresos_por_compra = "SELECT SUM(lineasdepedido.Subtotal) from lineasdepedido "
			+ "WHERE Recibido = 1";
	String capital_en_almacen = "SELECT SUM(Precio*Cantidad) FROM productos WHERE Eliminado IS NULL";
	
}
