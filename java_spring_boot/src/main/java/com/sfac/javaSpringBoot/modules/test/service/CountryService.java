package com.sfac.javaSpringBoot.modules.test.service;

import com.sfac.javaSpringBoot.modules.test.entity.Country;

public interface CountryService {
    //根据国家Id查询国家
    Country getCountryByCountryId(int countryId);

    //根据国家名查询国家信息
    Country getCountryByCountryName(String countryName);

    //从MySQL数据库拿数据存到redis中，然后再拿出来
    Country mograteCountryByRedis(int countryId);
}
