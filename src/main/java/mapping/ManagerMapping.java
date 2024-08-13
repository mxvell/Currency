package mapping;

import dto.CurrencyRequestDTO;
import dto.CurrencyResponseDTO;
import dto.ExchangeRateRequestDTO;
import dto.ExchangeRateResponseDTO;
import model.Currency;
import model.ExchangeRate;
import org.modelmapper.ModelMapper;

public class ManagerMapping {
    private static final ModelMapper modelMapper;

    static {
        modelMapper = new ModelMapper();
        modelMapper.typeMap(CurrencyRequestDTO.class, Currency.class).addMapping(CurrencyRequestDTO::getName, Currency::setFullName);
    }

     public static CurrencyResponseDTO covertToDto(Currency currency){
         return modelMapper.map(currency, CurrencyResponseDTO.class);
     }

     public static Currency convertToEntity(CurrencyRequestDTO currencyRequestDTO){
        return modelMapper.map(currencyRequestDTO, Currency.class);
     }

     public static ExchangeRateResponseDTO convertToDto(ExchangeRate exchangeRate){
        return modelMapper.map(exchangeRate, ExchangeRateResponseDTO.class);
     }
     public static ExchangeRate convertToEntity(ExchangeRateRequestDTO exchangeRateRequestDTO){
        return modelMapper.map(exchangeRateRequestDTO, ExchangeRate.class);
     }
}
