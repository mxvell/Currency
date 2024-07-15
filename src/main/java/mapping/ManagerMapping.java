package mapping;

import dto.CurrencyRequestDTO;
import dto.CurrencyResponseDTO;
import model.Currency;
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

}
