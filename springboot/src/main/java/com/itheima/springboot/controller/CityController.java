package com.itheima.springboot.controller;

import com.itheima.springboot.entities.City;
import com.itheima.springboot.resp.ResultData;
import com.itheima.springboot.service.Ipml.CityServiceImpl;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/cities")
@Tag(name = "旅游管理", description = "旅游管理相关接口")
public class CityController {


    @Autowired
    private CityServiceImpl cityService;


    @GetMapping("/getAll")
    @Operation(summary = "获取所有城市")
    public ResultData<List<City>> getAllCities() {
        List<City> cities = cityService.getAllCities();
        return ResultData.success(cities);
    }

    @PostMapping("/add")
    @Operation(summary = "添加城市")
    public ResultData<City> addCity( @RequestBody City city) {
        cityService.saveCity(city);
        return ResultData.success(city);
    }

    @GetMapping("/get/{id}")
    @Operation(summary = "根据id获取城市")
    public ResultData<City> getCityById(@PathVariable("id") Integer id) {
        City city = cityService.getCityById(id);
        return ResultData.success(city);
    }

    @PutMapping("/update/{id}")
    @Operation(summary = "更新城市")
    public ResultData<String> updateCity(@RequestBody City city) {
        cityService.updateCity(city);
        return ResultData.success("添加成功");
    }

    @DeleteMapping("/delete/{id}")
    @Operation(summary = "删除城市")
    public ResultData<String> deleteCity(@PathVariable("id") Integer id) {
        cityService.deleteCity(id);
        return ResultData.success("删除成功");
    }

    @GetMapping("/view/{id}")
    @Operation(summary = "查看城市")
    public ResultData<City> viewCity(@PathVariable("id") Integer id) {
        City city = cityService.getCityById(id);
        return ResultData.success(city);
    }
}
