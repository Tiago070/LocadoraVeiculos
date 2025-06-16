
package com.mycompany.locadoraveiculos;

/**
 * Classe de ENTIDADE. Representa os dados de uma Moto, herdando de Veiculo.
 */
public class Moto extends Veiculo {

    private int cilindradas;
    private String tipoMoto;
    private boolean partidaEletrica;
    private String tipoFreio;

    public Moto(int id, String placa, String marca, String modelo, int anoFabricacao,
            double precoDiario, String cor, int quilometragem, int cilindradas,
            boolean partidaEletrica, String tipoMoto, String tipoFreio) {
        super(id, modelo, placa, marca, anoFabricacao, quilometragem, precoDiario);
        this.setMarca(marca);
        this.setCor(cor);
        this.setQuilometragem(quilometragem);
        this.setPrecoDiario(precoDiario);
        this.cilindradas = cilindradas;
        this.tipoMoto = tipoMoto;
        this.partidaEletrica = partidaEletrica;
        this.tipoFreio = tipoFreio;
    }

    // Construtor padrão
    public Moto() {
        super();
    }

    // Getters e Setters
    public int getCilindradas() {
        return cilindradas;
    }

    public void setCilindradas(int cilindradas) {
        this.cilindradas = cilindradas;
    }

    public String getTipoMoto() {
        return tipoMoto;
    }

    public void setTipoMoto(String tipoMoto) {
        this.tipoMoto = tipoMoto;
    }

    public boolean isPartidaEletrica() {
        return partidaEletrica;
    }

    public void setPartidaEletrica(boolean partidaEletrica) {
        this.partidaEletrica = partidaEletrica;
    }

    public String getTipoFreio() {
        return tipoFreio;
    }

    public void setTipoFreio(String tipoFreio) {
        this.tipoFreio = tipoFreio;
    }

    @Override
    public double calcularSeguro() {
        return this.getPrecoDiario() * 0.10;
    }
    @Override
    public String toString() {
        // Retorna uma combinação do modelo e da placa para fácil identificação
        return this.getModelo() + " [" + this.getPlaca() + "]";
    }
}