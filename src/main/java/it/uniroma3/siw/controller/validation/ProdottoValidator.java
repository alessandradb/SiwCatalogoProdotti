package it.uniroma3.siw.controller.validation;

import org.springframework.beans.factory.annotation.Autowired;


import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import it.uniroma3.siw.model.Prodotto;
import it.uniroma3.siw.repository.ProdottoRepository;




@Component
public class ProdottoValidator implements Validator {
	
	@Autowired 
	private ProdottoRepository prodottoRepository;
	
	@Override
	public void validate(Object o, Errors errors) {
		Prodotto prod = (Prodotto)o;
		if (prod.getNome()!=null && prod.getPrezzo()!=null && 
				this.prodottoRepository.existsByNomeAndPrezzo(prod.getNome(),prod.getPrezzo())) {
			errors.reject("prodotto.duplicate");
		}
	}
	@Override
	public boolean supports(Class<?> aClass) {
		return Prodotto.class.equals(aClass);
	}
	
}
