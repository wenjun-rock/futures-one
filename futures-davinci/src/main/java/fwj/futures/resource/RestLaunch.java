package fwj.futures.resource;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class RestLaunch {

	public static void main(String[] args) {
		ConfigurableApplicationContext cxt = SpringApplication //
				.run(new String[] { "classpath:applicationContext.xml", //
						"classpath:dataSource.xml" }, args);
		cxt.registerShutdownHook();
	}
}
