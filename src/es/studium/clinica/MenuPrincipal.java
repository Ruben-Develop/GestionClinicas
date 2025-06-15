package es.studium.clinica;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.Menu;
import java.awt.MenuBar;
import java.awt.MenuItem;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
public class MenuPrincipal implements WindowListener, ActionListener
{

	Frame ventana = new Frame("Menú Principal");
	
	MenuBar barraMenu = new MenuBar();
	Menu mnuClinicas = new Menu("Clinicas");
	Menu mnuTratamientos = new Menu("Tratamientos");
	Menu mnuPacientes = new Menu("Pacientes");
	Menu mnuIngieren = new Menu("Ingieren");
	Menu mnuAyuda = new Menu("Ayuda");

	MenuItem mniConsultaClinicas = new MenuItem("Consulta");
	MenuItem mniAltaClinicas = new MenuItem("Alta");
	MenuItem mniBajaClinicas = new MenuItem("Baja");
	MenuItem mniEditarClinicas = new MenuItem("Edición");

	MenuItem mniConsultaTratamientos = new MenuItem("Consulta");
	MenuItem mniAltaTratamientos = new MenuItem("Alta");
	MenuItem mniBajaTratamientos = new MenuItem("Baja");
	MenuItem mniEditarTratamientos = new MenuItem("Edición");

	MenuItem mniConsultaPacientes = new MenuItem("Consulta");
	MenuItem mniAltaPacientes = new MenuItem("Alta");
	MenuItem mniBajaPacientes = new MenuItem("Baja");
	MenuItem mniEditarPacientes = new MenuItem("Edición");

	MenuItem mniConsultaIngieren = new MenuItem("Consulta");
	MenuItem mniAltaIngieren = new MenuItem("Alta");
	MenuItem mniBajaIngieren = new MenuItem("Baja");
	MenuItem mniEditarIngieren = new MenuItem("Edición");
	
	MenuItem mniAyuda = new MenuItem("Ayuda");

	int tipo;
	String usuario;

	public MenuPrincipal(int t, String u)
	{
		tipo = t;
		usuario = u;
		ventana.setLayout(new FlowLayout());
		ventana.addWindowListener(this);
		ventana.setBackground(Color.pink);

		mniConsultaClinicas.addActionListener(this);
		mniAltaClinicas.addActionListener(this);
		mniBajaClinicas.addActionListener(this);
		mniEditarClinicas.addActionListener(this);

		mnuClinicas.add(mniAltaClinicas);
		if(tipo==1) 
		{
			mnuClinicas.add(mniConsultaClinicas);
			mnuClinicas.add(mniBajaClinicas);
			mnuClinicas.add(mniEditarClinicas);
		}

		mniConsultaTratamientos.addActionListener(this);
		mniAltaTratamientos.addActionListener(this);
		mniBajaTratamientos.addActionListener(this);
		mniEditarTratamientos.addActionListener(this);

		mnuTratamientos.add(mniAltaTratamientos);
		if(tipo==1)
		{
			mnuTratamientos.add(mniConsultaTratamientos);
			mnuTratamientos.add(mniBajaTratamientos);
			mnuTratamientos.add(mniEditarTratamientos);
		}
		mniConsultaPacientes.addActionListener(this);
		mniAltaPacientes.addActionListener(this);
		mniBajaPacientes.addActionListener(this);
		mniEditarPacientes.addActionListener(this);

		mnuPacientes.add(mniAltaPacientes);
		if(tipo==1) {
			mnuPacientes.add(mniConsultaPacientes);
			mnuPacientes.add(mniBajaPacientes);
			mnuPacientes.add(mniEditarPacientes);
		}
		mniConsultaIngieren.addActionListener(this);
		mniAltaIngieren.addActionListener(this);
		mniBajaIngieren.addActionListener(this);
		mniEditarIngieren.addActionListener(this);

		mnuIngieren.add(mniAltaIngieren);
		if(tipo==1) {
			mnuIngieren.add(mniConsultaIngieren);
			mnuIngieren.add(mniBajaIngieren);
			mnuIngieren.add(mniEditarIngieren);
		}
		mniAyuda.addActionListener(this);
		mnuAyuda.add(mniAyuda);

	
		barraMenu.add(mnuClinicas);
		barraMenu.add(mnuTratamientos);
		barraMenu.add(mnuPacientes);
		barraMenu.add(mnuIngieren);
		barraMenu.add(mnuAyuda);

		ventana.setMenuBar(barraMenu);
		ventana.setSize(350,200);
		ventana.setVisible(true);
	}
	public void windowActivated(WindowEvent windowEvent){}
	public void windowClosed(WindowEvent windowEvent) {}
	public void windowClosing(WindowEvent windowEvent)
	{
		System.exit(0);
	}
	public void windowDeactivated(WindowEvent windowEvent) {}
	public void windowDeiconified(WindowEvent windowEvent) {}
	public void windowIconified(WindowEvent windowEvent) {}
	public void windowOpened(WindowEvent windowEvent) {}
	public void actionPerformed(ActionEvent actionEvent)
	{
		if(actionEvent.getSource().equals(mniAltaClinicas))
		{
		new AltaClinicas(usuario);
		}
		else if(actionEvent.getSource().equals(mniConsultaClinicas))
		{
		new ConsultaClinicas(usuario);
		}
		else if(actionEvent.getSource().equals(mniBajaClinicas))
		{
		new BajaClinicas(usuario);
		}
		else if(actionEvent.getSource().equals(mniEditarClinicas))
		{
		new EdicionClinicas(usuario);
		}
		
		else if(actionEvent.getSource().equals(mniAltaTratamientos))
		{
		new AltaTratamientos(usuario);
		}
		else if(actionEvent.getSource().equals(mniConsultaTratamientos))
		{
		new ConsultaTratamientos(usuario);
		}
		else if(actionEvent.getSource().equals(mniBajaTratamientos))
		{
		new BajaTratamientos(usuario);
		}
		else if(actionEvent.getSource().equals(mniEditarTratamientos))
		{
		new EdicionTratamientos(usuario);
		}
		
		else if(actionEvent.getSource().equals(mniAltaPacientes))
		{
		new AltaPacientes(usuario);
		}
		else if(actionEvent.getSource().equals(mniConsultaPacientes))
		{
		new ConsultaPacientes(usuario);
		}
		else if(actionEvent.getSource().equals(mniBajaPacientes))
		{
		new BajaPacientes(usuario);
		}
		else if(actionEvent.getSource().equals(mniEditarPacientes))
		{
		new EdicionPacientes(usuario);
		}
		
		else if(actionEvent.getSource().equals(mniAltaIngieren))
		{
		new AltaIngieren(usuario);
		}
		else if(actionEvent.getSource().equals(mniConsultaIngieren))
		{
		new ConsultaIngieren(usuario);
		}
		else if(actionEvent.getSource().equals(mniBajaIngieren))
		{
		new BajaIngieren(usuario);
		}
		else if(actionEvent.getSource().equals(mniEditarIngieren))
		{
		new EdicionIngieren(usuario);
		}
		else if(actionEvent.getSource().equals(mniAyuda))
		{
		Modelo.ayuda();
		}
	}
}
