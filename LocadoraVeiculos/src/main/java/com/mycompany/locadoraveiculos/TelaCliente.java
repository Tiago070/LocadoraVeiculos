package com.mycompany.locadoraveiculos;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;

public class TelaCliente extends JFrame {

    private final JTextField txtNome, txtCpf, txtTelefone, txtEmail;
    private final JButton btnSalvar, btnEditar, btnExcluir, btnLimpar;
    private final JTable tabelaClientes;
    private final DefaultTableModel modeloTabela;

    // Objeto da camada de persistência
    private final PCliente persistenciaCliente;

    private Integer idClienteSelecionado = null;

    public TelaCliente() {

        this.persistenciaCliente = new PCliente();

        setTitle("Cadastro de Clientes (Arquitetura em Camadas)");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel painelFormulario = new JPanel(new GridLayout(5, 2, 10, 10));
        painelFormulario.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        painelFormulario.add(new JLabel("Nome:"));
        txtNome = new JTextField();
        painelFormulario.add(txtNome);

        painelFormulario.add(new JLabel("CPF:"));
        txtCpf = new JTextField();
        painelFormulario.add(txtCpf);

        painelFormulario.add(new JLabel("Telefone:"));
        txtTelefone = new JTextField();
        painelFormulario.add(txtTelefone);

        painelFormulario.add(new JLabel("Email:"));
        txtEmail = new JTextField();
        painelFormulario.add(txtEmail);

        btnSalvar = new JButton("Salvar");
        btnEditar = new JButton("Editar");
        btnExcluir = new JButton("Excluir");
        btnLimpar = new JButton("Limpar");
        JPanel painelBotoes = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        painelBotoes.add(btnSalvar);
        painelBotoes.add(btnEditar);
        painelBotoes.add(btnExcluir);
        painelBotoes.add(btnLimpar);
        painelFormulario.add(new JLabel());
        painelFormulario.add(painelBotoes);

        String[] colunas = {"ID", "Nome", "CPF", "Telefone", "Email"};
        modeloTabela = new DefaultTableModel(colunas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tabelaClientes = new JTable(modeloTabela);
        JScrollPane scrollPane = new JScrollPane(tabelaClientes);

        add(painelFormulario, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);

        adicionarListeners();
        atualizarTabela();
    }

    private void adicionarListeners() {
        btnSalvar.addActionListener(e -> salvarCliente());
        btnEditar.addActionListener(e -> editarCliente());
        btnExcluir.addActionListener(e -> excluirCliente());
        btnLimpar.addActionListener(e -> limparCampos());
        tabelaClientes.getSelectionModel().addListSelectionListener(event -> {
            if (!event.getValueIsAdjusting() && tabelaClientes.getSelectedRow() != -1) {
                preencherFormularioPelaTabela();
            }
        });
    }

    private void preencherFormularioPelaTabela() {
        int linha = tabelaClientes.getSelectedRow();
        if (linha != -1) {
            idClienteSelecionado = (Integer) modeloTabela.getValueAt(linha, 0);
            txtNome.setText(modeloTabela.getValueAt(linha, 1).toString());
            txtCpf.setText(modeloTabela.getValueAt(linha, 2).toString());
            txtTelefone.setText(modeloTabela.getValueAt(linha, 3).toString());
            txtEmail.setText(modeloTabela.getValueAt(linha, 4).toString());
        }
    }

    private void atualizarTabela() {
        modeloTabela.setRowCount(0);
        // Pede a lista de clientes para a camada de persistência
        ArrayList<Cliente> clientes = persistenciaCliente.consultarClientes();
        for (Cliente cliente : clientes) {
            modeloTabela.addRow(new Object[]{
                cliente.getIdCliente(),
                cliente.getNome(),
                cliente.getCpf(),
                cliente.getTelefone(),
                cliente.getEmail()
            });
        }
    }

    private void salvarCliente() {
        Cliente novoCliente = new Cliente();
        novoCliente.setNome(txtNome.getText());
        novoCliente.setCpf(txtCpf.getText());
        novoCliente.setTelefone(txtTelefone.getText());
        novoCliente.setEmail(txtEmail.getText());

        // Envia o objeto para a camada de persistência e recebe a resposta
        String resultado = persistenciaCliente.incluirCliente(novoCliente);

        JOptionPane.showMessageDialog(this, resultado, "Inclusão", JOptionPane.INFORMATION_MESSAGE);

        atualizarTabela();
        limparCampos();
    }

    private void editarCliente() {
        if (idClienteSelecionado == null) {
            JOptionPane.showMessageDialog(this, "Selecione um cliente para editar.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Cliente clienteEditado = new Cliente();
        clienteEditado.setIdCliente(idClienteSelecionado);
        clienteEditado.setNome(txtNome.getText());
        clienteEditado.setCpf(txtCpf.getText());
        clienteEditado.setTelefone(txtTelefone.getText());
        clienteEditado.setEmail(txtEmail.getText());

        String resultado = persistenciaCliente.alterarCliente(clienteEditado);

        JOptionPane.showMessageDialog(this, resultado, "Alteração", JOptionPane.INFORMATION_MESSAGE);

        atualizarTabela();
        limparCampos();
    }

    private void excluirCliente() {
        if (idClienteSelecionado == null) {
            JOptionPane.showMessageDialog(this, "Selecione um cliente para excluir.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        int confirma = JOptionPane.showConfirmDialog(this, "Confirma a exclusão?", "Excluir Cliente", JOptionPane.YES_NO_OPTION);
        if (confirma == JOptionPane.YES_OPTION) {
            String resultado = persistenciaCliente.excluirCliente(idClienteSelecionado);
            JOptionPane.showMessageDialog(this, resultado, "Exclusão", JOptionPane.INFORMATION_MESSAGE);
            atualizarTabela();
            limparCampos();
        }
    }

    private void limparCampos() {
        txtNome.setText("");
        txtCpf.setText("");
        txtTelefone.setText("");
        txtEmail.setText("");
        idClienteSelecionado = null;
        tabelaClientes.clearSelection();
    }
}
