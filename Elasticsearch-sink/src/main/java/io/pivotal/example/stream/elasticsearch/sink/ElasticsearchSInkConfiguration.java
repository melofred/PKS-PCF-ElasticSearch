package io.pivotal.example.stream.elasticsearch.sink;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.cloud.stream.messaging.Sink;
import org.springframework.messaging.Message;

import io.pivotal.example.stream.elasticsearch.sink.service.Earthquake;
import io.pivotal.example.stream.elasticsearch.sink.service.ElasticsearchPersistenceService;

@EnableBinding(Sink.class)
@EnableConfigurationProperties(ElasticsearchSinkProperties.class)
public class ElasticsearchSInkConfiguration {


	@Autowired
	private ElasticsearchSinkProperties properties;
	
	@Autowired
	private ElasticsearchPersistenceService svc;

	@StreamListener(Sink.INPUT)
	/*
	public void handle(Earthquake quake) {
		svc.insert(quake);
	}
	*/

	public void onMessage(Message<?> message) {
		
		String payload = message.getPayload().toString();
		svc.insert(svc.parse(payload));
		 
		
		
	}
	
	
	
	
	
}
