package com.mycompany.locadoraveiculos;

import java.sql.*;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class Aluguel {

    private int id;
    private Cliente cliente;
    private Veiculo veiculo;
    private LocalDate dataInicio;
    private LocalDate dataFim;

    public Aluguel(int id, Cliente cliente, Veiculo veiculo, LocalDate dataInicio, LocalDate dataFim) {
        this.id = id;
        this.cliente = cliente;
        this.veiculo = veiculo;
        this.dataInicio = dataInicio;
        this.dataFim = dataFim;
    }

    public double calcularTotalAluguel() {
        long dias = ChronoUnit.DAYS.between(dataInicio, dataFim);
        return dias * veiculo.getPrecoDiario();
    }

    @Override
    public String toString() {
        return "Aluguel ID: " + id + " | Cliente: " + cliente.getNome() + " | Veículo: " + veiculo.getModelo()
                + " | Início: " + dataInicio + " | Fim: " + dataFim + " | Total: R$" + calcularTotalAluguel();
    }

    // Métodos de controle com banco de dados
    public static void inserirAluguel(Cliente cliente, Veiculo veiculo, LocalDate dataInicio, LocalDate dataFim) {
        String query = "INSERT INTO alugueis (id_cliente, id_veiculo, data_inicio, data_fim) VALUES (?, ?, ?, ?)";

        try (Connection connection = Conexao.getConnection(); PreparedStatement stmt = connection.prepareStatement(query)) {

            stmt.setInt(1, cliente.getId()); // ID do cliente
            stmt.setInt(2, veiculo.getId()); // ID do veículo
            stmt.setDate(3, Date.valueOf(dataInicio));
            stmt.setDate(4, Date.valueOf(dataFim));

            stmt.executeUpdate();
            System.out.println("Aluguel inserido com sucesso!");

        } catch (SQLException e) {
            System.out.println("Erro ao inserir aluguel: " + e.getMessage());
        }
    }

    public static void editarAluguel(int id, Cliente cliente, Veiculo veiculo, LocalDate dataInicio, LocalDate dataFim) {
        String query = "UPDATE alugueis SET id_cliente = ?, id_veiculo = ?, data_inicio = ?, data_fim = ? WHERE id = ?";

        try (Connection connection = Conexao.getConnection(); PreparedStatement stmt = connection.prepareStatement(query)) {

            stmt.setInt(1, cliente.getId()); // ID do cliente
            stmt.setInt(2, veiculo.getId()); // ID do veículo
            stmt.setDate(3, Date.valueOf(dataInicio));
            stmt.setDate(4, Date.valueOf(dataFim));
            stmt.setInt(5, id);

            int rowsUpdated = stmt.executeUpdate();

            if (rowsUpdated > 0) {
                System.out.println("Aluguel atualizado com sucesso!");
            } else {
                System.out.println("Aluguel não encontrado.");
            }

        } catch (SQLException e) {
            System.out.println("Erro ao editar aluguel: " + e.getMessage());
        }
    }

    public static void devolucaoAluguel(int id) {
        String query = "UPDATE alugueis SET data_fim = ? WHERE id = ?";

        try (Connection connection = Conexao.getConnection(); PreparedStatement stmt = connection.prepareStatement(query)) {

            stmt.setDate(1, Date.valueOf(LocalDate.now())); // Data de devolução
            stmt.setInt(2, id);

            int rowsUpdated = stmt.executeUpdate();

            if (rowsUpdated > 0) {
                System.out.println("Veículo devolvido com sucesso! Total a pagar: R$" + calcularTotalAluguel(id));
            } else {
                System.out.println("Aluguel não encontrado.");
            }

        } catch (SQLException e) {
            System.out.println("Erro ao realizar devolução: " + e.getMessage());
        }
    }

    public static void listarAlugueis() {
        String query = "SELECT a.id, a.data_inicio, a.data_fim, c.nome AS cliente, v.modelo AS veiculo "
                + "FROM alugueis a "
                + "JOIN clientes c ON a.id_cliente = c.id "
                + "JOIN veiculos v ON a.id_veiculo = v.id";

        try (Connection connection = Conexao.getConnection(); Statement stmt = connection.createStatement(); ResultSet rs = stmt.executeQuery(query)) {

            System.out.println("Lista de Aluguéis:");
            while (rs.next()) {
                int id = rs.getInt("id");
                String cliente = rs.getString("cliente");
                String veiculo = rs.getString("veiculo");
                LocalDate dataInicio = rs.getDate("data_inicio").toLocalDate();
                LocalDate dataFim = rs.getDate("data_fim") != null ? rs.getDate("data_fim").toLocalDate() : null;

                System.out.printf("ID: %d | Cliente: %s | Veículo: %s | Início: %s | Fim: %s\n",
                        id, cliente, veiculo, dataInicio, dataFim != null ? dataFim : "Em andamento");
            }

        } catch (SQLException e) {
            System.out.println("Erro ao listar aluguéis: " + e.getMessage());
        }
    }

    // Método auxiliar para calcular o total de aluguel com base no ID
    private static double calcularTotalAluguel(int id) {
        String query = "SELECT a.data_inicio, a.data_fim, v.valor_diaria FROM alugueis a "
                + "JOIN veiculos v ON a.id_veiculo = v.id WHERE a.id = ?";
        try (Connection connection = Conexao.getConnection(); PreparedStatement stmt = connection.prepareStatement(query)) {

            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                LocalDate dataInicio = rs.getDate("data_inicio").toLocalDate();
                LocalDate dataFim = rs.getDate("data_fim") != null ? rs.getDate("data_fim").toLocalDate() : LocalDate.now();
                double precoDiario = rs.getDouble("valor_diaria");
                long dias = ChronoUnit.DAYS.between(dataInicio, dataFim);
                return dias * precoDiario;
            }

        } catch (SQLException e) {
            System.out.println("Erro ao calcular total do aluguel: " + e.getMessage());
        }
        return 0.0;
    }

    public int getId() {
        return id;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public Veiculo getVeiculo() {
        return veiculo;
    }

}