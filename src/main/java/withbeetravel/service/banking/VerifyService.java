package withbeetravel.service.banking;

import withbeetravel.dto.response.account.PinNumberResponse;

public interface VerifyService {
    public void verifyPin(String pin);

    public PinNumberResponse verifyUser();
}
