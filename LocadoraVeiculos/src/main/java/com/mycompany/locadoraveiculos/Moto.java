package com.mycompany.locadoraveiculos;

import java.sql.*;

public class Moto extends Veiculo {
    private int cilindradas;
    private String tipoMoto;
    private boolean partidaEletrica;
    private String tipoFreio;

    public Moto(int id, String placa, String marca, String modelo, int anoFabricacao,
                double valorDiaria, String cor, int quilometragem, int cilindradas,
                boolean partidaEletrica, String tipoMoto, String tipoFreio) {
        super(id, modelo, placa, anoFabricacao,quilometragem, valorDiaria);
        this.setMarca(marca);
        this.setCor(cor);
        this.setQuilometragem(quilometragem);
        this.cilindradas = cilindradas;
        this.tipoMoto = tipoMoto;
        this.partidaEletrica = partidaEletrica;
        this.tipoFreio = tipoFreio;
    }

    public static void cadastrarMoto(String placa, String marca, String modelo, int anoFabricacao,
                                     double valorDiaria, String cor, int quilometragem,
                                     int cilindradas, boolean partidaEletrica,
                                     String tipoMoto, String tipoFreio) {
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

                String sqlMoto = "INSERT INTO moto (id, cilindradas, partidaEletrica, tipoMoto, tipoFreio) " +
                                 "VALUES (?, ?, ?, ?, ?)";
                PreparedStatement psMoto = conn.prepareStatement(sqlMoto);
                psMoto.setInt(1, idVeiculo);
                psMoto.setInt(2, cilindradas);
                psMoto.setBoolean(3, partidaEletrica);
                psMoto.setString(4, tipoMoto);
                psMoto.setString(5, tipoFreio);
                psMoto.executeUpdate();
            }
            System.out.println("Moto cadastrada com sucesso!");
        } catch (SQLException e) {
            System.out.println("Erro ao cadastrar moto: " + e.getMessage());
        }
    }

    public static void listarMotos() {
        try (Connection conn = Conexao.getConnection()) {
            String sql = "SELECT v.id, v.placa, v.marca, v.modelo, v.ano, v.precoDiario, v.cor, v.quilometragem, " +
                         "m.cilindradas, m.partidaEletrica, m.tipoMoto, m.tipoFreio " +
                         "FROM veiculo v INNER JOIN moto m ON v.id = m.id";
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);

            while (rs.next()) {
                System.out.printf("ID: %d | Modelo: %s | Marca: %s | Placa: %s | Ano: %d | Preço: R$%.2f | Cor: %s | Km: %d\n",
                                  rs.getInt("id"), rs.getString("modelo"), rs.getString("marca"),
                                  rs.getString("placa"), rs.getInt("ano"), rs.getDouble("precoDiario"),
                                  rs.getString("cor"), rs.getInt("quilometragem"));
                System.out.printf("Cilindradas: %d | Partida Elétrica: %s | Tipo: %s | Freio: %s\n\n",
                                  rs.getInt("cilindradas"),
                                  rs.getBoolean("partidaEletrica") ? "Sim" : "Não",
                                  rs.getString("tipoMoto"),
                                  rs.getString("tipoFreio"));
            }
        } catch (SQLException e) {
            System.out.println("Erro ao listar motos: " + e.getMessage());
        }
    }

    public static void editarMoto(int id, String placa, String marca, String modelo,
                                  int anoFabricacao, double valorDiaria, String cor,
                                  int quilometragem, int cilindradas, boolean partidaEletrica,
                                  String tipoMoto, String tipoFreio) {
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

            String sqlMoto = "UPDATE moto SET cilindradas = ?, partidaEletrica = ?, tipoMoto = ?, tipoFreio = ? " +
                             "WHERE id = ?";
            PreparedStatement psMoto = conn.prepareStatement(sqlMoto);
            psMoto.setInt(1, cilindradas);
            psMoto.setBoolean(2, partidaEletrica);
            psMoto.setString(3, tipoMoto);
            psMoto.setString(4, tipoFreio);
            psMoto.setInt(5, id);
            psMoto.executeUpdate();

            System.out.println("Moto editada com sucesso!");
        } catch (SQLException e) {
            System.out.println("Erro ao editar moto: " + e.getMessage());
        }
    }

    public static void apagarMoto(int id) {
        try (Connection conn = Conexao.getConnection()) {
            String sqlMoto = "DELETE FROM moto WHERE id = ?";
            PreparedStatement psMoto = conn.prepareStatement(sqlMoto);
            psMoto.setInt(1, id);
            psMoto.executeUpdate();

            String sqlVeiculo = "DELETE FROM veiculo WHERE id = ?";
            PreparedStatement psVeiculo = conn.prepareStatement(sqlVeiculo);
            psVeiculo.setInt(1, id);
            psVeiculo.executeUpdate();

            System.out.println("Moto removida com sucesso!");
        } catch (SQLException e) {
            System.out.println("Erro ao apagar moto: " + e.getMessage());
        }
    }

    @Override
    public double calcularSeguro() {
        return this.getPrecoDiario() * 0.10; // Exemplo para motos
    }
}