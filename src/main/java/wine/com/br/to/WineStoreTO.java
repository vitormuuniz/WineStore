package wine.com.br.to;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.lang.Nullable;

@Entity
@Getter
@Setter
@NoArgsConstructor
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
}
