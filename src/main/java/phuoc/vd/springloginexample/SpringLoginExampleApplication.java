package phuoc.vd.springloginexample;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Import;
import phuoc.vd.springloginexample.config.AppProperties;
import phuoc.vd.springloginexample.config.SwaggerConfiguration;

@SpringBootApplication
@EnableConfigurationProperties(AppProperties.class)
@Import(SwaggerConfiguration.class)
public class SpringLoginExampleApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringLoginExampleApplication.class, args);
	}

}
