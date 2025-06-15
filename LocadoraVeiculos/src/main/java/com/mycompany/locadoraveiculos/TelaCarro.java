package com.mycompany.locadoraveiculos;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;

public class TelaCarro extends JFrame {

    private JTextField txtPlaca, txtMarca, txtModelo, txtAno, txtPreco, txtCor, txtKm;
    private JComboBox<Integer> comboPortas;
    private JComboBox<String> comboCombustivel, comboCambio;
    private JCheckBox checkArCondicionado;
    private JButton btnSalvar, btnEditar, btnExcluir, btnLimpar;

    private JTable tabelaCarros;
    private DefaultTableModel modeloTabela;

    private PCarro persistenciaCarro;
    private Integer idSelecionado = null;

    public TelaCarro() {
        persistenciaCarro = new PCarro();

        setTitle("Cadastro de Carros");
        setSize(1000, 700);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel painelForm = new JPanel(new GridLayout(0, 4, 10, 10));
        painelForm.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        painelForm.add(new JLabel("Placa:"));
        txtPlaca = new JTextField();
        painelForm.add(txtPlaca);
        painelForm.add(new JLabel("Marca:"));
        txtMarca = new JTextField();
        painelForm.add(txtMarca);
        painelForm.add(new JLabel("Modelo:"));
        txtModelo = new JTextField();
        painelForm.add(txtModelo);
        painelForm.add(new JLabel("Cor:"));
        txtCor = new JTextField();
        painelForm.add(txtCor);
        painelForm.add(new JLabel("Ano:"));
        txtAno = new JTextField();
        painelForm.add(txtAno);
        painelForm.add(new JLabel("Quilometragem:"));
        txtKm = new JTextField();
        painelForm.add(txtKm);
        painelForm.add(new JLabel("Preço Diário:"));
        txtPreco = new JTextField();
        painelForm.add(txtPreco);
        painelForm.add(new JLabel("Portas:"));
        comboPortas = new JComboBox<>(new Integer[]{2, 4});
        painelForm.add(comboPortas);
        painelForm.add(new JLabel("Combustível:"));
        comboCombustivel = new JComboBox<>(new String[]{"Gasolina", "Etanol", "Flex", "Diesel", "Elétrico"});
        painelForm.add(comboCombustivel);
        painelForm.add(new JLabel("Câmbio:"));
        comboCambio = new JComboBox<>(new String[]{"Manual", "Automático"});
        painelForm.add(comboCambio);
        painelForm.add(new JLabel("Ar Condicionado:"));
        checkArCondicionado = new JCheckBox();
        painelForm.add(checkArCondicionado);

        JPanel painelBotoes = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        btnSalvar = new JButton("Salvar");
        btnEditar = new JButton("Editar"); // Nome do botão atualizado
        btnExcluir = new JButton("Excluir");
        btnLimpar = new JButton("Limpar"); // Botão Limpar criado
        painelBotoes.add(btnSalvar);
        painelBotoes.add(btnEditar);
        painelBotoes.add(btnExcluir);
        painelBotoes.add(btnLimpar);

        JPanel painelSuperior = new JPanel(new BorderLayout());
        painelSuperior.add(painelForm, BorderLayout.CENTER);
        painelSuperior.add(painelBotoes, BorderLayout.SOUTH);
        add(painelSuperior, BorderLayout.NORTH);

        String[] colunas = {"ID", "Placa", "Marca", "Modelo", "Ano", "Preço", "Cor", "KM", "Portas", "Comb.", "Ar", "Câmbio"};
        modeloTabela = new DefaultTableModel(colunas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tabelaCarros = new JTable(modeloTabela);
        add(new JScrollPane(tabelaCarros), BorderLayout.CENTER);

        adicionarListeners();
        atualizarTabela();
    }

    // ATUALIZADO: Contém todas as ações de botões e da tabela
    private void adicionarListeners() {
        btnSalvar.addActionListener(e -> salvarCarro());
        btnEditar.addActionListener(e -> editarCarro());
        btnExcluir.addActionListener(e -> excluirCarro());
        btnLimpar.addActionListener(e -> limparCampos());

        // NOVO: Listener para preencher o formulário ao selecionar uma linha na tabela
        tabelaCarros.getSelectionModel().addListSelectionListener(event -> {
            if (!event.getValueIsAdjusting() && tabelaCarros.getSelectedRow() != -1) {
                preencherFormularioPelaTabela();
            }
        });
    }

    private void preencherFormularioPelaTabela() {
        int linha = tabelaCarros.getSelectedRow();
        idSelecionado = (Integer) modeloTabela.getValueAt(linha, 0);

        txtPlaca.setText(modeloTabela.getValueAt(linha, 1).toString());
        txtMarca.setText(modeloTabela.getValueAt(linha, 2).toString());
        txtModelo.setText(modeloTabela.getValueAt(linha, 3).toString());
        txtAno.setText(modeloTabela.getValueAt(linha, 4).toString());
        txtPreco.setText(modeloTabela.getValueAt(linha, 5).toString());
        txtCor.setText(modeloTabela.getValueAt(linha, 6).toString());
        txtKm.setText(modeloTabela.getValueAt(linha, 7).toString());
        comboPortas.setSelectedItem(modeloTabela.getValueAt(linha, 8));
        comboCombustivel.setSelectedItem(modeloTabela.getValueAt(linha, 9).toString());
        checkArCondicionado.setSelected(modeloTabela.getValueAt(linha, 10).toString().equalsIgnoreCase("Sim"));
        comboCambio.setSelectedItem(modeloTabela.getValueAt(linha, 11).toString());
    }

    // NOVO: Método para limpar os campos do formulário
    private void limparCampos() {
        idSelecionado = null;
        txtPlaca.setText("");
        txtMarca.setText("");
        txtModelo.setText("");
        txtCor.setText("");
        txtAno.setText("");
        txtKm.setText("");
        txtPreco.setText("");
        comboPortas.setSelectedIndex(0);
        comboCombustivel.setSelectedIndex(0);
        comboCambio.setSelectedIndex(0);
        checkArCondicionado.setSelected(false);
        tabelaCarros.clearSelection();
    }

    private void atualizarTabela() {
        modeloTabela.setRowCount(0);
        ArrayList<Carro> carros = persistenciaCarro.consultarCarros();
        for (Carro c : carros) {
            modeloTabela.addRow(new Object[]{
                c.getId(), c.getPlaca(), c.getMarca(), c.getModelo(),
                c.getAno(), c.getPrecoDiario(), c.getCor(), c.getQuilometragem(),
                c.getNumPortas(), c.getTipoCombustivel(),
                c.isArCondicionado() ? "Sim" : "Não", c.getCambio()
            });
        }
    }

    private Carro criarCarroDoFormulario() throws NumberFormatException {
        Carro carro = new Carro();
        if (idSelecionado != null) {
            carro.setId(idSelecionado);
        }
        carro.setPlaca(txtPlaca.getText());
        carro.setMarca(txtMarca.getText());
        carro.setModelo(txtModelo.getText());
        carro.setCor(txtCor.getText());
        carro.setAno(Integer.parseInt(txtAno.getText()));
        carro.setQuilometragem(Integer.parseInt(txtKm.getText()));
        carro.setPrecoDiario(Double.parseDouble(txtPreco.getText()));
        carro.setNumPortas((Integer) comboPortas.getSelectedItem());
        carro.setTipoCombustivel((String) comboCombustivel.getSelectedItem());
        carro.setCambio((String) comboCambio.getSelectedItem());
        carro.setArCondicionado(checkArCondicionado.isSelected());
        return carro;
    }

    private void salvarCarro() {
        try {
            Carro novoCarro = criarCarroDoFormulario();
            String resultado = persistenciaCarro.incluirCarro(novoCarro);
            JOptionPane.showMessageDialog(this, resultado);
            atualizarTabela();
            limparCampos();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Erro de formato em campos numéricos.", "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    // NOVO: Método para editar o carro
    private void editarCarro() {
        if (idSelecionado == null) {
            JOptionPane.showMessageDialog(this, "Selecione um carro na tabela para editar.");
            return;
        }
        try {
            Carro carroEditado = criarCarroDoFormulario();
            String resultado = persistenciaCarro.alterarCarro(carroEditado);
            JOptionPane.showMessageDialog(this, resultado);
            atualizarTabela();
            limparCampos();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Erro de formato em campos numéricos.", "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void excluirCarro() {
        if (idSelecionado == null) {
            JOptionPane.showMessageDialog(this, "Selecione um carro na tabela para excluir.");
            return;
        }
        int confirma = JOptionPane.showConfirmDialog(this, "Tem certeza?", "Excluir Carro", JOptionPane.YES_NO_OPTION);
        if (confirma == JOptionPane.YES_OPTION) {
            String resultado = persistenciaCarro.excluirCarro(idSelecionado);
            JOptionPane.showMessageDialog(this, resultado);
            limparCampos();
            atualizarTabela();
        }
    }
}