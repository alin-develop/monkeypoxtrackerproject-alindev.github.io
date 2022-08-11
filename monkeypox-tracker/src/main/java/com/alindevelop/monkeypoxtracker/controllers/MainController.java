package com.alindevelop.monkeypoxtracker.controllers;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.alindevelop.monkeypoxtracker.services.MonkeyPoxDataService;

@Controller
public class MainController {

	private final MonkeyPoxDataService monkeyPoxDataService;

	@Autowired
	public MainController(MonkeyPoxDataService monkeyPoxDataService) {
		this.monkeyPoxDataService = monkeyPoxDataService;
	}
	
	@GetMapping("/main")
	public String main(@RequestParam(name = "sort-field") String sortField, Model model) {

		if(sortField.equalsIgnoreCase("dailycases")) {
			model.addAttribute("latestLocationStats", monkeyPoxDataService.getLatestLocationStatsByDailyNewCases());
		}else if(sortField.equalsIgnoreCase("totalcases")) {
			model.addAttribute("latestLocationStats", monkeyPoxDataService.getLatestLocationStatsByTotalCases());
		}else {
			model.addAttribute("latestLocationStats", monkeyPoxDataService.getLatestLocationStats());
		}
		model.addAttribute("totalReportedCases", monkeyPoxDataService.getTotalReportedCases());
		
		return "main";
	}
	
	@GetMapping(value = "/")
    public String viewIndexPage() {
        return "redirect:main?sort-field=country";
    }
	
	
	
}
