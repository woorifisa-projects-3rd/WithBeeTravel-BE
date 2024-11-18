package withbeetravel.controller.travel.docs;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import withbeetravel.dto.response.ErrorResponse;
import withbeetravel.dto.response.travel.HoneyCapsuleResponse;
import withbeetravel.dto.response.SuccessResponse;

import java.util.List;

@Tag(name = "공동 결제 내역 API", description = "에 대한 설명입니다.")
public interface HoneyCapsuleControllerDocs {

    @Operation(
            summary = "허니캡슐 정보 불러오기 API",
            description = "허니캡슐에 대한 정보 리스트를 불러올 수 있습니다.",
            tags = {"User Management", "여행"},
            parameters = {
                    @Parameter(
                            name = "travelId",
                            description = "여행 ID",
                            required = true,
                            in = ParameterIn.PATH,
                            example = "1234"
                    )
            }
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "여행 기록 조회 성공", content = @Content(schema = @Schema(implementation = HoneyCapsuleResponse.class))),
            @ApiResponse(responseCode = "401", description = "AUTH-001", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "403", description = "TRAVEL-002", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "TRAVEL-001", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "409", description = "SETTLEMENT-005", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
    })
    SuccessResponse<List<HoneyCapsuleResponse>> getHoneyCapsuleList(Long travelId);
}
