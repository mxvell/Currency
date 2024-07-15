package validation;

import dto.CurrencyRequestDTO;
import exception.IncorrectParameterException;

import java.security.InvalidParameterException;

public class ValidateCurrency {

    public static void validateCurrency(CurrencyRequestDTO currencyRequestDTO) {
        String name = currencyRequestDTO.getName();
        String code = currencyRequestDTO.getCode();
        String sign = currencyRequestDTO.getSign();

        if (name == null || name.isBlank()) {
            throw new IncorrectParameterException("Incorrect currency parameter name");
        }
        if (code == null || code.isBlank()) {
            throw new IncorrectParameterException("Incorrect currency parameter code");
        }
        if (sign == null || sign.isBlank()) {
            throw new IncorrectParameterException("Incorrect currency parameter sign");
        }
    }

    public static void validateCurrencyCode(String code) {
        if (code.length() != 3) {
            throw new InvalidParameterException("Invalid parameter '" + code + "' ,CurrencyServlet code must be 3 letters");
        }
        if (!(code.matches("[A-Z]{3}"))) {
            throw new InvalidParameterException("Invalid parameter '" + code + "' ,CurrencyServlet code must be 3 uppercase letters");
        }
    }
}
