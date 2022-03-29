package wine.com.br.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
        verify(wineStoreRepository, times(0)).save(wineStore);
    }

    @Test
    void testCreateWineStoreWithFaixaInicioNullShouldThrowBaseException() {
        wineStore.setFaixaInicio(null);
        BaseException ex = assertThrows(BaseException.class, () -> wineStoreService.createWineStore(wineStore),
                "It was expected that createWineStore() thrown an exception, " +
                        "due to faixaInicio null");
        assertEquals(HttpStatus.BAD_REQUEST, ex.getHttpStatus());
        verify(wineStoreRepository, times(0)).save(wineStore);
    }

    @Test
    void testCreateWineStoreWithFaixaFimNullShouldThrowBaseException() {
        wineStore.setFaixaFim(null);
        BaseException ex = assertThrows(BaseException.class, () -> wineStoreService.createWineStore(wineStore),
                "It was expected that createWineStore() thrown an exception, " +
                        "due to faixaFim null");
        assertEquals(HttpStatus.BAD_REQUEST, ex.getHttpStatus());
        verify(wineStoreRepository, times(0)).save(wineStore);
    }

    @Test
    void testCreateWineStoreWithFaixaInicioLessThanFaixaFimShouldThrowBaseException() {
        wineStore.setFaixaInicio(15000L);
        wineStore.setFaixaFim(14000L);
        BaseException ex = assertThrows(BaseException.class, () -> wineStoreService.createWineStore(wineStore),
                "It was expected that createWineStore() thrown an exception, " +
                        "due to faixaInicio less than faixaFim");
        assertEquals(HttpStatus.BAD_REQUEST, ex.getHttpStatus());
        verify(wineStoreRepository, times(0)).save(wineStore);
    }

    @Test
    void testCreateWineStoreWithZipRangeConflictShouldThrowBaseException() {
        when(wineStoreRepository.findWineStoresFiltered(anyLong(), anyLong())).thenReturn(List.of(new WineStore()));
        when(wineStoreRepository.count()).thenReturn(1L);
        BaseException ex = assertThrows(BaseException.class, () -> wineStoreService.createWineStore(wineStore),
                "It was expected that createWineStore() thrown an exception, " +
                        "due to zip range conflict");
        assertEquals(HttpStatus.BAD_REQUEST, ex.getHttpStatus());
        verify(wineStoreRepository, times(0)).save(wineStore);
    }

    @Test
    void testListAllWineStoresByCodigoLoja() {
        when(wineStoreRepository.findByCodigoLoja(anyString())).thenReturn(List.of(wineStore));
        wineStoreService
                .listAllWineStores(null, null, wineStore.getCodigoLoja())
                .stream()
                .findFirst()
                .ifPresent(this::validateWineStore);
        verify(wineStoreRepository, times(1)).findByCodigoLoja(wineStore.getCodigoLoja());
    }

    @Test
    void testListAllWineStores() {
        WineStore wineStore1 = buildWineStore();
        wineStore1.setFaixaInicio(1000L);
        when(wineStoreRepository.findAll()).thenReturn(List.of(wineStore, wineStore1));
        List<WineStore> response = wineStoreService.listAllWineStores(null, null, null);
        assertEquals(2, response.size());
        verify(wineStoreRepository, times(1)).findAll();
    }

    @Test
    void testListAllWineStoresBetweenFaixaInicioAndFaixaFim() {
        WineStore wineStore1 = buildWineStore();
        wineStore1.setFaixaInicio(1000L);
        WineStore wineStore2 = buildWineStore();
        wineStore2.setFaixaInicio(20000L);
        when(wineStoreRepository.findByFaixaInicioGreaterThan(anyLong())).thenReturn(List.of(wineStore));
        wineStoreService.listAllWineStores(14000L, 19000L, null)
                .stream()
                .findFirst()
                .ifPresent(this::validateWineStore);
        verify(wineStoreRepository, times(1))
                .findBetweenFaixaInicioAndFaixaFim(14000L, 19000L);
    }

    @Test
    void testListAllWineStoresByFaixaInicio() {
        WineStore wineStore1 = buildWineStore();
        wineStore1.setFaixaInicio(1000L);
        WineStore wineStore2 = buildWineStore();
        wineStore2.setFaixaInicio(20000L);
        when(wineStoreRepository.findByFaixaInicioGreaterThan(anyLong())).thenReturn(List.of(wineStore, wineStore2));
        List<WineStore> response = wineStoreService.listAllWineStores(13000L, null, null);
        assertEquals(2, response.size());
        verify(wineStoreRepository, times(1))
                .findByFaixaInicioGreaterThan(13000L);
    }

    @Test
    void testListAllWineStoresByFaixaFim() {
        WineStore wineStore1 = buildWineStore();
        wineStore1.setFaixaInicio(1000L);
        WineStore wineStore2 = buildWineStore();
        wineStore2.setFaixaInicio(20000L);
        when(wineStoreRepository.findByFaixaFimLessThan(anyLong())).thenReturn(List.of(wineStore, wineStore1, wineStore2));
        List<WineStore> response = wineStoreService.listAllWineStores(null, 20000L, null);
        assertEquals(3, response.size());
        verify(wineStoreRepository, times(1))
                .findByFaixaFimLessThan(20000L);
    }

    @Test
    void testFindById() throws BaseException {
        when(wineStoreRepository.findById(anyLong())).thenReturn(Optional.of(wineStore));
        WineStore response = wineStoreService.findWineStoreById(wineStore.getId());
        validateWineStore(response);
    }

    @Test
    void testFindByIdShouldThrowException() {
        when(wineStoreRepository.findById(anyLong())).thenThrow(new BaseException("any exception message", HttpStatus.NOT_FOUND));
        BaseException ex = assertThrows(BaseException.class, () -> wineStoreService.findWineStoreById(wineStore.getId()),
                "It was expected that findWineStoreById() thrown an exception, " +
                        "due to wine store not found");
        assertEquals(HttpStatus.NOT_FOUND, ex.getHttpStatus());
    }

    @Test
    void testUpdateWineStoreCodigoLoja() {
        when(wineStoreRepository.findById(anyLong())).thenReturn(Optional.of(wineStore));
        wineStore.setCodigoLoja("999");
        when(wineStoreRepository.save(any())).thenReturn(wineStore);
        WineStore wineStoreToBeUpdated = new WineStore();
        wineStoreToBeUpdated.setCodigoLoja("999");
        WineStore response = wineStoreService.updateWineStore(wineStoreToBeUpdated, 123L);
        assertEquals(wineStoreToBeUpdated.getCodigoLoja(), response.getCodigoLoja());
    }

    @Test
    void testUpdateWineStoreFaixaInicio() {
        when(wineStoreRepository.findById(anyLong())).thenReturn(Optional.of(wineStore));
        wineStore.setFaixaInicio(15000L);
        when(wineStoreRepository.save(any())).thenReturn(wineStore);
        WineStore wineStoreToBeUpdated = new WineStore();
        wineStoreToBeUpdated.setFaixaInicio(15000L);
        WineStore response = wineStoreService.updateWineStore(wineStoreToBeUpdated, 123L);
        assertEquals(wineStoreToBeUpdated.getFaixaInicio(), response.getFaixaInicio());
    }

    @Test
    void testUpdateWineStoreFaixaFim() {
        when(wineStoreRepository.findById(anyLong())).thenReturn(Optional.of(wineStore));
        wineStore.setFaixaFim(16000L);
        when(wineStoreRepository.save(any())).thenReturn(wineStore);
        WineStore wineStoreToBeUpdated = new WineStore();
        wineStoreToBeUpdated.setFaixaFim(16000L);
        WineStore response = wineStoreService.updateWineStore(wineStoreToBeUpdated, 123L);
        assertEquals(wineStoreToBeUpdated.getFaixaFim(), response.getFaixaFim());
    }

    @Test
    void testUpdateWineStoreShouldThrowException() {
        when(wineStoreRepository.findById(anyLong())).thenThrow(new BaseException("any exception message", HttpStatus.NOT_FOUND));
        WineStore wineStoreToBeUpdated = new WineStore();
        wineStoreToBeUpdated.setFaixaFim(16000L);
        BaseException ex = assertThrows(BaseException.class, () -> wineStoreService.updateWineStore(wineStoreToBeUpdated, 123L),
                "It was expected that updateWineStore() thrown an exception, " +
                        "due to wine store not found");
        assertEquals(HttpStatus.NOT_FOUND, ex.getHttpStatus());
        verify(wineStoreRepository, times(0)).save(wineStore);
    }

    @Test
    void testDeleteWineStore() throws BaseException {
        when(wineStoreRepository.findById(anyLong())).thenReturn(Optional.of(wineStore));
        wineStoreService.deleteWineStore(wineStore.getId());
        verify(wineStoreRepository, times(1)).deleteById(wineStore.getId());
    }

    @Test
    void testDeleteWineStoreShouldThrowException() {
        when(wineStoreRepository.findById(anyLong())).thenThrow(new BaseException("any exception message", HttpStatus.NOT_FOUND));
        BaseException ex = assertThrows(BaseException.class, () -> wineStoreService.deleteWineStore(123L),
                "It was expected that deleteWineStore() thrown an exception, " +
                        "due to wine store not found");
        assertEquals(HttpStatus.NOT_FOUND, ex.getHttpStatus());
        verify(wineStoreRepository, times(0)).deleteById(wineStore.getId());
    }

    private WineStore buildWineStore() {
        wineStore = new WineStore();
        wineStore.setId(123L);
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
