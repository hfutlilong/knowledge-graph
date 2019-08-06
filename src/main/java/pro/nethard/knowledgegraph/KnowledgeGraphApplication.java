package pro.nethard.knowledgegraph;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class KnowledgeGraphApplication {

	public static void main(String[] args) {
		SpringApplication.run(KnowledgeGraphApplication.class, args);
	}

}
