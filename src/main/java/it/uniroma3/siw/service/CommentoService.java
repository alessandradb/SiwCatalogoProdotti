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
import it.uniroma3.siw.repository.UserRepository;

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
	
	@Autowired 
	private ProdottoRepository prodottoRepository;
	
	@Autowired 
	private UserRepository userRepository;

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

	@Transactional
	public void saveCommento(@Valid Commento com, Prodotto prod, User user) {
		this.commentoRepository.save(com);
		prod.getCommenti().add(com);
		user.getCommenti().add(com);
		this.prodottoRepository.save(prod);
		this.userRepository.save(user);	
	}
	
	@Transactional
	public Commento findCommentoById(Long id) {
		return this.commentoRepository.findById(id).get();
	}
	
	@Transactional
	public void rimuoviCommento(Commento com) {
		this.commentoRepository.delete(com);
	}
	
	@Transactional
	public void saveCommento(Commento c) {
		this.commentoRepository.save(c);
	}
	
	
}
