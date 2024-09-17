package com.yaroslav.dragontmsbackend.config;

import com.yaroslav.dragontmsbackend.filter.HttpsEnforcementFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FilterConfig {


    @Bean
    public FilterRegistrationBean<HttpsEnforcementFilter> httpsEnforcementFilter() {
        FilterRegistrationBean<HttpsEnforcementFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(new HttpsEnforcementFilter());
        registrationBean.addUrlPatterns("/*"); // Применяем фильтр ко всем URL
        return registrationBean;
    }

}
