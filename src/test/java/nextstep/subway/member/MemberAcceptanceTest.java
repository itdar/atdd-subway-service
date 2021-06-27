package nextstep.subway.member;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.auth.dto.TokenResponse;
import nextstep.subway.member.dto.MemberRequest;
import nextstep.subway.member.dto.MemberResponse;
import nextstep.subway.utils.RestAssuredCRUD;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import static nextstep.subway.auth.acceptance.AuthAcceptanceTest.로그인_요청;
import static org.assertj.core.api.Assertions.assertThat;

public class MemberAcceptanceTest extends AcceptanceTest {
    public static final String EMAIL = "email@email.com";
    public static final String PASSWORD = "password";
    public static final String NEW_EMAIL = "newemail@email.com";
    public static final String NEW_PASSWORD = "newpassword";
    public static final int AGE = 20;
    public static final int NEW_AGE = 21;

    @BeforeEach
    public void setUp() {
        super.setUp();
    }

    @DisplayName("회원 정보를 관리한다.")
    @Test
    void manageMember() {
        // when
        ExtractableResponse<Response> createResponse = 회원_등록되어_있음(EMAIL, PASSWORD, AGE);
        // then
        회원_생성됨(createResponse);

        // when
        ExtractableResponse<Response> findResponse = 회원_정보_조회_요청(createResponse);
        // then
        회원_정보_조회됨(findResponse, EMAIL, AGE);

        // when
        ExtractableResponse<Response> updateResponse = 회원_정보_수정_요청(createResponse, EMAIL, PASSWORD, NEW_AGE);
        // then
        회원_정보_수정됨(updateResponse);

        // when
        ExtractableResponse<Response> deleteResponse = 회원_삭제_요청(createResponse);
        // then
        회원_삭제됨(deleteResponse);
    }

    @DisplayName("나의 정보를 관리한다.")
    @Test
    void manageMyInfo() {
        // Given 생성 및 로그인
        회원_등록되어_있음(EMAIL, PASSWORD, AGE);
        ExtractableResponse<Response> tokenResponse = 로그인_요청(EMAIL, PASSWORD);
        String token = tokenResponse.as(TokenResponse.class).getAccessToken();

        // When
        ExtractableResponse<Response> meResponse = 내_정보_조회(token);
        // Then
        회원_정보_조회됨(meResponse, EMAIL, AGE);

        // When
        ExtractableResponse<Response> updateResponse = 내_정보_수정_요청(
                new MemberRequest(EMAIL, PASSWORD, NEW_AGE), token);
        // Then
        회원_정보_수정됨(updateResponse);
        meResponse = 내_정보_조회(token);
        회원_정보_조회됨(meResponse, EMAIL, NEW_AGE);

        // When
        ExtractableResponse<Response> deleteResponse = 내_정보_삭제_요청(token);
        // Then
        회원_삭제됨(deleteResponse);
    }

    public static ExtractableResponse<Response> 내_정보_조회(String token) {
        return RestAssuredCRUD.getWithOAuth("/members/me", token);
    }

    public static ExtractableResponse<Response> 내_정보_수정_요청(MemberRequest memberRequest, String token) {
        return RestAssuredCRUD.putRequestWithAOuth("/members/me", memberRequest, token);
    }

    public static ExtractableResponse<Response> 내_정보_삭제_요청(String token) {
        return RestAssuredCRUD.deleteWithOAuth("/members/me", token);
    }

    public static ExtractableResponse<Response> 회원_등록되어_있음(String email, String password, Integer age) {
        MemberRequest memberRequest = new MemberRequest(email, password, age);
        return RestAssuredCRUD.postRequest("/members", memberRequest);
    }

    public static ExtractableResponse<Response> 회원_정보_조회_요청(ExtractableResponse<Response> response) {
        String uri = response.header("Location");
        return RestAssuredCRUD.get(uri);
    }

    public static ExtractableResponse<Response> 회원_정보_수정_요청(ExtractableResponse<Response> response, String email, String password, Integer age) {
        String uri = response.header("Location");
        MemberRequest memberRequest = new MemberRequest(email, password, age);
        return RestAssuredCRUD.putRequest(uri, memberRequest);
    }

    public static ExtractableResponse<Response> 회원_삭제_요청(ExtractableResponse<Response> response) {
        String uri = response.header("Location");
        return RestAssuredCRUD.delete(uri);
    }

    public static void 회원_생성됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    public static void 회원_정보_조회됨(ExtractableResponse<Response> response, String email, int age) {
        MemberResponse memberResponse = response.as(MemberResponse.class);
        assertThat(memberResponse.getId()).isNotNull();
        assertThat(memberResponse.getEmail()).isEqualTo(email);
        assertThat(memberResponse.getAge()).isEqualTo(age);
    }

    public static void 회원_정보_수정됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    public static void 회원_삭제됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }
}
