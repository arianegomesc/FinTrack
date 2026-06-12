package fintrack.exception;

public class ValorInvalidoException extends Exception {

    public ValorInvalidoException(double valor) {
        super("Valor inválido: R$ " + valor + ". O valor deve ser maior que zero.");
    }

    public ValorInvalidoException(String mensagem) {
        super(mensagem);
    }
}
