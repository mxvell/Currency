package servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import dao.CurrencyDAO;
import exception.NotFoundException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import mapping.ManagerMapping;
import model.Currency;
import validation.ValidateCurrency;

import java.io.IOException;

import static mapping.ManagerMapping.covertToDto;

@WebServlet("/currency/*")
public class CurrencyServlet extends HttpServlet {
    private final CurrencyDAO currencyDAO = new CurrencyDAO();
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String code = req.getPathInfo().substring(1);
        ValidateCurrency.validateCurrencyCode(code);
        Currency currency = currencyDAO.findByCode(code).orElseThrow(() -> new NotFoundException("Currency with code " + code + " not found"));
        objectMapper.writeValue(resp.getWriter(), covertToDto(currency));
    }
}
