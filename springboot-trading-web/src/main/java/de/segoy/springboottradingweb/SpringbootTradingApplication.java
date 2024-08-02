package de.segoy.springboottradingweb;

import de.segoy.springboottradingdata.config.PropertiesConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableJpaRepositories(basePackages = "de.segoy")
@EntityScan(basePackages = "de.segoy")
@ComponentScan(basePackages = "de.segoy")
@EnableTransactionManagement
@EnableAspectJAutoProxy
@EnableScheduling
public class SpringbootTradingApplication {

	public static void main(String[] args) {
		ApplicationContext context = SpringApplication.run(SpringbootTradingApplication.class, args);

		PropertiesConfig propertiesConfig = context.getBean(PropertiesConfig.class);
		ConnectionInitiator connection = context.getBean(ConnectionInitiator.class);
		connection.connect(propertiesConfig.getTradingPort());
	}

}
