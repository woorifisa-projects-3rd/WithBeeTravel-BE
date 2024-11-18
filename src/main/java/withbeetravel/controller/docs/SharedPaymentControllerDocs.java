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

}
