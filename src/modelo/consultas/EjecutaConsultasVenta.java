package modelo.consultas;

import java.sql.*;
import java.util.ArrayList;
import java.util.Date;

import javax.swing.JOptionPane;
import javax.swing.JSpinner;

import modelo.Conexion;
import modelo.objetos.Cliente;
import modelo.objetos.CuentaCorriente;
import modelo.objetos.FormaDePago;
import modelo.objetos.LineaReparto;
import modelo.objetos.LineaVenta;
import modelo.objetos.Venta;


//CLASE QUE EJECUTA LAS CONSULTAS A LA BD RELACIONADAS CON LAS VENTAS
public class EjecutaConsultasVenta {
	
	//OBTIENE EL NÚMERO DE VENTAS REGISTRADAS
	public int consultaN_venta(){
		miconexion = new Conexion();
		conexion = miconexion.dameConexion();
		int numero_venta=0;
		rs=null;
		
		try{
			envia_consulta = conexion.createStatement();
			rs = envia_consulta.executeQuery(consulta_numeroventas);
			
			//GUARDA EL NÚMERO DE VENTAS
			while(rs.next()){
				numero_venta=rs.getInt("id");
			}
			return numero_venta;
		}catch(Exception e){
			System.out.println("La consulta falló");
			return 0;
		}
		finally{
			try {
				if(conexion!=null){
					conexion.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	
	//OBTIENE LAS FORMAS DE PAGO ALMACENADAS EN LA BASE DE DATOS
	public ArrayList<FormaDePago> obtener_formas_de_pago() {
		
		rs=null;
		ArrayList<FormaDePago> formasdepago = new ArrayList<FormaDePago>();
		try{
			miconexion=new Conexion();
			conexion = miconexion.dameConexion();
			
			envia_consulta = conexion.createStatement();
			rs = envia_consulta.executeQuery(consulta_formas_de_pago);
			
			//CREA UN ARRAYLIST CON LAS FORMAS DE PAGO ALMACENADAS
			while(rs.next()){
				formasdepago.add(new FormaDePago(rs.getInt("CodigoForma"),rs.getString("Tipo"),rs.getDouble("Recargo"),rs.getDouble("Descuento")));
			}
			return formasdepago;
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}finally{
			try {
				if(conexion!=null){
					conexion.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
	}
	
	//MODIFICA DATOS DE LAS FORMAS DE PAGO
	public boolean modificaFormasDePago(FormaDePago forma){
		miconexion=new Conexion();
		conexion = miconexion.dameConexion();
		
		try{
		
			//REALIZA LA PRIMERA CONSULTA
			envia_consulta_preparada = conexion.prepareStatement(modifica_forma_de_pago);
			envia_consulta_preparada.setDouble(1, forma.getRecargo());
			envia_consulta_preparada.setDouble(2,forma.getDescuento());
			envia_consulta_preparada.setInt(3, forma.getCodigo());
			envia_consulta_preparada.execute();

			return true;
		}catch(Exception e){
			try {
				conexion.rollback();
			} catch (SQLException e1) {
				System.out.println("No se pudo deshacer");
				e1.printStackTrace();
			}
			System.out.println("No se guardar los cambios");
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
	
	//VERIFICA EL STOCK DE UN PRODUCTO
	public boolean verificarStock(int cod_producto,double cant_requerida) {
		
		miconexion=new Conexion();
		conexion = miconexion.dameConexion();
		double cantidad=0;
		
		try{
			envia_consulta_preparada = conexion.prepareStatement(verificar_stock);
			envia_consulta_preparada.setInt(1,cod_producto);
			rs = envia_consulta_preparada.executeQuery();
			while(rs.next()){
				cantidad=rs.getInt("Cantidad");
			}
 			if(cantidad>=cant_requerida){
 				return true;
 			}else
 				throw new Exception();
		}catch(Exception e){
			JOptionPane.showMessageDialog(null,"<html><font size=4>¡El stock del producto " + cod_producto + " es insuficiente!</font></html>",
					"Eliminar Línea", JOptionPane.WARNING_MESSAGE);
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
	
	//PERMITE FACTURAR UNA VENTA
	public ArrayList<Object[]> facturarVenta(int cod_venta,Venta nuevaVenta){
		miconexion=new Conexion();
		conexion = miconexion.dameConexion();
		ResultSet rs2;
		PreparedStatement envia_consulta_preparada2;
		try{
			conexion.setAutoCommit(false);
			if(nuevaVenta.getCta_cte()==0){
				envia_consulta_preparada = conexion.prepareStatement(facturar_venta);
				
				envia_consulta_preparada.setTimestamp(1, new java.sql.Timestamp(nuevaVenta.getFecha().getTime()));
				envia_consulta_preparada.setDouble(2, nuevaVenta.getRecdesc());
				envia_consulta_preparada.setInt(3,nuevaVenta.getForma_pago());
				envia_consulta_preparada.setInt(4, cod_venta);
				envia_consulta_preparada.execute();
			}else{
				envia_consulta_preparada = conexion.prepareStatement(facturar_venta_con_ctacte);
				envia_consulta_preparada.setTimestamp(1, new java.sql.Timestamp(nuevaVenta.getFecha().getTime()));
				envia_consulta_preparada.setDouble(2, nuevaVenta.getRecdesc());
				envia_consulta_preparada.setInt(3, nuevaVenta.getCta_cte());
				envia_consulta_preparada.setInt(4,nuevaVenta.getForma_pago());
				envia_consulta_preparada.setInt(5, cod_venta);
				envia_consulta_preparada.execute();
				
				actualizar_estado_de_cuenta = "UPDATE cuentascorrientes SET Estado = Estado - ROUND(?,2) WHERE CodigoCuenta = ?";
				
				envia_consulta_preparada = conexion.prepareStatement(actualizar_estado_de_cuenta);
				envia_consulta_preparada.setDouble(1,nuevaVenta.getMonto_total()+nuevaVenta.getRecdesc());
				envia_consulta_preparada.setInt(2,nuevaVenta.getCta_cte());
				envia_consulta_preparada.execute();
			}
			
			envia_consulta_preparada = conexion.prepareStatement(obtener_lineas_para_facturar);
			envia_consulta_preparada.setInt(1, cod_venta);
			rs = envia_consulta_preparada.executeQuery();
			envia_consulta_preparada = conexion.prepareStatement(insertar_linea_venta);
			
			ArrayList<Object[]> para_reparto = new ArrayList<Object[]>(); 
			while(rs.next()){

				int cod_producto = rs.getInt("CodigoProducto");
				double precio_u = rs.getDouble("Precio_u");
				double cantidad = rs.getDouble("Cantidad");
				double subtotal = rs.getDouble("Subtotal");
				int reparte = rs.getInt("Reparte");
				
				envia_consulta_preparada.setInt(1, cod_venta);
				envia_consulta_preparada.setInt(2, cod_producto);
				envia_consulta_preparada.setDouble(3, precio_u);
				envia_consulta_preparada.setDouble(4, cantidad);
				envia_consulta_preparada.setDouble(5, subtotal);
				envia_consulta_preparada.setInt(6, reparte);
				envia_consulta_preparada.execute();
				
				envia_consulta = conexion.createStatement();
				String leerProducto = "SELECT * FROM productos WHERE codigoproducto = " + cod_producto;
				rs2 = envia_consulta.executeQuery(leerProducto);
				double cant_minima = 0.00;
				double cantidad_actual = 0.00;
				while(rs2.next()){
					cant_minima = rs2.getDouble("CantMinima");
					cantidad_actual = rs2.getDouble("Cantidad");
				}
				
				String estado;
				
				if(cantidad_actual - cantidad== 0.00){
					estado = "AGOTADO";
				}
				else{ 
					if(cantidad_actual - cantidad>cant_minima){
						estado = "NORMAL";
					}else estado = "BAJO";
				}
				//REALIZA LA SEGUNDA CONSULTA
				envia_consulta_preparada2 = conexion.prepareStatement(actualiza_stock);
				envia_consulta_preparada2.setDouble(1, cantidad);
				envia_consulta_preparada2.setString(2, estado);
				envia_consulta_preparada2.setInt(3, cod_producto);
				envia_consulta_preparada2.execute();
				
				//REALIZA LA TERCERA CONSULTA
				rs2 = envia_consulta.executeQuery(consulta_nlineaventa);
			
				//CONFIRMA LA TRANSACCIÓN
				conexion.commit();
				
				int n_linea;
				//OBTIENE EL NÚMERO DE LÍNEA REGISTRADA
				while(rs2.next()){
					n_linea = rs2.getInt("id");
					if(reparte==1){
						Object[] linea_reparto = {n_linea,cod_producto,cantidad};
						para_reparto.add(linea_reparto);
					}
				}				
			}
			
			envia_consulta_preparada = conexion.prepareStatement(remover_lineas_venta_pendientes);
			envia_consulta_preparada.setInt(1,cod_venta);
			envia_consulta_preparada.execute();
			
			return para_reparto;
			
		}catch(Exception e){
			try {
				conexion.rollback();
			} catch (SQLException e1) {
				System.out.println("No se pudo deshacer");
				e1.printStackTrace();
			}
			System.out.println("No se pudo almacenar la línea");
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
	
	
	//AÑADE UNA LINEA DE REPARTO A LA BD
	public int aniadirLineaReparto(LineaReparto linea){
		miconexion=new Conexion();
		conexion = miconexion.dameConexion();
		int n = 0;
		try{
			//PREPARA Y REALIZA LA CONSULTA
			conexion.setAutoCommit(false);
			
			envia_consulta_preparada = conexion.prepareStatement(insertar_linea_reparto);
			envia_consulta = conexion.createStatement();
			envia_consulta_preparada.setString(1, linea.getNombre());
			envia_consulta_preparada.setString(2, linea.getDireccion());
			envia_consulta_preparada.setString(3, linea.getTurno());
			envia_consulta_preparada.setString(4, linea.getObservaciones());
			envia_consulta_preparada.setInt(5, linea.getCod_linea_venta());
			envia_consulta_preparada.setInt(6, 1);
			envia_consulta_preparada.execute();
			
			rs = envia_consulta.executeQuery(obtener_nlinea_reparto);
			
			conexion.commit();
			//OBTIENE EL NÚMERO DE LÍNEA REGISTRADA
			while(rs.next()){
				n = rs.getInt("id");
			}
			return n;
		}catch(Exception e){
			try {
				conexion.rollback();
			} catch (SQLException e1) {
				System.out.println("No se pudo deshacer");
				e1.printStackTrace();
			}
			e.printStackTrace();
			return n;
		}finally{
			try {
				conexion.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
	}
	
	//OBTIENE UNA CUENTA CORRIENTE A PARTIR DEL DNI DEL CLIENTE
	public CuentaCorriente obtieneCuentaCorriente(String dni_cliente){
		miconexion=new Conexion();
		conexion=miconexion.dameConexion();
		rs = null;
		CuentaCorriente cuenta=null;
		try{
			envia_consulta_preparada = conexion.prepareStatement(obtener_cuenta_corriente);
			envia_consulta_preparada.setString(1, dni_cliente);
			rs = envia_consulta_preparada.executeQuery();
			while(rs.next()){
				int codigo = rs.getInt("CodigoCuenta");
				double estado = rs.getDouble("Estado");
				String dni = dni_cliente;
				
				//CREA EL OBJETO CUENTA
				cuenta = new CuentaCorriente(estado,dni);
				cuenta.setCodigo_cuenta(codigo);
			}
			return cuenta;
			
		}catch(Exception e){
			System.out.println("No se pudo obtener la cuenta corriente");
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
	
	//OBTIENE LISTA DE CUENTAS CORRIENTES
		public ArrayList<Object[]> obtieneClientes(){
			miconexion=new Conexion();
			conexion=miconexion.dameConexion();
			rs = null;
			ArrayList<Object[]> clientes = new ArrayList<Object[]>();
			try{
				envia_consulta = conexion.createStatement();
				
				rs = envia_consulta.executeQuery(obtener_clientes);
				
				while(rs.next()){
					String dni = rs.getString("Dni");
					String nombre = rs.getString("Nombre");
					String apellido = rs.getString("Apellido");
					String domicilio = rs.getString("Domicilio");
					String telefono = rs.getString("Telefono");
					String email = rs.getString("Email");
					
					//CREA EL OBJETO CLIENTE
					Object[] cliente = {dni,nombre,apellido,domicilio,telefono,email};
					clientes.add(cliente);
				}
				return clientes;
				
			}catch(Exception e){
				System.out.println("No se pudo el cliente");
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
	
	//OBTIENE DATOS DE CLIENTE A PARTIR DE SU DNI
	public Cliente obtieneCliente(String dni_cliente){
		miconexion = new Conexion();
		conexion = miconexion.dameConexion();
		rs=null;
		Cliente cliente = null;
		try{
			envia_consulta_preparada = conexion.prepareStatement(obtener_cliente);
			envia_consulta_preparada.setString(1, dni_cliente);
			rs=envia_consulta_preparada.executeQuery();
			while(rs.next()){
				String nombre = rs.getString("Nombre");
				String apellido = rs.getString("Apellido");
				String domicilio = rs.getString("Domicilio");
				String telefono = rs.getString("Telefono");
				String email = rs.getString("Email");
				
				//CREA EL OBJETO CLIENTE
				cliente = new Cliente(dni_cliente,nombre,apellido,domicilio,telefono,email);	
			}
			return cliente;
		}catch(Exception e){
			System.out.println("No se pudo obtener información del cliente");
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
	
	//OBTIENE LINEA DE VENTA A PARTIR DE SU CÓDIGO
	public LineaVenta consultaLineaVenta(int codigo_linea){
		miconexion = new Conexion();
		conexion = miconexion.dameConexion();
		rs=null;
		LineaVenta linea = null;
		try{
			envia_consulta_preparada = conexion.prepareStatement(obtener_linea_venta);
			envia_consulta_preparada.setInt(1, codigo_linea);
			rs=envia_consulta_preparada.executeQuery();
			while(rs.next()){
				int codigo_venta = rs.getInt("Ventas_CodigoVenta");
				int codigo_producto = rs.getInt("Productos_CodigoProducto");
				double precio_u = rs.getDouble("Precio_u");
				double cantidad = rs.getDouble("Cantidad");
				double subtotal = rs.getDouble("Subtotal");
				int reparte = rs.getInt("Reparte");
				boolean para_reparto;
				if(reparte==1){
					para_reparto=true;
				}else{
					para_reparto=false;
				}
					
				//CREA EL OBJETO CLIENTE
				linea = new LineaVenta(codigo_venta,codigo_producto,precio_u,cantidad,subtotal,para_reparto);	
				linea.setCodigo(codigo_linea);
			}
			return linea;
		}catch(Exception e){
			System.out.println("No se pudo obtener información del cliente");
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
		
	//CONSULTA VENTA A PARTIR DE SU CODIGO
	public Venta consultaVenta(int codigo_venta){
		//OBTIENE VENTA A PARTIR DE SU CÓDIGO
		miconexion = new Conexion();
		conexion = miconexion.dameConexion();
		rs=null;
		Venta venta = null;
		try{
			envia_consulta_preparada = conexion.prepareStatement(obtener_venta);
			envia_consulta_preparada.setInt(1, codigo_venta);
			rs=envia_consulta_preparada.executeQuery();
			while(rs.next()){
				java.util.Date fecha = new java.util.Date(rs.getTimestamp("Fecha").getTime());
				double monto_total = rs.getDouble("MontoTotal");
				Double recdesc = rs.getDouble("RecDesc");
				int id_empleado = rs.getInt("Empleados_IdEmpleado");
				int cta_cte = rs.getInt("CuentasCorrientes_CodigoCuenta");
				int formadepago = rs.getInt("FormasDePago_CodigoForma");
				
				//CREA EL OBJETO VENTA
				venta = new Venta(fecha,monto_total,recdesc,id_empleado,cta_cte,formadepago);
				venta.setCodigoventa(codigo_venta);
			}
			return venta;
		}catch(Exception e){
			System.out.println("No se pudo obtener información de la venta");
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
	
	//OBTIENE VENTAS REALIZADAS POR PERIODO
	public ArrayList<Venta> obtenerVentas(Date despues_de, Date antes_de){
		
		miconexion = new Conexion();
		conexion = miconexion.dameConexion();
		rs=null;
		ArrayList<Venta> ventas = new ArrayList<Venta>();
		try{
			envia_consulta_preparada = conexion.prepareStatement(obtener_ventas);
			envia_consulta_preparada.setDate(1, new java.sql.Date(despues_de.getTime()));
			envia_consulta_preparada.setDate(2, new java.sql.Date(antes_de.getTime()));
			rs=envia_consulta_preparada.executeQuery();
			while(rs.next()){
				int codigo_venta = rs.getInt("CodigoVenta");
				java.util.Date fecha = new java.util.Date(rs.getTimestamp("Fecha").getTime());
				double monto_total = rs.getDouble("MontoTotal");
				Double recdesc = rs.getDouble("RecDesc");
				int id_empleado = rs.getInt("Empleados_IdEmpleado");
				int cta_cte = rs.getInt("CuentasCorrientes_CodigoCuenta");
				int formadepago = rs.getInt("FormasDePago_CodigoForma");
						
				//CREA EL OBJETO VENTA
				Venta venta = new Venta(fecha,monto_total,recdesc,id_empleado,cta_cte,formadepago);
				venta.setCodigoventa(codigo_venta);
				
				ventas.add(venta);
			}
			return ventas;
		}catch(Exception e){
			e.printStackTrace();
			return ventas;
		}finally{
			try {
				conexion.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	
	//OBTIENE LISTA DE VENTAS REALIZADAS
	public ArrayList<Object[]> obtenerTodasLasVentas(){
		
		miconexion = new Conexion();
		conexion = miconexion.dameConexion();
		rs=null;
		ArrayList<Object[]> ventas = new ArrayList<Object[]>();
		
		try{
			envia_consulta = conexion.createStatement();
			rs=envia_consulta.executeQuery(obtener_todas_las_ventas);
			while(rs.next()){
				int codigo_venta = rs.getInt("CodigoVenta");
				java.util.Date fecha = new java.util.Date(rs.getTimestamp("Fecha").getTime());
				double monto_total = rs.getDouble("MontoTotal");
				Double recdesc = rs.getDouble("RecDesc");
				String usuario = rs.getString("Usuario");
				int cta_cte = rs.getInt("CuentasCorrientes_CodigoCuenta");
				String formadepago = rs.getString("Tipo");
						
				//CREA EL OBJETO VENTA
				Object[] venta = {codigo_venta,fecha,monto_total,recdesc,usuario,cta_cte,formadepago};
				
				ventas.add(venta);
			}
			return ventas;
		}catch(Exception e){
			System.out.println("No se pudo obtener información de la venta");
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
	
	//OBTIENE LINEAS DE VENTA ASOCIADAS A UNA VENTA
	public ArrayList<LineaVenta> obtenerLineasDeVenta(int codigo_venta){
		
		miconexion = new Conexion();
		conexion = miconexion.dameConexion();
		rs=null;
		ArrayList<LineaVenta> lineas = new ArrayList<LineaVenta>();
		try{
			envia_consulta_preparada = conexion.prepareStatement(obtener_lineasdeventa);
			envia_consulta_preparada.setInt(1, codigo_venta);
			rs=envia_consulta_preparada.executeQuery();
			while(rs.next()){
				int codigo_linea = rs.getInt("CodigoLinea");
				int codigo_producto = rs.getInt("Productos_CodigoProducto");
				double precio_u = rs.getDouble("Precio_u");
				double cantidad = rs.getDouble("Cantidad");
				double subtotal = rs.getDouble("Subtotal");
				int reparte = rs.getInt("Reparte");
				boolean para_reparto;
				if(reparte==1){
					para_reparto=true;
				}else{
					para_reparto=false;
				}
					
				//CREA EL OBJETO CLIENTE
				LineaVenta linea = new LineaVenta(codigo_venta,codigo_producto,precio_u,cantidad,subtotal,para_reparto);	
				linea.setCodigo(codigo_linea);
				lineas.add(linea);
			}
			return lineas;
		}catch(Exception e){
			System.out.println("No se pudo obtener información de la venta");
			e.printStackTrace();
			return lineas;
		}finally{
			try {
				conexion.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	//PERMITE REGISTRAR LA DEVOLUCIÓN DE UN PRODUCTO
	public boolean cancelarLinea(LineaVenta linea,JSpinner selec_cant){
		miconexion = new Conexion();
		conexion = miconexion.dameConexion();
		double a_eliminar = (double)selec_cant.getValue(); 
		rs=null;
		try{
			conexion.setAutoCommit(false);
			envia_consulta = conexion.createStatement();
			String leerProducto = "SELECT * FROM productos WHERE codigoproducto = " + linea.getCodProducto();
			rs = envia_consulta.executeQuery(leerProducto);
			double cant_minima = 0.00;
			double cantidad = 0.00;
			while(rs.next()){
				cant_minima = rs.getDouble("CantMinima");
				cantidad = rs.getDouble("Cantidad");
			}
			//ESTABLECE EL NUEVO ESTADO DE STOCK
			String estado;
			if(cantidad + linea.getCantidad()>cant_minima){
				estado = "NORMAL";
			}else estado = "BAJO";
			
			//ACTUALIZA LA TABLA DE PRODUCTOS
			String actualiza_stock = "UPDATE productos SET cantidad = cantidad + ?, estado = ? WHERE codigoproducto = ?";
			envia_consulta_preparada = conexion.prepareStatement(actualiza_stock);
			envia_consulta_preparada.setDouble(1, a_eliminar);
			envia_consulta_preparada.setString(2, estado);
			envia_consulta_preparada.setInt(3, linea.getCodProducto());
			envia_consulta_preparada.execute();

			//ACTUALIZA LA LINEA DE VENTA
			envia_consulta_preparada = conexion.prepareStatement(registrar_devolucion);
			double cant = linea.getCantidad() - a_eliminar; 
			envia_consulta_preparada.setDouble(1, cant);
			double subtotal = linea.getPrecio_u()*cant;
			envia_consulta_preparada.setDouble(2, subtotal);
			envia_consulta_preparada.setInt(3, linea.getCodigo());
			envia_consulta_preparada.execute();
				
			double a_restar = a_eliminar*linea.getPrecio_u();
			a_restar = Math.round(a_restar*100d)/100d;

			//ACTUALIZA LA VENTA
			envia_consulta_preparada = conexion.prepareStatement(actualizar_monto_venta); 
			envia_consulta_preparada.setDouble(1,  a_restar);
			envia_consulta_preparada.setInt(2, linea.getCodVenta());
			envia_consulta_preparada.execute();
			
			//ACTUALIZA LINEA DE REPARTO
			String obtener_linea_reparto_asociada = "SELECT CodigoLinea FROM lineasdereparto WHERE LineasDeVenta_CodigoLinea = ?";
			envia_consulta_preparada = conexion.prepareStatement(obtener_linea_reparto_asociada);
			envia_consulta_preparada.setInt(1, linea.getCodigo());
			rs = envia_consulta_preparada.executeQuery();
			int cod_l_reparto = 0;
			while(rs.next()){
				cod_l_reparto = rs.getInt("CodigoLinea");
			}
			
			String eliminar_linea_reparto = "UPDATE lineasdereparto SET Pendiente = 0, Observaciones = ?, Repartos_CodigoReparto = NULL WHERE CodigoLinea = ?";
			envia_consulta_preparada = conexion.prepareStatement(eliminar_linea_reparto);
			envia_consulta_preparada.setString(1, "Eliminado (Devolucion)");
			envia_consulta_preparada.setInt(2, cod_l_reparto);
			envia_consulta_preparada.execute();
			
			conexion.commit();
			return true;
		}catch(Exception e){
			System.out.println("No se pudo eliminar la línea");
			e.printStackTrace();
			try {
				conexion.rollback();
			} catch (SQLException e1) {
				System.out.println("No se pudo deshacer");
				e1.printStackTrace();
			}
			return false;
		}finally{
			try {
				conexion.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	
	//ENVIA UNA VENTA A FACTURACIÓN
	public boolean registrar_venta_sin_facturar(int cod_empleado,ArrayList<Object[]> compra){
		miconexion = new Conexion();
		conexion = miconexion.dameConexion();
		rs=null;
		
		double importe_venta = 0;
		for(Object[] o : compra){
			importe_venta+=(double)o[3];
		}
		
		try{
			conexion.setAutoCommit(false);
			envia_consulta_preparada = conexion.prepareStatement(registrar_venta_sin_facturar);
			envia_consulta_preparada.setDouble(1, importe_venta);
			envia_consulta_preparada.setInt(2, cod_empleado);
			envia_consulta_preparada.execute();
			
			rs = envia_consulta_preparada.executeQuery(consulta_numeroventas);
			int n_venta=0;
			while(rs.next()){
				n_venta=rs.getInt("id");
			}
			
			envia_consulta_preparada = conexion.prepareStatement(registrar_lineas_sin_facturar);
			for(Object[] o : compra){
				int cod_producto=(int)o[0];
				double precio_u = (double)o[1];
				double cantidad = (double)o[2];
				double subtotal = (double)o[3];
				boolean reparte = (boolean)o[4];
				
				envia_consulta_preparada.setInt(1, n_venta);
				envia_consulta_preparada.setInt(2, cod_producto);
				envia_consulta_preparada.setDouble(3, precio_u);
				envia_consulta_preparada.setDouble(4, cantidad);
				envia_consulta_preparada.setDouble(5, subtotal);
				if(reparte)
					envia_consulta_preparada.setInt(6, 1);
				else
					envia_consulta_preparada.setInt(6, 0);
				envia_consulta_preparada.execute();	
			}
			conexion.commit();
			return true;	
		}catch(Exception e){
			e.printStackTrace();
			try {
				conexion.rollback();
			} catch (SQLException e1) {
				System.out.println("No se pudo deshacer");
				e1.printStackTrace();
			}
			return false;
		}finally{
			try {
				conexion.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	
	//OBTIENE LISTA DE VENTAS SIN FACTURAR
	public ArrayList<Object[]> obtenerVentasSinFacturar(){
		
		miconexion = new Conexion();
		conexion = miconexion.dameConexion();
		rs=null;
		
		ArrayList<Object[]> ventas = new ArrayList<Object[]>();
		
		try{
			envia_consulta = conexion.createStatement();
			rs = envia_consulta.executeQuery(obtener_ventas_sin_facturar);
			while(rs.next()){
				int cod_venta = rs.getInt("CodigoVenta");
				double monto = rs.getDouble("MontoTotal");
				int cod_empleado = rs.getInt("Empleados_IdEmpleado");
				
				Object[] venta = {cod_venta,monto,cod_empleado};
				ventas.add(venta);
			}
			return ventas;
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
	
	//OBTIENE LINEAS DE VENTA ASOCIADAS A VENTA SIN FACTURAR
	public ArrayList<LineaVenta> obtenerLineasPendientesDeFacturacion(int n_venta){
		
		miconexion = new Conexion();
		conexion = miconexion.dameConexion();
		rs=null;
		
		ArrayList<LineaVenta> lineas = new ArrayList<LineaVenta>();
		
		try{
			envia_consulta_preparada = conexion.prepareStatement(obtener_lineas_para_facturar);
			envia_consulta_preparada.setInt(1, n_venta);
			
			rs = envia_consulta_preparada.executeQuery();
			while(rs.next()){
				int cod_producto = rs.getInt("CodigoProducto");
				double precio_u = rs.getDouble("Precio_u");
				double cantidad = rs.getDouble("Cantidad");
				double subtotal = rs.getDouble("Subtotal");
				int reparte = rs.getInt("Reparte");
				boolean hay_reparto=false;
				if(reparte==1){
					hay_reparto=true;
				}
				LineaVenta linea = new LineaVenta(n_venta, cod_producto, precio_u, cantidad, subtotal, hay_reparto) ;
				lineas.add(linea);
			}
			return lineas;
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
	
	//REGISTRA LA CANCELACIÓN DE UNA VENTA
	public boolean registrar_venta_cancelada(int cod_empleado,Date fecha, int cod_venta){
	
		miconexion = new Conexion();
		conexion = miconexion.dameConexion();
		
		try{
			conexion.setAutoCommit(false);
			
			String cancelar_venta = "UPDATE ventas SET Fecha=? WHERE CodigoVenta=? ";
			envia_consulta_preparada = conexion.prepareStatement(cancelar_venta);
			envia_consulta_preparada.setTimestamp(1, new java.sql.Timestamp(fecha.getTime()));
			envia_consulta_preparada.setInt(2,cod_venta);
			envia_consulta_preparada.execute();
			
			String cancelar_lineas_venta = "DELETE FROM lineasdeventapendientes WHERE CodigoVenta=?";
			envia_consulta_preparada = conexion.prepareStatement(cancelar_lineas_venta);
			envia_consulta_preparada.setInt(1,cod_venta);
			envia_consulta_preparada.execute();
			
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
	Conexion miconexion;
	Connection conexion;
	ResultSet rs;
	PreparedStatement envia_consulta_preparada;
	Statement envia_consulta;
	String consulta_numeroventas = "SELECT MAX(CodigoVenta) AS id FROM ventas ";
	String consulta_formas_de_pago = "SELECT * FROM formasdepago";
	String modifica_forma_de_pago = "UPDATE formasdepago SET Recargo=?, Descuento=? WHERE CodigoForma = ?";
	String insertar_venta_sin_ctacte="INSERT INTO ventas(Fecha,MontoTotal,RecDesc,Empleados_IdEmpleado,FormasDePago_CodigoForma)"+
			"VALUES(?,?,?,?,?)";
	String insertar_venta_con_ctacte="INSERT INTO ventas(Fecha,MontoTotal,RecDesc,Empleados_IdEmpleado,CuentasCorrientes_CodigoCuenta,FormasDePago_CodigoForma)"+
			"VALUES(?,?,?,?,?,?)";
	String registrar_venta_sin_facturar = "INSERT INTO ventas(MontoTotal,Empleados_IdEmpleado)values(?,?)";
	String insertar_linea_venta = "INSERT INTO lineasdeventa(Ventas_CodigoVenta,Productos_CodigoProducto,Precio_u,Cantidad,Subtotal,Reparte)"+
			"VALUES(?,?,?,?,?,?)";
	String actualiza_stock = "UPDATE productos SET cantidad = cantidad - ?, estado = ? WHERE codigoproducto = ?";
	String consulta_nlineaventa = "SELECT MAX(CodigoLinea) AS id FROM lineasdeventa ";
	String insertar_linea_reparto = "INSERT INTO lineasdereparto(NombreyApellido,Direccion,Turno,Observaciones,LineasDeVenta_CodigoLinea,Pendiente)"+
			"VALUES(?,?,?,?,?,?)";
	String obtener_cuenta_corriente = "SELECT * FROM cuentascorrientes WHERE Clientes_Dni=?";
	String obtener_clientes = "SELECT Dni, Nombre, Apellido, Domicilio, Telefono, Email FROM clientes WHERE Eliminado = 0";
	String obtener_cuentas_corrientes = "SELECT Dni, Nombre, Apellido, Domicilio, Telefono, Email, "
			+ "cuentascorrientes.CodigoCuenta, cuentascorrientes.Estado "
			+ "FROM clientes INNER JOIN cuentascorrientes "
			+ "WHERE cuentascorrientes.Clientes_Dni=Dni "
			+ "AND cuentascorrientes.Eliminado IS null";
	String obtener_cliente = "SELECT * FROM clientes WHERE Dni=?";
	String obtener_linea_venta = "SELECT * FROM lineasdeventa WHERE CodigoLinea = ?";
	String obtener_venta = "SELECT * FROM ventas WHERE CodigoVenta = ?";
	String obtener_todas_las_ventas = "SELECT CODIGOVENTA,Fecha,MontoTotal,RecDesc,empleados.Usuario,CuentasCorrientes_CodigoCuenta,formasdepago.Tipo from Ventas "+
			"INNER JOIN empleados INNER JOIN formasdepago "+
			"WHERE empleados.IdEmpleado = Empleados_IdEmpleado "+
			"AND formasdepago.CodigoForma = FormasDePago_CodigoForma "+
			"ORDER BY CodigoVenta";
	String actualizar_estado_de_cuenta = "UPDATE cuentascorrientes SET Estado = Estado - ROUND(?,2) WHERE CodigoCuenta = ?";
	String obtener_nlinea_reparto = "SELECT MAX(CodigoLinea) AS id FROM lineasdereparto ";
	String obtener_ventas = "SELECT * FROM ventas WHERE Fecha BETWEEN ? AND ?";
	String obtener_lineasdeventa = "SELECT * FROM lineasdeventa WHERE Ventas_CodigoVenta=?";
	String registrar_devolucion = "UPDATE lineasdeventa SET Cantidad = ? , Subtotal = ? WHERE CodigoLinea=?";
	String actualizar_monto_venta = "UPDATE ventas SET MontoTotal = ROUND(MontoTotal - ?,2) WHERE CodigoVenta = ?";
	String verificar_stock = "SELECT Cantidad FROM productos WHERE CodigoProducto = ?";
	String registrar_lineas_sin_facturar = "INSERT INTO lineasdeventapendientes(CodigoVenta,CodigoProducto,Precio_u,"
			+ "Cantidad,Subtotal,Reparte)VALUES(?,?,?,?,?,?)";
	String obtener_ventas_sin_facturar = "SELECT CodigoVenta,MontoTotal,Empleados_IdEmpleado FROM ventas WHERE Fecha IS NULL";
	String obtener_lineas_para_facturar = "SELECT * FROM lineasdeventapendientes WHERE CodigoVenta = ?";
	String facturar_venta = "UPDATE ventas SET Fecha=?,RecDesc=?,FormasDePago_CodigoForma=? WHERE CodigoVenta=?";
	String facturar_venta_con_ctacte = "UPDATE ventas SET Fecha=?,RecDesc=?,CuentasCorrientes_CodigoCuenta=?,FormasDePago_CodigoForma=? WHERE CodigoVenta=?";
	String remover_lineas_venta_pendientes = "DELETE FROM lineasdeventapendientes WHERE CodigoVenta = ?";
}
