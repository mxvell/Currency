package dao;

import config.DataConnection;
import dto.ExchangeRateRequestDTO;
import exception.NotFoundException;
import model.Currency;
import model.ExchangeRate;
import exception.DatabaseException;

import javax.sql.DataSource;
import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ExchangeDAO {


    public List<ExchangeRate> findAll() {
        try (Connection connection = DataConnection.getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT er.id," +
                    "bc.id  base_id," +
                    " bc.code base_code," +
                    " bc.full_name base_name," +
                    " bc.sign base_sign," +
                    " tc.id target_id," +
                    " tc.code target_code," +
                    " tc.full_name target_name," +
                    " tc.sign target_sign," +
                    " er.rate AS rate " +
                    "FROM ExchangeRates er " +
                    "JOIN Currencies bc ON er.base_currency_id = bc.id " +
                    "JOIN Currencies tc ON er.target_currency_id = tc.id");
            List<ExchangeRate> exchangeRates = new ArrayList<>();
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                exchangeRates.add(getExchangeRate(resultSet));
            }
            return exchangeRates;
        } catch (SQLException e) {
            throw new DatabaseException("Database error");
        }
    }

    public ExchangeRate findByCodePair(String baseCurrencyCode, String targetCurrencyCode) {
        try (Connection connection = DataConnection.getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT er.id AS id," +
                    "bc.id  base_id," +
                    " bc.code  base_code," +
                    " bc.full_name  base_name," +
                    " bc.sign  base_sign," +
                    " tc.id  target_id," +
                    " tc.code  target_code," +
                    " tc.full_name  target_name," +
                    " tc.sign  target_sign," +
                    " er.rate  rate " +
                    "FROM ExchangeRates er " +
                    "JOIN Currencies bc ON er.base_currency_id = bc.id " +
                    "JOIN Currencies tc ON er.target_currency_id = tc.id" +
                    " WHERE (base_currency_id =( SELECT c1.id FROM currencies c1 WHERE c1.code = ?) " +
                    "AND target_currency_id = ( SELECT c2.id FROM currencies c2  WHERE c2.code = ?))");
            preparedStatement.setString(1, baseCurrencyCode);
            preparedStatement.setString(2, targetCurrencyCode);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return getExchangeRate(resultSet);
            }
        } catch (SQLException e) {
            throw new DatabaseException("Database error, failed to find exchange rates to the database");
        }
        return null;
    }

    private ExchangeRate save(ExchangeRate exchangeRate) {
        try (Connection connection = DataConnection.getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "INSERT INTO ExchangeRates(base_currency_id, target_currency_id, rate) " +
                            "VALUES (?,?,?) ");
            preparedStatement.setInt(1, exchangeRate.getBaseCurrency().getId());
            preparedStatement.setInt(2, exchangeRate.getTargetCurrency().getId());
            preparedStatement.setBigDecimal(3, exchangeRate.getRate());
            ResultSet resultSet = preparedStatement.executeQuery();
            while (!resultSet.next()) {
                throw new DatabaseException("Database error, failed to save exchange rate");
            }
            exchangeRate.setId(resultSet.getInt("id"));
            return exchangeRate;
        } catch (SQLException e) {
            throw new DatabaseException("Database error");
        }
    }

    public ExchangeRate addByCode(ExchangeRateRequestDTO exchangeRateRequestDTO) {
        CurrencyDAO currencyDAO = new CurrencyDAO();
        String baseCurrencyCode = exchangeRateRequestDTO.getBaseCurrencyCode();
        String targetCurrencyCode = exchangeRateRequestDTO.getTargetCurrencyCode();
        BigDecimal rate = exchangeRateRequestDTO.getRate();
        Currency baseCurrency = currencyDAO.findByCode(baseCurrencyCode).orElseThrow(() ->
                new NotFoundException("not found base_currency code " + baseCurrencyCode));
        Currency targetCurrency = currencyDAO.findByCode(targetCurrencyCode).orElseThrow(()
                -> new NotFoundException("not found target_currency code " + targetCurrencyCode));
        ExchangeRate exchangeRate = new ExchangeRate(baseCurrency, targetCurrency, rate);
        return save(exchangeRate);
    }

    public Optional<ExchangeRate> update(ExchangeRate exchangeRate) {
        try (Connection connection = DataConnection.getConnection()){
         PreparedStatement preparedStatement = connection.prepareStatement("UPDATE ExchangeRates SET rate =?" +
                 "WHERE base_currency_id=? AND target_currency_id=?");
         preparedStatement.setBigDecimal(1,exchangeRate.getRate());
         preparedStatement.setInt(2, exchangeRate.getBaseCurrency().getId());
         preparedStatement.setInt(3, exchangeRate.getTargetCurrency().getId());
         ResultSet resultSet = preparedStatement.executeQuery();
         if (resultSet.next()) {
         exchangeRate.setId(resultSet.getInt("id"));
         return Optional.of(exchangeRate);
         }
        }catch (SQLException e){
            throw new NotFoundException("Error in updating exchange rate");
        }
        return Optional.empty();
    }

    private ExchangeRate getExchangeRate(ResultSet resultSet) throws SQLException {
        return new ExchangeRate(
                resultSet.getInt("id"),
                new Currency(
                        resultSet.getInt("base_id"),
                        resultSet.getString("base_code"),
                        resultSet.getString("base_name"),
                        resultSet.getString("base_sign")
                ),
                new Currency(
                        resultSet.getInt("target_id"),
                        resultSet.getString("target_code"),
                        resultSet.getString("target_name"),
                        resultSet.getString("target_sign")
                ),
                resultSet.getBigDecimal("rate"));
    }
}





















