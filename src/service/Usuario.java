package service;

public class Usuario {
	
	private String idUsuario;
	private String nomeUsuario;
	private String RAUsuario;
	private String setorUsuario;
	private String senhaUsuario;
	private String dataCriacaoUsuario;
	
	public static void main(String[] args) {
		
	}
	
	

	public Usuario(String idUsuario, String nomeUsuario, String rAUsuario, String setorUsuario, String senhaUsuario,
			String dataCriacaoUsuario) {
		super();
		this.idUsuario = idUsuario;
		this.nomeUsuario = nomeUsuario;
		RAUsuario = rAUsuario;
		this.setorUsuario = setorUsuario;
		this.senhaUsuario = senhaUsuario;
		this.dataCriacaoUsuario = dataCriacaoUsuario;
	}

	public String getIdUsuario() {
		return idUsuario;
	}

	public void setIdUsuario(String idUsuario) {
		this.idUsuario = idUsuario;
	}

	public String getNomeUsuario() {
		return nomeUsuario;
	}

	public void setNomeUsuario(String nomeUsuario) {
		this.nomeUsuario = nomeUsuario;
	}

	public String getRAUsuario() {
		return RAUsuario;
	}

	public void setRAUsuario(String rAUsuario) {
		RAUsuario = rAUsuario;
	}

	public String getSetorUsuario() {
		return setorUsuario;
	}

	public void setSetorUsuario(String setorUsuario) {
		this.setorUsuario = setorUsuario;
	}

	public String getSenhaUsuario() {
		return senhaUsuario;
	}

	public void setSenhaUsuario(String senhaUsuario) {
		this.senhaUsuario = senhaUsuario;
	}

	public String getDataCriacaoUsuario() {
		return dataCriacaoUsuario;
	}

	public void setDataCriacaoUsuario(String dataCriacaoUsuario) {
		this.dataCriacaoUsuario = dataCriacaoUsuario;
	}
}
