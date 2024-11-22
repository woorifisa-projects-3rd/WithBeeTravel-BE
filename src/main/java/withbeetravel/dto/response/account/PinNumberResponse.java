package withbeetravel.dto.response.account;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PinNumberResponse {
    private int failedPinCount;
    private boolean pinLocked;
}
