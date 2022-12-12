package dev.courses.springdemo.assignment.injection.service;

import org.springframework.stereotype.Component;

@Component
public class AppLogger {

    public void printLog(String log) {
        System.out.printf("I'm the Logger, I'm printing = %s", log);
    }

}
