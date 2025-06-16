package com.mycompany.locadoraveiculos;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;

public class TelaAluguel extends JFrame {

    private JComboBox<Cliente> comboClientes;
    private JComboBox<Veiculo> comboVeiculos;
    private JTextField txtDataInicio;
    private JButton btnRegistrarAluguel, btnRegistrarDevolucao, btnEditarAluguel;
    private JButton btnCancelarEdicao;

    private JTable tabelaAlugueis;
    private DefaultTableModel modeloTabela;

    private PAluguel persistenciaAluguel;
    private PCliente persistenciaCliente;
    private PCarro persistenciaCarro;
    private PMoto persistenciaMoto;

    private int idAluguelEmEdicao = -1;

    public TelaAluguel() {
        persistenciaAluguel = new PAluguel();
        persistenciaCliente = new PCliente();
        persistenciaCarro = new PCarro();
        persistenciaMoto = new PMoto();

        setTitle("Gerenciamento de Aluguéis");
        setSize(1000, 700);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel painelForm = new JPanel(new GridLayout(0, 2, 10, 10));
        painelForm.setBorder(BorderFactory.createTitledBorder("Registrar Novo Aluguel"));

        painelForm.add(new JLabel("Cliente:"));
        comboClientes = new JComboBox<>();
        painelForm.add(comboClientes);

        painelForm.add(new JLabel("Veículo:"));
        comboVeiculos = new JComboBox<>();
        painelForm.add(comboVeiculos);

        painelForm.add(new JLabel("Data de Início (dd/MM/yyyy):"));
        txtDataInicio = new JTextField();
        painelForm.add(txtDataInicio);

        btnRegistrarAluguel = new JButton("Registrar Aluguel");
        painelForm.add(new JLabel());
        painelForm.add(btnRegistrarAluguel);

        btnCancelarEdicao = new JButton("Cancelar Edição");
        btnCancelarEdicao.setVisible(false);
        painelForm.add(btnCancelarEdicao);

        JPanel painelTabela = new JPanel(new BorderLayout(10, 10));
        painelTabela.setBorder(BorderFactory.createTitledBorder("Histórico de Aluguéis"));

        String[] colunas = {"ID", "Cliente", "Veículo", "Placa", "Data Início", "Data Fim", "Total (R$)"};
        modeloTabela = new DefaultTableModel(colunas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tabelaAlugueis = new JTable(modeloTabela);
        painelTabela.add(new JScrollPane(tabelaAlugueis), BorderLayout.CENTER);

        JPanel painelBotoesTabela = new JPanel(new FlowLayout(FlowLayout.LEFT));
        btnRegistrarDevolucao = new JButton("Registrar Devolução de Item Selecionado");
        painelBotoesTabela.add(btnRegistrarDevolucao);

        btnEditarAluguel = new JButton("Editar Aluguel Selecionado");
        painelBotoesTabela.add(btnEditarAluguel);

        painelTabela.add(painelBotoesTabela, BorderLayout.SOUTH);

        add(painelForm, BorderLayout.NORTH);
        add(painelTabela, BorderLayout.CENTER);

        adicionarListeners();
        carregarCombos();
        atualizarTabela();
    }

    private void carregarCombos() {
        ArrayList<Cliente> clientes = persistenciaCliente.consultarClientes();
        for (Cliente c : clientes) {
            comboClientes.addItem(c);
        }

        ArrayList<Carro> carros = persistenciaCarro.consultarCarros();
        for (Carro car : carros) {
            comboVeiculos.addItem(car);
        }
        ArrayList<Moto> motos = persistenciaMoto.consultarMotos();
        for (Moto m : motos) {
            comboVeiculos.addItem(m);
        }
    }

    private void adicionarListeners() {
        btnRegistrarAluguel.addActionListener(e -> registrarAluguel());
        btnRegistrarDevolucao.addActionListener(e -> registrarDevolucao());
        btnEditarAluguel.addActionListener(e -> editarAluguel());
        btnCancelarEdicao.addActionListener(e -> cancelarEdicao());
    }

    private void atualizarTabela() {
        modeloTabela.setRowCount(0);
        ArrayList<Aluguel> alugueis = persistenciaAluguel.consultarAlugueis();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        for (Aluguel a : alugueis) {
            modeloTabela.addRow(new Object[]{
                a.getId(),
                a.getCliente().getNome(),
                a.getVeiculo().getModelo(),
                a.getVeiculo().getPlaca(),
                a.getDataInicio().format(formatter),
                (a.getDataFim() != null) ? a.getDataFim().format(formatter) : "Em andamento",
                String.format("%.2f", a.calcularTotalAluguel())
            });
        }
    }

    private void registrarAluguel() {
        try {
            Cliente cliente = (Cliente) comboClientes.getSelectedItem();
            Veiculo veiculo = (Veiculo) comboVeiculos.getSelectedItem();

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            LocalDate dataInicio = LocalDate.parse(txtDataInicio.getText(), formatter);

            if (cliente == null || veiculo == null) {
                JOptionPane.showMessageDialog(this, "Selecione um cliente e um veículo.", "Erro", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (idAluguelEmEdicao == -1) {
                Aluguel novoAluguel = new Aluguel();
                novoAluguel.setCliente(cliente);
                novoAluguel.setVeiculo(veiculo);
                novoAluguel.setDataInicio(dataInicio);

                String resultado = persistenciaAluguel.incluirAluguel(novoAluguel);
                JOptionPane.showMessageDialog(this, resultado);
            } else {
                Aluguel aluguelEditado = new Aluguel();
                aluguelEditado.setId(idAluguelEmEdicao);
                aluguelEditado.setCliente(cliente);
                aluguelEditado.setVeiculo(veiculo);
                aluguelEditado.setDataInicio(dataInicio);

                String resultado = persistenciaAluguel.atualizarAluguel(aluguelEditado);
                JOptionPane.showMessageDialog(this, resultado);
                
                idAluguelEmEdicao = -1;
                btnRegistrarAluguel.setText("Registrar Aluguel");
                btnCancelarEdicao.setVisible(false);
            }
            
            atualizarTabela();
            limparFormulario();
            
        } catch (DateTimeParseException ex) {
            JOptionPane.showMessageDialog(this, "Formato de data inválido. Use dd/MM/yyyy.", "Erro de Formato", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void limparFormulario() {
        comboClientes.setSelectedIndex(0);
        comboVeiculos.setSelectedIndex(0);
        txtDataInicio.setText("");
    }

    private void registrarDevolucao() {
        int linhaSelecionada = tabelaAlugueis.getSelectedRow();
        if (linhaSelecionada == -1) {
            JOptionPane.showMessageDialog(this, "Selecione um aluguel na tabela para registrar a devolução.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (!modeloTabela.getValueAt(linhaSelecionada, 5).toString().equals("Em andamento")) {
            JOptionPane.showMessageDialog(this, "Este aluguel já foi finalizado.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int idAluguel = (int) modeloTabela.getValueAt(linhaSelecionada, 0);

        JPanel panel = new JPanel(new GridLayout(0, 1));
        JLabel label = new JLabel("Digite a data de devolução (dd/MM/yyyy):");
        JTextField dataField = new JTextField(10);
        panel.add(label);
        panel.add(dataField);

        String[] options = {"Confirmar", "Usar Data de Hoje", "Cancelar"};

        int result = JOptionPane.showOptionDialog(
                this,
                panel,
                "Registrar Devolução",
                JOptionPane.YES_NO_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE,
                null,
                options,
                options[0]
        );

        LocalDate dataDevolucao = null;

        if (result == 0) {
            try {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                dataDevolucao = LocalDate.parse(dataField.getText(), formatter);
            } catch (DateTimeParseException ex) {
                JOptionPane.showMessageDialog(this, "Formato de data inválido. Use dd/MM/yyyy.", "Erro de Formato", JOptionPane.ERROR_MESSAGE);
                return;
            }
        } else if (result == 1) {
            dataDevolucao = LocalDate.now();
        } else {
            return;
        }

        if (dataDevolucao != null) {
            String resultado = persistenciaAluguel.registrarDevolucao(idAluguel, dataDevolucao);
            JOptionPane.showMessageDialog(this, resultado);
            atualizarTabela();
        }
    }

    private void editarAluguel() {
        int linhaSelecionada = tabelaAlugueis.getSelectedRow();
        if (linhaSelecionada == -1) {
            JOptionPane.showMessageDialog(this, 
                "Selecione um aluguel na tabela para editar.", 
                "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (!modeloTabela.getValueAt(linhaSelecionada, 5).toString().equals("Em andamento")) {
            JOptionPane.showMessageDialog(this, 
                "Não é possível editar aluguéis finalizados.", 
                "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        idAluguelEmEdicao = (int) modeloTabela.getValueAt(linhaSelecionada, 0);
        Aluguel aluguel = persistenciaAluguel.consultarAluguelPorId(idAluguelEmEdicao);
        
        if (aluguel != null) {
            preencherFormulario(aluguel);
            btnRegistrarAluguel.setText("Salvar Edição");
            btnCancelarEdicao.setVisible(true);
        }
    }

    private void preencherFormulario(Aluguel aluguel) {
        for (int i = 0; i < comboClientes.getItemCount(); i++) {
            Cliente c = comboClientes.getItemAt(i);
            if (c.getIdCliente() == aluguel.getCliente().getIdCliente()) {
                comboClientes.setSelectedIndex(i);
                break;
            }
        }

        for (int i = 0; i < comboVeiculos.getItemCount(); i++) {
            Veiculo v = comboVeiculos.getItemAt(i);
            if (v.getId() == aluguel.getVeiculo().getId()) {
                comboVeiculos.setSelectedIndex(i);
                break;
            }
        }

        txtDataInicio.setText(aluguel.getDataInicio()
            .format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
    }

    private void cancelarEdicao() {
        idAluguelEmEdicao = -1;
        btnRegistrarAluguel.setText("Registrar Aluguel");
        btnCancelarEdicao.setVisible(false);
        limparFormulario();
    }
}