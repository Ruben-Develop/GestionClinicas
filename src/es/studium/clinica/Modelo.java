package es.studium.clinica;

import java.awt.Choice;
import java.awt.Desktop;
import java.awt.TextField;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.LocalTime;

import com.itextpdf.io.font.constants.StandardFonts;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.properties.UnitValue;

public class Modelo
{
	String driver = "com.mysql.cj.jdbc.Driver";
	String url = "jdbc:mysql://localhost:3306/clinica";
	String login = "admin";
	String password = "Studium2025#";
	String sentencia = "";

	Connection connection = null;
	Statement statement = null;
	ResultSet rs = null;

	public Connection conectar()
	{
		try
		{
			Class.forName(driver);
			connection = DriverManager.getConnection(url, login, password);
		}
		catch(ClassNotFoundException e)
		{
			return null;
		}
		catch(SQLException e)
		{
			System.out.println(e.getMessage());
			return null;
		}
		return connection;
	}

	public void desconectar(Connection connection)
	{
		if(connection!=null)
		{
			try
			{
				connection.close();
			}
			catch (SQLException e){}
		}
	}

	public int comprobarCredenciales(Connection connection,

			String usuario, String clave)

	{

		int tipo = -1;

		sentencia = "SELECT * FROM usuarios "

				+ "WHERE nombreUsuario = '"

				+ usuario

				+ "' AND claveUsuario = SHA2('"

				+ clave

				+ "', 256);";

		try

		{

			statement = connection.createStatement();

			rs = statement.executeQuery(sentencia);

			if(rs.next()==true)

			{

				tipo = rs.getInt("tipoUsuario");

			}

		}

		catch(SQLException e)

		{

			System.out.println("Error en la sentencia SQL");

		}

		return tipo;

	}

	public static void guardarLog(String usuario, String mensaje) {
		try {
			File file = new File("memoriaLogs.log");
			System.out.println("Ruta absoluta para el log: " + file.getAbsolutePath());

			if (!file.exists()) {
				boolean creado = file.createNewFile();
				System.out.println(creado ? "Archivo creado" : "Archivo NO creado");
			}

			FileWriter fw = new FileWriter(file, true);
			BufferedWriter bw = new BufferedWriter(fw);
			PrintWriter salida = new PrintWriter(bw);

			LocalDate fechaActual = LocalDate.now();
			LocalTime horaActual = LocalTime.now();

			String cadena = "[" + fechaActual + "][" + horaActual + "][" + usuario + "][" + mensaje + "]";
			salida.println(cadena);

			salida.close();
			bw.close();
			fw.close();

			System.out.println("¡Log guardado correctamente!");

		} catch (IOException e) {
			System.err.println("Error al guardar el log:");
			e.printStackTrace();
		}
	}

	public void process(Table table, String line, PdfFont font, boolean isHeader)
	{

		String[] tokens = line.split(" - ");

		for (String token : tokens) {
			table.addCell(new Cell().add(new Paragraph(token.trim()).setFont(font)));
		}
	}


	public boolean altaClinica(Connection conexion, String usuario, String direccion, String telefono, String horario)
	{
		guardarLog(usuario, "Alta de nueva clínica");

		boolean altaCorrecta = false;
		if(!direccion.isBlank()&&!telefono.isBlank()&&!horario.isBlank())
		{

			sentencia = "INSERT INTO clinicas VALUES (null,'"+direccion+"','"+telefono+"','"+horario+"');";
			try
			{
				statement = conexion.createStatement();
				guardarLog (usuario, sentencia); //Antes del execute siempre
				statement.executeUpdate(sentencia);
				altaCorrecta = true;
			}

			catch(SQLException e)
			{
				altaCorrecta = false;
			}
		}

		return altaCorrecta;
	}


