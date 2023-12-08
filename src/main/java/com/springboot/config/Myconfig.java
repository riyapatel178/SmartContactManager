package com.springboot.config;

import org.springframework.context.annotation.Bean;
import static org.springframework.security.config.Customizer.withDefaults;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;


@Configuration
@EnableWebSecurity
public class Myconfig {
	
	@Bean
	public UserDetailsService getUserdetailservice() {
		return new UserdetailsServiceimp();
	}
	
	@Bean
	public BCryptPasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	@Bean
	public DaoAuthenticationProvider authenticationProvider() {
		DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
		provider.setUserDetailsService(this.getUserdetailservice());
		provider.setPasswordEncoder(passwordEncoder());
		return provider;
	}


	// Configure Method
	@Bean
	public AuthenticationManager authenticationmanagerbean(AuthenticationConfiguration configuration) throws Exception{
		return configuration.getAuthenticationManager();
	}
	 @Bean
	    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
	        http
	            .authorizeRequests()
	                    .requestMatchers("/admin/**").hasRole("ADMIN")
	                    .requestMatchers("/user/**").hasRole("USER")
	                    .requestMatchers("/**").permitAll().and()
	            .formLogin().loginPage("/signin").loginProcessingUrl("/dologin")
	            .defaultSuccessUrl("/user/dashboard")
	            .and()
	            .csrf().disable();
	           http.authenticationProvider(authenticationProvider());

	        return http.build();
	    }
		

}

