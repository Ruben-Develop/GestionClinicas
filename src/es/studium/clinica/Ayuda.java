package es.studium.clinica;

import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

public class Ayuda
{
    public Ayuda()
    {
        goToURL("index.html");
    }
    
    public void goToURL(String URL)
    {
        if (Desktop.isDesktopSupported())
        {
            Desktop desktop = Desktop.getDesktop();
            if (desktop.isSupported(Desktop.Action.BROWSE))
            {
                try
                {
                    URI uri = new URI(URL);
                    desktop.browse(uri);
                }
                catch (URISyntaxException | IOException ex)
                {
                    System.out.println("Error al abrir la URL: " + ex.getMessage());
                }
            }
        }
    }
}
