package wine.com.br.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import wine.com.br.exception.BaseException;
import wine.com.br.repository.WineStoreRepository;
import wine.com.br.to.WineStoreTO;
import wine.com.br.utils.WineStoreUtils;

import java.util.List;

@Service
public class WineStoreService {

	@Autowired
	private WineStoreRepository wineStoreRepository;

	public WineStoreTO createWineStore(WineStoreTO request) throws BaseException {
		WineStoreUtils.verifyAttributesNull(request);
		WineStoreTO wineStore = null;
		if (WineStoreUtils.canCreateOrUpdateWineStore(request, wineStoreRepository, false))
			wineStore = wineStoreRepository.save(request);
		return wineStore;
	}

	public List<WineStoreTO> listAllWineStores(Long faixaInicio, Long faixaFim, String codigoLoja) {
		if (codigoLoja != null) return wineStoreRepository.findByCodigoLoja(codigoLoja);
		if (faixaInicio != null) return wineStoreRepository.findByFaixaInicioGreaterThan(faixaInicio);
		if (faixaFim != null) return wineStoreRepository.findByFaixaFimLessThan(faixaFim);
		return wineStoreRepository.findAll();
	}

	public WineStoreTO findWineStoreById(Long id) throws BaseException {
		return WineStoreUtils.verifyIfExists(id, wineStoreRepository);
	}

	public WineStoreTO updateWineStore(WineStoreTO request, Long id) throws BaseException {
		WineStoreTO wineStore = WineStoreUtils.verifyIfExists(id, wineStoreRepository);
		if (WineStoreUtils.canCreateOrUpdateWineStore(request, wineStoreRepository, true)) {
			if (request.getCodigoLoja() != null) wineStore.setCodigoLoja(request.getCodigoLoja());
			if (request.getFaixaInicio() != null) wineStore.setFaixaInicio(request.getFaixaInicio());
			if (request.getFaixaFim() != null) wineStore.setFaixaFim(request.getFaixaFim());
			wineStoreRepository.save(wineStore);
		}
		return wineStore;
	}

	public void deleteWineStore(Long id) throws BaseException {
		WineStoreUtils.verifyIfExists(id, wineStoreRepository);
		wineStoreRepository.deleteById(id);
	}
}