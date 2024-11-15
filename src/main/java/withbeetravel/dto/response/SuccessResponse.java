package withbeetravel.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(description = "요청 성공에 대한 Response DTO")
public class SuccessResponse<T> {

    @Schema(description = "HTTP 상태 코드")
    private final int status;

    @Schema(description = "성공 메세지")
    private final String message;

    @Schema(description = "응답 데이터")
    private final T data;

    public static <T> SuccessResponse<T> of(int status, String message, T data) {
        return SuccessResponse.<T>builder()
                .status(status)
                .message(message)
                .data(data)
                .build();
    }

    public static <T> SuccessResponse<T> of(int status, String message) {
        return SuccessResponse.<T>builder()
                .status(status)
                .message(message)
                .build();
    }
}
