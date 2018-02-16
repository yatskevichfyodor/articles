package fyodor.config;

import fyodor.service.CustomUserDetailsService;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableAutoConfiguration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(11);
    }

	@Override
	public UserDetailsService userDetailsService() {
		return new CustomUserDetailsService();
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.authorizeRequests()
			.antMatchers("/**").permitAll()
				// .antMatchers("/admin").hasRole("ADMIN")
				// .anyRequest().authenticated()
//			.antMatchers("/user-profile", "/update-user", "/update-password", "/logout").authenticated()
//		.and()
//			.formLogin()
//			.loginPage("/login")
//			.failureUrl("/login?error")
//			.permitAll()
		.and()
			.logout().logoutSuccessUrl("/index.html").permitAll();
//		.and()
//			.requiresChannel()
//				.antMatchers("/reg").requiresSecure();
		
//		http.exceptionHandling().accessDeniedPage("/403");
	}
}