package withbeetravel.controller.auth.docs;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import withbeetravel.dto.response.ErrorResponse;
import withbeetravel.dto.response.SuccessResponse;
import withbeetravel.dto.response.auth.MyPageResponse;

@Tag(name = "인증 API", description = "에 대한 설명입니다.")
public interface AuthControllerDocs {

    @Operation(
            summary = "마이페이지 초기 정보 API",
            description = "마이페이지에 필요한 초기 정보를 불러올 수 있습니다.",
            tags = {"User Management", "인증"}
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "마이페이지 정보입니다.", content = @Content(schema = @Schema(implementation = MyPageResponse.class))),
            @ApiResponse(responseCode = "401", description = "AUTH-001", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "AUTH-017", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
    })
    SuccessResponse<MyPageResponse> getMyPageInfo();
}
