package shop.freenanum.trade.lambda;

import lombok.RequiredArgsConstructor;

import java.util.function.BiFunction;

public class CalculatorLambda {
    @RequiredArgsConstructor
    enum Operation {
        PLUS("+", (x, y) -> (x + y)),
        MINUS("-", (x, y) -> (x - y)),
        MULTI("*", (x, y) -> (x * y)),
        DIVIDE("/", (x, y) -> (x / y)),
        ;

        private final String opcode;
        private final BiFunction<Integer, Integer, Integer> f;

        @Override
        public String toString() {
            return opcode;
        }

        public int apply(int a, int b) {
            return f.apply(a, b);
        }
    }


}
