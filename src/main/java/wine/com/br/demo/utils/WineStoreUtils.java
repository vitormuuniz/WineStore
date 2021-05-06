package wine.com.br.demo.utils;

import java.util.List;

import wine.com.br.demo.repository.WineStoreRepository;
import wine.com.br.demo.to.WineStoreTO;

public class WineStoreUtils {

	public static boolean canCreateOrUpdateWineStore(WineStoreTO wineStoreTO, WineStoreRepository wineStoreRepository) {

		List<WineStoreTO> winStoreList = wineStoreRepository.findAll();

		if (winStoreList.size() > 0) {
			for (WineStoreTO wineStore : winStoreList) {
				if (!zipRangeVerification(wineStoreTO, wineStore))
					return false;
			}
		}

		return true;
	}

	private static boolean zipRangeVerification(WineStoreTO wsToBeCreate, WineStoreTO databaseWs) {
		if (wsToBeCreate.getFaixaInicio() >= databaseWs.getFaixaInicio()
				&& wsToBeCreate.getFaixaInicio() <= databaseWs.getFaixaFim())
			return false;

		if (wsToBeCreate.getFaixaFim() >= databaseWs.getFaixaInicio()
				&& wsToBeCreate.getFaixaFim() <= databaseWs.getFaixaFim())
			return false;

		if (wsToBeCreate.getFaixaInicio() >= databaseWs.getFaixaInicio()
				&& wsToBeCreate.getFaixaFim() <= databaseWs.getFaixaFim())
			return false;

		if (wsToBeCreate.getFaixaInicio() <= databaseWs.getFaixaInicio()
				&& wsToBeCreate.getFaixaFim() >= databaseWs.getFaixaFim())
			return false;

		return true;
	}
}