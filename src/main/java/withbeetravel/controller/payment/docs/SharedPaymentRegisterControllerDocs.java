package withbeetravel.controller.payment.docs;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.multipart.MultipartFile;
import withbeetravel.dto.response.ErrorResponse;
import withbeetravel.dto.response.SuccessResponse;

@Tag(name = "공동 결제 내역 API", description = "에 대한 설명입니다.")
public interface SharedPaymentRegisterControllerDocs {

    @Operation(
            summary = "직접 결제 내역 추가하기",
            description = "현금 지출이나 위비 트래블 카드가 없는 사용자는 직접 결제 내역을 추가할 수 있습니다.",
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
                            name = "paymentDate",
                            description = "결제 시각",
                            required = true,
                            in = ParameterIn.DEFAULT,
                            example = "2024-11-01 14:33"
                    ),
                    @Parameter(
                            name = "storeName",
                            description = "상호명",
                            required = true,
                            in = ParameterIn.DEFAULT,
                            example = "뜨개뜨개"
                    ),
                    @Parameter(
                            name = "paymentAmount",
                            description = "결제 금액(원화)",
                            required = true,
                            in = ParameterIn.DEFAULT,
                            example = "8990"
                    ),
                    @Parameter(
                            name = "foreignPaymentAmount",
                            description = "결제 금액(외화)",
                            in = ParameterIn.DEFAULT,
                            example = "1.25",
                            allowEmptyValue = true
                    ),
                    @Parameter(
                            name = "currencyUnit",
                            description = "통화 단위",
                            required = true,
                            in = ParameterIn.DEFAULT,
                            example = "USD"
                    ),
                    @Parameter(
                            name = "exchangeRate",
                            description = "환율",
                            in = ParameterIn.DEFAULT,
                            example = "1392.50",
                            allowEmptyValue = true
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
            @ApiResponse(responseCode = "200", description = "결제 내역이 추가되었습니다.", content = @Content(schema = @Schema(implementation = SuccessResponse.class))),
            @ApiResponse(responseCode = "400", description = "VALIDATION-001\nVALIDATION-002\nVALIDATION-005", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "401", description = "AUTH-001", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "403", description = "TRAVEL-002", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "TRAVEL-001", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
    })
    SuccessResponse<Void> addManualSharedPayment(
            Long travelId,
            String paymentDate,
            String storeName,
            int paymentAmount,
            Double foreignPaymentAmount,
            String currencyUnit,
            Double exchangeRate,
            MultipartFile paymentImage,
            String paymentComment,
            boolean isMainImage
    );

    @Operation(
            summary = "직접 결제 내역 추가하기",
            description = "현금 지출이나 위비 트래블 카드가 없는 사용자는 직접 결제 내역을 추가할 수 있습니다.",
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
                    ),
                    @Parameter(
                            name = "paymentDate",
                            description = "결제 시각",
                            required = true,
                            in = ParameterIn.DEFAULT,
                            example = "2024-11-01 14:33"
                    ),
                    @Parameter(
                            name = "storeName",
                            description = "상호명",
                            required = true,
                            in = ParameterIn.DEFAULT,
                            example = "뜨개뜨개"
                    ),
                    @Parameter(
                            name = "paymentAmount",
                            description = "결제 금액(원화)",
                            required = true,
                            in = ParameterIn.DEFAULT,
                            example = "8990"
                    ),
                    @Parameter(
                            name = "foreignPaymentAmount",
                            description = "결제 금액(외화)",
                            in = ParameterIn.DEFAULT,
                            example = "1.25",
                            allowEmptyValue = true
                    ),
                    @Parameter(
                            name = "currencyUnit",
                            description = "통화 단위",
                            required = true,
                            in = ParameterIn.DEFAULT,
                            example = "USD"
                    ),
                    @Parameter(
                            name = "exchangeRate",
                            description = "환율",
                            in = ParameterIn.DEFAULT,
                            example = "1392.50",
                            allowEmptyValue = true
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
            @ApiResponse(responseCode = "200", description = "결제 내역 정보가 수정되었습니다.", content = @Content(schema = @Schema(implementation = SuccessResponse.class))),
            @ApiResponse(responseCode = "400", description = "VALIDATION-001\nVALIDATION-002\nVALIDATION-005", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "401", description = "AUTH-001", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "403", description = "TRAVEL-002\nPAYMENT-002", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "TRAVEL-001\nPAYMENT-001", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
    })
    SuccessResponse<Void> updateManualSharedPayment(
            Long travelId,
            Long sharedPaymentId,
            String paymentDate,
            String storeName,
            int paymentAmount,
            Double foreignPaymentAmount,
            String currencyUnit,
            Double exchangeRate,
            MultipartFile paymentImage,
            String paymentComment,
            boolean isMainImage
    );
}
