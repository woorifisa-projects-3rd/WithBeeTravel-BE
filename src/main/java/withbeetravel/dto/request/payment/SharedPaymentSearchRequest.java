package withbeetravel.dto.request.payment;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Data
@Schema(description = "공동 결제 내역 필터 Request DTO")
public class SharedPaymentSearchRequest {
    @Schema(description = "페이지 번호")
    private int page = 0;

    @Schema(description = "정렬 기준 (latest/amount)", defaultValue = "latest", allowableValues = {"latest", "amount"})
    @Pattern(regexp = "^(latest|amount)$", message = "정렬 기준은 latest 또는 amount만 가능합니다.")
    private String sortBy = "latest";

    @Schema(description = "시작 날짜")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate startDate;

    @Schema(description = "종료 날짜")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate endDate;

    @Schema(description = "특정 멤버의 결제만 보기")
    private Long memberId;
}