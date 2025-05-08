package com.mycompany.locadoraveiculos;

import java.util.ArrayList;
import java.util.List;

public class Pagamento {
    private static int proximoId = 1;
    private static List<Pagamento> pagamentos = new ArrayList<>();

    private int id;
    private Aluguel aluguel;
    private String formaPagamento;
    private double valor;

    public Pagamento(Aluguel aluguel, String formaPagamento) {
        this.id = proximoId++;
        this.aluguel = aluguel;
        this.formaPagamento = formaPagamento;
        this.valor = aluguel.calcularTotalAluguel();
    }

    public int getId() { return id; }
    public Aluguel getAluguel() { return aluguel; }
    public String getFormaPagamento() { return formaPagamento; }
    public double getValor() { return valor; }

    public static void cadastrarPagamento(Aluguel aluguel, String formaPagamento) {
        Pagamento novo = new Pagamento(aluguel, formaPagamento);
        pagamentos.add(novo);
        System.out.println("Pagamento cadastrado com sucesso! ID: " + novo.getId());
    }

    public static void listarPagamentos() {
        if (pagamentos.isEmpty()) {
            System.out.println("Nenhum pagamento registrado.");
            return;
        }

        for (Pagamento p : pagamentos) {
            System.out.println("ID: " + p.getId() +
                               ", Cliente: " + p.getAluguel().getCliente().getNome() +
                               ", Veículo: " + p.getAluguel().getVeiculo().getModelo() +
                               ", Forma: " + p.getFormaPagamento() +
                               ", Valor: R$ " + p.getValor());
        }
    }

    public static Pagamento buscarPorId(int id) {
        for (Pagamento p : pagamentos) {
            if (p.getId() == id) return p;
        }
        return null;
    }
}