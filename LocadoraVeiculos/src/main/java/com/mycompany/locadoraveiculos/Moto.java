package com.mycompany.locadoraveiculos;

public class Moto extends Veiculo {

    private int cilindradas;
    private String tipoMoto; // esportiva, custom, etc.
    private boolean partidaEletrica;
    private String tipoFreio;

    public Moto(int id, String modelo, String placa, int ano, double precoDiario,
            int cilindradas, String tipoMoto, boolean partidaEletrica, String tipoFreio) {
        super(id, modelo, placa, ano, precoDiario);
        this.cilindradas = cilindradas;
        this.tipoMoto = tipoMoto;
        this.partidaEletrica = partidaEletrica;
        this.tipoFreio = tipoFreio;
    }

    @Override
    public double calcularSeguro() {
        // Seguro para moto: 15% do preço diário + taxa fixa de R$10
        return getPrecoDiario() * 0.15 + 10;
    }

    // Getters e Setters específicos
    public int getCilindradas() {
        return cilindradas;
    }

    public String getTipoMoto() {
        return tipoMoto;
    }

    public boolean isPartidaEletrica() {
        return partidaEletrica;
    }

    public String getTipoFreio() {
        return tipoFreio;
    }

    public void setCilindradas(int cilindradas) {
        this.cilindradas = cilindradas;
    }

    public void setTipoMoto(String tipoMoto) {
        this.tipoMoto = tipoMoto;
    }

    public void setPartidaEletrica(boolean partidaEletrica) {
        this.partidaEletrica = partidaEletrica;
    }

    public void setTipoFreio(String tipoFreio) {
        this.tipoFreio = tipoFreio;
    }

    
}

