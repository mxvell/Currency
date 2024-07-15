package dao;

import config.DataConnection;
import exception.ExistResourceException;
import model.Currency;
import exception.DatabaseException;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CurrencyDAO {

    public List<Currency> findAll() {
        try (Connection connection = DataConnection.getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM Currencies");
            ResultSet resultSet = preparedStatement.executeQuery();
            List<Currency> currencies = new ArrayList<>();
            while (resultSet.next()) {
                currencies.add(getCurrency(resultSet));
            }
            return currencies;
        } catch (SQLException e) {
            throw new DatabaseException("Database error");
        }
    }

    public Optional<Currency> findByCode(String code) {
        try (Connection connection = DataConnection.getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM Currencies WHERE code =?");
            preparedStatement.setString(1,code);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()){
               return Optional.of(getCurrency(resultSet));
            }
        } catch (SQLException e) {
            throw new DatabaseException("Database with code ");
        }
        return Optional.empty();
    }
    public Currency save(Currency currency){
        try (Connection connection = DataConnection.getConnection()){
            PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO Currencies (full_name, code, sign) VALUES (?,?,?)");
            preparedStatement.setString(1, currency.getFullName());
            preparedStatement.setString(2,currency.getCode());
            preparedStatement.setString(3, currency.getSign());
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()){
                return getCurrency(resultSet);
            }
        }catch (SQLException e){
            throw new DatabaseException("Database error");
        }
        throw new ExistResourceException("Exist with" + currency.getCode() + " to database");
    }

    private Currency getCurrency(ResultSet resultSet) throws SQLException {
        return new Currency(
                resultSet.getInt("id"),
                resultSet.getString("code"),
                resultSet.getString("full_name"),
                resultSet.getString("sign"));
    }
}




















