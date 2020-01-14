package org.litespring.test.v4;

import org.junit.Assert;
import org.junit.Test;
import org.litespring.beans.factory.config.DependencyDescriptor;
import org.litespring.beans.factory.support.DefaultBeanFactory;
import org.litespring.beans.factory.xml.XmlBeanDefinitionReader;
import org.litespring.core.io.ClassPathResource;
import org.litespring.core.io.Resource;
import org.litespring.dao.v4.AccountDao;
import org.litespring.service.v4.PetStoreService;

import java.lang.reflect.Field;

public class DependencyDescriptorTest {

	@Test
	public void testResolveDependency() throws Exception{
		
		DefaultBeanFactory factory = new DefaultBeanFactory();
		XmlBeanDefinitionReader reader = new XmlBeanDefinitionReader(factory);
		Resource resource = new ClassPathResource("petstore-v4.xml");
		reader.loadBeanDefinitions(resource);
		//通过类去拿到字段
		Field f = PetStoreService.class.getDeclaredField("accountDao");
		//调用构造函数
		DependencyDescriptor  descriptor = new DependencyDescriptor(f,true);
		//工厂调用resolveDependency(descriptor)对比type.如果type 是AccountDao,则找到（或者创建）对应的实例，并且返回
		Object o = factory.resolveDependency(descriptor);
		Assert.assertTrue(o instanceof AccountDao);
	}

}
