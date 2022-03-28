package wine.com.br.service;

import static wine.com.br.utils.WineStoreUtils.isNullOrEqualsZero;
import static wine.com.br.utils.WineStoreUtils.validateFields;
import static wine.com.br.utils.WineStoreUtils.validateFieldsRange;
import static wine.com.br.utils.WineStoreUtils.verifyIfExists;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import wine.com.br.exception.BaseException;
import wine.com.br.repository.WineStoreRepository;
import wine.com.br.model.WineStore;

@Service
public class WineStoreService {

	private final WineStoreRepository wineStoreRepository;

	@Autowired
	public WineStoreService(WineStoreRepository wineStoreRepository) {
		this.wineStoreRepository = wineStoreRepository;
	}

	public WineStore createWineStore(WineStore request) throws BaseException {
		validateFields(request);
		validateFieldsRange(request, wineStoreRepository);
		return wineStoreRepository.save(request);
	}

	public List<WineStore> listAllWineStores(Long faixaInicio, Long faixaFim, String codigoLoja) {
		if (codigoLoja != null) {
			return wineStoreRepository.findByCodigoLoja(codigoLoja);
		}
		if (faixaInicio != null && faixaFim != null) {
			return wineStoreRepository.findBetweenFaixaInicioAndFaixaFim(faixaInicio, faixaFim);
		}
		if (faixaInicio != null) {
			return wineStoreRepository.findByFaixaInicioGreaterThan(faixaInicio);
		}
		if (faixaFim != null) {
			return wineStoreRepository.findByFaixaFimLessThan(faixaFim);
		}
		return wineStoreRepository.findAll();
	}

	public WineStore findWineStoreById(Long id) throws BaseException {
		return verifyIfExists(id, wineStoreRepository);
	}

	public WineStore updateWineStore(WineStore request, Long id) throws BaseException {
		WineStore wineStore = verifyIfExists(id, wineStoreRepository);
		if (StringUtils.hasText(request.getCodigoLoja())) {
			wineStore.setCodigoLoja(request.getCodigoLoja());
		}
		if (!isNullOrEqualsZero(request.getFaixaInicio())) {
			wineStore.setFaixaInicio(request.getFaixaInicio());
		}
		if (!isNullOrEqualsZero(request.getFaixaFim())) {
			wineStore.setFaixaFim(request.getFaixaFim());
		}
		return wineStoreRepository.save(wineStore);
	}

	public void deleteWineStore(Long id) throws BaseException {
		verifyIfExists(id, wineStoreRepository);
		wineStoreRepository.deleteById(id);
	}
}