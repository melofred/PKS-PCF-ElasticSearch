package io.pivotal.example.stream.elasticsearch.sink.service;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.StringTokenizer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class ElasticsearchPersistenceService {

	//@Autowired(required=false)
	//private EarthquakeRepository repository;	
	
	static ObjectMapper mapper = new ObjectMapper();
	static ElasticSearchClient client = new ElasticSearchClient();
	
	private DateFormat formatter = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss.SS");
	
	public void insert(Earthquake quake) {
		try {
			client.insert(mapper.writeValueAsString(quake));
		}
		catch(Exception e) {
			throw new RuntimeException(e);
		}
		//repository.save(quake);
		
	}

	/*
	 *       DateTime,Latitude,Longitude,Depth,Magnitude,MagType,NbStations,Gap,Distance,RMS,Source,EventID
	 */
	public Earthquake parse(String csv) throws ParseException{
		Earthquake quake = new Earthquake();
		
		StringTokenizer tokenizer = new StringTokenizer(csv, ",");
		
		if (tokenizer.countTokens()!=12) throw new ParseException("Bad formatted record. Missing data. Tokens:"+ tokenizer.countTokens(), 0);
		
		quake.setTimestamp(formatter.parse(tokenizer.nextToken()));
		quake.setLatitude(Float.valueOf(tokenizer.nextToken()));
		quake.setLongitude(Float.valueOf(tokenizer.nextToken()));
		quake.setDepth(Float.valueOf(tokenizer.nextToken()));
		quake.setMagnitude(Float.valueOf(tokenizer.nextToken()));
		quake.setMagType(tokenizer.nextToken());
		quake.setNBStations(Integer.valueOf(tokenizer.nextToken()));
		quake.setGap(Float.valueOf(tokenizer.nextToken()));
		quake.setDistance(Float.valueOf(tokenizer.nextToken()));
		quake.setRMS(Float.valueOf(tokenizer.nextToken()));
		quake.setSource(tokenizer.nextToken());
		quake.setEventId(tokenizer.nextToken());
		return quake;
	}
	
	
}
