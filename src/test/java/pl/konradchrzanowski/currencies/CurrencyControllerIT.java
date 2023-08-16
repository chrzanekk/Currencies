package pl.konradchrzanowski.currencies;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import pl.konradchrzanowski.currencies.domain.Currency;
import pl.konradchrzanowski.currencies.payload.CurrencyRequest;
import pl.konradchrzanowski.currencies.payload.CurrencyValueResponse;
import pl.konradchrzanowski.currencies.repository.CurrencyRepository;
import pl.konradchrzanowski.currencies.service.client.dto.CurrencyDTO;
import pl.konradchrzanowski.currencies.service.client.mapper.CurrencyMapper;
import pl.konradchrzanowski.currencies.service.nbp.NbpService;
import pl.konradchrzanowski.currencies.util.TestUtil;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(classes = CurrenciesApplication.class)
@AutoConfigureMockMvc
public class CurrencyControllerIT {

    private static final String DEFAULT_NAME = "first name";
    private static final String WRONG_NAME = "wrong name";
    private static final String GOOD_CURRENCY_CODE = "PLN";
    private static final String BAD_CURRENCY_CODE = "AAA";
    private static final String TO_SHORT_PATTERN_CURRENCY_CODE = "AA";
    private static final String TO_LONG_PATTERN_CURRENCY_CODE = "AAAA";
    private static final String WITH_DIGIT_PATTERN_CURRENCY_CODE = "1AA";

    private static final BigDecimal DEFAULT_VALUE_RESPONSE = new BigDecimal("4.45");
    private static final BigDecimal WRONG_VALUE_RESPONSE = new BigDecimal("6.45");
    private static final LocalDateTime DEFAULT_REQUEST_DATE_TIME = LocalDateTime.parse("2023-08-15T12:22");
    private static final LocalDateTime DEFAULT_REQUEST_DATE_TIME_END = LocalDateTime.parse("2023-08-16T12:22");
    private static final LocalDateTime WRONG_REQUEST_DATE_TIME_START = LocalDateTime.parse("2023-08-11T12:22");
    private static final LocalDateTime WRONG_REQUEST_DATE_TIME_END = LocalDateTime.parse("2023-08-14T12:22");
    private static final String API_PATH = "/currencies";

    @Autowired
    private CurrencyRepository currencyRepository;

    @Autowired
    private CurrencyMapper currencyMapper;

    @Autowired
    private MockMvc restCurrencyMockMvc;

    @Autowired
    private EntityManager em;

    @MockBean
    private NbpService nbpService;

    private Currency currency;

    @BeforeEach
    public void initTest() {
        currencyRepository.deleteAll();
    }


    @Test
    public void shouldReturnCorrectDataWithCurrencyValue() throws Exception {

        CurrencyRequest request = CurrencyRequest.builder()
                .currency(GOOD_CURRENCY_CODE)
                .name(DEFAULT_NAME).build();

        CurrencyValueResponse expectedResponse = CurrencyValueResponse.builder().value(DEFAULT_VALUE_RESPONSE).build();

        given(nbpService.getCurrencyValue(GOOD_CURRENCY_CODE, "A")).willReturn(expectedResponse);

        MvcResult result = restCurrencyMockMvc.perform(post(API_PATH + "/get-current-currency-value-command")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(TestUtil.convertObjectToJsonBytes(request)))
                .andExpect(status().isOk()).andReturn();

        ObjectMapper mapper = new ObjectMapper();
        CurrencyValueResponse response = mapper.readValue(result.getResponse().getContentAsString(),
                CurrencyValueResponse.class);
        assertThat(response.getValue()).isEqualTo(expectedResponse.getValue());

        List<Currency> currencyList = currencyRepository.findAll();
        assertThat(currencyList.size()).isEqualTo(1);
    }

