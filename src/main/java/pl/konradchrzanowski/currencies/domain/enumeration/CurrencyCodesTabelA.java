package pl.konradchrzanowski.currencies.domain.enumeration;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public enum CurrencyCodesTabelA {
    THB,
    USD,
    AUD,
    HKD,
    CAD,
    NZD,
    SGD,
    EUR,
    HUF,
    CHF,
    GBP,
    UAH,
    JPY,
    CZK,
    DKK,
    ISK,
    NOK,
    SEK,
    HRK,
    RON,
    BGN,
    TRY,
    ILS,
    CLP,
    PHP,
    MXN,
    ZAR,
    MYR,
    RUB,
    IDR,
    INR,
    KRW,
    CNY,
    XDR,
    PLN;

    public static final List<String> codes =
            Stream.of(CurrencyCodesTabelA.values()).map(CurrencyCodesTabelA::name).collect(Collectors.toList());



}
