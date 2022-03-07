package com.goodsmile.springboot.gettemperature.controller;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;

import java.util.Optional;

@Controller
public class ResultController extends TemperatureGetter{
    //判断是否获取到参数
    @RequestMapping("getT")
    @ResponseBody
    public String getTemp(@RequestParam("province")String province, @RequestParam("city")String city, @RequestParam("county")String county) throws JsonProcessingException {
        Optional<Integer> temperature=getTemperature(province, city, county);
        if(temperature.isPresent()){
            return "今天的温度是："+temperature.get().toString();
        }
        else return "error.html";

    }

    @ExceptionHandler(value = {JsonParseException.class})
    public String JsonParseExceptionExceptionHandle(Model model, Exception e) {

        return "error";
    }

    @ExceptionHandler(value = {HttpClientErrorException.class})
    public String HttpClientErrorExceptionExceptionHandle(Model model, Exception e) {

        return "error2";
    }

}
