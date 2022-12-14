package com.example.Spring_boot_demo.assignment.injection;

import com.example.Spring_boot_demo.assignment.injection.service.AppLogger;
import com.example.Spring_boot_demo.assignment.injection.service.DateTimeService;
import com.example.Spring_boot_demo.assignment.injection.service.OutputAggregator;
import com.example.Spring_boot_demo.assignment.injection.service.TextFormatter;

public class Injection {


    public static void main(String[] args) {
        // call OutputAggregator.printInput("Hello world") method with an input
        OutputAggregator outputAggregator1 = new OutputAggregator(new AppLogger(), new TextFormatter(new DateTimeService()));
        outputAggregator1.printInput("hello world");
    }

}
