package withbeetravel.controller.payment.docs;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import withbeetravel.dto.request.payment.SharedPaymentParticipateRequest;
import withbeetravel.dto.response.ErrorResponse;
import withbeetravel.dto.response.SuccessResponse;

@Tag(name = "공동 결제 내역 API", description = "에 대한 설명입니다.")
public interface SharedPaymentParticipantControllerDocs {

    @Operation(
            summary = "공동 결제 내역 참여 인원 수정 API",
            description = "공동 결제 내역 참여 인원을 수정할 수 있습니다.",
            tags = {"User Management", "공동 결제 내역"},
            parameters = {
                    @Parameter(
                            name = "travelId",
                            description = "여행 ID",
                            required = true,
                            in = ParameterIn.PATH,
                            example = "1234"
                    ),
                    @Parameter(
                            name = "sharedPaymentId",
                            description = "공동 결제 내역 ID",
                            required = true,
                            in = ParameterIn.PATH,
                            example = "1234"
                    )
            }
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "이미지 및 문구를 성공적으로 변경하였습니다.", content = @Content(schema = @Schema(implementation = SuccessResponse.class))),
            @ApiResponse(responseCode = "401", description = "AUTH-001", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "403", description = "TRAVEL-002\nPAYMENT-003\nPAYMENT-004", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "TRAVEL-001\nPAYMENT-001", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
    })
    public SuccessResponse<Void> updateParticipantMembers(
            Long travelId,
            Long sharedPaymentId,
            SharedPaymentParticipateRequest sharedPaymentParticipateRequest
    );

}
