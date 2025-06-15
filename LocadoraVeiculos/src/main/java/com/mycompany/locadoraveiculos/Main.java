package com.mycompany.locadoraveiculos;

import javax.swing.SwingUtilities;

public class Main {

    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> {

            TelaInicial telaMenu = new TelaInicial();
            telaMenu.setVisible(true);
        });
    }
}