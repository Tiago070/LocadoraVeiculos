package com.mycompany.locadoraveiculos;

import java.sql.*;
import java.util.ArrayList;

public class Carro extends Veiculo {
    private int numPortas;
    private String tipoCombustivel;
    private boolean arCondicionado;
    private String cambio;

    public Carro(int id, String placa, String marca, String modelo, int anoFabricacao,
                 double precoDiario, String cor, int quilometragem, int numPortas,
                 String tipoCombustivel, boolean arCondicionado, String cambio) {
        super(id, modelo, placa,marca, anoFabricacao,quilometragem, precoDiario);
        this.setMarca(marca);
        this.setCor(cor);
        this.setQuilometragem(quilometragem);
        this.setPrecoDiario(precoDiario);
        this.numPortas = numPortas;
        this.tipoCombustivel = tipoCombustivel;
        this.arCondicionado = arCondicionado;
        this.cambio = cambio;
    }

    public static void cadastrarCarro(String placa, String marca, String modelo, int anoFabricacao,
                                      double valorDiaria, String cor, int quilometragem,
                                      int numPortas, String tipoCombustivel, boolean arCondicionado,
                                      String cambio) {
        try (Connection conn = Conexao.getConnection()) {
            String sqlVeiculo = "INSERT INTO veiculo (placa, marca, modelo, ano, precoDiario, cor, quilometragem) " +
                                "VALUES (?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement psVeiculo = conn.prepareStatement(sqlVeiculo, Statement.RETURN_GENERATED_KEYS);
            psVeiculo.setString(1, placa);
            psVeiculo.setString(2, marca);
            psVeiculo.setString(3, modelo);
            psVeiculo.setInt(4, anoFabricacao);
            psVeiculo.setDouble(5, valorDiaria);
            psVeiculo.setString(6, cor);
            psVeiculo.setInt(7, quilometragem);
            psVeiculo.executeUpdate();

            ResultSet rs = psVeiculo.getGeneratedKeys();
            if (rs.next()) {
                int idVeiculo = rs.getInt(1);

                String sqlCarro = "INSERT INTO carro (id, portas, tipoCombustivel, arCondicionado, tipoCambio) " +
                                  "VALUES (?, ?, ?, ?, ?)";
                PreparedStatement psCarro = conn.prepareStatement(sqlCarro);
                psCarro.setInt(1, idVeiculo);
                psCarro.setInt(2, numPortas);
                psCarro.setString(3, tipoCombustivel);
                psCarro.setBoolean(4, arCondicionado);
                psCarro.setString(5, cambio);
                psCarro.executeUpdate();
            }
            System.out.println("Carro cadastrado com sucesso!");
        } catch (SQLException e) {
            System.out.println("Erro ao cadastrar carro: " + e.getMessage());
        }
    }

    public static void listarCarros() {
        try (Connection conn = Conexao.getConnection()) {
            String sql = "SELECT v.id, v.placa, v.marca, v.modelo, v.ano, v.precoDiario, v.cor, v.quilometragem, " +
                         "c.portas, c.tipoCombustivel, c.arCondicionado, c.tipoCambio " +
                         "FROM veiculo v INNER JOIN carro c ON v.id = c.id";
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);

            while (rs.next()) {
                System.out.printf("ID: %d | Modelo: %s | Marca: %s | Placa: %s | Ano: %d | Preço: R$%.2f | Cor: %s | Km: %d\n",
                                  rs.getInt("id"), rs.getString("modelo"), rs.getString("marca"),
                                  rs.getString("placa"), rs.getInt("ano"), rs.getDouble("precoDiario"),
                                  rs.getString("cor"), rs.getInt("quilometragem"));
                System.out.printf("Portas: %d | Combustível: %s | Ar Condicionado: %s | Câmbio: %s\n\n",
                                  rs.getInt("portas"), rs.getString("tipoCombustivel"),
                                  rs.getBoolean("arCondicionado") ? "Sim" : "Não",
                                  rs.getString("tipoCambio"));
            }
        } catch (SQLException e) {
            System.out.println("Erro ao listar carros: " + e.getMessage());
        }
    }

    public static void editarCarro(int id, String placa, String marca, String modelo, int anoFabricacao,
                                   double valorDiaria, String cor, int quilometragem, int numPortas,
                                   String tipoCombustivel, boolean arCondicionado, String cambio) {
        try (Connection conn = Conexao.getConnection()) {
            String sqlVeiculo = "UPDATE veiculo SET placa = ?, marca = ?, modelo = ?, ano = ?, precoDiario = ?, " +
                                "cor = ?, quilometragem = ? WHERE id = ?";
            PreparedStatement psVeiculo = conn.prepareStatement(sqlVeiculo);
            psVeiculo.setString(1, placa);
            psVeiculo.setString(2, marca);
            psVeiculo.setString(3, modelo);
            psVeiculo.setInt(4, anoFabricacao);
            psVeiculo.setDouble(5, valorDiaria);
            psVeiculo.setString(6, cor);
            psVeiculo.setInt(7, quilometragem);
            psVeiculo.setInt(8, id);
            psVeiculo.executeUpdate();

            String sqlCarro = "UPDATE carro SET portas = ?, tipoCombustivel = ?, arCondicionado = ?, tipoCambio = ? " +
                              "WHERE id = ?";
            PreparedStatement psCarro = conn.prepareStatement(sqlCarro);
            psCarro.setInt(1, numPortas);
            psCarro.setString(2, tipoCombustivel);
            psCarro.setBoolean(3, arCondicionado);
            psCarro.setString(4, cambio);
            psCarro.setInt(5, id);
            psCarro.executeUpdate();

            System.out.println("Carro editado com sucesso!");
        } catch (SQLException e) {
            System.out.println("Erro ao editar carro: " + e.getMessage());
        }
    }

    public static void apagarCarro(int id) {
        try (Connection conn = Conexao.getConnection()) {
            String sqlCarro = "DELETE FROM carro WHERE id = ?";
            PreparedStatement psCarro = conn.prepareStatement(sqlCarro);
            psCarro.setInt(1, id);
            psCarro.executeUpdate();

            String sqlVeiculo = "DELETE FROM veiculo WHERE id = ?";
            PreparedStatement psVeiculo = conn.prepareStatement(sqlVeiculo);
            psVeiculo.setInt(1, id);
            psVeiculo.executeUpdate();

            System.out.println("Carro removido com sucesso!");
        } catch (SQLException e) {
            System.out.println("Erro ao apagar carro: " + e.getMessage());
        }
    }

    @Override
    public double calcularSeguro() {
        return this.getPrecoDiario() * 0.15; // Exemplo
    }
}