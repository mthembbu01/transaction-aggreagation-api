/**
 * 
 */
package za.co.capitec.coreapi.dtos.accounts.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import za.co.capitec.coreapi.dtos.accounts.records.AccountsRecord;

import java.util.List;

/**
 * @author Buhlebakhe.Mthembu
 */
@Data
@AllArgsConstructor
@Builder
public class  AccountsResponse {
	private List<AccountsRecord> content;
	private int pageNo;
	private int pageSize;
	private int totalPages;
	private long totalElements;
	private boolean isLast;
}
