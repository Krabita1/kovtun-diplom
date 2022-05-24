package com.chdtu.stanislav.controller;

import com.chdtu.stanislav.domain.User;
import com.chdtu.stanislav.service.ShoppingCartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

@ControllerAdvice
public class GlobalControllerAdvice {
	
	public static final String DEFAULT_ERROR_VIEW = "error";
		
	@Autowired
	private ShoppingCartService shoppingCartService;

	@InitBinder
	public void initBinder(WebDataBinder dataBinder) {
		StringTrimmerEditor stringTrimmerEditor = new StringTrimmerEditor(true);
		dataBinder.registerCustomEditor(String.class, stringTrimmerEditor);
	}	
	
	@ModelAttribute
	public void populateModel(Model model) {	
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (auth != null && auth.isAuthenticated() && !(auth instanceof AnonymousAuthenticationToken)) {				
			User user =  (User) auth.getPrincipal(); 
			if (user != null) {
				model.addAttribute("shoppingCartItemNumber", shoppingCartService.getItemsNumber(user) );
			}
		} else { 
			model.addAttribute("shoppingCartItemNumber", 0);
		} 
	}
	
	@ExceptionHandler(value = Exception.class)
	public ModelAndView defaultErrorHandler(HttpServletRequest req, Exception e) throws Exception {
		if (AnnotationUtils.findAnnotation(e.getClass(), ResponseStatus.class) != null)
			throw e;		
		ModelAndView mav = new ModelAndView();
		mav.addObject("timestamp", new Date(System.currentTimeMillis()));
		mav.addObject("path", req.getRequestURL());
		mav.addObject("message", e.getMessage());
		mav.setViewName(DEFAULT_ERROR_VIEW);
		return mav;
	}
	
	
}
