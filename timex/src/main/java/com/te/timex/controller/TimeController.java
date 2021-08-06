
package com.te.timex.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
@RequestMapping("/time")
public class TimeController{

	
	@GetMapping
	public String index() {
		return "time/time";
	}
	

}