package modelo.consultas;

import java.sql.*;
import java.util.ArrayList;

import modelo.Conexion;
import modelo.objetos.Empleado;

//CLASE QUE EJECUTA QUE LAS CONSULTAS RELACIONADAS CON LOS EMPLEADOS A LA BASE DE DATOS
public class EjecutaConsultasEmpleado{
	
	//OBTIENE EMPLEADOS REGISTRADOS EN LA BD
	public ArrayList<Object[]> obtenerEmpleados(){
		miconexion = new Conexion();
		conexion = miconexion.dameConexion();
		ArrayList<Object[]> empleados = new ArrayList<Object[]>();
		
		try{
			envia_consulta=conexion.prepareStatement(obtener_empleados);
			rs = envia_consulta.executeQuery();
			
			while(rs.next()){
				int id = rs.getInt("IdEmpleado");
				String tipo = rs.getString("Tipo");
				String nombre = rs.getString("Nombre");
				String apellido = rs.getString("Apellido");
				String dni = rs.getString("Dni");
				String domicilio = rs.getString("Domicilio");
				String email = rs.getString("Email");
				String telefono = rs.getString("Telefono");
				String usuario = rs.getString("Usuario");
				String pass = rs.getString("Password");
				
				Object[] empleado = {id,tipo,nombre,apellido,dni,domicilio,email,telefono,usuario,pass};
				
				empleados.add(empleado);
			}
		
			return empleados;
		}catch(Exception e){
			e.printStackTrace();
			return empleados;
		}finally{
			try {
				conexion.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
	}
	
	//OBTIENE PASSWORD Y TIPO DE USUARIO
	public Object[] obtienePasswordTipoyEstado(String usuario){
		miconexion = new Conexion();
		conexion = miconexion.dameConexion();
		Object[] passytipo = new Object[3];
		
		try{
			envia_consulta=conexion.prepareStatement(obtiene_password_tipo_estado);
			envia_consulta.setString(1, usuario);
			rs = envia_consulta.executeQuery();
			
			//GUARDA EN UN ARRAY EL PASSWORD Y EL TIPO DE USUARIO
			while(rs.next()){
				passytipo[0]=rs.getString("Password");
				passytipo[1]=rs.getString("Tipo");
				passytipo[2]=rs.getInt("Conectado");
			}
		
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			try {
				conexion.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return passytipo;
	}
	
	//ESTABLECE EL ESTADO DE USUARIO A CONECTADO
	public boolean conectarUsuario(String usuario){
		miconexion = new Conexion();
		conexion = miconexion.dameConexion();
		
		try{
			envia_consulta=conexion.prepareStatement(conectar_usuario);
			envia_consulta.setString(1, usuario);
			envia_consulta.execute();
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
	
	//ESTABLECE EL ESTADO DE USUARIO A DESCONECTADO
	public boolean desconectarUsuario(String usuario){
		miconexion = new Conexion();
		conexion = miconexion.dameConexion();
		
		try{
			envia_consulta=conexion.prepareStatement(desconectar_usuario);
			envia_consulta.setString(1, usuario);
			envia_consulta.execute();
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
	
	//RESETEA LAS CONEXIONES A DESCONECTADO
	public boolean desconectarTodos(){
		miconexion = new Conexion();
		conexion = miconexion.dameConexion();
		
		try{
			envia_consulta=conexion.prepareStatement(desconectar_todos);
			envia_consulta.execute();
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
	
	//OBTIENE EL ID DE UN EMPLEADO
	public int obtieneId(String usuario) {
		miconexion = new Conexion();
		conexion = miconexion.dameConexion();

		int id=0;
		try{
			envia_consulta=conexion.prepareStatement(obtiene_id);
			envia_consulta.setString(1, usuario);
			rs = envia_consulta.executeQuery();
			
			//OBTIENE EL ID DEL EMPLEADO
			while(rs.next()){
				id=rs.getInt("IdEmpleado");
			}	
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			try {
				conexion.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		return id;
	}
	
	//OBTIENE EL USUARIO DE UN EMPLEADO A PARTIR DE SU ID
	public String obtieneNombre(int id) {
		miconexion = new Conexion();
		conexion = miconexion.dameConexion();

		String usuario = "";
		try{
			envia_consulta=conexion.prepareStatement(obtiene_nombre);
			envia_consulta.setInt(1, id);
			rs = envia_consulta.executeQuery();
			
			//OBTIENE EL ID DEL EMPLEADO
			while(rs.next()){
				usuario=rs.getString("Usuario");
			}	
			return usuario;
		}catch(Exception e){
			System.out.println("La consulta de usuario falló");
			e.printStackTrace();
			return usuario;
		}finally{
			try {
				conexion.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
	}
	
	//CREA UN NUEVO REGISTRO DE EMPLEADO
	public String crearEmpleado(Empleado nuevo_empleado) {
		miconexion = new Conexion();
		conexion = miconexion.dameConexion();
		
		try{
			envia_consulta=conexion.prepareStatement(crear_empleado);
			envia_consulta.setInt(1, nuevo_empleado.getId());
			envia_consulta.setString(2, nuevo_empleado.getTipo());
			envia_consulta.setString(3, nuevo_empleado.getNombre());
			envia_consulta.setString(4, nuevo_empleado.getApellido());
			envia_consulta.setString(5, nuevo_empleado.getDni());
			envia_consulta.setString(6, nuevo_empleado.getDomicilio());
			envia_consulta.setString(7, nuevo_empleado.getEmail());
			envia_consulta.setString(8, nuevo_empleado.getTel());
			envia_consulta.setString(9, nuevo_empleado.getUsuario());
			envia_consulta.setString(10, nuevo_empleado.getPass());
			envia_consulta.execute();
			
			return "ok";
		}catch(Exception e){
			System.out.println("Falló la creación de usuario");
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
	
	//MODIFICA LOS DATOS DE UN EMPLEADO
	public String modificarEmpleado(Empleado empleado) {
		miconexion = new Conexion();
		conexion = miconexion.dameConexion();
		
		try{
			envia_consulta=conexion.prepareStatement(modificar_empleado);
			envia_consulta.setString(1, empleado.getTipo());
			envia_consulta.setString(2, empleado.getNombre());
			envia_consulta.setString(3, empleado.getApellido());
			envia_consulta.setString(4, empleado.getDni());
			envia_consulta.setString(5, empleado.getDomicilio());
			envia_consulta.setString(6, empleado.getEmail());
			envia_consulta.setString(7, empleado.getTel());
			envia_consulta.setString(8, empleado.getUsuario());
			envia_consulta.setString(9, empleado.getPass());
			envia_consulta.setInt(10, empleado.getId());
			envia_consulta.execute();
			
			return "ok";
		}catch(Exception e){
			System.out.println("Falló la modificación de usuario");
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
	
	//ELIMINA EL REGISTRO DE UN EMPLEADO
	public boolean eliminarEmpleado(Empleado empleado) {
		miconexion = new Conexion();
		conexion = miconexion.dameConexion();
		
		try{
			envia_consulta = conexion.prepareStatement(eliminar_empleado);
			envia_consulta.setInt(1, empleado.getId());
			envia_consulta.execute();
			
			return true;
		}catch(SQLException e){
			e.printStackTrace();
			System.out.println("No se pudo eliminar el empleado");
			return false;
		}finally{
			try {
				conexion.close();
			} catch (SQLException e){
				e.printStackTrace();
			}
		}
	}
	
	//OBTIENE UN NUEVO ID PARA UN NUEVO EMPLEADO
	public int obtener_n_empleados() {
		miconexion = new Conexion();
		conexion = miconexion.dameConexion();
		
		try{
			envia_consulta = conexion.prepareStatement(obtener_n_empleados);
			rs = envia_consulta.executeQuery();
			int n_empleados=0;
			while(rs.next()){
				n_empleados = rs.getInt("MAX(IdEmpleado)");
			}
			return n_empleados;
		}catch(SQLException e){
			e.printStackTrace();
			System.out.println("No se pudo eliminar el empleado");
			return 0;
		}finally{
			try {
				conexion.close();
			} catch (SQLException e){
				e.printStackTrace();
			}
		}
	}
	Conexion miconexion;
	Connection conexion;
	ResultSet rs;
	PreparedStatement envia_consulta;
	String obtener_empleados = "SELECT * FROM empleados WHERE Eliminado IS NULL";
	String obtiene_password_tipo_estado = "SELECT Tipo,Password,Conectado FROM empleados WHERE Usuario=?";
	String conectar_usuario = "UPDATE empleados SET Conectado = 1 WHERE Usuario = ?";
	String desconectar_usuario = "UPDATE empleados SET Conectado = 0 WHERE Usuario = ?";

	String desconectar_todos = "UPDATE empleados SET Conectado = 0";
	String obtiene_id = "SELECT IdEmpleado FROM empleados WHERE Usuario=?";
	
	String obtiene_nombre = "SELECT Usuario FROM empleados WHERE IdEmpleado=?";
	String crear_empleado = "INSERT INTO empleados(IdEmpleado,Tipo,Nombre,Apellido,Dni,Domicilio,Email,Telefono,Usuario,Password)"
			+ "VALUES(?,?,?,?,?,?,?,?,?,?)";
	String modificar_empleado = "UPDATE empleados SET Tipo=?, Nombre=?, Apellido=?, Dni=?, Domicilio=?, Email=?, Telefono=?, "
			+ "Usuario=?, Password=? WHERE IdEmpleado=?";
	String eliminar_empleado = "UPDATE empleados SET Eliminado = 1 WHERE IdEmpleado = ?";
	String obtener_n_empleados = "SELECT MAX(IdEmpleado) FROM empleados";
	
}
