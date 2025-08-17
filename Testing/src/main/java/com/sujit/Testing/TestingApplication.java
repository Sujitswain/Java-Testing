package com.sujit.Testing;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class TestingApplication {

	public static void main(String[] args) {
		SpringApplication.run(TestingApplication.class, args);
	}

	/**
	 *
	 * GET 			->		/api/products
	 * GET			-> 		/api/products/{id}
	 * POST			-> 		/api/products
	 * PUT			->		/api/products/{id}
	 * DELETE		-> 		/api/products/{id}
	 *
	 */

}
