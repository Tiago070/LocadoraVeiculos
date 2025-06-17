package com.mycompany.locadoraveiculos;

import java.sql.*;
import java.util.ArrayList;


public class PMoto {

    public String incluirMoto(Moto moto) {
        Connection conn = null;
        try {
            conn = Conexao.getConnection();
            conn.setAutoCommit(false); // Inicia a transação

            // 1. Inserir na tabela 'veiculo'
            String sqlVeiculo = "INSERT INTO veiculo (placa, marca, modelo, ano, precoDiario, cor, quilometragem) VALUES (?, ?, ?, ?, ?, ?, ?)";
            try (PreparedStatement psVeiculo = conn.prepareStatement(sqlVeiculo, Statement.RETURN_GENERATED_KEYS)) {
                psVeiculo.setString(1, moto.getPlaca());
                psVeiculo.setString(2, moto.getMarca());
                psVeiculo.setString(3, moto.getModelo());
                psVeiculo.setInt(4, moto.getAno());
                psVeiculo.setDouble(5, moto.getPrecoDiario());
                psVeiculo.setString(6, moto.getCor());
                psVeiculo.setInt(7, moto.getQuilometragem());
                psVeiculo.executeUpdate();

                try (ResultSet rs = psVeiculo.getGeneratedKeys()) {
                    if (rs.next()) {
                        moto.setId(rs.getInt(1)); // Pega o ID gerado
                    } else {
                        throw new SQLException("Falha ao obter o ID do veículo.");
                    }
                }
            }

            // 2. Inserir na tabela 'moto'
            String sqlMoto = "INSERT INTO moto (id, cilindradas, partidaEletrica, tipoMoto, tipoFreio) VALUES (?, ?, ?, ?, ?)";
            try (PreparedStatement psMoto = conn.prepareStatement(sqlMoto)) {
                psMoto.setInt(1, moto.getId());
                psMoto.setInt(2, moto.getCilindradas());
                psMoto.setBoolean(3, moto.isPartidaEletrica());
                psMoto.setString(4, moto.getTipoMoto());
                psMoto.setString(5, moto.getTipoFreio());
                psMoto.executeUpdate();
            }

            conn.commit(); // Efetiva a transação
            return "Moto cadastrada com sucesso!";

        } catch (SQLException e) {
            if (conn != null) {
                try {
                    conn.rollback();
                } catch (SQLException ex) {
                }
            }
            return "Erro ao cadastrar moto: " + e.getMessage();
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

    public String alterarMoto(Moto moto) {
        Connection conn = null;
        try {
            conn = Conexao.getConnection();
            conn.setAutoCommit(false); // Inicia a transação

            String sqlVeiculo = "UPDATE veiculo SET placa = ?, marca = ?, modelo = ?, ano = ?, precoDiario = ?, "
                    + "cor = ?, quilometragem = ? WHERE id = ?";
            try (PreparedStatement psVeiculo = conn.prepareStatement(sqlVeiculo)) {
                psVeiculo.setString(1, moto.getPlaca());
                psVeiculo.setString(2, moto.getMarca());
                psVeiculo.setString(3, moto.getModelo());
                psVeiculo.setInt(4, moto.getAno());
                psVeiculo.setDouble(5, moto.getPrecoDiario());
                psVeiculo.setString(6, moto.getCor());
                psVeiculo.setInt(7, moto.getQuilometragem());
                psVeiculo.setInt(8, moto.getId());
                psVeiculo.executeUpdate();
            }

            String sqlMoto = "UPDATE moto SET cilindradas = ?, partidaEletrica = ?, tipoMoto = ?, tipoFreio = ? "
                    + "WHERE id = ?";
            try (PreparedStatement psMoto = conn.prepareStatement(sqlMoto)) {
                psMoto.setInt(1, moto.getCilindradas());
                psMoto.setBoolean(2, moto.isPartidaEletrica());
                psMoto.setString(3, moto.getTipoMoto());
                psMoto.setString(4, moto.getTipoFreio());
                psMoto.setInt(5, moto.getId());
                psMoto.executeUpdate();
            }

            conn.commit();
            return "Moto alterada com sucesso!";

        } catch (SQLException e) {
            if (conn != null) {
                try {
                    conn.rollback();
                } catch (SQLException ex) {
                }
            }
            return "Erro ao alterar moto: " + e.getMessage();
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

    public String excluirMoto(int idMoto) {
        Connection conn = null;
        try {
            conn = Conexao.getConnection();
            conn.setAutoCommit(false);

            String sqlMoto = "DELETE FROM moto WHERE id = ?";
            try (PreparedStatement psMoto = conn.prepareStatement(sqlMoto)) {
                psMoto.setInt(1, idMoto);
                psMoto.executeUpdate();
            }

            String sqlVeiculo = "DELETE FROM veiculo WHERE id = ?";
            try (PreparedStatement psVeiculo = conn.prepareStatement(sqlVeiculo)) {
                psVeiculo.setInt(1, idMoto);
                psVeiculo.executeUpdate();
            }

            conn.commit();
            return "Moto excluída com sucesso!";

        } catch (SQLException e) {
            if (conn != null) {
                try {
                    conn.rollback();
                } catch (SQLException ex) {
                }
            }
            return "Erro ao excluir moto: " + e.getMessage();
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

    public ArrayList<Moto> consultarMotos() {
        ArrayList<Moto> lista = new ArrayList<>();
        String sql = "SELECT v.*, m.cilindradas, m.partidaEletrica, m.tipoMoto, m.tipoFreio "
                + "FROM veiculo v INNER JOIN moto m ON v.id = m.id ORDER BY v.modelo";

        try (Connection conn = Conexao.getConnection(); Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Moto moto = new Moto();
                // Veiculo
                moto.setId(rs.getInt("id"));
                moto.setPlaca(rs.getString("placa"));
                moto.setMarca(rs.getString("marca"));
                moto.setModelo(rs.getString("modelo"));
                moto.setAno(rs.getInt("ano"));
                moto.setPrecoDiario(rs.getDouble("precoDiario"));
                moto.setCor(rs.getString("cor"));
                moto.setQuilometragem(rs.getInt("quilometragem"));
                // Moto
                moto.setCilindradas(rs.getInt("cilindradas"));
                moto.setPartidaEletrica(rs.getBoolean("partidaEletrica"));
                moto.setTipoMoto(rs.getString("tipoMoto"));
                moto.setTipoFreio(rs.getString("tipoFreio"));
                lista.add(moto);
            }
        } catch (SQLException e) {
            System.out.println("Erro ao consultar motos: " + e.getMessage());
        }
        return lista;
    }
}