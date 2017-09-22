package io.pivotal.example.stream.elasticsearch.sink.service;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Hashtable;

import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.entity.StringEntity;
import org.elasticsearch.client.Response;
import org.elasticsearch.client.RestClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ElasticSearchClient {

    @Value("${elasticsearch.host}")
    private String esHost;

    @Value("${elasticsearch.port}")
    private int esPort;

    @Value("${elasticsearch.clustername}")
    private String EsClusterName;
	
	
	RestClient client;
	
	
	public ElasticSearchClient() {
		
		if (esHost==null) {
			esHost="35.196.187.65";
			esPort=9200;
		}
		client = RestClient
	            .builder(new HttpHost(esHost, esPort))
	            .build();
	            
	}
	
	public void insert(String json) throws UnsupportedEncodingException, IOException {
		
		HttpEntity requestBody  = new StringEntity(json);
		Response response = client.performRequest(
		        "POST",
		        "/earthquakes/earthquakes",
		        new Hashtable<>(),
		        requestBody);

		
	}
	
	
}
