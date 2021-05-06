package wine.com.br.demo.to;

import org.springframework.lang.Nullable;

public class WineStoreUpdateForm {
	
	@Nullable
	private String codigoLoja;
	@Nullable
	private Long faixaInicio;
	@Nullable
	private Long faixaFim;
	
	public WineStoreUpdateForm() {
		
	}

	public String getCodigoLoja() {
		return codigoLoja;
	}

	public void setCodigoLoja(String codigoLoja) {
		this.codigoLoja = codigoLoja;
	}

	public Long getFaixaInicio() {
		return faixaInicio;
	}

	public void setFaixaInicio(Long faixaInicio) {
		this.faixaInicio = faixaInicio;
	}

	public Long getFaixaFim() {
		return faixaFim;
	}

	public void setFaixaFim(Long faixaFim) {
		this.faixaFim = faixaFim;
	}
}
