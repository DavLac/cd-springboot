package dev.courses.springdemo.designpattern.strategy;

public class StrategyPatternDemo {
    public static void main(String[] args) {
        Calc calc = new Calc(new OperationAdd());
        System.out.println("10 + 5 = " + calc.executeStrategy(10, 5));

        calc = new Calc(new OperationSubstract());
        System.out.println("10 - 5 = " + calc.executeStrategy(10, 5));

        calc = new Calc(new OperationMultiply());
        System.out.println("10 * 5 = " + calc.executeStrategy(10, 5));
    }
}
