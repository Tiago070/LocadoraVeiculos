package com.mycompany.locadoraveiculos;

import java.sql.*;

public class Moto extends Veiculo {

    private int cilindradas;
    private String tipoMoto;
    private boolean partidaEletrica;
    private String tipoFreio;

    public Moto(int id, String modelo, String placa,String marca,String cor, int ano,int quilometragem, double precoDiario,
            int cilindradas, String tipoMoto, boolean partidaEletrica, String tipoFreio) {
        super(id, modelo, placa, ano, precoDiario);
        this.cilindradas = cilindradas;
        this.tipoMoto = tipoMoto;
        this.partidaEletrica = partidaEletrica;
        this.tipoFreio = tipoFreio;
    }

    @Override
    public double calcularSeguro() {
        return getPrecoDiario() * 0.15 + 10;
    }

    // Getters e Setters
    public int getCilindradas() {
        return cilindradas;
    }

    public String getTipoMoto() {
        return tipoMoto;
    }

    public boolean isPartidaEletrica() {
        return partidaEletrica;
    }

    public String getTipoFreio() {
        return tipoFreio;
    }

    public void setCilindradas(int cilindradas) {
        this.cilindradas = cilindradas;
    }

    public void setTipoMoto(String tipoMoto) {
        this.tipoMoto = tipoMoto;
    }

    public void setPartidaEletrica(boolean partidaEletrica) {
        this.partidaEletrica = partidaEletrica;
    }

    public void setTipoFreio(String tipoFreio) {
        this.tipoFreio = tipoFreio;
    }

    public static void cadastrarMoto(String placa, String marca, String modelo, int anoFabricacao,
            double valorDiaria, String cor, int quilometragem,
            int cilindradas, boolean partidaEletrica,
            String tipoMoto, String tipoFreio) {
        String queryVeiculo = "INSERT INTO veiculos (placa, marca, modelo, ano_fabricacao, "
                + "valor_diaria, cor, quilometragem, tipo) VALUES (?, ?, ?, ?, ?, ?, ?, 'MOTO')";

        String queryMoto = "INSERT INTO motos (id_veiculo, cilindradas, partida_eletrica, tipo_moto, tipo_freio) "
                + "VALUES (LAST_INSERT_ID(), ?, ?, ?, ?)";

        try (Connection connection = Conexao.getConnection()) {
            connection.setAutoCommit(false);
            try (PreparedStatement stmtVeiculo = connection.prepareStatement(queryVeiculo); PreparedStatement stmtMoto = connection.prepareStatement(queryMoto)) {

                stmtVeiculo.setString(1, placa);
                stmtVeiculo.setString(2, marca);
                stmtVeiculo.setString(3, modelo);
                stmtVeiculo.setInt(4, anoFabricacao);
                stmtVeiculo.setDouble(5, valorDiaria);
                stmtVeiculo.setString(6, cor);
                stmtVeiculo.setInt(7, quilometragem);
                stmtVeiculo.executeUpdate();

                stmtMoto.setInt(1, cilindradas);
                stmtMoto.setBoolean(2, partidaEletrica);
                stmtMoto.setString(3, tipoMoto);
                stmtMoto.setString(4, tipoFreio);
                stmtMoto.executeUpdate();

                connection.commit();
                System.out.println("Moto cadastrada com sucesso.");
            } catch (SQLException e) {
                connection.rollback();
                System.out.println("Erro ao cadastrar moto: " + e.getMessage());
            }
        } catch (SQLException e) {
            System.out.println("Erro na conexão com o banco: " + e.getMessage());
        }
    }

    public static void listarMotos() {
        String query = "SELECT v.*, m.cilindradas, m.partida_eletrica, m.tipo_moto, m.tipo_freio "
                + "FROM veiculos v JOIN motos m ON v.id = m.id_veiculo WHERE v.tipo = 'MOTO'";

        try (Connection connection = Conexao.getConnection(); Statement stmt = connection.createStatement(); ResultSet rs = stmt.executeQuery(query)) {

            System.out.println("\nMotos cadastradas:");
            System.out.println("----------------------------------------------------------------------------------------------");
            System.out.printf("| %-4s | %-10s | %-10s | %-15s | %-4s | %-5s | %-10s | %-10s | %-10s |\n",
                    "ID", "Placa", "Marca", "Modelo", "Ano", "CC", "Partida", "Tipo", "Freio");
            System.out.println("----------------------------------------------------------------------------------------------");

            while (rs.next()) {
                int id = rs.getInt("id");
                String placa = rs.getString("placa");
                String marca = rs.getString("marca");
                String modelo = rs.getString("modelo");
                int ano = rs.getInt("ano_fabricacao");
                int cilindradas = rs.getInt("cilindradas");
                boolean partidaEletrica = rs.getBoolean("partida_eletrica");
                String tipoMoto = rs.getString("tipo_moto");
                String tipoFreio = rs.getString("tipo_freio");

                System.out.printf("| %-4d | %-10s | %-10s | %-15s | %-4d | %-5dcc | %-10s | %-10s | %-10s |\n",
                        id, placa, marca, modelo, ano, cilindradas,
                        partidaEletrica ? "Sim" : "Não", tipoMoto, tipoFreio);
            }
            System.out.println("----------------------------------------------------------------------------------------------");

        } catch (SQLException e) {
            System.out.println("Erro ao listar motos: " + e.getMessage());
        }
    }

