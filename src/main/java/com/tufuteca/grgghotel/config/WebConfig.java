package com.tufuteca.grgghotel.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/photos/rooms/**")
                .addResourceLocations("file:upload-dir/photos/rooms/");
        registry.addResourceHandler("/photos/bookingTypes/**")
                .addResourceLocations("file:upload-dir/photos/bookingTypes/");
        registry.addResourceHandler("/photos/slider/**")
                .addResourceLocations("file:upload-dir/photos/slider/");
    }
}