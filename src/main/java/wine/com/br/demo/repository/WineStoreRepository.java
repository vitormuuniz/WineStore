package wine.com.br.demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import wine.com.br.demo.to.WineStoreTO;

public interface WineStoreRepository extends JpaRepository<WineStoreTO, Long>{

	List<WineStoreTO> findByFaixaInicioGreaterThan(Long faixaInicio);
	List<WineStoreTO> findByFaixaFimLessThan(Long faixaFim);
	List<WineStoreTO> findByCodigoLoja(String codigoLoja);
}
