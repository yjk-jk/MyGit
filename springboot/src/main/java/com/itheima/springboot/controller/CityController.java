package com.itheima.springboot.controller;

import com.itheima.springboot.entities.City;
import com.itheima.springboot.resp.ResultData;
import com.itheima.springboot.service.Ipml.CityServiceImpl;
import com.itheima.springboot.utils.ImageUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;


import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


import java.io.*;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/cities")
@Tag(name = "旅游管理", description = "旅游管理相关接口")
public class CityController {

    @Autowired
    private CityServiceImpl cityService;

    // 图片上传接口
    @PostMapping("/{cityId}/upload")
    @Operation(summary = "上传城市图片", description = "根据城市ID上传对应的图片")
    public ResultData<String> uploadImage(
            @PathVariable("cityId") Integer cityId, // 显式指定参数名称
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

        // 获取图片保存路径
        String uploadPath = "D:/uploads/";

        File uploadFile = new File(uploadPath + file.getOriginalFilename());

        // 将文件保存到指定目录
        file.transferTo(uploadFile);

        String url = "http://localhost:8001/uploads/"+file.getOriginalFilename();

        // 更新数据库记录
        city.setImageUrl(url);
        cityService.updateCity(city);

        return ResultData.success("上传成功");

    }




    // 图片下载接口
    @GetMapping("/{cityId}/images")
    @Operation(summary = "下载城市图片", description = "根据城市ID和图片名下载对应的图片")
    public ResponseEntity<InputStreamResource> downloadImage(
            @PathVariable("cityId") Integer cityId) throws IOException {

        // 验证城市是否存在
        City city = cityService.getCityById(cityId);
        if (city == null) {
            throw new RuntimeException("城市不存在");
        }

        // 获取图片路径
        String localPath = city.getImageUrl().replace("http://localhost:8001/uploads/", "D:/uploads/");

        FileInputStream fileInputStream = new FileInputStream(localPath);
            // 生成唯一文件名
            String fileName = UUID.randomUUID().toString();
            InputStreamResource inputStreamResource = new InputStreamResource(fileInputStream);

            return ResponseEntity.ok()
                    .contentType(ImageUtil.getMediaType(localPath))
                    .contentLength(fileInputStream.available())
                    .header(HttpHeaders.CONTENT_DISPOSITION, fileName)
                    .body(inputStreamResource);

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
