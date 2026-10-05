package fintrack.dao;

import fintrack.db.Conexao;
import fintrack.model.TipoTransacao;
import fintrack.model.Transacao;
import fintrack.repository.RepositorioGenerico;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class TransacaoDAO implements RepositorioGenerico<Transacao, Integer> {
    public TransacaoDAO() throws SQLException {
        Conexao.inicializar();
    }

    @Override
    public Transacao salvar(Transacao transacao) {
        String sql = "INSERT INTO transacoes (descricao, valor, tipo, data) VALUES (?, ?, ?, ?)";
        try (Connection connection = Conexao.abrir();
             PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, transacao.getDescricao());
            statement.setDouble(2, transacao.getValor());
            statement.setString(3, transacao.getTipo().name());
            statement.setString(4, transacao.getData().toString());
            statement.executeUpdate();
            try (ResultSet keys = statement.getGeneratedKeys()) {
                if (keys.next()) {
                    return new Transacao(keys.getInt(1), transacao.getDescricao(), transacao.getValor(),
                            transacao.getTipo(), transacao.getData());
                }
            }
            return transacao;
        } catch (SQLException e) {
            throw new IllegalStateException("Não foi possível salvar a transação.", e);
        }
    }

    @Override
    public void remover(Integer id) {
        try (Connection connection = Conexao.abrir();
             PreparedStatement statement = connection.prepareStatement("DELETE FROM transacoes WHERE id = ?")) {
            statement.setInt(1, id);
            if (statement.executeUpdate() == 0) throw new IllegalArgumentException("Transação não encontrada: " + id);
        } catch (SQLException e) {
            throw new IllegalStateException("Não foi possível remover a transação.", e);
        }
    }

    @Override
    public Optional<Transacao> buscarPorId(Integer id) {
        try (Connection connection = Conexao.abrir();
             PreparedStatement statement = connection.prepareStatement("SELECT * FROM transacoes WHERE id = ?")) {
            statement.setInt(1, id);
            try (ResultSet result = statement.executeQuery()) {
                return result.next() ? Optional.of(mapear(result)) : Optional.empty();
            }
        } catch (SQLException e) {
            throw new IllegalStateException("Não foi possível consultar a transação.", e);
        }
    }

    @Override
    public List<Transacao> listar() {
        List<Transacao> transacoes = new ArrayList<>();
        try (Connection connection = Conexao.abrir();
             PreparedStatement statement = connection.prepareStatement("SELECT * FROM transacoes ORDER BY data DESC, id DESC");
             ResultSet result = statement.executeQuery()) {
            while (result.next()) transacoes.add(mapear(result));
            return transacoes;
        } catch (SQLException e) {
            throw new IllegalStateException("Não foi possível listar as transações.", e);
        }
    }

    private Transacao mapear(ResultSet result) throws SQLException {
        return new Transacao(result.getInt("id"), result.getString("descricao"),
                result.getDouble("valor"), TipoTransacao.valueOf(result.getString("tipo")),
                LocalDate.parse(result.getString("data")));
    }
}
