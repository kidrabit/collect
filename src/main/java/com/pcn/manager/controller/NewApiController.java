package com.pcn.manager.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.aggregations.bucket.histogram.Histogram;
import org.joda.time.LocalDate;
import org.json.JSONArray;
import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.NoHandlerFoundException;

import com.google.gson.JsonArray;
import com.pcn.manager.monitor.service.ApiRestService;
import com.pcn.manager.monitor.service.NewApiService;

/**
 * @class NewApiController
 * 	com.pcn.manager.controller
 * @section 클래스작성정보
 *    |    항  목        	|       	내  용       			|
 *    | :--------: 	| -----------------------------	|
 *    | Company 	| PCN
 *    | Author 		| rnd
 *    | Date 		| 2019. 10. 4.
 *    | 작업자 		| rnd, Others...
 * @section 상세설명
 * - 클래스의 업무내용에 대해 기술...
*/
@RestController
@RequestMapping("/plugin")
public class NewApiController {

    @Autowired
    public NewApiService apiService;

    @Autowired
    public ApiRestService restService;

    @Autowired
    public NewApiService newService;

    public static LocalDate currentDate = LocalDate.now();
    
    //404
    @RequestMapping(value = "/**")
    public void apiNotMappingRequest(HttpServletRequest req, HttpServletResponse res,@RequestHeader HttpHeaders headers) throws NoHandlerFoundException {
    	throw new NoHandlerFoundException("URL not found exception", req.getRequestURL().toString(), headers);
    }
    
