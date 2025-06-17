package com.mycompany.locadoraveiculos;

import java.time.LocalDate;


public class Pagamento {

    private int id;
    private Aluguel aluguel;
    private double valorPago;
    private String formaPagamento;
    private LocalDate dataPagamento;

    // Construtor padrão
    public Pagamento() {
    }

    // Construtor principal
    public Pagamento(int id, Aluguel aluguel, String formaPagamento, LocalDate dataPagamento) {
        this.id = id;
        this.aluguel = aluguel;
        this.valorPago = aluguel.calcularTotalAluguel(); // Calcula o valor no momento da criação
        this.formaPagamento = formaPagamento;
        this.dataPagamento = dataPagamento;
    }

    // Getters e Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Aluguel getAluguel() {
        return aluguel;
    }

    public void setAluguel(Aluguel aluguel) {
        this.aluguel = aluguel;
    }

    public double getValorPago() {
        return valorPago;
    }

    public void setValorPago(double valorPago) {
        this.valorPago = valorPago;
    }

    public String getFormaPagamento() {
        return formaPagamento;
    }

    public void setFormaPagamento(String formaPagamento) {
        this.formaPagamento = formaPagamento;
    }

    public LocalDate getDataPagamento() {
        return dataPagamento;
    }

    public void setDataPagamento(LocalDate dataPagamento) {
        this.dataPagamento = dataPagamento;
    }
}