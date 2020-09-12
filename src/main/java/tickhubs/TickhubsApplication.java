package tickhubs;

import java.util.TimeZone;

import javax.annotation.PostConstruct;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;

/**
 * $ created by Vaibhav Varshney on : Aug 22, 2020
 */

@SpringBootApplication
@EntityScan(basePackageClasses = { TickhubsApplication.class })
//@EnableEurekaServer
public class TickhubsApplication {

	@PostConstruct
	void init() {
		TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
	}

	public static void main(String[] args) {
		SpringApplication.run(TickhubsApplication.class, args);
	}

}
