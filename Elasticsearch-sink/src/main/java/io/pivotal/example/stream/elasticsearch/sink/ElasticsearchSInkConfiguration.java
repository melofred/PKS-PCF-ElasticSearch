package io.pivotal.example.stream.elasticsearch.sink;

import java.text.ParseException;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.cloud.stream.messaging.Sink;
import org.springframework.messaging.Message;

import io.pivotal.example.stream.elasticsearch.sink.service.ElasticsearchPersistenceService;

@EnableBinding(Sink.class)
@EnableConfigurationProperties(ElasticsearchSinkProperties.class)
public class ElasticsearchSInkConfiguration {


	@Autowired
	private ElasticsearchSinkProperties properties;
	
	@Autowired
	private ElasticsearchPersistenceService svc;

	private Logger logger = Logger.getLogger("ElasticSearchSink");
	
	@StreamListener(Sink.INPUT)
	/*
	public void handle(Earthquake quake) {
		svc.insert(quake);
	}
	*/

	public void onMessage(Message<?> message) {
		
		String payload = message.getPayload().toString();
		try {
			svc.insert(svc.parse(payload));
		}
		catch(ParseException pe) {
			logger.warning("Ignoring line with data missing: "+payload);
		}
		 
		
		
	}
	
	
	
	
	
}
