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

public class AltaTratamientos implements WindowListener, ActionListener
{
	Frame ventana = new Frame("Alta tratamiento");
	
	Label lblClinica = new Label("Tratamiento:");
	TextField txtClinica = new TextField(20);
	
	Label lblFechaDeCaducidad = new Label("Fecha de caducidad:");
	TextField txtFechaDeCaducidad = new TextField(20);
	
	Label lblTipoTratamiento = new Label("Tipo de tratamiento:");
	TextField txtTipoTratamiento = new TextField(20);
	
	Label lblPrecioTratamiento = new Label("Precio del tratamiento:");
	TextField txtPrecioTratamiento = new TextField(20);
	
	Button btnAceptar = new Button("Aceptar");
	Button btnLimpiar = new Button("Limpiar");
	
	Dialog feedback = new Dialog(ventana, "Mensaje", true);
	Label mensaje = new Label("");

	public AltaTratamientos()
	{
		ventana.setLayout(new FlowLayout());
		ventana.setSize(370,150);
		ventana.setResizable(false);
		ventana.setLocationRelativeTo(null);
		ventana.setBackground(Color.PINK);
		
		ventana.add(lblFechaDeCaducidad);
		ventana.add(txtFechaDeCaducidad);
		ventana.add(lblTipoTratamiento);
		ventana.add(txtTipoTratamiento);
		ventana.add(lblPrecioTratamiento);
		ventana.add(txtPrecioTratamiento);
		
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
			txtFechaDeCaducidad.setText("");
			txtTipoTratamiento.setText("");
			txtPrecioTratamiento.setText("");
			txtClinica.requestFocus();
		}
		else if(actionEvent.getSource().equals(btnAceptar))
		{
			// Conectarse a la BD
			Modelo modelo = new Modelo();
			Connection connection = modelo.conectar();
			// Hacer el Alta
			if(!modelo.altaTratamiento(connection, txtFechaDeCaducidad.getText(), txtTipoTratamiento.getText(), txtPrecioTratamiento.getText())) 
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
