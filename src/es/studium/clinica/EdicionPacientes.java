package es.studium.clinica;

import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class EdicionPacientes implements WindowListener, ActionListener 
{
    Frame ventana = new Frame("Edición Pacientes");
    
    Choice choPaciente = new Choice();
    Button btnEditar = new Button("Editar registro");
    Frame ventanaEdicion = new Frame("Editando...");
    
    Label lblNombrePaciente = new Label("Nombre:");
    Label lblEdadPaciente = new Label("Edad:");
    Label lblMedicoPaciente = new Label("Médico:");
    TextField txtNombrePaciente = new TextField(20);
    TextField txtEdadPaciente = new TextField(5);
    TextField txtMedicoPaciente = new TextField(20);
    
    Button btnModificar = new Button("Modificar registro");
    
    Dialog feedback = new Dialog(ventana, "Feedback", true);
    Label mensaje = new Label("Modificación Correcta");
    String sentencia = "";
    
    Modelo modelo = new Modelo();
    Connection connection = null;
    Statement statement = null;
    ResultSet resultset = null;
    String idPaciente = null;
    
    String usuario;

    public EdicionPacientes(String usuario) 
    {
        this.usuario = usuario;
        ventana.setLayout(new FlowLayout());
        ventana.setSize(267, 150);
        ventana.setResizable(false);
        ventana.setLocationRelativeTo(null);
        ventana.setBackground(Color.pink);
        
        rellenarChoice();
        ventana.add(choPaciente);
        
        ventana.add(btnEditar);
        btnEditar.addActionListener(this);
        ventana.addWindowListener(this);
        
        ventanaEdicion.setLayout(new GridLayout(4, 2));
        ventanaEdicion.setSize(300, 200);
        ventanaEdicion.setResizable(false);
        ventanaEdicion.add(lblNombrePaciente);
        ventanaEdicion.add(txtNombrePaciente);
        ventanaEdicion.add(lblEdadPaciente);
        ventanaEdicion.add(txtEdadPaciente);
        ventanaEdicion.add(lblMedicoPaciente);
        ventanaEdicion.add(txtMedicoPaciente);
        ventanaEdicion.add(new Label()); 
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
            ventana.dispose();
        }
    }

    public void actionPerformed(ActionEvent actionEvent) 
    {
        if (actionEvent.getSource().equals(btnEditar)) 
        {
            idPaciente = choPaciente.getSelectedItem().split(" - ")[0];
            connection = modelo.conectar();
            modelo.editarCamposPaciente(connection, usuario, idPaciente, txtNombrePaciente, txtEdadPaciente, txtMedicoPaciente);
            modelo.desconectar(connection);
            ventanaEdicion.setVisible(true);
        } 
        else if (actionEvent.getSource().equals(btnModificar)) 
        {
            if (txtNombrePaciente.getText().isBlank() ||
                txtEdadPaciente.getText().isBlank() ||
                txtMedicoPaciente.getText().isBlank())
            {
                mensaje.setText("Todos los campos deben estar completos.");
                feedback.setVisible(true);
            }
            else 
            {
                connection = modelo.conectar();
                modelo.modificarPaciente(connection, usuario,
                        "UPDATE pacientes SET nombrePaciente = '"
                        + txtNombrePaciente.getText() + "', edadPaciente = '"
                        + txtEdadPaciente.getText() + "', medicoPaciente = '"
                        + txtMedicoPaciente.getText() + "' WHERE idPaciente = "
                        + idPaciente + ";");
                modelo.desconectar(connection);
                mensaje.setText("Modificación correcta");
                feedback.setVisible(true);
                rellenarChoice();
                ventanaEdicion.setVisible(false);
            }
        }
    }

    public void rellenarChoice() {
        connection = modelo.conectar();
        modelo.rellenarChoicePaciente(connection, choPaciente);
        modelo.desconectar(connection);
    }

    public void windowActivated(WindowEvent e) {}
    public void windowClosed(WindowEvent e) {}
    public void windowDeactivated(WindowEvent e) {}
    public void windowDeiconified(WindowEvent e) {}
    public void windowIconified(WindowEvent e) {}
    public void windowOpened(WindowEvent e) {}
}