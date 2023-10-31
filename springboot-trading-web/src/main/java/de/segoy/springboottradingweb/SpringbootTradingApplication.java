package de.segoy.springboottradingweb;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories(basePackages = "de.segoy.springboottradingdata.dao")
@EntityScan(basePackages = "de.segoy.springboottradingdata.ds")
public class SpringbootTradingApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringbootTradingApplication.class, args);
	}

}
