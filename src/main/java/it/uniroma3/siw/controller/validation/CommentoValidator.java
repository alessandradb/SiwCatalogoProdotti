package it.uniroma3.siw.controller.validation;

import org.springframework.beans.factory.annotation.Autowired;


import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import it.uniroma3.siw.model.Commento;
import it.uniroma3.siw.repository.CommentoRepository;



@Component
public class CommentoValidator implements Validator {

	@Autowired 
	private CommentoRepository commentoRepository;
	
	@Override
	public void validate(Object o, Errors errors) {
		Commento com = (Commento)o;
		if (com.getProdotto()!=null && com.getUser()!=null && 
				this.commentoRepository.existsByProdottoAndUser(com.getProdotto(),com.getUser())) {
			errors.reject("commento.duplicate");
		}
	}
	@Override
	public boolean supports(Class<?> aClass) {
		return Commento.class.equals(aClass);
	}
	
}
