package model;

public class Sabor {
    private int id;
    private String nome;
    private String descricao;

    public Sabor(int id, String nome, String descricao) { this.id = id; this.nome = nome; this.descricao = descricao; }
    public int getId() { return id; }
    public String getNome() { return nome; }
    public String getDescricao() { return descricao; }
    @Override public String toString() { return nome; }
}
