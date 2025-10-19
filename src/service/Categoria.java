package service;

public class Categoria {
	private String idCategoria;
	private String nomeCategoria;
	private String descricaoCategoria;
	private String dataCriacaoCategoria;
	
	public static void main(String[] args) {
		
	}
	
	

	public Categoria(String idCategoria, String nomeCategoria, String descricaoCategoria, String dataCriacaoCategoria) {
		super();
		this.idCategoria = idCategoria;
		this.nomeCategoria = nomeCategoria;
		this.descricaoCategoria = descricaoCategoria;
		this.dataCriacaoCategoria = dataCriacaoCategoria;
	}

	public String getIdCategoria() {
		return idCategoria;
	}

	public void setIdCategoria(String idCategoria) {
		this.idCategoria = idCategoria;
	}

	public String getNomeCategoria() {
		return nomeCategoria;
	}

	public void setNomeCategoria(String nomeCategoria) {
		this.nomeCategoria = nomeCategoria;
	}

	public String getDescricaoCategoria() {
		return descricaoCategoria;
	}

	public void setDescricaoCategoria(String descricaoCategoria) {
		this.descricaoCategoria = descricaoCategoria;
	}

	public String getDataCriacaoCategoria() {
		return dataCriacaoCategoria;
	}

	public void setDataCriacaoCategoria(String dataCriacaoCategoria) {
		this.dataCriacaoCategoria = dataCriacaoCategoria;
	}
}
