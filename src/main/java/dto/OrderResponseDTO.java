
package dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
public class OrderResponseDTO {

    private boolean complete;
    private int id;
    private int petId;
    private int quantity;
    private String shipDate;
    private String status;
}
