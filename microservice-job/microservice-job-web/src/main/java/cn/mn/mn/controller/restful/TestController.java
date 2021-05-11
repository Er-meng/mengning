package cn.mn.mn.controller.restful;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * @author ermeng
 */
@CrossOrigin
@RestController
@RequestMapping(value = "/rest/test")
@Api(tags = "REST测试")
public class TestController {
    /**
     * URL:http://{ip}:{port}/rest/test
     * method:GET
     *
     * @return
     */
    @GetMapping()
    @ApiOperation(value = "测试", notes = "测试")
    public ResponseEntity<?> test() {
        return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    }
}
