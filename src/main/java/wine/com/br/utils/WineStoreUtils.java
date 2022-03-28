package wine.com.br.utils;

import java.util.List;
import java.util.Objects;

import org.springframework.http.HttpStatus;
import org.springframework.util.StringUtils;

import wine.com.br.exception.BaseException;
import wine.com.br.repository.WineStoreRepository;
import wine.com.br.model.WineStore;

public class WineStoreUtils {

	private WineStoreUtils() {}

	public static void validateFields(WineStore to) throws BaseException {
		if (isNullOrEqualsZero(to.getFaixaInicio()) || isNullOrEqualsZero(to.getFaixaFim())) {
			throw new BaseException("faixaInicio and faixaFim must be non null and greater than zero", HttpStatus.BAD_REQUEST);
		}
		if (!StringUtils.hasText(to.getCodigoLoja())) {
			throw new BaseException("codigoLoja must be non null and not blank", HttpStatus.BAD_REQUEST);
		}
	}

	public static void validateFieldsRange(WineStore to, WineStoreRepository wineStoreRepository) throws BaseException {
		if (to.getFaixaFim() <= to.getFaixaInicio()) {
			throw new BaseException("faixaFim must be greater than faixaInicio", HttpStatus.BAD_REQUEST);
		}
		List<WineStore> wineStores = wineStoreRepository.findWineStoresFiltered(to.getFaixaInicio(), to.getFaixaFim());
		if (wineStoreRepository.count() > 0 && !wineStores.isEmpty()) {
			throw new BaseException("There is a zip range conflict, verify your data", HttpStatus.BAD_REQUEST);
		}
	}

	public static boolean isNullOrEqualsZero(Long faixa) {
		return Objects.isNull(faixa) || faixa == 0;
	}

	public static WineStore verifyIfExists(Long id, WineStoreRepository wineStoreRepository) throws BaseException {
		return wineStoreRepository.findById(id)
				.orElseThrow(() -> new BaseException("There isn't a wine store with id = " + id, HttpStatus.NOT_FOUND));
	}
}
