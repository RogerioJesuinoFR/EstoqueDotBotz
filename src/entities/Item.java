package entities;


public class Item {
	private String idItem;
	private String nomeItem;
	private Categoria categoria;
	private String descricaoItem;
	private int quantidadeAtualItem;
	private int quantidadeMinimaItem;
	private String unidadeMedidaItem;
	private String validadeItem;
	private String setorItem;
	private String dataCriacaoItem;
	
	
	public Item() {
		
		this.categoria = new Categoria();
	}
	
	public Item(String idItem, String nomeItem, Categoria categoria, String descricaoItem, int quantidadeAtualItem,
			int quantidadeMinimaItem, String unidadeMedidaItem, String validadeItem, String setorItem,
			String dataCriacaoItem) {
		super();
		this.idItem = idItem;
		this.nomeItem = nomeItem;
		
        // CORREÇÃO CRÍTICA: Garante que a categoria nunca seja nula
        if (categoria == null) {
		    this.categoria = new Categoria();
        } else {
            this.categoria = categoria;
        }
        
		this.descricaoItem = descricaoItem;
		this.quantidadeAtualItem = quantidadeAtualItem;
		this.quantidadeMinimaItem = quantidadeMinimaItem;
		this.unidadeMedidaItem = unidadeMedidaItem;
		this.validadeItem = validadeItem;
		this.setorItem = setorItem;
		this.dataCriacaoItem = dataCriacaoItem;
	}

	public String getIdItem() {
		return idItem;
	}

	public void setIdItem(String idItem) {
		this.idItem = idItem;
	}

	public String getNomeItem() {
		return nomeItem;
	}

	public void setNomeItem(String nomeItem) {
		this.nomeItem = nomeItem;
	}

	public String getCategoria() {
		return categoria.getIdCategoria();
	}

	public void setCategoria(String idCategoria) {
		this.categoria.setIdCategoria(idCategoria);
	}

	public String getDescricaoItem() {
		return descricaoItem;
	}

	public void setDescricaoItem(String descricaoItem) {
		this.descricaoItem = descricaoItem;
	}

	public int getQuantidadeAtualItem() {
		return quantidadeAtualItem;
	}

	public void setQuantidadeAtualItem(int quantidadeAtualItem) {
		this.quantidadeAtualItem = quantidadeAtualItem;
	}

	public int getQuantidadeMinimaItem() {
		return quantidadeMinimaItem;
	}

	public void setQuantidadeMinimaItem(int quantidadeMinimaItem) {
		this.quantidadeMinimaItem = quantidadeMinimaItem;
	}

	public String getUnidadeMedidaItem() {
		return unidadeMedidaItem;
	}

	public void setUnidadeMedidaItem(String unidadeMedidaItem) {
		this.unidadeMedidaItem = unidadeMedidaItem;
	}

	public String getValidadeItem() {
		return validadeItem;
	}

	public void setValidadeItem(String validadeItem) {
		this.validadeItem = validadeItem;
	}

	public String getSetorItem() {
		return setorItem;
	}

	public void setSetorItem(String setorItem) {
		this.setorItem = setorItem;
	}

	public String getDataCriacaoItem() {
		return dataCriacaoItem;
	}

	public void setDataCriacaoItem(String dataCriacaoItem) {
		this.dataCriacaoItem = dataCriacaoItem;
	}
}
