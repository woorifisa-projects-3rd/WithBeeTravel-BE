package withbeetravel.service.auth;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import withbeetravel.domain.User;
import withbeetravel.dto.request.auth.CustomUserInfoDto;
import withbeetravel.exception.CustomException;
import withbeetravel.exception.error.AuthErrorCode;
import withbeetravel.repository.UserRepository;
import withbeetravel.security.CustomUserDetails;

// 사용자 인증 정보 로드하여 UserDetails 객체로 변환
@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;
    private final ModelMapper mapper;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findUserByEmail(email).orElseThrow(() -> new CustomException(AuthErrorCode.INVALID_CREDENTIALS));

        CustomUserInfoDto customUserInfoDto = mapper.map(user, CustomUserInfoDto.class);
        return new CustomUserDetails(customUserInfoDto);
    }
}
