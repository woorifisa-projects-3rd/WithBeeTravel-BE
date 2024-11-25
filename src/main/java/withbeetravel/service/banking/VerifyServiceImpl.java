package withbeetravel.service.banking;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import withbeetravel.domain.User;
import withbeetravel.dto.response.account.PinNumberResponse;
import withbeetravel.exception.CustomException;
import withbeetravel.exception.error.AuthErrorCode;
import withbeetravel.exception.error.BankingErrorCode;
import withbeetravel.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class VerifyServiceImpl implements VerifyService{

    private final UserRepository userRepository;

    // TODO: 로그인 정보로 그에맞는 회원 id로 검증해야함

    public void verifyPin(String pin){
        User user = userRepository.findById(1L)
                .orElseThrow(()-> new CustomException(AuthErrorCode.AUTHENTICATION_FAILED));

        if (user.isPinLocked()) {
            throw new CustomException(BankingErrorCode.ACCOUNT_LOCKED);
        }

        if (!user.validatePin(pin)) {
            user.incrementFailedPinCount();
            saveUser(user); // 저장 후 예외 던지기
            throw new CustomException(BankingErrorCode.INVALID_PIN_NUMBER);
        }

        user.resetFailedPinCount();
        userRepository.save(user);
    }

    // 예외가 발생하더라도 저장되야함 유저 실패 횟수를 늘리고 저장
    @Transactional
    private void saveUser(User user) {
        userRepository.save(user);
    }

    // TODO: 여기도 회원 id 받아서 처리해야함 일단 1번인 유저로 확인하게 했음
    public PinNumberResponse verifyUser(){
        User user = userRepository.findById(1L)
                .orElseThrow(()-> new CustomException(AuthErrorCode.AUTHENTICATION_FAILED));

        if(user.isPinLocked()){
            throw new CustomException(BankingErrorCode.ACCOUNT_LOCKED);
        }
        return new PinNumberResponse(user.getFailedPinCount(), user.isPinLocked());
    }

}
