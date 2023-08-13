package services;

import dto.OrderDTO;
import io.restassured.http.ContentType;
import io.restassured.response.ValidatableResponse;
import io.restassured.specification.RequestSpecification;

import static io.restassured.RestAssured.given;

public class OrderOtusApi {
    public static final String BASE_URL= "https://petstore.swagger.io/v2";
    public static final String ORDER= "/store/order";
    private RequestSpecification spec;
    public OrderOtusApi(){
        spec = given()
                .baseUri(BASE_URL)
                .contentType(ContentType.JSON);
    }

    public ValidatableResponse createOrder(OrderDTO orderDTO){
       return given(spec)
                .log().all()
                .body(orderDTO)
                .when()
                .post(ORDER)
                .then()
                .log().all();
    }

    public ValidatableResponse getOrder(int id){
        return given(spec)
                .log().all()
                .when()
                .get(ORDER+"/"+id)
                .then()
                .log().all();
    }
}
