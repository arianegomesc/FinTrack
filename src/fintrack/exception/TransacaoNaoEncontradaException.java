package fintrack.exception;

public class TransacaoNaoEncontradaException extends Exception {

    public TransacaoNaoEncontradaException(int id) {
        super("Transação com ID " + id + " não encontrada.");
    }
}