	public boolean altaTratamiento(Connection conexion, String usuario, String fechaCaducidad, String tipo, String precio)
	{
		guardarLog(usuario, "Alta de nuevo tratamiento");

		boolean altaCorrecta = false;
		if(!fechaCaducidad.isBlank()&&!tipo.isBlank()&&!precio.isBlank())
		{

			sentencia = "INSERT INTO tratamientos VALUES (null,'"+fechaCaducidad+"','"+tipo+"','"+precio+"');";
			try
			{
				statement = conexion.createStatement();
				guardarLog(usuario,  sentencia);
				statement.executeUpdate(sentencia);
				altaCorrecta = true;
			}

			catch(SQLException e)
			{
				altaCorrecta = false;
			}
		}

		return altaCorrecta;
	}

	public boolean altaPaciente(Connection conexion, String usuario, String nombre, int edad, String medico, String clinica) 
	{
		guardarLog(usuario, "Alta de nuevo paciente");

		boolean altaCorrecta = false;
		if(!nombre.isBlank()&&!medico.isBlank()&&!clinica.isBlank())
		{

			sentencia = "INSERT INTO pacientes VALUES (null,'"+nombre+"','"+edad+"','"+medico+"','"+clinica+"');";
			try 
			{
				statement = conexion.createStatement();
				guardarLog(usuario, sentencia);
				statement.executeUpdate(sentencia);
				altaCorrecta = true;
			}
			catch(SQLException e)
			{
				altaCorrecta = false;
			}
		}
		return altaCorrecta;
	}

	public void choiceAltaPacientes(Connection connection, Choice ch) 
	{
		ch.add("Seleccionar una clínica...");
		try
		{
			statement = connection.createStatement();
			sentencia = "SELECT * FROM clinicas";
			rs = statement.executeQuery(sentencia);
			while (rs.next())
			{
				ch.add(rs.getInt("idClinica") + " - " + rs.getString("direccionClinica"));
			}
		}
		catch (SQLException sqlex)
		{}
	}

	public boolean altaIngieren(Connection conexion, String usuario, String idPaciente, String idTratamiento, String cantidad) 
	{
		guardarLog(usuario, "Alta de nuevo registro de ingesta");

		boolean altaCorrecta = false;
		if(!idPaciente.isBlank() && !idTratamiento.isBlank() && !cantidad.isBlank())
		{
			sentencia = "INSERT INTO ingieren (recetaIngiere, medicoIngiere, idPacienteFK, idTratamientoFK) "
					+ "VALUES ('" + cantidad + "', '" + usuario + "', " + idPaciente + ", " + idTratamiento + ");";
			try 
			{
				statement = conexion.createStatement();
				guardarLog(usuario, sentencia);
				statement.executeUpdate(sentencia);
				altaCorrecta = true;
			}
			catch(SQLException e)
			{
				System.out.println("Error en altaIngieren: " + e.getMessage());
				altaCorrecta = false;
			}
		}
		return altaCorrecta;
	}

	public void rellenarChoicePacientes(Connection connection, Choice chPaciente) 
	{
		chPaciente.removeAll();
		chPaciente.add("Seleccionar un paciente...");
		try
		{
			statement = connection.createStatement();
			sentencia = "SELECT idPaciente, nombrePaciente FROM pacientes";
			rs = statement.executeQuery(sentencia);
			while (rs.next())
			{
				chPaciente.add(rs.getInt("idPaciente") + " - " + rs.getString("nombrePaciente"));
			}
		}
		catch (SQLException sqlex)
		{
			System.out.println("Error al cargar pacientes: " + sqlex.getMessage());
		}
	}

	public void rellenarChoiceTratamientos(Connection connection, Choice chTratamiento) 
	{
		chTratamiento.removeAll();
		chTratamiento.add("Seleccionar un tratamiento...");
		try
		{
			statement = connection.createStatement();
			sentencia = "SELECT idTratamiento, tipoTratamiento FROM tratamientos";
			rs = statement.executeQuery(sentencia);
			while (rs.next())
			{
				chTratamiento.add(rs.getInt("idTratamiento") + " - " + rs.getString("tipoTratamiento"));
			}
		}
		catch (SQLException sqlex)
		{
			System.out.println("Error al cargar tratamientos: " + sqlex.getMessage());
		}
	}

