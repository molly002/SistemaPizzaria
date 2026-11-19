package model;

public class Tamanho {
    private int id;
    private int qtdPedacos;

    public Tamanho(int id, int qtdPedacos) { this.id = id; this.qtdPedacos = qtdPedacos; }
    public int getId() { return id; }
    public int getQtdPedacos() { return qtdPedacos; }
    @Override public String toString() { return qtdPedacos + " pedaços"; }
}
