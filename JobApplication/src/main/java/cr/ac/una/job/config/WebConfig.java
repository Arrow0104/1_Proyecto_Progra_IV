package cr.ac.una.job.config;

import cr.ac.una.job.auth.AuthInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.nio.file.Paths;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new AuthInterceptor());
    }


    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // Ruta absoluta de la carpeta de uploads
        String uploadPath = Paths.get("uploads/cv/").toAbsolutePath().toUri().toString();

        registry.addResourceHandler("/cv/**")
                // Primero busca PDFs empaquetados en el proyecto (demo), luego en uploads
                .addResourceLocations("classpath:/static/cv/", uploadPath);
    }
}