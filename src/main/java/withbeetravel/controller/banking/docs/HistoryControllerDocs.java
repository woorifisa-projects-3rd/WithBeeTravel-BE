package withbeetravel.controller.banking.docs;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import withbeetravel.dto.request.account.HistoryRequest;
import withbeetravel.dto.response.ErrorResponse;
import withbeetravel.dto.response.SuccessResponse;
import withbeetravel.dto.response.account.WibeeCardHistoryListResponse;

@Tag(name = "코어뱅킹 API", description = "에 대한 설명입니다.")
public interface HistoryControllerDocs {

    @Operation(
            summary = "결제 내역 추가하기 API",
            description = "계좌에 결제 내역을 추가할 수 있습니다.",
            tags = {"User Management", "코어뱅킹"},
            parameters = {
                    @Parameter(
                            name = "accountId",
                            description = "계좌 ID",
                            in = ParameterIn.DEFAULT,
                            example = "1234"
                    )
            }
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "결제 내역 등록 완료", content = @Content(schema = @Schema(implementation = SuccessResponse.class))),
            @ApiResponse(responseCode = "400", description = "BANKING-01", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "401", description = "AUTH-001", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "403", description = "BANKING-004\nBANKING-006", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "BANKING-002", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
    })
    SuccessResponse<Void> addPayment(
            @PathVariable Long accountId,
            @RequestBody HistoryRequest historyRequest
    );

    @Operation(
            summary = "위비 트래블 카드 결제 내역 불러오기 API",
            description = "위비 트래블 카드 결제 내역을 불러올 수 있습니다.",
            tags = {"User Management", "코어뱅킹"},
            parameters = {
                    @Parameter(
                            name = "startDate",
                            description = "필터링 시작 범위",
                            in = ParameterIn.QUERY,
                            example = "2024-11-26"
                    ),
                    @Parameter(
                            name = "endDate",
                            description = "필터링 끝 범위",
                            in = ParameterIn.QUERY,
                            example = "2024-11-28"
                    )
            }
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "위비 카드 결제 내역입니다.", content = @Content(schema = @Schema(implementation = WibeeCardHistoryListResponse.class))),
            @ApiResponse(responseCode = "400", description = "VALIDATION-003", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "401", description = "AUTH-001", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "403", description = "BANKING-003\nBANKING-006", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "AUTH-017", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
    })
    SuccessResponse<WibeeCardHistoryListResponse> getWibeeCardHistory(
            String startDate,
            String endDate
    );
}
