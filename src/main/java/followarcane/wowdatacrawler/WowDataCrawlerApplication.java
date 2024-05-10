package followarcane.wowdatacrawler;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class WowDataCrawlerApplication {

	public static void main(String[] args) {
		SpringApplication.run(WowDataCrawlerApplication.class, args);
	}

}
