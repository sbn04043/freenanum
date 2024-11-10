package shop.freenanum.trade.stream;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class HelloStream {
    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Hello {
        private String greeting, inLanguage;

        @Override
        public String toString() {
            return String.format("인사 : %s, %s", inLanguage, greeting);
        }
    }

    interface HelloService {
        Set<Hello> great(List<Hello> arr);
    }

    static class HelloServiceImpl implements HelloService {
        @Override
        public Set<Hello> great(List<Hello> arr) {

            return arr
                    .stream()
                    .filter(e -> e.getInLanguage().equals("영어"))
                    .collect(Collectors.toSet());

        }
    }
}
