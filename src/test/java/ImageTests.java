import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.*;
import java.io.File;
import java.io.IOException;
import java.util.Base64;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.core.IsNull.notNullValue;


public class ImageTests extends BaseTest {

    private final String PATH_TO_IMAGE = "src/test/resources/Dragon.jpg";
    static String encodedFile;
    String addedImageId;

    @BeforeEach
    void beforeTest(){
        byte[] byteArray = getFileContent();
        encodedFile = Base64.getEncoder().encodeToString(byteArray);
    }

    @Test
    void postAddImageTest(){
        addedImageId = given(requestSpecification)
                .multiPart("image", encodedFile)
                .formParam("title", "act")
                .log()
                .method()
                .log()
                .uri()
                .expect()
                .body("success", is(true))
                .body("data.id", is(notNullValue()))
                .when()
                .post("/image")
                .prettyPeek()
                .then()
                .spec(responseSpecification)
                .extract()
                .response()
                .jsonPath()
                .getString("data.deletehash");
    }

    @Test
    void postAddImageFaviritesTest(){
        addedImageId = given(requestSpecification)
                .multiPart("image", encodedFile)
                .formParam("title", "act")
                .log()
                .method()
                .log()
                .uri()
                .expect()
                .body("success", is(true))
                .body("data.id", is(notNullValue()))
                .when()
                .post("/image")
                .prettyPeek()
                .then()
                .spec(responseSpecification)
                .extract()
                .response()
                .jsonPath()
                .getString("data.id");
        addFavirites();
    }

    void addFavirites(){
        given(requestSpecification)
                .log()
                .method()
                .log()
                .uri()
                .when()
                .post("/image/{imageHash}/favorite", addedImageId)
                .prettyPeek()
                .then()
                .spec(responseSpecification);
    }

    @Test
    void postUnfavoritedImage(){
        postAddImageFaviritesTest();
        addFavirites();
    }

    private byte[] getFileContent(){
        byte[] byteArray = new byte[0];
        try {
            byteArray = FileUtils.readFileToByteArray(new File(PATH_TO_IMAGE));
        } catch (IOException e){
            e.printStackTrace();
        }
        return byteArray;
    }

    @AfterEach
    void takeDown(){
        given(requestSpecification)
                .log()
                .method()
                .log()
                .uri()
                .when()
                .delete("/account/{username}/image/{deleteHash}", username, addedImageId)
                .prettyPeek()
                .then()
                .spec(responseSpecification);
    }
}