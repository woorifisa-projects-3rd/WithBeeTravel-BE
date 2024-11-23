package withbeetravel.aspect;

import lombok.RequiredArgsConstructor;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;
import withbeetravel.dto.request.payment.SharedPaymentSearchRequest;
import withbeetravel.exception.CustomException;
import withbeetravel.exception.error.ValidationErrorCode;

import java.time.LocalDate;

@Aspect
@Component
@RequiredArgsConstructor
public class PaymentValidationAspect {

    @Before("@annotation(paymentValidation)")
    public void validatePaymentRequest(JoinPoint joinPoint, PaymentValidation paymentValidation) {
        Object[] args = joinPoint.getArgs();
        for (Object arg : args) {
            if (arg instanceof SharedPaymentSearchRequest request) {
                validateDateRange(request.getStartDate(), request.getEndDate());
            }
        }
    }

    private void validateDateRange(LocalDate startDate, LocalDate endDate) {
        if (startDate != null && endDate != null && startDate.isAfter(endDate)) {
            throw new CustomException(ValidationErrorCode.DATE_RANGE_ERROR);
        }
    }
}