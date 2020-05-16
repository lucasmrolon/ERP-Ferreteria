package modelo.consultas;

import java.sql.*;
import java.util.ArrayList;

import com.mysql.jdbc.exceptions.jdbc4.MySQLIntegrityConstraintViolationException;

import modelo.Conexion;
import modelo.objetos.LineaPedido;
import modelo.objetos.Pedido;
import modelo.objetos.Proveedor;

//CLASE QUE EJECUTA LAS CONSULTAS RELACIONADAS CON LOS PROVEEDORES
public class EjecutaConsultasProveedor {

	//CONSULTA EL CÓDIGO DE UN PROVEEDOR
	public String consultaCodProveedor(String proveedor){
		
		//ESTABLECE LA CONEXIÓN
		miConexion=new Conexion();
		Connection conexion = miConexion.dameConexion();
		
		String cod="";
		rs=null;
		
		try{
			
			//EJECUTA LA CONSULTA
			envia_consulta_preparada = conexion.prepareStatement(consulta_codigo);
			envia_consulta_preparada.setString(1,proveedor);
			rs = envia_consulta_preparada.executeQuery();

			//CAPTURA LOS RESULTADOS
			while(rs.next()){
				cod= rs.getString("CodigoProveedor");
			}
			
			//CIERRA LA CONEXIÓN
			conexion.close();
			
			return cod;

		}catch(Exception e){
			return "";
		}
	}
	
	//DEVUELVE LA LISTA DE PROVEEDORES ALMACENADOS EN LA BASE DE DATOS
	public ArrayList<Proveedor> consultaProveedores() {
		
		//ESTABLECE LA CONEXION
		miConexion=new Conexion();
		Connection conexion = miConexion.dameConexion();
		
		rs=null;
		ArrayList<Proveedor> proveedores = new ArrayList<Proveedor>();
		
		try{
			
			//EJECUTA LA CONSULTA Y ANALIZA LOS RESULTADOS
			envia_consulta=conexion.createStatement();
			rs = envia_consulta.executeQuery(consulta_proveedores);
			
			while(rs.next()){
				Proveedor proveedor=  new Proveedor(rs.getString("CodigoProveedor"),rs.getString("Nombre"),rs.getString("Domicilio"),rs.getString("Telefono"),
						rs.getString("Email"));
				proveedor.setListaprecios(rs.getString("ListaPrecios"));
				proveedores.add(proveedor);
			}
			
			//CIERRA LA CONEXIÓN
			conexion.close();
			return proveedores;
			
		}catch(Exception e){
			System.out.println("Consulta fallo");
			return null;
		}
	}
	
	//REGISTRA UN PROVEEDOR
	public String crearProveedor(Proveedor nuevo_proveedor){
		miConexion = new Conexion();
		Connection conexion = miConexion.dameConexion();
		
		try{
			envia_consulta_preparada = conexion.prepareStatement(crear_proveedor);
			envia_consulta_preparada.setString(1, nuevo_proveedor.getCodigo());
			envia_consulta_preparada.setString(2, nuevo_proveedor.getNombre());
			envia_consulta_preparada.setString(3, nuevo_proveedor.getDomicilio());
			envia_consulta_preparada.setString(4, nuevo_proveedor.getTelefono());
			envia_consulta_preparada.setString(5, nuevo_proveedor.getEmail());
			envia_consulta_preparada.execute();
			
			return "ok";
		
		}catch(MySQLIntegrityConstraintViolationException e2){
			return "existente";
		}catch(SQLException e){
			e.printStackTrace();
			return "fallo";
		}finally{
			try {
				conexion.close();
			} catch (SQLException e){
				e.printStackTrace();
			}
		}
	}
	
	//MODIFICA LOS DATOS DE UN PROVEEDOR
	public boolean modificarProveedor(Proveedor proveedor){
		miConexion = new Conexion();
		Connection conexion = miConexion.dameConexion();
		
		try{
			envia_consulta_preparada = conexion.prepareStatement(modificar_proveedor);
			envia_consulta_preparada.setString(1, proveedor.getNombre());
			envia_consulta_preparada.setString(2, proveedor.getDomicilio());
			envia_consulta_preparada.setString(3, proveedor.getTelefono());
			envia_consulta_preparada.setString(4, proveedor.getEmail());
			envia_consulta_preparada.setString(5, proveedor.getCodigo());
			envia_consulta_preparada.execute();
			
			return true;
		
		}catch(SQLException e){
			e.printStackTrace();
			return false;
		}finally{
			try {
				conexion.close();
			} catch (SQLException e){
				e.printStackTrace();
			}
		}
	}
	
