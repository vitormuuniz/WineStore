package wine.com.br.utils;

import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import wine.com.br.exception.BaseException;
import wine.com.br.repository.WineStoreRepository;
import wine.com.br.to.WineStoreTO;

public class WineStoreUtils {

	private WineStoreUtils() {}

	public static boolean canCreateOrUpdateWineStore(WineStoreTO wineStoreRequest,
													 WineStoreRepository wineStoreRepository,
													 boolean isUpdate) throws BaseException {
		if (wineStoreRequest.getFaixaInicio() != null && wineStoreRequest.getFaixaFim() != null) {
			if (wineStoreRequest.getFaixaFim() <= wineStoreRequest.getFaixaInicio()) throw new BaseException(
					"faixaFim must be greater than faixaInicio",
					HttpStatus.BAD_REQUEST
			);
			List<WineStoreTO> winStoreList = wineStoreRepository
					.findWineStoresFiltered(wineStoreRequest.getFaixaInicio(), wineStoreRequest.getFaixaFim());
			if (winStoreList.isEmpty())
				throw new BaseException("There is a zip range conflict, verify your data", HttpStatus.BAD_REQUEST);
		} else if (!isUpdate) throw new BaseException(
				"faixaFim and faixaInicio must be greater than zero and must be not null",
				HttpStatus.BAD_REQUEST
		);
		return true;
	}
	
	public static void verifyAttributesNull(WineStoreTO to) throws BaseException {
		if(to.getCodigoLoja() == null || to.getFaixaInicio() == null || to.getFaixaFim() == null)
			throw new BaseException(
					"All of the fields must not be null, verify your data",
					HttpStatus.BAD_REQUEST
			);
	}

	public static WineStoreTO verifyIfExists(Long id, WineStoreRepository wineStoreRepository) throws BaseException {
		Optional<WineStoreTO> wineStoreOp = wineStoreRepository.findById(id);
		if (wineStoreOp.isEmpty()) throw new BaseException(
				"There isn't a wine store with id = $id",
				HttpStatus.NOT_FOUND
		);
		return wineStoreOp.get();
	}
}
