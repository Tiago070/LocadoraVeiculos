package com.mycompany.locadoraveiculos;

import java.sql.*;
import java.util.ArrayList;


public class PCarro {

    public String incluirCarro(Carro carro) {
        Connection conn = null;
        try {
            conn = Conexao.getConnection();
            conn.setAutoCommit(false); // Inicia a transação

            // 1. Inserir na tabela 'veiculo'
            String sqlVeiculo = "INSERT INTO veiculo (placa, marca, modelo, ano, precoDiario, cor, quilometragem) VALUES (?, ?, ?, ?, ?, ?, ?)";
            try (PreparedStatement psVeiculo = conn.prepareStatement(sqlVeiculo, Statement.RETURN_GENERATED_KEYS)) {
                psVeiculo.setString(1, carro.getPlaca());
                psVeiculo.setString(2, carro.getMarca());
                psVeiculo.setString(3, carro.getModelo());
                psVeiculo.setInt(4, carro.getAno());
                psVeiculo.setDouble(5, carro.getPrecoDiario());
                psVeiculo.setString(6, carro.getCor());
                psVeiculo.setInt(7, carro.getQuilometragem());
                psVeiculo.executeUpdate();

                // Obter o ID gerado para o veículo
                try (ResultSet rs = psVeiculo.getGeneratedKeys()) {
                    if (rs.next()) {
                        int idVeiculo = rs.getInt(1);
                        carro.setId(idVeiculo); // Atualiza o ID no objeto
                    } else {
                        throw new SQLException("Falha ao obter o ID do veículo, nenhuma chave gerada.");
                    }
                }
            }

            // 2. Inserir na tabela 'carro'
            String sqlCarro = "INSERT INTO carro (id, portas, tipoCombustivel, arCondicionado, tipoCambio) VALUES (?, ?, ?, ?, ?)";
            try (PreparedStatement psCarro = conn.prepareStatement(sqlCarro)) {
                psCarro.setInt(1, carro.getId());
                psCarro.setInt(2, carro.getNumPortas());
                psCarro.setString(3, carro.getTipoCombustivel());
                psCarro.setBoolean(4, carro.isArCondicionado());
                psCarro.setString(5, carro.getCambio());
                psCarro.executeUpdate();
            }

            conn.commit(); // Efetiva a transação
            return "Carro cadastrado com sucesso!";

        } catch (SQLException e) {
            if (conn != null) {
                try {
                    conn.rollback(); // Desfaz a transação em caso de erro
                } catch (SQLException ex) {
                    return "Erro ao reverter a transação: " + ex.getMessage();
                }
            }
            return "Erro ao cadastrar carro: " + e.getMessage();
        } finally {
            if (conn != null) {
                try {
                    conn.setAutoCommit(true);
                    conn.close();
                } catch (SQLException e) {
                    // Logar ou tratar erro de fechamento
                }
            }
        }
    }

