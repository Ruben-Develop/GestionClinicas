package es.studium.clinica;

import java.awt.Choice;
import java.awt.TextField;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

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


	public boolean altaClinica(Connection conexion, String direccion, String telefono, String horario)
	{

		boolean altaCorrecta = false;
		if(!direccion.isBlank()&&!telefono.isBlank()&&!horario.isBlank())
		{

			sentencia = "INSERT INTO clinicas VALUES (null,'"+direccion+"','"+telefono+"','"+horario+"');";
			try
			{
				statement = conexion.createStatement();
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


	public boolean bajaClinica(Connection connection, String idClinica)
	{
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


	public boolean modificarClinica(String sentencia)
	{
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


	public void editarCamposClinica(Connection connection2, String idClinica, TextField direccionClinica, TextField telefonoClinica, TextField horarioClinica)
	{
		try
		{
			statement = connection.createStatement();
			sentencia = "SELECT * FROM clinicas WHERE idClinica = " + idClinica;
			rs = statement.executeQuery(sentencia);
			rs.next();
			direccionClinica.setText(rs.getString("direccionClinica"));
			telefonoClinica.setText(rs.getString("telefonoClinica"));
			horarioClinica.setText(rs.getString("horarioClinica"));
		}
		catch (SQLException sqlex)
		{}
	}
	
	
	public boolean altaTratamiento(Connection conexion, String fechaCaducidad, String tipo, String precio)
	{

		boolean altaCorrecta = false;
		if(!fechaCaducidad.isBlank()&&!tipo.isBlank()&&!precio.isBlank())
		{

			sentencia = "INSERT INTO tratamientos VALUES (null,'"+fechaCaducidad+"','"+tipo+"','"+precio+"');";
			try
			{
				statement = conexion.createStatement();
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


	public boolean bajaTratamiento(Connection connection, String idTratamiento)
	{
		boolean resultado = false;
		sentencia = "DELETE FROM tratamientos WHERE idTratamiento = " + idTratamiento + ";";
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


	public void rellenarChoiceTratamiento(Connection connection, Choice ch)
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


	public String consultarTratamientos(Connection conexion)
	{
		String contenidoTextarea = "Código - FechaCaducidadTratamiento - Tipo - Precio\n";
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


	public boolean modificarTratamiento(String sentencia)
	{
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


	public void editarCamposTratamiento(Connection connection2, String idTratamiento, TextField fechaCaducidadTratamiento, TextField tipoTratamiento, TextField precioTratamiento)
	{
		try
		{
			statement = connection.createStatement();
			sentencia = "SELECT * FROM tratamientos WHERE idTratamiento = " + idTratamiento;
			rs = statement.executeQuery(sentencia);
			rs.next();
			fechaCaducidadTratamiento.setText(rs.getString("fechaCaducidadTratamiento"));
			tipoTratamiento.setText(rs.getString("tipoTratamiento"));
			precioTratamiento.setText(rs.getString("precioTratamiento"));
		}
		catch (SQLException sqlex)
		{}
	}
}
