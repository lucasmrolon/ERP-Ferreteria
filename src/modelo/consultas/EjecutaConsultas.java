package modelo.consultas;

import java.sql.*;
import java.util.ArrayList;

import javax.swing.JOptionPane;

import modelo.Conexion;
import modelo.objetos.LineaPedido;
import modelo.objetos.Producto;
import modelo.objetos.Rubro;

//CLASE QUE EJECUTA LAS CONSULTAS RELACIONADAS CON LOS PRODUCTOS
public class EjecutaConsultas {
	
	//DEVUELTA LISTA DE PRODUCTOS ALMACENADA EN LA BASE DE DATOS
	public ArrayList<Producto> consultaProductos(){
		
		ArrayList<Producto> listaProductos = new ArrayList<Producto>();

		//INICIA LA CONEXIÓN
		miConexion=new Conexion();		
		Connection conexion = miConexion.dameConexion();
		
		rs=null;
		
		//SE REALIZA LA CONSULTA, Y SE INSTANCIA TODOS LOS PRODUCTOS OBTENIDOS
		try{
			enviaConsulta = conexion.prepareStatement(consultaTotal);
			rs = enviaConsulta.executeQuery();

			while(rs.next()){

				int codigo = rs.getInt("CodigoProducto");
				String descripcion = rs.getString("Descripcion");
				int rubro = rs.getInt("Rubro_CodigoRubro");
				double precio = rs.getDouble("Precio");
				double cantidad = rs.getDouble("Cantidad");
				String unidad = rs.getString("unidad");
				String codproveedor = rs.getString("Proveedores_CodigoProveedor");
				String estado = rs.getString("Estado");
				String comentarios = rs.getString("Observaciones");
				
				double cant_min = rs.getDouble("CantMinima");
				
				producto=new Producto(codigo,codproveedor,rubro,descripcion,unidad,estado,comentarios,precio,cantidad,cant_min);				
				
				listaProductos.add(producto);	
				
			}
			
			//SE CIERRA LA CONEXIÓN
			conexion.close();
		
		}catch(Exception e){
			JOptionPane.showMessageDialog(null,"<html><font size=4>¡No se pudo obtener la lista de productos!</font></html>",
					"¡Error!", JOptionPane.WARNING_MESSAGE);
			e.printStackTrace();
		}
		
		return listaProductos;
		
	}
	
	//CREA UN NUEVO PRODUCTO Y LO AÑADE A LA BASE DE DATOS
	public void productoNuevo (Producto nuevo_producto){
		
		miConexion=new Conexion();
		
		int codigo = nuevo_producto.getCodigo(); 
		String desc = nuevo_producto.getDescripcion();
		int rubro = nuevo_producto.getCodrubro();
		double precio = nuevo_producto.getPrecio();
		double cantidad = nuevo_producto.getCantidad();
		String unidad = nuevo_producto.getUnidad();
		String cod_prov= nuevo_producto.getCodproveedor();
		String estado = nuevo_producto.getEstado();
		String comentarios = nuevo_producto.getComentarios();
		double alerta = nuevo_producto.getCant_min();
		
		try {
			Connection conexion = miConexion.dameConexion();
			
			enviaConsulta = conexion.prepareStatement(agregarProducto);
			enviaConsulta.setInt(1,codigo);
			enviaConsulta.setString(2,desc);
			enviaConsulta.setInt(3,rubro);
			enviaConsulta.setDouble(4, precio);
			enviaConsulta.setDouble(5, cantidad);
			enviaConsulta.setString(6, unidad);
			enviaConsulta.setString(7, cod_prov);
			enviaConsulta.setString(8, estado);
			enviaConsulta.setString(9,comentarios);
			enviaConsulta.setDouble(10, alerta);
			enviaConsulta.execute();
			
			conexion.close();
			
		} catch (SQLException e) {
			JOptionPane.showMessageDialog(null,"<html><font size=4>¡Error al crear el producto! Intente nuevamente.</font></html>",
					"Eliminar Línea", JOptionPane.WARNING_MESSAGE);
			e.printStackTrace();
		}
	}
	
	//ELIMINA UN PRODUCTO DE LA BASE DE DATOS
	public boolean eliminarProducto(int a_eliminar) {
		
		miConexion = new Conexion();
		int codigo = a_eliminar;
		
		try{
			Connection conexion = miConexion.dameConexion();
			enviaConsulta = conexion.prepareStatement(eliminarProducto);
			enviaConsulta.setInt(1, codigo);
			enviaConsulta.execute();
			
			conexion.close();
			return true;
		}catch(Exception e){	
			e.printStackTrace();
			return false;
		}
	}

