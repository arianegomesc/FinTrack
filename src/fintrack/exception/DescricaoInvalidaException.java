package fintrack.exception;

public class DescricaoInvalidaException extends Exception {

    public DescricaoInvalidaException() {
        super("Descrição inválida: não pode ser vazia ou conter apenas espaços.");
    }

    public DescricaoInvalidaException(String mensagem) {
        super(mensagem);
    }
}
