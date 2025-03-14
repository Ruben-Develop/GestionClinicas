package es.studium.clinica;

import java.awt.Button;
import java.awt.Color;
import java.awt.Dialog;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.Label;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.sql.Connection;

public class AltaClinicas implements WindowListener, ActionListener
{
	Frame ventana = new Frame("Alta Clinica");
	
	Label lblClinica = new Label("Clinica:");
	TextField txtClinica = new TextField(20);
	
	Label lblDireccion = new Label("Dirección:");
	TextField txtDireccion = new TextField(20);
	
	Label lblTelefono = new Label("Teléfono:");
	TextField txtTelefono = new TextField(20);
	
	Label lblHorario = new Label("Horario:");
	TextField txtHorario = new TextField(20);
	
	Button btnAceptar = new Button("Aceptar");
	Button btnLimpiar = new Button("Limpiar");
	
	Dialog feedback = new Dialog(ventana, "Mensaje", true);
	Label mensaje = new Label("");

	public AltaClinicas()
	{
		ventana.setLayout(new FlowLayout());
		ventana.setSize(300,150);
		ventana.setResizable(false);
		ventana.setLocationRelativeTo(null);
		ventana.setBackground(Color.PINK);
		
		ventana.add(lblDireccion);
		ventana.add(txtDireccion);
		ventana.add(lblTelefono);
		ventana.add(txtTelefono);
		ventana.add(lblHorario);
		ventana.add(txtHorario);
		
		btnAceptar.addActionListener(this);
		ventana.add(btnAceptar);
		btnLimpiar.addActionListener(this);
		ventana.add(btnLimpiar);
		ventana.addWindowListener(this);
		
		feedback.setLayout(new FlowLayout());
		feedback.setSize(280,100);
		feedback.setResizable(false);
		feedback.setBackground(Color.pink);
		feedback.add(mensaje);
		feedback.addWindowListener(this);
		
		ventana.setVisible(true);
	}

	public void windowActivated(WindowEvent windowEvent){}
	public void windowClosed(WindowEvent windowEvent) {}
	public void windowClosing(WindowEvent windowEvent)
	{
		if(windowEvent.getSource().equals(feedback))
		{
			feedback.setVisible(false);
		}
		else
		{
			System.exit(0);
		}
	}
	public void windowDeactivated(WindowEvent windowEvent) {}
	public void windowDeiconified(WindowEvent windowEvent) {}
	public void windowIconified(WindowEvent windowEvent) {}
	public void windowOpened(WindowEvent windowEvent) {}

	public void actionPerformed(ActionEvent actionEvent)
	{
		if(actionEvent.getSource().equals(btnLimpiar))
		{
			txtClinica.setText("");
			txtDireccion.setText("");
			txtTelefono.setText("");
			txtHorario.setText("");
			txtClinica.requestFocus();
		}
		else if(actionEvent.getSource().equals(btnAceptar))
		{
			// Conectarse a la BD
			Modelo modelo = new Modelo();
			Connection connection = modelo.conectar();
			// Hacer el Alta
			if(!modelo.altaClinica(connection, txtDireccion.getText(), txtTelefono.getText(), txtHorario.getText())) 
			{
				mensaje.setText("Error en Alta");
			}
			else
			{
				mensaje.setText("Alta Correcta");
			}
			feedback.setVisible(true);
			// Desconectar
			modelo.desconectar(connection);
		}
	}
}

