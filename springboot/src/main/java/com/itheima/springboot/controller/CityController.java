package com.itheima.springboot.controller;

import cn.hutool.core.io.resource.InputStreamResource;
import com.itheima.springboot.entities.City;
import com.itheima.springboot.resp.ResultData;
import com.itheima.springboot.service.Ipml.CityServiceImpl;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/cities")
@Tag(name = "旅游管理", description = "旅游管理相关接口")
public class CityController {

    @Autowired
    private CityServiceImpl cityService;

    // 图片存储目录
    @Value("${spring.mvc.static-path-pattern}")
    private static String UPLOAD_DIR;

    // 图片上传接口
    @PostMapping("/{cityId}/upload")
    @Operation(summary = "上传城市图片", description = "根据城市ID上传对应的图片")
    public ResultData<String> uploadImage(
            @PathVariable Integer cityId,
            @RequestParam("file") MultipartFile file) throws IOException {

        // 验证城市是否存在
        City city = cityService.getCityById(cityId);
        if (city == null) {
            throw new IllegalArgumentException("城市不存在");
        }

        Long size = file.getSize();
        if (size > 1024 * 1024 * 10) {
            throw new IllegalArgumentException("图片大小不能超过10M");
        }

        file.transferTo(new File(UPLOAD_DIR + "/" + file.getOriginalFilename()));

        return ResultData.success("上传成功");

    }

    // 图片下载接口
    @GetMapping("/{cityId}/images")
    @Operation(summary = "下载城市图片", description = "根据城市ID和图片名下载对应的图片")
    public ResponseEntity<InputStreamResource> downloadImage(
            @PathVariable Integer cityId) throws FileNotFoundException {

        // 验证城市是否存在
        City city = cityService.getCityById(cityId);
        if (city == null) {
            throw new IllegalArgumentException("城市不存在");
        }

        FileInputStream fileInputStream = new FileInputStream(city.getImageUrl());

        try {
            // 生成唯一文件名
            String fileName = UUID.randomUUID().toString();
            InputStreamResource inputStreamResource = new InputStreamResource
                    (fileInputStream);

            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .contentLength(fileInputStream.available())
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileName + "\"")
                    .body(inputStreamResource);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }


    @GetMapping("/getAll")
    @Operation(summary = "获取所有城市")
    public ResultData<List<City>> getAllCities() {
        List<City> cities = cityService.getAllCities();
        return ResultData.success(cities);
    }

    @PostMapping("/add")
    @Operation(summary = "添加城市")
    public ResultData<City> addCity(@RequestBody City city) {
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
