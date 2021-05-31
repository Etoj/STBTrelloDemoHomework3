package trello;

import base.BaseTest;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.*;

public class OrganizationInvalidDataTest extends BaseTest {

    protected static String organizationId;
    public static Response response;

    private static Stream<Arguments> createInvalidOrganizationData() {
        return Stream.of(
                Arguments.of("incorrect protocol", "invalid data 1", "name test", "sftp"),
                Arguments.of("1", "2", "3", "4"),
                Arguments.of("short name", "invalid data 2", "aa", "http"),
                Arguments.of("capital letter", "invalid data 3", "CAPITAL", "http"),
                Arguments.of("special char", "invalid data 4", "name !@#$%^&*()", "http"),
                Arguments.of("must be unique", "invalid data 5", "name test", "ftp"),
                Arguments.of("must be unique", "invalid data 5", "name test", "ftp")
        );
    }
    @DisplayName("Create organization with invalid valid data")
    @ParameterizedTest(name = "Display name: {0}, desc: {1}, name:{2}, website:{3}")
    @MethodSource("createInvalidOrganizationData")
    public void createOrganizationWithInvalidData(String displayName, String desc, String name, String website) {

        Organization organization = new Organization();
        organization.setDisplayName(displayName);
        organization.setDesc(desc);
        organization.setName(name);
        organization.setWebsite(website);

        response = given()
                .spec(reqSpec)
                .queryParam("displayName", organization.getDisplayName())
                .queryParam("desc", organization.getDesc())
                .queryParam("name", organization.getName())
                .queryParam("website", organization.getWebsite())
                .when()
                .post(BASE_URL + "/" + ORGANIZATIONS)
                .then()
                .statusCode(200)
                .extract()
                .response();

        JsonPath json = response.jsonPath();
        assertThat(json.getString("displayName")).isEqualTo(organization.getDisplayName());
        assertThat(json.getString("desc")).isEqualTo(organization.getDesc());
        assertThat(json.getString("name")).isEqualTo(organization.getName());
        assertThat(json.getString("website")).isEqualTo(organization.getWebsite());

        organizationId = json.getString("id");

        DeleteOrganization.deleteOrganization();
    }
}
