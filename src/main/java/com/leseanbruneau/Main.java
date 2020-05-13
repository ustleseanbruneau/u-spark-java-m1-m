package com.leseanbruneau;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;

import com.google.common.collect.Iterables;

import scala.Tuple2;

public class Main {

	public static void main(String[] args) {
		
		List<String> inputData = new ArrayList<>();
		inputData.add("WARN:  Tuesday 4 September 0405");
		inputData.add("WARN:  Tuesday 4 September 0406");
		inputData.add("ERROR:  Tuesday 4 September 0408");
		inputData.add("FATAL:  Wednesday 5 September 1632");
		inputData.add("ERROR:  Friday 7 September 1854");
		inputData.add("WARN:  Saturday 8 September 1942");
		
		Logger.getLogger("org.apache").setLevel(Level.WARN);
		
		SparkConf conf = new SparkConf().setAppName("startingSpark").setMaster("local[*]");
		JavaSparkContext sc = new JavaSparkContext(conf);
		
		/*
		JavaRDD<String> originalLogMessages = sc.parallelize(inputData);
		
		//JavaPairRDD<String, String> pairRdd = originalLogMessages.mapToPair( rawValue -> {
		JavaPairRDD<String, Long> pairRdd = originalLogMessages.mapToPair( rawValue -> {
			
			String[] columns = rawValue.split(":");
			String level = columns[0];
			String date = columns[1];
			
			//return new Tuple2<String, String>(level, date);
			// JavePairRDD specifies types <String, String>, so can remove
			return new Tuple2<>(level, 1L);
			
		} );
		
		JavaPairRDD<String, Long> sumsRdd = pairRdd.reduceByKey( (value1, value2) -> value1 + value2);
		
		sumsRdd.foreach( tuple -> System.out.println(tuple._1 + " has " + tuple._2 + " instances"));
		*/
		
//		sc.parallelize(inputData)
//		  .mapToPair( rawValue -> new Tuple2<>(rawValue.split(":")[0] , 1L ))
//		  .reduceByKey((value1, value2) -> value1 + value2)
//		  .foreach(tuple -> System.out.println(tuple._1 + " has " + tuple._2 + " instances"));
		
		// groupbykey version
		
		sc.parallelize(inputData)
		  .mapToPair( rawValue -> new Tuple2<>(rawValue.split(":")[0] , 1L ))
		  .groupByKey()
		  .foreach( tuple -> System.out.println(tuple._1 + " has " + Iterables.size(tuple._2) + " instances "));
		
		// NOTE:
		// 
		//  Use reduceByKey whenever possible - better performance
		
		sc.close();

	}

}
