package servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import dao.CurrencyDAO;
import dto.CurrencyRequestDTO;
import dto.CurrencyResponseDTO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import mapping.ManagerMapping;
import model.Currency;
import validation.ValidateCurrency;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/currencies")
public class CurrenciesServlet extends HttpServlet {
    private final CurrencyDAO currencyDAO = new CurrencyDAO();
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        List<Currency> currencies = currencyDAO.findAll();
        List<CurrencyResponseDTO> currencyDTO = new ArrayList<>();
        for (Currency dao : currencies) {
            currencyDTO.add(ManagerMapping.covertToDto(dao));
        }
        objectMapper.writeValue(resp.getWriter(), currencyDTO);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String name = req.getParameter("name");
        String code = req.getParameter("code");
        String sign = req.getParameter("sign");
        CurrencyRequestDTO currencyDTO = new CurrencyRequestDTO(name, code, sign);

        ValidateCurrency.validateCurrency(currencyDTO);

        Currency currency = currencyDAO.save(ManagerMapping.convertToEntity(currencyDTO));
        resp.setStatus(HttpServletResponse.SC_CREATED);
        objectMapper.writeValue(resp.getWriter(), ManagerMapping.covertToDto(currency));
    }
}
