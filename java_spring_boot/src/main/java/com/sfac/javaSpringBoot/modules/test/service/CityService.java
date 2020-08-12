package com.sfac.javaSpringBoot.modules.test.service;

import com.github.pagehelper.PageInfo;
import com.sfac.javaSpringBoot.modules.common.vo.Result;
import com.sfac.javaSpringBoot.modules.common.vo.SearchVo;
import com.sfac.javaSpringBoot.modules.test.entity.City;

import java.util.List;

public interface CityService {

    //根据国家名来查询城市
    List<City> getCitiesByCountryId(int countryId);

    //分页对象（pageInfo）
    PageInfo<City> getCitiesBySearchVo(int countryId,SearchVo searchVo);

    PageInfo<City> getCitiesBySearchVo(SearchVo searchVo);

    Result<City> insertCity(City city);

    Result<City> updateCity(City city);

    //因为删除对象都没有了，所以就可以直接不用<City>了，直接<Object>就行了
    Result<Object> deleteCity(int cityId);
}
