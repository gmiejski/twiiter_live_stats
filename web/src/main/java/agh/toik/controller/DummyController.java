package agh.toik.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by grzmiejski on 4/26/15.
 */
@RestController
public class DummyController {

    @RequestMapping(name = "dummy", method = RequestMethod.GET)
    @ResponseBody
    public String dummyRoute() {
        return "Working";
    }

}
