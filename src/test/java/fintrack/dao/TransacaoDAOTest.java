package fintrack.dao;

import fintrack.db.Conexao;
import fintrack.model.TipoTransacao;
import fintrack.model.Transacao;
import org.junit.jupiter.api.*;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class TransacaoDAOTest {
    private fintrack.dao.TransacaoDAO dao;

    @BeforeEach
    void prepararBanco() throws SQLException {
        System.setProperty("fintrack.db", "jdbc:sqlite::memory:");
        dao = new fintrack.dao.TransacaoDAO();
        try (Connection conn = Conexao.abrir(); Statement st = conn.createStatement()) {
            st.executeUpdate("DELETE FROM transacoes");
        }
    }

    @AfterEach
    void limparConfiguracao() {
        System.clearProperty("fintrack.db");
    }

    @Test
    void salvaListaBuscaERemove() {
        Transacao criada = dao.salvar(new Transacao("Mercado", 80, TipoTransacao.DESPESA, LocalDate.now()));
        assertEquals(1, dao.listar().size());
        assertEquals("Mercado", dao.buscarPorId(criada.getId()).orElseThrow().getDescricao());
        dao.remover(criada.getId());
        assertTrue(dao.listar().isEmpty());
    }

    @Test
    void atualizaTransacaoExistente() {
        Transacao criada = dao.salvar(new Transacao("Aluguel", 1200.0, TipoTransacao.DESPESA, LocalDate.now()));
        Transacao paraAtualizar = new Transacao(criada.getId(), "Aluguel Reajustado", 1300.0, TipoTransacao.DESPESA, LocalDate.now());

        dao.atualizar(paraAtualizar);

        Transacao atualizada = dao.buscarPorId(criada.getId()).orElseThrow();
        assertEquals("Aluguel Reajustado", atualizada.getDescricao());
        assertEquals(1300.0, atualizada.getValor());
        assertEquals(TipoTransacao.DESPESA, atualizada.getTipo());
    }

    @Test
    void atualizaTransacaoInexistenteLancaExcecao() {
        Transacao inexistente = new Transacao(9999, "Fantasma", 100.0, TipoTransacao.RECEITA, LocalDate.now());
        assertThrows(IllegalArgumentException.class, () -> dao.atualizar(inexistente));
    }
}
