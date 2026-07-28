/**
 * 
 */
package za.co.capitec.coreapi.dtos.customer.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import za.co.capitec.coreapi.dtos.customer.records.CustomersRecord;

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
