package org.litespring.test.v4;

import org.junit.Assert;
import org.junit.Test;
import org.litespring.context.ApplicationContext;
import org.litespring.context.support.ClassPathXmlApplicationContext;
import org.litespring.service.v4.PetStoreService;

public class ApplicationContextTest4 {

	@Test
	public void testGetBeanProperty() {
		//把文件中bean或者注解都注入了容器中
		ApplicationContext ctx = new ClassPathXmlApplicationContext("petstore-v4.xml");
		//创建Bean
		PetStoreService petStore = (PetStoreService)ctx.getBean("petStore");
		
		Assert.assertNotNull(petStore.getAccountDao());
		Assert.assertNotNull(petStore.getItemDao());
		
	}	
}
