package wine.com.br.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.net.URI;
import java.net.URISyntaxException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import com.fasterxml.jackson.databind.ObjectMapper;

import wine.com.br.model.WineStore;
import wine.com.br.repository.WineStoreRepository;

@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = ClassMode.BEFORE_EACH_TEST_METHOD)
class WineStoreControllerTest {

    @Autowired
    private WineStoreRepository wineStoreRepository;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private static URI uri;

    private WineStore to;

    @BeforeEach
    void init() throws URISyntaxException {
        uri = new URI("/api/v1");
        to = buildWineStore();
    }

    @Test
    void testCreateWineStore() throws Exception {
        MvcResult response = mockMvc.perform(post(uri)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(to)))
                .andExpect(status().isCreated())
                .andReturn();

        String contentAsString = response.getResponse().getContentAsString();

        WineStore wineStoreObjectResponse = objectMapper.readValue(contentAsString, WineStore.class);

        validateWineStore(wineStoreObjectResponse);
        assertTrue(wineStoreRepository.findById(wineStoreObjectResponse.getId()).isPresent());
    }

    @Test
    void testListAllWineStores() throws Exception {
        WineStore to2 = new WineStore();
        to2.setCodigoLoja("123456");
        to2.setFaixaInicio(15000L);
        to2.setFaixaFim(16000L);

        wineStoreRepository.save(to);
        wineStoreRepository.save(to2);

        MvcResult response = mockMvc.perform(get(uri))
                .andExpect(status().isOk())
                .andReturn();

        String contentAsString = response.getResponse().getContentAsString();

        WineStore[] wineStoreObjectResponse = objectMapper.readValue(contentAsString, WineStore[].class);

        assertEquals(2, wineStoreObjectResponse.length);
    }

    @Test
    void testListOneWineStoreById() throws Exception {
        WineStore to2 = new WineStore();
        to2.setCodigoLoja("123456");
        to2.setFaixaInicio(15000L);
        to2.setFaixaFim(16000L);

        WineStore savedWineStore = wineStoreRepository.save(to);
        wineStoreRepository.save(to2);

        MvcResult response = mockMvc.perform(get(uri+"/"+savedWineStore.getId()))
                .andExpect(status().isOk())
                .andReturn();

        String contentAsString = response.getResponse().getContentAsString();

        WineStore wineStoreObjectResponse = objectMapper.readValue(contentAsString, WineStore.class);

        validateWineStore(wineStoreObjectResponse);
    }

    @Test
    void testUpdateWineStore() throws Exception {
        WineStore wineStoreDB = wineStoreRepository.save(to);

        WineStore wineStoreToBeUpdated = new WineStore();
        wineStoreToBeUpdated.setCodigoLoja("999999");

        MvcResult response = mockMvc.perform(put(uri + "/" + wineStoreDB.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(wineStoreToBeUpdated)))
                .andExpect(status().isOk())
                .andReturn();

        String contentAsString = response.getResponse().getContentAsString();

        WineStore wineStoreObjectResponse = objectMapper.readValue(contentAsString, WineStore.class);

        assertEquals(wineStoreToBeUpdated.getCodigoLoja(), wineStoreObjectResponse.getCodigoLoja());
    }

    @Test
    void testDeleteWineStore() throws Exception {
        WineStore savedTo = wineStoreRepository.save(to);

        mockMvc.perform(delete(uri+"/"+savedTo.getId()))
                .andExpect(status().isOk())
                .andReturn();

        assertTrue(wineStoreRepository.findById(savedTo.getId()).isEmpty());
    }

    private WineStore buildWineStore() {
        to = new WineStore();
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
