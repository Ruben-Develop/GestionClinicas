package es.studium.clinica;

import java.awt.Button;
import java.awt.Color;
import java.awt.Dialog;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.Label;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.sql.Connection;

public class AltaClinicas implements WindowListener, ActionListener
{
	Frame ventana = new Frame("Alta Clínica");
	
	Label lblClinica = new Label("Clínica:");
	TextField txtClinica = new TextField(20);
	
	Label lblDireccion = new Label("Dirección:");
	TextField txtDireccion = new TextField(20);
	
	Label lblTelefono = new Label("Teléfono: ");
	TextField txtTelefono = new TextField(20);
	
	Label lblHorario = new Label("Horario:  ");
	TextField txtHorario = new TextField(20);
	
	Button btnAceptar = new Button("Aceptar");
	Button btnLimpiar = new Button("Limpiar");
	
	Dialog feedback = new Dialog(ventana, "Mensaje", true);
	Label mensaje = new Label("");
	
	String usuario;

	public AltaClinicas(String u)
	{
		usuario = u;
		ventana.setLayout(new GridLayout(4,2, 5, 5));
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
		feedback.setBackground(Color.GRAY);
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
			ventana.dispose();
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
			txtDireccion.setText("");
			txtTelefono.setText("");
			txtHorario.setText("");
			txtClinica.requestFocus();
		}
		
		else if(actionEvent.getSource().equals(btnAceptar))
		{
			if(txtDireccion.getText().isBlank() || txtTelefono.getText().isBlank() || txtHorario.getText().isBlank()) 
			{
				mensaje.setText("Debe rellenar todos los campos");
				feedback.setVisible(true);
				return;
			}
			
			// Conectarse a la BD
			Modelo modelo = new Modelo();
			Connection connection = modelo.conectar();
			
			// Hacer el Alta
			if(!modelo.altaClinica(connection, usuario,
					txtDireccion.getText(), txtTelefono.getText(), txtHorario.getText())) 
			{
				mensaje.setText("Error en Alta");
			}
			else
			{
				mensaje.setText("Alta Correcta");

				txtDireccion.setText("");
				txtTelefono.setText("");
				txtHorario.setText("");
				txtClinica.requestFocus();
			}
			feedback.setVisible(true);
			
			// Desconectar
			modelo.desconectar(connection);
		}
	}
}

