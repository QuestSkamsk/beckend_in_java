import io.restassured.response.Response;
import org.junit.jupiter.api.Test;
import static io.restassured.RestAssured.given;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsEqual.equalTo;

public class AccountTests extends BaseTest {

    @Test
    void getAccountInfoTest() {
        given(requestSpecification)
                .log()
                .method()
                .log()
                .uri()
                .when()
                .get("https://api.imgur.com/3/account/{username}", username)
                .then()
                .spec(responseSpecification);
    }

    @Test
    void getAccountAccountWithLoggingTest() {
        given(requestSpecification)
                .log()
                .method()
                .log()
                .uri()
                .when()
                .get("https://api.imgur.com/3/account/{username}", username)
                .prettyPeek()
                .then()
                .spec(responseSpecification);
    }

    @Test
    void getGalleryTagsTest() {
        given(requestSpecification)
                .log()
                .method()
                .log()
                .uri()
                .when()
                .get("https://api.imgur.com/3/tags")
                .prettyPeek()
                .then()
                .spec(responseSpecification)
                .extract()
                .response()
                .jsonPath()
                .getString("data.tags.name");

    }


    @Test
    void getAccountInfoWithAssertionsInGivenTest() {
        given(requestSpecification)
                .log()
                .method()
                .log()
                .uri()
                .expect()
                .statusCode(200)
                .contentType("application/json")
                .body("data.url", equalTo(username))
                .body("success", equalTo(true))
                .body("status", equalTo(200))
                .when()
                .get("https://api.imgur.com/3/account/{username}", username)
                .prettyPeek();

    }

    @Test
    void putChangeAccountSettingsTest() {
        given(requestSpecification)
                .multiPart("public_images", true)
                .multiPart("messaging_enabled", false)
                .log()
                .method()
                .log()
                .uri()
                .when()
                .put("https://api.imgur.com/3/account/{username}/settings", username)
                .prettyPeek()
                .then()
                .spec(responseSpecification);
    }

    @Test
    void getAccountInfoWithAssertionsAfterTest() {
        Response response = given(requestSpecification)
                .log()
                .method()
                .log()
                .uri()
                .when()
                .get("https://api.imgur.com/3/account/{username}", username)
                .prettyPeek();
        assertThat(response.jsonPath().get("data.url"), equalTo(username));
        assertThat(response.statusCode(), equalTo(200));
    }

    @Test
    void getAccountImagesTest() {
        imageHash=given(requestSpecification)
                .log()
                .method()
                .log()
                .uri()
                .when()
                .get("https://api.imgur.com/3/account/me/images")
                .prettyPeek()
                .then()
                .spec(responseSpecification)
                .extract()
                .response()
                .jsonPath()
                .getString("data.id");
    }

    @Test
    void postTokenDTOTest(){
        MyDTO myDTO = new MyDTO();
        myDTO.setUsername("admin");
        myDTO.setPassword("password123");

        TokenDTO token = given()
                .contentType("application/json; charset=utf-8").log().all()
                .when().body(myDTO)
                .request("POST", "https://restful-booker.herokuapp.com/auth")
                .prettyPeek()
                .then().statusCode(200).extract()
                .body()
                .as(TokenDTO.class);
    }
}
