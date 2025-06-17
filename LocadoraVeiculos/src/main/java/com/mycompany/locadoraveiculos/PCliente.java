// Pacote da sua aplicação
package com.mycompany.locadoraveiculos;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;


public class PCliente {

    // INCLUIR Cliente
    public String incluirCliente(Cliente cliente) {
        String resultado;
        try (Connection conn = Conexao.getConnection()) {
            String sql = "INSERT INTO cliente (nome, cpf, telefone, email) VALUES (?, ?, ?, ?)";
            try (PreparedStatement pstm = conn.prepareStatement(sql)) {
                pstm.setString(1, cliente.getNome());
                pstm.setString(2, cliente.getCpf());
                pstm.setString(3, cliente.getTelefone());
                pstm.setString(4, cliente.getEmail());
                pstm.executeUpdate();
            }
            resultado = "Inclusão efetuada com sucesso!";
        } catch (SQLException e) {
            resultado = "Erro na inclusão: " + e.getMessage();
        }
        return resultado;
    }

    // ALTERAR Cliente
    public String alterarCliente(Cliente cliente) {
        String resultado;
        try (Connection conn = Conexao.getConnection()) {
            String sql = "UPDATE cliente SET nome = ?, cpf = ?, telefone = ?, email = ? WHERE idCliente = ?";
            try (PreparedStatement pstm = conn.prepareStatement(sql)) {
                pstm.setString(1, cliente.getNome());
                pstm.setString(2, cliente.getCpf());
                pstm.setString(3, cliente.getTelefone());
                pstm.setString(4, cliente.getEmail());
                pstm.setInt(5, cliente.getIdCliente());
                pstm.executeUpdate();
            }
            resultado = "Alteração efetuada com sucesso!";
        } catch (SQLException e) {
            resultado = "Erro na alteração: " + e.getMessage();
        }
        return resultado;
    }

    // EXCLUIR Cliente
    public String excluirCliente(int idCliente) {
        String resultado;
        try (Connection conn = Conexao.getConnection()) {
            String sql = "DELETE FROM cliente WHERE idCliente = ?";
            try (PreparedStatement pstm = conn.prepareStatement(sql)) {
                pstm.setInt(1, idCliente);
                pstm.executeUpdate();
            }
            resultado = "Exclusão efetuada com sucesso!";
        } catch (SQLException e) {
            resultado = "Erro na exclusão: " + e.getMessage();
        }
        return resultado;
    }

    // CONSULTAR todos os Clientes
    public ArrayList<Cliente> consultarClientes() {
        ArrayList<Cliente> listaClientes = new ArrayList<>();
        try (Connection conn = Conexao.getConnection()) {
            String sql = "SELECT * FROM cliente ORDER BY nome";
            try (Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {

                while (rs.next()) {
                    Cliente cliente = new Cliente(
                            rs.getInt("idCliente"),
                            rs.getString("nome"),
                            rs.getString("cpf"),
                            rs.getString("telefone"),
                            rs.getString("email")
                    );
                    listaClientes.add(cliente);
                }
            }
        } catch (SQLException e) {
            System.out.println("Erro ao consultar clientes: " + e.getMessage());

        }
        return listaClientes;
    }
}