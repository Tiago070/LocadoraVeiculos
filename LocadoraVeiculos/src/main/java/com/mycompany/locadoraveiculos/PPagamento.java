package com.mycompany.locadoraveiculos;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;


public class PPagamento {

    public String incluirPagamento(Pagamento pagamento) {
        String sql = "INSERT INTO pagamentos (id_aluguel, id_cliente, id_veiculo, valor_pago, forma_pagamento, data_pagamento) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = Conexao.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {

            Aluguel aluguel = pagamento.getAluguel(); 

            stmt.setInt(1, aluguel.getId());
            stmt.setInt(2, aluguel.getCliente().getIdCliente());
            stmt.setInt(3, aluguel.getVeiculo().getId());

            stmt.setDouble(4, pagamento.getValorPago());
            stmt.setString(5, pagamento.getFormaPagamento());
            stmt.setDate(6, Date.valueOf(pagamento.getDataPagamento()));

            stmt.executeUpdate();
            return "Pagamento registrado com sucesso!";

        } catch (SQLException e) {
            e.printStackTrace();
            return "Erro ao registrar pagamento: " + e.getMessage();
        }
    }

    public ArrayList<Object[]> consultarHistoricoPagamentos() {
        ArrayList<Object[]> historico = new ArrayList<>();
        String sql = "SELECT p.id, c.nome, v.modelo, p.valor_pago, p.forma_pagamento, p.data_pagamento "
                + "FROM pagamentos p "
                + "JOIN aluguel a ON p.id_aluguel = a.id "
                + "JOIN cliente c ON a.idCliente = c.idCliente "
                + "JOIN veiculo v ON a.id_veiculo = v.id "
                + "ORDER BY p.data_pagamento DESC";

        try (Connection conn = Conexao.getConnection(); Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                historico.add(new Object[]{
                    rs.getInt("id"),
                    rs.getString("nome"),
                    rs.getString("modelo"),
                    rs.getDouble("valor_pago"),
                    rs.getString("forma_pagamento"),
                    rs.getDate("data_pagamento").toLocalDate()
                });
            }
        } catch (SQLException e) {
            System.out.println("Erro ao consultar histórico de pagamentos: " + e.getMessage());
        }
        return historico;
    }
}
