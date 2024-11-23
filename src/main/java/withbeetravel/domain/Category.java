package withbeetravel.domain;

import lombok.Getter;

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
}
