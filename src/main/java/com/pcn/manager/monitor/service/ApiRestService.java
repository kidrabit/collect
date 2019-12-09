package com.pcn.manager.monitor.service;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Map;
import java.util.TimeZone;

import org.springframework.stereotype.Service;

import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.aggregations.bucket.histogram.Histogram;
import org.elasticsearch.search.aggregations.metrics.Max;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

@Service
public class ApiRestService {

    public JsonArray getJvmHeapData(Histogram dateHistogram) {

        JsonObject jsonObj = null;
        JsonArray jsonArray = new JsonArray();

        for (Histogram.Bucket bucket : dateHistogram.getBuckets()) {
            Max heap_max = bucket.getAggregations().get("avg_heap_max_in_bytes");
            Max heap_used = bucket.getAggregations().get("avg_heap_used_in_bytes");

            jsonObj = new JsonObject();

            jsonObj.addProperty("timestamp", utcToLocaltime(bucket.getKeyAsString()));
            jsonObj.addProperty("heap_max_in_bytes", this.getChangeUnit(String.valueOf(heap_max.getValue()), "GB"));
            jsonObj.addProperty("heap_used_in_bytes", this.getChangeUnit(String.valueOf(heap_used.getValue()), "GB"));
            jsonArray.add(jsonObj);
        }

        return jsonArray;
    }

    public JsonArray getCpuUtilizationData(Histogram dateHistogram) {

        JsonObject jsonObj = null;
        JsonArray jsonArray = new JsonArray();
        String val = "";

        for (Histogram.Bucket bucket : dateHistogram.getBuckets()) {
            Max cpu_avg = bucket.getAggregations().get("avg_cpu_percent");
            jsonObj = new JsonObject();

            jsonObj.addProperty("timestamp", utcToLocaltime(bucket.getKeyAsString()));

            val = this.valueChk(String.valueOf(cpu_avg.getValue()));
            jsonObj.addProperty("percent", val);
            jsonArray.add(jsonObj);
        }
        return jsonArray;
    }

    public JsonArray getSystemLoadData(Histogram dateHistogram) {
        JsonObject jsonObj = null;
        JsonArray jsonArray = new JsonArray();

        for (Histogram.Bucket bucket : dateHistogram.getBuckets()) {
            Max one_min = bucket.getAggregations().get("avg_load_data_1m");
            Max five_min = bucket.getAggregations().get("avg_load_data_5m");
            Max fifteen_min = bucket.getAggregations().get("avg_load_data_15m");

            jsonObj = new JsonObject();

            jsonObj.addProperty("timestamp", utcToLocaltime(bucket.getKeyAsString()));
            jsonObj.addProperty("one_min", this.getRound(String.valueOf(one_min.getValue()), 100));
            jsonObj.addProperty("five_min", this.getRound(String.valueOf(five_min.getValue()), 100));
            jsonObj.addProperty("fifteen_min", this.getRound(String.valueOf(fifteen_min.getValue()), 100));

            jsonArray.add(jsonObj);
        }
        return jsonArray;
    }

    public JsonArray getEventsReceivedData(Histogram searchAgg) {
        JsonObject jsonObj = null;
        JsonArray jsonArray = new JsonArray();
        for (Histogram.Bucket bucket : searchAgg.getBuckets()) {
            Max avg = bucket.getAggregations().get("avg_event_received_data");

            jsonObj = new JsonObject();

            jsonObj.addProperty("timestamp", utcToLocaltime(bucket.getKeyAsString()));
            jsonObj.addProperty("events_out", avg.getValue());

            jsonArray.add(jsonObj);
        }
        return convert(jsonArray, "events_out");
    }

