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
import java.util.Locale;

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
    @FXML private Button btnSalvar;
    @FXML private Button btnCancelar;
    @FXML private Label totalReceitas;
    @FXML private Label totalDespesas;
    @FXML private Label saldo;
    @FXML private Label status;

    private TransacaoDAO dao;
    private Transacao transacaoEmEdicao;

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

        tabela.setRowFactory(tv -> {
            TableRow<Transacao> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && !row.isEmpty()) {
                    preencherCamposParaEdicao(row.getItem());
                }
            });
            return row;
        });

        atualizar();
    }

    @FXML
    private void salvar() {
        try {
            String texto = descricao.getText();
            if (texto == null || texto.isBlank()) {
                mostrarErro("Dados inválidos", "Informe uma descrição válida.");
                return;
            }

            String valorTexto = valor.getText() != null ? valor.getText().trim().replace(',', '.') : "";
            if (valorTexto.isBlank()) {
                mostrarErro("Dados inválidos", "Informe um valor numérico positivo.");
                return;
            }

            double quantia;
            try {
                quantia = Double.parseDouble(valorTexto);
            } catch (NumberFormatException e) {
                mostrarErro("Dados inválidos", "O valor informado é inválido. Exemplo: 150.00");
                return;
            }

            if (quantia <= 0) {
                mostrarErro("Dados inválidos", "O valor deve ser maior que zero.");
                return;
            }

            LocalDate dataVal = data.getValue();
            if (dataVal == null) {
                mostrarErro("Dados inválidos", "Selecione uma data para a transação.");
                return;
            }

            TipoTransacao tipoVal = tipo.getValue();
            if (tipoVal == null) {
                mostrarErro("Dados inválidos", "Selecione o tipo da transação.");
                return;
            }

            if (transacaoEmEdicao == null) {
                dao.salvar(new Transacao(texto.trim(), quantia, tipoVal, dataVal));
                status.setText("Transação cadastrada com sucesso.");
            } else {
                Transacao atualizada = new Transacao(transacaoEmEdicao.getId(), texto.trim(), quantia, tipoVal, dataVal);
                dao.atualizar(atualizada);
                status.setText("Transação (ID: " + transacaoEmEdicao.getId() + ") atualizada com sucesso.");
            }

            limparFormulario();
            atualizar();
        } catch (IllegalStateException e) {
            mostrarErro("Erro de persistência", e.getMessage());
        } catch (Exception e) {
            mostrarErro("Erro inesperado", e.getMessage());
        }
    }

    @FXML
    private void carregarParaEdicao() {
        Transacao selecionada = tabela.getSelectionModel().getSelectedItem();
        if (selecionada == null) {
            mostrarErro("Editar transação", "Selecione uma transação na tabela para editar.");
            return;
        }
        preencherCamposParaEdicao(selecionada);
    }

    private void preencherCamposParaEdicao(Transacao transacao) {
        this.transacaoEmEdicao = transacao;
        descricao.setText(transacao.getDescricao());
        valor.setText(String.format(Locale.US, "%.2f", transacao.getValor()));
        data.setValue(transacao.getData());
        tipo.setValue(transacao.getTipo());

        btnSalvar.setText("Atualizar");
        if (btnCancelar != null) {
            btnCancelar.setVisible(true);
            btnCancelar.setManaged(true);
        }
        status.setText("Editando transação ID: " + transacao.getId());
    }

    @FXML
    private void cancelarEdicao() {
        limparFormulario();
        status.setText("Edição cancelada.");
    }

    @FXML
    private void remover() {
        Transacao selecionada = tabela.getSelectionModel().getSelectedItem();
        if (selecionada == null) {
            mostrarErro("Remover transação", "Selecione uma transação na tabela.");
            return;
        }

        if (transacaoEmEdicao != null && transacaoEmEdicao.getId() == selecionada.getId()) {
            limparFormulario();
        }

        dao.remover(selecionada.getId());
        status.setText("Transação removida.");
        atualizar();
    }

    private void limparFormulario() {
        transacaoEmEdicao = null;
        descricao.clear();
        valor.clear();
        data.setValue(LocalDate.now());
        tipo.getSelectionModel().select(TipoTransacao.RECEITA);
        btnSalvar.setText("Adicionar");
        if (btnCancelar != null) {
            btnCancelar.setVisible(false);
            btnCancelar.setManaged(false);
        }
    }

    private void atualizar() {
        var itens = dao.listar();
        tabela.setItems(FXCollections.observableArrayList(itens));

        double receitasTotal = itens.stream()
                .filter(t -> t.getTipo() == TipoTransacao.RECEITA)
                .mapToDouble(Transacao::getValor)
                .sum();

        double despesasTotal = itens.stream()
                .filter(t -> t.getTipo() == TipoTransacao.DESPESA)
                .mapToDouble(Transacao::getValor)
                .sum();

        double saldoTotal = receitasTotal - despesasTotal;

        if (totalReceitas != null) {
            totalReceitas.setText(String.format("Receitas: R$ %.2f", receitasTotal));
        }
        if (totalDespesas != null) {
            totalDespesas.setText(String.format("Despesas: R$ %.2f", despesasTotal));
        }
        if (saldo != null) {
            saldo.setText(String.format("Saldo: R$ %.2f", saldoTotal));
        }
    }

    private void mostrarErro(String titulo, String mensagem) {
        Alert alert = new Alert(Alert.AlertType.ERROR, mensagem, ButtonType.OK);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.showAndWait();
    }
}
