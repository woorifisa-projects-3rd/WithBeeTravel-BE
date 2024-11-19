package withbeetravel.dto.response.travel;

import lombok.Builder;
import lombok.Data;
import withbeetravel.domain.TravelMember;

@Data
@Builder
public class TravelMemberResponse {
    private Long TravelMemberId;
    private String name;
    private String profileImage;

    public static TravelMemberResponse from(TravelMember travelMember) {
        return TravelMemberResponse.builder()
                .TravelMemberId(travelMember.getId())
                .name(travelMember.getUser().getName())
                .profileImage(travelMember.getUser().getProfileImage())
                .build();
    }
}
