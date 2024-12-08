package withbeetravel.dto.response.admin;


import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DashboardResponse {
    private long loginCount;
    private long totalUser;
    private long totalTravel;
}