	//MODIFICA UN PRODUCTO YA EXISTENTE
	public boolean modificarProducto(Producto a_modificar) {

		miConexion = new Conexion();
		
		int codigo = a_modificar.getCodigo();
		String desc = a_modificar.getDescripcion();
		int rubro = a_modificar.getCodrubro();
		double precio = a_modificar.getPrecio();
		double cantidad= a_modificar.getCantidad();
		String tipo = a_modificar.getUnidad();
		String codprov = a_modificar.getCodproveedor();
		String estado = a_modificar.getEstado();
		String coment = a_modificar.getComentarios();
		double alerta = a_modificar.getCant_min();
		
		try{
			Connection conexion = miConexion.dameConexion();
			enviaConsulta = conexion.prepareStatement(modificarProducto);
			enviaConsulta.setString(1, desc);
			enviaConsulta.setInt(2, rubro);
			enviaConsulta.setDouble(3, (double)(Math.rint(precio*100)/100));
			enviaConsulta.setDouble(4, cantidad);
			enviaConsulta.setString(5, tipo);
			enviaConsulta.setString(6, codprov);
			enviaConsulta.setString(7, estado);
			enviaConsulta.setString(8, coment);
			enviaConsulta.setDouble(9, alerta);
			enviaConsulta.setInt(10, codigo);
			enviaConsulta.execute();
			
			//CIERRA LA CONEXIÓN
			conexion.close();
			return true;
		}catch(Exception e){
			e.printStackTrace();
			return false;
		}
		

	}
	
	//CONSULTA LOS CÓDIGOS EXISTENTES PARA SABER SI HAY UN CÓDIGO LIBRE
	public String consultaCodigo(int cod){
		
		String libre="no";
		
		miConexion=new Conexion();
		Connection conexion=miConexion.dameConexion();
		rs=null;
		
		try {
			enviaConsulta = conexion.prepareStatement(consulta_producto);
			enviaConsulta.setInt(1,cod);
			rs=enviaConsulta.executeQuery();
			
			//SE VERIFICA SI EL CÓDIGO ESTÁ OCUPADO O NO 
			if(rs.next()) libre="no";
			else libre="si";
			
			conexion.close();
			
			return libre;
		
		} catch (SQLException e) {
			return "error";
		}
	}
	
	//CLASE QUE CONSULTA EL TIPO DE UNIDAD DEL PRODUCTO
	public String consultaUnidad(int cod){
		
		String unidad="";
		
		miConexion=new Conexion();
		Connection conexion=miConexion.dameConexion();
		rs=null;
		
		try {
			enviaConsulta = conexion.prepareStatement(consulta_unidad_de_producto);
			enviaConsulta.setInt(1,cod);
			rs=enviaConsulta.executeQuery();
			
			//SE VERIFICA SI EL CÓDIGO ESTÁ OCUPADO O NO 
			while(rs.next()) {
				unidad = rs.getString("Unidad");
			}
			
			conexion.close();
			
			return unidad;
		
		} catch (SQLException e) {
			return "error";
		}
	}
	
	//DEVUELVE LA LISTA DE RUBROS EXISTENTES
	public ArrayList<Rubro> consultaRubros() {

		miConexion=new Conexion();
		Connection conexion = miConexion.dameConexion();

		rs=null;
		
		ArrayList<Rubro> rubros = new ArrayList<Rubro>();
		
		try{
			
			enviaConsulta = conexion.prepareStatement(consulta_rubros);	
			rs = enviaConsulta.executeQuery();
			
			//CREA UNA LISTA CON LOS RESULTADOS
			while(rs.next()){
				rubros.add(new Rubro(
				rs.getInt("CodigoRubro"),rs.getString("Nombre"),rs.getString("Subrubro")));
			}
			return rubros;

		}catch(Exception e){
			e.printStackTrace();
			return null;
		}
	}
	
	//CONSULTA EL CÓDIGO DE UN RUBRO
	public int consultaCodRubro(String rub) {
		
		//ESTABLECE LA CONEXIÓN
		miConexion=new Conexion();
		Connection conexion = miConexion.dameConexion();
		
		int cod=0;
		rs=null;
		
		try{
			
			//EJECUTA LA CONSULTA
			enviaConsulta = conexion.prepareStatement(consulta_codrubro);
			enviaConsulta.setString(1,rub);
			
			//OBTIENE EL CÓDIGO DEL RUBRO
			rs = enviaConsulta.executeQuery();
			while(rs.next()){
				cod= rs.getInt("Codigorubro");
			}
			return cod;

		}catch(Exception e){
			JOptionPane.showMessageDialog(null,"<html><font size=4>¡Error al obtener información del rubro!</font></html>",
					"¡Error!", JOptionPane.WARNING_MESSAGE);
			return 0;
		}
		
	}
	
