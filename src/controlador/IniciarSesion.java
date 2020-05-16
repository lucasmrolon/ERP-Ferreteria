package controlador;

import java.awt.event.*;

import javax.swing.*;
import javax.swing.SwingUtilities;

import modelo.consultas.EjecutaConsultasEmpleado;
import vista.*;

//CLASE QUE VERIFICA LOS DATOS DE USUARIO AL INICIAR SESIÓN
public class IniciarSesion implements ActionListener{

	public IniciarSesion(JTextField usuario,JTextField contrasena,JPanel panel){
		
		this.usuario=usuario;
		this.contrasena=contrasena;
		this.panel=panel;
		
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		
		//SI LOS DATOS SON CORRECTOS, INICIA SESIÓN Y CAMBIA AL PANEL DE LA APLICACIÓN
		EjecutaConsultasEmpleado nuevaConsulta = new EjecutaConsultasEmpleado();
		
		//OBTIENE EL PASSWORD Y EL TIPO DE USUARIO DE LA BD, SI EXISTE
		Object[] passytipo = nuevaConsulta.obtienePasswordTipoyEstado(usuario.getText());
		
		//VERIFICA QUE EL USUARIO EXISTA
		if(passytipo[0]!=null){
			
			//VERIFICA QUE LA CONTRASEÑA SEA CORRECTA
			if(passytipo[0].equals(contrasena.getText())){
				
				if((int)passytipo[2]==0){
					nuevaConsulta.conectarUsuario(usuario.getText());
					JFrame ventana = (JFrame)SwingUtilities.getWindowAncestor(panel);
					ventana.getContentPane().removeAll();	
					ventana.repaint();
					PanelAplicacion nuevoAplicacion = new PanelAplicacion(usuario.getText(),(String)passytipo[1]);
					ventana.add(nuevoAplicacion);
					ventana.repaint();
					ventana.validate();	
				}
				else{
					JOptionPane.showMessageDialog(panel, "El usuario ya se encuentra conectado",
							"¡Error!",JOptionPane.ERROR_MESSAGE);
				}
			}
			else{
				JOptionPane.showMessageDialog(panel, "El usuario no existe, o bien la contraseña es incorrecta",
						"¡Error! - Datos Inválidos",JOptionPane.ERROR_MESSAGE);
			}
		}
		//SI LOS DATOS SON INCORRECTOS, MUESTRA UN MENSAJE DE ERROR
		else{
			JOptionPane.showMessageDialog(panel, "El usuario no existe, o bien la contraseña es incorrecta",
					"¡Error! - Datos Inválidos",JOptionPane.ERROR_MESSAGE);
		}
	}

	JTextField usuario,contrasena;
	JPanel panel;
}
