package wine.com.br.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import wine.com.br.exception.BaseException;
import wine.com.br.repository.WineStoreRepository;
import wine.com.br.to.WineStoreTO;
import wine.com.br.utils.WineStoreUtils;

@Service
public class WineStoreService {

	@Autowired
	private WineStoreRepository wineStoreRepository;

	public WineStoreTO createWineStore(WineStoreTO request) throws BaseException {
		
		if (WineStoreUtils.atributtesAreNull(request))
			throw new BaseException("All of the fields must not be null, verify your data", HttpStatus.BAD_REQUEST);
		
		if (request.getFaixaFim() <= request.getFaixaInicio())
			throw new BaseException("FAIXA_FIM must be greater than FAIXA_INICIO", HttpStatus.BAD_REQUEST);

		if (!WineStoreUtils.canCreateOrUpdateWineStore(request, wineStoreRepository)) {
			throw new BaseException("There is a zip range conflit, verify your data", HttpStatus.BAD_REQUEST);
		}

		return wineStoreRepository.save(request);
	}

	public List<WineStoreTO> listAllWineStores(Long faixaInicio, Long faixaFim, String codigoLoja) {

		if (codigoLoja != null)
			return wineStoreRepository.findByCodigoLoja(codigoLoja);

		if (faixaInicio != null)
			return wineStoreRepository.findByFaixaInicioGreaterThan(faixaInicio);

		if (faixaFim != null)
			return wineStoreRepository.findByFaixaFimLessThan(faixaFim);

		return wineStoreRepository.findAll();
	}

	public WineStoreTO findWineStoreById(Long id) throws BaseException {

		Optional<WineStoreTO> wineStoreOp = wineStoreRepository.findById(id);

		if (!wineStoreOp.isPresent())
			throw new BaseException("There isn't a wine store with id = " + id, HttpStatus.NOT_FOUND);

		return wineStoreOp.get();
	}

	public WineStoreTO updateWineStore(WineStoreTO request, Long id) throws BaseException {
		
		if (request.getFaixaFim() <= request.getFaixaInicio())
			throw new BaseException("FAIXA_FIM MUST BE GREATER THAN FAIXA_INICIO", HttpStatus.BAD_REQUEST);

		Optional<WineStoreTO> wineStoreOp = wineStoreRepository.findById(id);

		if (!wineStoreOp.isPresent())
			throw new BaseException("There isn't a wine store with id = " + id, HttpStatus.NOT_FOUND);

		if (!WineStoreUtils.canCreateOrUpdateWineStore(request, wineStoreRepository)) {
			throw new BaseException("There is a zip range conflit, verify your data", HttpStatus.BAD_REQUEST);
		}

		WineStoreTO wineStore = wineStoreOp.get();

		if (request.getCodigoLoja() != null) {
			wineStore.setCodigoLoja(request.getCodigoLoja());
		}

		if (request.getFaixaInicio() != null) {
			wineStore.setFaixaInicio(request.getFaixaInicio());
		}

		if (request.getFaixaFim() != null) {
			wineStore.setFaixaFim(request.getFaixaFim());
		}

		wineStoreRepository.save(wineStore);

		return wineStore;
	}

	public void deleteWineStore(Long id) throws BaseException {

		Optional<WineStoreTO> room = wineStoreRepository.findById(id);

		if (!room.isPresent())
			throw new BaseException("Wine Store ID haven't found", HttpStatus.NOT_FOUND);

		wineStoreRepository.deleteById(id);
	}
}