package fintrack.view;

import fintrack.dao.TransacaoDAO;
import fintrack.model.TipoTransacao;
import fintrack.model.Transacao;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.sql.SQLException;
import java.time.LocalDate;

public class MainController {
    @FXML private TableView<Transacao> tabela;
    @FXML private TableColumn<Transacao, LocalDate> colunaData;
    @FXML private TableColumn<Transacao, String> colunaDescricao;
    @FXML private TableColumn<Transacao, Number> colunaValor;
    @FXML private TableColumn<Transacao, TipoTransacao> colunaTipo;
    @FXML private TextField descricao;
    @FXML private TextField valor;
    @FXML private DatePicker data;
    @FXML private ComboBox<TipoTransacao> tipo;
    @FXML private Label saldo;
    @FXML private Label status;

    private TransacaoDAO dao;

    @FXML
    private void initialize() {
        try {
            dao = new TransacaoDAO();
        } catch (SQLException e) {
            mostrarErro("Banco de dados", e.getMessage());
            return;
        }
        colunaData.setCellValueFactory(new PropertyValueFactory<>("data"));
        colunaDescricao.setCellValueFactory(new PropertyValueFactory<>("descricao"));
        colunaValor.setCellValueFactory(new PropertyValueFactory<>("valor"));
        colunaTipo.setCellValueFactory(new PropertyValueFactory<>("tipo"));
        tipo.setItems(FXCollections.observableArrayList(TipoTransacao.values()));
        tipo.getSelectionModel().select(TipoTransacao.RECEITA);
        data.setValue(LocalDate.now());
        atualizar();
    }

    @FXML
    private void salvar() {
        try {
            String texto = descricao.getText();
            double quantia = Double.parseDouble(valor.getText().trim().replace(',', '.'));
            if (texto == null || texto.isBlank() || quantia <= 0 || data.getValue() == null) {
                throw new IllegalArgumentException("Preencha descrição, valor positivo e data.");
            }
            dao.salvar(new Transacao(texto, quantia, tipo.getValue(), data.getValue()));
            descricao.clear();
            valor.clear();
            status.setText("Transação cadastrada.");
            atualizar();
        } catch (IllegalArgumentException e) {
            mostrarErro("Dados inválidos", "Informe uma descrição e um valor positivo.");
        } catch (IllegalStateException e) {
            mostrarErro("Erro de persistência", e.getMessage());
        }
    }

    @FXML
    private void remover() {
        Transacao selecionada = tabela.getSelectionModel().getSelectedItem();
        if (selecionada == null) {
            mostrarErro("Remover transação", "Selecione uma transação na tabela.");
            return;
        }
        dao.remover(selecionada.getId());
        status.setText("Transação removida.");
        atualizar();
    }

    private void atualizar() {
        var itens = dao.listar();
        tabela.setItems(FXCollections.observableArrayList(itens));
        double total = itens.stream().mapToDouble(t -> t.getTipo() == TipoTransacao.RECEITA ? t.getValor() : -t.getValor()).sum();
        saldo.setText(String.format("Saldo: R$ %.2f", total));
    }

    private void mostrarErro(String titulo, String mensagem) {
        new Alert(Alert.AlertType.ERROR, mensagem, ButtonType.OK).showAndWait();
    }
}
