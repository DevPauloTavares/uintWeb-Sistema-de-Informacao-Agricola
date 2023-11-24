package QuintwebApp;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.*;
import java.util.Random;

//todo
public class Encomendas {

    static void gestaoEncomendas(){

        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

        while(true) {
            String input = "";
            System.out.print("Encomendas >");

            try {
                input = reader.readLine();
            } catch (IOException e) {
                e.printStackTrace();
            }

            // Limpa os espaços em branco no início e fim da string
            input = input.trim();

            switch(input.toLowerCase()) {
                case "listar":
                    listEnc();
                    break;
                case "inserir":
                    insertEnc();
                    break;
                case "remover":
                    removeEnc();
                    break;
                case "ajuda":
                case "help":
                    orderHelp();
                    break;
                case "voltar":
                case "back":
                case "return":
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

    private static void listEnc(){
        try {
            Statement statement = QWApp.connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM ENCOMENDA;");

            while (resultSet.next()) {
                int EncomendaId = resultSet.getInt("idencomenda");
                java.sql.Date EncomendaDate = resultSet.getDate("dataencomenda");
                int clientID = resultSet.getInt("idcliente");

                System.out.print("IdEncomenda: " + EncomendaId);
                System.out.print(" | DataEncomenda: " + EncomendaDate);
                System.out.print(" | IdCliente: " + clientID + "\n");
            }

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

    private static void insertEnc(){

        PreparedStatement preparedStatement;
        PreparedStatement preparedStatement2;
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        ResultSet resultSet;

        try {
            Statement statement = QWApp.connection.createStatement();
            preparedStatement = QWApp.connection.prepareStatement("INSERT INTO ENCOMENDA (idencomenda, dataencomenda, idcliente) VALUES ( ?, ?, ?)");

            preparedStatement2 = QWApp.connection.prepareStatement("INSERT INTO DETALHEENCOMENDA (idencomenda, idproduto, quantidade) VALUES ( ?, ?, ?)");

            System.out.print("Introduza o Id do CLiente: ");
            int clientId = QWApp.scanInteger(reader);


            boolean Pfound = true;
            int IdP = 0;
            String nP = "**Not Selected**";
            while(Pfound) {
                System.out.print("Qual produto deseja comprar(Batatas/Gansos/FeijaoVerde/Galinhas):");
                String P = reader.readLine();
                P = P.trim();

                switch (P.toLowerCase()) {
                    case "batatas":
                        IdP = 10;
                        nP = "Batatas";
                        System.out.println("Produto selecionado -> Batatas");
                        Pfound = false;
                        break;
                    case "gansos":
                        IdP = 11;
                        nP = "Gansos";
                        System.out.println("Produto selecionado -> Gansos");
                        Pfound = false;
                        break;
                    case "feijaoverde":
                        IdP = 12;
                        nP = "Feijao Verde";
                        System.out.println("Produto selecionado -> Feijao Verde");
                        Pfound = false;
                        break;
                    case "galinhas":
                        IdP = 20;
                        nP = "Galinhas";
                        System.out.println("Produto selecionado -> Galinhas");
                        Pfound = false;
                        break;
                    default:
                        System.out.println("Produto inválido.");
                }
            }

            System.out.print("Qual a Quantidade de " + nP + " que deseja comprar?");
            int Quant = QWApp.scanInteger(reader);

            Random n = new Random();
            java.sql.Date d = new java.sql.Date(new java.util.Date().getTime());
            int idEnc = Math.abs(n.nextInt());
            preparedStatement.setInt(1,idEnc);
            preparedStatement.setDate(2, d);
            preparedStatement.setInt(3, clientId);

            preparedStatement.executeUpdate();

            preparedStatement2.setInt(1,idEnc);
            preparedStatement2.setInt(2, IdP);
            preparedStatement2.setInt(3, Quant);

            preparedStatement2.executeUpdate();

            // Obtém o ID do cliente adicionado
            resultSet = statement.executeQuery("SELECT MAX(idencomenda) FROM ENCOMENDA");
            resultSet.next();
            int id = resultSet.getInt("max");

            System.out.println("Registo da encomenda concluído. ID: " + id);

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

    private static void removeEnc(){

        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

        System.out.print("Introduza o ID da Encomenda a eliminar: ");
        int EncId = QWApp.scanInteger(reader);

        try {
            Statement statement = QWApp.connection.createStatement();
            statement.executeUpdate("DELETE FROM ENCOMENDA WHERE idencomenda = " + EncId);
            statement.executeUpdate("DELETE FROM DETELHEENCOMENDA WHERE idencomenda = " + EncId);

            statement.close();

            System.out.println("Encomenda removida");
        } catch (SQLException e) {
            System.out.println("Erro: SQLException  " + e.getErrorCode() + ": " + e.getMessage());
            System.exit(1);
        }
    }

    private static void orderHelp() {
        System.out.println("Comandos disponíveis:");
        System.out.println("listar - Listar todas as encomendas");
        System.out.println("detalhes - Apresentar detalhes da encomenda");
        System.out.println("ajuda/help - Apresentar os comandos disponíveis");
        System.out.println("voltar/return - Voltar para o menu anterior");
        System.out.println("sair/quit - Terminar o programa");
    }
}
