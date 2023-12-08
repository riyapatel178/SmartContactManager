package com.springboot.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.springboot.entities.User;
import com.springboot.repository.UserRepository;

public class UserdetailsServiceimp implements UserDetailsService {
	@Autowired
	private UserRepository UserRepository;

	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		// fetching user from data base
		
		User user = UserRepository.getuserbyemail(email);
		if(user==null) {
			throw new UsernameNotFoundException("user cannot found");
		}
		CustomUserDetails CustomUserDetails = new CustomUserDetails(user);
		
		
		return CustomUserDetails;
	}

}
