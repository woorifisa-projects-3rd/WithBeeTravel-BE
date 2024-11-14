package withbeetravel.dto.travel;


import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import java.util.List;

@Getter
@Setter
public class TravelRequestDto {

    @NotBlank
    private String travelName;
    
    private boolean isDomesticTravel;
    
    // 각 필드별로 유효성 검사 규칙을 적용할 적절한 어노테이션을 각 필드 위에 작성
    private List<String> travelCountries;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private String travelStartDate;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private String travelEndDate;

    public TravelRequestDto(String travelName, Boolean isDomesticTravel, List<String> travelCountries, String travelStartDate, String travelEndDate) {
        this.travelName = travelName;
        this.isDomesticTravel = isDomesticTravel;
        this.travelCountries = travelCountries;
        this.travelStartDate = travelStartDate;
        this.travelEndDate = travelEndDate;
    }

}
