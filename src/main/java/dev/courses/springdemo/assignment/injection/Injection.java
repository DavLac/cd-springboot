package dev.courses.springdemo.assignment.injection;

import dev.courses.springdemo.assignment.injection.service.AppLogger;
import dev.courses.springdemo.assignment.injection.service.DateTimeService;
import dev.courses.springdemo.assignment.injection.service.OutputAggregator;
import dev.courses.springdemo.assignment.injection.service.TextFormatter;

public class Injection {

    public static void main(String[] args) {
        // call OutputAggregator.printInput("Hello world") method with an input
        OutputAggregator outputAggregator = new OutputAggregator(new AppLogger(), new TextFormatter(new DateTimeService()));
        outputAggregator.printInput("Hello world");
    }

}
