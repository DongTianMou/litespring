package org.litespring.test.v4;

import org.junit.Assert;
import org.junit.Test;
import org.litespring.core.io.Resource;
import org.litespring.core.io.support.PackageResourceLoader;

import java.io.IOException;

public class PackageResourceLoaderTest {
    /*给定包路径，把路径转化为resource*/
    @Test
    public void testGetResources() throws IOException {
        //新建一个PackageResourceLoader。它有一个getResources（String url）方法，把路径转化为resource数组
        PackageResourceLoader loader = new PackageResourceLoader();
        Resource[] resources = loader.getResources("org.litespring.dao.v4");
        Assert.assertEquals(2, resources.length);
    }

}