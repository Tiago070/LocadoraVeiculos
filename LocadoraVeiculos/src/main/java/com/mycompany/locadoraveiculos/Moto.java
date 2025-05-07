package com.mycompany.locadoraveiculos;

import java.sql.*;

public class Moto extends Veiculo {

    private int cilindradas;
    private String tipoMoto; // esportiva, custom, etc.
    private boolean partidaEletrica;
    private String tipoFreio;

    public Moto(int id, String modelo, String placa, int ano, double precoDiario,
            int cilindradas, String tipoMoto, boolean partidaEletrica, String tipoFreio) {
        super(id, modelo, placa, ano, precoDiario);
        this.cilindradas = cilindradas;
        this.tipoMoto = tipoMoto;
        this.partidaEletrica = partidaEletrica;
        this.tipoFreio = tipoFreio;
    }

    @Override
    public double calcularSeguro() {
        // Seguro para moto: 15% do preço diário + taxa fixa de R$10
        return getPrecoDiario() * 0.15 + 10;
    }

    // Getters e Setters específicos
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
                                   int cilindradas, boolean partidaEletrica) {
        // Primeiro cadastra o veículo na tabela pai
        String queryVeiculo = "INSERT INTO veiculos (placa, marca, modelo, ano_fabricacao, " +
                             "valor_diaria, cor, quilometragem, tipo) VALUES (?, ?, ?, ?, ?, ?, ?, 'MOTO')";
        
        // Depois cadastra os dados específicos da moto
        String queryMoto = "INSERT INTO motos (id_veiculo, cilindradas, partida_eletrica) " +
                          "VALUES (LAST_INSERT_ID(), ?, ?)";

        try (Connection connection = Conexao.getConnection()) {
            // Inicia transação
            connection.setAutoCommit(false);

            try (PreparedStatement stmtVeiculo = connection.prepareStatement(queryVeiculo);
                 PreparedStatement stmtMoto = connection.prepareStatement(queryMoto)) {
                
                // Cadastra o veículo
                stmtVeiculo.setString(1, placa);
                stmtVeiculo.setString(2, marca);
                stmtVeiculo.setString(3, modelo);
                stmtVeiculo.setInt(4, anoFabricacao);
                stmtVeiculo.setDouble(5, valorDiaria);
                stmtVeiculo.setString(6, cor);
                stmtVeiculo.setInt(7, quilometragem);
                stmtVeiculo.executeUpdate();

                // Cadastra os dados específicos da moto
                stmtMoto.setInt(1, cilindradas);
                stmtMoto.setBoolean(2, partidaEletrica);
                stmtMoto.executeUpdate();

                // Confirma transação
                connection.commit();
                System.out.println("Moto cadastrada com sucesso.");
                
            } catch (SQLException e) {
                // Em caso de erro, faz rollback
                connection.rollback();
                System.out.println("Erro ao cadastrar moto: " + e.getMessage());
            }
        } catch (SQLException e) {
            System.out.println("Erro na conexão com o banco: " + e.getMessage());
        }
    }
    public static void listarMotos() {
        String query = "SELECT v.*, m.cilindradas, m.partida_eletrica " +
                      "FROM veiculos v " +
                      "JOIN motos m ON v.id = m.id_veiculo " +
                      "WHERE v.tipo = 'MOTO'";

        try (Connection connection = Conexao.getConnection();
             Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            System.out.println("\nMotos cadastradas:");
            System.out.println("----------------------------------------------------------------");
            System.out.printf("| %-4s | %-10s | %-15s | %-15s | %-4s | %-10s | %-15s |\n", 
                             "ID", "Placa", "Marca", "Modelo", "Ano", "Cilindradas", "Partida Elétrica");
            System.out.println("----------------------------------------------------------------");

            while (rs.next()) {
                int id = rs.getInt("id");
                String placa = rs.getString("placa");
                String marca = rs.getString("marca");
                String modelo = rs.getString("modelo");
                int ano = rs.getInt("ano_fabricacao");
                int cilindradas = rs.getInt("cilindradas");
                boolean partidaEletrica = rs.getBoolean("partida_eletrica");

                System.out.printf("| %-4d | %-10s | %-15s | %-15s | %-4d | %-10d | %-15s |\n",
                                id, placa, marca, modelo, ano, cilindradas, 
                                partidaEletrica ? "Sim" : "Não");
            }
            System.out.println("----------------------------------------------------------------");
        } catch (SQLException e) {
            System.out.println("Erro ao listar motos: " + e.getMessage());
        }
    }
    public static void editarMoto(int idVeiculo, String placa, String marca, String modelo, 
                                int anoFabricacao, double valorDiaria, String cor, 
                                int quilometragem, int cilindradas, boolean partidaEletrica) {
        // Query para atualizar a tabela veiculos
        String queryVeiculo = "UPDATE veiculos SET placa = ?, marca = ?, modelo = ?, " +
                             "ano_fabricacao = ?, valor_diaria = ?, cor = ?, quilometragem = ? " +
                             "WHERE id = ? AND tipo = 'MOTO'";
        
        // Query para atualizar a tabela motos
        String queryMoto = "UPDATE motos SET cilindradas = ?, partida_eletrica = ? " +
                          "WHERE id_veiculo = ?";

        try (Connection connection = Conexao.getConnection()) {
            // Inicia transação
            connection.setAutoCommit(false);

            try (PreparedStatement stmtVeiculo = connection.prepareStatement(queryVeiculo);
                 PreparedStatement stmtMoto = connection.prepareStatement(queryMoto)) {
                
                // Atualiza dados na tabela veiculos
                stmtVeiculo.setString(1, placa);
                stmtVeiculo.setString(2, marca);
                stmtVeiculo.setString(3, modelo);
                stmtVeiculo.setInt(4, anoFabricacao);
                stmtVeiculo.setDouble(5, valorDiaria);
                stmtVeiculo.setString(6, cor);
                stmtVeiculo.setInt(7, quilometragem);
                stmtVeiculo.setInt(8, idVeiculo);
                int rowsVeiculo = stmtVeiculo.executeUpdate();

                // Atualiza dados na tabela motos
                stmtMoto.setInt(1, cilindradas);
                stmtMoto.setBoolean(2, partidaEletrica);
                stmtMoto.setInt(3, idVeiculo);
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
}

