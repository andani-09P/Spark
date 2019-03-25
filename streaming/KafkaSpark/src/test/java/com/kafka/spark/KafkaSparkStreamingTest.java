package com.kafka.spark;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.api.java.function.VoidFunction;
import org.apache.spark.streaming.Durations;
import org.apache.spark.streaming.api.java.JavaDStream;
import org.apache.spark.streaming.api.java.JavaInputDStream;
import org.apache.spark.streaming.api.java.JavaStreamingContext;
import org.apache.spark.streaming.kafka010.ConsumerStrategies;
import org.apache.spark.streaming.kafka010.HasOffsetRanges;
import org.apache.spark.streaming.kafka010.KafkaTestUtils;
import org.apache.spark.streaming.kafka010.KafkaUtils;
import org.apache.spark.streaming.kafka010.LocationStrategies;
import org.apache.spark.streaming.kafka010.OffsetRange;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class KafkaSparkStreamingTest implements Serializable {

	private static final long serialVersionUID = 1L;
	private transient JavaStreamingContext ssc = null;
	private transient KafkaTestUtils kafkaTestUtils = null;

	@Before
	public void setUp() {
		kafkaTestUtils = new KafkaTestUtils();
		kafkaTestUtils.setup();
		SparkConf sparkConf = new SparkConf().setMaster("local[2]").setAppName(this.getClass().getSimpleName());
		ssc = new JavaStreamingContext(sparkConf, Durations.milliseconds(20));
	}

	@After
	public void tearDown() {
		if (ssc != null) {
			ssc.stop();
			ssc = null;
		}

		if (kafkaTestUtils != null) {
			kafkaTestUtils.teardown();
			kafkaTestUtils = null;
		}
	}

	@Test
	public void testKafkaStream() throws InterruptedException {

		final String topic1 = "topicA";
		// hold a reference to the current offset ranges, so it can be used downstream
		final AtomicReference<OffsetRange[]> offsetRanges = new AtomicReference<>();

		String[] topic1data = createTopicAndSendData(topic1);

		Set<String> sent = new HashSet<>();
		sent.addAll(Arrays.asList(topic1data));

		Random random = new Random();

		final Map<String, Object> kafkaParams = new HashMap<>();
		kafkaParams.put("bootstrap.servers", kafkaTestUtils.brokerAddress());
		kafkaParams.put("key.deserializer", StringDeserializer.class);
		kafkaParams.put("value.deserializer", StringDeserializer.class);
		kafkaParams.put("auto.offset.reset", "earliest");
		kafkaParams.put("group.id", "java-test-consumer-" + random.nextInt() + "-" + System.currentTimeMillis());

		JavaInputDStream<ConsumerRecord<String, String>> istream = KafkaUtils.createDirectStream(ssc,
				LocationStrategies.PreferConsistent(),
				ConsumerStrategies.<String, String>Subscribe(Arrays.asList(topic1), kafkaParams));

		JavaDStream<String> stream = istream.transform(
				// Make sure you can get offset ranges from the rdd
				new Function<JavaRDD<ConsumerRecord<String, String>>, JavaRDD<ConsumerRecord<String, String>>>() {
					@Override
					public JavaRDD<ConsumerRecord<String, String>> call(JavaRDD<ConsumerRecord<String, String>> rdd) {
						OffsetRange[] offsets = ((HasOffsetRanges) rdd.rdd()).offsetRanges();
						offsetRanges.set(offsets);
						Assert.assertEquals(topic1, offsets[0].topic());
						return rdd;
					}
				}).map(new Function<ConsumerRecord<String, String>, String>() {
					@Override
					public String call(ConsumerRecord<String, String> r) {
						return r.value();
					}
				});
//JavaDStream<String> unifiedStream = stream;

		final Set<String> result = Collections.synchronizedSet(new HashSet<String>());
		stream.foreachRDD(new VoidFunction<JavaRDD<String>>() {
			@Override
			public void call(JavaRDD<String> rdd) {
				result.addAll(rdd.collect());
			}
		});
		ssc.start();
		long startTime = System.currentTimeMillis();
		boolean matches = false;
		while (!matches && System.currentTimeMillis() - startTime < 20000) {
			matches = sent.size() == result.size();
			Thread.sleep(50);
		}
		Assert.assertEquals(sent, result);
		ssc.stop();
	}

	private String[] createTopicAndSendData(String topic) {
		String[] data = { topic + "-1", topic + "-2", topic + "-3" };
		kafkaTestUtils.createTopic(topic);
		kafkaTestUtils.sendMessages(topic, data);
		return data;
	}
}