package QuintwebApp;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Locale;
import java.util.Random;

public class Produtos {

    public static void gestaoProdutos(){

        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

        while(true) {
            String input = "";
            System.out.print("produtos> ");

            try {
                input = reader.readLine();
            } catch (IOException e) {
                e.printStackTrace();
            }

            // Limpa os espaços em branco no início e fim da string
            input = input.trim();

            switch(input.toLowerCase()) {
                case "listar":
                    listProducts();
                    break;
                case "inserir":
                    insertProduct();
                    break;
                case "eliminar":
                    removeProduct();
                    break;
                case "ajuda":
                case "help":
                    productHelp();
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

    private static void removeProduct() {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

        System.out.print("Introduza o ID do produto a eliminar: ");
        int productId = QWApp.scanInteger(reader);

        try {
            Statement statement = QWApp.connection.createStatement();
            statement.executeUpdate("DELETE FROM PRODUTO WHERE IdProduto = " + productId);
            statement.executeUpdate("DELETE FROM DETALHEENCOMENDA WHERE IdProduto = " + productId);

            statement.close();

            System.out.println("Produto removido");
        } catch (SQLException e) {
            System.out.println("Erro: SQLException " + e.getErrorCode() + ": " + e.getMessage());
            System.exit(1);
        }
    }

    private static void insertProduct() {
        PreparedStatement preparedStatement;
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        ResultSet resultSet;

        try {
            Statement statement = QWApp.connection.createStatement();
            preparedStatement = QWApp.connection.prepareStatement("INSERT INTO PRODUTO (idproduto, custounidade, designaproduto, quantidadedisponivel, tipoproduto) VALUES (?, ?, ?, ?, ?)");

            System.out.println("Introduza os parâmetros do produto: ");

            System.out.print("Nome do produto: ");
            String productName = reader.readLine();

            System.out.print("Custo/uni: ");
            int productPrice = QWApp.scanInteger(reader);

            System.out.print("Quantidade Disponivel: ");
            int productHowMuch = QWApp.scanInteger(reader);

            String productType = "";

            while(true){
                System.out.print("Tipo de Produto (Agricola/Pecuario): ");
                productType = reader.readLine();
                productType = productType.toLowerCase();

                if(productType.equals("agricola") || productType.equals("pecuario")){
                    break;
                }
                else System.out.println("Tipo de Produto invalido");
            }

            Random n = new Random();
            int idEnc = Math.abs(n.nextInt());
            preparedStatement.setInt(1,idEnc);
            preparedStatement.setInt(2, productPrice);
            preparedStatement.setString(3, productName);
            preparedStatement.setInt(4, productHowMuch);
            preparedStatement.setString(5, productType);

            preparedStatement.executeUpdate();

            // Obtém o ID do produto adicionado
            resultSet = statement.executeQuery("SELECT MAX(IdProduto) FROM PRODUTO");
            resultSet.next();

            System.out.println("Registo do produto concluído. ID: " + resultSet.getInt("max"));

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

    private static void listProducts() {
        try {
            Statement statement = QWApp.connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM Produto;");

            while (resultSet.next()) {
                int productId = resultSet.getInt("idproduto");
                int productPrice = resultSet.getInt("custounidade");
                String productName = resultSet.getString("designaproduto");
                int productHowMuch = resultSet.getInt("quantidadedisponivel");
                String producttype = resultSet.getString("tipoproduto");

                System.out.print("IdProduto: " + productId);
                System.out.print(" | Custo/uni: " + productPrice);
                System.out.print(" | NomeProduto: " + productName);
                System.out.print(" | Quantidade Disponivel: " + productHowMuch);
                System.out.println(" | Tipo de Produto: " + producttype);
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

    private static void productHelp() {
        System.out.println("Comandos disponíveis:");
        System.out.println("listar - Listar todos os produtos registados");
        System.out.println("registar - Registar novo produto");
        System.out.println("eliminar - Eliminar produto");
        System.out.println("ajuda/help - Apresentar os comandos disponíveis");
        System.out.println("voltar/return - Voltar para o menu principal");
        System.out.println("sair/quit - Terminar o programa");
    }
}
