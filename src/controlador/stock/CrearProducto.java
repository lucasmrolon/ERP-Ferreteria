  package controlador.stock;

import java.awt.event.*;
import javax.swing.*;

import modelo.consultas.EjecutaConsultas;
import modelo.consultas.EjecutaConsultasProveedor;
import modelo.objetos.Producto;
import vista.stock.PanelStock;
import vista.stock.VentanaCrearProducto;

//CLASE QUE SE ENCARGA DE AÑADIR UN PRODUCTO NUEVO A LA BASE DE DATOS
public class CrearProducto implements ActionListener{

	//CONSTRUCTOR DE LA CLASE
	public CrearProducto(PanelStock padre, VentanaCrearProducto dialogo, boolean[] errores, JLabel con_errores,
			JTextField paraCodigo, JTextField paraDesc, JComboBox<String> paraRubro, JTextField paraPrecio, JTextField paraCantidad,
			JComboBox<String> paraTipo, JComboBox<String> paraProveedor, JTextPane paraComent,JTextField paraAlerta,
			JCheckBox no_alertar) {
		
		codigo=paraCodigo;
		desc=paraDesc;
		rubro=paraRubro;
		precio=paraPrecio;
		cantidad=paraCantidad;
		tipo=paraTipo;
		prov=paraProveedor;
		coment=paraComent;
		alerta_min=paraAlerta;
		ventana = dialogo;
		this.padre=padre;				//PANEL DE STOCK PARA ACTUALIZAR LA TABLA
		this.errores=errores;			//ARREGLO DE BOOLEANOS QUE MUESTRA LA PRESENCIA O NO DE ERRORES
		this.con_errores=con_errores;	//LABEL PARA MOSTRAR MENSAJE DE ERROR
		this.no_alertar=no_alertar;		//CHECK QUE INDICA SI SE DEBE ESTABLECER UN STOCK MINIMO DE PRODUCTO
		
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		
		
		//SE COMPRUEBA LA NO EXISTENCIA DE ERRORES
		int n_err=0;
		for(boolean b:errores){
			if(b) n_err++; 
		}
		
		//SE OBTIENEN LOS VALORES INGRESADOS EN LOS CAMPOS
		if(n_err==0){
			int cod = Integer.parseInt(codigo.getText());
			String descrip = desc.getText().toUpperCase();
			String rub = rubro.getSelectedItem().toString().toUpperCase();
			double prec = Double.parseDouble(precio.getText());
			double cant = Double.parseDouble(cantidad.getText());
			String unidad = tipo.getSelectedItem().toString().toUpperCase();
			String provee = prov.getSelectedItem().toString().toUpperCase();
			String observ = coment.getText().toUpperCase();
			
			//SE ESTABLECE EL ESTADO DEL PRODUCTO
			double alerta;
			String estado;
			if(no_alertar.isSelected()){
				alerta = -1;
				estado = "";
			}else{
				alerta = Double.parseDouble(alerta_min.getText());
				if(cant==0.00){
					estado="AGOTADO";
				}else{
					if(cant<=alerta){
						estado="BAJO";
					}else estado="NORMAL";
				}
			}
			//SE OBTIENEN LOS CODIGOS DE PROVEEDOR Y DE RUBRO SELECCIONADO
			EjecutaConsultasProveedor consulta1= new EjecutaConsultasProveedor();
			String codProv=consulta1.consultaCodProveedor(provee);
			EjecutaConsultas consulta2= new EjecutaConsultas();
			int rubro=consulta2.consultaCodRubro(rub);
			
			if(rubro!=0 && codProv!=null){
				//SE CREA EL OBJETO PRODUCTO
				Producto nuevo_producto= new Producto(cod,codProv,rubro,descrip,unidad,estado,observ,prec,cant,alerta);
		
				//SE AÑADE A LA BASE DE DATOS Y SE ACTUALIZA LA TABLA
				EjecutaConsultas nueva_consulta = new EjecutaConsultas();
				nueva_consulta.productoNuevo(nuevo_producto);
				padre.actualizarTabla(nuevo_producto,'a');
		
				ventana.dispose();
			}else{
				String mensaje = "<html><Font size=5>¡Error al obtener datos de rubro y/o proveedor! Intente nuevamente.</Font></html>";
				JOptionPane.showMessageDialog(null, mensaje, "¡Error!", JOptionPane.WARNING_MESSAGE);
			}
		
		//SI HAY ERRORES, SE MUESTRA UN MENSAJE
		}else{ 
			con_errores.setText("*Hay errores en el formulario");
		}
		
	}
	
	boolean[] errores;
	JLabel con_errores;
	PanelStock padre;
	JTextField codigo,desc,precio,cantidad,alerta_min;
	JComboBox<String> rubro,tipo,prov;
	JTextPane coment;
	JCheckBox no_alertar;
	VentanaCrearProducto ventana;
}
