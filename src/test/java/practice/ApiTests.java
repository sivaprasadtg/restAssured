package practice;

import models.Products;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class ApiTests {

    @Test
    public void getCategories(){
        String endpoint = "http://localhost:8080/api_testing/category/read.php";
        var response = given().when().get(endpoint).then();
        response.log().body();
    }

    @Test
    public void getProducts() {
        String endpoint = "http://localhost:8888/api_testing/product/read.php";
        var response = given().when().get(endpoint).then();
        response.assertThat(). // validating complex data within response body
                statusCode(200).
                body("records.size()", greaterThan(0)).
                body("records.id", everyItem(notNullValue()));
        response.assertThat(). //validating info in response headers.
                header("Content-Type", equalTo("application/json; charset=UTF-8"));
        response.log().body();
    }

    @Test
    public void getProduct(){
        String endpoint = "http://localhost:8888/api_testing/product/read_one.php";
        var response =
                given().
                        queryParam("id",2).
                when().
                       get(endpoint).
                then();
        response.log().body();
        response.assertThat().statusCode(200); //validating the expected response code.
        response.assertThat(). //verify whether the response contains what's needed and only what's needed!
                body("id", equalTo("2")).
                body("name",equalTo("Water bottle"));
    }

    @Test
    public void createProduct(){
        String endpoint = "http://localhost:8888/api_testing/product/create.php";
        String body = "{\n"+
            "    \" name\": \"Water Bottle\",\n"+
            "    \"description\": \"Blue water bottle that holds 1 liter\",\n"+
            "    \"price\": 12,\n"+
            "    \"category_id\": 3\n"+
            "}";
        var response = given().body(body).when().post(endpoint).then();
        response.log().body();
    }

    @Test
    public void putProduct(){
        String endpoint = "http://localhost:8888/api_testing/product/update.php";
        String body = "{\n"+
                "    \"id\": 19,\n"+
                "    \" name\": \"Water Bottle\",\n"+
                "    \"description\": \"Blue water bottle that holds 1 liter\",\n"+
                "    \"price\": 15,\n"+  //this is where the update has been done.
                "    \"category_id\": 3\n"+
                "}";
        var response = given().when().body(body).put(endpoint).then();
        response.log().body();
    }

    @Test
    public void deleteProduct(){
        String endpoint = "http://localhost:8888/api_testing/product/delete.php";
        String body = "{\n"+
                "    \"id\": 19,\n"+
                "}";
        var response = given().body(body).when().delete(endpoint).then();
        response.log().body();
    }

    @Test
    public void createSerializedProduct(){
        String endpoint = "http://localhost:8888/api_testing/product/create.php";
        Products product = new Products(
                "Water Bottle",
                "Blue water bottle that can hold 1 liter",
                12,
                3
        );
        var response = given().body(product).when().post(endpoint).then();
        response.log().body();
    }

    @Test
    public void getDeserializedProduct(){
        String endpoint = "http://localhost:8888/api_testing/product/read_one.php";
        Products expectedProduct = new Products(
                2,
                "Shirt",
                "A wearable item.",
                299,
                2,
                "Apparel"
        );
        Products actualProduct = given().
                                    queryParam("id", 4).
                                when().
                                    get(endpoint).
                                    as(Products.class);
        assertThat(actualProduct, samePropertyValuesAs(expectedProduct));
    }
}
