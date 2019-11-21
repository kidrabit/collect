package com.pcn.manager.monitor.service;

import java.io.IOException;

import org.springframework.stereotype.Service;

import org.apache.http.HttpHost;
import org.apache.http.client.config.RequestConfig.Builder;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.aggregations.AggregationBuilder;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.histogram.DateHistogramInterval;
import org.elasticsearch.search.aggregations.bucket.histogram.Histogram;
import org.elasticsearch.search.builder.SearchSourceBuilder;

/**
 * @class ApiCallService
 * 	com.pcn.manager.monitor.service
 * @section 클래스작성정보
 *    |    항  목        	|       	내  용       			|
 *    | :--------: 	| -----------------------------	|
 *    | Company 	| PCN
 *    | Author 		| rnd
 *    | Date 		| 2019. 8. 28.
 *    | 작업자 		| rnd, Others...
 * @section 상세설명
 * - 클래스의 업무내용에 대해 기술...
*/
@Service
public class ApiCallService {

    String indexName = ".monitoring-logstash-7-*";

    private static DateHistogramInterval interval = DateHistogramInterval.minutes(10);

    /**
     * @fn DateHistogramInterval getInterval()
     * @return the interval get
    */
    public static DateHistogramInterval getInterval() {
        return interval;
    }

    /**
     * @fn void setInterval(DateHistogramInterval interval)
     * @param interval the interval to set
    */
    public static void setInterval(DateHistogramInterval interval) {
        ApiCallService.interval = interval;
    }

    public Histogram callApi(String type, String fromDate, String toDate) {
        RestHighLevelClient client = new RestHighLevelClient(this.getRestClientBuilder());
        SearchRequest request = new SearchRequest(indexName);
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();

        AggregationBuilder aggBuilder = null;

        if ("NONE".equalsIgnoreCase(type)) {
            aggBuilder = getNoneAggBuilder();
        } else if ("EVENT".equalsIgnoreCase(type)) {
            aggBuilder = getEventAggBuilder();
        }

        searchSourceBuilder.size(0);
        searchSourceBuilder.aggregation(aggBuilder);
        searchSourceBuilder.query(QueryBuilders.boolQuery().filter(QueryBuilders.rangeQuery("timestamp").from(fromDate).to(toDate)));
        request.source(searchSourceBuilder);
        SearchResponse response = null;
        Histogram dateHistogram = null;
        try {
            response = client.search(request, RequestOptions.DEFAULT);
            dateHistogram = response.getAggregations().get("counts_over_time");

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (client != null) {
                try {
                    client.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return dateHistogram;
    }

    private AggregationBuilder getNoneAggBuilder() {

        AggregationBuilder aggBuilder = null;

        aggBuilder = AggregationBuilders.dateHistogram("counts_over_time").field("timestamp").dateHistogramInterval(interval);
        //JVM Heap
        aggBuilder.subAggregation(AggregationBuilders.max("avg_heap_max_in_bytes").field("logstash_stats.jvm.mem.heap_max_in_bytes"))
            .subAggregation(AggregationBuilders.max("avg_heap_used_in_bytes").field("logstash_stats.jvm.mem.heap_used_in_bytes"));
        //CPU Utilization
        aggBuilder.subAggregation(AggregationBuilders.max("avg_cpu_percent").field("logstash_stats.process.cpu.percent"));
        //System Load
        aggBuilder.subAggregation(AggregationBuilders.max("avg_load_data_1m").field("logstash_stats.os.cpu.load_average.1m"))
            .subAggregation(AggregationBuilders.max("avg_load_data_5m").field("logstash_stats.os.cpu.load_average.5m"))
            .subAggregation(AggregationBuilders.max("avg_load_data_15m").field("logstash_stats.os.cpu.load_average.15m"));

        return aggBuilder;
    }

    private AggregationBuilder getEventAggBuilder() {

        AggregationBuilder aggBuilder = null;

        aggBuilder = AggregationBuilders.dateHistogram("counts_over_time").field("timestamp").dateHistogramInterval(interval);
        //Events Received Rate
        aggBuilder.subAggregation(AggregationBuilders.max("avg_event_received_data").field("logstash_stats.events.in"));
        //Events Emitted Rate
        aggBuilder.subAggregation(AggregationBuilders.max("avg_event_emitted_data").field("logstash_stats.events.out"));
        //Event Latency
        aggBuilder.subAggregation(AggregationBuilders.max("avg_event_latency_out").field("logstash_stats.events.out"))
            .subAggregation(AggregationBuilders.max("avg_event_latency_duration").field("logstash_stats.events.duration_in_millis"));

        return aggBuilder;
    }

    public SearchHits dashBoard() {

        RestHighLevelClient client = new RestHighLevelClient(this.getRestClientBuilder());
        SearchRequest request = new SearchRequest(indexName);
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();

        String[] source = new String[4];
        source[0] = "timestamp";
        source[1] = "logstash_stats.events";
        source[2] = "logstash_stats.jvm.mem";
        source[3] = "logstash_state.pipeline.representation.graph.vertices";

        searchSourceBuilder.fetchSource(source, null);
        BoolQueryBuilder builder = QueryBuilders.boolQuery().filter(QueryBuilders.rangeQuery("timestamp").from("now-20s/s").to("now"));

        searchSourceBuilder.query(builder);
        searchSourceBuilder.from(0);
        searchSourceBuilder.size(2);
        request.source(searchSourceBuilder);
        SearchResponse response = null;
        SearchHits searchHits = null;
        try {
            response = client.search(request, RequestOptions.DEFAULT);
            searchHits = response.getHits();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (client != null) {
                try {
                    client.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return searchHits;
    }
    
    private RestClientBuilder getRestClientBuilder() {
    	
    	RestClientBuilder restClientBuilder = RestClient.builder(new HttpHost("222.122.196.182", 9200, "http"));
    	
    	restClientBuilder = restClientBuilder.setRequestConfigCallback(new RestClientBuilder.RequestConfigCallback() {
			@Override
			public Builder customizeRequestConfig(Builder requestConfigBuilder) {
				// TODO Auto-generated method stub
				//connect timeout(defaults to 1 second), socket timeout(defaults to 30 seconds)
				return requestConfigBuilder.setConnectTimeout(3000).setSocketTimeout(40000);
			}
		});
    	
    	return restClientBuilder;
    }

}
