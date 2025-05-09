package com.mycompany.locadoraveiculos;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public abstract class Veiculo {

    private int id;
    private String modelo;
    private String placa;
    private String marca;
    private String cor;
    private int ano;
    private int quilometragem;
    private double precoDiario;

    public Veiculo(int id, String modelo, String placa,String marca, int ano, int quilometragem, double precoDiario) {
        this.id = id;
        this.modelo = modelo;
        this.marca = marca;
        this.placa = placa;
        this.ano = ano;
        this.quilometragem = quilometragem;
        this.precoDiario = precoDiario;
    }

    // Método abstrato que será implementado pelas classes filhas
    public abstract double calcularSeguro();

    public String getCor() {
        return cor;
    }

    public void setCor(String cor) {
        this.cor = cor;
    }

    // Getters e Setters
    public int getId() {
        return id;
    }

    public String getMarca() {
        return marca;
    }

    public int getQuilometragem() {
        return quilometragem;
    }

    public void setQuilometragem(int quilometragem) {
        this.quilometragem = quilometragem;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public String getModelo() {
        return modelo;
    }

    public String getPlaca() {
        return placa;
    }

    public int getAno() {
        return ano;
    }

    public double getPrecoDiario() {
        return precoDiario;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setModelo(String modelo) {
        this.modelo = modelo;
    }

    public void setPlaca(String placa) {
        this.placa = placa;
    }

    public void setAno(int ano) {
        this.ano = ano;
    }

    public void setPrecoDiario(double precoDiario) {
        this.precoDiario = precoDiario;
    }

    public static Veiculo buscarPorId(int id) {
        String sql = "SELECT * FROM veiculo WHERE id = ?";

        try (Connection conn = Conexao.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                String tipo = rs.getString("tipo"); // "carro" ou "moto"

                // Dados comuns
                String placa = rs.getString("placa");
                String marca = rs.getString("marca");
                String modelo = rs.getString("modelo");
                int ano = rs.getInt("ano");
                int quilometragem = rs.getInt("quilometragem");
                double precoDiario = rs.getDouble("preco_diario");
                String cor = rs.getString("cor");

                if (tipo.equalsIgnoreCase("carro")) {
                    int numPortas = rs.getInt("num_portas");
                    String tipoCombustivel = rs.getString("tipo_combustivel");
                    boolean arCondicionado = rs.getBoolean("ar_condicionado");
                    String cambio = rs.getString("cambio");

                    return new Carro(
                            id, placa, marca, modelo, ano,
                            precoDiario, cor, quilometragem,
                            numPortas, tipoCombustivel, arCondicionado, cambio
                    );

                } else if (tipo.equalsIgnoreCase("moto")) {
                    int cilindradas = rs.getInt("cilindradas");
                    boolean partidaEletrica = rs.getBoolean("partida_eletrica");
                    String tipoMoto = rs.getString("tipo_moto");
                    String tipoFreio = rs.getString("tipo_freio");

                    return new Moto(
                            id, placa, marca, modelo, ano,
                            precoDiario, cor, quilometragem,
                            cilindradas, partidaEletrica, tipoMoto, tipoFreio
                    );
                }
            }

        } catch (SQLException e) {
            System.out.println("Erro ao buscar veículo por ID: " + e.getMessage());
        }

        return null;

    }
}
