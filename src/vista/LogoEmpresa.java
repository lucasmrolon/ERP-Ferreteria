package vista;

import java.awt.Dimension;
import java.awt.Graphics;

import javax.swing.ImageIcon;
import javax.swing.JPanel;

class LogoEmpresa extends JPanel{
	/**
	 * 
	 */
	private static final long serialVersionUID = -1955194194285390226L;

	public LogoEmpresa(int x, int y) {
		this.setSize(x, y); 
	}

	public void paint(Graphics grafico) {
		Dimension height = getSize();

		ImageIcon Img = new ImageIcon(getClass().getResource("/img/logo_empresa.png")); //"src/logo_empresa.png"


		grafico.drawImage(Img.getImage(), 0, 0, height.width, height.height, null);

		setOpaque(false);
		super.paintComponent(grafico);
	}
}
