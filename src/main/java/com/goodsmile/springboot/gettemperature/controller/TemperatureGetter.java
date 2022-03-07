package com.goodsmile.springboot.gettemperature.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

//@RequestMapping("getTs")
public class TemperatureGetter {
    @Autowired
    public RestTemplate restTemplate;
    //@RequestMapping(value="getT",produces = "application/json;charset=GB2312")
    public Optional<Integer> getTemperature(String province, String city, String county) throws JsonProcessingException,NullPointerException {

        String provinceUrl="http://www.weather.com.cn/data/city3jdata/china.html";

        //获取发回的信息
        ResponseEntity<String> response=restTemplate.getForEntity(provinceUrl, String.class);


        //System.out.println(response.getHeaders());
        String provincejson=response.getBody();
        ObjectMapper objMapper=new ObjectMapper();

        //将json转化为Map
        Map<String,Object> provinceMap=objMapper.readValue(provincejson, Map.class);
        //反转，便于查找
        Set<String>  provinceCodes=provinceMap.keySet();
        Map<String,String> provinceMap2=new HashMap<>();
        for(String code : provinceCodes) {
            String value = (String) provinceMap.get(code);
            provinceMap2.put(value, code);
        }

        String provinceCode=provinceMap2.get(province);



        //查询市代码

        String cityUrl="http://www.weather.com.cn/data/city3jdata/provshi/"+provinceCode+".html";
        response=restTemplate.getForEntity(cityUrl, String.class);
        String cityjson=response.getBody();
        //将json转化为Map
        Map<String,Object> cityMap=objMapper.readValue(cityjson, Map.class);
        //反转，便于查找
        Set<String>  cityCodes=cityMap.keySet();
        Map<String,String> cityMap2=new HashMap<>();
        for(String code : cityCodes) {
            String value = (String) cityMap.get(code);
            cityMap2.put(value, code);
        }
        String cityCode=cityMap2.get(city);

        String countyUrl="http://www.weather.com.cn/data/city3jdata/station/"+provinceCode+cityCode+".html";
        response=restTemplate.getForEntity(countyUrl, String.class);
        String countyjson=response.getBody();
        //将json转化为Map
        Map<String,Object> countyMap=objMapper.readValue(countyjson, Map.class);
        //反转，便于查找
        Set<String>  countyCodes=countyMap.keySet();
        Map<String,String> countyMap2=new HashMap<>();
        for(String code : countyCodes) {
            String value = (String) countyMap.get(code);
            countyMap2.put(value, code);
        }
        String countyCode=countyMap2.get(county);

        String weatherUrl="http://www.weather.com.cn/data/sk/"+provinceCode+cityCode+countyCode+".html";
        response=restTemplate.getForEntity(weatherUrl, String.class);

        //解析json，获取最后的temp

        String weatherjson=response.getBody();
        //Map<String,Object> weatherMap=objMapper.readValue(weatherjson, Map.class);
        JsonNode weatherJsonNode=objMapper.readTree(weatherjson);
        String weatherInfo=weatherJsonNode.get("weatherinfo").toString();
        Map<String,String> weatherMap=objMapper.readValue(weatherInfo,Map.class);
        Double d=Double.parseDouble(weatherMap.get("temp"));
        Integer tem= d.intValue();
        Optional<Integer> temp=Optional.ofNullable(tem);
        return temp;
        }
}

