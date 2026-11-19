package model;

/** Classe abstrata base para Cliente, Funcionario (cozinha) e Administrador. */
public abstract class Pessoa {
    private int id;
    private String nome;

    protected Pessoa(int id, String nome) { this.id = id; this.nome = nome; }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    /** Polimorfismo: cada subclasse informa seu tipo. */
    public abstract String getTipo();

    @Override public String toString() { return nome; }
}
