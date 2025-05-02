package com.mycompany.locadoraveiculos;

public abstract class Veiculo {
    private int id;
    private String modelo;
    private String placa;
    private int ano;
    private double precoDiario;

    public Veiculo(int id, String modelo, String placa, int ano, double precoDiario) {
        this.id = id;
        this.modelo = modelo;
        this.placa = placa;
        this.ano = ano;
        this.precoDiario = precoDiario;
    }

    // Método abstrato que será implementado pelas classes filhas
    public abstract double calcularSeguro();

    // Getters e Setters
    public int getId() {
        return id;
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

    
}
