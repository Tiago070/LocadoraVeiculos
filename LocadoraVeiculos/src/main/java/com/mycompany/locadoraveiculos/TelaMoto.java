// Pacote da sua aplicação
package com.mycompany.locadoraveiculos;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;

public class TelaMoto extends JFrame {

    private JTextField txtPlaca, txtMarca, txtModelo, txtAno, txtPreco, txtCor, txtKm, txtCilindradas;
    private JComboBox<String> comboTipoMoto, comboTipoFreio;
    private JCheckBox checkPartidaEletrica;
    // NOVO: Botão Limpar
    private JButton btnSalvar, btnEditar, btnExcluir, btnLimpar;
    
    private JTable tabelaMotos;
    private DefaultTableModel modeloTabela;
    
    private PMoto persistenciaMoto;
    private Integer idSelecionado = null;

    public TelaMoto() {
        persistenciaMoto = new PMoto();
        
        setTitle("Cadastro de Motos");
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
        painelForm.add(new JLabel("Cilindradas (cc):"));
        txtCilindradas = new JTextField();
        painelForm.add(txtCilindradas);
        painelForm.add(new JLabel("Tipo de Moto:"));
        comboTipoMoto = new JComboBox<>(new String[]{"Street", "Custom", "Trail", "Esportiva", "Scooter"});
        painelForm.add(comboTipoMoto);
        painelForm.add(new JLabel("Tipo de Freio:"));
        comboTipoFreio = new JComboBox<>(new String[]{"A Disco", "ABS", "Combinado (CBS)", "A Tambor"});
        painelForm.add(comboTipoFreio);
        painelForm.add(new JLabel("Partida Elétrica:"));
        checkPartidaEletrica = new JCheckBox();
        painelForm.add(checkPartidaEletrica);

        JPanel painelBotoes = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        btnSalvar = new JButton("Salvar");
        btnEditar = new JButton("Editar");
        btnExcluir = new JButton("Excluir");
        btnLimpar = new JButton("Limpar");
        painelBotoes.add(btnSalvar);
        painelBotoes.add(btnEditar);
        painelBotoes.add(btnExcluir);
        painelBotoes.add(btnLimpar);
        
        JPanel painelSuperior = new JPanel(new BorderLayout());
        painelSuperior.add(painelForm, BorderLayout.CENTER);
        painelSuperior.add(painelBotoes, BorderLayout.SOUTH);
        add(painelSuperior, BorderLayout.NORTH);
        
        String[] colunas = {"ID", "Placa", "Marca", "Modelo", "Ano", "Preço", "Cor", "KM", "CC", "Tipo", "Partida", "Freio"};
        modeloTabela = new DefaultTableModel(colunas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
        tabelaMotos = new JTable(modeloTabela);
        add(new JScrollPane(tabelaMotos), BorderLayout.CENTER);

        adicionarListeners();
        atualizarTabela();
    }
    
    private void adicionarListeners() {
        btnSalvar.addActionListener(e -> salvarMoto());
        btnEditar.addActionListener(e -> editarMoto());
        btnExcluir.addActionListener(e -> excluirMoto());
        btnLimpar.addActionListener(e -> limparCampos());

        tabelaMotos.getSelectionModel().addListSelectionListener(event -> {
             if (!event.getValueIsAdjusting() && tabelaMotos.getSelectedRow() != -1) {
                preencherFormularioPelaTabela();
            }
        });
    }

    private void preencherFormularioPelaTabela() {
        int linha = tabelaMotos.getSelectedRow();
        idSelecionado = (Integer) modeloTabela.getValueAt(linha, 0);

        txtPlaca.setText(modeloTabela.getValueAt(linha, 1).toString());
        txtMarca.setText(modeloTabela.getValueAt(linha, 2).toString());
        txtModelo.setText(modeloTabela.getValueAt(linha, 3).toString());
        txtAno.setText(modeloTabela.getValueAt(linha, 4).toString());
        txtPreco.setText(modeloTabela.getValueAt(linha, 5).toString());
        txtCor.setText(modeloTabela.getValueAt(linha, 6).toString());
        txtKm.setText(modeloTabela.getValueAt(linha, 7).toString());
        txtCilindradas.setText(modeloTabela.getValueAt(linha, 8).toString());
        comboTipoMoto.setSelectedItem(modeloTabela.getValueAt(linha, 9).toString());
        checkPartidaEletrica.setSelected(modeloTabela.getValueAt(linha, 10).toString().equalsIgnoreCase("Sim"));
        comboTipoFreio.setSelectedItem(modeloTabela.getValueAt(linha, 11).toString());
    }

    private void limparCampos() {
        idSelecionado = null;
        txtPlaca.setText("");
        txtMarca.setText("");
        txtModelo.setText("");
        txtCor.setText("");
        txtAno.setText("");
        txtKm.setText("");
        txtPreco.setText("");
        txtCilindradas.setText("");
        comboTipoMoto.setSelectedIndex(0);
        comboTipoFreio.setSelectedIndex(0);
        checkPartidaEletrica.setSelected(false);
        tabelaMotos.clearSelection();
    }

    private void atualizarTabela() {
        modeloTabela.setRowCount(0);
        ArrayList<Moto> motos = persistenciaMoto.consultarMotos();
        for (Moto m : motos) {
            modeloTabela.addRow(new Object[]{
                m.getId(), m.getPlaca(), m.getMarca(), m.getModelo(),
                m.getAno(), m.getPrecoDiario(), m.getCor(), m.getQuilometragem(),
                m.getCilindradas(), m.getTipoMoto(), 
                m.isPartidaEletrica() ? "Sim" : "Não", m.getTipoFreio()
            });
        }
    }

    private Moto criarMotoDoFormulario() throws NumberFormatException {
        Moto moto = new Moto();
        if (idSelecionado != null) moto.setId(idSelecionado);
        moto.setPlaca(txtPlaca.getText());
        moto.setMarca(txtMarca.getText());
        moto.setModelo(txtModelo.getText());
        moto.setCor(txtCor.getText());
        moto.setAno(Integer.parseInt(txtAno.getText()));
        moto.setQuilometragem(Integer.parseInt(txtKm.getText()));
        moto.setPrecoDiario(Double.parseDouble(txtPreco.getText()));
        moto.setCilindradas(Integer.parseInt(txtCilindradas.getText()));
        moto.setTipoMoto((String) comboTipoMoto.getSelectedItem());
        moto.setTipoFreio((String) comboTipoFreio.getSelectedItem());
        moto.setPartidaEletrica(checkPartidaEletrica.isSelected());
        return moto;
    }

    private void salvarMoto() {
        try {
            Moto novaMoto = criarMotoDoFormulario();
            String resultado = persistenciaMoto.incluirMoto(novaMoto);
            JOptionPane.showMessageDialog(this, resultado);
            atualizarTabela();
            limparCampos();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Erro de formato em campos numéricos.", "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void editarMoto() {
        if (idSelecionado == null) {
            JOptionPane.showMessageDialog(this, "Selecione uma moto na tabela para editar.");
            return;
        }
        try {
            Moto motoEditada = criarMotoDoFormulario();
            String resultado = persistenciaMoto.alterarMoto(motoEditada);
            JOptionPane.showMessageDialog(this, resultado);
            atualizarTabela();
            limparCampos();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Erro de formato em campos numéricos.", "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void excluirMoto() {
        if (idSelecionado == null) {
            JOptionPane.showMessageDialog(this, "Selecione uma moto na tabela para excluir.");
            return;
        }
        int confirma = JOptionPane.showConfirmDialog(this, "Tem certeza?", "Excluir Moto", JOptionPane.YES_NO_OPTION);
        if (confirma == JOptionPane.YES_OPTION) {
            String resultado = persistenciaMoto.excluirMoto(idSelecionado);
            JOptionPane.showMessageDialog(this, resultado);
            limparCampos();
            atualizarTabela();
        }
    }
}