	public boolean bajaClinica(Connection connection,String usuario, String idClinica)
	{
		guardarLog(usuario, "Baja de clínica");

		boolean resultado = false;
		sentencia = "DELETE FROM clinicas WHERE idClinica = " + idClinica + ";";
		try
		{
			statement = connection.createStatement();
			statement.executeUpdate(sentencia);
			resultado = true;
		}
		catch (SQLException sqlex)
		{
			resultado = false;
		}
		return resultado;
	}


	public void rellenarChoiceClinica(Connection connection, Choice ch)
	{
		ch.removeAll();
		ch.add("Seleccionar una clínica...");
		try
		{
			statement = connection.createStatement();
			sentencia = "SELECT * FROM clinicas";
			rs = statement.executeQuery(sentencia);
			while (rs.next())
			{
				ch.add(rs.getInt("idClinica") + " - " + rs.getString("direccionClinica"));
			}
		}
		catch (SQLException sqlex)
		{}
	}

	public boolean bajaTratamiento(Connection connection, String usuario, String idTratamiento)
	{
		guardarLog(usuario, "Baja de tratamiento");
		boolean resultado = false;
		sentencia = "DELETE FROM tratamientos WHERE idTratamiento = " + idTratamiento + ";";
		System.out.println("Ejecutando sentencia: " + sentencia);
		try
		{
			statement = connection.createStatement();
			statement.executeUpdate(sentencia);
			resultado = true;
		}
		catch (SQLException sqlex)
		{
			 sqlex.printStackTrace(); 
			resultado = false;
		}
		return resultado;
	}


	public void rellenarChoiceTratamiento(Connection connection,String usuario, Choice ch)
	{
		ch.removeAll();
		ch.add("Seleccionar un tratamiento...");
		try
		{
			statement = connection.createStatement();
			sentencia = "SELECT * FROM tratamientos";
			rs = statement.executeQuery(sentencia);
			while (rs.next())
			{
				ch.add(rs.getInt("idTratamiento") + " - " + rs.getString("fechaCaducidadTratamiento"));
			}
		}
		catch (SQLException sqlex)
		{}
	}

	public boolean bajaPaciente(Connection conexion, String usuario, String idPaciente)
	{
		guardarLog(usuario, "Baja de un paciente");
		boolean resultado = false;
		try
		{
			statement = conexion.createStatement();
			sentencia = "DELETE FROM pacientes WHERE idPaciente = " + idPaciente + ";";
			System.out.println("Ejecutando: " + sentencia); // Para depurar
			statement.executeUpdate(sentencia);
			resultado = true;
		}
		catch(SQLException e)
		{
			System.out.println("Error al borrar: " + e.getMessage());
			resultado = false;
		}
		return resultado;
	}

	public void rellenarChoicePaciente(Connection connection, Choice ch)
	{
		ch.removeAll();
		ch.add("Seleccionar un paciente...");
		try
		{
			statement = connection.createStatement();
			sentencia = "SELECT * FROM pacientes";
			rs = statement.executeQuery(sentencia);
			while (rs.next())
			{
				ch.add(rs.getInt("idPaciente") + " - " + rs.getString("nombrePaciente"));
			}
		}
		catch (SQLException sqlex)
		{}
	}

	public boolean bajaIngieren(Connection connection, String usuario, String idIngiere) {
		guardarLog(usuario, "Baja de registro de ingesta");

		boolean resultado = false;
		sentencia = "DELETE FROM ingieren WHERE idIngiere = " + idIngiere + ";";
		try {
			statement = connection.createStatement();
			statement.executeUpdate(sentencia);
			resultado = true;
		} catch (SQLException sqlex) {
			System.out.println("Error en bajaIngieren: " + sqlex.getMessage());
			resultado = false;
		}
		return resultado;
	}


	public String consultarClinicas(Connection conexion)
	{
		String contenidoTextarea = "Código - Dirección - Teléfono - Horario\n";
		sentencia = "SELECT * FROM clinicas;";
		try
		{
			statement = conexion.createStatement();
			rs = statement.executeQuery(sentencia);
			while (rs.next())
			{
				contenidoTextarea = contenidoTextarea
						+ rs.getInt("idClinica")
						+ " - "
						+ rs.getString("direccionClinica")
						+ " - "
						+ rs.getString("telefonoClinica")
						+ " - "
						+ rs.getString("horarioClinica")
						+ "\n";
			}
		}
		catch (SQLException sqlex)
		{}
		return contenidoTextarea;
	}


