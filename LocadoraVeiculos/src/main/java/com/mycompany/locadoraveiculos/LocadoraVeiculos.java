package com.mycompany.locadoraveiculos;

import java.util.*;
import java.time.LocalDate;

public class LocadoraVeiculos {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int opcao = 0;

        do {
            System.out.println("\n=== SISTEMA DE LOCAÇÃO DE VEÍCULOS ===");
            System.out.println("1. Cadastrar Carro");
            System.out.println("2. Listar Carros");
            System.out.println("3. Editar Carro");
            System.out.println("4. Apagar Carro");
            System.out.println("5. Cadastrar Moto");
            System.out.println("6. Listar Motos");
            System.out.println("7. Editar Moto");
            System.out.println("8. Apagar Moto");
            System.out.println("9. Cadastrar Aluguel");
            System.out.println("10. Listar Aluguéis");
            System.out.println("11. Editar Aluguel");
            System.out.println("12. Devolver Aluguel");
            System.out.println("0. Sair");
            System.out.print("Escolha uma opção: ");

            try {
                opcao = scanner.nextInt();
                scanner.nextLine(); // Consumir a nova linha

                switch (opcao) {
                    case 1: // Cadastrar Carro
                        System.out.print("Modelo: ");
                        String modeloCarro = scanner.nextLine();
                        System.out.print("Placa: ");
                        String placaCarro = scanner.nextLine();
                        System.out.print("Ano: ");
                        int anoCarro = scanner.nextInt();
                        System.out.print("Preço Diário: ");
                        double precoCarro = scanner.nextDouble();
                        scanner.nextLine(); // Consumir a nova linha
                        System.out.print("Número de Portas: ");
                        int numPortas = scanner.nextInt();
                        scanner.nextLine(); // Consumir a nova linha
                        System.out.print("Tipo de Combustível: ");
                        String combustivel = scanner.nextLine();
                        System.out.print("Tem Ar Condicionado? (S/N): ");
                        boolean arCondicionado = scanner.nextLine().equalsIgnoreCase("S");
                        System.out.print("Tipo de Câmbio: ");
                        String cambio = scanner.nextLine();
                        System.out.print("Marca: ");
                        String marcaCarro = scanner.nextLine();
                        System.out.print("Cor: ");
                        String corCarro = scanner.nextLine();
                        System.out.print("Quilometragem: ");
                        int kmCarro = scanner.nextInt();
                        scanner.nextLine(); // Consumir a nova linha

                        Carro.cadastrarCarro(
                                placaCarro, marcaCarro, modeloCarro, anoCarro, precoCarro,
                                corCarro, kmCarro, numPortas, cambio, arCondicionado, combustivel
                        );

                        break;

                    case 2: // Listar Carros
                        Carro.listarCarros();
                        break;

                    case 3: // Editar Carro
                        System.out.print("ID do Carro a editar: ");
                        int idCarroEdit = scanner.nextInt();
                        scanner.nextLine(); // limpar

                        System.out.print("Nova Placa: ");
                        String novaPlacaCarro = scanner.nextLine();
                        System.out.print("Nova Marca: ");
                        String novaMarcaCarro = scanner.nextLine();
                        System.out.print("Novo Modelo: ");
                        String novoModeloCarro = scanner.nextLine();
                        System.out.print("Novo Ano: ");
                        int novoAnoCarro = scanner.nextInt();
                        System.out.print("Novo Preço Diário: ");
                        double novoPrecoCarro = scanner.nextDouble();
                        scanner.nextLine();
                        System.out.print("Nova Cor: ");
                        String novaCorCarro = scanner.nextLine();
                        System.out.print("Nova Quilometragem: ");
                        int novaKmCarro = scanner.nextInt();
                        System.out.print("Novo Número de Portas: ");
                        int novoNumPortas = scanner.nextInt();
                        scanner.nextLine();
                        System.out.print("Novo Tipo de Câmbio: ");
                        String novoCambio = scanner.nextLine();
                        System.out.print("Tem Ar Condicionado? (S/N): ");
                        boolean novoArCondicionado = scanner.nextLine().equalsIgnoreCase("S");
                        System.out.print("Novo Tipo de Combustível: ");
                        String novoCombustivel = scanner.nextLine();

                        Carro.editarCarro(idCarroEdit, novaPlacaCarro, novaMarcaCarro, novoModeloCarro,
                                novoAnoCarro, novoPrecoCarro, novaCorCarro, novaKmCarro,
                                novoNumPortas, novoCambio, novoArCondicionado, novoCombustivel);
                        break;

                    case 4: // Apagar Carro
                        System.out.print("ID do Carro a apagar: ");
                        int idCarroDel = scanner.nextInt();
                        Carro.apagarCarro(idCarroDel);
                        break;

                    case 5: // Cadastrar Moto
                        System.out.print("Placa: ");
                        String placaMoto = scanner.nextLine();
                        System.out.print("Marca: ");
                        String marcaMoto = scanner.nextLine();
                        System.out.print("Modelo: ");
                        String modeloMoto = scanner.nextLine();
                        System.out.print("Ano de Fabricação: ");
                        int anoMoto = scanner.nextInt();
                        System.out.print("Valor da Diária: ");
                        double precoMoto = scanner.nextDouble();
                        scanner.nextLine(); // Consumir a nova linha
                        System.out.print("Cor: ");
                        String corMoto = scanner.nextLine();
                        System.out.print("Quilometragem: ");
                        int kmMoto = scanner.nextInt();
                        System.out.print("Cilindradas: ");
                        int cilindradas = scanner.nextInt();
                        scanner.nextLine(); // Consumir a nova linha
                        System.out.print("Tem Partida Elétrica? (S/N): ");
                        boolean partidaEletrica = scanner.nextLine().equalsIgnoreCase("S");
                        System.out.print("Tipo de Moto (Esportiva/Custom/Street): ");
                        String tipoMoto = scanner.nextLine();
                        System.out.print("Tipo de Freio: ");
                        String tipoFreio = scanner.nextLine();

                        Moto.cadastrarMoto(placaMoto, marcaMoto, modeloMoto, anoMoto, precoMoto,
                                corMoto, kmMoto, cilindradas, partidaEletrica,
                                tipoMoto, tipoFreio);
                        break;

                    case 6: // Listar Motos
                        Moto.listarMotos();
                        break;

                    case 7: // Editar Moto
                        System.out.print("ID da Moto a editar: ");
                        int idMotoEdit = scanner.nextInt();
                        scanner.nextLine(); // Consumir a nova linha

                        System.out.print("Nova Placa: ");
                        String novaPlacaMoto = scanner.nextLine();
                        System.out.print("Nova Marca: ");
                        String novaMarcaMoto = scanner.nextLine();
                        System.out.print("Novo Modelo: ");
                        String novoModeloMoto = scanner.nextLine();
                        System.out.print("Novo Ano de Fabricação: ");
                        int novoAnoMoto = scanner.nextInt();
                        System.out.print("Novo Valor da Diária: ");
                        double novoPrecoMoto = scanner.nextDouble();
                        scanner.nextLine(); // Consumir a nova linha
                        System.out.print("Nova Cor: ");
                        String novaCorMoto = scanner.nextLine();
                        System.out.print("Nova Quilometragem: ");
                        int novaKmMoto = scanner.nextInt();
                        System.out.print("Novas Cilindradas: ");
                        int novasCilindradas = scanner.nextInt();
                        scanner.nextLine(); // Consumir a nova linha
                        System.out.print("Tem Partida Elétrica? (S/N): ");
                        boolean novaPartidaEletrica = scanner.nextLine().equalsIgnoreCase("S");
                        System.out.print("Novo Tipo de Moto: ");
                        String novoTipoMoto = scanner.nextLine();
                        System.out.print("Novo Tipo de Freio: ");
                        String novoTipoFreio = scanner.nextLine();

                        Moto.editarMoto(idMotoEdit, novaPlacaMoto, novaMarcaMoto, novoModeloMoto, novoAnoMoto,
                                novoPrecoMoto, novaCorMoto, novaKmMoto, novasCilindradas,
                                novaPartidaEletrica, novoTipoMoto, novoTipoFreio);
                        break;

                    case 8: // Apagar Moto
                        System.out.print("ID da Moto a apagar: ");
                        int idMotoDel = scanner.nextInt();
                        Moto.apagarMoto(idMotoDel);
                        break;

                    case 9: // Cadastrar Aluguel
                        System.out.print("Nome do Cliente: ");
                        scanner.nextLine(); // Consumir a nova linha
                        String nomeCliente = scanner.nextLine();
                        Cliente cliente = new Cliente(nomeCliente); // ou buscar em uma lista, se existir

                        System.out.print("ID do Veículo para alugar (Moto ou Carro): ");
                        int idVeiculo = scanner.nextInt();
                        scanner.nextLine(); // limpar
                        // Você pode adaptar para realizar a busca com os IDs de moto ou carro
                        Veiculo veiculo = Veiculo.buscarPorId(idVeiculo); // Isso precisa ser implementado na classe Veiculo

                        System.out.print("Data de Início (AAAA-MM-DD): ");
                        LocalDate inicio = LocalDate.parse(scanner.nextLine());
                        System.out.print("Data de Fim (AAAA-MM-DD): ");
                        LocalDate fim = LocalDate.parse(scanner.nextLine());

                        Aluguel.inserirAluguel(cliente, veiculo, inicio, fim);
                        break;

                    case 10: // Listar Aluguéis
                        Aluguel.listarAluguel();
                        break;

                    case 11: // Editar Aluguel
                        System.out.print("ID do Aluguel a editar: ");
                        int idAluguelEdit = scanner.nextInt();
                        scanner.nextLine();

                        System.out.print("Novo nome do Cliente: ");
                        String novoNomeCliente = scanner.nextLine();
                        Cliente novoCliente = new Cliente(novoNomeCliente); // ou buscar

                        System.out.print("Novo ID do Veículo: ");
                        int novoIdVeiculo = scanner.nextInt();
                        scanner.nextLine();
                        Veiculo novoVeiculo = Veiculo.buscarPorId(novoIdVeiculo); // ou usar lista

                        System.out.print("Nova Data de Início (AAAA-MM-DD): ");
                        LocalDate novaDataInicio = LocalDate.parse(scanner.nextLine());
                        System.out.print("Nova Data de Fim (AAAA-MM-DD): ");
                        LocalDate novaDataFim = LocalDate.parse(scanner.nextLine());

                        Aluguel.editarAluguel(idAluguelEdit, novoCliente, novoVeiculo, novaDataInicio, novaDataFim);
                        break;

                    case 12: // Devolução
                        System.out.print("ID do Aluguel para devolução: ");
                        int idDevolucao = scanner.nextInt();
                        scanner.nextLine();
                        Aluguel.devolucaoAluguel(idDevolucao);
                        break;

                    case 0:
                        System.out.println("Saindo do sistema...");
                        break;

                    default:
                        System.out.println("Opção inválida! Tente novamente.");
                        break;
                }
            } catch (Exception e) {
                System.out.println("Erro: " + e.getMessage());
                scanner.nextLine(); // Limpar o buffer
            }
        } while (opcao != 0);

        scanner.close();
    }
}
