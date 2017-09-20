package io.pivotal.example.stream.elasticsearch.sink.service;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.StringTokenizer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ElasticsearchPersistenceService {

	@Autowired
	private EarthquakeRepository repository;	
	
	private DateFormat formatter = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss.SS");
	
	public void insert(Earthquake quake) {
		repository.save(quake);
	}

	/*
	 *       DateTime,Latitude,Longitude,Depth,Magnitude,MagType,NbStations,Gap,Distance,RMS,Source,EventID
	 */
	public Earthquake parse(String csv) throws ParseException{
		Earthquake quake = new Earthquake();
		
		StringTokenizer tokenizer = new StringTokenizer(csv, ",");
		
		quake.setTimestamp(formatter.parse(tokenizer.nextToken()));
		quake.setLatitude(Float.valueOf(tokenizer.nextToken()));
		quake.setLongitude(Float.valueOf(tokenizer.nextToken()));
		quake.setDepth(depth);
		quake.setMagnitude(magnitude);
		quake.set
		return quake;
	}
	
	
}
