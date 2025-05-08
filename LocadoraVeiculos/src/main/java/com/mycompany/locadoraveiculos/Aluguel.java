package com.mycompany.locadoraveiculos;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

public class Aluguel {
    private int id;
    private Cliente cliente;
    private Veiculo veiculo;
    private LocalDate dataInicio;
    private LocalDate dataFim;

    private static List<Aluguel> listaAlugueis = new ArrayList<>();
    private static int proximoId = 1;

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
        return "Aluguel ID: " + id + " | Cliente: " + cliente.getNome() + " | Veículo: " + veiculo.getModelo() +
               " | Início: " + dataInicio + " | Fim: " + dataFim + " | Total: R$" + calcularTotal();
    }

    // Métodos de controle

    public static void inserir(Cliente cliente, Veiculo veiculo, LocalDate dataInicio, LocalDate dataFim) {
        Aluguel novo = new Aluguel(proximoId++, cliente, veiculo, dataInicio, dataFim);
        listaAlugueis.add(novo);
        System.out.println("✅ Aluguel inserido com sucesso!");
    }

    public static void editar(int id, Cliente cliente, Veiculo veiculo, LocalDate dataInicio, LocalDate dataFim) {
        for (Aluguel a : listaAlugueis) {
            if (a.id == id) {
                a.cliente = cliente;
                a.veiculo = veiculo;
                a.dataInicio = dataInicio;
                a.dataFim = dataFim;
                System.out.println("✏️ Aluguel editado com sucesso!");
                return;
            }
        }
        System.out.println("❌ Aluguel com ID " + id + " não encontrado.");
    }

    public static void apagar(int id) {
        Aluguel encontrado = null;
        for (Aluguel a : listaAlugueis) {
            if (a.id == id) {
                encontrado = a;
                break;
            }
        }
        if (encontrado != null) {
            listaAlugueis.remove(encontrado);
            System.out.println("🗑️ Aluguel removido com sucesso!");
        } else {
            System.out.println("❌ Aluguel com ID " + id + " não encontrado.");
        }
    }

    public static void listar() {
        if (listaAlugueis.isEmpty()) {
            System.out.println("📭 Nenhum aluguel cadastrado.");
        } else {
            System.out.println("📋 Lista de Aluguéis:");
            for (Aluguel a : listaAlugueis) {
                System.out.println(a);
            }
        }
    }

    // Getters (se precisar)
    public int getId() {
        return id;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public Veiculo getVeiculo() {
        return veiculo;
    }

    public LocalDate getDataInicio() {
        return dataInicio;
    }

    public LocalDate getDataFim() {
        return dataFim;
    }
}
