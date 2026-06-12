package fintrack.view;

import fintrack.controller.FinanceiroController;
import fintrack.exception.DescricaoInvalidaException;
import fintrack.exception.TransacaoNaoEncontradaException;
import fintrack.exception.ValorInvalidoException;
import fintrack.model.TipoTransacao;
import fintrack.model.Transacao;

import java.util.List;
import java.util.Scanner;

public class ConsoleView {

    private final Scanner scanner;
    private final FinanceiroController controller;

    public ConsoleView(FinanceiroController controller) {
        this.controller = controller;
        this.scanner = new Scanner(System.in);
    }

    // ── Loop principal ────────────────────────────────────────────

    public void iniciar() {
        exibirCabecalho();

        int opcao;
        do {
            exibirMenu();
            opcao = lerInteiro("Escolha uma opção: ");

            switch (opcao) {
                case 1 -> fluxoCadastrar(TipoTransacao.RECEITA);
                case 2 -> fluxoCadastrar(TipoTransacao.DESPESA);
                case 3 -> fluxoListar();
                case 4 -> fluxoSaldo();
                case 5 -> fluxoRemover();
                case 0 -> System.out.println("\nEncerrando o FinTrack. Até logo!");
                default -> System.out.println("\n[!] Opção inválida. Tente novamente.");
            }

        } while (opcao != 0);

        scanner.close();
    }

    // ── Fluxos de cada funcionalidade ─────────────────────────────

    private void fluxoCadastrar(TipoTransacao tipo) {
        String label = (tipo == TipoTransacao.RECEITA) ? "Receita" : "Despesa";
        System.out.println("\n--- Cadastrar " + label + " ---");

        try {
            System.out.print("Descrição: ");
            String descricao = scanner.nextLine();

            double valor = lerDouble("Valor (R$): ");

            Transacao t = controller.cadastrar(descricao, valor, tipo);
            System.out.println("\n[OK] Transação cadastrada: " + t);

        } catch (DescricaoInvalidaException | ValorInvalidoException e) {
            System.out.println("\n[ERRO] " + e.getMessage());
        }
    }

    private void fluxoListar() {
        List<Transacao> lista = controller.listarTodas();

        if (lista.isEmpty()) {
            System.out.println("\nNenhuma transação cadastrada.");
            return;
        }

        System.out.println("\n" + "=".repeat(62));
        System.out.printf("%-6s %-28s %-10s %-12s%n", "ID", "Descrição", "Tipo", "Valor");
        System.out.println("=".repeat(62));

        for (Transacao t : lista) {
            String sinal = (t.getTipo() == TipoTransacao.RECEITA) ? "+" : "-";
            System.out.printf("%-6d %-28s %-10s %s R$ %.2f%n",
                    t.getId(),
                    t.getDescricao(),
                    t.getTipo(),
                    sinal,
                    t.getValor());
        }

        System.out.println("=".repeat(62));
        System.out.printf("  Total de transações: %d%n", lista.size());
    }

    private void fluxoSaldo() {
        double receitas  = controller.calcularTotalReceitas();
        double despesas  = controller.calcularTotalDespesas();
        double saldo     = controller.calcularSaldo();

        System.out.println("\n" + "=".repeat(36));
        System.out.printf("  Receitas :  R$ %10.2f%n", receitas);
        System.out.printf("  Despesas :  R$ %10.2f%n", despesas);
        System.out.println("  " + "-".repeat(32));
        System.out.printf("  Saldo    :  R$ %10.2f%n", saldo);
        System.out.println("=".repeat(36));

        if (saldo < 0) {
            System.out.println("  [!] Atenção: saldo negativo!");
        } else if (saldo == 0) {
            System.out.println("  [!] Saldo zerado.");
        } else {
            System.out.println("  [OK] Finanças equilibradas.");
        }
    }

    private void fluxoRemover() {
        if (!controller.hasTransacoes()) {
            System.out.println("\nNenhuma transação para remover.");
            return;
        }

        fluxoListar();

        try {
            int id = lerInteiro("Digite o ID da transação a remover: ");
            Transacao removida = controller.remover(id);
            System.out.println("\n[OK] Removida: " + removida);

        } catch (TransacaoNaoEncontradaException e) {
            System.out.println("\n[ERRO] " + e.getMessage());
        }
    }

    // ── Exibição ──────────────────────────────────────────────────

    private void exibirCabecalho() {
        System.out.println("+=====================================+");
        System.out.println("|   FinTrack - Controle de Finanças   |");
        System.out.println("+=====================================+");
    }

    private void exibirMenu() {
        System.out.println("\n+------------------------------+");
        System.out.println("|           MENU               |");
        System.out.println("+------------------------------+");
        System.out.println("|  1. Cadastrar Receita        |");
        System.out.println("|  2. Cadastrar Despesa        |");
        System.out.println("|  3. Listar Transações        |");
        System.out.println("|  4. Exibir Saldo             |");
        System.out.println("|  5. Remover Transação        |");
        System.out.println("|  0. Sair                     |");
        System.out.println("+------------------------------+");
    }

    // ── Leitura segura ────────────────────────────────────────────

    private int lerInteiro(String mensagem) {
        while (true) {
            System.out.print(mensagem);
            try {
                return Integer.parseInt(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("[!] Entrada inválida. Digite um número inteiro.");
            }
        }
    }

    private double lerDouble(String mensagem) {
        while (true) {
            System.out.print(mensagem);
            try {
                return Double.parseDouble(scanner.nextLine().trim().replace(",", "."));
            } catch (NumberFormatException e) {
                System.out.println("[!] Entrada inválida. Digite um número (ex: 150.00).");
            }
        }
    }
}
