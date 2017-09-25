package io.pivotal.example.stream.elasticsearch.sink.service;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
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
	
	private DateFormat formatterIn = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss.SS");
	private DateFormat formatterOut = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SS");
	
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
		
		// "post_date" : "2009-11-15T14:12:12",
		//quake.setTimestamp(formatter.parse(tokenizer.nextToken()));
		quake.setTimestamp(formatterOut.format(formatterIn.parse(tokenizer.nextToken())));
		//quake.setLatitude(Float.valueOf(tokenizer.nextToken()));
		//quake.setLongitude(Float.valueOf(tokenizer.nextToken()));
		
		Float latitude=Float.valueOf(tokenizer.nextToken());
		Float longitude=Float.valueOf(tokenizer.nextToken());
		quake.setLocation(latitude+", "+longitude);
		
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
	
	public void createEarthquakesIndex() {
		try {
			client.createIndex();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}
