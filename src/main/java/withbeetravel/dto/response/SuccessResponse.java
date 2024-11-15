package withbeetravel.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SuccessResponse<T> {
    private final int status;
    private final String message;
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
