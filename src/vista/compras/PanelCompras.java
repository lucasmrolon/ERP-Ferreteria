package vista.compras;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;

import modelo.consultas.EjecutaConsultasEmpleado;
import modelo.consultas.EjecutaConsultasProveedor;
import modelo.objetos.Proveedor;
import vista.stock.PanelStock;

//PANEL QUE PERMITE REALIZAR PEDIDOS A PROVEEDORES Y CONFIRMAR SU RECEPCION
public class PanelCompras extends JPanel{

	/**
	 * 
	 */
	private static final long serialVersionUID = 6947375809904451408L;
	public PanelCompras(String usuario,PanelStock panelStock){
		setBackground(new Color(224,224,248));
		setLayout(null);
		
		//CREA Y MUESTRA LA TABLA DE PROVEEDORES
		tablaProveedores = new JTable();
		tablaProveedores.setRowHeight(30);
		
		Dimension tamanio_pantalla = Toolkit.getDefaultToolkit().getScreenSize();
		actualizarModelo();
		JScrollPane paraTabla = new JScrollPane(tablaProveedores);
		DefaultTableModel modelo = (DefaultTableModel)tablaProveedores.getModel();
		
		//DIMENSIONA LA TABLA
		if(modelo.getRowCount()<10){
			paraTabla.setSize(new Dimension(710,modelo.getRowCount()*tablaProveedores.getRowHeight()+23));
		}
		else
			paraTabla.setSize(new Dimension(710,tablaProveedores.getRowHeight()*10+23));
		paraTabla.setLocation(tamanio_pantalla.width/2-350, 80);
		add(paraTabla);
		
		//OBTIENE TIPO DE USUARIO 
		EjecutaConsultasEmpleado paraObtenerTipo = new EjecutaConsultasEmpleado();
		Object[] datos_usuario = paraObtenerTipo.obtienePasswordTipoyEstado(usuario);
		String tipo = (String)datos_usuario[1];
		if(tipo.equals("administrador")){
			//BOTON QUE PERMITE CREAR UN NUEVO PEDIDO
			crear_pedido = new JButton("Generar Pedido");
			crear_pedido.setBounds(tamanio_pantalla.width/2-600, 90, 200, 40);
			crear_pedido.setIcon(new ImageIcon(getClass().getResource("/img/aniadir_producto.png")));
			crear_pedido.addActionListener(new ActionListener(){
				@Override
				public void actionPerformed(ActionEvent e) {
					try{
						new CrearPedido(tablaProveedores,panelStock);
					}catch(ArrayIndexOutOfBoundsException ex){
						JOptionPane.showMessageDialog(null, "<html><font size=5>Para crear un pedido, antes debe seleccionar un proveedor.</font></html>","¡Error!",
							JOptionPane.WARNING_MESSAGE);
					}
				}
			});
			add(crear_pedido);
		}
		//BOTON QUE PERMITE VER PEDIDOS PENDIENTES DE RECEPCION
		ver_pedidos = new JButton("Ver Pedidos");
		ver_pedidos.setBounds(tamanio_pantalla.width/2-600, 150, 200, 40);
		ver_pedidos.setIcon(new ImageIcon(getClass().getResource("/img/lupa.png")));
		ver_pedidos.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				try{
					new VerPedidos(tablaProveedores,usuario);
				}catch(ArrayIndexOutOfBoundsException ex){
					ex.printStackTrace();
					JOptionPane.showMessageDialog(null, "<html><font size=5>Para ver los pedidos creados, "
							+ "antes debe seleccionar un proveedor.</font></html>","¡Error!",
							JOptionPane.WARNING_MESSAGE);
				}
			}
		});
		add(ver_pedidos);
		
		//BOTON QUE PERMITE VER PRODUCTOS PENDIENTES
		ver_pendientes = new JButton("Ver productos pendientes");
		ver_pendientes.setBounds(tamanio_pantalla.width/2-600, 210, 200, 40);
		ver_pendientes.setHorizontalTextPosition(SwingConstants.LEFT);
		ver_pendientes.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				new VerProductosPendientesDeRecepcion(usuario);
			}
		});
		add(ver_pendientes);
		
	}
	
	//METODO QUE ACTUALIZA LA TABLA DE PROVEEDORES
	public void actualizarModelo(){
		
		String[] columnasTabla = {"CODIGO","NOMBRE","DOMICILIO","TELÉFONO","EMAIL"};
		
		EjecutaConsultasProveedor paraObtenerProveedores = new EjecutaConsultasProveedor();
		
		ArrayList<Proveedor> proveedores = paraObtenerProveedores.consultaProveedores();
		
		DefaultTableModel modeloProveedores = new DefaultTableModel(null,columnasTabla){
			/**
			 * 
			 */
			private static final long serialVersionUID = 4298311609816596301L;

			public boolean isCellEditable(int rowIndex,int columnIndex){
				return false;
			}
		};
		
		for(Proveedor p: proveedores){
			
			String cod = p.getCodigo();
			String nombre = p.getNombre();
			String domi = p.getDomicilio();
			String tel = p.getTelefono();
			String email = p.getEmail();
			
			Object[] proveedor = {cod,nombre,domi,tel,email};
			
			modeloProveedores.addRow(proveedor);
			
		}
		
		tablaProveedores.setModel(modeloProveedores);
		
		darFormatoATabla();
		
		
	};
	
	//DA FORMATO A LA TABLA DE PROVEEDORES
	public void darFormatoATabla(){
		
		DefaultTableCellRenderer tcr = new DefaultTableCellRenderer();
		tcr.setHorizontalAlignment(SwingConstants.CENTER);
		TableColumnModel modeloColumnas= tablaProveedores.getColumnModel(); 
		modeloColumnas.getColumn(0).setMaxWidth(60);
		modeloColumnas.getColumn(1).setMaxWidth(170);
		modeloColumnas.getColumn(2).setMaxWidth(170);
		modeloColumnas.getColumn(3).setMaxWidth(110);
		modeloColumnas.getColumn(4).setMaxWidth(200);
		
		for(int i=0;i<=4;i++){
			modeloColumnas.getColumn(i).setCellRenderer(tcr);
		}
		
	}
	
	public JTable tablaProveedores;
	public JButton aniadir,modif,elim,lis_prec,crear_pedido,ver_pedidos,ver_pendientes;
}
