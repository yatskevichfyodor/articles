package fyodor.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.ui.context.ThemeSource;
import org.springframework.ui.context.support.ResourceBundleThemeSource;
import org.springframework.web.servlet.ThemeResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.theme.CookieThemeResolver;
import org.springframework.web.servlet.theme.ThemeChangeInterceptor;

@Configuration
public class ThemesConfig extends WebMvcConfigurerAdapter {
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
    	ThemeChangeInterceptor themeInterceptor = new ThemeChangeInterceptor();
        themeInterceptor.setParamName("theme");
        registry.addInterceptor(themeInterceptor); 
    }
    
    @Bean
    public ThemeSource themeSource() {
    	ResourceBundleThemeSource source = new ResourceBundleThemeSource();
    	source.setBasenamePrefix("themes/theme-");
    	return source;
    }
    @Bean 
    public ThemeResolver themeResolver(){
    	CookieThemeResolver resolver = new CookieThemeResolver();
    	resolver.setCookieMaxAge(2400);
    	resolver.setCookieName("mythemecookie");
    	resolver.setDefaultThemeName("light");
    	return resolver;
    }
}