package fintrack.controller;

import fintrack.exception.ValorInvalidoException;
import fintrack.model.TipoTransacao;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class FinanceiroControllerTest {
    @Test
    void calculaSaldoComReceitasEDespesas() throws Exception {
        FinanceiroController controller = new FinanceiroController();
        controller.cadastrar("Salário", 3000, TipoTransacao.RECEITA);
        controller.cadastrar("Aluguel", 1200, TipoTransacao.DESPESA);
        assertEquals(1800, controller.calcularSaldo());
    }

    @Test
    void rejeitaValorNaoPositivo() {
        FinanceiroController controller = new FinanceiroController();
        assertThrows(ValorInvalidoException.class,
                () -> controller.cadastrar("Inválida", 0, TipoTransacao.RECEITA));
    }

    @Test
    void removeTransacaoPeloId() throws Exception {
        FinanceiroController controller = new FinanceiroController();
        var transacao = controller.cadastrar("Café", 10, TipoTransacao.DESPESA);
        controller.remover(transacao.getId());
        assertTrue(controller.listarTodas().isEmpty());
    }

    @Test
    void atualizaTransacaoExistente() throws Exception {
        FinanceiroController controller = new FinanceiroController();
        var transacao = controller.cadastrar("Freelance", 500, TipoTransacao.RECEITA);
        controller.atualizar(transacao.getId(), "Freelance Java", 650, TipoTransacao.RECEITA);

        var atualizada = controller.listarTodas().get(0);
        assertEquals("Freelance Java", atualizada.getDescricao());
        assertEquals(650.0, atualizada.getValor());
    }
}
