package wine.com.br.controller;

import java.util.List;

import javax.transaction.Transactional;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import wine.com.br.exception.BaseException;
import wine.com.br.service.WineStoreService;
import wine.com.br.to.WineStoreTO;

@RestController
@RequestMapping("/wine-stores")
public class WineStoreController {

	@Autowired
	private WineStoreService wineStoreService;

	@PostMapping
	public ResponseEntity<WineStoreTO> registerRoom(@RequestBody @Valid WineStoreTO request, UriComponentsBuilder uriBuilder)
			throws BaseException {

		WineStoreTO wineStore = wineStoreService.createWineStore(request);

        	return ResponseEntity.status(HttpStatus.CREATED).body(wineStore)
	}

	@GetMapping
	public ResponseEntity<List<WineStoreTO>> listWineRooms(@RequestParam(required = false) Long faixaInicio,
			@RequestParam(required = false) Long faixaFim, @RequestParam(required = false) String codigoLoja) throws BaseException {
		
		List<WineStoreTO> wineStoreList = wineStoreService.listAllWineStores(faixaInicio, faixaFim, codigoLoja);

		return ResponseEntity.ok(wineStoreList);

	}

	@GetMapping("/{id}")
	public ResponseEntity<WineStoreTO> listOneWineStoreById(@PathVariable Long id) throws BaseException {
		
		WineStoreTO wineStore = wineStoreService.findWineStoreById(id);

		return ResponseEntity.ok(wineStore);
	}
	
	@PutMapping("/{id}")
	public ResponseEntity<WineStoreTO> updateWineStore(@RequestBody @Valid WineStoreTO form, @PathVariable Long id) throws BaseException {
		
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