	//ELIMINA EL REGISTRO DE UN PROVEEDOR
	public boolean eliminarProveedor(Proveedor proveedor){
		miConexion = new Conexion();
		Connection conexion = miConexion.dameConexion();
		
		try{
			envia_consulta_preparada = conexion.prepareStatement(eliminar_proveedor);
			envia_consulta_preparada.setString(1, proveedor.getCodigo());
			envia_consulta_preparada.execute();
			
			return true;
		}catch(SQLException e){
			e.printStackTrace();
			return false;
		}finally{
			try {
				conexion.close();
			} catch (SQLException e){
				e.printStackTrace();
			}
		}
	}
	
	//OBTIENE EL CODIGO PARA UN NUEVO PEDIDO
	public int obtenerNProxPedido(){
		
		miConexion = new Conexion();
		Connection conexion = miConexion.dameConexion();
		int n_pedido=0;
		
		try{
			envia_consulta = conexion.createStatement();
			rs = envia_consulta.executeQuery(obtener_n_sig_pedido);
			
			while(rs.next()){
				
				n_pedido = rs.getInt("MAX(CodigoPedido)") + 1;
				
			}
			
			return n_pedido;
		}catch(SQLException e){
			e.printStackTrace();
			return n_pedido;
		}finally{
			try {
				conexion.close();
			} catch (SQLException e){
				e.printStackTrace();
			}
		}
		
	}
	
