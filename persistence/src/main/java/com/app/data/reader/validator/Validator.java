package com.app.data.reader.validator;

import java.math.BigDecimal;

public sealed interface Validator<T> permits OrderDataValidator {
    T validate(T data);

    static boolean validateExpressionWithRegex(String expression, String regex) {
        return expression != null && expression.matches(regex);
    }

    static boolean isLongNumberGreaterThan(String number, long limit) {
        if (!isNumber(number)) {
            return false;
        }
        return Long.parseLong(number) > limit;
    }


    static boolean isDoubleNumberGreaterThan(String number, double limit) {
        if (!isDoubleNumber(number)) {
            return false;
        }
        return Double.parseDouble(number) > limit;
    }

    static boolean isDoubleNumber(String number) {
        return number.matches("[0-9.]+");
    }

    static boolean isBigDecimalPositive(String bigDecimal) {
        return new BigDecimal(bigDecimal).compareTo(BigDecimal.ZERO) > 0;
    }

    static boolean isNumber(String number) {
        return number.matches("\\d+");
    }

    static <T extends Comparable<T>> boolean isGreaterThanOrEqual(T number1, T number2){
        if(number1  == null || number2 == null ){
            throw new IllegalArgumentException("Input argument is null");
        }
        return number1.compareTo(number2) > 0 || number1.compareTo(number2) == 0;
    }

}
