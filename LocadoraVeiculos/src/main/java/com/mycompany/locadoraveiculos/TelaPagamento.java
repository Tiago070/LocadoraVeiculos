// Pacote da sua aplicação
package com.mycompany.locadoraveiculos;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

/**
 * Classe de TELA (VIEW) para Pagamento.
 */
public class TelaPagamento extends JFrame {

    private JComboBox<Aluguel> comboAlugueis;
    private JComboBox<String> comboFormaPagamento;
    private JLabel lblValorAPagar;
    private JButton btnConfirmarPagamento;

    private JTable tabelaPagamentos;
    private DefaultTableModel modeloTabela;

    private PAluguel persistenciaAluguel;
    private PPagamento persistenciaPagamento;

    public TelaPagamento() {
        persistenciaAluguel = new PAluguel();
        persistenciaPagamento = new PPagamento();

        setTitle("Registro de Pagamentos");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        // --- PAINEL DO FORMULÁRIO ---
        JPanel painelForm = new JPanel(new GridLayout(0, 2, 10, 10));
        painelForm.setBorder(BorderFactory.createTitledBorder("Registrar Pagamento de Aluguel Finalizado"));

        painelForm.add(new JLabel("Selecione o Aluguel:"));
        comboAlugueis = new JComboBox<>();
        painelForm.add(comboAlugueis);

        painelForm.add(new JLabel("Valor a Pagar (R$):"));
        lblValorAPagar = new JLabel("0.00");
        lblValorAPagar.setFont(new Font("Arial", Font.BOLD, 16));
        painelForm.add(lblValorAPagar);

        painelForm.add(new JLabel("Forma de Pagamento:"));
        comboFormaPagamento = new JComboBox<>(new String[]{"Cartão de Crédito", "Cartão de Débito", "PIX", "Dinheiro"});
        painelForm.add(comboFormaPagamento);

        btnConfirmarPagamento = new JButton("Confirmar Pagamento");
        painelForm.add(new JLabel()); // Espaçador
        painelForm.add(btnConfirmarPagamento);

        // --- PAINEL DA TABELA ---
        JPanel painelTabela = new JPanel(new BorderLayout(10, 10));
        painelTabela.setBorder(BorderFactory.createTitledBorder("Histórico de Pagamentos"));

        String[] colunas = {"ID Pag.", "Cliente", "Veículo", "Valor Pago (R$)", "Forma", "Data Pag."};
        modeloTabela = new DefaultTableModel(colunas, 0);
        tabelaPagamentos = new JTable(modeloTabela);
        painelTabela.add(new JScrollPane(tabelaPagamentos), BorderLayout.CENTER);

        // Adiciona os painéis à janela
        add(painelForm, BorderLayout.NORTH);
        add(painelTabela, BorderLayout.CENTER);

        adicionarListeners();
        carregarAlugueisPendentes();
        atualizarHistorico();
    }

    private void adicionarListeners() {
        // Atualiza o valor a pagar quando um aluguel é selecionado
        comboAlugueis.addItemListener(e -> {
            if (e.getStateChange() == java.awt.event.ItemEvent.SELECTED) {
                Aluguel aluguelSelecionado = (Aluguel) comboAlugueis.getSelectedItem();
                if (aluguelSelecionado != null) {
                    lblValorAPagar.setText(String.format("%.2f", aluguelSelecionado.calcularTotalAluguel()));
                } else {
                    lblValorAPagar.setText("0.00");
                }
            }
        });

        btnConfirmarPagamento.addActionListener(e -> registrarPagamento());
    }

    private void carregarAlugueisPendentes() {
        comboAlugueis.removeAllItems();
        ArrayList<Aluguel> alugueis = persistenciaAluguel.consultarAlugueisParaPagamento();
        comboAlugueis.addItem(null); // Opção vazia
        for (Aluguel a : alugueis) {
            comboAlugueis.addItem(a);
        }
    }

    private void atualizarHistorico() {
        modeloTabela.setRowCount(0);
        ArrayList<Object[]> pagamentos = persistenciaPagamento.consultarHistoricoPagamentos();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        for (Object[] p : pagamentos) {
            // Converte a data para o formato correto antes de adicionar
            LocalDate data = (LocalDate) p[5];
            p[5] = data.format(formatter);
            // Formata o valor
            p[3] = String.format("%.2f", p[3]);
            modeloTabela.addRow(p);
        }
    }

    private void registrarPagamento() {
        Aluguel aluguelSelecionado = (Aluguel) comboAlugueis.getSelectedItem();
        if (aluguelSelecionado == null) {
            JOptionPane.showMessageDialog(this, "Selecione um aluguel para registrar o pagamento.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String formaPagamento = (String) comboFormaPagamento.getSelectedItem();

        Pagamento novoPagamento = new Pagamento(0, aluguelSelecionado, formaPagamento, LocalDate.now());

        String resultado = persistenciaPagamento.incluirPagamento(novoPagamento);
        JOptionPane.showMessageDialog(this, resultado);

        // Atualiza a tela
        carregarAlugueisPendentes();
        atualizarHistorico();
        lblValorAPagar.setText("0.00");
    }
}