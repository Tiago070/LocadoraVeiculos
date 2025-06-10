package com.mycompany.locadoraveiculos;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class TelaInicial extends JFrame {

    public TelaInicial() {

        setTitle("Sistema de Locação de Veículos");
        setBounds(300, 200, 400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(null);
        setLocationRelativeTo(null);

        JLabel lblTitulo = new JLabel("Sistema de Locação de Veículos", SwingConstants.CENTER);
        lblTitulo.setBounds(50, 20, 300, 30);
        add(lblTitulo);

        JButton btnClientes = new JButton("Cadastro de Clientes");
        btnClientes.setBounds(100, 70, 200, 40);
        add(btnClientes);

        JButton btnCarros = new JButton("Cadastro de Carros");
        btnCarros.setBounds(100, 120, 200, 40);
        add(btnCarros);

        JButton btnMotos = new JButton("Cadastro de Motos");
        btnMotos.setBounds(100, 170, 200, 40);
        add(btnMotos);

        btnClientes.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                TelaCliente telaCliente = new TelaCliente();
                telaCliente.setVisible(true);

                dispose();
            }
        });

        ActionListener naoImplementadoListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(
                        TelaInicial.this,
                        "Esta funcionalidade ainda não foi implementada.",
                        "Aviso",
                        JOptionPane.INFORMATION_MESSAGE
                );
            }
        };

        btnCarros.addActionListener(naoImplementadoListener);
        btnMotos.addActionListener(naoImplementadoListener);
    }
}
