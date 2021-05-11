package cn.mn.mn.controller.ms;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;

/**
 * @author ermeng
 */
@Component
@FeignClient(name = "microservice-base", configuration = FeignConfiguration.class)
public interface ServiceBaseFeignClient {
}
