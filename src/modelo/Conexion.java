package modelo;

import java.sql.*;

import javax.swing.JOptionPane;

//CLASE QUE CREA LA CONEXIÓN CON LA BASE DE DATOS
public class Conexion {
	
	Connection miConexion=null;
	
	public Connection dameConexion(){
		
		try{
			//miConexion=DriverManager.getConnection("jdbc:mysql://localhost:3306/mydb?autoReconnect=true&user=root&password=&useSSL=false");
			miConexion=DriverManager.getConnection("jdbc:mysql://192.168.43.49:3306/mydb?autoReconnect=true&user=client1&password=client1&useSSL=false");
			
		//SI NO SE PUDO ESTABLECER LA CONEXIÓN, SE MUESTRA UN MENSAJE DE ERROR
		}catch(Exception e){
			String mensaje = "<html><Font size=5>Error de conexión con la base de datos.</Font></html>";
			JOptionPane.showMessageDialog(null, mensaje, "¡Error!", JOptionPane.WARNING_MESSAGE);
		}
		
		return miConexion;
	}
}

