/**
 * 
 */
package za.co.capitec.customer.entity.dtos.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import za.co.capitec.customer.entity.dtos.records.CustomersRecord;

import java.util.List;

/**
 * @author Buhlebakhe.Mthembu
 */
@Data
@AllArgsConstructor
@Builder
public class CustomerResponse {
	private List<CustomersRecord> content;
	private int pageNo;
	private int pageSize;
	private int totalPages;
	private long totalElements;
	private boolean isLast;
}
