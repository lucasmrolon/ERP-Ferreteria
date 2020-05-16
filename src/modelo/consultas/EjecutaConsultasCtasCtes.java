package modelo.consultas;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.mysql.jdbc.exceptions.jdbc4.MySQLIntegrityConstraintViolationException;

import modelo.Conexion;
import modelo.objetos.Cliente;

public class EjecutaConsultasCtasCtes {

	//PERMITE ACTUALIZAR ESTADO DE CUENTA CORRIENTE
	public boolean actualizarEstado(double monto,int n_cuenta){
		miconexion = new Conexion();
		conexion = miconexion.dameConexion();
		
		try{
			envia_consulta_preparada=conexion.prepareStatement(abonar_cuenta_corriente);
			envia_consulta_preparada.setDouble(1, monto);
			envia_consulta_preparada.setInt(2, n_cuenta);
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
	
	//PERMITE OBTENER EL ESTADO ACTUAL DE LA CUENTA
	public Double obtenerEstado(int n_cuenta){
		miconexion = new Conexion();
		conexion = miconexion.dameConexion();
		ResultSet rs;
		Double estado = 999999.00;
		try{
			envia_consulta_preparada=conexion.prepareStatement(obtener_estado_cuenta);
			envia_consulta_preparada.setInt(1, n_cuenta);
			rs = envia_consulta_preparada.executeQuery();
		
			while(rs.next()){
				estado = rs.getDouble("Estado");
			}
			
			return estado;
		}catch(Exception e){
			e.printStackTrace();
			return estado;
		}finally{
			try {
				conexion.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

	}
	
	//OBTIENE UN NUEVO NUMERO DE CUENTA
	public int obtenerNuevo_n_cuenta(){
		miconexion = new Conexion();
		conexion = miconexion.dameConexion();
		ResultSet rs;
		int nuevo_codigo=0;
		try{
			envia_consulta_preparada=conexion.prepareStatement(obtener_nuevo_n_cuenta);
			rs = envia_consulta_preparada.executeQuery();
			while(rs.next()){
				nuevo_codigo = rs.getInt("MAX(CodigoCuenta)")+1;
			}
			
			return nuevo_codigo;
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
	
	//ABRE UNA NUEVA CUENTA CORRIENTE
	public boolean abrirCuenta(double monto, int n_cuenta, String dni_cliente){
		miconexion = new Conexion();
		conexion = miconexion.dameConexion();
		try{
			envia_consulta_preparada=conexion.prepareStatement(abrir_cuenta);
			envia_consulta_preparada.setInt(1,n_cuenta);
			envia_consulta_preparada.setDouble(2, monto);
			envia_consulta_preparada.setString(3, dni_cliente);
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
	
	//CIERRA UNA CUENTA
	public boolean cerrarCuenta(int n_cuenta){
		miconexion = new Conexion();
		conexion = miconexion.dameConexion();
		try{
			envia_consulta_preparada=conexion.prepareStatement(cerrar_cuenta);
			envia_consulta_preparada.setInt(1,n_cuenta);
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
	
	//REGISTRA UN NUEVO CLIENTE
	public String crearNuevoCliente(Cliente nuevo_cliente){
		miconexion = new Conexion();
		conexion = miconexion.dameConexion();
		
		try{
			envia_consulta_preparada=conexion.prepareStatement(crear_cliente);
			envia_consulta_preparada.setString(1,nuevo_cliente.getDni());
			envia_consulta_preparada.setString(2,nuevo_cliente.getNombre());
			envia_consulta_preparada.setString(3,nuevo_cliente.getApellido());
			envia_consulta_preparada.setString(4,nuevo_cliente.getDomicilio());
			envia_consulta_preparada.setString(5,nuevo_cliente.getTelefono());
			envia_consulta_preparada.setString(6,nuevo_cliente.getEmail());
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
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

	}
	
	//MODIFICA EL REGISTRO DE UN CLIENTE
	public boolean modificarCliente(Cliente cliente){
		miconexion = new Conexion();
		conexion = miconexion.dameConexion();
		
		try{
			conexion.setAutoCommit(false);
			envia_consulta_preparada=conexion.prepareStatement(modificar_cliente);
			envia_consulta_preparada.setString(1,cliente.getNombre());
			envia_consulta_preparada.setString(2,cliente.getApellido());
			envia_consulta_preparada.setString(3,cliente.getDomicilio());
			envia_consulta_preparada.setString(4,cliente.getTelefono());
			envia_consulta_preparada.setString(5,cliente.getEmail());
			envia_consulta_preparada.setString(6,cliente.getDni());
			envia_consulta_preparada.execute();
			
			conexion.commit();
			return true;
		}catch(Exception e){
			try {
				conexion.rollback();
			} catch (SQLException e1) {
				System.out.println("No se pudo deshacer la modificación de la cuenta");
				e1.printStackTrace();
			}
			System.out.println("La consulta falló.No se pudo modificar");
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
	
	//ELIMINA EL REGISTRO DE UN CLIENTE
	public boolean eliminarCliente(String dni){
		miconexion = new Conexion();
		conexion = miconexion.dameConexion();
		
		try{
			envia_consulta_preparada=conexion.prepareStatement(eliminar_cliente);
			envia_consulta_preparada.setString(1,dni);
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
	
	//OBTIENE DATOS DE UN CLIENTE A PARTIR DE SU DNI
	public Object[] obtenerDatosCliente(String dni_cliente){
		
		miconexion = new Conexion();
		conexion = miconexion.dameConexion();
		
		ResultSet rs = null;
		Object[] datos_cliente = new Object[7];
		
		try{
			
			envia_consulta_preparada=conexion.prepareStatement(obtener_cliente);
			envia_consulta_preparada.setString(1,dni_cliente);
			rs = envia_consulta_preparada.executeQuery();
			while(rs.next()){
				datos_cliente[0] = rs.getString("Dni");
				datos_cliente[1] = rs.getString("Nombre");
				datos_cliente[2] = rs.getString("Apellido");
				datos_cliente[3] = rs.getString("Domicilio");
				datos_cliente[4] = rs.getString("Telefono");
				datos_cliente[5] = rs.getString("Email");
				datos_cliente[6] = rs.getInt("Eliminado");
			}
			
			
			
			return datos_cliente;
			
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
	
	//OBTIENE DATOS DE UN CLIENTE PREVIAMENTE ELIMINADO
	public String recuperarCliente(Cliente cliente){
		miconexion = new Conexion();
		conexion = miconexion.dameConexion();
		try{
			
			envia_consulta_preparada=conexion.prepareStatement(recuperar_cliente);
			envia_consulta_preparada.setString(1,cliente.getNombre());
			envia_consulta_preparada.setString(2,cliente.getApellido());
			envia_consulta_preparada.setString(3,cliente.getDomicilio());
			envia_consulta_preparada.setString(4,cliente.getTelefono());
			envia_consulta_preparada.setString(5,cliente.getEmail());
			envia_consulta_preparada.setString(6,cliente.getDni());
			envia_consulta_preparada.execute();
			return "ok";
			
		}catch(Exception e){
			System.out.print("fallo");
			e.printStackTrace();
			return "fallo";
			
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
	PreparedStatement envia_consulta_preparada;
	String abonar_cuenta_corriente = "UPDATE cuentascorrientes SET Estado=Estado+? WHERE CodigoCuenta=?";
	String crear_cuenta_corriente = "INSERT INTO cuentascorrientes(Estado,Clientes_Dni)VALUES(?,?)";
	String crear_cliente = "INSERT INTO clientes(Dni,Nombre,Apellido,Domicilio,Telefono,Email)VALUES(?,?,?,?,?,?)";
	String modificar_cuenta_corriente = "UPDATE cuentascorrientes SET Estado=? WHERE CodigoCuenta=?";
	String modificar_cliente = "UPDATE clientes SET Nombre=?,Apellido=?,Domicilio=?,Telefono=?,Email=? WHERE Dni=?";
	String eliminar_cuenta_corriente = "UPDATE cuentascorrientes SET Eliminado = 1 WHERE CodigoCuenta = ?";
	String eliminar_cliente = "UPDATE clientes SET Eliminado = 1 WHERE Dni = ?";
	String obtener_cliente = "SELECT * FROM clientes WHERE Dni = ?";
	String obtener_cuenta_corriente = "SELECT * FROM cuentascorrientes WHERE Clientes_Dni = ? AND Eliminado = 1";
	String recuperar_cliente = "UPDATE clientes SET Nombre=?,Apellido=?,Domicilio=?,Telefono=?,Email=?,Eliminado=0 WHERE Dni=?";
	String obtener_estado_cuenta = "SELECT Estado FROM cuentascorrientes WHERE CodigoCuenta=?";
	String obtener_nuevo_n_cuenta = "SELECT MAX(CodigoCuenta) FROM cuentascorrientes";
	String abrir_cuenta = "INSERT INTO cuentascorrientes(CodigoCuenta,Estado,Clientes_Dni)VALUES(?,?,?)";
	String cerrar_cuenta = "UPDATE cuentascorrientes SET Eliminado = 1, Clientes_Dni=NULL WHERE CodigoCuenta = ?";
}