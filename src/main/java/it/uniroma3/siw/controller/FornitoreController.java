package it.uniroma3.siw.controller;



import java.io.IOException;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import it.uniroma3.siw.controller.validation.FornitoreValidator;
import it.uniroma3.siw.model.Fornitore;
import it.uniroma3.siw.service.FornitoreService;



@Controller
public class FornitoreController {
	
	@Autowired
	private FornitoreService fornitoreService;
	
	@Autowired
	private FornitoreValidator fornitoreValidator;
	
	@GetMapping("/admin/formNewFornitore")
	public String formNewFornitore(Model model) {
		model.addAttribute("fornitore", new Fornitore());
		return "admin/formNewFornitore.html";
	}
	
	@PostMapping("/admin/newFornitore")
	public String newFornitore(@Valid @ModelAttribute("fornitore") Fornitore forn, BindingResult bindingResult, Model model) 
			throws IOException {
		
		this.fornitoreValidator.validate(forn, bindingResult);
		
		if (!bindingResult.hasErrors()) {
			this.fornitoreService.saveFornitore(forn);
			//this.prodottoService.newImagesCat(file, prod);

			model.addAttribute("fornitori", this.fornitoreService.allFornitori());
			return "admin/adminFornitori.html";
		}
		else {
			return "/admin/formNewFornitore";
		}
	}
	
	@GetMapping("/fornitori")
	public String getFornitori(Model model) {
		model.addAttribute("fornitori",this.fornitoreService.allFornitori());
		return "fornitori.html";
	}
	
	@GetMapping("/fornitore/{fornId}")
	public String getProdotto(@PathVariable("fornId") Long fornId, Model model) {
		Fornitore forn=this.fornitoreService.findFornitoreById(fornId);
		model.addAttribute("fornitore", forn);
		model.addAttribute("prodotti", forn.getProdotti());
		
		return "fornitore.html";
	}
	
	@PostMapping("/admin/updateNome/{fornId}")
	public String modificaNome(Model model, @RequestParam String nome,@PathVariable("fornId") Long fornId) {
		
		Fornitore forn = this.fornitoreService.findFornitoreById(fornId);
		forn.setNome(nome);
		this.fornitoreService.saveFornitore(forn);
		model.addAttribute("fornitore", forn);
		
		return "admin/adminFornitore.html";
	}
	
	@PostMapping("/admin/updateIndirizzo/{fornId}")
	public String modificaIndirizzo(Model model, @RequestParam String ind,@PathVariable("fornId") Long fornId) {
		
		Fornitore forn = this.fornitoreService.findFornitoreById(fornId);
		forn.setIndirizzo(ind);
		this.fornitoreService.saveFornitore(forn);
		model.addAttribute("fornitore", forn);
		
		return "admin/adminFornitore.html";
	}
	
	@PostMapping("/admin/updateNome/{fornId}")
	public String modificaEmail(Model model, @RequestParam String email,@PathVariable("fornId") Long fornId) {
		
		Fornitore forn = this.fornitoreService.findFornitoreById(fornId);
		forn.setEmail(email);
		this.fornitoreService.saveFornitore(forn);
		model.addAttribute("fornitore", forn);
		
		return "admin/adminFornitore.html";
	}
	
	@PostMapping("/cercaFornitoreNome")
	public String cercaFornitoreNome(Model model, @RequestParam String nome) {
		model.addAttribute("fornitori", this.fornitoreService.findFornitoreByNome(nome));
		return "prodotti.html";
	}

}
