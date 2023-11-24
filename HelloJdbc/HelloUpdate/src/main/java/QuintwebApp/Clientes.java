package QuintwebApp;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.*;
import java.util.Random;

public class Clientes {

    static void gestaoClientes(){

        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

        while(true) {
            String input = "";
            System.out.print("clientes> ");

            try {
                input = reader.readLine();
            } catch (IOException e) {
                e.printStackTrace();
            }

            // Limpa os espaços em branco no início e fim da string
            input = input.trim();

            switch(input.toLowerCase()) {
                case "alterar":
                    changeClients();
                    break;
                case "listar":
                    listClients();
                    break;
                case "ajuda":
                case "help":
                    clientHelp();
                    break;
                case "inserir":
                    insertClient();
                    break;
                case "eliminar":
                    delCliente();
                    break;
                case "voltar":
                case "return":
                case "back":
                    return;
                case "sair":
                case "quit":
                    QWApp.closeProgram();
                    break;
                default:
                    System.out.println("Comando inválido. Execute o comando \"ajuda\" ou \"help\" para apresentar os comandos disponíveis");
                    break;
            }

        }

    }

    private static void changeClients(){
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        ResultSet resultSet;

        try{
            Statement statement = QWApp.connection.createStatement();

            System.out.print("idcliente\nnifcliente\nnomecliente\nmorada\npais");
            System.out.println();
            System.out.print("Qual o campo que deseja alterar?:");
            String val = reader.readLine();
            val.toLowerCase();

            System.out.print("Qual o novo valor?:");
            String newVal = reader.readLine();
            newVal.toLowerCase();

            System.out.print("Qual o id do Cliente a alterar?:");
            int id = QWApp.scanInteger(reader);

            statement.execute("UPDATE CLIENTE SET " + val + " = " + newVal + "WHERE idcliente = " + id);

            statement.close();

            System.out.println("Update Concluido");
        }catch (SQLException se) {
            System.out.println("Erro: SQLException " + se.getErrorCode() + ": " + se.getMessage());
            System.exit(1);
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }

    }

    private static void insertClient(){

        PreparedStatement preparedStatement;
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        ResultSet resultSet;

        try {
            Statement statement = QWApp.connection.createStatement();
            preparedStatement = QWApp.connection.prepareStatement("INSERT INTO CLIENTE (idcliente, nifcliente, nomecliente, morada, pais) VALUES (?, ?, ?, ?, ?)");

            System.out.println("Introduza os parâmetros do cliente: ");

            System.out.print("Nome: ");
            String clientName = reader.readLine();

            System.out.print("Morada: ");
            String clientMorada = reader.readLine();

            System.out.print("NIF: ");
            int clientNif = QWApp.scanInteger(reader);

            System.out.print("Pais: ");
            String clientPais = reader.readLine();

            Random n = new Random();
            preparedStatement.setInt(1,Math.abs(n.nextInt()));
            preparedStatement.setInt(2, clientNif);
            preparedStatement.setString(3, clientName);
            preparedStatement.setString(4, clientMorada);
            preparedStatement.setString(5, clientPais);

            preparedStatement.executeUpdate();

            // Obtém o ID do cliente adicionado
            resultSet = statement.executeQuery("SELECT MAX(IdCliente) FROM CLIENTE");
            resultSet.next();
            int clientId = resultSet.getInt("max");

            System.out.println("Registo do cliente concluído. ID: " + clientId);

            resultSet.close();
            statement.close();

        } catch (SQLException se) {
            System.out.println("Erro: SQLException " + se.getErrorCode() + ": " + se.getMessage());
            System.exit(1);
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }

    }

    private static void delCliente(){
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

        System.out.print("Introduza o ID do Cliente a eliminar: ");
        int carrierId = QWApp.scanInteger(reader);

        try {
            Statement statement = QWApp.connection.createStatement();
            statement.executeUpdate("DELETE FROM CLIENTE WHERE idcliente = " + carrierId);
            statement.executeUpdate("DELETE FROM ENCOMENDA WHERE idcliente = " + carrierId);

            statement.close();

            System.out.println("Cliente removido");
        } catch (SQLException e) {
            System.out.println("Erro: SQLException  " + e.getErrorCode() + ": " + e.getMessage());
            System.exit(1);
        }

    }

    private static void listClients() {
        try {
            Statement statement = QWApp.connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM CLIENTE;");

            while (resultSet.next()) {
                int clientId = resultSet.getInt("IdCliente");
                int clientNif = resultSet.getInt("nifcliente");
                String clientName = resultSet.getString("nomecliente");
                String clientMorada= resultSet.getString("Morada");
                String clientPais = resultSet.getString("Pais");


                System.out.print("IdCliente: " + clientId);
                System.out.print(" | Nif: " + clientNif);
                System.out.print(" | Nome: " + clientName);
                System.out.print(" | Morada: " + clientMorada);
                System.out.print(" | Pais: " + clientPais + "\n");
            }

            resultSet.close();
            statement.close();

        } catch (SQLException se) {
            System.out.println("Erro: SQLException  " + se.getErrorCode() + ": " + se.getMessage());
            System.exit(1);

        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    private static void clientHelp() {
        System.out.println("Comandos disponíveis:");
        System.out.println("listar - Listar todos os clientes registados");
        System.out.println("inserir - inserir novo cliente");
        System.out.println("alterar - alterar variaveis de clientes");
        System.out.println("eliminar - inserir novo cliente");
        System.out.println("ajuda/help - Apresentar os comandos disponíveis");
        System.out.println("voltar/return - Voltar para o menu principal");
        System.out.println("sair/quit - Terminar o programa");
    }
}