	public String consultarTratamientos(Connection conexion)
	{
		String contenidoTextarea = "Código - Fecha Caducidad Tratamiento - Tipo - Precio\n";
		sentencia = "SELECT * FROM tratamientos;";
		try
		{
			statement = conexion.createStatement();
			rs = statement.executeQuery(sentencia);
			while (rs.next())
			{
				contenidoTextarea = contenidoTextarea
						+ rs.getInt("idTratamiento")
						+ " - "
						+ rs.getString("fechaCaducidadTratamiento")
						+ " - "
						+ rs.getString("tipoTratamiento")
						+ " - "
						+ rs.getString("precioTratamiento")
						+ "\n";
			}
		}
		catch (SQLException sqlex)
		{}
		return contenidoTextarea;
	}


	public String consultarPacientes(Connection conexion) {
		String contenido = "Paciente - Edad - Médico - Clínica (Dirección) - Teléfono Clínica\n";

		String sentencia = "SELECT "
				+ "p.nombrePaciente, "
				+ "p.edadPaciente, "
				+ "p.medicoPaciente, "
				+ "c.direccionClinica, "
				+ "c.telefonoClinica "
				+ "FROM pacientes p "
				+ "JOIN clinicas c ON p.idClinicaFK = c.idClinica "
				+ "ORDER BY p.nombrePaciente;";

		try {
			statement = conexion.createStatement();
			rs = statement.executeQuery(sentencia);
			while (rs.next()) {
				String nombrePaciente = rs.getString("nombrePaciente");
				int edadPaciente = rs.getInt("edadPaciente");
				String medicoPaciente = rs.getString("medicoPaciente");
				String direccionClinica = rs.getString("direccionClinica");
				String telefonoClinica = rs.getString("telefonoClinica");

				contenido += nombrePaciente
						+ " - " + edadPaciente
						+ " - " + medicoPaciente
						+ " - " + direccionClinica
						+ " - " + telefonoClinica
						+ "\n";
			}
			rs.close();
			statement.close();
		} catch (SQLException e) {
			contenido += "Error en la consulta de pacientes: " + e.getMessage();
			System.err.println("Error en la consulta de pacientes: " + e.getMessage()); 
		}

		return contenido;
	}




	public String consultarIngieren(Connection conexion)
	{
		String contenidoTextarea = "Código - Paciente - Tratamiento - Cantidad - Médico\n";
		sentencia = "SELECT i.idIngiere, p.nombrePaciente, t.tipoTratamiento, i.recetaIngiere, p.medicoPaciente " +
	            "FROM ingieren i " +
	            "JOIN pacientes p ON i.idPacienteFK = p.idPaciente " +
	            "JOIN tratamientos t ON i.idTratamientoFK = t.idTratamiento;";
		try
		{
			statement = conexion.createStatement();
			rs = statement.executeQuery(sentencia);
			while (rs.next())
			{
				contenidoTextarea += rs.getInt("idIngiere")
					    + " - "
					    + rs.getString("nombrePaciente")
					    + " - "
					    + rs.getString("tipoTratamiento")
					    + " - "
					    + rs.getString("recetaIngiere")
					    + " - "
					    + rs.getString("medicoPaciente") 
					    + "\n";
			}
		}
		catch (SQLException sqlex)
		{
			System.out.println("Error en consultaIngieren: " + sqlex.getMessage());
		}
		return contenidoTextarea;
	}


	public boolean modificarClinica(Connection connection, String usuario, String sentencia)
	{
		guardarLog(usuario, "Modificación de Clínica");
		System.out.println(sentencia);
		boolean resultado = false;
		try
		{
			statement = connection.createStatement();
			statement.executeUpdate(sentencia);
			resultado = true;
		}
		catch (SQLException sqlex)
		{
			resultado = false;
		}
		return resultado;
	}


