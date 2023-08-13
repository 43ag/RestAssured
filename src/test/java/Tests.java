import dto.GetOrderResponseDTO;
import dto.OrderDTO;
import dto.OrderResponseDTO;
import io.restassured.response.ValidatableResponse;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import services.OrderOtusApi;

import java.time.LocalDateTime;

public class Tests {

    /*
    Тест кейс №1. Позитивная проверка создания заказа в магазине
    номер шага  |        шаг            |           данные                          |       ожидание                             |
        1       | отправить post запрос |        базовый урл =                      |     код                                    |
                | на создание заказа в  |   https://petstore.swagger.io/v2          |        ответа = 200                        |
                | магазине при помощи   |    пример тела запроса:                   |   пример тела ответа:                      |
                | эндпоинта             |  { "id": 0,                               |   { "id": 0,                               |
                | /store/order          |    "petId": 0,                            |     "petId": 0,                            |
                |                       |    "quantity": 0,                         |     "quantity": 0,                         |
                |                       |    "shipDate": "2023-07-24T13:54:45.519Z",|     "shipDate": "2023-07-24T13:54:45.519Z",|
                |                       |    "status": "placed",                    |     "status": "placed",                    |
                |                       |    "complete": true                       |     "complete": true                       |
                |                       |   }                                       |    }                                       |

     */
    @Test
    public void checkStoreOrder() {
        OrderOtusApi orderOtusApi = new OrderOtusApi();

        String date = LocalDateTime.now().toString();

        OrderDTO order = OrderDTO
                .builder()
                .id(9)
                .petId(1)
                .quantity(500)
                .shipDate(date)
                .status("placed")
                .complete(true)
                .build();
        ValidatableResponse response = orderOtusApi.createOrder(order)
                .statusCode(200);
        OrderResponseDTO orderResponseDTO = response.extract().body().as(OrderResponseDTO.class);
        boolean complete = response.extract().jsonPath().get("complete");
        Assertions.assertAll("check create order",
                () -> Assertions.assertEquals(9, orderResponseDTO.getId(), "Incorrect Id"),
                () -> Assertions.assertEquals(1, orderResponseDTO.getPetId(), "Incorrect PetId"),
                () -> Assertions.assertEquals(500, orderResponseDTO.getQuantity(), "Incorrect Quantity"),
                () -> Assertions.assertEquals(date.substring(0, 23), orderResponseDTO.getShipDate().substring(0, 23), "Incorrect ShipDate"),
                () -> Assertions.assertEquals("placed", orderResponseDTO.getStatus(), "Incorrect Status"),
                () -> Assertions.assertEquals(true, complete, "Incorrect Complete")
        );
    }

    /*
Тест кейс №2. Проверка создания заказа в магазине с единственным полем id
номер шага  |        шаг            |           данные                          |       ожидание                             |
        1   | отправить post запрос |        базовый урл =                      |     код                                    |
            | на создание заказа в  |   https://petstore.swagger.io/v2          |        ответа = 200                        |
            | магазине при помощи   |    пример тела запроса:                   |   пример тела ответа:                      |
            | эндпоинта             |  { "id": 1 }                              |  {  "id": 1,                               |
            |                       |                                           |     "petId": 0,                            |
            |                       |                                           |     "quantity": 0,                         |
            |                       |                                           |     "complete": false                      |
            |                       |                                           |   }                                        |
            |                       |                                           |                                            |

 */
    @Test
    public void checkOnlyIdStoreOrder() {
        OrderOtusApi orderOtusApi = new OrderOtusApi();
        OrderDTO order = OrderDTO
                .builder()
                .id(1)
                .build();
        ValidatableResponse response = orderOtusApi.createOrder(order)
                .statusCode(200);
        OrderResponseDTO orderResponseDTO = response.extract().body().as(OrderResponseDTO.class);
        boolean complete = response.extract().jsonPath().get("complete");
        Assertions.assertAll("check create order",
                () -> Assertions.assertEquals(1, orderResponseDTO.getId(), "Incorrect Id"),
                () -> Assertions.assertEquals(0, orderResponseDTO.getPetId(), "Incorrect PetId"),
                () -> Assertions.assertEquals(0, orderResponseDTO.getQuantity(), "Incorrect Quantity"),
                () -> Assertions.assertEquals(false, complete, "Incorrect Complete")
        );
    }

        /*
Тест кейс №3. Проверка получения информации о заказе по id
номер шага  |        шаг            |           данные                          |       ожидание                             |
        1   | отправить get запрос  |        базовый урл =                      |     код                                    |
            |на получение информации|   https://petstore.swagger.io/v2          |        ответа = 200                        |
            | о заказе при помощи   |                                           |   пример тела ответа:                      |
            | эндпоинта             |                                           |  {  "id": 9,                               |
            |/store/order/{orderId} |                                           |     "petId": 1,                            |
            |                       |                                           |     "quantity": 500,                       |
            |                       |                                           |  "shipDate": "2023-07-28T08:10:02.355+0000"|
            |                       |                                           |     "status": "placed"                     |
            |                       |                                           |   "complete": true                         |
            |                       |                                           |   }                                        |
              Для корректной работы теста запустить сначала тест-кейс №1
 */

    @Test
    public void checkOrderById() {
        OrderOtusApi orderOtusApi = new OrderOtusApi();
        ValidatableResponse response = orderOtusApi.getOrder(9)
                .statusCode(200);
        OrderResponseDTO orderResponseDTO = response.extract().body().as(OrderResponseDTO.class);
        boolean complete = response.extract().jsonPath().get("complete");
        Assertions.assertAll("check get order",
                () -> Assertions.assertEquals(9, orderResponseDTO.getId(), "Incorrect Id"),
                () -> Assertions.assertEquals(1, orderResponseDTO.getPetId(), "Incorrect PetId"),
                () -> Assertions.assertEquals(500, orderResponseDTO.getQuantity(), "Incorrect Quantity"),
                () -> Assertions.assertEquals("placed", orderResponseDTO.getStatus(), "Incorrect Status"),
                () -> Assertions.assertEquals(true, complete, "Incorrect Complete")
        );
    }

    /*
Тест кейс №4. Проверка получения информации о заказе по несуществующему в базе id
номер шага  |        шаг            |           данные                          |       ожидание                             |
        1   | отправить get запрос  |        базовый урл =                      |     код                                    |
            |на получение информации|   https://petstore.swagger.io/v2          |        ответа = 200                        |
            | о заказе при помощи   |                                           |   пример тела ответа:                      |
            | эндпоинта             |                                           |  {  "code": 1,                             |
            |/store/order/{orderId} |                                           |     "type": "error",                       |
            |                       |                                           |     "message": "Order not found",          |
            |                       |                                           |   }                                        |
*/
    @Test
    public void checkOrderById404() {
        OrderOtusApi orderOtusApi = new OrderOtusApi();
        ValidatableResponse response = orderOtusApi.getOrder(6)
                .statusCode(404);
        GetOrderResponseDTO getOrderResponseDT = response.extract().body().as(GetOrderResponseDTO.class);
        Assertions.assertAll("check get order",
                () -> Assertions.assertEquals(1, getOrderResponseDT.getCode(), "Incorrect Code"),
                () -> Assertions.assertEquals("error", getOrderResponseDT.getType(), "Incorrect Type"),
                () -> Assertions.assertEquals("Order not found", getOrderResponseDT.getMessage(), "Incorrect Message")
        );
    }
}