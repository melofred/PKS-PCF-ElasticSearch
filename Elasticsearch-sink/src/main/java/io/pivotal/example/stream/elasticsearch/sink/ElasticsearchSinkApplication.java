package io.pivotal.example.stream.elasticsearch.sink;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ElasticsearchSinkApplication {

	public static void main(String[] args) {
		SpringApplication.run(ElasticsearchSinkApplication.class, args);
	}
}
