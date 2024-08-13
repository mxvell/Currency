package dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ExchangeRateRequestDTO {
     private String baseCurrencyCode;
     private String targetCurrencyCode;
     private BigDecimal rate;
}
