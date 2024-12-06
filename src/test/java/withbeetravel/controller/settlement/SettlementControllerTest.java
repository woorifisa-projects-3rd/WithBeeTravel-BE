package withbeetravel.controller.settlement;

import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import withbeetravel.controller.BaseIntegrationTest;

import static io.restassured.RestAssured.given;
import static org.apache.http.HttpHeaders.AUTHORIZATION;

class SettlementControllerTest extends BaseIntegrationTest {

    @Test
    void 정산을_요청한다() {
        given().log().all()
                .header(AUTHORIZATION, "Bearer " + accessToken)
                .contentType(ContentType.JSON)
                .when()
                .post("/api/travels/1/settlements")
                .then()
                .log()
                .all()
                .statusCode(HttpStatus.OK.value());
    }

    @Test
    void 정산을_동의한다() {
        given().log().all()
                .header(AUTHORIZATION, "Bearer " + accessToken)
                .contentType(ContentType.JSON)
                .when()
                .post("/api/travels/1/settlements/agreement")
                .then()
                .log()
                .all()
                .statusCode(HttpStatus.OK.value());
    }

    @Test
    void 정산을_취소한다() {
        given().log().all()
                .header(AUTHORIZATION, "Bearer " + accessToken)
                .contentType(ContentType.JSON)
                .when()
                .delete("/api/travels/1/settlements")
                .then()
                .log()
                .all()
                .statusCode(HttpStatus.OK.value());
    }

    @Test
    void 세부지출내역을_조회한다() {
        given().log().all()
                .header(AUTHORIZATION, "Bearer " + accessToken)
                .contentType(ContentType.JSON)
                .when()
                .get("/api/travels/1/settlements")
                .then()
                .log()
                .all()
                .statusCode(HttpStatus.OK.value());
    }
}
