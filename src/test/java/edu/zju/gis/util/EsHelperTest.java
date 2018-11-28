package edu.zju.gis.util;

import org.junit.Test;

/**
 * @author yanlong_lee@qq.com
 * @version 1.0 2018/11/26
 */
public class EsHelperTest {
    @Test
    public void testESHealth() {
        ElasticSearchHelper helper = ElasticSearchHelper.getInstance();
        System.out.println(helper.getClusterHealth().toString());
    }
}
