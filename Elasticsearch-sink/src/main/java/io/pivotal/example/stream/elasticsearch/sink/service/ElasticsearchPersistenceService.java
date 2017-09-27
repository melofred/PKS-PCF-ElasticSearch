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
	
	private ElasticSearchClient client;
	
	private DateFormat formatterIn = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss.SS");
	private DateFormat formatterOut = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SS");
	
	public boolean isConfigured() {
		return client!=null;
	}
	
	public void config(String url) {
		client = new ElasticSearchClient(url);
	}
	
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
		
		//StringTokenizer tokenizer = new StringTokenizer(csv, ",");
		
		String[] tokens = csv.split(",", -1);
		
		String timestamp = tokens[0];
		String latitude = tokens[1];
		String longitude = tokens[2];
		String depth = tokens[3];
		String mag = tokens[4];
		String magType = tokens[5];
		String stations = tokens[6];
		String gap = tokens[7];
		String dmin = tokens[8];
		String rms = tokens[9];
		String source = tokens[10];
		String eventId = tokens[11];
		
		if (timestamp.isEmpty() || latitude.isEmpty() || longitude.isEmpty()) {
			throw new ParseException("Bad formatted record. Missing data. ",0);
		}
		
		//if (tokenizer.countTokens()!=12) throw new ParseException("Bad formatted record. Missing data. Tokens:"+ tokenizer.countTokens(), 0);
		
		// "post_date" : "2009-11-15T14:12:12",
		//quake.setTimestamp(formatter.parse(tokenizer.nextToken()));
		quake.setTimestamp(formatterOut.format(formatterIn.parse(timestamp)));
		//quake.setLatitude(Float.valueOf(tokenizer.nextToken()));
		//quake.setLongitude(Float.valueOf(tokenizer.nextToken()));
		
		quake.setLocation(latitude+", "+longitude);
		
		quake.setDepth(depth.isEmpty()?0:Float.valueOf(depth));
		quake.setMagnitude(mag.isEmpty()?0:Float.valueOf(mag));
		quake.setMagType(magType);
		quake.setNBStations(stations.isEmpty()?0:Integer.valueOf(stations));
		quake.setGap(gap.isEmpty()?0:Float.valueOf(gap));
		quake.setDistance(dmin.isEmpty()?0:Float.valueOf(dmin));
		quake.setRMS(rms.isEmpty()?0:Float.valueOf(rms));
		quake.setSource(source);
		quake.setEventId(eventId);
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
