package model;

public class Cliente extends Pessoa {
    public Cliente(int id, String nome) { super(id, nome); }
    @Override public String getTipo() { return "Cliente"; }
}
