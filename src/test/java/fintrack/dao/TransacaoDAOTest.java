package fintrack.dao;

import fintrack.model.TipoTransacao;
import fintrack.model.Transacao;
import org.junit.jupiter.api.*;

import java.sql.SQLException;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class TransacaoDAOTest {
    private TransacaoDAO dao;

    @BeforeEach
    void prepararBanco() throws SQLException {
        System.setProperty("fintrack.db", "jdbc:sqlite::memory:");
        dao = new TransacaoDAO();
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
}
