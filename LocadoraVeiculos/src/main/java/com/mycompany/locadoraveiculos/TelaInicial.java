package com.mycompany.locadoraveiculos;

import javax.swing.*;
import java.awt.*;

public class TelaInicial extends JFrame {

    public TelaInicial() {
        setTitle("Sistema de Locação de Veículos");
        setSize(400, 450); // Aumentei a altura novamente
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel painel = new JPanel(new GridLayout(6, 1, 10, 10)); // 6 linhas
        painel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel lblTitulo = new JLabel("Sistema de Locação", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 18));

        JButton btnClientes = new JButton("Cadastro de Clientes");
        JButton btnCarros = new JButton("Cadastro de Carros");
        JButton btnMotos = new JButton("Cadastro de Motos");
        JButton btnAlugueis = new JButton("Gerenciar Aluguéis");
        JButton btnPagamentos = new JButton("Registrar Pagamento"); // NOVO BOTÃO

        painel.add(lblTitulo);
        painel.add(btnClientes);
        painel.add(btnCarros);
        painel.add(btnMotos);
        painel.add(btnAlugueis);
        painel.add(btnPagamentos); // Adiciona o botão ao painel

        add(painel);

        // Ações
        btnClientes.addActionListener(e -> new TelaCliente().setVisible(true));
        btnCarros.addActionListener(e -> new TelaCarro().setVisible(true));
        btnMotos.addActionListener(e -> new TelaMoto().setVisible(true));
        btnAlugueis.addActionListener(e -> new TelaAluguel().setVisible(true));
        // AÇÃO PARA O NOVO BOTÃO
        btnPagamentos.addActionListener(e -> new TelaPagamento().setVisible(true));
    }
}