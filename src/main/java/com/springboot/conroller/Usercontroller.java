package com.springboot.conroller;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.springboot.entities.Contact;
import com.springboot.entities.User;
import com.springboot.helper.message;
import com.springboot.repository.ContactRepository;
import com.springboot.repository.UserRepository;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/user")
public class Usercontroller {
	
	@Autowired
	private UserRepository userepo;
	@Autowired
	private ContactRepository contactrepo;
	
	@ModelAttribute
	public void addcommandata(Model m,Principal principal) {
		String username = principal.getName();
		System.out.println(username);
		// get user detials using this username-email
		m.addAttribute("title", "DashBoard- smart contact Manager");
		User user = userepo.getuserbyemail(username);
		m.addAttribute("user", user);
		
	}
	
	@RequestMapping("/dashboard")
	public String dashboard(Model m,Principal principal) {
	
		return "normal/user_dashboard";
		
	}
	
	@RequestMapping("/addcontact")
	public String addcontacthandler(Model m) {
		
		m.addAttribute("title", "Add Contact- smart contact Manager");
		m.addAttribute("contact", new Contact());
		
		return "normal/addcontact";
		
	}
	
	@RequestMapping("/processcontact")
	public String processcontactdetails(@ModelAttribute("contact") Contact contact,
			@RequestParam("profileimg") MultipartFile file,HttpSession session,Model m,Principal principal) {
		try {
		System.out.println(contact);
		String name = principal.getName();
		User user = this.userepo.getuserbyemail(name);
		
		// processing and uploading file...
		if(file.isEmpty()) {
			System.out.println("file is empty");
		}else {
			// upload file to the folder and update the name into contact..
			String filename = file.getOriginalFilename();
			contact.setImgurl(filename);
			File savefile = new ClassPathResource("static/img").getFile();
			Path filepath = Paths.get(savefile.getAbsolutePath()+File.separator+file.getOriginalFilename());
			Files.copy(file.getInputStream(), filepath, StandardCopyOption.REPLACE_EXISTING);
			System.out.println("image uploaded");
		}
		contact.setUser(user);
		user.getContact().add(contact);
		this.userepo.save(user);
		System.out.println("done");
		m.addAttribute("session", session);
		session.setAttribute("message", new message("Contact is Saved..!!","alert-success"));
		return "normal/addcontact";
		}catch(Exception e) {
			e.printStackTrace();
			m.addAttribute("session", session);
			session.setAttribute("message", new message("Something went wrong..!!"+e.getMessage(),"alert-danger"));
			return "normal/addcontact";
			
		}
		
	}
	
	@RequestMapping("/showcontact/{page}")
	public String showcontact(Model m,Principal principal,@PathVariable("page") Integer page) {
		m.addAttribute("title", "Show Contact- smart contact Manager");
		String name = principal.getName();
		User user = this.userepo.getuserbyemail(name);
		int userid = user.getId();
		//pegination
		// current - page
		//contact per page -5
		Pageable pageable = PageRequest.of(page, 5);
		Page<Contact> contacts = this.contactrepo.findcontactsbyuser(userid,pageable);
		m.addAttribute("contacts", contacts);
		m.addAttribute("currentpage", page);
		m.addAttribute("totalpage", contacts.getTotalPages());
		// per page = 5[n]
		// current page = 0 [page]
		return "normal/showcontact";
	}
}
