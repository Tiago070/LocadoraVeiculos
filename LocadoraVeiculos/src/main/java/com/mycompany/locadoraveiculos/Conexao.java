package com.mycompany.locadoraveiculos;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Classe responsável por gerenciar a conexão com o banco de dados. Fornece o
 * método para obter uma conexão com o banco.
 *
 * @author Samuel
 */
public class Conexao {

    // Parâmetros da conexão com o banco de dados
    private static final String URL = "jdbc:mysql://localhost:3306/concessionaria";
    private static final String USER = "root";
    private static final String PASSWORD = "123456789";

    /**
     * Obtém uma conexão com o banco de dados.
     *
     * @return Um objeto Connection para a conexão com o banco de dados.
     * @throws SQLException Se ocorrer um erro ao tentar conectar ao banco de
     * dados.
     */
    public static Connection getConnection() throws SQLException {
        Connection conn = null;
        try {
            // Verifica se o driver JDBC está carregado
            Class.forName("com.mysql.cj.jdbc.Driver");

            // Estabelece a conexão com o banco de dados
            conn = DriverManager.getConnection(URL, USER, PASSWORD);

            // Retorna a conexão
            return conn;

        } catch (ClassNotFoundException e) {
            throw new SQLException("Driver JDBC do MySQL não encontrado.", e);
        } catch (SQLException e) {
            throw new SQLException("Erro ao tentar conectar ao banco de dados.", e);
        }
    }
}
