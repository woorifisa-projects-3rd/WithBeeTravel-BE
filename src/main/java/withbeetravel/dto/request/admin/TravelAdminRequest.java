package withbeetravel.dto.request.admin;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TravelAdminRequest {
    private int page;
    private int size;
    private Long userId;
}
