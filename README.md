# 💰 FinTrack — Sistema de Controle de Finanças Pessoais

> Projeto desenvolvido como atividade prática da **Capacitação Java iREDE**

---

## 📋 Descrição

O **FinTrack** é um sistema de controle de finanças pessoais executado via terminal (console).  
Permite ao usuário cadastrar receitas e despesas, visualizar o extrato completo e acompanhar o saldo em tempo real.

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

- **Java 25**
- **JDK** (Java Development Kit)
- **IntelliJ IDEA** (IDE)
- Paradigma: **Orientação a Objetos (POO)**

---

## ▶️ Como Executar

### Pré-requisitos
- JDK 17 ou superior instalado
- Terminal ou IDE com suporte a Java

### Passos

```bash
# 1. Clone o repositório
git clone https://github.com/arianegomesc/FinTrack.git

# 2. Entre na pasta do projeto
cd FinTrack

# 3. Crie a pasta de saída
mkdir out

# 4. Compile o projeto
javac -d out src/fintrack/model/*.java src/fintrack/exception/*.java src/fintrack/controller/*.java src/fintrack/view/*.java src/fintrack/Main.java

# 5. Execute
java -cp out fintrack.Main
```

---

## 👩‍💻 Autora

**Ariane Gomes Cunha**  
Capacitação Java — iREDE  
[![GitHub](https://img.shields.io/badge/GitHub-arianegomesc-181717?style=flat&logo=github)](https://github.com/arianegomesc)
[![LinkedIn](https://img.shields.io/badge/LinkedIn-ariane--gomesc-0077B5?style=flat&logo=linkedin)](https://linkedin.com/in/ariane-gomesc)