    public static void editarMoto(int idVeiculo, String placa, String marca, String modelo,
            int anoFabricacao, double valorDiaria, String cor,
            int quilometragem, int cilindradas, boolean partidaEletrica,
            String tipoMoto, String tipoFreio) {
        String queryVeiculo = "UPDATE veiculos SET placa = ?, marca = ?, modelo = ?, "
                + "ano_fabricacao = ?, valor_diaria = ?, cor = ?, quilometragem = ? "
                + "WHERE id = ? AND tipo = 'MOTO'";

        String queryMoto = "UPDATE motos SET cilindradas = ?, partida_eletrica = ?, tipo_moto = ?, tipo_freio = ? "
                + "WHERE id_veiculo = ?";

        try (Connection connection = Conexao.getConnection()) {
            connection.setAutoCommit(false);
            try (PreparedStatement stmtVeiculo = connection.prepareStatement(queryVeiculo); PreparedStatement stmtMoto = connection.prepareStatement(queryMoto)) {

                stmtVeiculo.setString(1, placa);
                stmtVeiculo.setString(2, marca);
                stmtVeiculo.setString(3, modelo);
                stmtVeiculo.setInt(4, anoFabricacao);
                stmtVeiculo.setDouble(5, valorDiaria);
                stmtVeiculo.setString(6, cor);
                stmtVeiculo.setInt(7, quilometragem);
                stmtVeiculo.setInt(8, idVeiculo);
                int rowsVeiculo = stmtVeiculo.executeUpdate();

                stmtMoto.setInt(1, cilindradas);
                stmtMoto.setBoolean(2, partidaEletrica);
                stmtMoto.setString(3, tipoMoto);
                stmtMoto.setString(4, tipoFreio);
                stmtMoto.setInt(5, idVeiculo);
                int rowsMoto = stmtMoto.executeUpdate();

                if (rowsVeiculo > 0 && rowsMoto > 0) {
                    connection.commit();
                    System.out.println("Moto atualizada com sucesso.");
                } else {
                    connection.rollback();
                    System.out.println("Moto não encontrada ou dados inconsistentes.");
                }
            } catch (SQLException e) {
                connection.rollback();
                System.out.println("Erro ao editar moto: " + e.getMessage());
            }
        } catch (SQLException e) {
            System.out.println("Erro na conexão com o banco: " + e.getMessage());
        }
    }

    public static void apagarMoto(int idVeiculo) {
        String queryMoto = "DELETE FROM motos WHERE id_veiculo = ?";
        String queryVeiculo = "DELETE FROM veiculos WHERE id = ? AND tipo = 'MOTO'";

        try (Connection connection = Conexao.getConnection()) {
            connection.setAutoCommit(false);
            try (PreparedStatement stmtMoto = connection.prepareStatement(queryMoto); PreparedStatement stmtVeiculo = connection.prepareStatement(queryVeiculo)) {

                stmtMoto.setInt(1, idVeiculo);
                int rowsMoto = stmtMoto.executeUpdate();

                stmtVeiculo.setInt(1, idVeiculo);
                int rowsVeiculo = stmtVeiculo.executeUpdate();

                if (rowsMoto > 0 && rowsVeiculo > 0) {
                    connection.commit();
                    System.out.println("Moto apagada com sucesso.");
                } else {
                    connection.rollback();
                    System.out.println("Moto não encontrada.");
                }
            } catch (SQLException e) {
                connection.rollback();
                System.out.println("Erro ao apagar moto: " + e.getMessage());
            }
        } catch (SQLException e) {
            System.out.println("Erro na conexão com o banco: " + e.getMessage());
        }
    }
}