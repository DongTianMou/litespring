package org.litespring.test.v1;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.litespring.beans.BeanDefinition;
import org.litespring.beans.factory.BeanCreationException;
import org.litespring.beans.factory.BeanDefinitionStoreException;
import org.litespring.beans.factory.BeanFactory;
import org.litespring.beans.factory.support.DefaultBeanFactory;
import org.litespring.beans.factory.xml.XmlBeanDefinitionReader;
import org.litespring.core.io.ClassPathResource;
import org.litespring.service.v1.PetStoreService;

import static org.junit.Assert.*;
import static org.junit.Assert.assertFalse;

public class BeanFactoryTest {
    /*
    给定一个xml配置的文件（内含bean的定义），能够从中获取：
    1.Bean的定义
    2.Bean 的实例
    */
//    @Test
////    public void testGetBean() {
////        //BeanFactory是接口，DefaultBeanFactory是实现类，负责读取配置文件
////        BeanFactory factory = new DefaultBeanFactory( "petstore-v1.xml");
////        //1.Bean的定义
////        BeanDefinition bd = factory.getBeanDefinition("petStore");
////        //判断获取的定义是否为我们所想要的
////        assertEquals("org.litespring.service.v1.PetStoreService",bd.getBeanClassName());
////        //2.Bean 的实例
////        PetStoreService petStore = (PetStoreService)factory.getBean("petStore");
////        //判断一下
////        assertNotNull(petStore);
////    }

    DefaultBeanFactory factory = null;
    XmlBeanDefinitionReader reader = null;

    @Before
    public void setUp(){
        factory = new DefaultBeanFactory();
        reader = new XmlBeanDefinitionReader(factory);

    }

    //修改，上述代码不符合单一职责原则
    @Test
    public void testGetBean() {
        //解析xml
        reader.loadBeanDefinitions(new ClassPathResource( "petstore-v1.xml" ) );
        //1.Bean的定义
        BeanDefinition bd = factory.getBeanDefinition("petStore");

        assertTrue(bd.isSingleton());

        assertFalse(bd.isPrototype());

        //判断获取的定义是否为我们所想要的
        assertEquals("org.litespring.service.v1.PetStoreService",bd.getBeanClassName());
        //2.Bean 的实例
        PetStoreService petStore = (PetStoreService)factory.getBean("petStore");
        //判断一下
        assertNotNull(petStore);

        PetStoreService petStore1 = (PetStoreService)factory.getBean("petStore");

        assertTrue(petStore.equals(petStore1));
    }

    @Test
    public void testInvalidBean(){

        reader.loadBeanDefinitions(new ClassPathResource("petstore-v1.xml"));
        try{
            factory.getBean("invalidBean");
        }catch(BeanCreationException e){
            return;
        }
        Assert.fail("expect BeanCreationException ");
    }
    @Test
    public void testInvalidXML(){
        try{
            reader.loadBeanDefinitions(new ClassPathResource("xxxx.xml"));
        }catch(BeanDefinitionStoreException e){
            return;
        }
        Assert.fail("expect BeanDefinitionStoreException ");
    }

}
