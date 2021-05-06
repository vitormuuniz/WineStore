package wine.com.br.demo.to;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import org.springframework.lang.Nullable;

@Entity
public class WineStoreTO {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Nullable
	private String codigoLoja;
	@Nullable
	private Long faixaInicio;
	@Nullable
	private Long faixaFim;
	
	public WineStoreTO() {
		
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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
