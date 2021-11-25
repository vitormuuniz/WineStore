package wine.com.br.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import wine.com.br.exception.BaseException;
import wine.com.br.service.WineStoreService;
import wine.com.br.to.WineStoreTO;

import javax.transaction.Transactional;
import javax.validation.Valid;
import java.util.List;

@RestController
public class WineStoreController {

	@Autowired
	private WineStoreService wineStoreService;

	@PostMapping
	public ResponseEntity<WineStoreTO> registerRoom(@RequestBody @Valid WineStoreTO request) throws BaseException {
		WineStoreTO wineStore = wineStoreService.createWineStore(request);
		return ResponseEntity.status(HttpStatus.CREATED).body(wineStore);
	}

	@GetMapping
	public ResponseEntity<List<WineStoreTO>> listWineRooms(
			@RequestParam(required = false) Long faixaInicio,
			@RequestParam(required = false) Long faixaFim,
			@RequestParam(required = false) String codigoLoja) throws BaseException {
		List<WineStoreTO> wineStoreList = wineStoreService.listAllWineStores(faixaInicio, faixaFim, codigoLoja);
		return ResponseEntity.ok(wineStoreList);
	}

	@GetMapping("/{id}")
	public ResponseEntity<WineStoreTO> listOneWineStoreById(@PathVariable Long id) throws BaseException {
		WineStoreTO wineStore = wineStoreService.findWineStoreById(id);
		return ResponseEntity.ok(wineStore);
	}
	
	@PutMapping("/{id}")
	public ResponseEntity<WineStoreTO> updateWineStore(
			@RequestBody @Valid WineStoreTO form,
			@PathVariable Long id) throws BaseException {
		WineStoreTO wineStore = wineStoreService.updateWineStore(form, id);
		return ResponseEntity.ok(wineStore);
	}

	@DeleteMapping("/{id}")
	@Transactional
	public ResponseEntity<Object> deleteWineStore(@PathVariable Long id) throws BaseException {
		wineStoreService.deleteWineStore(id);
		return ResponseEntity.ok().build();
	}
}
