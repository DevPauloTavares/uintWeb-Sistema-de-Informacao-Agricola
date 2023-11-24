package QuintwebApp;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class QWApp {

	private static final String currentDBMS = "PostgreSQL"; // PostgreSQL ou MySQL ou SQLServer
	private static final String currentDatabase = "QUINTWEB";

	// PosrgreSQL JDBC driver name and database URL
	private static final String POSTGRESQL_CONNECTION_URL = "jdbc:postgresql://iserver.local:5432/postgres?currentSchema="
			+ currentDatabase;
	private static final String POSTGRESQL_USER = "postgres";
	private static final String POSTGRESQL_PASSWORD = "is";

	static Connection connection = null;

	public static void main(String[] args) {
		// Initialize current JDBC driver name, database URL, user and password
		System.out.println("Current DBMS: " + currentDBMS);

		String urlForConnection = POSTGRESQL_CONNECTION_URL;
		String USER = POSTGRESQL_USER;
		String PASSWORD = POSTGRESQL_PASSWORD;

		try {
			Properties properties = new Properties();
			properties.put("user", USER);
			properties.put("password", PASSWORD);

			System.out.println("URL = " + urlForConnection + "  User = " + USER + "  Password = " + PASSWORD);
			connection = DriverManager.getConnection(urlForConnection, properties); // from JDBC version 2.0
		} catch (Exception e) {
			e.printStackTrace();
		}

		while (true) {

			BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

			System.out.print("Escolha a açao a realizar: ");
			String toDoAction = "";

			try {
				toDoAction = reader.readLine().toLowerCase();
			} catch (IOException e) {
				e.printStackTrace();
			}

			toDoAction = toDoAction.trim();

			switch (toDoAction) {
				case "gestao":
					Clientes.gestaoClientes();
					break;
				case "produtos":
					Produtos.gestaoProdutos();
					break;
				case "encomendas":
					Encomendas.gestaoEncomendas();
					break;
				case "pagamento":
					Pagamento.gestaoPagamento();
					break;
				case "ajuda":
					SpecialQ.listar();
					break;
				case "help":
					help();
					break;
				case "sair":
				case "quit":
					closeProgram();
					break;
				default:
					System.out.println("Comando inválido. Execute o comando \"ajuda\" ou \"help\" para apresentar os comandos disponíveis");
					break;

			}
		}
	}


	static void closeProgram() {
        try {
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        System.exit(0);
    }

	private static void help() {
		System.out.println("Comandos disponíveis:");
		System.out.println("gestao - inserir, alterar, eliminar e listar os registos de clientes");
		System.out.println("produtos -  adicionar, remover ou listar produtos");
		System.out.println("encomendas -  adicionar, remover ou listar encomendas");
		System.out.println("pagamento -  gestão do pagamento das encomendas");
		System.out.println("listar -  obtenção de todas as encomendas realizadas por um cliente num dado período (entre duas datas) ou associadas a um dos tipos de clientes");
		System.out.println("ajuda/help - Apresentar os comandos disponíveis");
		System.out.println("sair/quit - Terminar o programa");
	}


	static int scanInteger(BufferedReader reader) {
		int result;
		String input;

		// Obtém o valor introduzido e converte num inteiro
		try {
			input = reader.readLine();
			result = Integer.parseInt(input);
		}
		catch (NumberFormatException | IOException e) {
			result = -1;
		}

		// Pede ao utilizador enquanto o valor for inválido ou negativo
		while(result < 0) {
			System.out.print("Erro: Valor introduzido inválido. Introduza um valor inteiro positivo: ");

			try {
				input = reader.readLine();
				result = Integer.parseInt(input);
			}
			catch (NumberFormatException | IOException e) {
				result = -1;
			}
		}

		return result;
	}

}