	public void editarCamposClinica(Connection connection2, String usuario, String idClinica, TextField direccionClinica, TextField telefonoClinica, TextField horarioClinica)
	{

		try
		{
			statement = connection.createStatement();
			sentencia = "SELECT * FROM clinicas WHERE idClinica = " + idClinica;
			guardarLog(usuario, sentencia);
			rs = statement.executeQuery(sentencia);
			rs.next();
			direccionClinica.setText(rs.getString("direccionClinica"));
			telefonoClinica.setText(rs.getString("telefonoClinica"));
			horarioClinica.setText(rs.getString("horarioClinica"));
		}
		catch (SQLException sqlex)
		{}
	}

	public boolean modificarTratamiento(Connection connection, String usuario, String sentencia) {
		guardarLog(usuario, "Modificación de tratamiento");
		boolean resultado = false;
		try {
			statement = connection.createStatement();
			int filas = statement.executeUpdate(sentencia);
			resultado = filas > 0;  // true si se modificó alguna fila
			statement.close();
		} catch (SQLException sqlex) {
			sqlex.printStackTrace();
			resultado = false;
		}
		return resultado;
	}

	public void editarCamposTratamiento(Connection connection, String usuario, String idTratamiento,
			TextField fechaCaducidadTratamiento, TextField tipoTratamiento, TextField precioTratamiento) {
		try {
			statement = connection.createStatement();
			sentencia = "SELECT * FROM tratamientos WHERE idTratamiento = " + idTratamiento;
			rs = statement.executeQuery(sentencia);
			if (rs.next()) {
				fechaCaducidadTratamiento.setText(rs.getString("fechaCaducidadTratamiento"));
				tipoTratamiento.setText(rs.getString("tipoTratamiento"));
				precioTratamiento.setText(rs.getString("precioTratamiento"));
			} else {
				fechaCaducidadTratamiento.setText("");
				tipoTratamiento.setText("");
				precioTratamiento.setText("");
				System.out.println("No existe tratamiento con id " + idTratamiento);
			}
			rs.close();
			statement.close();
		} catch (SQLException sqlex) {
			sqlex.printStackTrace();
		}
	}

	public boolean modificarPaciente(Connection connection, String usuario, String sentencia)
	{
		guardarLog(usuario, "Modificación de paciente");

		System.out.println(sentencia);
		boolean resultado = false;
		try
		{
			statement = connection.createStatement();
			statement.executeUpdate(sentencia);
			resultado = true;
		}
		catch (SQLException sqlex)
		{
			resultado = false;
		}
		return resultado;
	}

	public void editarCamposPaciente(Connection connection2, String usuario, String idPaciente, TextField nombrePaciente, TextField edadPaciente, TextField medicoPaciente)
	{
		try
		{
			statement = connection2.createStatement(); 
			sentencia = "SELECT * FROM pacientes WHERE idPaciente = " + idPaciente;
			rs = statement.executeQuery(sentencia);
			if (rs.next())
			{
				nombrePaciente.setText(rs.getString("nombrePaciente"));
				edadPaciente.setText(String.valueOf(rs.getInt("edadPaciente")));
				medicoPaciente.setText(rs.getString("medicoPaciente"));
			}
		}
		catch (SQLException sqlex)
		{
			System.out.println("Error al cargar datos del paciente: " + sqlex.getMessage());
		}
	}

	public boolean modificarIngieren(Connection connection, String usuario, String idIngiere,
			String pacienteSeleccionado, String tratamientoSeleccionado,
			String recetaIngiere, String medicoIngiere, String idIngieren) {
		guardarLog(usuario, "Modificación de registro de ingesta id: " + idIngiere);

		String sql = "UPDATE ingieren SET idPacienteFK = ?, idTratamientoFK = ?, recetaIngiere = ?, medicoIngiere = ? WHERE idIngiere = ?";
		boolean resultado = false;
		try (PreparedStatement ps = connection.prepareStatement(sql)) {
			int idPacienteFK = Integer.parseInt(pacienteSeleccionado.split(" - ")[0]);
			int idTratamientoFK = Integer.parseInt(tratamientoSeleccionado.split(" - ")[0]);

			ps.setInt(1, idPacienteFK);
			ps.setInt(2, idTratamientoFK);
			ps.setString(3, recetaIngiere);
			ps.setString(4, medicoIngiere);
			ps.setInt(6, Integer.parseInt(idIngiere));

			int filasAfectadas = ps.executeUpdate();
			resultado = filasAfectadas > 0;
		} catch (SQLException e) {
			System.out.println("Error en modificarIngieren: " + e.getMessage());
			resultado = false;
		}
		return resultado;
	}

