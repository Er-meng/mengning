package cn.mn.mn;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * 依赖注入是按照包名逐级向下查找，所以依赖注入的类要在这个类的同级或下级包中才可以被注入
 * @author ermeng
 */
@SpringBootApplication
@ServletComponentScan
@EnableScheduling
@EnableEurekaClient
@EnableFeignClients
@EnableHystrix
public class JobWebApplication extends SpringBootServletInitializer {

    public static void main(String[] args) {
        SpringApplication.run(JobWebApplication.class, args);
    }

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(JobWebApplication.class);
    }

}
