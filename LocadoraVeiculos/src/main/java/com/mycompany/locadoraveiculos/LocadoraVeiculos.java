
package com.mycompany.locadoraveiculos;

/**
 *
 * @author tiago
 */

import model.*;
import java.util.*;
import java.time.LocalDate;

public class App {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        Cliente c1 = new Cliente(1, "João", "123.456.789-00", "(62) 99999-9999", "joao@mail.com");
        Veiculo v1 = new Veiculo(1, "Civic", "ABC-1234", 2020, 150.0);
        Aluguel a1 = new Aluguel(1, c1, v1, LocalDate.of(2025, 5, 1), LocalDate.of(2025, 5, 5));
        Pagamento p1 = new Pagamento(1, a1, "Cartão de Crédito");

        System.out.println(c1);
        System.out.println(v1);
        System.out.println(a1);
        System.out.println(p1);

        sc.close();
    }
}

