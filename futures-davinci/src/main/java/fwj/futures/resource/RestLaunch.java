package fwj.futures.resource;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = "fwj.futures.resource")
public class RestLaunch {

	public static void main(String[] args) {
		ConfigurableApplicationContext cxt = SpringApplication.run(RestLaunch.class);
		cxt.registerShutdownHook();
	}
}
