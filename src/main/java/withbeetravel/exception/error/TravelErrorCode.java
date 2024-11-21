package withbeetravel.exception.error;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class TravelErrorCode extends ErrorCode{

    public static final TravelErrorCode TRAVEL_NOT_FOUND = new TravelErrorCode(HttpStatus.NOT_FOUND, "TRAVEL_NOT_FOUND", "TRAVEL-001", "TRAVEL ID에 해당하는 여행 정보 없음");
    public static final TravelErrorCode TRAVEL_ACCESS_FORBIDDEN = new TravelErrorCode(HttpStatus.FORBIDDEN, "TRAVEL_ACCESS_FORBIDDEN", "TRAVEL-002", "여행 정보 접근 권한 없음");
    public static final TravelErrorCode TRAVEL_CATEGORY_NOT_FOUND = new TravelErrorCode(HttpStatus.NOT_FOUND, "TRAVEL_CATEGORY_NOT_FOUND", "TRAVEL-003", "존재하지 않는 카테고리");
    public static final TravelErrorCode TRAVEL_CAPTAIN_NOT = new TravelErrorCode(HttpStatus.NOT_FOUND, "TRAVEL_CAPTAIN_NOT", "TRAVEL-004", "여행 생성 권한 없음");
    public static final TravelErrorCode TRAVEL_CAPTAIN_NOT_FOUND = new TravelErrorCode(HttpStatus.NOT_FOUND, "TRAVEL_CAPTAIN_NOT_FOUND", "TRAVEL-007", "여행장 정보 없음");

    private TravelErrorCode(HttpStatus status, String name, String code, String message) {
        super(status, name, code, message);
    }
}
