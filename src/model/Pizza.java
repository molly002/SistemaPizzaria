package model;

import java.math.BigDecimal;

public class Pizza {
    private int id;
    private int idPedido;
    private Tamanho tamanho;
    private Sabor sabor1;
    private Sabor sabor2;        // opcional (pizza meio a meio)
    private BigDecimal valor;

    public Pizza(int id, int idPedido, Tamanho tamanho, Sabor sabor1, Sabor sabor2, BigDecimal valor) {
        this.id = id; this.idPedido = idPedido; this.tamanho = tamanho;
        this.sabor1 = sabor1; this.sabor2 = sabor2; this.valor = valor;
    }
    public int getId() { return id; }
    public int getIdPedido() { return idPedido; }
    public Tamanho getTamanho() { return tamanho; }
    public Sabor getSabor1() { return sabor1; }
    public Sabor getSabor2() { return sabor2; }
    public BigDecimal getValor() { return valor; }
}
