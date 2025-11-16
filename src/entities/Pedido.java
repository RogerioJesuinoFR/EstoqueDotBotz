package entities;

import java.util.List;
import java.util.ArrayList;

public class Pedido {
	
	private String idPedido;
	private String idUsuario; // Apenas o ID
    private String nomeUsuario; // Para exibição na tabela
	private String dataSolicitacaoPedido;
	private String dataPrevisaoPedido;
	private String statusPedido;
	private String dataCriacaoPedido;
    
    // CORREÇÃO: Um pedido tem uma lista de itens
	private List<PedidoItem> itensDoPedido;

    public Pedido() {
        this.itensDoPedido = new ArrayList<>();
    }
    
	public Pedido(String idPedido, String idUsuario, String dataSolicitacaoPedido,
			String dataPrevisaoPedido, String statusPedido, String dataCriacaoPedido) {
		this.idPedido = idPedido;
		this.idUsuario = idUsuario;
		this.dataSolicitacaoPedido = dataSolicitacaoPedido;
		this.dataPrevisaoPedido = dataPrevisaoPedido;
		this.statusPedido = statusPedido;
		this.dataCriacaoPedido = dataCriacaoPedido;
        this.itensDoPedido = new ArrayList<>();
	}

    // Getters e Setters
	public String getIdPedido() { return idPedido; }
	public void setIdPedido(String idPedido) { this.idPedido = idPedido; }

	public String getIdUsuario() { return idUsuario; }
	public void setIdUsuario(String idUsuario) { this.idUsuario = idUsuario; }
    
    public String getNomeUsuario() { return nomeUsuario; }
    public void setNomeUsuario(String nomeUsuario) { this.nomeUsuario = nomeUsuario; }

	public String getDataSolicitacaoPedido() { return dataSolicitacaoPedido; }
	public void setDataSolicitacaoPedido(String dataSolicitacaoPedido) { this.dataSolicitacaoPedido = dataSolicitacaoPedido; }

	public String getDataPrevisaoPedido() { return dataPrevisaoPedido; }
	public void setDataPrevisaoPedido(String dataPrevisaoPedido) { this.dataPrevisaoPedido = dataPrevisaoPedido; }

	public String getStatusPedido() { return statusPedido; }
	public void setStatusPedido(String statusPedido) { this.statusPedido = statusPedido; }

	public String getDataCriacaoPedido() { return dataCriacaoPedido; }
	public void setDataCriacaoPedido(String dataCriacaoPedido) { this.dataCriacaoPedido = dataCriacaoPedido; }

	public List<PedidoItem> getItensDoPedido() { return itensDoPedido; }
	public void setItensDoPedido(List<PedidoItem> itensDoPedido) { this.itensDoPedido = itensDoPedido; }
    
    public void adicionarItem(PedidoItem item) { this.itensDoPedido.add(item); }
}