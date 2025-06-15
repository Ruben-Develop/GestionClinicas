package es.studium.clinica;

import java.awt.*;
import java.awt.event.*;
import java.sql.Connection;

public class AltaPacientes implements WindowListener, ActionListener
{
    Frame ventana = new Frame("Alta Paciente");

    Label lblNombre = new Label("Nombre:");
    TextField txtNombre = new TextField(20);

    Label lblEdad = new Label("Edad:");
    TextField txtEdad = new TextField(5);

    Label lblMedico = new Label("Médico:");
    TextField txtMedico = new TextField(20);

    Label lblClinica = new Label("Clínica:");
    Choice chClinica = new Choice();

    Button btnAceptar = new Button("Aceptar");
    Button btnLimpiar = new Button("Limpiar");

    Dialog feedback = new Dialog(ventana, "Mensaje", true);
    Label mensaje = new Label("");

    String usuario;

    public AltaPacientes(String u)
    {
        usuario = u;
        ventana.setLayout(new GridLayout(5,2, 5, 5));
        ventana.setSize(300,200);
        ventana.setResizable(false);
        ventana.setLocationRelativeTo(null);
        ventana.setBackground(Color.PINK);

        ventana.add(lblNombre);
        ventana.add(txtNombre);
        ventana.add(lblEdad);
        ventana.add(txtEdad);
        ventana.add(lblMedico);
        ventana.add(txtMedico);
        ventana.add(lblClinica);
        ventana.add(chClinica);

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

        // Cargar clínicas disponibles
        Modelo modelo = new Modelo();
        Connection connection = modelo.conectar();
        chClinica.add("Seleccione una clínica"); // <- valor inicial de control
        modelo.choiceAltaPacientes(connection, chClinica);
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
            txtNombre.setText("");
            txtEdad.setText("");
            txtMedico.setText("");
            chClinica.select(0);
            txtNombre.requestFocus();
        }
        else if(actionEvent.getSource().equals(btnAceptar))
        {
            if(txtNombre.getText().isBlank() || txtEdad.getText().isBlank() || txtMedico.getText().isBlank() || chClinica.getSelectedIndex() == 0) 
            {
                mensaje.setText("Debe rellenar todos los campos");
                feedback.setVisible(true);
                return;
            }

            int edad;
            try {
                edad = Integer.parseInt(txtEdad.getText());
            }
            catch(NumberFormatException e) {
                mensaje.setText("Edad debe ser un número válido");
                feedback.setVisible(true);
                return;
            }

            // Obtener ID de clínica desde el Choice
            String idClinica = chClinica.getSelectedItem().split(" - ")[0];

            // Conectarse a la BD
            Modelo modelo = new Modelo();
            Connection connection = modelo.conectar();

            // Hacer el Alta
            boolean exito = modelo.altaPaciente(connection, usuario, txtNombre.getText(), edad, txtMedico.getText(), idClinica);
            if(!exito) 
            {
                mensaje.setText("Error en Alta");
            }
            else
            {
                mensaje.setText("Alta Correcta");
                txtNombre.setText("");
                txtEdad.setText("");
                txtMedico.setText("");
                chClinica.select(0);
                txtNombre.requestFocus();
            }
            feedback.setVisible(true);

            // Desconectar
            modelo.desconectar(connection);
        }
    }
}