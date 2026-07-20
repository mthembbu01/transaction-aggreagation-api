package za.co.capitec.creditcards;

import org.springframework.boot.SpringApplication;

public class TestCreditcardsApplication {

	public static void main(String[] args) {
		SpringApplication.from(CreditcardsApplication::main).with(TestcontainersConfiguration.class).run(args);
	}

}
