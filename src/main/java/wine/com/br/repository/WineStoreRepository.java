package wine.com.br.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import wine.com.br.model.WineStore;

import java.util.List;

public interface WineStoreRepository extends JpaRepository<WineStore, Long>{

	List<WineStore> findByFaixaInicioGreaterThan(Long faixaInicio);
	@Query(value = "SELECT * FROM WINE_STORE WHERE faixa_inicio >= ?1 AND faixa_fim <= ?2", nativeQuery = true)
	List<WineStore> findBetweenFaixaInicioAndFaixaFim(Long faixaInicio, Long faixaFim);
	List<WineStore> findByFaixaFimLessThan(Long faixaFim);
	List<WineStore> findByCodigoLoja(String codigoLoja);
	@Query(value = "" +
			"SELECT * FROM WINE_STORE WHERE " +
			"(?1 NOT BETWEEN faixa_inicio AND faixa_fim) AND" +
			"(?2 NOT BETWEEN faixa_inicio AND faixa_fim) AND" +
			" NOT(?1 >= faixa_inicio AND ?2 <= faixa_fim) AND" +
			" NOT(?1 <= faixa_inicio AND ?2 >= faixa_fim)", nativeQuery = true)
	List<WineStore> findWineStoresFiltered(Long faixaInicio, Long faixaFim);
}
