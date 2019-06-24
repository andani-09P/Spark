package com.spark.controller;

import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;

public class TestCode {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

        SparkSession spark = SparkSession.builder()
                .master("local")
                .appName("meowingful")
                .getOrCreate();

        Dataset<Row> df = spark.read()
                    .option("header", "true")
                    .option("delimiter",",")
                    .csv("/home/andy/Desktop/test.csv");

        df.show();

	}

}
