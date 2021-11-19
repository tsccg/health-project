package com.tsccg.jobs;

import java.util.Date;

/**
 * @Author: TSCCG
 * @Date: 2021/11/19 15:25
 * 自定义job
 */
public class JobDemo {
    public void run() {
        System.out.println("job execute..." + new Date());
    }
}
