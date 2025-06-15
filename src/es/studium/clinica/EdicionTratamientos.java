package es.studium.clinica;


import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class EdicionTratamientos implements WindowListener, ActionListener 
{
	
	Frame ventana = new Frame("Edición Tratamientos");
	
	Choice choTratamiento = new Choice();
	Button btnEditar = new Button("Editar registro");
	Frame ventanaEdicion = new Frame("Editando...");
	
	Label lblFechaCaducidad = new Label("Fecha de caducidad:");
	Label lblTipo = new Label("Tipo:");
	Label lblPrecio = new Label("Precio:");
	TextField txtFechaCaducidad = new TextField(15);
	TextField txtTipo = new TextField(20);
	TextField txtPrecio = new TextField(20);
	
	Button btnModificar = new Button("Modificar registro");
	
	Dialog feedback = new Dialog(ventana, "Feedback", true);
	Label mensaje = new Label("Modificación Correcta");
	String sentencia = "";
	
	Modelo modelo = new Modelo();
	Connection connection = null;
	Statement statement = null;
	ResultSet resultset = null;
	String idTratamiento = null;

	String usuario;
	public EdicionTratamientos(String usuario) 
	{
		
		ventana.setLayout(new FlowLayout());
		ventana.setSize(270, 150);
		ventana.setResizable(false);
		ventana.setLocationRelativeTo(null);
		ventana.setBackground(Color.pink);
		
		rellenarChoice();
		ventana.add(choTratamiento);
		
		ventana.add(btnEditar);
		btnEditar.addActionListener(this);
		ventana.addWindowListener(this);
		
		ventanaEdicion.setLayout(new FlowLayout());
		ventanaEdicion.setSize(300, 170);
		ventanaEdicion.setResizable(false);
		ventanaEdicion.setBackground(Color.pink);
		
		ventanaEdicion.add(lblFechaCaducidad);
		ventanaEdicion.add(txtFechaCaducidad);
		ventanaEdicion.add(lblTipo);
		ventanaEdicion.add(txtTipo);
		ventanaEdicion.add(lblPrecio);
		ventanaEdicion.add(txtPrecio);
		ventanaEdicion.add(btnModificar);
		
		btnModificar.addActionListener(this);
		ventanaEdicion.addWindowListener(this);
		ventanaEdicion.setLocationRelativeTo(null);
		
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
			
			ventana.dispose();
		}
	}

	public void actionPerformed(ActionEvent actionEvent) 
	{
		
		if (actionEvent.getSource().equals(btnEditar)) 
		{
			
			idTratamiento = choTratamiento.getSelectedItem().split(" - ")[0];
			connection = modelo.conectar();
			modelo.editarCamposTratamiento(connection, usuario, idTratamiento, txtFechaCaducidad, txtTipo, txtPrecio);
			sentencia = "SELECT * FROM tratamientos WHERE idTratamiento = " + idTratamiento;
			modelo.desconectar(connection);
			ventanaEdicion.setVisible(true);
		
		} 
		
		else if (actionEvent.getSource().equals(btnModificar)) 
		{
		    // Validar campos vacíos
		    if (txtFechaCaducidad.getText().trim().isEmpty() ||
		        txtTipo.getText().trim().isEmpty() ||
		        txtPrecio.getText().trim().isEmpty()) 
		    {
		        mensaje.setText("No puede haber campos vacíos");
		        feedback.setVisible(true);
		        return;  // Salimos sin modificar nada
		    }
		    
		    connection = modelo.conectar();
		    boolean modificado = modelo.modificarTratamiento(connection, usuario, "UPDATE tratamientos SET fechaCaducidadTratamiento = '"
		        + txtFechaCaducidad.getText() + "', tipoTratamiento = '"
		        + txtTipo.getText() + "', precioTratamiento = '"
		        + txtPrecio.getText() + "' WHERE idTratamiento = "
		        + idTratamiento + ";");
		    modelo.desconectar(connection);
		    
		    if (modificado) {
		        mensaje.setText("Modificación correcta");
		    } else {
		        mensaje.setText("Modificación incorrecta");
		    }
		    feedback.setVisible(true);
		    rellenarChoice();
		    ventanaEdicion.setVisible(false);
		}
	}

	public void rellenarChoice() {
		connection = modelo.conectar();
		modelo.rellenarChoiceTratamiento(connection, sentencia, choTratamiento);
		modelo.desconectar(connection);
	}

	public void windowActivated(WindowEvent e) {}
	public void windowClosed(WindowEvent e) {}
	public void windowDeactivated(WindowEvent e) {}
	public void windowDeiconified(WindowEvent e) {}
	public void windowIconified(WindowEvent e) {}
	public void windowOpened(WindowEvent e) {}
}
