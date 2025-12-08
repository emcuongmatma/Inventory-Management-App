
package DTO;

import java.util.Date;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import utils.OrderStatus;

/**
 *
 * @author ECMM
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderDTO {
    private String userId;
    private Long totalAmount;
    private String paymentMethod;
    private OrderStatus status;
    private Date orderDate;
    private List<OrderItemDTO> items;
}