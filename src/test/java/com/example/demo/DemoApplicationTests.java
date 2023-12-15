package com.example.demo;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.support.DefaultConversionService;

@SpringBootTest
class DemoApplicationTests {

    @Test
    void contextLoads() {
        Long i = 1L;

        ConversionService conversion = DefaultConversionService.getSharedInstance();
        Integer convert = conversion.convert(i, Integer.TYPE);
        System.out.println(i.getClass());
    }

}
