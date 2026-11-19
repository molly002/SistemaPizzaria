package model;

/** Funcionário da cozinha (tabela cozinha). */
public class Funcionario extends Pessoa {
    public Funcionario(int id, String nome) { super(id, nome); }
    @Override public String getTipo() { return "Funcionário"; }
}
