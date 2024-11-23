package withbeetravel.service.payment;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import okhttp3.*;
import org.springframework.stereotype.Service;
import withbeetravel.config.OpenAIConfig;
import withbeetravel.domain.Category;
import withbeetravel.exception.CustomException;
import withbeetravel.exception.error.TravelErrorCode;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class SharedPaymentCategoryClassificationServiceImpl implements SharedPaymentCategoryClassificationService {

    // 생성형 AI 관련 필드
    private final OpenAIConfig config;
    private final ObjectMapper objectMapper;
    private final OkHttpClient client = new OkHttpClient();

    // 생성형 AI 메시지 클래스
    @RequiredArgsConstructor
    @Getter
    private static class Message {
        private final String role;
        private final String content;
    }

    @Override
    public Category getCategory(String storeName) {
        try {
            List<Message> messages = new ArrayList<>();
            messages.add(new Message("system",
                    "당신은 여행 카테고리를 분류하는 AI입니다. " +
                            "상호명을 보고 다음 카테고리 중 하나로만 분류해주세요. 국가는 한국입니다: " +
                            "항공, 교통, 숙박, 식비, 관광, 액티비티, 쇼핑, 기타 " +
                            "카테고리 이름만 정확히 답변해주세요. " +
                            "- 호텔, 리조트, 에어비앤비 등은 '숙박' " +
                            "- 식당, 카페, 바 등은 '식비' " +
                            "- 버스, 택시, 지하철, 기차 등은 '교통' " +
                            "- 항공사, 공항 등은 '항공' " +
                            "- 박물관, 미술관, 랜드마크 등은 '관광' " +
                            "- 테마파크, 스포츠, 체험 등은 '액티비티' " +
                            "- 마트, 쇼핑몰, 아울렛 등은 '쇼핑' 으로 분류해주세요."
            ));

            messages.add(new Message("user", String.format("상호명: %s", storeName)));

            String requestBody = objectMapper.writeValueAsString(Map.of(
                    "model", config.getModel(),
                    "messages", messages,
                    "temperature", 0.3
            ));

            Request request = new Request.Builder()
                    .url(config.getEndpoint())
                    .post(RequestBody.create(
                            requestBody,
                            MediaType.parse("application/json")
                    ))
                    .addHeader("Authorization", "Bearer " + config.getApiKey())
                    .build();

            try (Response response = client.newCall(request).execute()) {
                if (!response.isSuccessful()) {
                    throw new CustomException(TravelErrorCode.TRAVEL_CATEGORY_NOT_FOUND);
                }

                JsonNode jsonResponse = objectMapper.readTree(response.body().string());
                String categoryName = jsonResponse
                        .path("choices")
                        .get(0)
                        .path("message")
                        .path("content")
                        .asText()
                        .trim();

                // 반환된 카테고리명을 Category enum으로 변환
                return switch (categoryName) {
                    case "항공" -> Category.FLIGHT;
                    case "교통" -> Category.TRANSPORTATION;
                    case "숙박" -> Category.ACCOMMODATION;
                    case "식비" -> Category.FOOD;
                    case "관광" -> Category.TOUR;
                    case "액티비티" -> Category.ACTIVITY;
                    case "쇼핑" -> Category.SHOPPING;
                    default -> Category.ETC;
                };
            }
        } catch (Exception e) {
            // OpenAI API 호출 실패시 기타로 분류
            return Category.ETC;
        }
    }
}
