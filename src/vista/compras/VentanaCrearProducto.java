package vista.compras;

import java.awt.Color;
import java.awt.event.*;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.table.DefaultTableModel;

import controlador.stock.CrearProducto;
import modelo.consultas.EjecutaConsultas;
import vista.stock.PanelStock;

//VENTANA PARA CREAR UN NUEVO PEDIDO ASOCIADO AL PROVEEDOR
public class VentanaCrearProducto extends vista.stock.VentanaCrearProducto{

	/**
	 * 
	 */
	private static final long serialVersionUID = 359875987825165210L;
	public VentanaCrearProducto(VentanaAdicionProductos ventana, PanelStock panelStock) {
		super(panelStock);
		VentanaCrearProducto ventana_creacion = this;
		
		errores= new boolean[]{true,true,false,false,true};
		
		//INICIA LA VENTANA
		paraProveedor.removeAllItems();
		paraProveedor.addItem(ventana.nomb_prov);
		paraCantidad.setText("0.00");
		
		paraCantidad.setEnabled(false);
		paraPrecio.setEnabled(false);
		
		no_alertar.removeActionListener(no_alertar.getActionListeners()[0]);
		no_alertar.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if(no_alertar.isSelected()){
					paraAlerta.setEnabled(false);
					paraAlerta.setText("");
					err_alerta.setText("");
					paraAlerta.setBorder(por_defecto);	
					errores[4]=false;
				}else{
					paraAlerta.setEnabled(true);
					paraAlerta.setBorder(por_defecto);
					errores[4]=true;
					err_alerta.setText("¡Debe ser un número!");
				}
			}
		});
		
		//MODIFICA METODO DE DETECCION DE ERRORES
		paraCodigo.removeFocusListener(paraCodigo.getFocusListeners()[0]);
		paraDesc.removeFocusListener(paraDesc.getFocusListeners()[0]);
		paraRubro.removeFocusListener(paraRubro.getFocusListeners()[0]);
		paraPrecio.removeFocusListener(paraPrecio.getFocusListeners()[0]);
		paraCantidad.removeFocusListener(paraCantidad.getFocusListeners()[0]);
		paraTipo.removeFocusListener(paraTipo.getFocusListeners()[0]);
		paraProveedor.removeFocusListener(paraProveedor.getFocusListeners()[0]);
		paraComent.removeFocusListener(paraComent.getFocusListeners()[0]);
		paraAlerta.removeFocusListener(paraAlerta.getFocusListeners()[0]);
	
		paraCodigo.addFocusListener(new CuadroSeleccionado(paraCodigo,err_cod));
		paraDesc.addFocusListener(new CuadroSeleccionado(paraDesc,err_desc));
		paraAlerta.addFocusListener(new CuadroSeleccionado(paraAlerta,err_alerta));
		
		agregar_producto.removeActionListener(agregar_producto.getActionListeners()[0]);
		
		agregar_producto.addActionListener(new CrearProducto(panelStock,ventana_creacion,errores,con_errores,
						paraCodigo,paraDesc,paraRubro,paraPrecio,paraCantidad,paraTipo,paraProveedor,paraComent,paraAlerta,no_alertar));
		
		agregar_producto.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				
				Object[] fila = {paraCodigo.getText(),
						paraDesc.getText().toUpperCase(),
						Double.parseDouble(paraPrecio.getText()),
						Double.parseDouble(paraCantidad.getText()),
						paraTipo.getSelectedItem().toString().toUpperCase(),
						paraComent.getText().toUpperCase()};
				
				DefaultTableModel modeloTabla = (DefaultTableModel)ventana.muestraProductoSeleccionado.getModel();
				modeloTabla.addRow(fila);
				ventana.paraNuevoProducto.setEnabled(false);
				Object nuevoItem = paraDesc.getText().toUpperCase();
				ventana.paraBuscarProductos.addItem(nuevoItem);
				ventana.paraBuscarProductos.setSelectedItem(nuevoItem);
			}
			
		});
	}
	
	//METODO QUE MANEJA VERIFICACION DE ERRORES DE ACUERDO AL FOCO
	private class CuadroSeleccionado implements FocusListener{

		public CuadroSeleccionado(JComponent campo,JLabel mens_err){
			
			this.campo = campo;
			this.mens_err=mens_err;
			
		}
		
		//CUANDO OBTIENE EL FOCO
		public void focusGained(FocusEvent e) {
				campo.setBorder(BorderFactory.createLineBorder(Color.GREEN,2));
		}

		//CUANDO PIERDE EL FOCO, COMPRUEBA ERRORES
		@Override
		public void focusLost(FocusEvent e) {
			
			//VERIFICA LOS VALORES
			if(campo==paraCodigo||campo==paraDesc||campo==paraAlerta){
				JTextField a_verificar = (JTextField)campo;
				if(a_verificar==paraCodigo){
					try{
						int cod=Integer.parseInt(a_verificar.getText());
						if(cod<1||cod>99999){
							a_verificar.setBorder(BorderFactory.createLineBorder(Color.RED,2));
							mens_err.setText("¡Código Inválido!");
							errores[0]=true;
						}else{
							mens_err.setText("");
							errores[0]=false;
						}
						EjecutaConsultas consulta = new EjecutaConsultas();
						
						//VERIFICA QUE EL CÓDIGO INGRESADO NO ESTÉ YA OCUPADO
						if(!errores[0]){
							String codigo_libre = consulta.consultaCodigo(cod);
							if(codigo_libre.equals("no")){
								a_verificar.setBorder(BorderFactory.createLineBorder(Color.RED,2));
								mens_err.setText("¡Código ya existente!");
								errores[0]=true;
							}
							else if(codigo_libre.equals("si")){
								mens_err.setText("");
								errores[0]=false;
							}
							else if(codigo_libre.equals("error")){
								a_verificar.setBorder(BorderFactory.createLineBorder(Color.RED,2));
								errores[0]=true;
								JOptionPane.showMessageDialog(null,"<html><font size=4>¡No se pudo conectar con la base de datos!</font></html>",
										"¡Error!", JOptionPane.WARNING_MESSAGE);
							}
						}
					}catch(NumberFormatException ex){
						a_verificar.setBorder(BorderFactory.createLineBorder(Color.RED,2));
						mens_err.setText("¡Código Inválido!");
						errores[0]=true;
					}
				}
				
				//VERIFICA QUE LA DESCRIPCIÓN NO ESTÉ EN BLANCO
				else if(a_verificar==paraDesc){
					String sinespacios = a_verificar.getText().trim();
					if(sinespacios.length()==0){
						a_verificar.setBorder(BorderFactory.createLineBorder(Color.RED,2));
						mens_err.setText("La descripción no puede estar en blanco");
						errores[1]=true;
					} else{
						errores[1]=false;
						mens_err.setText("");
					}
				}
				
				//VERIFICA QUE EL VALOR DE CANTIDAD MÍNIMA SEA VÁLIDO
				else if(a_verificar==paraAlerta){
					if(a_verificar.isEnabled()){
						try{
							double valor = Double.parseDouble(a_verificar.getText());
							if(valor<0){
								a_verificar.setBorder(BorderFactory.createLineBorder(Color.RED,2));
								mens_err.setText("¡Debe ser mayor a 0!");
								errores[4]=true;
							}else{
								mens_err.setText("");
								errores[4]=false;
							}
						}catch(Exception ex){
							a_verificar.setBorder(BorderFactory.createLineBorder(Color.RED,2));
							mens_err.setText("¡Debe ser un número!");
							errores[4]=true;
						}
					}
				}
				System.out.println(errores[2]);
			}
			else{
				campo.setBorder(por_defecto);				
			}	
			
		}

		JComponent campo;
		JLabel mens_err;
	}
	
	boolean[] errores;
	Border por_defecto;
}



