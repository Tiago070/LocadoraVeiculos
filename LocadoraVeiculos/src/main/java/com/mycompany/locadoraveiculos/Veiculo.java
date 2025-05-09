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

    // Método para buscar veículo por ID (unificado para Carro e Moto)
    public static Veiculo buscarPorId(int id) {
        try (Connection conn = Conexao.getConnection()) {
            // Tentar buscar como Carro
            String sqlCarro = "SELECT v.*, c.portas, c.tipoCombustivel, c.arCondicionado, c.tipoCambio " +
                              "FROM veiculo v JOIN carro c ON v.id = c.id WHERE v.id = ?";
            PreparedStatement stmtCarro = conn.prepareStatement(sqlCarro);
            stmtCarro.setInt(1, id);
            ResultSet rsCarro = stmtCarro.executeQuery();
            if (rsCarro.next()) {
                return new Carro(
                        rsCarro.getInt("id"),
                        rsCarro.getString("placa"),
                        rsCarro.getString("marca"),
                        rsCarro.getString("modelo"),
                        rsCarro.getInt("ano"),
                        rsCarro.getDouble("precoDiario"),
                        rsCarro.getString("cor"),
                        rsCarro.getInt("quilometragem"),
                        rsCarro.getInt("portas"),
                        rsCarro.getString("tipoCombustivel"),
                        rsCarro.getBoolean("arCondicionado"),
                        rsCarro.getString("tipoCambio")
                );
            }

            // Tentar buscar como Moto
            String sqlMoto = "SELECT v.*, m.cilindradas, m.partidaEletrica, m.tipoMoto, m.tipoFreio " +
                            "FROM veiculo v JOIN moto m ON v.id = m.id WHERE v.id = ?";
            PreparedStatement stmtMoto = conn.prepareStatement(sqlMoto);
            stmtMoto.setInt(1, id);
            ResultSet rsMoto = stmtMoto.executeQuery();
            if (rsMoto.next()) {
                return new Moto(
                        rsMoto.getInt("id"),
                        rsMoto.getString("placa"),
                        rsMoto.getString("marca"),
                        rsMoto.getString("modelo"),
                        rsMoto.getInt("ano"),
                        rsMoto.getDouble("precoDiario"),
                        rsMoto.getString("cor"),
                        rsMoto.getInt("quilometragem"),
                        rsMoto.getInt("cilindradas"),
                        rsMoto.getBoolean("partidaEletrica"),
                        rsMoto.getString("tipoMoto"),
                        rsMoto.getString("tipoFreio")
                );
            }
        } catch (SQLException e) {
            System.out.println("Erro ao buscar veículo por ID: " + e.getMessage());
        }
        System.out.println("Veículo com ID " + id + " não encontrado.");
        return null;
    }
}