	public void editarCamposIngieren(Connection connection, String usuario, String idIngieren,
			Choice chPaciente, Choice chTratamiento, TextField txtCantidad)
		{
			String sql = "SELECT i.idPacienteFK, i.idTratamientoFK, i.cantidad FROM ingieren i WHERE i.idIngiere = ?";
			try (PreparedStatement ps = connection.prepareStatement(sql)) {
				ps.setInt(1, Integer.parseInt(idIngieren));
				try (ResultSet rs = ps.executeQuery()) {
					if (rs.next()) {
						int idPacienteFK = rs.getInt("idPacienteFK");
						int idTratamientoFK = rs.getInt("idTratamientoFK");

						for (int i = 0; i < chPaciente.getItemCount(); i++) {
							if (chPaciente.getItem(i).startsWith(idPacienteFK + " -")) {
								chPaciente.select(i);
								break;
							}
						}

						for (int i = 0; i < chTratamiento.getItemCount(); i++) {
							if (chTratamiento.getItem(i).startsWith(idTratamientoFK + " -")) {
								chTratamiento.select(i);
								break;
							}
						}

						txtCantidad.setText(rs.getString("cantidad"));
					}
				}
			} catch (SQLException e) {
				System.out.println("Error en editarCamposIngieren: " + e.getMessage());
			}
		}

	public void rellenarChoiceIngieren1(Connection connection, Choice ch) {
		ch.removeAll();
		ch.add("Seleccionar un registro...");
		Statement statement = null;
		ResultSet rs = null;
		try {
			statement = connection.createStatement();
			String sentencia = "SELECT idIngiere, recetaIngiere FROM ingieren";
			rs = statement.executeQuery(sentencia);
			while (rs.next()) {
				String registro = rs.getInt("idIngiere") + " - " + rs.getString("recetaIngiere");
				ch.add(registro);
			}
		} catch (SQLException sqlex) {
			System.out.println("Error al cargar registros de ingesta: " + sqlex.getMessage());
		} finally {
			try { if (rs != null) rs.close(); } catch (SQLException e) {}
			try { if (statement != null) statement.close(); } catch (SQLException e) {}
		}
	}


	public void ImprimirClinicas(String dest, String datos, String usuario)
	{
		guardarLog(usuario, "Exportación de datos de clínicas");

		try
		{
			PdfWriter writer = new PdfWriter(dest);
			PdfDocument pdf = new PdfDocument(writer);
			Document document = new Document(pdf, PageSize.A4.rotate());
			document.setMargins(20, 20, 20, 20);
			PdfFont font = PdfFontFactory.createFont(StandardFonts.HELVETICA);
			PdfFont bold = PdfFontFactory.createFont(StandardFonts.HELVETICA_BOLD);


			Table table = new Table(UnitValue.createPercentArray(new float[] { 15, 40, 20, 25 })) 
					.useAllAvailableWidth();

			String[] lineas = datos.split("\n");
			for (int i = 0; i < lineas.length; i++) {
				String linea = lineas[i].trim();
				if (!linea.isEmpty()) {
					process(table, linea, (i == 0 ? bold : font), (i == 0));
				}
			}
			document.add(table);
			document.close();
			Desktop.getDesktop().open(new File(dest));
		}
		catch (IOException ioe) {
			System.err.println("Error al imprimir clínicas a PDF: " + ioe.getMessage());
			ioe.printStackTrace();
		}
	}

