package servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import dao.ExchangeDAO;
import dto.ExchangeRateRequestDTO;
import exception.IncorrectParameterException;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import mapping.ManagerMapping;
import model.ExchangeRate;
import service.ExchangeService;
import validation.ValidateCurrency;
import validation.ValidateExchange;

import java.io.IOException;
import java.math.BigDecimal;

@WebServlet("/exchangeRate/*")
public class ExchangeRateServlet extends HttpServlet {
    private final ObjectMapper mapper = new ObjectMapper();
    private final ExchangeDAO exchangeDAO = new ExchangeDAO();

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (req.getMethod().equalsIgnoreCase("PATCH")) {
            doPatch(req, resp);
        } else {
            super.service(req, resp);
        }
    }


    protected void doPatch(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        ExchangeService exchangeService = new ExchangeService();
        String currencyCode = req.getPathInfo().replaceFirst("/", "");
        String baseCurrencyCode = currencyCode.substring(0, 3);
        String targetCurrencyCode = currencyCode.substring(3);
        String requestBody = req.getReader().readLine();
        ValidateCurrency.validateCurrencyCode(baseCurrencyCode);
        ValidateCurrency.validateCurrencyCode(targetCurrencyCode);
        ValidateExchange.validateParameter(requestBody);
        String rate = requestBody.replace("rate=", "");
        BigDecimal rateValue;
        try {
            rateValue = new BigDecimal(rate);
        } catch (IncorrectParameterException e) {
            throw new IncorrectParameterException("Incorrect parameter");
        }
        ExchangeRateRequestDTO exchangeRateRequestDTO = new ExchangeRateRequestDTO(baseCurrencyCode, targetCurrencyCode, rateValue);
        ValidateExchange.validateExchange(exchangeRateRequestDTO);
        ExchangeRate exchangeRate = exchangeService.update(exchangeRateRequestDTO);
        mapper.writeValue(resp.getWriter(), ManagerMapping.convertToDto(exchangeRate));

    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String currencyCode = req.getPathInfo().replaceFirst("/", "");
        String baseCurrencyCode = currencyCode.substring(0, 3);
        String targetCurrencyCode = currencyCode.substring(3);
        ValidateCurrency.validateCurrencyCode(baseCurrencyCode);
        ValidateCurrency.validateCurrencyCode(targetCurrencyCode);
        ExchangeRate exchangeRate = exchangeDAO.findByCodePair(baseCurrencyCode, targetCurrencyCode);

        mapper.writeValue(resp.getWriter(), ManagerMapping.convertToDto(exchangeRate));
    }
}