    public String alterarCarro(Carro carro) {
        Connection conn = null;
        try {
            conn = Conexao.getConnection();
            conn.setAutoCommit(false); // Inicia a transação

            // 1. Atualizar a tabela 'veiculo'
            String sqlVeiculo = "UPDATE veiculo SET placa = ?, marca = ?, modelo = ?, ano = ?, precoDiario = ?, "
                    + "cor = ?, quilometragem = ? WHERE id = ?";
            try (PreparedStatement psVeiculo = conn.prepareStatement(sqlVeiculo)) {
                psVeiculo.setString(1, carro.getPlaca());
                psVeiculo.setString(2, carro.getMarca());
                psVeiculo.setString(3, carro.getModelo());
                psVeiculo.setInt(4, carro.getAno());
                psVeiculo.setDouble(5, carro.getPrecoDiario());
                psVeiculo.setString(6, carro.getCor());
                psVeiculo.setInt(7, carro.getQuilometragem());
                psVeiculo.setInt(8, carro.getId());
                psVeiculo.executeUpdate();
            }

            // 2. Atualizar a tabela 'carro'
            String sqlCarro = "UPDATE carro SET portas = ?, tipoCombustivel = ?, arCondicionado = ?, tipoCambio = ? "
                    + "WHERE id = ?";
            try (PreparedStatement psCarro = conn.prepareStatement(sqlCarro)) {
                psCarro.setInt(1, carro.getNumPortas());
                psCarro.setString(2, carro.getTipoCombustivel());
                psCarro.setBoolean(3, carro.isArCondicionado());
                psCarro.setString(4, carro.getCambio());
                psCarro.setInt(5, carro.getId());
                psCarro.executeUpdate();
            }

            conn.commit(); // Efetiva a transação
            return "Carro alterado com sucesso!";

        } catch (SQLException e) {
            if (conn != null) {
                try {
                    conn.rollback();
                } catch (SQLException ex) {
                }
            }
            return "Erro ao alterar carro: " + e.getMessage();
        } finally {
            if (conn != null) {
                try {
                    conn.setAutoCommit(true);
                    conn.close();
                } catch (SQLException e) {
                }
            }
        }
    }

    public String excluirCarro(int idCarro) {
        Connection conn = null;
        try {
            conn = Conexao.getConnection();
            conn.setAutoCommit(false); // Inicia a transação

            // 1. Deletar da tabela filha 'carro'
            String sqlCarro = "DELETE FROM carro WHERE id = ?";
            try (PreparedStatement psCarro = conn.prepareStatement(sqlCarro)) {
                psCarro.setInt(1, idCarro);
                psCarro.executeUpdate();
            }

            // 2. Deletar da tabela pai 'veiculo'
            String sqlVeiculo = "DELETE FROM veiculo WHERE id = ?";
            try (PreparedStatement psVeiculo = conn.prepareStatement(sqlVeiculo)) {
                psVeiculo.setInt(1, idCarro);
                psVeiculo.executeUpdate();
            }

            conn.commit(); // Efetiva a transação
            return "Carro excluído com sucesso!";

        } catch (SQLException e) {
            if (conn != null) {
                try {
                    conn.rollback();
                } catch (SQLException ex) {
                    return "Erro ao reverter a transação: " + ex.getMessage();
                }
            }
            return "Erro ao excluir carro: " + e.getMessage();
        } finally {
            if (conn != null) {
                try {
                    conn.setAutoCommit(true);
                    conn.close();
                } catch (SQLException e) {
                }
            }
        }
    }

    public ArrayList<Carro> consultarCarros() {
        ArrayList<Carro> lista = new ArrayList<>();
        String sql = "SELECT v.*, c.portas, c.tipoCombustivel, c.arCondicionado, c.tipoCambio "
                + "FROM veiculo v INNER JOIN carro c ON v.id = c.id ORDER BY v.modelo";

        try (Connection conn = Conexao.getConnection(); Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Carro carro = new Carro();
                // Atributos de Veiculo
                carro.setId(rs.getInt("id"));
                carro.setPlaca(rs.getString("placa"));
                carro.setMarca(rs.getString("marca"));
                carro.setModelo(rs.getString("modelo"));
                carro.setAno(rs.getInt("ano"));
                carro.setPrecoDiario(rs.getDouble("precoDiario"));
                carro.setCor(rs.getString("cor"));
                carro.setQuilometragem(rs.getInt("quilometragem"));
                // Atributos de Carro
                carro.setNumPortas(rs.getInt("portas"));
                carro.setTipoCombustivel(rs.getString("tipoCombustivel"));
                carro.setArCondicionado(rs.getBoolean("arCondicionado"));
                carro.setCambio(rs.getString("tipoCambio"));
                lista.add(carro);
            }
        } catch (SQLException e) {
            System.out.println("Erro ao consultar carros: " + e.getMessage());
        }
        return lista;

    }
}