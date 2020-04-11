package todo.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewResolverRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.resource.PathResourceResolver;
import org.thymeleaf.spring5.SpringTemplateEngine;
import org.thymeleaf.spring5.templateresolver.SpringResourceTemplateResolver;
import org.thymeleaf.spring5.view.ThymeleafViewResolver;

@EnableWebMvc
@Configuration
public class TodoWebConfig implements WebMvcConfigurer {

	private final ApplicationContext applicationContext;
	
    private static final String[] CLASSPATH_RESOURCE_LOCATIONS = {
    		"classpath:/META-INF/resources/",
    		"classpath:/META-INF/resources/webjars/", 
            "classpath:/resources/",
            "classpath:/templates/",
            "classpath:/swagger/",
            "classpath:/static/"
            };

	@Autowired
	public TodoWebConfig(ApplicationContext applicationContext) {
		this.applicationContext = applicationContext;
	}

	@Bean
	public SpringResourceTemplateResolver templateResolver() {
		SpringResourceTemplateResolver templateResolver = new SpringResourceTemplateResolver();
		templateResolver.setApplicationContext(applicationContext);
		templateResolver.setPrefix("classpath:templates/");
		templateResolver.setSuffix(".html");
		templateResolver.setCacheable(false);
		templateResolver.setTemplateMode("HTML5");
		return templateResolver;
	}

	@Bean
	public SpringTemplateEngine templateEngine() {
		SpringTemplateEngine templateEngine = new SpringTemplateEngine();
		templateEngine.setTemplateResolver(templateResolver());
		templateEngine.setEnableSpringELCompiler(true);
		return templateEngine;
	}

	@Override
	public void configureViewResolvers(ViewResolverRegistry registry) {
		ThymeleafViewResolver resolver = new ThymeleafViewResolver();
		resolver.setTemplateEngine(templateEngine());
		registry.viewResolver(resolver);
	}
	
	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		registry.addResourceHandler("/**")
        .addResourceLocations(CLASSPATH_RESOURCE_LOCATIONS)
        .setCachePeriod(1)
        .resourceChain(true)
        .addResolver(new PathResourceResolver());
	}
}
