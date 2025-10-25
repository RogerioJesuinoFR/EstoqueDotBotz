package entities;

import java.sql.Date;

public class Categoria {
	private String idCategoria;
	private String nomeCategoria;
	private String descricaoCategoria;
	private Date dataCriacaoCategoria;
	

	public Categoria(String idCategoria, String nomeCategoria, String descricaoCategoria, Date dataCriacaoCategoria) {
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

	public Date getDataCriacaoCategoria() {
		return dataCriacaoCategoria;
	}

	public void setDataCriacaoCategoria(Date dataCriacaoCategoria) {
		this.dataCriacaoCategoria = dataCriacaoCategoria;
	}
}
