package withbeetravel.exception.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import org.springframework.http.ResponseEntity;
import withbeetravel.exception.error.ErrorCode;

@Getter
@Builder
@Schema(description = "Error에 대한 Response DTO")
public class ErrorResponseDto {

    @Schema(description = "HTTP 상태 코드")
    private int status;

    @Schema(description = "에러명")
    private String name;

    @Schema(description = "에러 코드")
    private String code;

    @Schema(description = "에러 메세지")
    private String message;

    public static ResponseEntity<ErrorResponseDto> toResponseEntity(ErrorCode e) {

        return ResponseEntity
                .status(e.getStatus())
                .body(ErrorResponseDto.builder()
                        .status(e.getStatus().value())
                        .name(e.getName())
                        .code(e.getCode())
                        .message(e.getMessage())
                        .build());
    }
}
