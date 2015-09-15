package fwj.futures.launch;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class RestLaunch {

	public static void main(String[] args) {
		SpringApplication.run("classpath:applicationContext.xml", args);
	}
}
