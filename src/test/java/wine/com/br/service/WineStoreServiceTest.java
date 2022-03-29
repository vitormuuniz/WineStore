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

@SpringBootTest(classes = WineStoreService.class)
class WineStoreServiceTest {

    @Autowired
    private WineStoreService service;

    @MockBean
    private WineStoreRepository repository;

    private WineStore to;

    @BeforeEach
    void init() {
        to = buildWineStore();
    }

    @Test
    void testCreateWineStore() throws BaseException {
        when(repository.findWineStoresFiltered(anyLong(), anyLong())).thenReturn(new ArrayList<>());
        when(repository.save(any())).thenReturn(to);
        WineStore response = service.createWineStore(to);
        validateWineStore(response);
    }

    @Test
    void testCreateWineStoreWithCodigoLojaNullShouldThrowBaseException() {
        to.setCodigoLoja(null);
        BaseException ex = assertThrows(BaseException.class, () -> service.createWineStore(to),
                "It was expected that createWineStore() thrown an exception, " +
                        "due to codigoLoja null");
        assertEquals(HttpStatus.BAD_REQUEST, ex.getHttpStatus());
        verify(repository, times(0)).save(to);
    }

    @Test
    void testCreateWineStoreWithFaixaInicioNullShouldThrowBaseException() {
        to.setFaixaInicio(null);
        BaseException ex = assertThrows(BaseException.class, () -> service.createWineStore(to),
                "It was expected that createWineStore() thrown an exception, " +
                        "due to faixaInicio null");
        assertEquals(HttpStatus.BAD_REQUEST, ex.getHttpStatus());
        verify(repository, times(0)).save(to);
    }

    @Test
    void testCreateWineStoreWithFaixaFimNullShouldThrowBaseException() {
        to.setFaixaFim(null);
        BaseException ex = assertThrows(BaseException.class, () -> service.createWineStore(to),
                "It was expected that createWineStore() thrown an exception, " +
                        "due to faixaFim null");
        assertEquals(HttpStatus.BAD_REQUEST, ex.getHttpStatus());
        verify(repository, times(0)).save(to);
    }

    @Test
    void testCreateWineStoreWithFaixaInicioLessThanFaixaFimShouldThrowBaseException() {
        to.setFaixaInicio(15000L);
        to.setFaixaFim(14000L);
        BaseException ex = assertThrows(BaseException.class, () -> service.createWineStore(to),
                "It was expected that createWineStore() thrown an exception, " +
                        "due to faixaInicio less than faixaFim");
        assertEquals(HttpStatus.BAD_REQUEST, ex.getHttpStatus());
        verify(repository, times(0)).save(to);
    }

    @Test
    void testCreateWineStoreWithZipRangeConflictShouldThrowBaseException() {
        when(repository.findWineStoresFiltered(anyLong(), anyLong())).thenReturn(List.of(new WineStore()));
        when(repository.count()).thenReturn(1L);
        BaseException ex = assertThrows(BaseException.class, () -> service.createWineStore(to),
                "It was expected that createWineStore() thrown an exception, " +
                        "due to zip range conflict");
        assertEquals(HttpStatus.BAD_REQUEST, ex.getHttpStatus());
        verify(repository, times(0)).save(to);
    }

    @Test
    void testListAllWineStoresByCodigoLoja() {
        when(repository.findByCodigoLoja(anyString())).thenReturn(List.of(to));
        service
                .listWineStores(null, null, to.getCodigoLoja())
                .stream()
                .findFirst()
                .ifPresent(this::validateWineStore);
        verify(repository, times(1)).findByCodigoLoja(to.getCodigoLoja());
    }

    @Test
    void testListAllWineStores() {
        WineStore wineStore1 = buildWineStore();
        wineStore1.setFaixaInicio(1000L);
        when(repository.findAll()).thenReturn(List.of(to, wineStore1));
        List<WineStore> response = service.listWineStores(null, null, null);
        assertEquals(2, response.size());
        verify(repository, times(1)).findAll();
    }

    @Test
    void testListAllWineStoresBetweenFaixaInicioAndFaixaFim() {
        WineStore wineStore1 = buildWineStore();
        wineStore1.setFaixaInicio(1000L);
        WineStore wineStore2 = buildWineStore();
        wineStore2.setFaixaInicio(20000L);
        when(repository.findByFaixaInicioGreaterThan(anyLong())).thenReturn(List.of(to));
        service.listWineStores(14000L, 19000L, null)
                .stream()
                .findFirst()
                .ifPresent(this::validateWineStore);
        verify(repository, times(1))
                .findBetweenFaixaInicioAndFaixaFim(14000L, 19000L);
    }

    @Test
    void testListAllWineStoresByFaixaInicio() {
        WineStore wineStore1 = buildWineStore();
        wineStore1.setFaixaInicio(1000L);
        WineStore wineStore2 = buildWineStore();
        wineStore2.setFaixaInicio(20000L);
        when(repository.findByFaixaInicioGreaterThan(anyLong())).thenReturn(List.of(to, wineStore2));
        List<WineStore> response = service.listWineStores(13000L, null, null);
        assertEquals(2, response.size());
        verify(repository, times(1))
                .findByFaixaInicioGreaterThan(13000L);
    }

