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
import pl.konradchrzanowski.currencies.service.client.mapper.CurrencyMapper;
import pl.konradchrzanowski.currencies.service.nbp.NbpService;
import pl.konradchrzanowski.currencies.util.TestUtil;

import javax.persistence.EntityManager;
import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = CurrenciesApplication.class)
//@WebMvcTest(CurrencyController.class)
//@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
public class CurrencyControllerIT {

    private static final String FIRST_NAME = "first name";
    private static final String GOOD_CURRENCY_CODE = "PLN";
    private static final String BAD_CURRENCY_CODE = "AAA";
    private static final String TO_SHORT_PATTERN_CURRENCY_CODE = "AA";
    private static final String TO_LONG_PATTERN_CURRENCY_CODE = "AAAA";
    private static final String WITH_DIGIT_PATTERN_CURRENCY_CODE = "1AA";

    private static final String API_PATCH = "/currencies";

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
                .name(FIRST_NAME).build();

        CurrencyValueResponse expectedResponse = CurrencyValueResponse.builder().value(new BigDecimal("4.45")).build();

        given(nbpService.getCurrencyValue(GOOD_CURRENCY_CODE, "A")).willReturn(expectedResponse);

        MvcResult result = restCurrencyMockMvc.perform(post(API_PATCH + "/get-current-currency-value-command")
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
                .name(FIRST_NAME).build();

        CurrencyValueResponse expectedResponse = CurrencyValueResponse.builder().value(new BigDecimal("4.45")).build();

        given(nbpService.getCurrencyValue(TO_SHORT_PATTERN_CURRENCY_CODE, "A")).willReturn(expectedResponse);

        restCurrencyMockMvc.perform(post(API_PATCH + "/get-current-currency-value-command")
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
                .name(FIRST_NAME).build();

        CurrencyValueResponse expectedResponse = CurrencyValueResponse.builder().value(new BigDecimal("4.45")).build();

        given(nbpService.getCurrencyValue(TO_LONG_PATTERN_CURRENCY_CODE, "A")).willReturn(expectedResponse);

        restCurrencyMockMvc.perform(post(API_PATCH + "/get-current-currency-value-command")
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
                .name(FIRST_NAME).build();

        CurrencyValueResponse expectedResponse = CurrencyValueResponse.builder().value(new BigDecimal("4.45")).build();

        given(nbpService.getCurrencyValue(WITH_DIGIT_PATTERN_CURRENCY_CODE, "A")).willReturn(expectedResponse);

        restCurrencyMockMvc.perform(post(API_PATCH + "/get-current-currency-value-command")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(TestUtil.convertObjectToJsonBytes(request)))
                .andExpect(status().isBadRequest());


        List<Currency> currencyList = currencyRepository.findAll();
        assertThat(currencyList.size()).isEqualTo(0);
    }

}
