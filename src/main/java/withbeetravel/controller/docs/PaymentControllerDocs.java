package withbeetravel.controller.docs;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import withbeetravel.dto.ChooseParticipantsRequestDto;
import withbeetravel.exception.dto.ErrorResponseDto;

@Tag(name = "공동 결제 내역 API", description = "에 대한 설명입니다.")
public interface PaymentControllerDocs {

    @Operation(
            summary = "정산 인원 선택",
            description = "공동 결제 내역에 대한 정산 인원을 선택할 수 있습니다.",
            tags = {"User Management"},
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
            @ApiResponse(responseCode = "200", description = "정산인원 변경 성공"),
            @ApiResponse(responseCode = "401", description = "AUTH-001", content = @Content(schema = @Schema(implementation = ErrorResponseDto.class))),
            @ApiResponse(responseCode = "404", description = "TRAVEL-001", content = @Content(schema = @Schema(implementation = ErrorResponseDto.class))),
            @ApiResponse(responseCode = "403", description = "TRAVEL-002", content = @Content(schema = @Schema(implementation = ErrorResponseDto.class))),
            @ApiResponse(responseCode = "404", description = "PAYMENT-001", content = @Content(schema = @Schema(implementation = ErrorResponseDto.class))),
    })
    public ResponseEntity<String> chooseParticipant(@PathVariable Long travelId,
                                                    @PathVariable Long sharedPaymentId,
                                                    @RequestBody ChooseParticipantsRequestDto requestDto);
}
