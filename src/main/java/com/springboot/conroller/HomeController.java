package com.springboot.conroller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.springboot.entities.User;
import com.springboot.helper.message;
import com.springboot.repository.UserRepository;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
@Controller
public class HomeController {
	@Autowired
	private UserRepository userepo;
	@Autowired
	private BCryptPasswordEncoder passwordencoder;
	@RequestMapping("/")
	public String home(Model m) {
		m.addAttribute("title", "Home- smart contact Manager");
		return "home";
	}
	
	@RequestMapping("/about")
	public String about(Model m) {
		m.addAttribute("title", "About- smart contact Manager");
		return "about";
	}
	@RequestMapping("/signup")
	public String signuo(Model m) {
		m.addAttribute("title", "Signup- smart contact Manager");
		m.addAttribute("user", new User());
		return "signup";
	}
	@RequestMapping(value="/register", method= RequestMethod.POST)
	public String register(@Valid @ModelAttribute("user") User user,BindingResult result,
			@RequestParam(value="agrement",defaultValue="false")boolean agrement,
			HttpSession session,Model m) {
		
		try {
			if(!agrement) {
				System.out.println("you have not agreed terms and conditions");
				throw new Exception("you have not agreed terms and conditions");
			}
			else if(result.hasErrors()) {
				 m.addAttribute("user", user);
		        System.out.println(result);
		        return "signup";
		    }
			else {
			user.setRole("ROLE_USER");
			user.setEnable(true);
			user.setImgUrl("default.png");
			user.setPassword(passwordencoder.encode(user.getPassword()));
			User result1 = this.userepo.save(user);
			System.out.println(result1);
			System.out.println(user);
			System.out.println("agrement" + agrement);
			m.addAttribute("user",new User());
			session.setAttribute("message", new message("Successfully registered..!!","alert-success"));
			return "signup";
			}
			
		}catch(Exception e) {
			e.printStackTrace();
			m.addAttribute("user", user);
			session.setAttribute("message", new message("Something went wrong..!!"+e.getMessage(),"alert-danger"));
			return "signup";
		}
		
		
		
	}
	@RequestMapping("/signin")
	public String login(Model m) {
		m.addAttribute("title", "Login- smart contact Manager");
		
		return "Login";
	}
	
	

}
