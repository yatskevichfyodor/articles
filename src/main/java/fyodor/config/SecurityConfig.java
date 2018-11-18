package fyodor.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.BeanIds;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
//@Order(1)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private PasswordEncoder passwordEncoder;

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.authorizeRequests()
				.antMatchers("/admin", "/admin/**", "/sql-terminal", "/sql-request",
						"/categoryManagement", "category/add", "category/delete",
						"/userAttribute/add", "/userAttribute/delete").hasRole("ADMIN")

				.antMatchers("/profile", "/article/add", "/article/delete", "/article/changeRating",
						"/article/edit/**", "/article/edit",
						"/commentAdd", "/comment/delete", "/comment/edit",
						"/userParam/edit").authenticated()

                .antMatchers("/testtest", "/login").permitAll()

				.anyRequest().permitAll()
		.and()
				.logout().logoutSuccessUrl("/index.html").permitAll()
		.and()
				.sessionManagement().maximumSessions(1).sessionRegistry(sessionRegistry());
	}

	@Bean
	public SessionRegistry sessionRegistry() {
		return new SessionRegistryImpl();
	}

	@Bean(name = BeanIds.AUTHENTICATION_MANAGER)
	@Override
	public AuthenticationManager authenticationManagerBean() throws Exception {
    	return super.authenticationManagerBean();
	}

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(11);
    }
}