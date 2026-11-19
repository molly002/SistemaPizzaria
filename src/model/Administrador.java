package model;

public class Administrador extends Pessoa {
    private String senha;
    public Administrador(int id, String nome, String senha) { super(id, nome); this.senha = senha; }
    public String getSenha() { return senha; }
    public void setSenha(String senha) { this.senha = senha; }
    @Override public String getTipo() { return "Administrador"; }
}
