package es.studium.clinica;

import java.awt.Button;
import java.awt.Choice;
import java.awt.Color;
import java.awt.Dialog;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.Label;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class BajaTratamientos implements WindowListener, ActionListener 
{
	
	Frame ventana = new Frame("Baja Tratamientos");
	Label fecha = new Label("Fecha de caducidad:");
	
	Choice ch = new Choice();
	Button btnBorrar = new Button("Borrar");
	
	Dialog feedback = new Dialog(ventana, "Mensaje", true);
	
	Label msj = new Label("Borrado correcto");
	Dialog confirmacion = new Dialog(ventana, "Mensaje", true);
	Label pregunta = new Label("¿ Estas seguro de querer borrar el tratamiento ?");
	
	Button btnSi = new Button("Sí");
	Button btnNo = new Button("No");
	
	String sentencia = "";
	Connection connection = null;
	Statement statement = null;
	ResultSet rs = null;

	String usuario;
	
	public BajaTratamientos(String usuario) {
		ventana.setLayout(new FlowLayout());
		ventana.setSize(240, 200);
		ventana.setResizable(false);
		ventana.setBackground(Color.pink);
		
		Modelo modelo = new Modelo();
		Connection connection = modelo.conectar();
		
		modelo.rellenarChoiceTratamiento(connection, usuario, ch);
		modelo.desconectar(connection);
		
		ventana.add(ch);
		ventana.add(btnBorrar);
		btnBorrar.addActionListener(this);
		ventana.setLocationRelativeTo(null);
		ventana.addWindowListener(this);
		ventana.setVisible(true);
		
		feedback.setLayout(new FlowLayout());
		feedback.setSize(300, 100);
		feedback.setResizable(false);
		feedback.add(msj);
		feedback.addWindowListener(this);
		feedback.setBackground(Color.pink);
		
		confirmacion.setLayout(new FlowLayout());
		confirmacion.setSize(200, 100);
		confirmacion.setResizable(false);
		confirmacion.add(pregunta);
		
		btnSi.addActionListener(this);
		btnNo.addActionListener(this);
		
		confirmacion.add(btnSi);
		confirmacion.add(btnNo);
		confirmacion.setLocationRelativeTo(null);
		confirmacion.addWindowListener(this);
		confirmacion.setBackground(Color.pink);
		
		ventana.setVisible(true);
	}

	@Override
	public void actionPerformed(ActionEvent actionEvent) 
	{
		
		if(actionEvent.getSource().equals(btnBorrar)) {
			pregunta.setText("¿Segur@ de borrar " + ch.getSelectedItem()+"?");
			confirmacion.setVisible(true);
			
		}
		
		else if(actionEvent.getSource().equals(btnNo)) 
		{
			
			confirmacion.setVisible(false);
			
		}
		
		else if(actionEvent.getSource().equals(btnSi)) 
		{
		    Modelo modelo = new Modelo();
		    Connection connection = modelo.conectar();
		    String idTratamiento = ch.getSelectedItem().split(" - ")[0];
		    
		    // Llamar al método y mostrar resultado
		    if(!modelo.bajaTratamiento(connection, usuario, idTratamiento)) 
		    {
		        msj.setText("Baja incorrecta");
		    }
		    else 
		    {
		        msj.setText("Baja correcta");
		    }
		    
		    feedback.setVisible(true);
		    modelo.desconectar(connection);
		}
	}

	@Override
	public void windowOpened(WindowEvent e) {}

	@Override
	public void windowClosing(WindowEvent e) 
	{
		
		if(e.getSource().equals(feedback)) 
		{
			
			feedback.setVisible(false);
			confirmacion.setVisible(false);
			
		}
		
		else if(e.getSource().equals(confirmacion)) 
		{
			
			confirmacion.setVisible(false);
			
		}
		
		else 
		{
			
			ventana.dispose();
			
		}
	}

	@Override
	public void windowClosed(WindowEvent e) {}
	@Override
	public void windowIconified(WindowEvent e) {}
	@Override
	public void windowDeiconified(WindowEvent e) {}
	@Override
	public void windowActivated(WindowEvent e) {}
	@Override
	public void windowDeactivated(WindowEvent e) {}
}
