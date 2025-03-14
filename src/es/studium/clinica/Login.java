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

public class Login implements WindowListener, ActionListener
{
	
	Frame ventana = new Frame("Clínica");
	Label lblUsuario = new Label("Usuario:");
	TextField txtUsuario = new TextField(20);
	Label lblClave = new Label("Contraseña:");
	TextField txtClave = new TextField(20);
	Button btnAceptar = new Button("Aceptar");
	Button btnLimpiar = new Button("Limpiar");

	Dialog feedback = new Dialog(ventana, "Error", true);
	Label mensaje = new Label("Error en la contraseña o nombre de usuario");

	
	public Login()
	{
		
		ventana.setLayout(new FlowLayout());
		ventana.setSize(220,200);
		ventana.setResizable(false);
		ventana.setBackground(Color.pink);
		
		ventana.add(lblUsuario);
		ventana.add(txtUsuario);
		ventana.add(lblClave);
		
		txtClave.setEchoChar('#');
		ventana.add(txtClave);
		btnAceptar.addActionListener(this);
		btnLimpiar.addActionListener(this);
		ventana.add(btnAceptar);
		ventana.add(btnLimpiar);
		ventana.setLocationRelativeTo(null);
		ventana.addWindowListener(this);

		feedback.setLayout(new FlowLayout());
		feedback.setSize(200,100);
		feedback.setResizable(false);
		feedback.add(mensaje);
		feedback.addWindowListener(this);
		feedback.setLocationRelativeTo(null);

		ventana.setVisible(true);
	}

	public static void main(String[] args)
	{
		
		new Login();
		
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
			
			txtUsuario.setText("");
			txtClave.setText("");
			txtUsuario.requestFocus();
			
		}
		
		else if(actionEvent.getSource().equals(btnAceptar))
		{
			
			// Conectar a la BD
			Modelo modelo = new Modelo();
			Connection connection = modelo.conectar();
			System.out.println(connection);
			// Comprobar credenciales
			
			int tipo = modelo.comprobarCredenciales(connection, txtUsuario.getText(), txtClave.getText());
			if(tipo==-1)

				// Si NO, mostrar feedback
			{
				
				mensaje.setText("Credenciales incorrectas");
				feedback.setVisible(true);
				
			}

			// Si SÍ, cerrar esta VENTANA y abrir Menú Ppal
			else
			{
				
				ventana.setVisible(false);
				new MenuPrincipal(tipo);
				
			}
		}

	}

}
