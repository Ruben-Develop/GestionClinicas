package es.studium.clinica;

import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class EdicionIngieren implements WindowListener, ActionListener 
{
    Frame ventana = new Frame("Edición Ingieren");

    Choice choRegistro = new Choice();
    Button btnEditar = new Button("Editar registro");
    Frame ventanaEdicion = new Frame("Editando...");

    Label lblPaciente = new Label("Paciente:");
    Label lblTratamiento = new Label("Tratamiento:");

    Choice chPaciente = new Choice();
    Choice chTratamiento = new Choice();

    Label lblCantidad = new Label("Cantidad:");

    TextField txtCantidad = new TextField(10);

    Button btnModificar = new Button("Modificar registro");

    Dialog feedback = new Dialog(ventana, "Feedback", true);
    Label mensaje = new Label("Modificación Correcta");
    String sentencia = "";

    Modelo modelo = new Modelo();
    Connection connection = null;
    Statement statement = null;
    ResultSet resultset = null;
    String idIngieren = null;

    String usuario;

    public EdicionIngieren(String usuario) 
    {
        this.usuario = usuario;

        ventana.setLayout(new FlowLayout());
        ventana.setSize(300, 150);
        ventana.setResizable(false);
        ventana.setLocationRelativeTo(null);
        ventana.setBackground(Color.PINK);

        rellenarChoice();
        ventana.add(choRegistro);
        ventana.add(btnEditar);
        btnEditar.addActionListener(this);
        ventana.addWindowListener(this);

        ventanaEdicion.setLayout(new GridLayout(4, 2));
        ventanaEdicion.setSize(370, 200);
        ventanaEdicion.setResizable(false);

        // Labels
        ventanaEdicion.add(lblPaciente);
        ventanaEdicion.add(chPaciente);
        ventanaEdicion.add(lblTratamiento);
        ventanaEdicion.add(chTratamiento);
        ventanaEdicion.add(lblCantidad);
        ventanaEdicion.add(txtCantidad);
        ventanaEdicion.add(btnModificar);

        btnModificar.addActionListener(this);
        ventanaEdicion.addWindowListener(this);
        ventanaEdicion.setLocationRelativeTo(null);
        ventanaEdicion.setBackground(Color.PINK);

        feedback.setLayout(new FlowLayout());
        feedback.setSize(300, 100);
        feedback.setResizable(false);
        feedback.add(mensaje);
        feedback.addWindowListener(this);
        feedback.setLocationRelativeTo(null);
        feedback.setBackground(Color.LIGHT_GRAY);

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
                }
                if (connection != null)
                {
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
            idIngieren = choRegistro.getSelectedItem().split(" - ")[0];
            connection = modelo.conectar();

            // Llenar Choices
            modelo.rellenarChoicePacientes(connection, chPaciente);
            modelo.rellenarChoiceTratamientos(connection, chTratamiento);

            // Llenar campos
            modelo.editarCamposIngieren(connection, usuario, idIngieren, chPaciente, chTratamiento, txtCantidad);

            modelo.desconectar(connection);
            ventanaEdicion.setVisible(true);
        } 
        else if (actionEvent.getSource().equals(btnModificar)) 
        {
            if (!txtCantidad.getText().isBlank()) 
            {
                connection = modelo.conectar();
                boolean exito = modelo.modificarIngieren(connection, usuario, idIngieren, txtCantidad.getText(), idIngieren, idIngieren, idIngieren, idIngieren);
                modelo.desconectar(connection);
                if (exito) {
                    mensaje.setText("Modificación Correcta");
                } else {
                    mensaje.setText("Error en la modificación");
                }
                feedback.setVisible(true);
                rellenarChoice();
                ventanaEdicion.setVisible(false);
            } 
            else 
            {
                mensaje.setText("No puede haber ningún campo en blanco");
                feedback.setVisible(true);
            }
        }
    }

    public void rellenarChoice() {
        connection = modelo.conectar();
        modelo.rellenarChoiceIngieren1(connection, choRegistro);
        modelo.desconectar(connection);
    }

    public void windowActivated(WindowEvent e) {}
    public void windowClosed(WindowEvent e) {}
    public void windowDeactivated(WindowEvent e) {}
    public void windowDeiconified(WindowEvent e) {}
    public void windowIconified(WindowEvent e) {}
    public void windowOpened(WindowEvent e) {}
}