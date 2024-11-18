package withbeetravel.domain;

import withbeetravel.exception.CustomException;
import withbeetravel.exception.error.ValidationErrorCode;

public enum CurrencyUnit {
    USD, EUR, JPY, CNY, KRW, GBP, AUD, CAD, NZD, MXN, INR, BRL, ZAR, SEK, NOK,
    DKK, CHF, HKD, SGD, THB, TRY, MYR, PHP, AED, SAR, KWD, BHD, QAR, OMR, JOD,
    LBP, EGP, IDR, PKR, TWD, VND, COP, PEN, CLP, ARS;

    public static CurrencyUnit from(String unit) {

        for (CurrencyUnit currencyUnit : CurrencyUnit.values()) {
            if(currencyUnit.name().equalsIgnoreCase(unit)) { // 대소문자 구분없이 비교
                return currencyUnit;
            }
        }
        throw new CustomException(ValidationErrorCode.INVALID_CURRENCY_UNIT);
    }
}
