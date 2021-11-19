package com.tsccg.app;

import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * @Author: TSCCG
 * @Date: 2021/11/19 15:49
 */
public class App {
    public static void main(String[] args) {
        new ClassPathXmlApplicationContext("spring-jobs.xml");
    }
}
