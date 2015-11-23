package com.example.milan.scgenerator;

import com.example.milan.scgenerator.generators.Generator;
import com.example.milan.scgenerator.generators.VideoGenerator;

import org.junit.Test;

import static org.junit.Assert.*;


/**
 * Created by milan on 20.11.15..
 */
public class MyUnitTest {
    @Test
    public void test(){
        float actual = 100;
        float expected = 100;


        assertEquals("Conversion from celsius to fahrenheit failed", expected,
                actual, 0.001);
    }

}
