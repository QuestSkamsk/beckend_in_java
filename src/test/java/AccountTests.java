import io.restassured.response.Response;
import org.junit.jupiter.api.Test;
import static io.restassured.RestAssured.given;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsEqual.equalTo;

public class AccountTests extends BaseTest {

    @Test
    void getAccountInfoTest() {
        given()
                .header("Authorization", token)
                .log()
                .method()
                .log()
                .uri()
                .when()
                .get("https://api.imgur.com/3/account/{username}", username)
                .then()
                .statusCode(200);
    }

    @Test
    void getAccountAccountWithLoggingTest() {
        given()
                .header("Authorization", token)
                .log()
                .method()
                .log()
                .uri()
                .when()
                .get("https://api.imgur.com/3/account/{username}", username)
                .prettyPeek()
                .then()
                .statusCode(200);
    }

    @Test
    void getGalleryTagsTest() {
        given()
                .headers("Authorization", token)
                .log()
                .method()
                .log()
                .uri()
                .when()
                .get("https://api.imgur.com/3/tags")
                .prettyPeek()
                .then()
                .extract()
                .response()
                .jsonPath()
                .getString("data.tags.name");
    }

    @Test
    void postFollowTagTest() {
        given()
                .headers("Authorization", token)
                .log()
                .method()
                .log()
                .uri()
                .when()
                .post("https://api.imgur.com/3/account/me/follow/tag/{tagName}", "disney")
                .prettyPeek()
                .then()
                .statusCode(200);
    }

    @Test
    void getAccountInfoWithAssertionsInGivenTest() {
        given()
                .header("Authorization", token)
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
        given()
                .headers("Authorization", token)
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
                .statusCode(200)
                .contentType("application/json");
    }

    @Test
    void getAccountInfoWithAssertionsAfterTest() {
        Response response = given()
                .header("Authorization", token)
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
        imageHash=given()
                .headers("Authorization", token)
                .log()
                .method()
                .log()
                .uri()
                .when()
                .get("https://api.imgur.com/3/account/me/images")
                .prettyPeek()
                .then()
                .statusCode(200)
                .extract()
                .response()
                .jsonPath()
                .getString("data.id");
    }
}
