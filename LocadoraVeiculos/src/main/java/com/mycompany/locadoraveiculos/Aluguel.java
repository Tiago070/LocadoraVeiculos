package com.mycompany.locadoraveiculos;

import java.sql.*;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class Aluguel {

    private int id;
    private Cliente cliente;
    private Veiculo veiculo;
    private LocalDate dataInicio;
    private LocalDate dataFim;

    public Aluguel(int id, Cliente cliente, Veiculo veiculo, LocalDate dataInicio, LocalDate dataFim) {
        this.id = id;
        this.cliente = cliente;
        this.veiculo = veiculo;
        this.dataInicio = dataInicio;
        this.dataFim = dataFim;
    }

    public double calcularTotalAluguel() {
        if (dataInicio == null || dataFim == null || veiculo == null) {
            return 0.0; // Evitar erro se datas ou veículo não estiverem definidos
        }
        long dias = ChronoUnit.DAYS.between(dataInicio, dataFim);
        return dias * veiculo.getPrecoDiario();
    }

    @Override
    public String toString() {
        return "Aluguel ID: " + id + " | Cliente: " + cliente.getNome() + " | Veículo: " + veiculo.getModelo()
                + " | Início: " + dataInicio + " | Fim: " + dataFim + " | Total: R$" + calcularTotalAluguel();
    }

    // Métodos de controle com banco de dados
    public static void inserirAluguel(Cliente cliente, Veiculo veiculo, LocalDate dataInicio, LocalDate dataFim) {
    String query = "INSERT INTO aluguel (idCliente, id_veiculo, data_inicio, data_fim) VALUES (?, ?, ?, ?)";
    
    try (Connection conn = Conexao.getConnection();
         PreparedStatement stmt = conn.prepareStatement(query)) {
        
        stmt.setInt(1, cliente.getIdCliente());
        stmt.setInt(2, veiculo.getId());
        stmt.setDate(3, Date.valueOf(dataInicio));
        stmt.setDate(4, Date.valueOf(dataFim));
        
        stmt.executeUpdate();
        System.out.println("Aluguel cadastrado com sucesso!");

        } catch (SQLException e) {
            System.out.println("Erro ao inserir aluguel: " + e.getMessage());
        }
    }

    public static void editarAluguel(int id, Cliente cliente, Veiculo veiculo, LocalDate dataInicio, LocalDate dataFim) {
        String query = "UPDATE aluguel SET idCliente = ?, id_veiculo = ?, data_inicio = ?, data_fim = ? WHERE id = ?";

        try (Connection connection = Conexao.getConnection(); PreparedStatement stmt = connection.prepareStatement(query)) {

            stmt.setInt(1, cliente.getIdCliente()); // ID do cliente
            stmt.setInt(2, veiculo.getId()); // ID do veículo
            stmt.setDate(3, Date.valueOf(dataInicio));
            stmt.setDate(4, Date.valueOf(dataFim));
            stmt.setInt(5, id);

            int rowsUpdated = stmt.executeUpdate();

            if (rowsUpdated > 0) {
                System.out.println("Aluguel atualizado com sucesso!");
            } else {
                System.out.println("Aluguel não encontrado.");
            }

        } catch (SQLException e) {
            System.out.println("Erro ao editar aluguel: " + e.getMessage());
        }
    }

    public static void devolucaoAluguel(int id) {
        String query = "UPDATE aluguel SET data_fim = ? WHERE id = ?";

        try (Connection connection = Conexao.getConnection(); PreparedStatement stmt = connection.prepareStatement(query)) {

            stmt.setDate(1, Date.valueOf(LocalDate.now())); // Data de devolução
            stmt.setInt(2, id);

            int rowsUpdated = stmt.executeUpdate();

            if (rowsUpdated > 0) {
                System.out.println("Veículo devolvido com sucesso! Total a pagar: R$" + calcularTotalAluguel(id));
            } else {
                System.out.println("Aluguel não encontrado.");
            }

        } catch (SQLException e) {
            System.out.println("Erro ao realizar devolução: " + e.getMessage());
        }
    }

    public static void listarAlugueis() {
        String query = "SELECT a.id, a.data_inicio, a.data_fim, c.nome AS cliente, v.modelo AS veiculo "
                + "FROM aluguel a "
                + "JOIN cliente c ON a.idCliente = c.idCliente "
                + "JOIN veiculo v ON a.id_veiculo = v.id";

        try (Connection connection = Conexao.getConnection(); Statement stmt = connection.createStatement(); ResultSet rs = stmt.executeQuery(query)) {

            System.out.println("Lista de Aluguéis:");
            while (rs.next()) {
                int id = rs.getInt("id");
                String cliente = rs.getString("cliente");
                String veiculo = rs.getString("veiculo");
                LocalDate dataInicio = rs.getDate("data_inicio").toLocalDate();
                LocalDate dataFim = rs.getDate("data_fim") != null ? rs.getDate("data_fim").toLocalDate() : null;

                System.out.printf("ID: %d | Cliente: %s | Veículo: %s | Início: %s | Fim: %s\n",
                        id, cliente, veiculo, dataInicio, dataFim != null ? dataFim : "Em andamento");
            }

        } catch (SQLException e) {
            System.out.println("Erro ao listar aluguéis: " + e.getMessage());
        }
    }

    // Método auxiliar para calcular o total de aluguel com base no ID
    private static double calcularTotalAluguel(int id) {
        String query = "SELECT a.data_inicio, a.data_fim, v.precoDiario FROM aluguel a "
                + "JOIN veiculo v ON a.id_veiculo = v.id WHERE a.id = ?";
        try (Connection connection = Conexao.getConnection(); PreparedStatement stmt = connection.prepareStatement(query)) {

            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                LocalDate dataInicio = rs.getDate("data_inicio").toLocalDate();
                LocalDate dataFim = rs.getDate("data_fim") != null ? rs.getDate("data_fim").toLocalDate() : LocalDate.now();
                double precoDiario = rs.getDouble("precoDiario");
                long dias = ChronoUnit.DAYS.between(dataInicio, dataFim);
                return dias * precoDiario;
            }

        } catch (SQLException e) {
            System.out.println("Erro ao calcular total do aluguel: " + e.getMessage());
        }
        return 0.0;
    }

    public int getId() {
        return id;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public Veiculo getVeiculo() {
        return veiculo;
    }

    public static Aluguel buscarPorId(int id) {
        String query = "SELECT a.id, a.idCliente, a.id_veiculo, a.data_inicio, a.data_fim, "
                + "c.nome AS nome_cliente, c.cpf, c.telefone, c.email, "
                + "v.placa, v.marca, v.modelo AS modelo_veiculo, v.ano, v.precoDiario, v.cor, v.quilometragem, "
                + "CASE "
                + "   WHEN EXISTS (SELECT 1 FROM carro WHERE id = v.id) THEN 'CARRO' "
                + "   WHEN EXISTS (SELECT 1 FROM moto WHERE id = v.id) THEN 'MOTO' "
                + "   ELSE NULL "
                + "END AS tipo_veiculo "
                + "FROM aluguel a "
                + "JOIN cliente c ON a.idCliente = c.idCliente " // Corrigido para "cliente" (singular)
                + "JOIN veiculo v ON a.id_veiculo = v.id "
                + "WHERE a.id = ?";

        try (Connection conn = Conexao.getConnection(); PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                Cliente cliente = new Cliente(
                        rs.getInt("idCliente"),
                        rs.getString("nome_cliente"),
                        rs.getString("cpf"),
                        rs.getString("telefone"),
                        rs.getString("email")
                );

                int veiculoId = rs.getInt("id_veiculo");
                String tipoVeiculo = rs.getString("tipo_veiculo");
                Veiculo veiculo = null;

                if ("CARRO".equals(tipoVeiculo)) {
                    String carroQuery = "SELECT * FROM veiculo v JOIN carro c ON v.id = c.id WHERE v.id = ?";
                    PreparedStatement carroStmt = conn.prepareStatement(carroQuery);
                    carroStmt.setInt(1, veiculoId);
                    ResultSet carroRs = carroStmt.executeQuery();
                    if (carroRs.next()) {
                        veiculo = new Carro(
                                carroRs.getInt("id"),
                                carroRs.getString("placa"),
                                carroRs.getString("marca"),
                                carroRs.getString("modelo"),
                                carroRs.getInt("ano"),
                                carroRs.getDouble("precoDiario"),
                                carroRs.getString("cor"),
                                carroRs.getInt("quilometragem"),
                                carroRs.getInt("portas"),
                                carroRs.getString("tipoCombustivel"),
                                carroRs.getBoolean("arCondicionado"),
                                carroRs.getString("tipoCambio")
                        );
                    }
                } else if ("MOTO".equals(tipoVeiculo)) {
                    String motoQuery = "SELECT * FROM veiculo v JOIN moto m ON v.id = m.id WHERE v.id = ?";
                    PreparedStatement motoStmt = conn.prepareStatement(motoQuery);
                    motoStmt.setInt(1, veiculoId);
                    ResultSet motoRs = motoStmt.executeQuery();
                    if (motoRs.next()) {
                        veiculo = new Moto(
                                motoRs.getInt("id"),
                                motoRs.getString("placa"),
                                motoRs.getString("marca"),
                                motoRs.getString("modelo"),
                                motoRs.getInt("ano"),
                                motoRs.getDouble("precoDiario"),
                                motoRs.getString("cor"),
                                motoRs.getInt("quilometragem"),
                                motoRs.getInt("cilindradas"),
                                motoRs.getBoolean("partidaEletrica"),
                                motoRs.getString("tipoMoto"),
                                motoRs.getString("tipoFreio")
                        );
                    }
                }

                LocalDate dataInicio = rs.getDate("data_inicio").toLocalDate();
                LocalDate dataFim = rs.getDate("data_fim") != null ? rs.getDate("data_fim").toLocalDate() : null;

                if (veiculo != null) {
                    return new Aluguel(id, cliente, veiculo, dataInicio, dataFim);
                } else {
                    System.out.println("Veículo associado ao aluguel ID " + id + " não encontrado ou tipo de veículo desconhecido.");
                    return null;
                }
            } else {
                System.out.println("Aluguel com ID " + id + " não encontrado.");
                return null;
            }
        } catch (SQLException e) {
            System.out.println("Erro ao buscar aluguel por ID: " + e.getMessage());
            return null;
        }

    }
}
