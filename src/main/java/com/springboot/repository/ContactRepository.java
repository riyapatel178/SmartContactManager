package com.springboot.repository;



import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.springboot.entities.Contact;

public interface ContactRepository extends JpaRepository<Contact, Integer>{
	
	//pegination
	// current - page
	//contact per page -5
	@Query("from Contact as c where c.user.id = :userid")
	public Page<Contact> findcontactsbyuser(@Param("userid")int userid, Pageable pageable);
	

}
