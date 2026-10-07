# 💰 FinTrack — Sistema de Controle de Finanças Pessoais

> Projeto desenvolvido como atividade prática da **Capacitação Java iREDE**

---

## 📋 Descrição

O **FinTrack** possui uma versão console e uma interface gráfica JavaFX. Permite cadastrar
receitas e despesas, visualizar o extrato completo, acompanhar o saldo e remover transações.

---

## ✅ Funcionalidades

- 📥 Cadastrar **receitas** (entradas)
- 📤 Cadastrar **despesas** (saídas)
- 📄 Listar todas as transações
- 💵 Exibir saldo atual (receitas − despesas)
- 🗑️ Remover uma transação pelo ID

---

## 🏗️ Estrutura do Projeto

```
FinTrack/
└── src/
    └── fintrack/
        ├── model/
        │   ├── Transacao.java              # Entidade principal
        │   └── TipoTransacao.java          # Enum: RECEITA | DESPESA
        ├── controller/
        │   └── FinanceiroController.java   # Regras de negócio
        ├── exception/
        │   ├── ValorInvalidoException.java
        │   ├── DescricaoInvalidaException.java
        │   └── TransacaoNaoEncontradaException.java
        ├── view/
        │   └── ConsoleView.java            # Interface com o usuário
        └── Main.java                       # Ponto de entrada
```

---

## 🧠 Conceitos de POO Aplicados

| Conceito | Aplicação |
|---|---|
| **Encapsulamento** | Atributos `private` com getters e setters em `Transacao` |
| **Enum** | `TipoTransacao` tipando as categorias de transação |
| **Exceptions customizadas** | 3 classes que estendem `Exception` para erros de negócio |
| **Separação de responsabilidades** | Camadas Model / Controller / View bem definidas |
| **Imutabilidade** | Campos `id` e `tipo` declarados como `final` |
| **Collections** | `List<Transacao>` com acesso protegido via `unmodifiableList` |

---

## 🛠️ Tecnologias

- **Java 25 (LTS) + JavaFX 25**
- **JDK** (Java Development Kit)
- **IntelliJ IDEA** (IDE)
- Paradigma: **Orientação a Objetos (POO)**

---

## ▶️ Como Executar

### Pré-requisitos
- JDK 25 e Maven instalados

### Passos

```bash
# Interface JavaFX (o SQLite é criado em fintrack.db)
mvn javafx:run

# Versão console
mvn package
java -cp target/classes fintrack.Main

# Testes unitários, incluindo o DAO em SQLite em memória
mvn test
```

### Estrutura adicional

- `RepositorioGenerico<T, ID>` define as operações reutilizáveis de persistência.
- `Conexao` centraliza a conexão SQLite e a criação da tabela `transacoes`.
- `TransacaoDAO` implementa o CRUD com `PreparedStatement` e `ResultSet`.
- `FinApp`, `main.fxml` e `style.css` compõem a interface JavaFX.

---

## 👩‍💻 Autora

**Ariane Gomes Cunha**  
Capacitação Java — iREDE  
[![GitHub](https://img.shields.io/badge/GitHub-arianegomesc-181717?style=flat&logo=github)](https://github.com/arianegomesc)
[![LinkedIn](https://img.shields.io/badge/LinkedIn-ariane--gomesc-0077B5?style=flat&logo=linkedin)](https://linkedin.com/in/ariane-gomesc)
