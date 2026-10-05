package fintrack.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

public final class Conexao {
    private static final Map<String, Connection> MEMORIA = new HashMap<>();

    private Conexao() {}

    public static Connection abrir() throws SQLException {
        String configuredUrl = System.getProperty("fintrack.db", "jdbc:sqlite:fintrack.db");
        String url = configuredUrl.equals("jdbc:sqlite::memory:")
                ? "jdbc:sqlite:file:fintrack_test?mode=memory&cache=shared" : configuredUrl;
        if (url.contains("mode=memory")) {
            synchronized (MEMORIA) {
                Connection anchor = MEMORIA.get(url);
                if (anchor == null || anchor.isClosed()) {
                    anchor = DriverManager.getConnection(url);
                    MEMORIA.put(url, anchor);
                }
            }
        }
        Connection connection = DriverManager.getConnection(url);
        connection.setAutoCommit(true);
        return connection;
    }

    public static void inicializar() throws SQLException {
        try (Connection connection = abrir(); Statement statement = connection.createStatement()) {
            statement.executeUpdate("""
                CREATE TABLE IF NOT EXISTS transacoes (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    descricao TEXT NOT NULL,
                    valor NUMERIC NOT NULL CHECK (valor > 0),
                    tipo TEXT NOT NULL CHECK (tipo IN ('RECEITA', 'DESPESA')),
                    data TEXT NOT NULL
                )
                """);
        }
    }
}