    public JsonArray getEventsEmittedRateData(Histogram searchAgg) {
        JsonObject jsonObj = null;
        JsonArray jsonArray = new JsonArray();
        for (Histogram.Bucket bucket : searchAgg.getBuckets()) {
            Max avg = bucket.getAggregations().get("avg_event_emitted_data");

            jsonObj = new JsonObject();

            jsonObj.addProperty("timestamp", utcToLocaltime(bucket.getKeyAsString()));
            jsonObj.addProperty("rate", avg.getValue());

            jsonArray.add(jsonObj);
        }
        return convert(jsonArray, "rate");
    }

    public JsonArray getEventLatencyData(Histogram searchAgg) {

        JsonObject jsonObj = null;
        JsonArray jsonArray = new JsonArray();

        for (Histogram.Bucket bucket : searchAgg.getBuckets()) {
            Max avg = bucket.getAggregations().get("avg_event_latency_duration");
            Max events = bucket.getAggregations().get("avg_event_latency_out");

            jsonObj = new JsonObject();
            jsonObj.addProperty("timestamp", utcToLocaltime(bucket.getKeyAsString()));
            jsonObj.addProperty("out", events.getValue());
            jsonObj.addProperty("millis", avg.getValue());

            jsonArray.add(jsonObj);
        }

        return convertLatency(jsonArray);
    }

    @SuppressWarnings("unchecked")
    public JsonArray getTopInfoData(SearchHit[] hitsArray) {
        JsonObject jsonObj = null;
        JsonArray jsonArray = new JsonArray();

        for (SearchHit element : hitsArray) {
            Map<String, Object> source_map = element.getSourceAsMap();

            if (!source_map.containsKey("logstash_stats")) {
                continue;
            }

            Map<String, Object> logstash_state_map = (Map<String, Object>) source_map.get("logstash_stats");
            Map<String, Object> events_map = (Map<String, Object>) logstash_state_map.get("events");

            Map<String, Object> jvm_map = (Map<String, Object>) logstash_state_map.get("jvm");
            Map<String, Object> mem_map = (Map<String, Object>) jvm_map.get("mem");
            jsonObj = new JsonObject();
            jsonObj.addProperty("events_in", events_map.get("in").toString());
            jsonObj.addProperty("events_out", events_map.get("out").toString());
            jsonObj.addProperty("heap_used_in_bytes", this.getChangeByte(mem_map.get("heap_used_in_bytes").toString()));
            jsonObj.addProperty("heap_max_in_bytes", this.getChangeByte(mem_map.get("heap_max_in_bytes").toString()));
            jsonArray.add(jsonObj);
        }
        return jsonArray;
    }

    @SuppressWarnings("unchecked")
    public JsonArray getPipelineData(SearchHit[] hitsArray) {

        JsonObject jsonObj = null;
        JsonArray jsonArray = new JsonArray();

        for (SearchHit element : hitsArray) {
            Map<String, Object> source_map = element.getSourceAsMap();

            if (!source_map.containsKey("logstash_state")) {
                continue;
            }

            Map<String, Object> logstash_state_map = (Map<String, Object>) source_map.get("logstash_state");
            Map<String, Object> pipeline_map = (Map<String, Object>) logstash_state_map.get("pipeline");
            Map<String, Object> representation_map = (Map<String, Object>) pipeline_map.get("representation");
            Map<String, Object> graph_map = (Map<String, Object>) representation_map.get("graph");
            ArrayList<Object> vertices_list = (ArrayList<Object>) graph_map.get("vertices");

            String type = "";
            String plugin_type = "";
            String input_nm = "";
            String queue_name = "";
            String filter_nm = "";
            String output_nm = "";

            for (Object vartices_item : vertices_list) {

                Map<String, Object> item_map = (Map<String, Object>) vartices_item;
                type = String.valueOf(item_map.get("type"));

                if ("plugin".equalsIgnoreCase(type)) {

                    plugin_type = String.valueOf(item_map.get("plugin_type"));

                    if ("input".equalsIgnoreCase(plugin_type)) {
                        input_nm += String.valueOf(item_map.get("config_name")) + "\\";
                    } else if ("filter".equalsIgnoreCase(plugin_type)) {
                        filter_nm += String.valueOf(item_map.get("config_name")) + "\\";
                    } else if ("output".equalsIgnoreCase(plugin_type)) {
                        output_nm += String.valueOf(item_map.get("config_name")) + "\\";
                    }
                } else if ("queue".equalsIgnoreCase(type)) {//큐는 나중에 확인 해봐야 함.
                    queue_name += "not available";
                }
            }

            input_nm = input_nm.length() > 0 ? input_nm.substring(0, input_nm.lastIndexOf("\\")) : "";
            filter_nm = filter_nm.length() > 0 ? filter_nm.substring(0, filter_nm.lastIndexOf("\\")) : "";
            output_nm = output_nm.length() > 0 ? output_nm.substring(0, output_nm.lastIndexOf("\\")) : "";

            jsonObj = new JsonObject();
            jsonObj.addProperty("input_name", input_nm);
            jsonObj.addProperty("queue_name", queue_name);
            jsonObj.addProperty("filter_name", filter_nm);
            jsonObj.addProperty("output_name", output_nm);
            jsonArray.add(jsonObj);
        }
        return jsonArray;
    }

