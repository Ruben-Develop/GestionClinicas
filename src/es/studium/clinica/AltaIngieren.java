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
import java.awt.Choice;

public class AltaIngieren implements WindowListener, ActionListener
{
    Frame ventana = new Frame("Alta Ingieren");

    Label lblPaciente = new Label("Paciente:");
    Choice chPaciente = new Choice();

    Label lblTratamiento = new Label("Tratamiento:");
    Choice chTratamiento = new Choice();

    Label lblCantidad = new Label("Cantidad:");
    TextField txtCantidad = new TextField(10);

    Button btnAceptar = new Button("Aceptar");
    Button btnLimpiar = new Button("Limpiar");

    Dialog feedback = new Dialog(ventana, "Mensaje", true);
    Label mensaje = new Label("");

    String usuario;

    public AltaIngieren(String u)
    {
        usuario = u;
        ventana.setLayout(new GridLayout(4,2, 5, 5));
        ventana.setSize(300,200);
        ventana.setResizable(false);
        ventana.setLocationRelativeTo(null);
        ventana.setBackground(Color.PINK);

        ventana.add(lblPaciente);
        ventana.add(chPaciente);
        ventana.add(lblTratamiento);
        ventana.add(chTratamiento);
        ventana.add(lblCantidad);
        ventana.add(txtCantidad);

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

        // Cargar pacientes y tratamientos disponibles
        Modelo modelo = new Modelo();
        Connection connection = modelo.conectar();
        modelo.rellenarChoicePaciente(connection, chPaciente);
        modelo.rellenarChoiceTratamiento(connection, usuario, chTratamiento);
        modelo.desconectar(connection);

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
            chPaciente.select(0);
            chTratamiento.select(0);
            txtCantidad.setText("");
            chPaciente.requestFocus();
        }
        else if(actionEvent.getSource().equals(btnAceptar))
        {
            if(chPaciente.getSelectedIndex() == 0 || chTratamiento.getSelectedIndex() == 0 || txtCantidad.getText().isBlank()) 
            {
                mensaje.setText("Debe rellenar todos los campos");
                feedback.setVisible(true);
                return;
            }

            // Conectarse a la BD
            Modelo modelo = new Modelo();
            Connection connection = modelo.conectar();

            // Extraer valores seleccionados
            String idPaciente = chPaciente.getSelectedItem().split(" - ")[0];
            String idTratamiento = chTratamiento.getSelectedItem().split(" - ")[0];

            // Hacer el Alta
            if(!modelo.altaIngieren(connection, usuario, idPaciente, idTratamiento, txtCantidad.getText())) 
            {
                mensaje.setText("Error en Alta");
            }
            else
            {
                mensaje.setText("Alta Correcta");
                chPaciente.select(0);
                chTratamiento.select(0);
                txtCantidad.setText("");
                chPaciente.requestFocus();
            }

            feedback.setVisible(true);

            // Desconectar
            modelo.desconectar(connection);
        }
    }
}
