package withbeetravel.domain;

import lombok.Getter;
import withbeetravel.exception.CustomException;
import withbeetravel.exception.error.PaymentErrorCode;
import withbeetravel.exception.error.TravelErrorCode;

import java.util.Arrays;

@Getter
public enum Category {
    TRANSPORTATION("교통"),
    FOOD("식비"),
    ACCOMMODATION("숙박"),
    TOUR("관광"),
    ACTIVITY("액티비티"),
    SHOPPING("쇼핑"),
    FLIGHT("항공"),
    ETC("기타");

    private final String description;

    Category(String description) {
        this.description = description;
    }

    public static Category fromString(String description) {
        return Arrays.stream(values())
                .filter(category -> category.description.equals(description))
                .findFirst()
                .orElseThrow(() -> new CustomException(TravelErrorCode.TRAVEL_CATEGORY_NOT_FOUND));
    }
}
