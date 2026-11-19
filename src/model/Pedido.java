package model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class Pedido {
    private int id;
    private int idCliente;
    private String nomeCliente;
    private Integer idFunc;       // pode ser nulo (ainda sem funcionário)
    private String nomeFunc;
    private LocalDateTime dataHora;
    private String status;
    private BigDecimal valor = BigDecimal.ZERO;

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public int getIdCliente() { return idCliente; }
    public void setIdCliente(int idCliente) { this.idCliente = idCliente; }
    public String getNomeCliente() { return nomeCliente; }
    public void setNomeCliente(String nomeCliente) { this.nomeCliente = nomeCliente; }
    public Integer getIdFunc() { return idFunc; }
    public void setIdFunc(Integer idFunc) { this.idFunc = idFunc; }
    public String getNomeFunc() { return nomeFunc; }
    public void setNomeFunc(String nomeFunc) { this.nomeFunc = nomeFunc; }
    public LocalDateTime getDataHora() { return dataHora; }
    public void setDataHora(LocalDateTime dataHora) { this.dataHora = dataHora; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public BigDecimal getValor() { return valor; }
    public void setValor(BigDecimal valor) { this.valor = valor; }

    @Override public String toString() { return "#" + id + " - " + nomeCliente; }
}