	//CONSULTA LOS COMENTARIOS DE UN PRODUCTO
	public String consultaComent(int cod){
		
		//ESTABLECE LA CONEXIÓN
		miConexion=new Conexion();
		Connection conexion=miConexion.dameConexion();
		
		try {
			//EJECUTA LA CONSULTA
			enviaConsulta = conexion.prepareStatement(consulta_observ);
			enviaConsulta.setInt(1,cod);
			rs=enviaConsulta.executeQuery();
			
			//OBTIENE LOS COMENTARIOS
			rs.next();
			return rs.getString("observaciones");
			
		} catch (SQLException e) {
			JOptionPane.showMessageDialog(null,"<html><font size=4>¡No se pudo obtener el comentario! Intente nuevamente.</font></html>",
					"Eliminar Línea", JOptionPane.WARNING_MESSAGE);
			return null;
			
		//PARA FINALIZAR, CIERRA LA CONEXIÓN
		}finally{
			try {
				conexion.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	
	//ACTUALIZA LOS PRECIOS DE TODOS LOS PRODUCTOS
	public boolean actualizaPrecios(double valor){
		
		//ESTABLECE LA CONEXIÓN
		miConexion=new Conexion();
		Connection nueva_conexion = miConexion.dameConexion();
		
		//EJECUTA LA CONSULTA DE ACTUALIZACIÓN
		try{
			enviaConsulta = nueva_conexion.prepareStatement(actualiza_precios);
			enviaConsulta.setDouble(1, valor/100);
			enviaConsulta.execute();
			nueva_conexion.close();
			return true;
			
		//SI FALLA, MUESTRA MENSAJE DE ERROR
		}catch(Exception e){
			JOptionPane.showMessageDialog(null,"<html><font size=4>¡Error al actualizar los precios! Intente nuevamente.</font></html>",
					"Eliminar Línea", JOptionPane.WARNING_MESSAGE);
			return false;
		}
		
	}
	
	//OBTIENE EL PRODUCTO A PARTIR DE SU CODIGO
	public Producto obtieneProducto(int codigo){
		miConexion = new Conexion();
		Connection nueva_conexion = miConexion.dameConexion();
		
		try{
			enviaConsulta = nueva_conexion.prepareStatement(consulta_producto);
			enviaConsulta.setInt(1, codigo);
			rs = enviaConsulta.executeQuery();
			
			while(rs.next()){

				String descripcion = rs.getString("Descripcion");
				int rubro = rs.getInt("Rubro_CodigoRubro");
				double precio = rs.getDouble("Precio");
				double cantidad = rs.getDouble("Cantidad");
				String unidad = rs.getString("unidad");
				String codproveedor = rs.getString("Proveedores_CodigoProveedor");
				String estado = rs.getString("Estado");
				String comentarios = rs.getString("Observaciones");
				double cant_min = rs.getDouble("CantMinima");
				
				producto=new Producto(codigo,codproveedor,rubro,descripcion,unidad,estado,comentarios,precio,cantidad,cant_min);	
				
			}
			
			return producto;
		}catch(Exception e){
			
			e.printStackTrace();
			return null;
		}
	}
	
	//PERMITE ACTUALIZAR EL STOCK DE UN PRODUCTO
	public boolean actualizarStockProducto(int codigo,double cantidad){
		miConexion = new Conexion();
		Connection nueva_conexion = miConexion.dameConexion();
		String actualiza_stock = "UPDATE productos SET cantidad = cantidad + ? WHERE codigoproducto = ?";
		try{
			enviaConsulta = nueva_conexion.prepareStatement(actualiza_stock);
			enviaConsulta.setDouble(1, cantidad);
			enviaConsulta.setInt(2, codigo);
			enviaConsulta.execute();
			
			return true;
		}catch(Exception e){
			e.printStackTrace();
			JOptionPane.showMessageDialog(null,"<html><font size=4>¡Error al actualizar el stock del producto! Intente nuevamente.</font></html>",
					"¡Error!", JOptionPane.WARNING_MESSAGE);
			return false;
		}finally{
			try {
				nueva_conexion.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	
	//PERMITE CREAR UN NUEVO RUBRO
	public boolean crearNuevoRubro(Rubro nuevo_rubro) {
		miConexion = new Conexion();
		Connection conexion = miConexion.dameConexion();
		
		try{
			enviaConsulta = conexion.prepareStatement(crear_nuevo_rubro);
			enviaConsulta.setInt(1, nuevo_rubro.getCodigoRubro());
			enviaConsulta.setString(2, nuevo_rubro.getNombre());
			enviaConsulta.execute();
			
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
	
	//PERMITE MODIFICAR EL NOMBRE DE UN RUBRO
	public boolean modificarRubro(Rubro rubro) {
		miConexion = new Conexion();
		Connection conexion = miConexion.dameConexion();
		
		try{
			enviaConsulta = conexion.prepareStatement(modificar_rubro);
			enviaConsulta.setString(1, rubro.getNombre());
			enviaConsulta.setInt(2, rubro.getCodigoRubro());
			enviaConsulta.execute();
			
			return true;
		}catch(Exception e){
			e.printStackTrace();
			JOptionPane.showMessageDialog(null,"<html><font size=4>¡No se pudo actualizar rubro! Intente nuevamente.</font></html>",
					"Eliminar Línea", JOptionPane.WARNING_MESSAGE);
			return false;
		}finally{
			try {
				conexion.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	
	//PERMITE ELIMINAR UN RUBRO DE LA LISTA DE RUBROS
	public boolean eliminarRubro(Rubro rubro) {
		miConexion = new Conexion();
		Connection conexion = miConexion.dameConexion();
		
		try{
			enviaConsulta = conexion.prepareStatement(eliminar_rubro);
			enviaConsulta.setInt(1, rubro.getCodigoRubro());
			enviaConsulta.execute();
			
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
	
	//PERMITE REGISTRAR LA RECEPCION DE UN PEDIDO Y SU CORRESPONDIENTE ACTUALIZACION DE STOCK
	public boolean registrarRecepcionYActualizarStock(int cod_pedido,ArrayList<LineaPedido> lineas_recibidas,int id_usuario){
		miConexion = new Conexion();
		Connection conexion = miConexion.dameConexion();
		try{
			conexion.setAutoCommit(false);
			//GUARDAR FECHA DE RECEPCION
			
			enviaConsulta = conexion.prepareStatement(guardar_fecha_recepcion_pedido);
			java.util.Date fecha_recepcion = new java.util.Date();
			enviaConsulta.setTimestamp(1, new java.sql.Timestamp(fecha_recepcion.getTime()));
			enviaConsulta.setInt(2, id_usuario);
			enviaConsulta.setInt(3, cod_pedido);
			enviaConsulta.execute();
			
			//MODIFICAR LINEAS PEDIDO
			enviaConsulta = conexion.prepareStatement(modifica_linea_pedido);
			double cant = 0;
			double prec_u = 0;
			int cod = 0;
			for(int i=0;i<lineas_recibidas.size();i++){
				prec_u = lineas_recibidas.get(i).getPrecio_u();
				cant = lineas_recibidas.get(i).getCantidad();
				cod = lineas_recibidas.get(i).getCodigo_linea();
				
				//System.out.println("Actualizar Precio = " + prec_u + ", Cantidad = " + cant + ", Subtotal = " + Math.round(prec_u*cant*100d)/100d + " donde el Codigo = " + cod);
				
				enviaConsulta.setDouble(1, prec_u);
				enviaConsulta.setDouble(2, cant);
				enviaConsulta.setDouble(3, Math.round(prec_u*cant*100d)/100d);
				enviaConsulta.setInt(4, cod);
				enviaConsulta.execute();
			}
			
			//ACTUALIZAR STOCK
			
			
			int cod_producto_recibido = 0;
			for(int i=0;i<lineas_recibidas.size();i++){
				cod_producto_recibido = lineas_recibidas.get(i).getCodigo_producto();
				enviaConsulta = conexion.prepareStatement(actualiza_stock);
				enviaConsulta.setDouble(1, lineas_recibidas.get(i).getCantidad());
				enviaConsulta.setDouble(2, lineas_recibidas.get(i).getPrecio_u());
				enviaConsulta.setInt(3, cod_producto_recibido);
				enviaConsulta.execute();					
			}
			
			conexion.commit();
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
	
	//OBTIENE LOS RUBROS ALMACENADOS
	public int consultaRubrosAlmacenados() {
		miConexion = new Conexion();
		Connection conexion = miConexion.dameConexion();

		try{
			enviaConsulta = conexion.prepareStatement(obtener_n_rubros);
			rs = enviaConsulta.executeQuery();
			int n_rubros = 0;
			while(rs.next()){
				n_rubros = rs.getInt("MAX(CodigoRubro)");
			}
			return n_rubros;
		}catch(Exception e){
			e.printStackTrace();
			return 0;
		}finally{
			try {
				conexion.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	
	//PERMITE REGISTRAR LA RECEPCION DE UN PRODUCTO ATRASADO
	public boolean registrarRecepcionProducto(int cod_linea,double precio,double cantidad,double subtotal) {
		miConexion = new Conexion();
		Connection conexion = miConexion.dameConexion();
		try{
			System.out.println(cod_linea +" "+precio+" "+cantidad+" "+subtotal);
			conexion.setAutoCommit(false);
			enviaConsulta = conexion.prepareStatement(modifica_linea_pedido);
			enviaConsulta.setDouble(1, precio);
			enviaConsulta.setDouble(2, cantidad);
			enviaConsulta.setDouble(3, subtotal);
			enviaConsulta.setInt(4, cod_linea);
			enviaConsulta.execute();
			
			
			enviaConsulta = conexion.prepareStatement(obtener_cod_prod_recibido);
			enviaConsulta.setDouble(1, cod_linea);
			rs = enviaConsulta.executeQuery();
			int cod_prod=0;
			while(rs.next()){
				cod_prod = rs.getInt("Productos_CodigoProducto");
			}
			
			System.out.println(cod_linea+" "+cod_prod);
			
			enviaConsulta = conexion.prepareStatement(actualiza_stock_producto_recibido);
			enviaConsulta.setDouble(1, cantidad);
			enviaConsulta.setDouble(2, cod_prod);
			enviaConsulta.execute();					
			
			conexion.commit();
			return true;
			
		}catch(SQLException e){
			e.printStackTrace();
			return true;
		}finally{
			try {
				conexion.close();
			} catch (SQLException e){
				e.printStackTrace();
			}
		}
	}
	
	Producto producto;
	
	private Conexion miConexion;
	private ResultSet rs;
	private PreparedStatement enviaConsulta;

	String obtener_codprod_ultimo = "SELECT MAX(codigoproducto) FROM productos";
	String consultaTotal = "SELECT * FROM productos WHERE eliminado IS NULL";
	String agregarProducto="INSERT INTO productos(codigoproducto,descripcion,rubro_codigorubro,"
			+ "precio,cantidad,unidad,proveedores_codigoproveedor,estado,observaciones,cantminima)"+
			"VALUES(?,?,?,?,?,?,?,?,?,?)";
	String eliminarProducto="UPDATE productos SET Eliminado = 1 WHERE codigoproducto=?";
	String modificarProducto="UPDATE productos SET descripcion=?, rubro_codigorubro=?, precio=?, cantidad=?, "
			+ "unidad=?, proveedores_codigoproveedor=?, estado=?, observaciones=?, cantminima=? WHERE codigoproducto=?";
	String consulta_producto = "SELECT * FROM productos WHERE codigoproducto=?";
	String consulta_unidad_de_producto = "SELECT Unidad FROM productos WHERE codigoproducto=?";
	String consulta_observ = "SELECT observaciones FROM productos WHERE codigoproducto=?";
	String consulta_codrubro = "SELECT CodigoRubro FROM rubro WHERE Nombre=?";
	String consulta_rubros = "SELECT * FROM rubro WHERE Eliminado is NULL";
	String actualiza_precios = "UPDATE productos SET precio = ROUND(precio + precio*?,2)";
	String actualiza_stock = "UPDATE productos SET cantidad = cantidad + ?, precio = ? WHERE codigoproducto = ?";
	String crear_nuevo_rubro = "INSERT INTO rubro(CodigoRubro,Nombre)VALUES(?,?)";
	String modificar_rubro = "UPDATE rubro SET Nombre = ? WHERE CodigoRubro=?";
	String eliminar_rubro = "UPDATE rubro SET Eliminado = 1 WHERE CodigoRubro=?";
	String obtener_n_rubros = "SELECT MAX(CodigoRubro) FROM rubro";
	String guardar_fecha_recepcion_pedido = "UPDATE pedidos SET Recepcion = ?,Empleados_IdEmpleado = ? WHERE CodigoPedido = ?";
	String modifica_linea_pedido = "UPDATE lineasdepedido SET Precio_u = ?, Cantidad = ?, Subtotal = ?, Recibido = 1 WHERE CodigoLinea = ?";
	String actualiza_stock_producto_recibido = "UPDATE productos SET cantidad = cantidad + ? "
			+ "WHERE codigoproducto = ?";
	String obtener_cod_prod_recibido = "SELECT Productos_CodigoProducto FROM lineasdepedido WHERE CodigoLinea = ?";
	
}
