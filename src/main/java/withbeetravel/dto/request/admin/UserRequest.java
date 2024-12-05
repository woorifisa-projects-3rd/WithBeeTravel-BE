package withbeetravel.dto.request.admin;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;

@Data
@Builder
public class UserRequest {
    private String name;
    private int page;
    private int size;
}
