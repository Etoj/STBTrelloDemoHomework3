package trello;

import base.BaseTest;

import static io.restassured.RestAssured.given;
import static trello.OrganizationInvalidDataTest.organizationId;
import static trello.OrganizationInvalidDataTest.response;

public class DeleteOrganization extends BaseTest {

    public static void deleteOrganization() {

        if (response.statusCode() == 200 || response.statusCode() == 201) {
            given()
                    .spec(reqSpec)
                    .when()
                    .delete(BASE_URL + "/" + ORGANIZATIONS + "/" + organizationId)
                    .then()
                    .statusCode(200);
        } else
            System.out.println("The organization was not created, delete was omitted");//
    }
}
