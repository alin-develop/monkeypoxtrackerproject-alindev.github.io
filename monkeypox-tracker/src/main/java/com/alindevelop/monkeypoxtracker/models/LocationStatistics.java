package com.alindevelop.monkeypoxtracker.models;

import java.time.LocalDate;

public class LocationStatistics {
	
	private final LocalDate date;
	private final int dailyNewCases;
	private final int totalCases;
	private final String country;

	@Override
	public String toString() {
		return "LocationStatistics [date=" + date + ", dailyNewCases=" + dailyNewCases + ", totalCases=" + totalCases
				+ ", country=" + country + "]";
	}
	public LocationStatistics(LocalDate date, int dailyNewCases, int totalCases, String country) {
		this.date = date;
		this.dailyNewCases = dailyNewCases;
		this.totalCases = totalCases;
		this.country = country;
	}
	public LocalDate getDate() {
		return date;
	}
	public int getDailyNewCases() {
		return dailyNewCases;
	}
	public int getTotalCases() {
		return totalCases;
	}
	public String getCountry() {
		return country;
	}
	
}
