package controlador.stock;

import java.awt.event.*;
import javax.swing.*;
import modelo.consultas.EjecutaConsultas;
import modelo.consultas.EjecutaConsultasProveedor;
import modelo.objetos.Producto;
import vista.stock.PanelStock;
import vista.stock.VentanaModificarProducto;

//CLASE QUE SE ENCARGA DE MODIFICAR UN PRODUCTO
public class ModificarProducto implements ActionListener{
	
	public ModificarProducto(PanelStock panel,VentanaModificarProducto ventana,boolean[] errores,JLabel con_errores,int cod, 
			JTextField paraDesc,JComboBox<String> paraRubro,JTextField paraPrecio,JTextField paraCantidad,
			JComboBox<String> paraTipo,JComboBox<String> paraProveedor,JTextPane paraComent,JTextField paraAlerta,
			JCheckBox no_alertar){
		this.panel=panel;
		this.ventana=ventana;
		this.errores=errores;
		this.con_errores=con_errores;
		codigo=cod;
		this.paraDesc=paraDesc;
		this.paraRubro=paraRubro;
		this.paraPrecio=paraPrecio;
		this.paraCantidad=paraCantidad;
		this.paraTipo=paraTipo;
		this.paraProveedor=paraProveedor;
		this.paraComent=paraComent;
		this.paraAlerta=paraAlerta;
		this.no_alertar=no_alertar;
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		
		//SE VERIFICA LA NO EXISTENCIA DE ERRORES
		int n_err=0;
		for(boolean b:errores){
			if(b) n_err++; 
		}
		
		//SI NO HAY ERRORES, SE PROCEDE A OBTENER LOS DATOS DEL PRODUCTO
		if(n_err==0){
			String desc = paraDesc.getText().toUpperCase();
			String rubro = (String)paraRubro.getSelectedItem();
			
			//SE ONTIENE EL CÓDIGO DEL RUBRO SELECCIONADO
			EjecutaConsultas consulta2= new EjecutaConsultas();
			int rub=consulta2.consultaCodRubro(rubro);
			
			double precio = Double.parseDouble(paraPrecio.getText());
			double cantidad = Double.parseDouble(paraCantidad.getText());
			String tipo = (String)paraTipo.getSelectedItem();
			String proveedor = (String)paraProveedor.getSelectedItem();
			
			//SE OBTIENE EL CÓDIGO DEL PROVEEDOR SELECCIONADO
			EjecutaConsultasProveedor consulta1= new EjecutaConsultasProveedor();
			String codProv=consulta1.consultaCodProveedor(proveedor);

			String coment = paraComent.getText().toUpperCase();
			double alerta;
			String estado;
			
			//ESTABLECE EL ESTADO DEL PRODUCTO
			if(no_alertar.isSelected()){
				alerta = -1;
				estado = "";
			}
			else{
				alerta = Double.parseDouble(paraAlerta.getText());
				if(cantidad==0.00){
					estado="AGOTADO";
				}else{
					if(cantidad<=alerta){
						estado="BAJO";
					}else estado="NORMAL";
				}
			}
			
			if(rub!=0 && codProv!=null){
				//PROCEDE A MODIFICAR LOS ATRIBUTOS EN LA BASE DE DATOS
				a_modificar= new Producto(codigo,codProv,rub,desc,tipo,estado,coment,precio,cantidad,alerta);
				EjecutaConsultas nueva_consulta=new EjecutaConsultas();
				boolean ok = nueva_consulta.modificarProducto(a_modificar);
			
				if(ok){
					//ACTUALIZA LA TABLA
					panel.actualizarTabla(a_modificar, 'm');
					ventana.dispose();
				}
				else{
					JOptionPane.showMessageDialog(null,"<html><font size=4>¡Error al aplicar los cambios al producto! Intente nuevamente.</font></html>",
							"¡Error!", JOptionPane.WARNING_MESSAGE);
				}
			}
			else{
				JOptionPane.showMessageDialog(null,"<html><font size=4>¡Error al obtener el rubro y/o proveedor! Intente nuevamente.</font></html>",
						"¡Error!", JOptionPane.WARNING_MESSAGE);
			}
		}
		
		//SI HAY ERRORES, SE MUESTRA UN MENSAJE DE ERROR
		else{
			con_errores.setText("*Hay errores en el formulario");
		}
		
	}
	
	PanelStock panel;
	VentanaModificarProducto ventana;
	boolean[] errores;
	JLabel con_errores;
	int codigo;
	JTextField paraDesc,paraPrecio,paraCantidad,paracodProv,paraAlerta;
	JComboBox<String> paraRubro, paraTipo,paraProveedor;
	JCheckBox no_alertar;
	JTextPane paraComent;
	Producto a_modificar;
}
