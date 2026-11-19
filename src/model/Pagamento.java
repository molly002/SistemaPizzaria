package model;

public class Pagamento {
    private int id;
    private int idPedido;
    private String status;
    private String metodo;

    public Pagamento(int id, int idPedido, String status, String metodo) {
        this.id = id; this.idPedido = idPedido; this.status = status; this.metodo = metodo;
    }
    public int getId() { return id; }
    public int getIdPedido() { return idPedido; }
    public String getStatus() { return status; }
    public String getMetodo() { return metodo; }
}
