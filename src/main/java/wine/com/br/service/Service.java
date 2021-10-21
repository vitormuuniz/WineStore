package wine.com.br.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;

import wine.com.br.exception.BaseException;
import wine.com.br.repository.Repository;
import wine.com.br.to.WineStoreTO;
import wine.com.br.utils.Utils;

@Service
public class Service {

	@Autowired
	private Repository wineStoreRepository;

	public WineStoreTO createWineStore(WineStoreTO form, UriComponentsBuilder uriBuilder) throws BaseException {
		
		if (form.getFaixaFim() <= form.getFaixaInicio())
			throw new BaseException("FAIXA_FIM MUST BE GREATER THAN FAIXA_INICIO", HttpStatus.BAD_REQUEST);
		
		if (form.getCodigoLoja() == null || form.getFaixaInicio() == null || form.getFaixaFim() == null)
			throw new BaseException("All of the fields must not be null, verify your data", HttpStatus.BAD_REQUEST);

		boolean canCreateWineStore = Utils.canCreateOrUpdateWineStore(form, wineStoreRepository);

		if (!canCreateWineStore) {
			throw new BaseException("There is a zip range conflit, verify your data", HttpStatus.BAD_REQUEST);
		}

		return wineStoreRepository.save(form);
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

	public WineStoreTO updateWineStore(WineStoreTO form, Long id) throws BaseException {
		
		if (form.getFaixaFim() <= form.getFaixaInicio())
			throw new BaseException("FAIXA_FIM MUST BE GREATER THAN FAIXA_INICIO", HttpStatus.BAD_REQUEST);

		Optional<WineStoreTO> wineStoreOp = wineStoreRepository.findById(id);

		if (!wineStoreOp.isPresent())
			throw new BaseException("There isn't a wine store with id = " + id, HttpStatus.NOT_FOUND);

		boolean canCreateWineStore = Utils.canCreateOrUpdateWineStore(form, wineStoreRepository);

		if (!canCreateWineStore) {
			throw new BaseException("There is a zip range conflit, verify your data", HttpStatus.BAD_REQUEST);
		}

		WineStoreTO wineStore = wineStoreOp.get();

		if (form.getCodigoLoja() != null) {
			wineStore.setCodigoLoja(form.getCodigoLoja());
		}

		if (form.getFaixaInicio() != null) {
			wineStore.setFaixaInicio(form.getFaixaInicio());
		}

		if (form.getFaixaFim() != null) {
			wineStore.setFaixaFim(form.getFaixaFim());
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