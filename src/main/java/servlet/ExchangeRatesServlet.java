package servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import dao.ExchangeDAO;
import dto.ExchangeRateRequestDTO;
import dto.ExchangeRateResponseDTO;
import exception.IncorrectParameterException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import mapping.ManagerMapping;
import model.ExchangeRate;
import validation.ValidateExchange;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@WebServlet("/exchangeRates")
public class ExchangeRatesServlet extends HttpServlet {
    private final ExchangeDAO exchangeDAO = new ExchangeDAO();
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        List<ExchangeRate> exchangeRates = exchangeDAO.findAll();
        List<ExchangeRateResponseDTO> exchangeRatesResponseDTO = new ArrayList<>();
        for (ExchangeRate dao : exchangeRates) {
            exchangeRatesResponseDTO.add(ManagerMapping.convertToDto(dao));
        }
        objectMapper.writeValue(resp.getWriter(), exchangeRatesResponseDTO);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String baseCurrencyCode = req.getParameter("baseCurrencyCode");
        String targetCurrencyCode = req.getParameter("targetCurrencyCode");
        String rate = req.getParameter("rate");
        ValidateExchange.validateParameter(rate);
        BigDecimal rateValue;
        try {
            rateValue = new BigDecimal(rate);
        } catch (IncorrectParameterException e) {
            throw new IncorrectParameterException("Incorrect parameter");
        }
        ExchangeRateRequestDTO exchangeRateRequestDTO = new ExchangeRateRequestDTO(baseCurrencyCode, targetCurrencyCode, rateValue);
        ValidateExchange.validateExchange(exchangeRateRequestDTO);
        ExchangeRate exchangeRate = exchangeDAO.addByCode(exchangeRateRequestDTO);
        objectMapper.writeValue(resp.getWriter(), ManagerMapping.convertToDto(exchangeRate));
    }
}
