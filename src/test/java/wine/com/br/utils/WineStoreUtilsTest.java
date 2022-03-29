package wine.com.br.utils;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;

import wine.com.br.exception.BaseException;
import wine.com.br.model.WineStore;
import wine.com.br.repository.WineStoreRepository;

@SpringBootTest
@DirtiesContext(classMode = ClassMode.BEFORE_EACH_TEST_METHOD)
class WineStoreUtilsTest {

    @Autowired
    WineStoreRepository wineStoreRepository;

    private WineStore to;

    @BeforeEach
    void setup() {
        to = new WineStore();
        to.setCodigoLoja("12345");
        to.setFaixaInicio(13000L);
        to.setFaixaFim(14000L);
    }

    @Test
    void testValidateFields() {
        assertDoesNotThrow(() -> WineStoreUtils.validateFields(to));
    }

    @Test
    void testValidateFieldsThrowExceptionDueToCodigoLojaNull() {
        to.setCodigoLoja(null);
        BaseException ex = assertThrows(BaseException.class, () -> WineStoreUtils.validateFields(to),
                "It was expected that validateFields() thrown an exception, " +
                        "due to codigoLoja null");
        assertEquals(HttpStatus.BAD_REQUEST, ex.getHttpStatus());
        assertEquals("codigoLoja must be non null and not blank", ex.getMessage());
    }

    @Test
    void testValidateFieldsThrowExceptionDueToCodigoLojaEmpty() {
        to.setCodigoLoja("");
        BaseException ex = assertThrows(BaseException.class, () -> WineStoreUtils.validateFields(to),
                "It was expected that validateFields() thrown an exception, " +
                        "due to codigoLoja empty");
        assertEquals(HttpStatus.BAD_REQUEST, ex.getHttpStatus());
        assertEquals("codigoLoja must be non null and not blank", ex.getMessage());
    }

    @Test
    void testValidateFieldsThrowExceptionDueToFaixaInicioNull() {
        to.setFaixaInicio(null);
        BaseException ex = assertThrows(BaseException.class, () -> WineStoreUtils.validateFields(to),
                "It was expected that validateFields() thrown an exception, " +
                        "due to faixaInicio null");
        assertEquals(HttpStatus.BAD_REQUEST, ex.getHttpStatus());
        assertEquals("faixaInicio and faixaFim must be non null and greater than zero", ex.getMessage());
    }

    @Test
    void testValidateFieldsThrowExceptionDueToFaixaFimNull() {
        to.setFaixaFim(null);
        BaseException ex = assertThrows(BaseException.class, () -> WineStoreUtils.validateFields(to),
                "It was expected that validateFields() thrown an exception, " +
                        "due to faixaFim null");
        assertEquals(HttpStatus.BAD_REQUEST, ex.getHttpStatus());
        assertEquals("faixaInicio and faixaFim must be non null and greater than zero", ex.getMessage());
    }

    @Test
    void testValidateFieldsThrowExceptionDueToFaixaInicioNullEqualsZero() {
        to.setFaixaInicio(0L);
        BaseException ex = assertThrows(BaseException.class, () -> WineStoreUtils.validateFields(to),
                "It was expected that validateFields() thrown an exception, " +
                        "due to faixaInicio equals zero");
        assertEquals(HttpStatus.BAD_REQUEST, ex.getHttpStatus());
        assertEquals("faixaInicio and faixaFim must be non null and greater than zero", ex.getMessage());
    }

    @Test
    void testValidateFieldsThrowExceptionDueToFaixaFimEqualsZero() {
        to.setFaixaFim(0L);
        BaseException ex = assertThrows(BaseException.class, () -> WineStoreUtils.validateFields(to),
                "It was expected that validateFields() thrown an exception, " +
                        "due to faixaFim equals zero");
        assertEquals(HttpStatus.BAD_REQUEST, ex.getHttpStatus());
        assertEquals("faixaInicio and faixaFim must be non null and greater than zero", ex.getMessage());
    }

    @Test
    void testValidateFieldsRange() {
        assertDoesNotThrow(() -> WineStoreUtils.validateFieldsRange(to, wineStoreRepository));
    }

    @Test
    void testValidateFieldsThrowExceptionDueToFaixaInicioGreaterThanFaixaFim() {
        to.setFaixaFim(to.getFaixaInicio() - 1000L);
        BaseException ex = assertThrows(BaseException.class, () -> WineStoreUtils.validateFieldsRange(to, wineStoreRepository),
                "It was expected that validateFieldsRange() thrown an exception, " +
                        "due to faixaInicio greater than faixaFim");
        assertEquals(HttpStatus.BAD_REQUEST, ex.getHttpStatus());
        assertEquals("faixaFim must be greater than faixaInicio", ex.getMessage());
    }

    @Test
    void testValidateFieldsThrowExceptionDueToZipRangeConflitBetween() {
        WineStore wineStoreWithZipConflict = new WineStore();
        wineStoreWithZipConflict.setCodigoLoja("9999");
        wineStoreWithZipConflict.setFaixaInicio(to.getFaixaInicio() + 500L);
        wineStoreWithZipConflict.setFaixaFim(to.getFaixaFim() - 500L);

        wineStoreRepository.save(wineStoreWithZipConflict);

        BaseException ex = assertThrows(BaseException.class, () -> WineStoreUtils.validateFieldsRange(to, wineStoreRepository),
                "It was expected that validateFieldsRange() thrown an exception, " +
                        "due to zip range conflict");
        assertEquals(HttpStatus.BAD_REQUEST, ex.getHttpStatus());
        assertEquals("There is a zip range conflict, verify your data", ex.getMessage());
    }

    @Test
    void testValidateFieldsThrowExceptionDueToZipRangeConflitAround() {
        WineStore wineStoreWithZipConflict = new WineStore();
        wineStoreWithZipConflict.setCodigoLoja("9999");
        wineStoreWithZipConflict.setFaixaInicio(to.getFaixaInicio() - 500L);
        wineStoreWithZipConflict.setFaixaFim(to.getFaixaFim() + 500L);

        wineStoreRepository.save(wineStoreWithZipConflict);

        BaseException ex = assertThrows(BaseException.class, () -> WineStoreUtils.validateFieldsRange(to, wineStoreRepository),
                "It was expected that validateFieldsRange() thrown an exception, " +
                        "due to zip range conflict");
        assertEquals(HttpStatus.BAD_REQUEST, ex.getHttpStatus());
        assertEquals("There is a zip range conflict, verify your data", ex.getMessage());
    }
}
