package fintrack.repository;

import java.util.List;
import java.util.Optional;

public interface RepositorioGenerico<T, ID> {
    T salvar(T entidade);
    void atualizar(T entidade);
    void remover(ID id);
    Optional<T> buscarPorId(ID id);
    List<T> listar();
}
