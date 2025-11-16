package entities;

// Esta classe representa a tabela 'pedido_itens'
public class PedidoItem {

    private String idItem;
    private String nomeItem; // Usado para exibição
    private int quantidadeComprada;
    
    // Construtor usado ao ler do banco
    public PedidoItem(String idItem, String nomeItem, int quantidadeComprada) {
        this.idItem = idItem;
        this.nomeItem = nomeItem;
        this.quantidadeComprada = quantidadeComprada;
    }
    
    // Construtor usado ao criar na GUI (pode precisar da entidade Item)
    public PedidoItem(String idItem, int quantidadeComprada) {
        this.idItem = idItem;
        this.quantidadeComprada = quantidadeComprada;
    }

    // Getters e Setters
    public String getIdItem() { return idItem; }
    public String getNomeItem() { return nomeItem; }
    public void setNomeItem(String nome) { this.nomeItem = nome; }
    public int getQuantidadeComprada() { return quantidadeComprada; }
}