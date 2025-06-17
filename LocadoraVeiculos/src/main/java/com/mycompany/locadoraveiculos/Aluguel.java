
package com.mycompany.locadoraveiculos;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;


public class Aluguel {

    private int id;
    private Cliente cliente;
    private Veiculo veiculo;
    private LocalDate dataInicio;
    private LocalDate dataFim;

    // Construtor padrão
    public Aluguel() {
    }

    // Construtor completo
    public Aluguel(int id, Cliente cliente, Veiculo veiculo, LocalDate dataInicio, LocalDate dataFim) {
        this.id = id;
        this.cliente = cliente;
        this.veiculo = veiculo;
        this.dataInicio = dataInicio;
        this.dataFim = dataFim;
    }

    public double calcularTotalAluguel() {
        if (dataInicio == null || dataFim == null || veiculo == null) {
            return 0.0;
        }
        long dias = ChronoUnit.DAYS.between(dataInicio, dataFim) + 1;
        return dias * veiculo.getPrecoDiario();
    }

    @Override
    public String toString() {
        return "ID: " + id + " | Cliente: " + cliente.getNome() + " | Veículo: " + veiculo.getModelo();
    }

    // Getters e Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public Veiculo getVeiculo() {
        return veiculo;
    }

    public void setVeiculo(Veiculo veiculo) {
        this.veiculo = veiculo;
    }

    public LocalDate getDataInicio() {
        return dataInicio;
    }

    public void setDataInicio(LocalDate dataInicio) {
        this.dataInicio = dataInicio;
    }

    public LocalDate getDataFim() {
        return dataFim;
    }

    public void setDataFim(LocalDate dataFim) {
        this.dataFim = dataFim;
    }
}