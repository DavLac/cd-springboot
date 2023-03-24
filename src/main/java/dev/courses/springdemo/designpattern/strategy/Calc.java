package dev.courses.springdemo.designpattern.strategy;

public class Calc {
    private Strategy strategy;

    public Calc(Strategy strategy){
        this.strategy = strategy;
    }

    public int executeStrategy(int num1, int num2){
        return strategy.doOperation(num1, num2);
    }
}
