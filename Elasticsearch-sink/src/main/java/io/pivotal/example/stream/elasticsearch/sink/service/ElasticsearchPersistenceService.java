package io.pivotal.example.stream.elasticsearch.sink.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ElasticsearchPersistenceService {

	@Autowired
	private EarthquakeRepository repository;	
	
	public void insert(Earthquake quake) {
		repository.save(quake);
	}
	
	
}
