package com.itheima.springboot.service.Ipml;


import com.itheima.springboot.entities.City;
import com.itheima.springboot.mapper.CityMapper;
import com.itheima.springboot.service.CityService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CityServiceImpl implements CityService {

    @Resource
    private CityMapper cityMapper;

    public List<City> getAllCities() {
        return cityMapper.selectAll();
    }

    public City getCityById(Integer id) {
        return cityMapper.selectByPrimaryKey(id);
    }

    public boolean saveCity(City city) {
        return cityMapper.insert(city) > 0;
    }

    public int updateCity(City city) {
        return cityMapper.updateByPrimaryKeySelective(city);
    }

    public boolean deleteCity(Integer id) {
        return cityMapper.deleteByPrimaryKey(id) > 0;
    }
}