    @Test
    public void shouldThrowExceptionForToShortCurrencyCodePattern() throws Exception {

        CurrencyRequest request = CurrencyRequest.builder()
                .currency(TO_SHORT_PATTERN_CURRENCY_CODE)
                .name(DEFAULT_NAME).build();


        restCurrencyMockMvc.perform(post(API_PATH + "/get-current-currency-value-command")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(TestUtil.convertObjectToJsonBytes(request)))
                .andExpect(status().isBadRequest());


        List<Currency> currencyList = currencyRepository.findAll();
        assertThat(currencyList.size()).isEqualTo(0);
    }
    @Test
    public void shouldThrowExceptionForToLongCurrencyCodePattern() throws Exception {

        CurrencyRequest request = CurrencyRequest.builder()
                .currency(TO_LONG_PATTERN_CURRENCY_CODE)
                .name(DEFAULT_NAME).build();


        restCurrencyMockMvc.perform(post(API_PATH + "/get-current-currency-value-command")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(TestUtil.convertObjectToJsonBytes(request)))
                .andExpect(status().isBadRequest());


        List<Currency> currencyList = currencyRepository.findAll();
        assertThat(currencyList.size()).isEqualTo(0);
    }
    @Test
    public void shouldThrowExceptionForCurrencyCodeWithDigitsPattern() throws Exception {

        CurrencyRequest request = CurrencyRequest.builder()
                .currency(WITH_DIGIT_PATTERN_CURRENCY_CODE)
                .name(DEFAULT_NAME).build();

        restCurrencyMockMvc.perform(post(API_PATH + "/get-current-currency-value-command")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(TestUtil.convertObjectToJsonBytes(request)))
                .andExpect(status().isBadRequest());


        List<Currency> currencyList = currencyRepository.findAll();
        assertThat(currencyList.size()).isEqualTo(0);
    }
    @Test
    public void shouldThrowExceptionForIncorrectCurrencyCode() throws Exception {

        CurrencyRequest request = CurrencyRequest.builder()
                .currency(BAD_CURRENCY_CODE)
                .name(DEFAULT_NAME).build();

        restCurrencyMockMvc.perform(post(API_PATH + "/get-current-currency-value-command")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(TestUtil.convertObjectToJsonBytes(request)))
                .andExpect(status().isBadRequest());


        List<Currency> currencyList = currencyRepository.findAll();
        assertThat(currencyList.size()).isEqualTo(0);
    }
    @Test
    public void shouldThrowExceptionForEmptyCurrencyCode() throws Exception {

        CurrencyRequest request = CurrencyRequest.builder()
                .currency(BAD_CURRENCY_CODE)
                .name(DEFAULT_NAME).build();

        restCurrencyMockMvc.perform(post(API_PATH + "/get-current-currency-value-command")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(TestUtil.convertObjectToJsonBytes(request)))
                .andExpect(status().isBadRequest());


        List<Currency> currencyList = currencyRepository.findAll();
        assertThat(currencyList.size()).isEqualTo(0);
    }

    @Test
    @Transactional
    public void shouldCheckIfNameFilterWorksWell() throws Exception {
        currency = createDefaultSavedRequest();

        defaultSavedRequestShouldBeFound("name=" + DEFAULT_NAME);

        defaultSavedRequestShouldNotBeFound("name=" + WRONG_NAME);
    }
    @Test
    @Transactional
    public void shouldCheckIfNameStartSubstringFilterWorksWell() throws Exception {
        currency = createDefaultSavedRequest();

        defaultSavedRequestShouldBeFound("name=" + DEFAULT_NAME.substring(0,3));

        defaultSavedRequestShouldNotBeFound("name=" + WRONG_NAME.substring(0,3));
    }
    @Test
    @Transactional
    public void shouldCheckIfNameEndSubstringFilterWorksWell() throws Exception {
        currency = createDefaultSavedRequest();

        defaultSavedRequestShouldBeFound("name=" + DEFAULT_NAME.substring(2,6));

        defaultSavedRequestShouldNotBeFound("name=" + WRONG_NAME.substring(2,6));
    }

