package it.uniroma3.siw.service;

import org.springframework.beans.factory.annotation.Autowired;



import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import it.uniroma3.siw.model.Prodotto;
import it.uniroma3.siw.repository.ProdottoRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

/**
 * The UserService handles logic for Users.
 */
@Service
public class ProdottoService {
	
	@Autowired 
	private ProdottoRepository prodottoRepository;

	
	public void saveProdotto(@Valid Prodotto prod) {
		this.prodottoRepository.save(prod);
		
	}

	public 
}
