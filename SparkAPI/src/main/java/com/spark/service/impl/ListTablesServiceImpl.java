package com.spark.service.impl;

import java.util.Arrays;
import java.util.List;

import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Encoders;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.spark.service.ListTablesService;

@Service
public class ListTablesServiceImpl implements ListTablesService {

	@Autowired
    private SparkSession sparkSession;
	
	@Override
	public Object getTableNames() {
		// TODO Auto-generated method stub
		
		
		

		/*
		 * Dataset<Row> df = sparkSession.read() .option("header", "true")
		 * .option("delimiter",",") .csv("/home/andy/Desktop/test.csv");
		 */

		/*
		 * df.show();
		 */
	        List<String> myList = Arrays.asList("one", "two", "three", "four", "five");
	        Dataset<Row> df = sparkSession.createDataset(myList, Encoders.STRING()).toDF();
	        df.show();
	        //using df.as
	        List<String> listOne = df.as(Encoders.STRING()).collectAsList();
	        System.out.println(listOne);
	        //using df.map
	       // List<String> listTwo = df.map(row -> row.mkString(), Encoders.STRING()).collectAsList();
	       // System.out.println(listTwo);
	        
	        
		return listOne;
	}

}
