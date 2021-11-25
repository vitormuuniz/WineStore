package wine.com.br.to;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.lang.Nullable;

@Getter
@Setter
@NoArgsConstructor
public class WineStoreUpdateForm {
	
	@Nullable
	private String codigoLoja;
	@Nullable
	private Long faixaInicio;
	@Nullable
	private Long faixaFim;
}
