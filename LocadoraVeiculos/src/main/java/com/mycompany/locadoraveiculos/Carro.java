package com.mycompany.locadoraveiculos;

public class Carro extends Veiculo {

    private int numPortas;
    private String tipoCombustivel;
    private boolean arCondicionado;
    private String cambio;

    public Carro(int id, String modelo, String placa, int ano, double precoDiario,
            int numPortas, String tipoCombustivel, boolean arCondicionado, String cambio) {
        super(id, modelo, placa, ano, precoDiario);
        this.numPortas = numPortas;
        this.tipoCombustivel = tipoCombustivel;
        this.arCondicionado = arCondicionado;
        this.cambio = cambio;
    }

    @Override
    public double calcularSeguro() {
        // Seguro para carro: 20% do preço diário + taxa fixa de R$15
        return getPrecoDiario() * 0.20 + 15;
    }

    // Getters e Setters específicos
    public int getNumPortas() {
        return numPortas;
    }

    public String getTipoCombustivel() {
        return tipoCombustivel;
    }

    public boolean isArCondicionado() {
        return arCondicionado;
    }

    public String getCambio() {
        return cambio;
    }

    public void setNumPortas(int numPortas) {
        this.numPortas = numPortas;
    }

    public void setTipoCombustivel(String tipoCombustivel) {
        this.tipoCombustivel = tipoCombustivel;
    }

    public void setArCondicionado(boolean arCondicionado) {
        this.arCondicionado = arCondicionado;
    }

    public void setCambio(String cambio) {
        this.cambio = cambio;
    }

    
}


