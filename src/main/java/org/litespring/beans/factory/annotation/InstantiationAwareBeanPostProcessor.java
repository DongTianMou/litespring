package org.litespring.beans.factory.annotation;

import org.litespring.beans.BeansException;
import org.litespring.beans.factory.config.BeanPostProcessor;

public interface InstantiationAwareBeanPostProcessor extends BeanPostProcessor {

	Object beforeInstantiation(Class<?> beanClass, String beanName) throws BeansException;

	boolean afterInstantiation(Object bean, String beanName) throws BeansException;

	void postProcessPropertyValues(Object bean, String beanName)
			throws BeansException;

}
