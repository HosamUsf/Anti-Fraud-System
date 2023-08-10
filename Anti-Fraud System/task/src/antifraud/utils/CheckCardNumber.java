package antifraud.utils;

public class CheckCardNumber {

    public static boolean isValidCardNumber(String cardNumber) {
        // Remove spaces and non-digit characters
        cardNumber = cardNumber.replaceAll("\\D", "");

        if (cardNumber.length() < 13 || cardNumber.length() > 19) {
            return false; // Card number length is invalid
        }

        int sum = 0;
        boolean alternate = false;

        for (int i = cardNumber.length() - 1; i >= 0; i--) {
            int digit = Integer.parseInt(cardNumber.substring(i, i + 1));

            if (alternate) {
                digit *= 2;
                if (digit > 9) {
                    digit = (digit % 10) + 1;
                }
            }

            sum += digit;
            alternate = !alternate;
        }

        return (sum % 10 == 0);
    }
}
