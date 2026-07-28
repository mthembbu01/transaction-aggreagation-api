package za.co.capitec.accounts.accountTests;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import za.co.capitec.accounts.TestcontainersConfiguration;

@Import(TestcontainersConfiguration.class)
@SpringBootTest
class AccountsAppTests {

	@Test
	void contextLoads() {
	}

}
