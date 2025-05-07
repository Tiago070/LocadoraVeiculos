package com.mycompany.locadoraveiculos;

import java.sql.*;

public class Carro extends Veiculo {

    private int numPortas;
    private String tipoCombustivel;
    private boolean arCondicionado;
    private String cambio;

    public Carro(int id, String modelo, String placa, int ano, double precoDiario,
            int numPortas, String tipoCombustivel, boolean arCondicionado, String cambio) {
        super(id, modelo, placa, ano, precoDiario);
        this.numPortas = numPortas;
        this.tipoCombustivel = tipoCombustivel;
        this.arCondicionado = arCondicionado;
        this.cambio = cambio;
    }

    @Override
    public double calcularSeguro() {
        // Seguro para carro: 20% do preço diário + taxa fixa de R$15
        return getPrecoDiario() * 0.20 + 15;
    }

    // Getters e Setters específicos
    public int getNumPortas() {
        return numPortas;
    }

    public String getTipoCombustivel() {
        return tipoCombustivel;
    }

    public boolean isArCondicionado() {
        return arCondicionado;
    }

    public String getCambio() {
        return cambio;
    }

    public void setNumPortas(int numPortas) {
        this.numPortas = numPortas;
    }

    public void setTipoCombustivel(String tipoCombustivel) {
        this.tipoCombustivel = tipoCombustivel;
    }

    public void setArCondicionado(boolean arCondicionado) {
        this.arCondicionado = arCondicionado;
    }

    public void setCambio(String cambio) {
        this.cambio = cambio;
    }

    public static void cadastrarCarro(String placa, String marca, String modelo, int anoFabricacao,
            double valorDiaria, String cor, int quilometragem,
            int numeroPortas, String tipoCombustivel) {
        // Primeiro cadastra o veículo na tabela pai
        String queryVeiculo = "INSERT INTO veiculos (placa, marca, modelo, ano_fabricacao, "
                + "valor_diaria, cor, quilometragem, tipo) VALUES (?, ?, ?, ?, ?, ?, ?, 'CARRO')";

        // Depois cadastra os dados específicos do carro
        String queryCarro = "INSERT INTO carros (id_veiculo, numero_portas, tipo_combustivel) "
                + "VALUES (LAST_INSERT_ID(), ?, ?)";

        try (Connection connection = Conexao.getConnection()) {
            // Inicia transação
            connection.setAutoCommit(false);

            try (PreparedStatement stmtVeiculo = connection.prepareStatement(queryVeiculo); PreparedStatement stmtCarro = connection.prepareStatement(queryCarro)) {

                // Cadastra o veículo
                stmtVeiculo.setString(1, placa);
                stmtVeiculo.setString(2, marca);
                stmtVeiculo.setString(3, modelo);
                stmtVeiculo.setInt(4, anoFabricacao);
                stmtVeiculo.setDouble(5, valorDiaria);
                stmtVeiculo.setString(6, cor);
                stmtVeiculo.setInt(7, quilometragem);
                stmtVeiculo.executeUpdate();

                // Cadastra os dados específicos do carro
                stmtCarro.setInt(1, numeroPortas);
                stmtCarro.setString(2, tipoCombustivel);
                stmtCarro.executeUpdate();

                // Confirma transação
                connection.commit();
                System.out.println("Carro cadastrado com sucesso.");

            } catch (SQLException e) {
                // Em caso de erro, faz rollback
                connection.rollback();
                System.out.println("Erro ao cadastrar carro: " + e.getMessage());
            }
        } catch (SQLException e) {
            System.out.println("Erro na conexão com o banco: " + e.getMessage());
        }
    }
    public static void listarCarros() {
        String query = "SELECT v.*, c.numero_portas, c.tipo_combustivel " +
                      "FROM veiculos v " +
                      "JOIN carros c ON v.id = c.id_veiculo " +
                      "WHERE v.tipo = 'CARRO'";

        try (Connection connection = Conexao.getConnection();
             Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            System.out.println("\nCarros cadastrados:");
            System.out.println("------------------------------------------------------------");
            System.out.printf("| %-4s | %-10s | %-15s | %-15s | %-4s | %-10s | %-15s |\n", 
                             "ID", "Placa", "Marca", "Modelo", "Ano", "Portas", "Combustível");
            System.out.println("------------------------------------------------------------");

            while (rs.next()) {
                int id = rs.getInt("id");
                String placa = rs.getString("placa");
                String marca = rs.getString("marca");
                String modelo = rs.getString("modelo");
                int ano = rs.getInt("ano_fabricacao");
                int portas = rs.getInt("numero_portas");
                String combustivel = rs.getString("tipo_combustivel");

                System.out.printf("| %-4d | %-10s | %-15s | %-15s | %-4d | %-8d | %-15s |\n",
                                id, placa, marca, modelo, ano, portas, combustivel);
            }
            System.out.println("------------------------------------------------------------");
        } catch (SQLException e) {
            System.out.println("Erro ao listar carros: " + e.getMessage());
        }
    }

}