    @RequestMapping(value = "/jvm/{fromDate}/{toDate}", method = RequestMethod.GET)
    @ResponseStatus(value = HttpStatus.OK)
    public JsonArray getJvm(@PathVariable String fromDate, @PathVariable String toDate) {

        if (fromDate.contains("now")) {
            fromDate = fromDate.concat("h/h");
        }
        Histogram dateHistogram = null;
        try {
            dateHistogram = apiService.callApi("jvm", convert(fromDate), convert(toDate));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return restService.getJvmHeapData(dateHistogram);
    }

    @RequestMapping(value = {"/jvm/all", "/jvm/all/{size}"}, method = RequestMethod.GET)
    @ResponseStatus(value = HttpStatus.OK)
    public JsonArray getJvm() {

        Histogram dateHistogram = apiService.callApi("jvm", "now/d", "now");
        return restService.getJvmHeapData(dateHistogram);
    }

    @RequestMapping(value = "/cpu/{fromDate}/{toDate}", method = RequestMethod.GET)
    @ResponseStatus(value = HttpStatus.OK)
    public JsonArray getCpu(@PathVariable String fromDate, @PathVariable String toDate) {

        if (fromDate.contains("now")) {
            fromDate = fromDate.concat("h/h");
            System.out.println(fromDate);
        }

        Histogram dateHistogram = null;
        try {
            dateHistogram = apiService.callApi("cpu", convert(fromDate), convert(toDate));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return restService.getCpuUtilizationData(dateHistogram);
    }

    @RequestMapping(value = "/cpu/all", method = RequestMethod.GET)
    @ResponseStatus(value = HttpStatus.OK)
    public JsonArray getCpu() {
        Histogram dateHistogram = apiService.callApi("cpu", "now/d", "now");
        return restService.getCpuUtilizationData(dateHistogram);
    }

    @RequestMapping(value = "/load/{fromDate}/{toDate}", method = RequestMethod.GET)
    @ResponseStatus(value = HttpStatus.OK)
    public JsonArray getLoadData(@PathVariable String fromDate, @PathVariable String toDate) {

        if (fromDate.contains("now")) {
            fromDate = fromDate.concat("h/h");
            System.out.println(fromDate);
        }

        Histogram dateHistogram = null;
        try {
            dateHistogram = apiService.callApi("load", convert(fromDate), convert(toDate));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return restService.getSystemLoadData(dateHistogram);
    }

    @RequestMapping(value = "/load/all", method = RequestMethod.GET)
    @ResponseStatus(value = HttpStatus.OK)
    public JsonArray getLoadData() {
        Histogram dateHistogram = apiService.callApi("load", "now/d", "now");
        return restService.getSystemLoadData(dateHistogram);
    }

    @RequestMapping(value = "/received/{fromDate}/{toDate}", method = RequestMethod.GET)
    @ResponseStatus(value = HttpStatus.OK)
    public JsonArray getReceivedEvent(@PathVariable String fromDate, @PathVariable String toDate) {

        if (fromDate.contains("now")) {
            fromDate = fromDate.concat("h/h");
            System.out.println(fromDate);
        }

        Histogram dateHistogram = null;
        try {
            dateHistogram = apiService.eventReceived(convert(fromDate), convert(toDate));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return restService.getEventsReceivedData(dateHistogram);
    }

    @RequestMapping(value = "/received/all", method = RequestMethod.GET)
    @ResponseStatus(value = HttpStatus.OK)
    public JsonArray getReceivedEvent() {
        Histogram dateHistogram = apiService.eventReceived("now/d", "now");
        return restService.getEventsReceivedData(dateHistogram);
    }

    @RequestMapping(value = "/emitted/{fromDate}/{toDate}", method = RequestMethod.GET)
    @ResponseStatus(value = HttpStatus.OK)
    public JsonArray getEmittedEvent(@PathVariable String fromDate, @PathVariable String toDate) {

        if (fromDate.contains("now")) {
            fromDate = fromDate.concat("h/h");
            System.out.println(fromDate);
        }

        Histogram dateHistogram = null;
        try {
            dateHistogram = apiService.eventEmitted(convert(fromDate), convert(toDate));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return restService.getEventsEmittedRateData(dateHistogram);
    }

    @RequestMapping(value = "/emitted/all", method = RequestMethod.GET)
    @ResponseStatus(value = HttpStatus.OK)
    public JsonArray getEmittedEvent() {
        Histogram dateHistogram = apiService.eventEmitted("now/d", "now");
        return restService.getEventsEmittedRateData(dateHistogram);
    }

    @RequestMapping(value = "/latency/{fromDate}/{toDate}", method = RequestMethod.GET)
    @ResponseStatus(value = HttpStatus.OK)
    public JsonArray getLatencyEvent(@PathVariable String fromDate, @PathVariable String toDate) {

        if (fromDate.contains("now")) {
            fromDate = fromDate.concat("h/h");
            System.out.println(fromDate);
        }

        Histogram dateHistogram = null;
        try {
            dateHistogram = apiService.eventLatency(convert(fromDate), convert(toDate));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return restService.getEventLatencyData(dateHistogram);
    }

    @RequestMapping(value = "/latency/all", method = RequestMethod.GET)
    @ResponseStatus(value = HttpStatus.OK)
    public JsonArray getLatencyEvent() {
        Histogram dateHistogram = apiService.eventLatency("now/d", "now");

        return restService.getEventLatencyData(dateHistogram);
    }

    @RequestMapping(value = {"/weather/all", "/weather/all/{size}"}, method = RequestMethod.GET)
    @ResponseStatus(value = HttpStatus.OK)
    public JSONArray getKWeatherData(@PathVariable(value = "size", required = false) Integer size) {
        if (size == null) {
            size = 10;
        }
        return getJson(newService.callKWeather("now-1", "now", size));
    }

    @RequestMapping(value = {"/weather/{fromDate}/{toDate}", "/weather/{fromDate}/{toDate}/{size}"}, method = RequestMethod.GET)
    @ResponseStatus(value = HttpStatus.OK)
    public JSONArray getKWeatherData(
        @PathVariable String fromDate,
            @PathVariable String toDate,
            @PathVariable(value = "size", required = false) Integer size) {
        if (size == null) {
            size = 10;
        }
        return getJson(newService.callKWeather(fromDate, toDate, size));
    }

    @RequestMapping(value = {"/dust/all", "/dust/all/{size}"}, method = RequestMethod.GET)
    @ResponseStatus(value = HttpStatus.OK)
    public JSONArray getDusts(@PathVariable(value = "size", required = false) Integer size) {
        if (size == null) {
            size = 10;
        }
        return getJson(newService.callPmDatas("now/d", "now", size));
    }

    @RequestMapping(value = {"/dust/{fromDate}/{toDate}", "/dust/{fromDate}/{toDate}/{size}"}, method = RequestMethod.GET)
    @ResponseStatus(value = HttpStatus.OK)
    public JSONArray getDusts(
        @PathVariable String fromDate,
            @PathVariable String toDate,
            @PathVariable(value = "size", required = false) Integer size) {
        if (size == null) {
            size = 10;
        }
        return getJson(newService.callPmDatas(fromDate, toDate, size));
    }

    @RequestMapping(value = {"/noise/all", "/noise/all/{size}"}, method = RequestMethod.GET)
    @ResponseStatus(value = HttpStatus.OK)
    public JSONArray getNoises(@PathVariable(value = "size", required = false) Integer size) {
        if (size == null) {
            size = 10;
        }
        return getJson(newService.callNoiseDatas("now/d", "now", size));
    }

    @RequestMapping(value = {"/noise/{fromDate}/{toDate}", "/noise/{fromDate}/{toDate}/{size}"}, method = RequestMethod.GET)
    @ResponseStatus(value = HttpStatus.OK)
    public JSONArray getNoises(
        @PathVariable String fromDate,
            @PathVariable String toDate,
            @PathVariable(value = "size", required = false) Integer size) {
        if (size == null) {
            size = 10;
        }
        return getJson(newService.callNoiseDatas(fromDate, toDate, size));
    }

    @RequestMapping(value = {"/temp/all", "/temp/all/{size}"}, method = RequestMethod.GET)
    @ResponseStatus(value = HttpStatus.OK)
    public JSONArray getTemp(@PathVariable(value = "size", required = false) Integer size) {
        if (size == null) {
            size = 10;
        }
        return getJson(newService.callTempDatas("now/d", "now", size));
    }

    @RequestMapping(value = {"/temp/{fromDate}/{toDate}", "/temp/{fromDate}/{toDate}/{size}"}, method = RequestMethod.GET)
    @ResponseStatus(value = HttpStatus.OK)
    public JSONArray getTemp(
        @PathVariable String fromDate,
            @PathVariable String toDate,
            @PathVariable(value = "size", required = false) Integer size) {
        if (size == null) {
            size = 10;
        }
        return getJson(newService.callTempDatas(fromDate, toDate, size));
    }

    public JSONArray getJson(SearchHit[] hits) {
        JSONArray jsonArray = new JSONArray();
        List<Map<String, Object>> list = new ArrayList();
        Map<String, Object> elements = new HashMap<>();
        SearchHit[] hitsArr = hits;
        for (SearchHit element : hitsArr) {
            elements = element.getSourceAsMap();
            list.add(elements);
        }
        for (int i = 0; i < list.size(); i++) {
            try {
                jsonArray.put(i, list.get(i));
            } catch (JSONException e) {
                System.out.println("검색 결과가 없습니다.");
                //e.printStackTrace();
            }
        }
        return jsonArray;
    }

    public String convert(String date) throws Exception {
        StringBuilder res = null;
        try {

            date = date.concat(":00.000Z");
            res = new StringBuilder(date);
            String d1 = res.substring(0, 10);
            String d2 = res.substring(10);

            res = new StringBuilder(d1.concat("T").concat(d2));
            String str1 = res.substring(0, 11);
            String str2 = res.substring(13, res.length());
            String time = Long.toString(Long.parseLong(res.substring(11, 13)) - 9);
            if (time.length() == 1) {
                time = "0".concat(time);
            }
            res = new StringBuilder(str1 + time + str2);
            System.out.println("res = " + res);
        } catch (Exception e) {
            System.out.println("날짜 형식이 잘못 되었습니다");
        }
        System.out.println(res.toString());
        return res.toString();
    }

}
