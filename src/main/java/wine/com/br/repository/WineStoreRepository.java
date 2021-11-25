package wine.com.br.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import wine.com.br.to.WineStoreTO;

import java.util.List;

public interface WineStoreRepository extends JpaRepository<WineStoreTO, Long>{

	List<WineStoreTO> findByFaixaInicioGreaterThan(Long faixaInicio);
	List<WineStoreTO> findByFaixaFimLessThan(Long faixaFim);
	List<WineStoreTO> findByCodigoLoja(String codigoLoja);
	@Query(value = "" +
			"SELECT * FROM WINE_STORE WHERE " +
			"(?1 NOT BETWEEN faixa_inicio AND faixa_fim) AND" +
			"(?2 NOT BETWEEN faixa_inicio AND faixa_fim) AND" +
			" NOT(?1 >= faixa_inicio AND ?2 <= faixa_fim) AND" +
			" NOT(?1 <= faixa_inicio AND ?2 >= faixa_fim)", nativeQuery = true)
	List<WineStoreTO> findWineStoresFiltered(Long faixaInicio, Long faixaFim);
}
