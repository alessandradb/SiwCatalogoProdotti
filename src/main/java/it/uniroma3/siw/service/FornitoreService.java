package it.uniroma3.siw.service;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.uniroma3.siw.model.Fornitore;
import it.uniroma3.siw.model.Prodotto;
import it.uniroma3.siw.repository.FornitoreRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * The UserService handles logic for Users.
 */
@Service
public class FornitoreService {

	@Autowired
	private FornitoreRepository fornitoreRepository;
	
	@Transactional
	public Fornitore findFornitoreByNome(String nome) {
		return this.fornitoreRepository.findByNome(nome);
	}
	
	@Transactional
	public void saveFornitore(Fornitore f) {
		this.fornitoreRepository.save(f);
	}
	
	@Transactional
	public List<Fornitore> findFornitoriNotInProdotto(Prodotto p){
		return this.fornitoreRepository.findByProdottiNot(p);
	}
   
}
