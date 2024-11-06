package withbeetravel.exception;

import lombok.Builder;
import lombok.Getter;
import org.springframework.http.ResponseEntity;
import withbeetravel.exception.error.ErrorCode;

@Builder
public class ErrorResponseDto {

    private int status;
    private String name;
    private String code;
    private String message;

    public static ResponseEntity<ErrorResponseDto> toResponseEntity(ErrorCode e) {

        return ResponseEntity
                .status(e.getStatus())
                .body(ErrorResponseDto.builder()
                        .name(e.).build());
    }
}
