package entities;

public class Pedido {
	
	private String idPedido;
	private Usuario usuarioPedido;
	private Item itemPedido;
	private String dataSolicitacaoPedido;
	private String dataPrevisaoPedido;
	private String statusPedido;
	private String dataCriacaoPedido;
	private String quantidadeCompradaPedido;
	
	public static void main(String[] args) {
		
	}
	
	

	public String getIdPedido() {
		return idPedido;
	}

	public void setIdPedido(String idPedido) {
		this.idPedido = idPedido;
	}

	public Usuario getUsuarioPedido() {
		return usuarioPedido;
	}

	public void setUsuarioPedido(Usuario usuarioPedido) {
		this.usuarioPedido = usuarioPedido;
	}

	public Item getItemPedido() {
		return itemPedido;
	}

	public void setItemPedido(Item itemPedido) {
		this.itemPedido = itemPedido;
	}

	public String getDataSolicitacaoPedido() {
		return dataSolicitacaoPedido;
	}

	public void setDataSolicitacaoPedido(String dataSolicitacaoPedido) {
		this.dataSolicitacaoPedido = dataSolicitacaoPedido;
	}

	public String getDataPrevisaoPedido() {
		return dataPrevisaoPedido;
	}

	public void setDataPrevisaoPedido(String dataPrevisaoPedido) {
		this.dataPrevisaoPedido = dataPrevisaoPedido;
	}

	public String getStatusPedido() {
		return statusPedido;
	}

	public void setStatusPedido(String statusPedido) {
		this.statusPedido = statusPedido;
	}

	public String getDataCriacaoPedido() {
		return dataCriacaoPedido;
	}

	public void setDataCriacaoPedido(String dataCriacaoPedido) {
		this.dataCriacaoPedido = dataCriacaoPedido;
	}

	public String getQuantidadeCompradaPedido() {
		return quantidadeCompradaPedido;
	}

	public void setQuantidadeCompradaPedido(String quantidadeCompradaPedido) {
		this.quantidadeCompradaPedido = quantidadeCompradaPedido;
	}

	public Pedido(String idPedido, Usuario usuarioPedido, Item itemPedido, String dataSolicitacaoPedido,
			String dataPrevisaoPedido, String statusPedido, String dataCriacaoPedido, String quantidadeCompradaPedido) {
		super();
		this.idPedido = idPedido;
		this.usuarioPedido = usuarioPedido;
		this.itemPedido = itemPedido;
		this.dataSolicitacaoPedido = dataSolicitacaoPedido;
		this.dataPrevisaoPedido = dataPrevisaoPedido;
		this.statusPedido = statusPedido;
		this.dataCriacaoPedido = dataCriacaoPedido;
		this.quantidadeCompradaPedido = quantidadeCompradaPedido;
	}
}
