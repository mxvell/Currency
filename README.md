# Currency Exchange Project

This project implements a REST API for managing currencies and exchange rates. It allows viewing and editing lists of currencies and exchange rates, as well as performing conversions between arbitrary amounts of different currencies. There is no web interface for this project.

## Technologies Used

- Java - collections, OOP
- MVC pattern
- Backend
  - Java servlets
  - HTTP - GET and POST requests, response codes
  - REST API, JSON
- Database - SQLite, JDBC
- Deployment - cloud hosting, Linux command line, Tomcat

## API Specifications

### Currencies

#### Get List of Currencies

- **Method:** GET
- **Path:** `/currencies`

Example response:
```json
[
  {
    "id": 0,
    "name": "United States dollar",
    "code": "USD",
    "sign": "$"
  },
  {
    "id": 1,
    "name": "Euro",
    "code": "EUR",
    "sign": "€"
  }
]
```

HTTP response codes:
- Success - 200
- Error (e.g., database unavailable) - 500

#### Get Specific Currency

- **Method:** GET
- **Path:** `/currency/{currencyCode}`

Example response:
```json
{
  "id": 0,
  "name": "Euro",
  "code": "EUR",
  "sign": "€"
}
```

HTTP response codes:
- Success - 200
- Currency code missing in address - 400
- Currency not found - 404
- Error (e.g., database unavailable) - 500

#### Add New Currency

- **Method:** POST
- **Path:** `/currencies`
- **Request Body:** Form fields (x-www-form-urlencoded) - name, code, sign

Example response:
```json
{
  "id": 0,
  "name": "Euro",
  "code": "EUR",
  "sign": "€"
}
```

HTTP response codes:
- Success - 200
- Required form field missing - 400
- Currency with this code already exists - 409
- Error (e.g., database unavailable) - 500

### Exchange Rates

#### Get List of All Exchange Rates

- **Method:** GET
- **Path:** `/exchangeRates`

Example response:
```json
[
  {
    "id": 0,
    "baseCurrency": {
      "id": 0,
      "name": "United States dollar",
      "code": "USD",
      "sign": "$"
    },
    "targetCurrency": {
      "id": 1,
      "name": "Euro",
      "code": "EUR",
      "sign": "€"
    },
    "rate": 0.99
  }
]
```

HTTP response codes:
- Success - 200
- Error (e.g., database unavailable) - 500

#### Get Specific Exchange Rate

- **Method:** GET
- **Path:** `/exchangeRate/{baseCurrencyCode}{targetCurrencyCode}`

Example response:
```json
{
  "id": 0,
  "baseCurrency": {
    "id": 0,
    "name": "United States dollar",
    "code": "USD",
    "sign": "$"
  },
  "targetCurrency": {
    "id": 1,
    "name": "Euro",
    "code": "EUR",
    "sign": "€"
  },
  "rate": 0.99
}
```

HTTP response codes:
- Success - 200
- Currency pair codes missing in address - 400
- Exchange rate for pair not found - 404
- Error (e.g., database unavailable) - 500

#### Add New Exchange Rate

- **Method:** POST
- **Path:** `/exchangeRates`
- **Request Body:** Form fields (x-www-form-urlencoded) - baseCurrencyCode, targetCurrencyCode, rate

Example response:
```json
{
  "id": 0,
  "baseCurrency": {
    "id": 0,
    "name": "United States dollar",
    "code": "USD",
    "sign": "$"
  },
  "targetCurrency": {
    "id": 1,
    "name": "Euro",
    "code": "EUR",
    "sign": "€"
  },
  "rate": 0.99
}
```

HTTP response codes:
- Success - 200
- Required form field missing - 400
- Currency pair with this code already exists - 409
- Error (e.g., database unavailable) - 500

#### Update Existing Exchange Rate

- **Method:** PATCH
- **Path:** `/exchangeRate/{baseCurrencyCode}{targetCurrencyCode}`
- **Request Body:** Form fields (x-www-form-urlencoded) - rate

Example response:
```json
{
  "id": 0,
  "baseCurrency": {
    "id": 0,
    "name": "United States dollar",
    "code": "USD",
    "sign": "$"
  },
  "targetCurrency": {
    "id": 1,
    "name": "Euro",
    "code": "EUR",
    "sign": "€"
  },
  "rate": 0.99
}
```

HTTP response codes:
- Success - 200
- Required form field missing - 400
- Currency pair not found in database - 404
- Error (e.g., database unavailable) - 500
