package org.litespring.context.support;

import org.litespring.core.io.ClassPathResource;
import org.litespring.core.io.Resource;

public class ClassPathXmlApplicationContext extends AbstractApplicationContext{
    //调用父类的构造函数
    public ClassPathXmlApplicationContext(String configFile) {
        super( configFile );
    }

    @Override
    //返回一个ClassPathResource对象
    protected Resource getResourceByPath(String path) {
        return new ClassPathResource(path,this.getBeanClassLoader());
    }

}
