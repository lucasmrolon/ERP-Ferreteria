package controlador.detalle;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;

import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;

import modelo.consultas.EjecutaConsultasProveedor;

//CLASE QUE PERMITE ACTUALIZAR LA LISTA DE PRECIOS DE UN PROVEEDOR
public class ActualizarListaPrecios {

	public ActualizarListaPrecios(int proveedor_seleccionado, JTable tabla_proveedores){
		
		seleccionado = proveedor_seleccionado;
		tabla = tabla_proveedores;
		
		//ESTABLECE CARACTERÍSTICAS DEL SELECTOR DE ARCHIVOS
		JFileChooser para_elegir_lista = new JFileChooser();
		FileFilter filtroexcel = new FileNameExtensionFilter("Planilla de Cálculo de Microsoft Excel","xls","xlsx");
		para_elegir_lista.addChoosableFileFilter(filtroexcel);
		FileFilter filtropdf = new FileNameExtensionFilter("Documento de Acrobat Reader","pdf");
		para_elegir_lista.addChoosableFileFilter(filtropdf);
		para_elegir_lista.showOpenDialog(null);
		
		
		File archivo_lista = para_elegir_lista.getSelectedFile();
		if(archivo_lista!=null){
			
			File origen = archivo_lista;
			
			//ESTABLECE DONDE SE GUARDARAN LAS LISTAS DE PRECIOS
			File destino = new File("listas_de_precios/" + archivo_lista.getName());
			
			Path de_origen = FileSystems.getDefault().getPath(origen.getAbsolutePath());
			Path de_destino = FileSystems.getDefault().getPath(destino.getAbsolutePath());
					
			try{
				//COPIA EL ARCHIVO
				Files.copy(de_origen, de_destino, StandardCopyOption.REPLACE_EXISTING);
				
				int index = origen.getPath().lastIndexOf('.');
				if(index!=-1){
					String extension = origen.getPath().substring(index+1);
					
					//ASIGNA EL ICONO CORRESPONDIENTE EN LA TABLA DEPENDIENDO DE EL TIPO DE ARCHIVO SELECCIONADO
					if(extension.equals("pdf")){
						
						ImageIcon icono_pdf = new ImageIcon(getClass().getResource("/img/icono_pdf.png"));
						JLabel paraIcono = new JLabel(icono_pdf);
						
						tabla.getModel().setValueAt(paraIcono, proveedor_seleccionado, 5);
						EjecutaConsultasProveedor paraActualizarLista = new EjecutaConsultasProveedor();
						String cod_proveedor = (String)tabla.getValueAt(seleccionado, 0);
						paraActualizarLista.actualizarListaPrecios(destino.getAbsolutePath(),cod_proveedor);
					}
					else if(extension.equals("xls") || extension.equals("xlsx")){
						
						ImageIcon icono_excel = new ImageIcon(getClass().getResource("/img/icono_excel.png"));
						JLabel paraIcono = new JLabel(icono_excel);
						tabla.getModel().setValueAt(paraIcono, proveedor_seleccionado, 5);
						EjecutaConsultasProveedor paraActualizarLista = new EjecutaConsultasProveedor();
						String cod_proveedor = (String)tabla.getValueAt(seleccionado, 0);
						paraActualizarLista.actualizarListaPrecios(destino.getAbsolutePath(),cod_proveedor);
					}
					//SI SE SELECCIONO UN TIPO DE ARCHIVO INCORRECTO, MUESTRA UN MENSAJE DE ERROR
					else{
						JOptionPane.showMessageDialog(null, "<html><font size=5>Solo se permiten archivos PDF o de Microsoft Excel</font></html>",
								"¡Error!",JOptionPane.WARNING_MESSAGE);
					}
				}
				
				
				
				
			}catch(IOException ex2){
				ex2.printStackTrace();
			}
		}
		
	}
	
	int seleccionado;
	JTable tabla;
}
