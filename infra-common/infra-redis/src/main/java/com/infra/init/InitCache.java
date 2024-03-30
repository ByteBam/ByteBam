package com.infra.init;

import com.infra.hook.SpringContextHook;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class InitCache implements CommandLineRunner {

    @Override
    public void run(String... args) throws Exception {
        //我要知道哪些缓存需要进行一个预热
        ApplicationContext applicationContext = SpringContextHook.getApplicationContext();
        Map<String, AbstractCache> beanMap = applicationContext.getBeansOfType(AbstractCache.class);
        //调用init方法
        if(beanMap.isEmpty()){
            return;
        }
        for(Map.Entry<String,AbstractCache> entry : beanMap.entrySet()){
            AbstractCache abstractCache = (AbstractCache) SpringContextHook.getBean(entry.getValue().getClass());
            abstractCache.initCache();
        }
    }

}
