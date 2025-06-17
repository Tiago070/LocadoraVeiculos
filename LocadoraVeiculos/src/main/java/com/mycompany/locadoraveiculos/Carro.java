package com.mycompany.locadoraveiculos;


public class Carro extends Veiculo {

    private int numPortas;
    private String tipoCombustivel;
    private boolean arCondicionado;
    private String cambio;

    
    public Carro() {
        super();
    }

    
    public Carro(int id, String placa, String marca, String modelo, int anoFabricacao,
            double precoDiario, String cor, int quilometragem, int numPortas,
            String tipoCombustivel, boolean arCondicionado, String cambio) {

        super(id, modelo, placa, marca, anoFabricacao, quilometragem, precoDiario);

        this.setCor(cor); 
        this.numPortas = numPortas;
        this.tipoCombustivel = tipoCombustivel;
        this.arCondicionado = arCondicionado;
        this.cambio = cambio;
    }

    // Getters e Setters
    public int getNumPortas() {
        return numPortas;
    }

    public void setNumPortas(int numPortas) {
        this.numPortas = numPortas;
    }

    public String getTipoCombustivel() {
        return tipoCombustivel;
    }

    public void setTipoCombustivel(String tipoCombustivel) {
        this.tipoCombustivel = tipoCombustivel;
    }

    public boolean isArCondicionado() {
        return arCondicionado;
    }

    public void setArCondicionado(boolean arCondicionado) {
        this.arCondicionado = arCondicionado;
    }

    public String getCambio() {
        return cambio;
    }

    public void setCambio(String cambio) {
        this.cambio = cambio;
    }

    @Override
    public double calcularSeguro() {
        return this.getPrecoDiario() * 0.15;
    }
    
    @Override
    public String toString() {
        return this.getModelo() + " [" + this.getPlaca() + "]";
    }
}