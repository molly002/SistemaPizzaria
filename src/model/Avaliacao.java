package model;

import java.time.LocalDate;

public class Avaliacao {
    private int id;
    private int idPedido;
    private int idCliente;
    private String nomeCliente;
    private int nota;
    private String comentario;
    private LocalDate data;

    public Avaliacao(int id, int idPedido, int idCliente, String nomeCliente, int nota, String comentario, LocalDate data) {
        this.id = id; this.idPedido = idPedido; this.idCliente = idCliente; this.nomeCliente = nomeCliente;
        this.nota = nota; this.comentario = comentario; this.data = data;
    }
    public int getId() { return id; }
    public int getIdPedido() { return idPedido; }
    public int getIdCliente() { return idCliente; }
    public String getNomeCliente() { return nomeCliente; }
    public int getNota() { return nota; }
    public String getComentario() { return comentario; }
    public LocalDate getData() { return data; }
}
