package com.mycompany.locadoraveiculos;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.sql.Date;

public class Pagamento {
    private int id;
    private Aluguel aluguel;
    private Cliente cliente;
    private Veiculo veiculo;
    private double valorPago;
    private String formaPagamento;
    private LocalDate dataPagamento;

    // Construtor
    public Pagamento(int id, Aluguel aluguel, String formaPagamento, LocalDate dataPagamento) {
        this.id = id;
        this.aluguel = aluguel;
        this.cliente = aluguel.getCliente();
        this.veiculo = aluguel.getVeiculo();
        this.valorPago = aluguel.calcularTotalAluguel();
        this.formaPagamento = formaPagamento;
        this.dataPagamento = dataPagamento;
    }

    // Getters e Setters
    public int getId() {
        return id;
    }

    public Aluguel getAluguel() {
        return aluguel;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public Veiculo getVeiculo() {
        return veiculo;
    }

    public double getValorPago() {
        return valorPago;
    }

    public String getFormaPagamento() {
        return formaPagamento;
    }

    public LocalDate getDataPagamento() {
        return dataPagamento;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setFormaPagamento(String formaPagamento) {
        this.formaPagamento = formaPagamento;
    }

    public void setDataPagamento(LocalDate dataPagamento) {
        this.dataPagamento = dataPagamento;
    }

    // Inserir pagamento no banco
    public void inserirPagamento() {
        String query = "INSERT INTO pagamentos (id_aluguel, id_cliente, id_veiculo, valor_pago, forma_pagamento, data_pagamento) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection connection = Conexao.getConnection();
             PreparedStatement stmt = connection.prepareStatement(query)) {

            stmt.setInt(1, aluguel.getId());
            stmt.setInt(2, cliente.getIdCliente());
            stmt.setInt(3, veiculo.getId());
            stmt.setDouble(4, valorPago);
            stmt.setString(5, formaPagamento);
            stmt.setDate(6, Date.valueOf(dataPagamento));

            int rows = stmt.executeUpdate();

            if (rows > 0) {
                System.out.println("Pagamento registrado com sucesso!");
            } else {
                System.out.println("Erro ao registrar pagamento.");
            }

        } catch (SQLException e) {
            System.out.println("Erro ao inserir pagamento: " + e.getMessage());
        }
    }

    // Listar pagamentos
    public static void listarPagamentos() {
        String query = "SELECT p.id, c.nome AS cliente, v.modelo AS veiculo, p.valor_pago, p.forma_pagamento, p.data_pagamento " +
                            "FROM pagamentos p " +
                            "JOIN cliente c ON p.id_cliente = c.idCliente " +
                            "JOIN veiculo v ON p.id_veiculo = v.id";

        try (Connection connection = Conexao.getConnection();
             Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            System.out.println("Lista de Pagamentos:");
            System.out.println("---------------------------------------------------------------");

            while (rs.next()) {
                int id = rs.getInt("id");
                String cliente = rs.getString("cliente");
                String veiculo = rs.getString("veiculo");
                double valor = rs.getDouble("valor_pago");
                String forma = rs.getString("forma_pagamento");
                LocalDate data = rs.getDate("data_pagamento").toLocalDate();

                System.out.printf("ID: %d | Cliente: %s | Veículo: %s | Valor: R$%.2f | Forma: %s | Data: %s\n",
                                    id, cliente, veiculo, valor, forma, data);
            }
            System.out.println("---------------------------------------------------------------");

        } catch (SQLException e) {
            System.out.println("Erro ao listar pagamentos: " + e.getMessage());
        }
    }
}