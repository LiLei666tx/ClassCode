package com.sfac.javaSpringBoot.modules.test.controller;

import com.sfac.javaSpringBoot.modules.test.entity.City;
import com.sfac.javaSpringBoot.modules.test.entity.Country;
import com.sfac.javaSpringBoot.modules.test.service.CityService;
import com.sfac.javaSpringBoot.modules.test.service.CountryService;
import com.sfac.javaSpringBoot.modules.test.vo.ApplicationTest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/test")
public class TestController {

    private final static Logger LOGGER = LoggerFactory.getLogger(TestController.class);

    @Value("${server.port}")
    private int port;
    @Value("${com.name}")
    private String name;
    @Value("${com.age}")
    private int age;
    @Value("${com.desc}")
    private String desc;
    @Value("${com.random}")
    private String random;

    @Autowired
    private ApplicationTest applicationTest;
    @Autowired
    private CityService cityService;
    @Autowired
    private CountryService countryService;

    /**
     * 下载文件
     *127.0.0.1/test/file --- get
     */
    @GetMapping("/file")
    public ResponseEntity<Resource> downloadFile(@RequestParam String fileName){
        Resource resource = null;
        try {
            resource = new UrlResource(
                    Paths.get("D:\\upload\\"+ fileName).toUri());
            //判断资源是否存在且可读的
            if(resource.exists() && resource.isReadable()){
                return ResponseEntity
                        .ok()
                        .header(HttpHeaders.CONTENT_TYPE,"application/octet-stream") //下载的网络请求头上的类型
                        .header(HttpHeaders.CONTENT_DISPOSITION, //描述
                                String.format("attachment; filename=\"%s\"", resource.getFilename()))
                        .body(resource);
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 单个文件的上传
     *127.0.0.1/test/file --- post
     */
    //前端提交文件以form表单提交的形式的话，那么consums就需要写
    @PostMapping(value = "/file", consumes = "multipart/form-data")
    //上传文件的参数应该写（）
    public String uploadFile(@RequestParam MultipartFile file, RedirectAttributes redirectAttributes){

        //判断是否未选择文件就提交，也即是上传的我呢见为空，就直接给出提示
        if(file.isEmpty()){
            redirectAttributes.addFlashAttribute("message","请选择上传文件");
            return "redirect:/test/index";
        }
        try {
            //把上传的文件放到（D:/upload），file.getOriginalFilename()这个意思是文件名还是上传之前的名字，不做变更
            String destFilePath = "D:\\upload\\" + file.getOriginalFilename();
            //把路径放到一个新的File(目标文件)
            File destFile = new File(destFilePath);
            //文件上传（刚刚new的File）
            file.transferTo(destFile);
            redirectAttributes.addFlashAttribute("message","上传成功");
        } catch (IOException e) {
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("message","上传失败");
        }

        //参数的复用
        return "redirect:/test/index";
    }

    /**
     *127.0.0.1/test/files
     */
    @PostMapping(value = "/files", consumes = "multipart/form-data")
    public String uploadFiles(@RequestParam MultipartFile[] files,
                              RedirectAttributes redirectAttributes){
        //假定所有的文件都为空
        boolean empty = true;

        try {
            for (MultipartFile file : files) {
                //判断单词文件上传执行的是否为空，是的话，就跳出本次循环，+
                //+如果不为空的话，就给empty赋值false,就不用提示（请选择文件）该语句
                if(file.isEmpty()){
                    continue;
                }

                String destFilePath = "D:\\upload\\" + file.getOriginalFilename();
                File destFile = new File(destFilePath);
                file.transferTo(destFile);
                empty = false;
            }

            //如果全为空的话，就提醒
            if(empty){
                redirectAttributes.addFlashAttribute("message","请选择上传文件");
            }else{
                redirectAttributes.addFlashAttribute("message","上传成功");
            }
        }catch (IOException e){
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("message","上传失败");
        }

        return "redirect:/test/index";
    }

    /**
     * 127.0.0.1/test/index --- get
     * @return
     */
    @GetMapping("/index")
    public String testIndexPage(ModelMap modelMap){
        modelMap.addAttribute("thymeleafTitle","Thymeleaf Text,hahaha");
        modelMap.addAttribute("template","test/index");

        int countryId = 522;
        List<City> cities = cityService.getCitiesByCountryId(countryId);
        cities = cities.stream().limit(10).collect(Collectors.toList());
        Country country = countryService.getCountryByCountryId(countryId);

        modelMap.addAttribute("thymeleafTitle", "scdscsadcsacd");
        modelMap.addAttribute("checked", true);
        modelMap.addAttribute("currentNumber", 99);
        modelMap.addAttribute("changeType", "checkbox");
        modelMap.addAttribute("baiduUrl", "/test/log");
        modelMap.addAttribute("city", cities.get(0));
        modelMap.addAttribute("shopLogo",
                "http://cdn.duitang.com/uploads/item/201308/13/20130813115619_EJCWm.thumb.700_0.jpeg");
        modelMap.addAttribute("shopLogo",
                "/upload/1111.png");
        modelMap.addAttribute("country", country);
        modelMap.addAttribute("cities", cities);
        modelMap.addAttribute("updateCityUri", "/api/city");
        //下面的代码可以用拦截器来实现
//        modelMap.addAttribute("template", "test/index");
        //返回templates外层的碎片组装器
        return "index";
    }

    /*
        127.0.0.1/test/index2
     */
    @GetMapping("index2")
    public String testIndex2Page(ModelMap modelMap){
        modelMap.addAttribute("template","test/index2");
        //返回templates外层的碎片组装器
        return "index";
    }

    /**
     * 127.0.0.1:8005/test/logTest ---- get
     * @return
     */
    @GetMapping("/logTest")
    @ResponseBody
    public String logTest(){
        LOGGER.trace("This is trace log");
        LOGGER.debug("This is debug log");
        LOGGER.info("This is info log");
        LOGGER.warn("This is warn log");
        LOGGER.error("This is error log");
        return "This is log test111";
    }

    /**
     * 127.0.0.1:8085/test/config --- get
     * @return
     */
    @GetMapping("/config")
    @ResponseBody
    public String configTest(){

        StringBuffer sb = new StringBuffer();

        sb.append(port).append("----")
        .append(name).append("----")
        .append(age).append("----")
        .append(desc).append("----")
        .append(random).append("----").append("<br>");

                sb.append(applicationTest.getPort()).append("----")
                .append(applicationTest.getName()).append("----")
                .append(applicationTest.getAge()).append("----")
                .append(applicationTest.getDesc()).append("----")
                .append(applicationTest.getRandom()).append("----").append("<br>");

                return sb.toString();
    }

    /**
     * 127.0.0.1/test/testDesc?paramKey=shit --- get
     * 过滤操作paramKey
     */
    @GetMapping("/testDesc")
    @ResponseBody
    public String testDesc(HttpServletRequest request, @RequestParam("paramKey") String paramValue){
        String paramValue2 = request.getParameter("paramKey");
        return "This is test module desc===" + paramValue + "======" + paramValue2;
    }
}
