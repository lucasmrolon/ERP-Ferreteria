package vista.stock;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import controlador.stock.ModificarProducto;
import modelo.consultas.EjecutaConsultas;
import modelo.objetos.Proveedor;
import modelo.objetos.Rubro;

//VENTANA DE MODIFICACIÓN DE PRODUCTO
public class VentanaModificarProducto extends VentanaCrearProducto{

	/**
	 * 
	 */
	private static final long serialVersionUID = -7828182584766537517L;

	public VentanaModificarProducto(PanelStock padre, DefaultTableModel modelo, int seleccionado_real){
		
		super(padre);
		
		//ARREGLO DE ERRORES
		errores=new boolean[]{false,false,false,false,false};
		
		//TÍTULO DE LA VENTANA
		titulo.setText("MODIFICAR PRODUCTO");
		titulo.setLocation(60, titulo.getLocation().y);

		//OBTIENE LOS VALORES DE LA TABLA
		int cod = Integer.parseInt((String)modelo.getValueAt(seleccionado_real, 0));
		paraCodigo.setText(cod+"");
		paraCodigo.setEnabled(false);
		
		String desc = (String)modelo.getValueAt(seleccionado_real, 1);
		paraDesc.setText(desc);
		
		int codrubro = (int)modelo.getValueAt(seleccionado_real, 2);
		if(rubros!=null){
			for (Rubro rubro : rubros){
				if (rubro.getCodigoRubro()==codrubro){
					paraRubro.setSelectedItem(rubro.getNombre());
				}
			}
		}
		
		double precio = (double)modelo.getValueAt(seleccionado_real, 3);
		paraPrecio.setText(precio+"");
		
		double cantidad = (double)modelo.getValueAt(seleccionado_real, 4);
		paraCantidad.setText(cantidad+"");
		
		String tipo = (String)modelo.getValueAt(seleccionado_real, 5);
		paraTipo.setSelectedItem(tipo);

		String prov = (String)modelo.getValueAt(seleccionado_real, 6);
		for (Proveedor proveedor : proveedores){
			if (proveedor.getCodigo().equals(prov)){
				paraProveedor.setSelectedItem(proveedor.getNombre());
			}
		}
		
		String obs = (String)modelo.getValueAt(seleccionado_real, 8);

		//BOTÓN PARA CONFIRMAR MODIFICACIÓN
		JButton modificar = new JButton("Modificar");
		
		//SI TIENE MARCA DE COMENTARIO, SE BUSCA EL COMENTARIO EN LA BASE DE DATOS
		String coment="";
		if(obs.trim().equals("X")){
			EjecutaConsultas nueva = new EjecutaConsultas();
			coment=nueva.consultaComent(cod);
		}
		if(coment!=null){
			paraComent.setText(coment);
		}else{
			paraComent.setText("");
			modificar.setEnabled(false);
		}
		//CAPTURA SI ESTÁ ACTIVADO EL ALERTA
		double cant_min;
		if((double)modelo.getValueAt(seleccionado_real, 9)==-1){
			no_alertar.setSelected(true);
			paraAlerta.setEnabled(false);
			paraAlerta.setText("");
		}else{
			cant_min=(double)modelo.getValueAt(seleccionado_real,9);
			paraAlerta.setText(cant_min+"");
		}
		
		
		
		//UBICA EL BOTÓN EN EL MISMO LUGAR QUE ESTABA EL DE AÑADIR PRODUCTO
		modificar.setLocation(super.agregar_producto.getLocation());
		modificar.setSize(super.agregar_producto.getSize());
		remove(agregar_producto);
		modificar.addActionListener(new ModificarProducto(padre,this,errores,con_errores,cod,
				paraDesc,paraRubro,paraPrecio,paraCantidad,paraTipo,paraProveedor,paraComent,paraAlerta,no_alertar));
		add(modificar);
	}
}
