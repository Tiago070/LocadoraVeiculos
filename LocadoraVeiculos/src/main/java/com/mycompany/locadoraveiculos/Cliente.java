package com.mycompany.locadoraveiculos;

import java.sql.*;

public class Cliente {

    private int id;
    private String nome;
    private String cpf;
    private String telefone;
    private String email;

    // Construtores
    public Cliente() {
    }

    public Cliente(int id, String nome, String cpf, String telefone, String email) {
        this.id = id;
        this.nome = nome;
        this.cpf = cpf;
        this.telefone = telefone;
        this.email = email;
    }

    // Getters e Setters
    public int getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public String getCpf() {
        return cpf;
    }

    public String getTelefone() {
        return telefone;
    }

    public String getEmail() {
        return email;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    // ================== MÉTODOS CRUD ===================
    // CADASTRAR CLIENTE
    public static void cadastrarCliente(String nome, String cpf, String telefone, String email) {
        String query = "INSERT INTO clientes (nome, cpf, telefone, email) VALUES (?, ?, ?, ?)";

        try (Connection connection = Conexao.getConnection(); PreparedStatement stmt = connection.prepareStatement(query)) {

            stmt.setString(1, nome);
            stmt.setString(2, cpf);
            stmt.setString(3, telefone);
            stmt.setString(4, email);

            int rows = stmt.executeUpdate();
            if (rows > 0) {
                System.out.println("Cliente cadastrado com sucesso.");
            } else {
                System.out.println("Erro ao cadastrar cliente.");
            }

        } catch (SQLException e) {
            System.out.println("Erro ao cadastrar cliente: " + e.getMessage());
        }
    }

    // LISTAR CLIENTES
    public static void listarClientes() {
        String query = "SELECT * FROM clientes";

        try (Connection connection = Conexao.getConnection(); Statement stmt = connection.createStatement(); ResultSet rs = stmt.executeQuery(query)) {

            System.out.println("\nClientes cadastrados:");
            System.out.println("----------------------------------------------------------");
            System.out.printf("| %-4s | %-20s | %-14s | %-15s | %-30s |\n", "ID", "Nome", "CPF", "Telefone", "Email");
            System.out.println("----------------------------------------------------------");

            while (rs.next()) {
                int id = rs.getInt("id");
                String nome = rs.getString("nome");
                String cpf = rs.getString("cpf");
                String telefone = rs.getString("telefone");
                String email = rs.getString("email");

                System.out.printf("| %-4d | %-20s | %-14s | %-15s | %-30s |\n", id, nome, cpf, telefone, email);
            }
            System.out.println("----------------------------------------------------------");

        } catch (SQLException e) {
            System.out.println("Erro ao listar clientes: " + e.getMessage());
        }
    }

    // EDITAR CLIENTE
    public static void editarCliente(int id, String nome, String cpf, String telefone, String email) {
        String query = "UPDATE clientes SET nome = ?, cpf = ?, telefone = ?, email = ? WHERE id = ?";

        try (Connection connection = Conexao.getConnection(); PreparedStatement stmt = connection.prepareStatement(query)) {

            stmt.setString(1, nome);
            stmt.setString(2, cpf);
            stmt.setString(3, telefone);
            stmt.setString(4, email);
            stmt.setInt(5, id);

            int rows = stmt.executeUpdate();
            if (rows > 0) {
                System.out.println("Cliente atualizado com sucesso.");
            } else {
                System.out.println("Cliente não encontrado.");
            }

        } catch (SQLException e) {
            System.out.println("Erro ao editar cliente: " + e.getMessage());
        }
    }

    // DELETAR CLIENTE
    public static void deletarCliente(int id) {
        String query = "DELETE FROM clientes WHERE id = ?";

        try (Connection connection = Conexao.getConnection(); PreparedStatement stmt = connection.prepareStatement(query)) {

            stmt.setInt(1, id);

            int rows = stmt.executeUpdate();
            if (rows > 0) {
                System.out.println("Cliente deletado com sucesso.");
            } else {
                System.out.println("Cliente não encontrado.");
            }

        } catch (SQLException e) {
            System.out.println("Erro ao deletar cliente: " + e.getMessage());
        }
    }

    // BUSCAR CLIENTE POR ID
    public static Cliente buscarClientePorId(int id) {
        String query = "SELECT * FROM clientes WHERE id = ?";

        try (Connection connection = Conexao.getConnection(); PreparedStatement stmt = connection.prepareStatement(query)) {

            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return new Cliente(
                        rs.getInt("id"),
                        rs.getString("nome"),
                        rs.getString("cpf"),
                        rs.getString("telefone"),
                        rs.getString("email")
                );
            } else {
                System.out.println("Cliente com ID " + id + " não encontrado.");
            }

        } catch (SQLException e) {
            System.out.println("Erro ao buscar cliente: " + e.getMessage());
        }
        return null;
    }

    // BUSCAR CLIENTE POR NOME (Adicionado para o menu principal)
    public static Cliente buscarClientePorNome(String nome) {
        String query = "SELECT * FROM clientes WHERE nome LIKE ?";

        try (Connection connection = Conexao.getConnection(); PreparedStatement stmt = connection.prepareStatement(query)) {

            stmt.setString(1, "%" + nome + "%");
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return new Cliente(
                        rs.getInt("id"),
                        rs.getString("nome"),
                        rs.getString("cpf"),
                        rs.getString("telefone"),
                        rs.getString("email")
                );
            } else {
                System.out.println("Cliente com nome contendo '" + nome + "' não encontrado.");
            }

        } catch (SQLException e) {
            System.out.println("Erro ao buscar cliente por nome: " + e.getMessage());
        }
        return null;
    }
}