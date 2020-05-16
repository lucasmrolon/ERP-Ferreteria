package modelo.consultas;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import modelo.Conexion;

public class EjecutaConsultasReporte {
	
	//OBTIENE AÑOS PARA CREAR REPORTES ANUALES
	public ArrayList<Integer> obtenerAnios(){
		miconexion = new Conexion();
		conexion = miconexion.dameConexion();
		ArrayList<Integer> anios = new ArrayList<Integer>(); 
		rs=null;
		
		try{
			envia_consulta = conexion.createStatement();
			rs=envia_consulta.executeQuery("SELECT YEAR(Fecha) AS ANIOS FROM ventas WHERE FormasDePago_CodigoForma IS NOT NULL "
					+ "GROUP BY YEAR(Fecha)");
			while(rs.next()){
				anios.add(rs.getInt("ANIOS"));
			}
			return anios;
		}catch(Exception e){
			return null;
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
	ResultSet rs;
}
