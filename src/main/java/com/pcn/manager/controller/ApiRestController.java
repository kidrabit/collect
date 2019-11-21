package com.pcn.manager.controller;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.aggregations.bucket.histogram.DateHistogramInterval;
import org.elasticsearch.search.aggregations.bucket.histogram.Histogram;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.pcn.manager.monitor.service.ApiCallService;
import com.pcn.manager.monitor.service.ApiRestService;
import com.pcn.manager.util.DateUtil;

@RestController
@RequestMapping("/manager")
public class ApiRestController {

    @Autowired
    public ApiRestService apiRestService;

    LocalDate curDate = LocalDate.now();
    String todayDate = curDate.toString().concat("T");
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");

    @GetMapping("/elasticGet")
    @ResponseBody
    public Object ChartCall(@RequestParam Map<String, String> paraMap) {
        ApiCallService api = new ApiCallService();
        DateUtil du = new DateUtil();

        if (paraMap.get("fromTime") != null) {
            String[] Dates = paraMap.get("fromTime").split(" - ");

            du.setFromDate(LocalDateTime.parse(Dates[0].concat("Z"), formatter).minusHours(9).atOffset(ZoneOffset.UTC).toString());
            du.setToDate(LocalDateTime.parse(Dates[1].concat("Z"), formatter).minusHours(9).atOffset(ZoneOffset.UTC).toString());
            du.dateDiffer();

        } else {
            du.setFromDate("now-10m");
            du.setToDate("now");
        }

        if (du.isDateDiffer()) {
            ApiCallService.setInterval(DateHistogramInterval.HOUR);
        } else {
            ApiCallService.setInterval(DateHistogramInterval.seconds(10));
        }

        JsonObject jsonObj = new JsonObject();
        Histogram dateHistogram = api.callApi("NONE", du.getFromDate(), du.getToDate());
        jsonObj.add("jvm_heap", apiRestService.getJvmHeapData(dateHistogram));
        jsonObj.add("cpu_utilization", apiRestService.getCpuUtilizationData(dateHistogram));
        jsonObj.add("system_load", apiRestService.getSystemLoadData(dateHistogram));

        Gson gson = new GsonBuilder().serializeNulls().create();
        return gson.toJson(jsonObj);
    }

    /**
     * @fn parseUTC
     * @remark
     * - 함수의 상세 설명 :
     * @param string
     * @param string2
    */

    /*
     * Event */
    @GetMapping("/eventGet")
    public Object EventChartCall(@RequestParam Map<String, String> paraMap) {
        ApiCallService api = new ApiCallService();
        DateUtil du = new DateUtil();
        LocalDateTime cur = LocalDateTime.now(ZoneOffset.UTC);
        //LocalDateTime cur = LocalDateTime.of(Integer.parseInt("2019"), Integer.parseInt("11"), Integer.parseInt("15"), Integer.parseInt("06"), Integer
        //   .parseInt("24"), Integer.parseInt("55"));

        if (paraMap.get("fromTime") != null) {
            String[] Dates = paraMap.get("fromTime").split(" - ");

            du.setFromDate(LocalDateTime.parse(Dates[0].concat("Z"), formatter).minusHours(9).minusMinutes(10).atOffset(ZoneOffset.UTC).toString());
            du.setToDate(LocalDateTime.parse(Dates[1].concat("Z"), formatter).minusHours(9).atOffset(ZoneOffset.UTC).toString());
            du.dateDiffer();

        } else {
            LocalDateTime preTenMin = cur.minusMinutes(10);
            if (preTenMin.getNano() == 0 && preTenMin.getSecond() != 0) {
                du.setFromDate(preTenMin.toString().concat(".000Z"));
            } else {
                du.setFromDate(preTenMin.toString().concat("Z"));
            }
            du.setToDate("now");
        }
        if (du.isDateDiffer()) {
            ApiCallService.setInterval(DateHistogramInterval.HOUR);
        } else {
            ApiCallService.setInterval(DateHistogramInterval.seconds(10));
        }
        JsonObject jsonObj = new JsonObject();

        Histogram dateHistogram = api.callApi("EVENT", du.getFromDate(), du.getToDate());

        jsonObj.add("events_recevied", apiRestService.getEventsReceivedData(dateHistogram));
        jsonObj.add("events_emitted_rate", apiRestService.getEventsEmittedRateData(dateHistogram));
        jsonObj.add("even_latency", apiRestService.getEventLatencyData(dateHistogram));
        Gson gson = new GsonBuilder().serializeNulls().create();
        return gson.toJson(jsonObj);
    }

    @GetMapping("/elasticDashboardGet")
    public Object DashboardCall() {

        ApiCallService api = new ApiCallService();
        SearchHits searchHits = api.dashBoard();
        SearchHit[] hitsArray = searchHits.getHits();
        JsonObject jsonObj = new JsonObject();

        jsonObj.add("top_info", apiRestService.getTopInfoData(hitsArray));//nodes필드 확인후 수정해야함.
        jsonObj.add("pipeline", apiRestService.getPipelineData(hitsArray));

        DateUtil du = new DateUtil();
        LocalDateTime cur = LocalDateTime.now(ZoneOffset.UTC);
        du.setFromDate(cur.minusMinutes(15).toString().concat("Z"));
        du.setToDate("now");

        ApiCallService.setInterval(DateHistogramInterval.seconds(10));

        Histogram histogram = api.callApi("EVENT", du.getFromDate(), du.getToDate());

        jsonObj.add("events_recevied", apiRestService.getEventsReceivedData(histogram));
        jsonObj.add("events_emitted_rate", apiRestService.getEventsEmittedRateData(histogram));
        jsonObj.add("even_latency", apiRestService.getEventLatencyData(histogram));

        Gson gson = new GsonBuilder().serializeNulls().create();
        return gson.toJson(jsonObj);
    }

}
