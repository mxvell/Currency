package service;

import dao.CurrencyDAO;
import dao.ExchangeDAO;
import dto.ExchangeRateRequestDTO;
import exception.ExistResourceException;
import exception.NotFoundException;
import model.Currency;
import model.ExchangeRate;

public class ExchangeService {
    private final ExchangeDAO exchangeDAO = new ExchangeDAO();
    private final CurrencyDAO currencyDAO = new CurrencyDAO();

    public ExchangeRate update(ExchangeRateRequestDTO exchangeRateRequestDTO) {
        String baseCurrencyCode = exchangeRateRequestDTO.getBaseCurrencyCode();
        String targetCurrencyCode = exchangeRateRequestDTO.getTargetCurrencyCode();

        Currency baseCurrency = currencyDAO.findByCode(baseCurrencyCode).
                orElseThrow(()-> new NotFoundException("Base Currency Not Found"));
        Currency targetCurrency = currencyDAO.findByCode(targetCurrencyCode).
                orElseThrow(()-> new NotFoundException("Target Currency Not Found"));
        ExchangeRate exchangeRate = new ExchangeRate(baseCurrency, targetCurrency, exchangeRateRequestDTO.getRate());
        return exchangeDAO.update(exchangeRate).
                orElseThrow(()-> new ExistResourceException("Exchange rate resource not found"));
    }
}
