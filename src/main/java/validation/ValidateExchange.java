package validation;

import dto.CurrencyRequestDTO;
import dto.ExchangeRateRequestDTO;
import dto.ExchangeRateResponseDTO;
import exception.IncorrectParameterException;

import java.math.BigDecimal;
import java.security.InvalidParameterException;

public class ValidateExchange {
    public static void validateExchange(ExchangeRateRequestDTO exchangeRateRequestDTO) {
        String baseCurrencyCode = exchangeRateRequestDTO.getBaseCurrencyCode();
        String targetCurrencyCode = exchangeRateRequestDTO.getTargetCurrencyCode();
        BigDecimal rate = exchangeRateRequestDTO.getRate();
        if (baseCurrencyCode == null && baseCurrencyCode.isEmpty()) {
            throw new IncorrectParameterException("The base currency code is mandatory");
        }
        if (targetCurrencyCode == null || targetCurrencyCode.isEmpty()) {
            throw new IncorrectParameterException("The target currency code is mandatory");
        }
        if (rate == null || rate.compareTo(BigDecimal.ZERO) < 0) {
            throw new IncorrectParameterException("The rate is mandatory");
        }

    }

    public static void validateParameter(String parameter) {

        if (parameter == null || parameter.isEmpty()) {
            throw new InvalidParameterException("Missing parameter - " + parameter);
        }
    }
}
