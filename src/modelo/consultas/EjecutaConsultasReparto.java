package modelo.consultas;

import java.sql.*;
import java.util.ArrayList;
import java.util.Date;

import modelo.Conexion;
import modelo.objetos.LineaReparto;
import modelo.objetos.Reparto;

public class EjecutaConsultasReparto {

	//OBTIENE LISTA DE REPARTOS REALIZADOS
	public ArrayList<Object[]> obtenerTodosLosRepartos(){
		
		miConexion = new Conexion();
		conexion = miConexion.dameConexion();
		
		rs=null;
		ArrayList<Object[]> repartos = new ArrayList<Object[]>();
		
		try{
		
			//EJECUTA LA CONSULTA Y ANALIZA LOS RESULTADOS
			envia_consulta=conexion.createStatement();

			rs = envia_consulta.executeQuery(obtiene_repartos);

			while(rs.next()){
				int codigo = rs.getInt("CodigoReparto");
				Date creado = new Date(rs.getTimestamp("FechaCreacion").getTime());
				Date confirm = new Date(rs.getTimestamp("FechaConfirm").getTime());
				String empleado = rs.getString("Usuario");
				
				
				Object[] reparto = {codigo,creado,confirm,empleado};
				
				repartos.add(reparto);
			}
			
			
			return repartos;
			
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}finally{
			try {
				conexion.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	
	//OBTIENE LINEAS DEL REPARTO SELECCIONADO
	public ArrayList<Object[]> obtenerDetalleLineas(int n_reparto){
		
		//ESTABLECE LA CONEXION
		miConexion=new Conexion();
		conexion = miConexion.dameConexion();
					
		rs=null;
		ArrayList<Object[]> lineasdereparto = new ArrayList<Object[]>();
		
		try{

			//EJECUTA LA CONSULTA Y ANALIZA LOS RESULTADOS
			envia_consulta_preparada=conexion.prepareStatement(obtener_lineas);
			envia_consulta_preparada.setInt(1, n_reparto);

			rs = envia_consulta_preparada.executeQuery();

			while(rs.next()){
				int codigo = rs.getInt("CodigoLinea");
				int venta = rs.getInt("CodigoVenta");
				String producto = rs.getString("Descripcion");
				String nombreyapellido = rs.getString("NombreyApellido");
				String direccion = rs.getString("Direccion");
				String turno = rs.getString("Turno");
				String observaciones = rs.getString("Observaciones");
				
				Object[] linea = {codigo,venta,producto,nombreyapellido,direccion,turno,observaciones};
				
				lineasdereparto.add(linea);
			}
			
			
			return lineasdereparto;
				
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}finally{
			try {
				conexion.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	
	//DEVUELVE LA LISTA DE REPARTOS PENDIENTES ALMACENADA EN LA BD
	public ArrayList<LineaReparto> consultaLineasRepartoPendientes() {
			
		//ESTABLECE LA CONEXION
		miConexion=new Conexion();
		conexion = miConexion.dameConexion();
			
		rs=null;
		ArrayList<LineaReparto> lineasdereparto = new ArrayList<LineaReparto>();
			
		try{

			//EJECUTA LA CONSULTA Y ANALIZA LOS RESULTADOS
			envia_consulta=conexion.createStatement();

			rs = envia_consulta.executeQuery(consulta_lineas_reparto_pendientes);

			while(rs.next()){
				LineaReparto linea = new LineaReparto(rs.getString("NombreyApellido"),rs.getString("Direccion"),
						rs.getString("Turno"),rs.getString("Observaciones"),rs.getInt("LineasDeVenta_CodigoLinea"),true);
				linea.setCodigo_linea(rs.getInt("CodigoLinea"));
				lineasdereparto.add(linea);
			}
			
			
			return lineasdereparto;
				
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}finally{
			try {
				conexion.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	
	//CREA UN REPARTO Y ASIGNA LINEAS DE REPARTO
	public Object[] creaRepartoyAsignaLineas(int[] lineasdereparto){
		
		miConexion=new Conexion();
		conexion = miConexion.dameConexion();	
		rs = null;
		int codigo = 0;
		try{
			conexion.setAutoCommit(false);
		
			envia_consulta_preparada = conexion.prepareStatement(crea_reparto);
			envia_consulta = conexion.createStatement();
			Date fecha = new Date();
			envia_consulta_preparada.setTimestamp(1, new java.sql.Timestamp(fecha.getTime()));
			envia_consulta_preparada.execute();
			
			rs = envia_consulta.executeQuery(obtiene_n_reparto);
			while(rs.next()){
				codigo = rs.getInt("codigo");
			}
			envia_consulta_preparada=conexion.prepareStatement(asigna_linea_a_reparto);
			for(int i=0;i<lineasdereparto.length;i++){
				envia_consulta_preparada.setInt(1,codigo);
				envia_consulta_preparada.setInt(2, lineasdereparto[i]);
				envia_consulta_preparada.execute();
			}
			
			conexion.commit();
			
			return new Object[]{codigo,fecha};
		}catch(Exception e){
			try {
				conexion.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			e.printStackTrace();
			return null;
		}finally{
			try {
				conexion.close();
				
			} catch (SQLException e) {
				System.out.println("No se pudo cerrar la conexión");
				e.printStackTrace();
			}	
		}
	}
	
	//MUESTRA LISTA DE REPARTOS PENDIENTES
	public ArrayList<Reparto> consultaRepartosPendientes(){
		miConexion=new Conexion();
		conexion = miConexion.dameConexion();	
		rs = null;
		ArrayList<Reparto> repartosPendientes = new ArrayList<Reparto>();
		
		try{

			//EJECUTA LA CONSULTA Y ANALIZA LOS RESULTADOS
			envia_consulta=conexion.createStatement();

			rs = envia_consulta.executeQuery(consulta_repartos_pendientes);
			while(rs.next()){
				int n_reparto = rs.getInt("CodigoReparto");
				Date creado = new Date(rs.getTimestamp("FechaCreacion").getTime());
				
				Reparto reparto = new Reparto(n_reparto,creado);
				repartosPendientes.add(reparto);
			}
			
			return repartosPendientes;
				
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}finally{
			try {
				conexion.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	
	//OBTIENE LINEAS DE REPARTO ASOCIADAS A UN REPARTO
	public ArrayList<LineaReparto> obtenerLineasdeReparto(int n_reparto){

		miConexion=new Conexion();
		conexion = miConexion.dameConexion();	
		rs = null;
		ArrayList<LineaReparto> lineas = new ArrayList<LineaReparto>();
		
		try{

			envia_consulta_preparada = conexion.prepareStatement(obtiene_lineas_asignadas);
			envia_consulta_preparada.setInt(1, n_reparto);
			rs = envia_consulta_preparada.executeQuery();
			
			while(rs.next()){
				LineaReparto linea = new LineaReparto(rs.getString("NombreyApellido"),rs.getString("Direccion"),
						rs.getString("Turno"),rs.getString("Observaciones"),rs.getInt("LineasDeVenta_CodigoLinea"),true);
				linea.setCodigo_linea(rs.getInt("CodigoLinea"));
				lineas.add(linea);
			}

			return lineas;
		}catch(Exception e){
			System.out.println("No se pudo obtener las lineas asociadas");
			e.printStackTrace();
			return null;
		}finally{
			try {
				conexion.close();
			} catch (SQLException e) {
				System.out.println("No se pudo cerrar la conexión");
				e.printStackTrace();
			}
		}
	}
	
	//LIBERA UNA LINEA DE REPARTO DE UN REPARTO
	public boolean liberarLineaReparto(int n_linea){
		miConexion=new Conexion();
		conexion = miConexion.dameConexion();	
		
		try{

			//EJECUTA LA CONSULTA Y ANALIZA LOS RESULTADOS
			envia_consulta_preparada = conexion.prepareStatement(libera_linea_asignada_a_reparto);
			envia_consulta_preparada.setInt(1, n_linea);
			envia_consulta_preparada.execute();
			return true;
		}catch(Exception e){
			e.printStackTrace();
			return false;
		}finally{
			try {
				conexion.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
	}
	
	//CONFIRMA REALIZACION DE UN REPARTO
	public boolean confirmarReparto(int n_reparto,int cod_usuario, Date confirmado){
		
		miConexion=new Conexion();
		conexion = miConexion.dameConexion();	
		try{

			//EJECUTA LA CONSULTA Y ANALIZA LOS RESULTADOS
			envia_consulta_preparada = conexion.prepareStatement(confirma_reparto);
			envia_consulta_preparada.setInt(1, cod_usuario);
			envia_consulta_preparada.setTimestamp(2, new java.sql.Timestamp(confirmado.getTime()));
			envia_consulta_preparada.setInt(3, n_reparto);
			envia_consulta_preparada.execute();
			return true;
		}catch(Exception e){
			e.printStackTrace();
			return false;
		}finally{
			try {
				conexion.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	
	//ELIMINIA UNA LINEA DE REPARTO SIN ASOCIAR
	public boolean eliminarLineaRepartoPendiente(int n_linea, String observacion){
		
		miConexion=new Conexion();
		conexion = miConexion.dameConexion();	
		try{

			//EJECUTA LA CONSULTA Y ANALIZA LOS RESULTADOS
			envia_consulta_preparada = conexion.prepareStatement(eliminar_linea_reparto);
			envia_consulta_preparada.setString(1, observacion);
			envia_consulta_preparada.setInt(2, n_linea);
			envia_consulta_preparada.execute();
			return true;
		}catch(Exception e){
			e.printStackTrace();
			return false;
		}finally{
			try {
				conexion.close();
			} catch (SQLException e) {
				System.out.println("No se pudo cerrar la conexión");
				e.printStackTrace();
			}
		}
		
	}
	
	Conexion miConexion;
	
	Connection conexion;
	ResultSet rs;
	Statement envia_consulta;
	PreparedStatement envia_consulta_preparada;
	
	String obtiene_repartos = "SELECT CodigoReparto, FechaCreacion, empleados.Usuario, FechaConfirm "+
			"FROM repartos INNER JOIN empleados "+
			"WHERE empleados.IdEmpleado = Empleados_IdEmpleado "+
			"ORDER BY CodigoReparto";
	String consulta_lineas_reparto_pendientes = "SELECT * FROM lineasdereparto WHERE Pendiente = 1 ORDER BY CodigoLinea";
	String crea_reparto = "INSERT INTO repartos(FechaCreacion) VALUES(?)";
	String obtiene_n_reparto = "SELECT MAX(CodigoReparto) AS codigo FROM repartos";
	String asigna_linea_a_reparto = "UPDATE lineasdereparto SET Repartos_CodigoReparto = ?, Pendiente = 0 WHERE CodigoLinea = ?";
	String consulta_repartos_pendientes = "SELECT * FROM repartos WHERE FechaConfirm IS NULL";
	String obtiene_lineas_asignadas = "SELECT * FROM lineasdereparto WHERE Repartos_CodigoReparto = ?";
	String libera_linea_asignada_a_reparto = "UPDATE lineasdereparto SET Pendiente = 1,Repartos_CodigoReparto = NULL WHERE CodigoLinea = ?";
	String confirma_reparto = "UPDATE repartos Set Empleados_IdEmpleado = ?,FechaConfirm = ? WHERE CodigoReparto = ?";
	String eliminar_linea_reparto = "UPDATE lineasdereparto SET Pendiente = 0, Observaciones = ?, Repartos_CodigoReparto = NULL WHERE CodigoLinea = ?";
	String obtener_linea_reparto_asociada = "SELECT CodigoLinea FROM lineasdereparto WHERE LineasDeVenta_CodigoLinea = ?";
	String obtener_lineas = "SELECT lineasdereparto.CodigoLinea, ventas.CodigoVenta, productos.Descripcion, "
			+ "NombreyApellido, Direccion, Turno, lineasdereparto.Observaciones "
			+ "FROM lineasdereparto INNER JOIN ventas INNER JOIN productos INNER JOIN lineasdeventa "
			+ "WHERE LineasDeVenta_CodigoLinea = lineasdeventa.CodigoLinea "
			+ "AND lineasdeventa.Ventas_CodigoVenta = ventas.CodigoVenta "
			+ "AND lineasdeventa.Productos_CodigoProducto = productos.CodigoProducto "
			+ "AND lineasdereparto.Repartos_CodigoReparto = ?";
}
