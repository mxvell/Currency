package dao;

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
    private final DataSource dataSource;

    public ExchangeDAO(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public List<ExchangeRate> findAll() {
        List<ExchangeRate> exchangeRates = null;
        try (Connection connection = dataSource.getConnection()) {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM ExchangeRates");
            exchangeRates = new ArrayList<>();
            if (resultSet.next()) {
                exchangeRates.add(
                        new ExchangeRate(
                                resultSet.getInt("id"),
                                resultSet.getInt("baseCurrencyId"),
                                resultSet.getInt("targetCurrencyId"),
                                resultSet.getBigDecimal("rate")

                        )
                );
            }
        } catch (SQLException e) {
            throw new DatabaseException("Database error");
        }
        return exchangeRates;
    }

    public Optional<ExchangeRate> findCurrencyPair(Currency baseCurrency, Currency targetCurrency) {
        ExchangeRate exchangeRate = null;
        try (Connection connection = dataSource.getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM ExchangeRates WHERE baseCurrencyId =? AND targetCurrencyId =?");
            preparedStatement.setInt(1, baseCurrency.getId());
            preparedStatement.setInt(2, targetCurrency.getId());
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                exchangeRate = new ExchangeRate();
                resultSet.getInt("id");
                resultSet.getInt("baseCurrencyId");
                resultSet.getInt("targetCurrencyId");
                resultSet.getBigDecimal("rate");
            }
        } catch (SQLException e) {
            throw new DatabaseException("Database error");
        }
        return Optional.ofNullable(exchangeRate);
    }

    public ExchangeRate save(ExchangeRate exchangeRate) {
        int rows;
        try (Connection connection = dataSource.getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO ExchangeRates (baseCurrencyId, targetCurrencyId, rate) VALUES (?,?,?)");
            preparedStatement.setInt(1, exchangeRate.getBaseCurrencyId());
            preparedStatement.setInt(2, exchangeRate.getTargetCurrencyId());
            preparedStatement.setBigDecimal(3, exchangeRate.getRate());
            rows = preparedStatement.executeUpdate();
            if (rows != 0) {
                ResultSet keys = preparedStatement.getGeneratedKeys();
                if (keys.next()) {
                    exchangeRate.setId(keys.getInt(1));
                }
            }
        } catch (SQLException e) {
            throw new DatabaseException("Database error");
        }
        return exchangeRate;
    }

    public ExchangeRate update(ExchangeRate exchangeRate, BigDecimal newRate) {
        try (Connection connection = dataSource.getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement("UPDATE ExchangeRates SET baseCurrencyId =?, targetCurrencyId =?, rate =? WHERE id =?");
            preparedStatement.setInt(1, exchangeRate.getBaseCurrencyId());
            preparedStatement.setInt(2, exchangeRate.getTargetCurrencyId());
            preparedStatement.setBigDecimal(3, newRate);
            preparedStatement.setInt(4, exchangeRate.getId());
            preparedStatement.executeUpdate();
            exchangeRate.setRate(newRate);
        } catch (SQLException e) {
            throw new DatabaseException("Database error");
        }
        return exchangeRate;
    }
}





















