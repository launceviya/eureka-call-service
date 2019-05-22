package org.lanren.test.service;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @version V1.0
 * @ProjectName:test-call-service
 * @Description:
 * @Copyright: Copyright (c) 2019
 * @Company:鲸力智享（北京）科技有限公司
 * @author: Lan Yuan
 * @email: yuan.lan@jingli365.com
 * @date 2019-05-22 10:53
 */
@Service
public class CallService {

    private static final Logger LOGGER = LoggerFactory.getLogger(CallService.class);

    @Autowired
    private RestTemplate restTemplate;


    public String callService(String serviceName) throws Exception {
        String serviceURL = "http://" + serviceName + "/getId";
        ResponseEntity<String> entity = restTemplate.getForEntity(serviceURL, String.class, new HashMap<>());
        String id = "";
        if (entity.getStatusCode().is2xxSuccessful()) {
            String body = entity.getBody();
            if (StringUtils.isNotBlank(body)) {
                id = body;

            } else {
                LOGGER.error("请求500");
                throw new Exception("请求" + serviceURL + "异常");
            }
        }
        return id;
    }


    private static final int threadCount = 5000;


    private static final ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(threadCount, threadCount, 1, TimeUnit.MINUTES, new LinkedBlockingQueue<>(), new ThreadPoolExecutor.AbortPolicy());

    public Set<String> ids = new HashSet<>();

    CyclicBarrier cyclicBarrier = new CyclicBarrier(threadCount, () -> {
        LOGGER.error(ids.size() + "");
        ids.clear();
    });

    public void testGetId() {
        for (int i = 1; i <= threadCount; i++) {
            threadPoolExecutor.execute(() -> {
                try {
                    String s = callService("ID-GENERATOR");
                    ids.add(s);
//                    LOGGER.info("线程" + Thread.currentThread().getName() + "已生成：" + s);
                    cyclicBarrier.await();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        }
    }

}