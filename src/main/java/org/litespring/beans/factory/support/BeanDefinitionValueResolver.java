package org.litespring.beans.factory.support;

import org.litespring.beans.factory.config.RuntimeBeanReference;
import org.litespring.beans.factory.config.TypedStringValue;
//类型转换
public class BeanDefinitionValueResolver {
	private final DefaultBeanFactory beanFactory;
	
	public BeanDefinitionValueResolver(DefaultBeanFactory beanFactory) {
		this.beanFactory = beanFactory;
	}
	
	public Object resolveValueIfNecessary(Object value) {
		//类型判断
		if (value instanceof RuntimeBeanReference) {
			RuntimeBeanReference ref = (RuntimeBeanReference) value;
			String refName = ref.getBeanName();
			//拿到beanFactory调用getBean(refName)生成bean
			Object bean = this.beanFactory.getBean(refName);
			return bean;
			
		}else if (value instanceof TypedStringValue) {
			return ((TypedStringValue) value).getValue();
		} else{
			//TODO
			throw new RuntimeException("the value " + value +" has not implemented");
		}		
	}
}
