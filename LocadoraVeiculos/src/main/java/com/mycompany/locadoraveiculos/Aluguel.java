/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.locadoraveiculos;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class Aluguel {
    private int id;
    private Cliente cliente;
    private Veiculo veiculo;
    private LocalDate dataInicio;
    private LocalDate dataFim;

    public Aluguel(int id, Cliente cliente, Veiculo veiculo, LocalDate dataInicio, LocalDate dataFim) {
        this.id = id;
        this.cliente = cliente;
        this.veiculo = veiculo;
        this.dataInicio = dataInicio;
        this.dataFim = dataFim;
    }

    public double calcularTotal() {
        long dias = ChronoUnit.DAYS.between(dataInicio, dataFim);
        return dias * veiculo.getPrecoDiario();
    }

    @Override
    public String toString() {
        return "Aluguel: " + cliente.getNome() + " - " + veiculo.getModelo();
    }
}

