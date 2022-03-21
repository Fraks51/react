package currency;

import java.util.Map;

public class Currency {

    static Map<String, Double> CurrencyValue = Map.of(
            "USD", 101.1,
            "EURO", 144.84,
            "RUB", 1.0
    );

    static public double toNewPrice(double price, String from, String to) {
        double rub = CurrencyValue.get(from) * price;
        return rub / CurrencyValue.get(to);
    }
}