	//REGISTRA UN PEDIDO NUEVO
	public boolean registrarNuevoPedido(Pedido nuevo_pedido, ArrayList<LineaPedido> lineasdepedido){
		
		miConexion = new Conexion();
		Connection conexion = miConexion.dameConexion();
		try{
			conexion.setAutoCommit(false);
			
			envia_consulta_preparada = conexion.prepareStatement(registrar_pedido);
			envia_consulta_preparada.setInt(1, nuevo_pedido.getCodigo_pedido());
			envia_consulta_preparada.setTimestamp(2, new java.sql.Timestamp(nuevo_pedido.getEmision().getTime()));
			envia_consulta_preparada.setString(3, nuevo_pedido.getCod_proveedor());
			envia_consulta_preparada.execute();
			
			envia_consulta_preparada = conexion.prepareStatement(registrar_linea_pedido);
			for(LineaPedido l:lineasdepedido){
				
				envia_consulta_preparada.setInt(1, nuevo_pedido.getCodigo_pedido());
				envia_consulta_preparada.setInt(2, l.getCodigo_producto());
				envia_consulta_preparada.setDouble(3, l.getPrecio_u());
				envia_consulta_preparada.setDouble(4, l.getCantidad());
				envia_consulta_preparada.setDouble(5, l.getSubtotal());
				envia_consulta_preparada.execute();	
			}
			conexion.commit();
			return true;
			
		}catch(SQLException e){
			try {
				conexion.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			e.printStackTrace();
			return false;
		}finally{
			try {
				conexion.close();
			} catch (SQLException e){
				e.printStackTrace();
			}
		}
	}
	
	//OBTIENE LOS PEDIDOS PENDIENTES DE RECEPCION
	public ArrayList<Object[]> obtenerPedidosSinRecibir(String cod_proveedor){
		
		miConexion = new Conexion();
		Connection conexion = miConexion.dameConexion();
		ArrayList<Object[]> pedidos = new ArrayList<Object[]>();
		
		try{
			envia_consulta_preparada = conexion.prepareStatement(obtener_pedidos_sin_recibir);
			envia_consulta_preparada.setString(1, cod_proveedor);
			rs = envia_consulta_preparada.executeQuery();
			
			while(rs.next()){
				int cod_pedido = rs.getInt("CodigoPedido");
				java.util.Date emision = new java.util.Date(rs.getTimestamp("Emision").getTime());
				String recepcion = "";
				double monto = rs.getDouble("SUM(lineasdepedido.Subtotal)");
				String empleado = "";
				
				Object[] pedido = {cod_pedido,emision,recepcion,monto,empleado};
				
				pedidos.add(pedido);
			}
			
			return pedidos;
			
		}catch(SQLException e){
			e.printStackTrace();
			return null;
		}finally{
			try {
				conexion.close();
			} catch (SQLException e){
				e.printStackTrace();
			}
		}
	}
	
	//OBTIENE LINEAS DE PEDIDO ASOCIADAS A UN PEDIDO
	public ArrayList<LineaPedido> obtenerLineasDePedido(int cod_pedido){
		
		miConexion = new Conexion();
		Connection conexion = miConexion.dameConexion();
		ArrayList<LineaPedido> lineas = new ArrayList<LineaPedido>();
		try{
			envia_consulta_preparada = conexion.prepareStatement(obtener_lineas_de_pedido);
			envia_consulta_preparada.setInt(1, cod_pedido);
			rs = envia_consulta_preparada.executeQuery();
			
			while(rs.next()){
				int cod_linea = rs.getInt("CodigoLinea");
				int cod_producto = rs.getInt("Productos_CodigoProducto");
				double precio_u = rs.getDouble("Precio_u");
				double cantidad = rs.getDouble("Cantidad");
				double subtotal = rs.getDouble("Subtotal");
				LineaPedido linea = new LineaPedido(cod_pedido,cod_linea,cod_producto,precio_u,cantidad,subtotal);
				
				lineas.add(linea);
			}
			
			return lineas;
			
		}catch(SQLException e){
			e.printStackTrace();
			return null;
		}finally{
			try {
				conexion.close();
			} catch (SQLException e){
				e.printStackTrace();
			}
		}
		
	}
	
	//ACTUALIZA EL ARCHIVO DE LISTA DE PRECIOS
	public boolean actualizarListaPrecios(String ruta_lista_precio, String codigo_proveedor) {
		
		miConexion = new Conexion();
		Connection conexion = miConexion.dameConexion();
		
		try{
			envia_consulta_preparada = conexion.prepareStatement(guardar_enlace_lista_precios);
			envia_consulta_preparada.setString(1, ruta_lista_precio);
			envia_consulta_preparada.setString(2, codigo_proveedor);
			envia_consulta_preparada.execute();
				
			return true;
			
		}catch(SQLException e){
			e.printStackTrace();
			return false;
		}finally{
			try {
				conexion.close();
			} catch (SQLException e){
				e.printStackTrace();
			}
		}
		
	}
	
	//OBTIENE EL ARCHIVO DE LISTA DE PRECIOS ASOCIADO AL PROVEEDOR
	public String obtenerListaPrecios(String codigo_proveedor) {
		
		miConexion = new Conexion();
		Connection conexion = miConexion.dameConexion();
		String enlace = null;
		try{
			envia_consulta_preparada = conexion.prepareStatement(obtener_enlace_lista_precios);
			envia_consulta_preparada.setString(1, codigo_proveedor);
			rs = envia_consulta_preparada.executeQuery();
			while(rs.next()){
				enlace = rs.getString("ListaPrecios");
			}
			return enlace;
			
		}catch(SQLException e){
			e.printStackTrace();
			return enlace;
		}finally{
			try {
				conexion.close();
			} catch (SQLException e){
				e.printStackTrace();
			}
		}
		
	}
	
	//OBTIENE LISTA DE PEDIDOS RECIBIDOS
	public ArrayList<Object[]> obtenerPedidosRecibidos() {
		miConexion = new Conexion();
		Connection conexion = miConexion.dameConexion();
		ArrayList<Object[]> pedidos = new ArrayList<Object[]>();
		try{
			envia_consulta = conexion.createStatement();
			rs = envia_consulta.executeQuery(obtener_todos_pedidos_recibidos);
			while(rs.next()){
				int cod_pedido = rs.getInt("CodigoPedido");
				String nomb_prov = rs.getString("proveedores.Nombre");
				java.util.Date emision = new java.util.Date(rs.getTimestamp("Emision").getTime());
				java.util.Date recepcion = new java.util.Date(rs.getTimestamp("Recepcion").getTime());
				double monto = rs.getDouble("SUM(lineasdepedido.Subtotal)");
				String empleado_confirma = rs.getString("empleados.Usuario");
				
				Object[] pedido = {cod_pedido,nomb_prov,monto,emision,recepcion,empleado_confirma};
				pedidos.add(pedido);
				
			}
			return pedidos;
			
		}catch(SQLException e){
			e.printStackTrace();
			return null;
		}finally{
			try {
				conexion.close();
			} catch (SQLException e){
				e.printStackTrace();
			}
		}
	}
	
	//OBTIENE LINEAS DE UN PEDIDO YA RECIBIDO
	public ArrayList<Object[]> obtenerLineasPedidoRecibido(int cod_pedido) {
		miConexion = new Conexion();
		Connection conexion = miConexion.dameConexion();
		ArrayList<Object[]> lineas = new ArrayList<Object[]>();
		try{
			envia_consulta_preparada = conexion.prepareStatement(obtener_lineas_pedido_recibido);
			envia_consulta_preparada.setInt(1, cod_pedido);
			rs = envia_consulta_preparada.executeQuery();
			while(rs.next()){
				int cod_linea = rs.getInt("CodigoLinea");
				String desc_producto = rs.getString("productos.Descripcion");
				double precio_u = rs.getDouble("Precio_u");
				double cantidad = rs.getDouble("Cantidad");
				double subtotal = rs.getDouble("Subtotal");
				Object[] linea= {cod_linea,desc_producto,precio_u,cantidad,subtotal};
				lineas.add(linea);
				
			}
			return lineas;
			
		}catch(SQLException e){
			e.printStackTrace();
			return null;
		}finally{
			try {
				conexion.close();
			} catch (SQLException e){
				e.printStackTrace();
			}
		}
	}
	
	//OBTIENE LISTA DE PRODUCTOS PENDIENTES
	public ArrayList<Object[]> obtenerTodosProductosPendientes(){
		miConexion = new Conexion();
		Connection conexion = miConexion.dameConexion();
		ArrayList<Object[]> productos_pendientes = new ArrayList<Object[]>();
		try{
			envia_consulta = conexion.createStatement();
			rs = envia_consulta.executeQuery(obtener_lineas_pendientes);
			while(rs.next()){
				String nomb_proveedor = rs.getString("proveedores.Nombre");
				int cod_pedido = rs.getInt("pedidos_CodigoPedido1");
				java.util.Date emision = new java.util.Date(rs.getTimestamp("pedidos.Emision").getTime());
				int cod_linea = rs.getInt("CodigoLinea");
				String desc_producto = rs.getString("productos.Descripcion");
				double precio_u = rs.getDouble("Precio_u");
				double cantidad = rs.getDouble("lineasdepedido.Cantidad");
				double subtotal = rs.getDouble("Subtotal");
				Object[] linea= {nomb_proveedor,cod_pedido,emision,cod_linea,desc_producto,precio_u,cantidad,subtotal};
				productos_pendientes.add(linea);
				
			}
			return productos_pendientes;
			
		}catch(SQLException e){
			e.printStackTrace();
			return null;
		}finally{
			try {
				conexion.close();
			} catch (SQLException e){
				e.printStackTrace();
			}
		}
	}
	
	//OBTIENE LISTA DE PRODUCTOS PENDIENTES SEGUN PROVEEDOR
	public ArrayList<Object[]> obtenerProductosPendientes(String cod_prov){
		miConexion = new Conexion();
		Connection conexion = miConexion.dameConexion();
		ArrayList<Object[]> productos_pendientes = new ArrayList<Object[]>();
		try{
			envia_consulta_preparada = conexion.prepareStatement(obtener_lineas_pendientes_proveedor);
			envia_consulta_preparada.setString(1, cod_prov);
			rs = envia_consulta_preparada.executeQuery();
			while(rs.next()){
				int cod_pedido = rs.getInt("pedidos_CodigoPedido1");
				java.util.Date emision = new java.util.Date(rs.getTimestamp("pedidos.Emision").getTime());
				int cod_linea = rs.getInt("CodigoLinea");
				String desc_producto = rs.getString("productos.Descripcion");
				double precio_u = rs.getDouble("Precio_u");
				double cantidad = rs.getDouble("lineasdepedido.Cantidad");
				double subtotal = rs.getDouble("Subtotal");
				Object[] linea= {cod_pedido,emision,cod_linea,desc_producto,precio_u,cantidad,subtotal};
				productos_pendientes.add(linea);
				
			}
			return productos_pendientes;
			
		}catch(SQLException e){
			e.printStackTrace();
			return null;
		}finally{
			try {
				conexion.close();
			} catch (SQLException e){
				e.printStackTrace();
			}
		}
	}
	
	

	private Conexion miConexion;
	private PreparedStatement envia_consulta_preparada;
	private Statement envia_consulta;
	private ResultSet rs;
	
	private String consulta_codigo="SELECT CodigoProveedor FROM proveedores WHERE Nombre=?";
	private String consulta_proveedores= "SELECT * FROM proveedores WHERE Eliminado IS NULL";
	private String crear_proveedor = "INSERT INTO proveedores(CodigoProveedor,Nombre,Domicilio,Telefono,Email)VALUES(?,?,?,?,?)";
	private String modificar_proveedor = "UPDATE proveedores SET Nombre = ?, Domicilio = ?, Telefono = ?, Email = ? WHERE CodigoProveedor = ?";
	private String eliminar_proveedor = "UPDATE proveedores SET Eliminado = 1 WHERE CodigoProveedor = ?";
	private String obtener_n_sig_pedido = "SELECT MAX(CodigoPedido) FROM pedidos";
	private String registrar_pedido = "INSERT INTO pedidos(CodigoPedido,Emision,Proveedores_CodigoProveedor)VALUES(?,?,?)";
	private String registrar_linea_pedido = "INSERT INTO lineasdepedido(Pedidos_CodigoPedido1,Productos_CodigoProducto,Precio_u,Cantidad,Subtotal)"
			+ "values(?,?,?,?,?)";
	private String obtener_pedidos_sin_recibir = "SELECT CodigoPedido,Emision,Recepcion,SUM(lineasdepedido.Subtotal),Empleados_IdEmpleado "
			+ "FROM pedidos INNER JOIN lineasdepedido "
			+ "WHERE CodigoPedido = lineasdepedido.Pedidos_CodigoPedido1 "
			+ "AND Proveedores_CodigoProveedor = ?"
			+ "AND Empleados_IdEmpleado IS NULL "
			+ "GROUP BY CodigoPedido ORDER BY CodigoPedido";
	private String obtener_lineas_de_pedido = "SELECT * FROM lineasdepedido WHERE Pedidos_CodigoPedido1 = ?";
	private String guardar_enlace_lista_precios = "UPDATE proveedores SET ListaPrecios = ? WHERE CodigoProveedor = ?";
	private String obtener_enlace_lista_precios = "SELECT ListaPrecios FROM proveedores WHERE CodigoProveedor = ?";
	private String obtener_todos_pedidos_recibidos = "SELECT CodigoPedido,proveedores.Nombre,Emision,Recepcion,SUM(lineasdepedido.Subtotal),empleados.Usuario "
			+ "FROM pedidos INNER JOIN lineasdepedido INNER JOIN empleados INNER JOIN proveedores "
			+ "WHERE CodigoPedido = lineasdepedido.Pedidos_CodigoPedido1 "
			+ "AND Empleados_IdEmpleado = empleados.IdEmpleado "
			+ "AND Proveedores_CodigoProveedor = proveedores.CodigoProveedor "
			+ "AND lineasdepedido.Recibido=1 "
			+ "GROUP BY CodigoPedido ORDER BY CodigoPedido";
	private String obtener_lineas_pedido_recibido = "SELECT CodigoLinea, productos.Descripcion, Precio_u, lineasdepedido.Cantidad, Subtotal "
			+ "FROM lineasdepedido INNER JOIN Productos "
			+ "WHERE productos.CodigoProducto = Productos_CodigoProducto AND Pedidos_CodigoPedido1 = ? ORDER BY CodigoLinea";
	private String obtener_lineas_pendientes = "SELECT proveedores.Nombre, Pedidos_CodigoPedido1, pedidos.Emision, CodigoLinea, "
			+ "productos.Descripcion, Precio_u, lineasdepedido.Cantidad, Subtotal "
			+ "FROM lineasdepedido INNER JOIN proveedores INNER JOIN productos INNER JOIN pedidos "
			+ "WHERE Pedidos_CodigoPedido1 = pedidos.CodigoPedido AND proveedores.CodigoProveedor = pedidos.Proveedores_CodigoProveedor "
			+ "AND productos.CodigoProducto = Productos_CodigoProducto AND Recibido = 0 "
			+ "AND pedidos.Recepcion IS NOT NULL";
	private String obtener_lineas_pendientes_proveedor = "SELECT Pedidos_CodigoPedido1, pedidos.Emision, "
			+ "CodigoLinea, Productos_CodigoProducto, "
			+ "productos.Descripcion, Precio_u, lineasdepedido.Cantidad, Subtotal "
			+ "FROM lineasdepedido INNER JOIN proveedores INNER JOIN productos INNER JOIN pedidos "
			+ "WHERE Pedidos_CodigoPedido1 = pedidos.CodigoPedido AND proveedores.CodigoProveedor = pedidos.Proveedores_CodigoProveedor "
			+ "AND productos.CodigoProducto = Productos_CodigoProducto AND Recibido = 0 AND CodigoProveedor = ? "
			+ "AND pedidos.Recepcion IS NOT NULL";
}
