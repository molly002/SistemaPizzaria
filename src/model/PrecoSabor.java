package model;

import java.math.BigDecimal;

/** Preço de um sabor em determinado tamanho (relação N:N entre sabor e tamanho). */
public class PrecoSabor {
    private int id;
    private Sabor sabor;
    private Tamanho tamanho;
    private BigDecimal valor;

    public PrecoSabor(int id, Sabor sabor, Tamanho tamanho, BigDecimal valor) {
        this.id = id; this.sabor = sabor; this.tamanho = tamanho; this.valor = valor;
    }
    public int getId() { return id; }
    public Sabor getSabor() { return sabor; }
    public Tamanho getTamanho() { return tamanho; }
    public BigDecimal getValor() { return valor; }
}
