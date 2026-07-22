package za.co.capitec.loans;

import org.springframework.boot.SpringApplication;

public class TestLoansApplication {

	public static void main(String[] args) {
		SpringApplication.from(LoansApp::main).with(TestcontainersConfiguration.class).run(args);
	}

}
