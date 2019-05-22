package org.lanren.test.controller;

import org.apache.commons.lang.StringUtils;
import org.lanren.test.service.CallService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

/**
 * @version V1.0
 * @ProjectName:test-call-service
 * @Description:
 * @Copyright: Copyright (c) 2019
 * @Company:鲸力智享（北京）科技有限公司
 * @author: Lan Yuan
 * @email: yuan.lan@jingli365.com
 * @date 2019-05-22 10:52
 */
@RestController
public class CallServiceController {
    @Autowired
    private CallService callService;

    @GetMapping("/getService/{serviceName}")
    public void getService(@PathVariable String serviceName) {
        if (StringUtils.isNotBlank(serviceName)) {
            callService.testGetId();
        }
    }
}
