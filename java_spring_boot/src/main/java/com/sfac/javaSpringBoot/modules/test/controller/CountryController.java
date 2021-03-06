package com.sfac.javaSpringBoot.modules.test.controller;

import com.sfac.javaSpringBoot.modules.test.entity.Country;
import com.sfac.javaSpringBoot.modules.test.service.CountryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class CountryController {

    @Autowired
    private CountryService countryService;

    /**
     * 127.0.0.1/api/country/522 ---- get
     * @param countryId
     * @return
     */
    //根据国家id查询国家信息
    @GetMapping("/country/{countryId}")
    public Country getCountryByCountryId(@PathVariable int countryId){
        return countryService.getCountryByCountryId(countryId);
    }

    /**
     * 127.0.0.1/api/country?countryName=China ---- get
     * @param countryName
     * @return
     */
    @GetMapping("/country")
    public Country getCountryByCountryId(@RequestParam String countryName){
        return countryService.getCountryByCountryName(countryName);
    }

    //从MySQL数据库拿数据存到redis中，然后再拿出来

    /**
     * 127.0.0.1/api/redis/country/522 --- get
     * @param countryId
     * @return
     */
    /**
     * 127.0.0.1/api/redis/country/522 ---- get
     */
    @GetMapping("/redis/country/{countryId}")
    public Country mograteCountryByRedis(@PathVariable int countryId) {
        return countryService.mograteCountryByRedis(countryId);
    }
}
