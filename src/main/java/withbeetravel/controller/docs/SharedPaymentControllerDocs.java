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
import org.springframework.web.multipart.MultipartFile;
import withbeetravel.dto.request.ChooseParticipantsRequest;
import withbeetravel.dto.response.SharedPaymentRecordResponse;
import withbeetravel.dto.response.SuccessResponse;
import withbeetravel.dto.response.ErrorResponse;

@Tag(name = "공동 결제 내역 API", description = "에 대한 설명입니다.")
public interface SharedPaymentControllerDocs {

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
            @ApiResponse(responseCode = "401", description = "AUTH-001", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "TRAVEL-001", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "403", description = "TRAVEL-002", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "PAYMENT-001", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
    })
    public ResponseEntity<String> chooseParticipant(@PathVariable Long travelId,
                                                    @PathVariable Long sharedPaymentId,
                                                    @RequestBody ChooseParticipantsRequest requestDto);

    @Operation(
            summary = "여행 기록 추가/수정하기",
            description = "공동 결제 내역에 대해 이미지, 문구를 추가/수정할 수 있습니다.",
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
                    ),
                    @Parameter(
                            name = "paymentImage",
                            description = "이미지(nullable)",
                            required = true,
                            in = ParameterIn.DEFAULT,
                            example = "image.jpg",
                            allowEmptyValue = true
                    ),
                    @Parameter(
                            name = "paymentComment",
                            description = "문구",
                            in = ParameterIn.DEFAULT,
                            example = "얏호박고구마~"
                    ),
                    @Parameter(
                            name = "isMainImage",
                            description = "대표 이미지 여부",
                            in = ParameterIn.DEFAULT
                    )
            }
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "이미지 및 문구를 성공적으로 변경하였습니다.", content = @Content(schema = @Schema(implementation = SuccessResponse.class))),
            @ApiResponse(responseCode = "401", description = "AUTH-001", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "403", description = "TRAVEL-002\nPAYMENT-003", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "TRAVEL-001\nPAYMENT-001", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "422", description = "VALIDATION-004", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
    })
    public SuccessResponse addAndUpdatePaymentRecord(Long travelId, Long sharedPaymentId,
                                                     MultipartFile paymentImage, String paymentComment, boolean isMainImage);

    @Operation(
            summary = "SHARED PAYMENT ID에 따른 여행 기록 불러오기",
            description = "SHARED PAYMENT ID에 따른 여행 사진, 문구를 불러올 수 있습니다.",
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
            @ApiResponse(responseCode = "200", description = "여행 기록 불러오기 성공", content = @Content(schema = @Schema(implementation = SharedPaymentRecordResponse.class))),
            @ApiResponse(responseCode = "401", description = "AUTH-001", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "403", description = "TRAVEL-002\nPAYMENT-003", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "TRAVEL-001\nPAYMENT-001", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
    })
    public SuccessResponse<SharedPaymentRecordResponse> getSharedPaymentRecord(
            Long travelId,
            Long sharedPaymentId
    );
}
