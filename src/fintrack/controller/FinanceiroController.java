package fintrack.controller;

import fintrack.exception.DescricaoInvalidaException;
import fintrack.exception.TransacaoNaoEncontradaException;
import fintrack.exception.ValorInvalidoException;
import fintrack.model.TipoTransacao;
import fintrack.model.Transacao;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class FinanceiroController {

    private final List<Transacao> transacoes;

    public FinanceiroController() {
        this.transacoes = new ArrayList<>();
    }

    // ── Cadastrar ────────────────────────────────────────────────

    public Transacao cadastrar(String descricao, double valor, TipoTransacao tipo)
            throws DescricaoInvalidaException, ValorInvalidoException {

        validarDescricao(descricao);
        validarValor(valor);

        Transacao t = new Transacao(descricao.trim(), valor, tipo);
        transacoes.add(t);
        return t;
    }

    // ── Remover ──────────────────────────────────────────────────

    public Transacao remover(int id) throws TransacaoNaoEncontradaException {
        Transacao encontrada = buscarPorId(id);
        transacoes.remove(encontrada);
        return encontrada;
    }

    // ── Consultas ────────────────────────────────────────────────

    public List<Transacao> listarTodas() {
        return Collections.unmodifiableList(transacoes);
    }

    public double calcularSaldo() {
        double total = 0;
        for (Transacao t : transacoes) {
            if (t.getTipo() == TipoTransacao.RECEITA) {
                total += t.getValor();
            } else {
                total -= t.getValor();
            }
        }
        return total;
    }

    public double calcularTotalReceitas() {
        return transacoes.stream()
                .filter(t -> t.getTipo() == TipoTransacao.RECEITA)
                .mapToDouble(Transacao::getValor)
                .sum();
    }

    public double calcularTotalDespesas() {
        return transacoes.stream()
                .filter(t -> t.getTipo() == TipoTransacao.DESPESA)
                .mapToDouble(Transacao::getValor)
                .sum();
    }

    public boolean hasTransacoes() {
        return !transacoes.isEmpty();
    }

    // ── Auxiliares privados ───────────────────────────────────────

    private Transacao buscarPorId(int id) throws TransacaoNaoEncontradaException {
        return transacoes.stream()
                .filter(t -> t.getId() == id)
                .findFirst()
                .orElseThrow(() -> new TransacaoNaoEncontradaException(id));
    }

    private void validarDescricao(String descricao) throws DescricaoInvalidaException {
        if (descricao == null || descricao.trim().isEmpty()) {
            throw new DescricaoInvalidaException();
        }
    }

    private void validarValor(double valor) throws ValorInvalidoException {
        if (valor <= 0) {
            throw new ValorInvalidoException(valor);
        }
    }
}