    @Test
    @Transactional
    public void shouldCheckIfCurrencyFilterWorksWell() throws Exception {
        currency = createDefaultSavedRequest();

        defaultSavedRequestShouldBeFound("currency=" + GOOD_CURRENCY_CODE);

        defaultSavedRequestShouldNotBeFound("currency=" + BAD_CURRENCY_CODE);
    }
    @Test
    @Transactional
    public void shouldCheckIfCurrencyFirstCharAtFilterWorksWell() throws Exception {
        currency = createDefaultSavedRequest();

        defaultSavedRequestShouldBeFound("currency=" + GOOD_CURRENCY_CODE.charAt(0));

        defaultSavedRequestShouldNotBeFound("currency=" + BAD_CURRENCY_CODE.charAt(0));
    }
    @Test
    @Transactional
    public void shouldCheckIfCurrencyMiddleCharAtFilterWorksWell() throws Exception {
        currency = createDefaultSavedRequest();

        defaultSavedRequestShouldBeFound("currency=" + GOOD_CURRENCY_CODE.charAt(1));

        defaultSavedRequestShouldNotBeFound("currency=" + BAD_CURRENCY_CODE.charAt(1));
    }
    @Test
    @Transactional
    public void shouldCheckIfCurrencyEndCharAtFilterWorksWell() throws Exception {
        currency = createDefaultSavedRequest();

        defaultSavedRequestShouldBeFound("currency=" + GOOD_CURRENCY_CODE.charAt(2));

        defaultSavedRequestShouldNotBeFound("currency=" + BAD_CURRENCY_CODE.charAt(2));
    }
    @Test
    @Transactional
    public void shouldCheckIfValueFilterWorksWell() throws Exception {
        currency = createDefaultSavedRequest();

        defaultSavedRequestShouldBeFound("valueStartsWith=" + DEFAULT_VALUE_RESPONSE + "&valueEndsWith=" + DEFAULT_VALUE_RESPONSE.add(BigDecimal.ONE));

        defaultSavedRequestShouldNotBeFound("valueStartsWith=" + WRONG_VALUE_RESPONSE + "&valueEndsWith=" + WRONG_VALUE_RESPONSE.add(BigDecimal.ONE));

    }
    @Test
    @Transactional
    public void shouldCheckIfDateFilterWorksWell() throws Exception {
        currency = createDefaultSavedRequest();

        defaultSavedRequestShouldBeFound("dateStartsWith=" + DEFAULT_REQUEST_DATE_TIME + "&dateEndsWith=" + DEFAULT_REQUEST_DATE_TIME_END);

        defaultSavedRequestShouldNotBeFound("dateStartsWith=" + WRONG_REQUEST_DATE_TIME_START + "&dateEndsWith=" + WRONG_REQUEST_DATE_TIME_END);

    }


    private void defaultSavedRequestShouldBeFound(String filter) throws Exception {
        restCurrencyMockMvc.perform(get(API_PATH + "/requests/?sort=id,desc&" + filter))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.[*].id").value(hasItem(currency.getId().intValue())))
                .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
                .andExpect(jsonPath("$.[*].value").value(hasItem(DEFAULT_VALUE_RESPONSE.doubleValue())))
                .andExpect(jsonPath("$.[*].currency").value(hasItem(GOOD_CURRENCY_CODE)))
                .andExpect(jsonPath("$.[*].date").value(hasItem(DEFAULT_REQUEST_DATE_TIME.toString()))).andReturn();
    }

    private void defaultSavedRequestShouldNotBeFound(String filter) throws Exception {
        restCurrencyMockMvc.perform(get(API_PATH + "/requests/?sort=id,desc&" + filter))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$").isEmpty());
    }

    private Currency createDefaultSavedRequest() {
        CurrencyDTO currencyDTO = CurrencyDTO.builder()
                .currency(GOOD_CURRENCY_CODE)
                .name(DEFAULT_NAME)
                .value(DEFAULT_VALUE_RESPONSE)
                .date(DEFAULT_REQUEST_DATE_TIME).build();
        return currencyRepository.save(currencyMapper.toEntity(currencyDTO));
    }

}
