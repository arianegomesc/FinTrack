package fintrack.model;

import java.time.LocalDate;

public class Transacao {

    private static int contadorId = 1;

    private final int id;
    private String descricao;
    private double valor;
    private final TipoTransacao tipo;
    private final LocalDate data;

    public Transacao(String descricao, double valor, TipoTransacao tipo) {
        this(descricao, valor, tipo, LocalDate.now());
    }

    public Transacao(String descricao, double valor, TipoTransacao tipo, LocalDate data) {
        this.id = contadorId++;
        this.descricao = descricao;
        this.valor = valor;
        this.tipo = tipo;
        this.data = data;
    }

    public Transacao(int id, String descricao, double valor, TipoTransacao tipo, LocalDate data) {
        this.id = id;
        this.descricao = descricao;
        this.valor = valor;
        this.tipo = tipo;
        this.data = data;
        contadorId = Math.max(contadorId, id + 1);
    }

    // Getters
    public int getId()             { return id; }
    public String getDescricao()   { return descricao; }
    public double getValor()       { return valor; }
    public TipoTransacao getTipo() { return tipo; }
    public LocalDate getData() { return data; }

    // Setters (apenas campos mutáveis)
    public void setDescricao(String descricao) { this.descricao = descricao; }
    public void setValor(double valor)         { this.valor = valor; }

    @Override
    public String toString() {
        String sinal = (tipo == TipoTransacao.RECEITA) ? "+" : "-";
        return String.format("[ID: %d] %-28s %s R$ %.2f  (%s)",
                id, descricao, sinal, valor, tipo);
    }
}
