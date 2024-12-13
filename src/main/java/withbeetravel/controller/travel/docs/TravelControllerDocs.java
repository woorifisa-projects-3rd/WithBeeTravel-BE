package withbeetravel.controller.travel.docs;

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
import withbeetravel.dto.response.travel.HoneyCapsuleResponse;

@Tag(name = "여행 API", description = "에 대한 설명입니다.")
public interface TravelControllerDocs {

    @Operation(
            summary = "여행 메인 이미지 변경 API",
            description = "여행 메인 이미지를 수정할 수 있습니다.",
            tags = {"User Management", "여행"},
            parameters = {
                    @Parameter(
                            name = "travelId",
                            description = "여행 ID",
                            required = true,
                            in = ParameterIn.PATH,
                            example = "1234"
                    ),
                    @Parameter(
                            name = "file",
                            description = "여행 메인 이미지로 변경할 파일",
                            required = true,
                            in = ParameterIn.DEFAULT,
                            example = "image.jpg"
                    )
            }
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "여행 기록 조회 성공", content = @Content(schema = @Schema(implementation = HoneyCapsuleResponse.class))),
            @ApiResponse(responseCode = "401", description = "AUTH-001", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "403", description = "TRAVEL-002", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "TRAVEL-001", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "422", description = "VALIDATION-004", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
    })
    SuccessResponse<Void> changeMainImage(
            Long travelId,
            MultipartFile file
    );
}