	public void ImprimirTratamientos(String dest, String datos, String usuario)
	{
		guardarLog(usuario, "Exportación de datos de tratamientos");

		try
		{
			PdfWriter writer = new PdfWriter(dest);
			PdfDocument pdf = new PdfDocument(writer);
			Document document = new Document(pdf, PageSize.A4.rotate());
			document.setMargins(20, 20, 20, 20);
			PdfFont font = PdfFontFactory.createFont(StandardFonts.HELVETICA);
			PdfFont bold = PdfFontFactory.createFont(StandardFonts.HELVETICA_BOLD);


			Table table = new Table(UnitValue.createPercentArray(new float[] { 15, 25, 30, 30 })) 
					.useAllAvailableWidth();

			String[] lineas = datos.split("\n");
			for (int i = 0; i < lineas.length; i++) {
				String linea = lineas[i].trim();
				if (!linea.isEmpty()) {

					process(table, linea, (i == 0 ? bold : font), (i == 0));
				}
			}
			document.add(table);
			document.close();
			Desktop.getDesktop().open(new File(dest));
		}
		catch (IOException ioe) {
			System.err.println("Error al imprimir tratamientos a PDF: " + ioe.getMessage());
			ioe.printStackTrace();
		}
	}

	public void ImprimirPacientes(String dest, String datos, String usuario)
	{
		guardarLog(usuario, "Exportación de datos de pacientes");

		try
		{
			PdfWriter writer = new PdfWriter(dest);
			PdfDocument pdf = new PdfDocument(writer);
			Document document = new Document(pdf, PageSize.A4.rotate());
			document.setMargins(20, 20, 20, 20);
			PdfFont font = PdfFontFactory.createFont(StandardFonts.HELVETICA);
			PdfFont bold = PdfFontFactory.createFont(StandardFonts.HELVETICA_BOLD);

			Table table = new Table(UnitValue.createPercentArray(new float[] { 25, 10, 25, 30, 10 }))
					.useAllAvailableWidth();

			String[] lineas = datos.split("\n");
			for (int i = 0; i < lineas.length; i++) {
				String linea = lineas[i].trim();

				if (!linea.isEmpty()) {
					process(table, linea, (i == 0 ? bold : font), (i == 0));
				}
			}
			document.add(table);
			document.close();
			Desktop.getDesktop().open(new File(dest));
		} catch (IOException ioe){
			System.err.println("Error al imprimir pacientes a PDF: " + ioe.getMessage());
			ioe.printStackTrace();
		}
	}

	public void ImprimirIngieren(String dest, String datos, String usuario)
	{
		guardarLog(usuario, "Exportación de datos de registros de ingesta");

		try
		{
			PdfWriter writer = new PdfWriter(dest);
			PdfDocument pdf = new PdfDocument(writer);
			Document document = new Document(pdf, PageSize.A4.rotate());
			document.setMargins(20, 20, 20, 20);
			PdfFont font = PdfFontFactory.createFont(StandardFonts.HELVETICA);
			PdfFont bold = PdfFontFactory.createFont(StandardFonts.HELVETICA_BOLD);


			Table table = new Table(UnitValue.createPercentArray(new float[] { 10, 25, 25, 20, 20 })) 
					.useAllAvailableWidth();

			String[] lineas = datos.split("\n");
			for (int i = 0; i < lineas.length; i++) {
				String linea = lineas[i].trim();
				if (!linea.isEmpty()) {

					process(table, linea, (i == 0 ? bold : font), (i == 0));
				}
			}
			document.add(table);
			document.close();
			Desktop.getDesktop().open(new File(dest));
		}
		catch (IOException ioe) {
			System.err.println("Error al imprimir registros de ingesta a PDF: " + ioe.getMessage());
			ioe.printStackTrace();
		}
	}

	public static void ayuda()
	{
		try
		{
			ProcessBuilder pb = new ProcessBuilder("hh.exe","ayuda.chm");
			pb.start();
			System.out.println("Abriendo el archivo CHM...");
		}
		catch (IOException e)
		{
			System.err.println("Error al intentar abrir el archivo CHM: " + e.getMessage());
			e.printStackTrace();
		}
	}

}