    @Test
    void testListAllWineStoresByFaixaFim() {
        WineStore wineStore1 = buildWineStore();
        wineStore1.setFaixaInicio(1000L);
        WineStore wineStore2 = buildWineStore();
        wineStore2.setFaixaInicio(20000L);
        when(repository.findByFaixaFimLessThan(anyLong())).thenReturn(List.of(to, wineStore1, wineStore2));
        List<WineStore> response = service.listWineStores(null, 20000L, null);
        assertEquals(3, response.size());
        verify(repository, times(1))
                .findByFaixaFimLessThan(20000L);
    }

    @Test
    void testFindById() throws BaseException {
        when(repository.findById(anyLong())).thenReturn(Optional.of(to));
        WineStore response = service.findWineStoreById(to.getId());
        validateWineStore(response);
    }

    @Test
    void testFindByIdShouldThrowException() {
        when(repository.findById(anyLong())).thenThrow(new BaseException("any exception message", HttpStatus.NOT_FOUND));
        BaseException ex = assertThrows(BaseException.class, () -> service.findWineStoreById(to.getId()),
                "It was expected that findWineStoreById() thrown an exception, " +
                        "due to wine store not found");
        assertEquals(HttpStatus.NOT_FOUND, ex.getHttpStatus());
    }

    @Test
    void testUpdateWineStoreCodigoLoja() {
        when(repository.findById(anyLong())).thenReturn(Optional.of(to));
        to.setCodigoLoja("999");
        when(repository.save(any())).thenReturn(to);
        WineStore wineStoreToBeUpdated = new WineStore();
        wineStoreToBeUpdated.setCodigoLoja("999");
        WineStore response = service.updateWineStore(wineStoreToBeUpdated, 123L);
        assertEquals(wineStoreToBeUpdated.getCodigoLoja(), response.getCodigoLoja());
    }

    @Test
    void testUpdateWineStoreFaixaInicio() {
        when(repository.findById(anyLong())).thenReturn(Optional.of(to));
        to.setFaixaInicio(15000L);
        when(repository.save(any())).thenReturn(to);
        WineStore wineStoreToBeUpdated = new WineStore();
        wineStoreToBeUpdated.setFaixaInicio(15000L);
        WineStore response = service.updateWineStore(wineStoreToBeUpdated, 123L);
        assertEquals(wineStoreToBeUpdated.getFaixaInicio(), response.getFaixaInicio());
    }

    @Test
    void testUpdateWineStoreFaixaFim() {
        when(repository.findById(anyLong())).thenReturn(Optional.of(to));
        to.setFaixaFim(16000L);
        when(repository.save(any())).thenReturn(to);
        WineStore wineStoreToBeUpdated = new WineStore();
        wineStoreToBeUpdated.setFaixaFim(16000L);
        WineStore response = service.updateWineStore(wineStoreToBeUpdated, 123L);
        assertEquals(wineStoreToBeUpdated.getFaixaFim(), response.getFaixaFim());
    }

    @Test
    void testUpdateWineStoreShouldThrowException() {
        when(repository.findById(anyLong())).thenThrow(new BaseException("any exception message", HttpStatus.NOT_FOUND));
        WineStore wineStoreToBeUpdated = new WineStore();
        wineStoreToBeUpdated.setFaixaFim(16000L);
        BaseException ex = assertThrows(BaseException.class, () -> service.updateWineStore(wineStoreToBeUpdated, 123L),
                "It was expected that updateWineStore() thrown an exception, " +
                        "due to wine store not found");
        assertEquals(HttpStatus.NOT_FOUND, ex.getHttpStatus());
        verify(repository, times(0)).save(to);
    }

    @Test
    void testDeleteWineStore() throws BaseException {
        when(repository.findById(anyLong())).thenReturn(Optional.of(to));
        service.deleteWineStore(to.getId());
        verify(repository, times(1)).deleteById(to.getId());
    }

    @Test
    void testDeleteWineStoreShouldThrowException() {
        when(repository.findById(anyLong())).thenThrow(new BaseException("any exception message", HttpStatus.NOT_FOUND));
        BaseException ex = assertThrows(BaseException.class, () -> service.deleteWineStore(123L),
                "It was expected that deleteWineStore() thrown an exception, " +
                        "due to wine store not found");
        assertEquals(HttpStatus.NOT_FOUND, ex.getHttpStatus());
        verify(repository, times(0)).deleteById(to.getId());
    }

    private WineStore buildWineStore() {
        to = new WineStore();
        to.setId(123L);
        to.setCodigoLoja("12345");
        to.setFaixaInicio(13000L);
        to.setFaixaFim(14000L);
        return to;
    }

    private void validateWineStore(WineStore response) {
        assertEquals(to.getCodigoLoja(), response.getCodigoLoja());
        assertEquals(to.getFaixaInicio(), response.getFaixaInicio());
        assertEquals(to.getFaixaFim(), response.getFaixaFim());
    }
}
