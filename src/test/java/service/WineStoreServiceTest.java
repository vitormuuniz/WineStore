package service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;

import wine.com.br.exception.BaseException;
import wine.com.br.model.WineStore;
import wine.com.br.repository.WineStoreRepository;
import wine.com.br.service.WineStoreService;

@SpringBootTest(classes = WineStoreService.class)
class WineStoreServiceTest {

    @Autowired
    private WineStoreService wineStoreService;

    @MockBean
    private WineStoreRepository wineStoreRepository;

    private WineStore wineStore;

    @BeforeEach
    void init() {
        wineStore = buildWineStore();
    }

    @Test
    void testCreateWineStore() throws BaseException {
        when(wineStoreRepository.findWineStoresFiltered(anyLong(), anyLong())).thenReturn(new ArrayList<>());
        when(wineStoreRepository.save(any())).thenReturn(wineStore);
        WineStore response = wineStoreService.createWineStore(wineStore);
        validateWineStore(response);
    }

    @Test
    void testCreateWineStoreWithCodigoLojaNullShouldThrowBaseException() {
        wineStore.setCodigoLoja(null);
        BaseException ex = assertThrows(BaseException.class, () -> wineStoreService.createWineStore(wineStore),
                "It was expected that createWineStore() thrown an exception, " +
                        "due to codigoLoja null");
        assertEquals(HttpStatus.BAD_REQUEST, ex.getHttpStatus());
    }

    @Test
    void testCreateWineStoreWithFaixaInicioNullShouldThrowBaseException() {
        wineStore.setFaixaInicio(null);
        BaseException ex = assertThrows(BaseException.class, () -> wineStoreService.createWineStore(wineStore),
                "It was expected that createWineStore() thrown an exception, " +
                        "due to faixaInicio null");
        assertEquals(HttpStatus.BAD_REQUEST, ex.getHttpStatus());
    }

    @Test
    void testCreateWineStoreWithFaixaFimNullShouldThrowBaseException() {
        wineStore.setFaixaFim(null);
        BaseException ex = assertThrows(BaseException.class, () -> wineStoreService.createWineStore(wineStore),
                "It was expected that createWineStore() thrown an exception, " +
                        "due to faixaFim null");
        assertEquals(HttpStatus.BAD_REQUEST, ex.getHttpStatus());
    }

    @Test
    void testCreateWineStoreWithFaixaInicioLessThanFaixaFimShouldThrowBaseException() {
        wineStore.setFaixaInicio(15000L);
        wineStore.setFaixaFim(14000L);
        BaseException ex = assertThrows(BaseException.class, () -> wineStoreService.createWineStore(wineStore),
                "It was expected that createWineStore() thrown an exception, " +
                        "due to faixaInicio less than faixaFim");
        assertEquals(HttpStatus.BAD_REQUEST, ex.getHttpStatus());
    }

    @Test
    void testCreateWineStoreWithZipRangeConflictShouldThrowBaseException() {
        when(wineStoreRepository.findWineStoresFiltered(anyLong(), anyLong())).thenReturn(List.of(new WineStore()));
        when(wineStoreRepository.count()).thenReturn(1L);
        BaseException ex = assertThrows(BaseException.class, () -> wineStoreService.createWineStore(wineStore),
                "It was expected that createWineStore() thrown an exception, " +
                        "due to zip range conflict");
        assertEquals(HttpStatus.BAD_REQUEST, ex.getHttpStatus());
    }

    @Test
    void listAllWineStoresByCodigoLoja() {
        when(wineStoreRepository.findByCodigoLoja(anyString())).thenReturn(List.of(wineStore));
        wineStoreService
                .listAllWineStores(null, null, wineStore.getCodigoLoja())
                .stream()
                .findFirst()
                .ifPresent(this::validateWineStore);
    }

    @Test
    void listAllWineStoresBetweenFaixaInicioAndFaixaFim() {
        WineStore wineStore1 = buildWineStore();
        wineStore1.setFaixaInicio(1000L);
        WineStore wineStore2 = buildWineStore();
        wineStore2.setFaixaInicio(20000L);
        when(wineStoreRepository.findByFaixaInicioGreaterThan(anyLong())).thenReturn(List.of(wineStore));
        wineStoreService.listAllWineStores(14000L, 19000L, null)
                .stream()
                .findFirst()
                .ifPresent(this::validateWineStore);;
    }

    @Test
    void listAllWineStoresByFaixaInicio() {
        WineStore wineStore1 = buildWineStore();
        wineStore1.setFaixaInicio(1000L);
        WineStore wineStore2 = buildWineStore();
        wineStore2.setFaixaInicio(20000L);
        when(wineStoreRepository.findByFaixaInicioGreaterThan(anyLong())).thenReturn(List.of(wineStore, wineStore2));
        List<WineStore> response = wineStoreService.listAllWineStores(13000L, null, null);
        assertEquals(2, response.size());
    }

    @Test
    void listAllWineStoresByFaixaFim() {
        WineStore wineStore1 = buildWineStore();
        wineStore1.setFaixaInicio(1000L);
        WineStore wineStore2 = buildWineStore();
        wineStore2.setFaixaInicio(20000L);
        when(wineStoreRepository.findByFaixaFimLessThan(anyLong())).thenReturn(List.of(wineStore, wineStore1, wineStore2));
        List<WineStore> response = wineStoreService.listAllWineStores(null, 20000L, null);
        assertEquals(3, response.size());
    }

    private WineStore buildWineStore() {
        wineStore = new WineStore();
        wineStore.setCodigoLoja("12345");
        wineStore.setFaixaInicio(13000L);
        wineStore.setFaixaFim(14000L);
        return wineStore;
    }

    private void validateWineStore(WineStore response) {
        assertEquals(wineStore.getCodigoLoja(), response.getCodigoLoja());
        assertEquals(wineStore.getFaixaInicio(), response.getFaixaInicio());
        assertEquals(wineStore.getFaixaFim(), response.getFaixaFim());
    }
}
