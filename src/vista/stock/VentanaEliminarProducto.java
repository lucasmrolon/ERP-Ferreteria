package vista.stock;

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import controlador.stock.EliminarProducto;
import modelo.consultas.EjecutaConsultasProveedor;
import modelo.objetos.Proveedor;

//VENTANA DE ELIMINACIÓN DE PRODUCTO
public class VentanaEliminarProducto extends JDialog{

	/**
	 * 
	 */
	private static final long serialVersionUID = 3619887690261874932L;

	public VentanaEliminarProducto(PanelStock padre,DefaultTableModel modelo,int seleccionado_real){
		
		//TAMAÑO Y UBICACIÓN DE LA VENTANA
		getContentPane().setBackground(new Color(225,240,240));
		setSize(600,270);
	    setLocationRelativeTo(padre);
		setModal(true);
//		setTitle("Eliminar Producto");
		setLayout(null);
		setUndecorated(true);
		getRootPane().setBorder(BorderFactory.createLineBorder(Color.BLACK));
	
		JLabel titulo = new JLabel("ELIMINAR PRODUCTO",JLabel.CENTER);
		titulo.setFont(new Font(Font.SANS_SERIF,Font.BOLD,24));
		titulo.setBounds(0,20,590,40);
		add(titulo);
		//MENSAJE DE CONFIRMACIÓN
		JLabel mensaje = new JLabel("¿Está seguro que desea eliminar el producto el siguiente Producto?");
		mensaje.setFont(new Font(Font.DIALOG,Font.BOLD,14));
		
		//OBIENE EL VALOR DEL CÓDIGO
		int cod = Integer.parseInt((String)modelo.getValueAt(seleccionado_real, 0));
		
		//OBTIENE Y CARGA LOS VALORES DE LOS CAMPOS DEL PRODUCTO
		JLabel codigo = new JLabel("Código: " + cod);
		JLabel descripcion = new JLabel("Descripción: " + modelo.getValueAt(seleccionado_real,1));
		String codprov = (String)modelo.getValueAt(seleccionado_real, 6);
		String prov = "Proveedor: ";
		EjecutaConsultasProveedor consulta1 = new EjecutaConsultasProveedor();
		ArrayList<Proveedor> proveedores = consulta1.consultaProveedores();
		if(proveedores!=null){
			for(Proveedor proveedor : proveedores){
				if(proveedor.getCodigo().equals(codprov)){
					prov = prov + proveedor.getNombre();
				}
			}
		}else{
			JOptionPane.showMessageDialog(null,"<html><font size=4>No se pudo obtener el proveedor.</font></html>",
					"¡Error!", JOptionPane.WARNING_MESSAGE);
		}
		JLabel nombre_prov = new JLabel(prov);
		
		//TAMAÑO Y UBICACIÓN DE LOS DATOS
		mensaje.setBounds(50, 80, 500, 30);
		codigo.setBounds(100, 120, 300, 20);
		descripcion.setBounds(100, 140, 490, 20);
		nombre_prov.setBounds(100, 160, 300, 20);
	
		add(mensaje);add(codigo);add(descripcion);add(nombre_prov);
		
		//BOTÓN PARA CONFIRMAR LA OPERACIÓN
		JButton eliminar_producto = new JButton("Eliminar Producto");
		eliminar_producto.setBounds(270, 210, 150, 30);
		eliminar_producto.addActionListener(new EliminarProducto(padre,this,cod));
		add(eliminar_producto);
		
		//BOTÓN PARA CANCELAR LA OPERACIÓN
		JButton cancelar = new JButton("Cancelar");
		cancelar.setBounds(440, 210, 100, 30);
		cancelar.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				dispose();
			}
		});
		add(cancelar);
	
		ActionMap mapaAccion = this.getRootPane().getActionMap();
		InputMap mapa = this.getRootPane().getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
		
		KeyStroke escape = KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE,0);		
		
		mapa.put(escape,"accion escape");
		mapaAccion.put("accion escape", new AbstractAction(){
			/**
			 * 
			 */
			private static final long serialVersionUID = -290900816092790508L;

			public void actionPerformed(ActionEvent e){
				dispose();
			}
		});
	}
}
