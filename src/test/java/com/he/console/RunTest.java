package com.he.console;

import cn.hutool.core.util.RandomUtil;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

/**
 * @author heyanjing
 * date:2021-04-10 2021/4/10:16:22
 */
@Slf4j
public class RunTest {

    @Test
    public void test()throws Exception {
        for (int i = 0; i < 100; i++) {
            log.info("{}", RandomUtil.randomInt(1, 3));
        }
    }
}