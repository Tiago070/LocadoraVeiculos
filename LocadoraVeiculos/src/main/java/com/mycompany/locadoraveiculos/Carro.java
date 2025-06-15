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

        // Chama o construtor da classe pai (Veiculo) para os atributos comuns
        super(id, modelo, placa, marca, anoFabricacao, quilometragem, precoDiario);

        // Define os atributos específicos de Carro
        this.setCor(cor); // O campo cor está em Veiculo, mas é bom garantir
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
        // Retorna uma combinação do modelo e da placa para fácil identificação
        return this.getModelo() + " [" + this.getPlaca() + "]";
    }
}