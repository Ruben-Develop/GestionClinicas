package es.studium.clinica;

import java.awt.Button;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.TextArea;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.sql.Connection;

public class ConsultaIngieren implements WindowListener, ActionListener
{
    Frame ventana = new Frame("Consulta Ingieren");
    TextArea txtArea = new TextArea();
    Modelo modelo = new Modelo();
    Connection connection = null;
    
    Button btnGenerarPDF = new Button("Generar PDF");
    String usuario;
    public ConsultaIngieren(String usuario)
    {
        ventana.setLayout(new GridBagLayout());

        // Configurar componentes
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.gridwidth = 2;
        constraints.gridheight = 2;
        constraints.fill = GridBagConstraints.BOTH;
        constraints.weighty = 1.0;

        // Conectarse a la BD y consultar registros de ingesta
        connection = modelo.conectar();
        txtArea.append(modelo.consultarIngieren(connection));
        modelo.desconectar(connection);

        ventana.add(txtArea, constraints);
        
        ventana.add(btnGenerarPDF);
        btnGenerarPDF.addActionListener(this);

        // Restaurar valores por defecto
        constraints.weighty = 0.0;

        ventana.setLocationRelativeTo(null);
        ventana.addWindowListener(this);
        ventana.setSize(600,250);
        ventana.setVisible(true);
    }
    public static final String DEST = "consultaIngieren.pdf";
    @Override
    public void actionPerformed(ActionEvent e) {
    	if (e.getSource().equals(btnGenerarPDF)) 
        {
            Modelo modelo = new Modelo();
            modelo.ImprimirPacientes(DEST, txtArea.getText(), usuario);

        }
    }

    @Override
    public void windowOpened(WindowEvent e) {}

    @Override
    public void windowClosing(WindowEvent e)
    {
        ventana.setVisible(false);
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