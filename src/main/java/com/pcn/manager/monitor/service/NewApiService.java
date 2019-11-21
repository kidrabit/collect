package com.pcn.manager.monitor.service;

import java.io.IOException;
import java.time.LocalDate;

import org.springframework.stereotype.Service;

import org.apache.http.HttpHost;
import org.apache.http.client.config.RequestConfig.Builder;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.aggregations.AggregationBuilder;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.histogram.DateHistogramInterval;
import org.elasticsearch.search.aggregations.bucket.histogram.Histogram;
import org.elasticsearch.search.builder.SearchSourceBuilder;

/**
 * @class NewApiService
 * 	com.pcn.manager.monitor.service
 * @section 클래스작성정보
 *    |    항  목        	|       	내  용       			|
 *    | :--------: 	| -----------------------------	|
 *    | Company 	| PCN
 *    | Author 		| rnd
 *    | Date 		| 2019. 10. 8.
 *    | 작업자 		| rnd, Others...
 * @section 상세설명
 * - 클래스의 업무내용에 대해 기술...
*/
@Service
public class NewApiService {

    /*LocalDate curDate = LocalDate.now();

    String currentTime = curDate.toString().replace("-", ".");
    String todayIndex = ".monitoring-logstash-7-" + currentTime;
    String indexName = ".monitoring-logstash-7-";*/

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
        NewApiService.interval = interval;
    }

    public RestHighLevelClient getRestHighLevelClient() {
        RestClientBuilder restClientBuilder = RestClient.builder(new HttpHost("222.122.196.182", 9200, "http"));
        restClientBuilder = restClientBuilder.setRequestConfigCallback(new RestClientBuilder.RequestConfigCallback() {
            @Override
            public Builder customizeRequestConfig(Builder requestConfigBuilder) {
                return requestConfigBuilder.setConnectTimeout(3000).setSocketTimeout(40000);
            }
        });

        RestHighLevelClient rc = new RestHighLevelClient(restClientBuilder);

        return rc;
    }

    public Histogram callApi(String type, String fromDate, String toDate) {
        RestHighLevelClient client = getRestHighLevelClient();
        LocalDate curDate = LocalDate.now();

        String currentTime = curDate.toString().replace("-", ".");
        String todayIndex = ".monitoring-logstash-7-" + currentTime;
        String indexName = ".monitoring-logstash-7-";

        SearchRequest request = new SearchRequest(todayIndex);

        if (!fromDate.contains("now")) {
            request = new SearchRequest(indexName + fromDate.substring(0, 10).replace("-", "."));

        }
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();

        AggregationBuilder aggBuilder = getType(type);

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

    public AggregationBuilder getType(String type) {
        AggregationBuilder aggBuilder = null;
        if (type == "jvm") {
            aggBuilder = AggregationBuilders.dateHistogram("counts_over_time").field("timestamp").dateHistogramInterval(interval)
                .subAggregation(AggregationBuilders.max("avg_heap_max_in_bytes").field("logstash_stats.jvm.mem.heap_max_in_bytes")).subAggregation(
                    AggregationBuilders.max("avg_heap_used_in_bytes").field("logstash_stats.jvm.mem.heap_used_in_bytes"));
        } else if (type == "cpu") {
            aggBuilder = AggregationBuilders.dateHistogram("counts_over_time").field("timestamp").dateHistogramInterval(interval)
                .subAggregation(AggregationBuilders.max("avg_cpu_percent").field("logstash_stats.process.cpu.percent"));
        } else if (type == "load") {
            aggBuilder = AggregationBuilders.dateHistogram("counts_over_time").field("timestamp").dateHistogramInterval(interval)
                .subAggregation(AggregationBuilders.max("avg_load_data_1m").field("logstash_stats.os.cpu.load_average.1m")).subAggregation(
                    AggregationBuilders.max("avg_load_data_5m").field("logstash_stats.os.cpu.load_average.5m")).subAggregation(AggregationBuilders
                        .max("avg_load_data_15m").field("logstash_stats.os.cpu.load_average.15m"));
        }
        return aggBuilder;
    }

    public Histogram eventType(String avgName, String type, String fromDate, String toDate) {
        RestHighLevelClient client = getRestHighLevelClient();

        LocalDate curDate = LocalDate.now();

        String currentTime = curDate.toString().replace("-", ".");
        String todayIndex = ".monitoring-logstash-7-" + currentTime;
        String indexName = ".monitoring-logstash-7-";

        SearchRequest request = new SearchRequest(todayIndex);
        if (!fromDate.contains("now")) {
            request = new SearchRequest(indexName + fromDate.substring(0, 10).replace("-", "."));
        }

        //SearchRequest request = new SearchRequest(indexName + fromDate.substring(0, 10).replace("-", "."));
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();

        AggregationBuilder aggBuilder = AggregationBuilders.dateHistogram("counts_over_time").field("timestamp").dateHistogramInterval(interval)
            .subAggregation(AggregationBuilders.max("avg_event_" + avgName + "_data").field("logstash_stats.events." + type));

        searchSourceBuilder.query(QueryBuilders.boolQuery().filter(QueryBuilders.rangeQuery("timestamp").from(fromDate).to(toDate)));

        /*
         *
         * */

        SearchResponse response = null;
        searchSourceBuilder.aggregation(aggBuilder);

        searchSourceBuilder.size(0);
        request.source(searchSourceBuilder);
        Histogram dateHistogram = null;

        try {
            response = client.search(request, RequestOptions.DEFAULT);
            dateHistogram = response.getAggregations().get("counts_over_time");

        } catch (Exception e) {
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

    public Histogram eventLatency(String fromDate, String toDate) {
        return eventType("latency", "duration_in_millis", fromDate, toDate);
    }

    public Histogram eventReceived(String fromDate, String toDate) {
        return eventType("received", "in", fromDate, toDate);
    }

    public Histogram eventEmitted(String fromDate, String toDate) {
        return eventType("emitted", "out", fromDate, toDate);

    }

    public void queryTest() {
        RestHighLevelClient client = getRestHighLevelClient();

        LocalDate curDate = LocalDate.now();

        String currentTime = curDate.toString().replace("-", ".");
        String todayIndex = ".monitoring-logstash-7-" + currentTime;
        String indexName = ".monitoring-logstash-7-";

        SearchRequest request = new SearchRequest(indexName);
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();

        AggregationBuilder aggBuilder = AggregationBuilders.dateHistogram("counts_over_time").field("timestamp").dateHistogramInterval(interval)
            .subAggregation(AggregationBuilders.max("events_in_max").field("logstash_stats.events.in"));

        //searchSourceBuilder.query(QueryBuilders.boolQuery().filter(QueryBuilders.rangeQuery("timestamp").from(fromDate).to(toDate)));

        /*
         *
         * */

        SearchResponse response = null;
        searchSourceBuilder.aggregation(aggBuilder);

        searchSourceBuilder.size(0);
        request.source(searchSourceBuilder);
        Histogram dateHistogram = null;

        try {
            response = client.search(request, RequestOptions.DEFAULT);
            dateHistogram = response.getAggregations().get("counts_over_time");

        } catch (Exception e) {
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

    }

    public SearchHit[] callKWeather(String fromDate, String toDate, int size) {
        RestHighLevelClient client = getRestHighLevelClient();
        SearchRequest request = new SearchRequest("kweather_data");
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        if (toDate.equals("now")) {
        } else {
            searchSourceBuilder.query(QueryBuilders.rangeQuery("Tm").from(fromDate).to(toDate));
        }
        searchSourceBuilder.size(size);
        request.source(searchSourceBuilder);
        SearchResponse response = null;
        SearchHits hits = null;
        try {
            response = client.search(request, RequestOptions.DEFAULT);
            hits = response.getHits();

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
        return hits.getHits();
    }

    public SearchHit[] callPmDatas(String fromDate, String toDate, int size) {
        RestHighLevelClient client = getRestHighLevelClient();
        SearchRequest request = new SearchRequest("kweather_data");
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.fetchSource(new String[] {"Tm", "Serial", "Flag", "Pm10", "Pm25"}, null);
        if (toDate.equals("now")) {
        } else {
            searchSourceBuilder.query(QueryBuilders.rangeQuery("Tm").from(fromDate).to(toDate));
        }
        searchSourceBuilder.size(size);
        request.source(searchSourceBuilder);
        SearchResponse response = null;
        SearchHits hits = null;
        try {
            response = client.search(request, RequestOptions.DEFAULT);
            hits = response.getHits();

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
        return hits.getHits();
    }

    public SearchHit[] callNoiseDatas(String fromDate, String toDate, int size) {
        RestHighLevelClient client = getRestHighLevelClient();
        SearchRequest request = new SearchRequest("kweather_data");
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.fetchSource(new String[] {"Tm", "Serial", "Flag", "Noise"}, null);
        if (toDate.equals("now")) {
        } else {
            searchSourceBuilder.query(QueryBuilders.rangeQuery("Tm").from(fromDate).to(toDate));
        }
        searchSourceBuilder.size(size);
        request.source(searchSourceBuilder);
        SearchResponse response = null;
        SearchHits hits = null;
        try {
            response = client.search(request, RequestOptions.DEFAULT);
            hits = response.getHits();

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
        return hits.getHits();
    }

    public SearchHit[] callTempDatas(String fromDate, String toDate, int size) {
        RestHighLevelClient client = getRestHighLevelClient();
        SearchRequest request = new SearchRequest("kweather_data");
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.fetchSource(new String[] {"Tm", "Serial", "Flag", "Temp", "Humi"}, null);
        if (toDate.equals("now")) {
        } else {
            searchSourceBuilder.query(QueryBuilders.rangeQuery("Tm").from(fromDate).to(toDate));
        }
        searchSourceBuilder.size(size);
        request.source(searchSourceBuilder);
        SearchResponse response = null;
        SearchHits hits = null;
        try {
            response = client.search(request, RequestOptions.DEFAULT);
            hits = response.getHits();

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
        return hits.getHits();
    }
}
