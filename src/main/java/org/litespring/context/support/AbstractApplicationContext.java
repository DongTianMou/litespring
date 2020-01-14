package org.litespring.context.support;

import org.litespring.beans.factory.annotation.AutowiredAnnotationProcessor;
import org.litespring.beans.factory.config.ConfigurableBeanFactory;
import org.litespring.beans.factory.support.DefaultBeanFactory;
import org.litespring.beans.factory.xml.XmlBeanDefinitionReader;
import org.litespring.context.ApplicationContext;
import org.litespring.core.io.Resource;
import org.litespring.utils.ClassUtils;

public abstract class AbstractApplicationContext implements ApplicationContext {

    private DefaultBeanFactory factory = null;
    private ClassLoader beanClassLoader;

    //通过传入文件来实例化工厂
    public AbstractApplicationContext(String configFile){
        //实例化工厂
        factory = new DefaultBeanFactory();
        //将工厂注册到reader中，让read持有工厂属性
        XmlBeanDefinitionReader reader = new XmlBeanDefinitionReader(factory);
        //设置xml文件路径和classLoader
        Resource resource = this.getResourceByPath(configFile);
        //解析resource
        reader.loadBeanDefinitions(resource);
        factory.setBeanClassLoader(this.getBeanClassLoader());
        //创建processors
        registerBeanPostProcessors(factory);
    }

    protected void registerBeanPostProcessors(ConfigurableBeanFactory beanFactory) {
        AutowiredAnnotationProcessor postProcessor = new AutowiredAnnotationProcessor();
        postProcessor.setBeanFactory(beanFactory);
        beanFactory.addBeanPostProcessor(postProcessor);
    }

    public void setBeanClassLoader(ClassLoader beanClassLoader) {
        this.beanClassLoader = beanClassLoader;
    }
    //如果没有传入构造器，就用默认的
    public ClassLoader getBeanClassLoader() {
        return (this.beanClassLoader != null ? this.beanClassLoader : ClassUtils.getDefaultClassLoader());
    }

    public Object getBean(String beanID) {
        return factory.getBean(beanID);
    }

    protected abstract Resource getResourceByPath(String path);

}
