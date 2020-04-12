package todo.configuration;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

import todo.BasePackageLocation;
import todo.api.repository.BaseMongoPackageLocation;

@SpringBootApplication
@EnableAutoConfiguration
@EnableMongoRepositories(basePackageClasses = BaseMongoPackageLocation.class)
@EntityScan(basePackageClasses = BasePackageLocation.class)
@ComponentScan(basePackageClasses = BasePackageLocation.class)
public class TodoAppApplication {
	public static void main(String[] args) {
		SpringApplication.run(TodoAppApplication.class, args);
	}
}
