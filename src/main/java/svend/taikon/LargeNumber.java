package svend.taikon;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.text.DecimalFormat;

public class LargeNumber implements Comparable<LargeNumber> {
    private final BigInteger value;

    public LargeNumber(String value) {
        this.value = new BigInteger(value);
    }

    public LargeNumber add(LargeNumber other) {
        return new LargeNumber(this.value.add(other.value).toString());
    }

    public LargeNumber subtract(LargeNumber other) {
        return new LargeNumber(this.value.subtract(other.value).toString());
    }

    public LargeNumber multiply(LargeNumber other) {
        return new LargeNumber(this.value.multiply(other.value).toString());
    }

    public LargeNumber divide(LargeNumber other) {
        if (other.value.equals(BigInteger.ZERO)) {
            throw new ArithmeticException("Деление на ноль");
        }
        return new LargeNumber(this.value.divide(other.value).toString());
    }

    // Методы сравнения
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LargeNumber that = (LargeNumber) o;
        return value.equals(that.value);
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }

    @Override
    public int compareTo(LargeNumber other) {
        return this.value.compareTo(other.value);
    }

    // Операторы сравнения
    public boolean left(LargeNumber other) {  // <
        return this.compareTo(other) < 0;
    }

    public boolean right(LargeNumber other) {  // >
        return this.compareTo(other) > 0;
    }

    public boolean rightOrEqual(LargeNumber other) {  // <=
        return this.compareTo(other) <= 0;
    }

    public boolean leftOrEqual(LargeNumber other) {  // >=
        return this.compareTo(other) >= 0;
    }

    // Форматирование (остается без изменений)
    public String format() {
        int maxPower = 63;
        String[] suffixes = new String[maxPower/3 + 1];
        BigInteger[] divisors = new BigInteger[maxPower/3 + 1];

        String[] suffixNames = {"", "тыс", "млн", "млрд", "трлн",
                "квадрлн", "квинтлн", "секстлн", "септлн",
                "октлн", "нонилн", "децилн"};

        for (int i = 0; i <= maxPower/3; i++) {
            divisors[i] = BigInteger.TEN.pow(i*3);
            suffixes[i] = " " + (i < suffixNames.length ? suffixNames[i] : "10^" + (i*3));
        }

        int index = 0;
        BigInteger currentDivisor = divisors[0];

        for (int i = divisors.length - 1; i >= 0; i--) {
            if (value.compareTo(divisors[i]) >= 0) {
                index = i;
                currentDivisor = divisors[i];
                break;
            }
        }

        BigDecimal formattedValue = new BigDecimal(value)
                .divide(new BigDecimal(currentDivisor), 2, RoundingMode.HALF_UP);

        return formattedValue.stripTrailingZeros().toPlainString() + suffixes[index];
    }

    @Override
    public String toString() {
        return format();
    }

    public BigInteger getValue() {
        return value;
    }

}