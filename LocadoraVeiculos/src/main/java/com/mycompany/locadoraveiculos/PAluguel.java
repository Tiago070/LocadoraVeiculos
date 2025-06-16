package com.mycompany.locadoraveiculos;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;

public class PAluguel {

    public String incluirAluguel(Aluguel aluguel) {
        String sql = "INSERT INTO aluguel (idCliente, id_veiculo, data_inicio, data_fim) VALUES (?, ?, ?, ?)";
        try (Connection conn = Conexao.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, aluguel.getCliente().getIdCliente());
            stmt.setInt(2, aluguel.getVeiculo().getId());
            stmt.setDate(3, Date.valueOf(aluguel.getDataInicio()));
            if (aluguel.getDataFim() != null) {
                stmt.setDate(4, Date.valueOf(aluguel.getDataFim()));
            } else {
                stmt.setNull(4, Types.DATE);
            }

            stmt.executeUpdate();
            return "Aluguel registrado com sucesso!";
        } catch (SQLException e) {
            return "Erro ao inserir aluguel: " + e.getMessage();
        }
    }

    public String registrarDevolucao(int idAluguel, LocalDate dataDevolucao) {
        String sql = "UPDATE aluguel SET data_fim = ? WHERE id = ?";
        try (Connection conn = Conexao.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setDate(1, Date.valueOf(dataDevolucao));
            stmt.setInt(2, idAluguel);

            int rowsUpdated = stmt.executeUpdate();
            if (rowsUpdated > 0) {
                return "Devolução registrada com sucesso!";
            } else {
                return "Aluguel não encontrado.";
            }
        } catch (SQLException e) {
            return "Erro ao registrar devolução: " + e.getMessage();
        }
    }

    public ArrayList<Aluguel> consultarAlugueis() {
        ArrayList<Aluguel> lista = new ArrayList<>();
        String sql = "SELECT a.id as aluguel_id, a.data_inicio, a.data_fim, "
                + "c.*, v.*, "
                + "cr.portas, cr.tipoCombustivel, cr.arCondicionado, cr.tipoCambio, "
                + "m.cilindradas, m.partidaEletrica, m.tipoMoto, m.tipoFreio "
                + "FROM aluguel a "
                + "JOIN cliente c ON a.idCliente = c.idCliente "
                + "JOIN veiculo v ON a.id_veiculo = v.id "
                + "LEFT JOIN carro cr ON v.id = cr.id "
                + "LEFT JOIN moto m ON v.id = m.id "
                + "ORDER BY a.data_inicio DESC";

        try (Connection conn = Conexao.getConnection(); Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Cliente cliente = new Cliente(
                        rs.getInt("idCliente"), rs.getString("nome"), rs.getString("cpf"),
                        rs.getString("telefone"), rs.getString("email")
                );

                Veiculo veiculo = null;
                if (rs.getObject("portas") != null) {
                    veiculo = new Carro(
                            rs.getInt("id"), rs.getString("placa"), rs.getString("marca"),
                            rs.getString("modelo"), rs.getInt("ano"), rs.getDouble("precoDiario"),
                            rs.getString("cor"), rs.getInt("quilometragem"), rs.getInt("portas"),
                            rs.getString("tipoCombustivel"), rs.getBoolean("arCondicionado"),
                            rs.getString("tipoCambio")
                    );
                } else {
                    veiculo = new Moto();
                    veiculo.setId(rs.getInt("id"));
                    veiculo.setPlaca(rs.getString("placa"));
                    veiculo.setMarca(rs.getString("marca"));
                    veiculo.setModelo(rs.getString("modelo"));
                    veiculo.setAno(rs.getInt("ano"));
                    veiculo.setPrecoDiario(rs.getDouble("precoDiario"));
                    veiculo.setCor(rs.getString("cor"));
                    veiculo.setQuilometragem(rs.getInt("quilometragem"));
                    ((Moto) veiculo).setCilindradas(rs.getInt("cilindradas"));
                    ((Moto) veiculo).setPartidaEletrica(rs.getBoolean("partidaEletrica"));
                    ((Moto) veiculo).setTipoMoto(rs.getString("tipoMoto"));
                    ((Moto) veiculo).setTipoFreio(rs.getString("tipoFreio"));
                }

                LocalDate dataFim = (rs.getDate("data_fim") != null) ? rs.getDate("data_fim").toLocalDate() : null;
                Aluguel aluguel = new Aluguel(
                        rs.getInt("aluguel_id"), cliente, veiculo,
                        rs.getDate("data_inicio").toLocalDate(), dataFim
                );

                lista.add(aluguel);
            }
        } catch (SQLException e) {
            System.out.println("Erro ao consultar aluguéis: " + e.getMessage());
            e.printStackTrace();
        }
        return lista;
    }

    public ArrayList<Aluguel> consultarAlugueisParaPagamento() {
        ArrayList<Aluguel> lista = new ArrayList<>();
        // A subquery `NOT IN (SELECT id_aluguel FROM pagamentos)` filtra os já pagos
        String sql = "SELECT a.id as aluguel_id, a.data_inicio, a.data_fim, c.*, v.* "
                + "FROM aluguel a "
                + "JOIN cliente c ON a.idCliente = c.idCliente "
                + "JOIN veiculo v ON a.id_veiculo = v.id "
                + "WHERE a.data_fim IS NOT NULL AND a.id NOT IN (SELECT id_aluguel FROM pagamentos)";

        try (Connection conn = Conexao.getConnection(); Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Cliente cliente = new Cliente();
                cliente.setIdCliente(rs.getInt("idCliente"));
                cliente.setNome(rs.getString("nome"));

                Veiculo veiculo = new Carro(); // Apenas para instanciar, poderia ser moto
                veiculo.setId(rs.getInt("id"));
                veiculo.setModelo(rs.getString("modelo"));
                veiculo.setPlaca(rs.getString("placa"));
                veiculo.setPrecoDiario(rs.getDouble("precoDiario"));

                Aluguel aluguel = new Aluguel(
                        rs.getInt("aluguel_id"), cliente, veiculo,
                        rs.getDate("data_inicio").toLocalDate(),
                        rs.getDate("data_fim").toLocalDate()
                );
                lista.add(aluguel);
            }
        } catch (SQLException e) {
            System.out.println("Erro ao consultar aluguéis para pagamento: " + e.getMessage());
        }
        return lista;
    }

    public String atualizarAluguel(Aluguel aluguel) {
        String sql = "UPDATE aluguel SET idCliente = ?, id_veiculo = ?, data_inicio = ? WHERE id = ?";
        try (Connection conn = Conexao.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, aluguel.getCliente().getIdCliente());
            stmt.setInt(2, aluguel.getVeiculo().getId());
            stmt.setDate(3, Date.valueOf(aluguel.getDataInicio()));
            stmt.setInt(4, aluguel.getId());

            int rowsUpdated = stmt.executeUpdate();
            if (rowsUpdated > 0) {
                return "Aluguel atualizado com sucesso!";
            } else {
                return "Aluguel não encontrado.";
            }
        } catch (SQLException e) {
            return "Erro ao atualizar aluguel: " + e.getMessage();
        }
    }

    public Aluguel consultarAluguelPorId(int idAluguel) {
        String sql = "SELECT a.id as aluguel_id, a.data_inicio, a.data_fim, "
                + "c.*, v.*, "
                + "cr.portas, cr.tipoCombustivel, cr.arCondicionado, cr.tipoCambio, "
                + "m.cilindradas, m.partidaEletrica, m.tipoMoto, m.tipoFreio "
                + "FROM aluguel a "
                + "JOIN cliente c ON a.idCliente = c.idCliente "
                + "JOIN veiculo v ON a.id_veiculo = v.id "
                + "LEFT JOIN carro cr ON v.id = cr.id "
                + "LEFT JOIN moto m ON v.id = m.id "
                + "WHERE a.id = ?";

        try (Connection conn = Conexao.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idAluguel);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                Cliente cliente = new Cliente(
                        rs.getInt("idCliente"), rs.getString("nome"), rs.getString("cpf"),
                        rs.getString("telefone"), rs.getString("email")
                );

                Veiculo veiculo = null;
                if (rs.getObject("portas") != null) {
                    veiculo = new Carro(
                            rs.getInt("id"), rs.getString("placa"), rs.getString("marca"),
                            rs.getString("modelo"), rs.getInt("ano"), rs.getDouble("precoDiario"),
                            rs.getString("cor"), rs.getInt("quilometragem"), rs.getInt("portas"),
                            rs.getString("tipoCombustivel"), rs.getBoolean("arCondicionado"),
                            rs.getString("tipoCambio")
                    );
                } else {
                    veiculo = new Moto();
                    veiculo.setId(rs.getInt("id"));
                    veiculo.setPlaca(rs.getString("placa"));
                    veiculo.setMarca(rs.getString("marca"));
                    veiculo.setModelo(rs.getString("modelo"));
                    veiculo.setAno(rs.getInt("ano"));
                    veiculo.setPrecoDiario(rs.getDouble("precoDiario"));
                    veiculo.setCor(rs.getString("cor"));
                    veiculo.setQuilometragem(rs.getInt("quilometragem"));
                    ((Moto) veiculo).setCilindradas(rs.getInt("cilindradas"));
                    ((Moto) veiculo).setPartidaEletrica(rs.getBoolean("partidaEletrica"));
                    ((Moto) veiculo).setTipoMoto(rs.getString("tipoMoto"));
                    ((Moto) veiculo).setTipoFreio(rs.getString("tipoFreio"));
                }

                LocalDate dataFim = (rs.getDate("data_fim") != null)
                        ? rs.getDate("data_fim").toLocalDate() : null;

                return new Aluguel(
                        rs.getInt("aluguel_id"), cliente, veiculo,
                        rs.getDate("data_inicio").toLocalDate(), dataFim
                );
            }
        } catch (SQLException e) {
            System.out.println("Erro ao consultar aluguel por ID: " + e.getMessage());
        }
        return null;
    }
}