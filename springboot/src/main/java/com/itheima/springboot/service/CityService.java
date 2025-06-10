package com.itheima.springboot.service;

import com.itheima.springboot.entities.City;

import java.util.List;

public interface CityService {
    public List<City> getAllCities();

    public City getCityById(Integer id);

    public boolean saveCity(City city);

    public int updateCity(City city);

    public boolean deleteCity(Integer id);
}
