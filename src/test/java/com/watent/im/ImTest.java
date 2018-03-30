package com.watent.im;

import org.junit.Test;

/**
 * Test
 *
 * @author Dylan
 * @date 2018/3/29 17:05
 */
public class ImTest {

    @Test
    public void testM1() {

        byte[] arr = "1234567890".getBytes();
        String content = new String(arr, 0, 10);
        System.out.println(content);

    }

}
