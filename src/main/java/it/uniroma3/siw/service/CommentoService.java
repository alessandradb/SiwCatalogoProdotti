package it.uniroma3.siw.service;

import org.springframework.beans.factory.annotation.Autowired;



import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.uniroma3.siw.model.Commento;
import it.uniroma3.siw.model.Prodotto;
import it.uniroma3.siw.model.User;
import it.uniroma3.siw.repository.CommentoRepository;
import it.uniroma3.siw.repository.ProdottoRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

/**
 * The UserService handles logic for Users.
 */
@Service
public class CommentoService {
	
	@Autowired 
	private CommentoRepository commentoRepository;
	
	@Autowired
	private CredentialsService credentialsService;

	@Transactional
	public List<Commento> getCommentiNotUtente(Authentication auth, Prodotto prod) {
		User user=this.credentialsService.getUser(auth);
		return this.commentoRepository.findByProdottoAndUserNot(prod,user);
	}
	
	@Transactional
	public Commento getCommentoUser(Authentication auth, Prodotto prod) {
		User user=this.credentialsService.getUser(auth);
		return this.commentoRepository.findByProdottoAndUser(prod, user);
	}
	
	
}
