package com.spark.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.spark.service.ListTablesService;

@RestController
public class SparkController {
	
	@Autowired
	ListTablesService listTableService;
	
	@RequestMapping("/get")
	public Object get() {		
		
		return listTableService.getTableNames();
		
	}

}
