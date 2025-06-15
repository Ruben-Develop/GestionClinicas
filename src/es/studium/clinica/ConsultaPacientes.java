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

public class ConsultaPacientes implements WindowListener, ActionListener
{
    Frame ventana = new Frame("Consulta Pacientes");
    TextArea txtArea = new TextArea();
    
    Modelo modelo = new Modelo();
    Connection connection = null;

    Button btnGenerarPDF = new Button("Generar PDF");
    
    String usuario;
    public ConsultaPacientes(String u)
    {
    	usuario = u;
        ventana.setLayout(new GridBagLayout());
        
        // Añadir componentes
        GridBagConstraints constraints = new GridBagConstraints();
        
        // El área de texto empieza en la columna cero.
        constraints.gridx = 0;
        
        // El área de texto empieza en la fila cero.
        constraints.gridy = 0;
        
        // El área de texto ocupa dos columnas.
        constraints.gridwidth = 2;
        
        // El área de texto ocupa 2 filas.
        constraints.gridheight = 2;
        constraints.fill = GridBagConstraints.BOTH;
        
        // La fila 0 debe estirarse, le ponemos un 1.0
        constraints.weighty = 1.0;

        // Conectarse a la BD y consultar pacientes
        connection = modelo.conectar();
        txtArea.append(modelo.consultarPacientes(connection));
        modelo.desconectar(connection);
        
        ventana.add(txtArea, constraints);
        
        ventana.add(btnGenerarPDF);
        btnGenerarPDF.addActionListener(this);
        
        // Restauramos al valor por defecto para no afectar a los siguientes componentes.
        constraints.weighty = 0.0;

        ventana.setLocationRelativeTo(null);
        ventana.addWindowListener(this);
        ventana.setSize(600, 300);
        ventana.setVisible(true);
        
    }

    public static final String DEST = "consultaPacientes.pdf";
    @Override
    public void actionPerformed(ActionEvent e)
    {
        if (e.getSource().equals(btnGenerarPDF)) 
        {
            Modelo modelo = new Modelo();
            modelo.ImprimirPacientes(DEST, txtArea.getText(), usuario);

        }
    }

    @Override
    public void windowOpened(WindowEvent e){}

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
