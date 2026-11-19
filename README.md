# Pizzaria – Java Swing + MySQL (JDBC)

## Como executar
1. No MySQL, rode `banco/01_script_banco.sql` e depois `banco/02_dados_teste.sql`.
2. Baixe o `mysql-connector-j` (arquivo .jar) e coloque em `lib/`.
3. Ajuste usuário/senha em `src/util/Conexao.java`.
4. Importe a pasta como projeto no NetBeans / IntelliJ / Eclipse (source folder = `src`,
   adicione o .jar de `lib/` ao classpath) e execute `view.Main`.
   Por linha de comando (Linux/Mac; no Windows troque `:` por `;`):
       javac -encoding UTF-8 -cp "lib/*" -d out $(find src -name "*.java")
       java -cp "out:lib/*" view.Main
5. Login: **admin / admin123**

## Estrutura
- `model`  – entidades (Pessoa → Cliente / Funcionario / Administrador, Sabor, Tamanho, PrecoSabor, Pedido, Pizza, Pagamento, Avaliacao)
- `dao`    – acesso ao banco (interface CrudDAO<T> + um DAO por tabela)
- `view`   – telas Swing (CrudPanel genérico, painéis por cadastro, PizzaDialog, LoginFrame, MainFrame, Main)
- `util`   – Conexao, Sessao, Formato
