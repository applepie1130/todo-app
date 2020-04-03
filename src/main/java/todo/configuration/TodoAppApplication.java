package todo.configuration;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;

import todo.BasePackageLocation;

@SpringBootApplication
@EnableAutoConfiguration
@ComponentScan(basePackageClasses = BasePackageLocation.class)
@Import({TodoWebConfig.class})
public class TodoAppApplication {
	public static void main(String[] args) {
		SpringApplication.run(TodoAppApplication.class, args);
	}
}
