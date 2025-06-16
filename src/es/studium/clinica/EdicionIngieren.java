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
    Label lblCantidad = new Label("Cantidad:");

    Choice chPaciente = new Choice();
    Choice chTratamiento = new Choice();
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
        ventanaEdicion.setLocationRelativeTo(null);
        ventanaEdicion.setBackground(Color.PINK);

        ventanaEdicion.add(lblPaciente);
        ventanaEdicion.add(chPaciente);
        ventanaEdicion.add(lblTratamiento);
        ventanaEdicion.add(chTratamiento);
        ventanaEdicion.add(lblCantidad);
        ventanaEdicion.add(txtCantidad);
        ventanaEdicion.add(new Label());
        ventanaEdicion.add(btnModificar);

        btnModificar.addActionListener(this);
        ventanaEdicion.addWindowListener(this);

        feedback.setLayout(new FlowLayout());
        feedback.setSize(300, 100);
        feedback.setResizable(false);
        feedback.add(mensaje);
        feedback.addWindowListener(this);
        feedback.setLocationRelativeTo(null);
        feedback.setBackground(Color.PINK);

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
            idIngieren = choRegistro.getSelectedItem().split(" - ")[0];

            connection = modelo.conectar();
            modelo.rellenarChoicePacientes(connection, chPaciente);
            modelo.rellenarChoiceTratamientos(connection, chTratamiento);
            modelo.editarCamposIngieren(connection, usuario, idIngieren, chPaciente, chTratamiento, txtCantidad);
            modelo.desconectar(connection);

            ventanaEdicion.setVisible(true);
        }
        else if (actionEvent.getSource().equals(btnModificar))
        {
            if (txtCantidad.getText().isBlank())
            {
                mensaje.setText("Todos los campos deben estar completos.");
                feedback.setVisible(true);
            }
            else
            {
                String idPacienteFK = chPaciente.getSelectedItem().split(" - ")[0];
                String idTratamientoFK = chTratamiento.getSelectedItem().split(" - ")[0];

                sentencia = "UPDATE ingieren SET idPacienteFK = '" + idPacienteFK +
                            "', idTratamientoFK = '" + idTratamientoFK +
                            "', cantidad = '" + txtCantidad.getText() +
                            "' WHERE idIngiere = " + idIngieren + ";";

                connection = modelo.conectar();
                modelo.modificarIngieren(connection, usuario, sentencia);
                modelo.desconectar(connection);

                mensaje.setText("Modificación correcta");
                feedback.setVisible(true);
                rellenarChoice();
                ventanaEdicion.setVisible(false);
            }
        }
    }

    public void rellenarChoice()
    {
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