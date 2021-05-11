package cn.mn.mn.controller.restful;

import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author ermeng
 */
@CrossOrigin
@RestController
@RequestMapping(value = "/rest/user")
@Api(tags = "REST用户")
public class UserController{
}
