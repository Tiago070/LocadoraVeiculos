package com.mycompany.locadoraveiculos;

/**
 * Classe de ENTIDADE (Abstrata). Representa os dados comuns a todos os
 * veículos.
 */
public abstract class Veiculo {

    private int id;
    private String modelo;
    private String placa;
    private String marca;
    private String cor;
    private int ano;
    private int quilometragem;
    private double precoDiario;

    // Construtor padrão
    public Veiculo() {
    }

    // Construtor completo
    public Veiculo(int id, String modelo, String placa, String marca, int ano, int quilometragem, double precoDiario) {
        this.id = id;
        this.modelo = modelo;
        this.marca = marca;
        this.placa = placa;
        this.ano = ano;
        this.quilometragem = quilometragem;
        this.precoDiario = precoDiario;
    }

    // Método abstrato
    public abstract double calcularSeguro();
    
     @Override
    public String toString() {
        // Retorna uma combinação do modelo e da placa para fácil identificação
        return this.getModelo() + " [" + this.getPlaca() + "]";
    }

    // Getters e Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getModelo() {
        return modelo;
    }

    public void setModelo(String modelo) {
        this.modelo = modelo;
    }

    public String getPlaca() {
        return placa;
    }

    public void setPlaca(String placa) {
        this.placa = placa;
    }

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public String getCor() {
        return cor;
    }

    public void setCor(String cor) {
        this.cor = cor;
    }

    public int getAno() {
        return ano;
    }

    public void setAno(int ano) {
        this.ano = ano;
    }

    public int getQuilometragem() {
        return quilometragem;
    }

    public void setQuilometragem(int quilometragem) {
        this.quilometragem = quilometragem;
    }

    public double getPrecoDiario() {
        return precoDiario;
    }

    public void setPrecoDiario(double precoDiario) {
        this.precoDiario = precoDiario;
    }
}