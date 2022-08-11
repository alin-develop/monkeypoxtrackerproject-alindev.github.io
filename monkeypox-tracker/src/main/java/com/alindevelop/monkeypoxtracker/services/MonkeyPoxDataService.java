package com.alindevelop.monkeypoxtracker.services;

import java.io.IOException;
import java.io.StringReader;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.springframework.stereotype.Service;

import com.alindevelop.monkeypoxtracker.models.LocationStatistics;

import org.springframework.scheduling.annotation.Scheduled;

@Service
public class MonkeyPoxDataService {

	private String MONKEYPOX_DATA_URL="https://raw.githubusercontent.com/globaldothealth/monkeypox/main/timeseries-country-confirmed.csv";
	private List<LocationStatistics> locationStats = new ArrayList<>();
	
	@PostConstruct
	@Scheduled(cron = "* * 1 * * *")
	public void fetchData() throws IOException, InterruptedException {
		List<LocationStatistics> locationStats = new ArrayList<>();
		
		HttpClient httpClient = HttpClient.newHttpClient();
		HttpRequest httpRequest = HttpRequest.newBuilder().uri(URI.create(MONKEYPOX_DATA_URL)).build();
		
		HttpResponse<String> httpResponse = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());
		
		StringReader csvBodyReader = new StringReader(httpResponse.body());
		
		Iterable<CSVRecord> records = CSVFormat.RFC4180.builder().setHeader()
				.setSkipHeaderRecord(true).build().parse(csvBodyReader);
		for (CSVRecord record : records) {
			LocationStatistics locationStat = new LocationStatistics(
					LocalDate.parse(record.get("Date")),
					Integer.parseInt(record.get("Cases")),
					Integer.parseInt(record.get("Cumulative_cases")),
					record.get("Country")
			);
			locationStats.add(locationStat);
		}
		this.locationStats = locationStats;
	}
	
	public List<LocationStatistics> getLatestLocationStatsByTotalCases(){
		LocalDate previousDay = LocalDate.now().minusDays(1);
		return locationStats.stream().filter(el -> el.getDate().equals(previousDay))
				.sorted(Comparator.comparingInt(LocationStatistics::getTotalCases).reversed())
				.collect(Collectors.toList());
	}
	
	public List<LocationStatistics> getLatestLocationStatsByDailyNewCases(){
		LocalDate previousDay = LocalDate.now().minusDays(1);
		return locationStats.stream().filter(el -> el.getDate().equals(previousDay))
				.sorted(Comparator.comparingInt(LocationStatistics::getDailyNewCases).reversed())
				.collect(Collectors.toList());
	}
	
	public List<LocationStatistics> getLatestLocationStats(){
		LocalDate previousDay = LocalDate.now().minusDays(1);
		
		return locationStats.stream().filter(el -> el.getDate().equals(previousDay))
				.sorted((a,b)->a.getCountry().compareToIgnoreCase(b.getCountry()))
				.collect(Collectors.toList());
	}

	public List<LocationStatistics> getLocationStats() {
		return locationStats;
	}

	public int getTotalReportedCases() {
		
		return locationStats.stream().collect(Collectors.groupingBy(LocationStatistics::getCountry,
				Collectors.maxBy(Comparator.comparing(LocationStatistics::getDate))))
		.values().stream().mapToInt(el -> el.get().getTotalCases()).sum();
		
		
	}
	
}