    private String utcToLocaltime(String timestamp) {

        String locTime = null;

        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
            sdf.setTimeZone(TimeZone.getTimeZone("GMT"));
            Date parseDate = sdf.parse(timestamp);

            sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            locTime = sdf.format(parseDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return locTime;
    }

    private String valueChk(String val) {

        if ("-Infinity".equalsIgnoreCase(val) || "null".equalsIgnoreCase(val)) {
            val = null;
        }
        return val;
    }

    private String getRound(String sByte, int div) {

        BigDecimal b = null;
        double temp_val = 0d;
        String val = "";

        try {
            b = new BigDecimal(sByte);
            temp_val = b.doubleValue();

            temp_val = ((double) Math.round((temp_val * div)) / div);
        } catch (Exception e) {
            val = this.valueChk(sByte);
            if (val == null) {
                return val;
            } else {
                temp_val = 0d;
            }
        }

        val = String.valueOf(temp_val);
        return val;
    }

    /*private String getChangeByte(String sByte) {

        BigDecimal b = null;
        double temp_val = 0d;
        int cnt = 0;
        String[] unit_array = {"BYTE", "KB", "MB", "GB"};
        String val = "";

        try {
            b = new BigDecimal(sByte);
            temp_val = b.doubleValue();

            while (temp_val > 1024) {
                temp_val = temp_val / 1024.0;
                cnt++;
            }
        } catch (Exception e) {
            val = this.valueChk(sByte);
            if (val == null) {
                return val;
            } else {
                temp_val = 0d;
            }
        }

        val = ((double) Math.round((temp_val * 10)) / 10) + " " + unit_array[cnt];
        return val;
    }

    private String getChangeUnit(String sByte, String unit) {

        BigDecimal b = null;
        double temp_val = 0d;
        int cnt = 0;
        String[] unit_array = {"BYTE", "KB", "MB", "GB", "TB"};
        String val = "";

        for (int i = 0; i < unit_array.length; i++) {

            if (unit_array[i].equalsIgnoreCase(unit)) {
                cnt = i;
            }
        }

        try {
            b = new BigDecimal(sByte);
            temp_val = b.doubleValue();

            for (int i = 0; i < cnt; i++) {
                temp_val = temp_val / 1024.0;
            }
        } catch (Exception e) {
            val = this.valueChk(sByte);
            if (val == null) {
                return val;
            } else {
                temp_val = 0d;
            }
        }

        val = String.valueOf(((double) Math.round((temp_val * 10)) / 10));
        return val;
    }

    public Map<String, Double> calEvent(List<Object> eData, int index, Map<String, Double> calEData) {
        if (index == eData.size()) {
            return calEData;
        } else {
            if (index == 0) {
                calEData.put("events_out", Double.parseDouble(eData.get(0).toString()));
            } else {
                int pre = (int) eData.get(index - 1);
                int cur = (int) eData.get(index);
                calEData.put("events_out", (cur - pre) / 10.0);
            }
        }
        return calEvent(eData, index + 1, calEData);
    }

    public JsonArray convert(JsonArray jsArr, String key) {
        JsonArray newArr = new JsonArray();

        float seconds = 10;
        if (ApiCallService.getInterval() == DateHistogramInterval.HOUR) {
            seconds = 3600;
        }

        for (int i = 0; i < jsArr.size(); i++) {
            newArr.add(jsArr.get(i).deepCopy());
            newArr.get(i).getAsJsonObject().remove("events_out");
        }

        JsonObject zeroObj = new JsonObject();
        zeroObj.add("timestamp", jsArr.get(0).getAsJsonObject().get("timestamp"));
        zeroObj.addProperty(key, jsArr.get(0).getAsJsonObject().get(key).toString());
        newArr.set(0, zeroObj);

        for (int i = 1; i < jsArr.size(); i++) {
            float preData = Float.parseFloat(jsArr.get(i - 1).getAsJsonObject().get(key).toString());
            float curData = Float.parseFloat(jsArr.get(i).getAsJsonObject().get(key).toString());

            JsonObject obj = new JsonObject();
            obj.add("timestamp", jsArr.get(i).getAsJsonObject().get("timestamp"));

            float res = 0f;
            String str_res = "";

            if(Float.isInfinite(preData) || Float.isInfinite(curData)) {
            	str_res = null;
            	obj.addProperty(key, str_res);
            }else {
            	res = (curData - preData) / seconds;

            	if(Float.isNaN(res)) {
            		res = 0f;
            	} else {
            		res = (res < 0) ? 0 : res;
            	}
            	obj.addProperty(key, this.getRound(String.valueOf(res), 10));
            }
            newArr.set(i, obj);
            obj = null;
        }
        newArr.remove(0);
        return newArr;
    }

    public JsonArray convertLatency(JsonArray jsArr) {
        JsonArray newArr = new JsonArray();

        float seconds = 10;
        if (ApiCallService.getInterval() == DateHistogramInterval.HOUR) {
            seconds = 3600;
        }

        for (int i = 0; i < jsArr.size(); i++) {
            newArr.add(jsArr.get(i).deepCopy());
            newArr.get(i).getAsJsonObject().remove("out");
            newArr.get(i).getAsJsonObject().remove("millis");
        }

        for (int i = 1; i < jsArr.size(); i++) {
            float old_millis = Float.parseFloat(jsArr.get(i - 1).getAsJsonObject().get("millis").toString());
            float cur_millis = Float.parseFloat(jsArr.get(i).getAsJsonObject().get("millis").toString());
            float old_out = Float.parseFloat(jsArr.get(i - 1).getAsJsonObject().get("out").toString());
            float cur_out = Float.parseFloat(jsArr.get(i).getAsJsonObject().get("out").toString());
            float res = 0;
            String str_res = "";
            JsonObject obj = new JsonObject();

            obj.add("timestamp", jsArr.get(i).getAsJsonObject().get("timestamp"));

            if((Float.isInfinite(old_millis) || Float.isInfinite(cur_millis)) || (Float.isInfinite(old_out) || Float.isInfinite(cur_out))) {
            	str_res = null;
            	obj.addProperty("millis", str_res);
            }else {
            	res = (cur_millis - old_millis) / (cur_out - old_out) / seconds;

            	if(Float.isNaN(res)) {
            		res = 0f;
            	} else {
            		res = (res < 0) ? 0 : res;
            	}
            	obj.addProperty("millis", this.getRound(String.valueOf(res), 100));
            }
            newArr.set(i, obj);
        }
        newArr.remove(0);
        return newArr;
    }*/
}
