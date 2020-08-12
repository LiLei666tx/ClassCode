package com.sfac.javaSpringBoot.modules.test.dao;

import com.sfac.javaSpringBoot.modules.test.entity.Country;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.Mapping;

import java.util.List;

//dao层的注解
@Repository
@Mapper
public interface CountryDao {

    //根据id查询country对象
    @Select("select * from m_country where country_id = #{countryId}")
    //做一个映射对City（@Results 封装结果集）
    @Results(id = "countryResults", value = {
            //再加一个countryId,因为被使用了
            @Result( column = "country_id" , property = "countryId"),
            @Result( column = "country_id" , property = "cities",
                    javaType = List.class,
                    many = @Many(select = "com.sfac.javaSpringBoot.modules.test.dao.CityDao.getCitiesByCountryId"))
    })
    Country getCountryByCountryId(int countryId);

    //根据country名字查询country信息
    @Select("select * from m_country where country_name = #{countryName}")
    @ResultMap(value = "countryResults")
    Country getCountryByCountryName(String countryName);
}
