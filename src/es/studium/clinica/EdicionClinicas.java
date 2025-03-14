package es.studium.clinica;

import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class EdicionClinicas implements WindowListener, ActionListener 
{
	
	Frame ventana = new Frame("Edición Clinicas");
	
	Choice choClinica = new Choice();
	Button btnEditar = new Button("Editar registro");
	Frame ventanaEdicion = new Frame("Editando...");
	
	Label lblDireccionClinica = new Label("Dirección:");
	Label lblTelefonoClinica = new Label("Teléfono:");
	Label lblHorarioClinica = new Label("Horario:");
	TextField txtDireccionClinica = new TextField(20);
	TextField txtTelefonoClinica = new TextField(20);
	TextField txtHorarioClinica = new TextField(20);
	
	Button btnModificar = new Button("Modificar registro");
	
	Dialog feedback = new Dialog(ventana, "Feedback", true);
	Label mensaje = new Label("Modificación Correcta");
	String sentencia = "";
	
	Modelo modelo = new Modelo();
	Connection connection = null;
	Statement statement = null;
	ResultSet resultset = null;
	String idClinica = null;

	public EdicionClinicas() 
	{
		
		ventana.setLayout(new FlowLayout());
		ventana.setSize(267, 150);
		ventana.setResizable(false);
		ventana.setLocationRelativeTo(null);
		ventana.setBackground(Color.pink);
		
		rellenarChoice();
		ventana.add(choClinica);
		
		ventana.add(btnEditar);
		btnEditar.addActionListener(this);
		ventana.addWindowListener(this);
		
		ventanaEdicion.setLayout(new FlowLayout());
		ventanaEdicion.setSize(300, 200);
		ventanaEdicion.setResizable(false);
		ventanaEdicion.add(lblDireccionClinica);
		ventanaEdicion.add(txtDireccionClinica);
		ventanaEdicion.add(lblTelefonoClinica);
		ventanaEdicion.add(txtTelefonoClinica);
		ventanaEdicion.add(lblHorarioClinica);
		ventanaEdicion.add(txtHorarioClinica);
		ventanaEdicion.add(btnModificar);
		
		btnModificar.addActionListener(this);
		ventanaEdicion.addWindowListener(this);
		ventanaEdicion.setLocationRelativeTo(null);
		ventanaEdicion.setBackground(Color.pink);
		
		feedback.setLayout(new FlowLayout());
		feedback.setSize(300, 100);
		feedback.setResizable(false);
		feedback.add(mensaje);
		feedback.addWindowListener(this);
		feedback.setLocationRelativeTo(null);
		feedback.setBackground(Color.pink);
		
		ventana.setVisible(true);
		
	}

	public void windowClosing(WindowEvent windowEvent) 
	{
		
		if (windowEvent.getSource().equals(feedback)) 
		{
			
			feedback.setVisible(false);
		} 
		
		else if (windowEvent.getSource().equals(ventanaEdicion)) 
		{
			
			ventanaEdicion.setVisible(false);
		} 
		else 
		{
			try 
			{
				
				if (statement != null)
				{
					statement.close();
					connection.close();
				}
			} 
			
			catch (SQLException e) 
			{
				
				System.out.println("Error al cerrar " + e.toString());
				
			}
			
			System.exit(0);
		}
	}

	public void actionPerformed(ActionEvent actionEvent) 
	{
		
		if (actionEvent.getSource().equals(btnEditar)) 
		{
			
			idClinica = choClinica.getSelectedItem().split(" - ")[0];
			connection = modelo.conectar();
			modelo.editarCamposClinica(connection, idClinica, txtDireccionClinica, txtTelefonoClinica, txtHorarioClinica);
			sentencia = "SELECT * FROM clinicas WHERE idClinica = " + idClinica;
			modelo.desconectar(connection);
			ventanaEdicion.setVisible(true);
		
		} 
		
		else if (actionEvent.getSource().equals(btnModificar)) 
		{
			
			connection = modelo.conectar();
			modelo.modificarClinica("UPDATE clinicas SET direccionClinica = '"
					+ txtDireccionClinica.getText() + "', telefonoClinica = '"
					+ txtTelefonoClinica.getText() + "', horarioClinica = '"
					+ txtHorarioClinica.getText() + "' WHERE idClinica = "
					+ idClinica + ";");
			modelo.desconectar(connection);
			feedback.setVisible(true);
			rellenarChoice();
			ventanaEdicion.setVisible(false);
			
		}
	}

	public void rellenarChoice() {
		connection = modelo.conectar();
		modelo.rellenarChoiceClinica(connection, choClinica);
		modelo.desconectar(connection);
	}

	public void windowActivated(WindowEvent e) {}
	public void windowClosed(WindowEvent e) {}
	public void windowDeactivated(WindowEvent e) {}
	public void windowDeiconified(WindowEvent e) {}
	public void windowIconified(WindowEvent e) {}
	public void windowOpened(WindowEvent e) {}
}
