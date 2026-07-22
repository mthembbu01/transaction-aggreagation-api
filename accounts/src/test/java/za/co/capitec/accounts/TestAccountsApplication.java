package za.co.capitec.accounts;

import org.springframework.boot.SpringApplication;

public class TestAccountsApplication {

	public static void main(String[] args) {
		SpringApplication.from(AccountsApp::main).with(TestcontainersConfiguration.class).run(args);
	}

}
