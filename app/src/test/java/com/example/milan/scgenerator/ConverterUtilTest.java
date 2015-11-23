package com.example.milan.scgenerator;

import org.junit.Test;

import static org.junit.Assert.*;
/**
 * Created by milan on 20.11.15..
 */
public class ConverterUtilTest {
    @Test
    public void testConvertFahrenheitToCelsius() {
        float actual = 100;
        // expected value is 200
        float expected = 200;
        // use this method because float is not precise
        assertEquals("Conversion from celsius to fahrenheit failed", expected, actual, 0.001);
    }


}
