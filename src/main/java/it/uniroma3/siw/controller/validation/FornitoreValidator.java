package it.uniroma3.siw.controller.validation;

import org.springframework.beans.factory.annotation.Autowired;


import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import it.uniroma3.siw.model.Fornitore;
import it.uniroma3.siw.repository.FornitoreRepository;



@Component
public class FornitoreValidator implements Validator {

	@Autowired 
	private FornitoreRepository fornitoreRepository;
	
	@Override
	public void validate(Object o, Errors errors) {
		Fornitore forn = (Fornitore)o;
		if (forn.getNome()!=null && forn.getEmail()!=null && 
				this.fornitoreRepository.existsByNomeAndEmail(forn.getNome(),forn.getEmail())) {
			errors.reject("fornitore.duplicate");
		}
	}
	@Override
	public boolean supports(Class<?> aClass) {
		return Fornitore.class.equals(aClass);
	}
	
}
