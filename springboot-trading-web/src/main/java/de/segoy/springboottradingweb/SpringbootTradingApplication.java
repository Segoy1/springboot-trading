package de.segoy.springboottradingweb;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableJpaRepositories(basePackages = "de.segoy")
@EntityScan(basePackages = "de.segoy")
@ComponentScan(basePackages = "de.segoy")
@EnableTransactionManagement
@EnableAspectJAutoProxy
public class SpringbootTradingApplication {

	public static final Integer LIVE_TRADING_PORT = 7496;
	public static final Integer PAPER_TRADING_PORT = 7497;

	public static void main(String[] args) {
		ApplicationContext context = SpringApplication.run(SpringbootTradingApplication.class, args);

		ConnectionInitiator connection = context.getBean(ConnectionInitiator.class);
		connection.connect(PAPER_TRADING_PORT);
	}

}
