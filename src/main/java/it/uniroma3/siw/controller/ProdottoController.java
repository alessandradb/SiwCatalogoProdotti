package it.uniroma3.siw.controller;



import java.io.IOException;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import it.uniroma3.siw.controller.validation.ProdottoValidator;
import it.uniroma3.siw.model.Fornitore;
import it.uniroma3.siw.model.Image;
import it.uniroma3.siw.model.Prodotto;
import it.uniroma3.siw.service.CommentoService;
import it.uniroma3.siw.service.FornitoreService;
import it.uniroma3.siw.service.ImageService;
import it.uniroma3.siw.service.ProdottoService;



@Controller
public class ProdottoController {
	
	@Autowired
	private ProdottoService prodottoService;
	
	@Autowired
	private ProdottoValidator prodottoValidator;
	
	@Autowired
	private CommentoService commentoService;
	
	@Autowired
	private FornitoreService fornitoreService;
	
	@Autowired
	private ImageService imageService;
	
	@GetMapping("/admin/formNewProdotto")
	public String formNewProdotto(Model model) {
		model.addAttribute("prodotto", new Prodotto());
		return "admin/formNewProdotto.html";
	}
	
	@PostMapping("/admin/newProdotto")
	public String newProdotto(@Valid @ModelAttribute("prodotto") Prodotto prod, BindingResult bindingResult, 
			@RequestParam("file") MultipartFile[] file, Model model) throws IOException {
		
		this.prodottoValidator.validate(prod, bindingResult);
		
		if (!bindingResult.hasErrors()) {
			this.prodottoService.saveProdotto(prod);
			this.prodottoService.newImagesProd(file, prod);

			model.addAttribute("prodotti", this.prodottoService.allProdotti());
			return "admin/prodottiAdmin.html";
		}
		else {
			return "/admin/formNewProdotto";
		}
	}
	
	@GetMapping("/prodotti")
	public String getProdotti(Model model) {
		model.addAttribute("prodotti",this.prodottoService.allProdotti());
		return "prodotti.html";
	}
	
	@GetMapping("/admin/prodotti")
	public String getProdottiAdmin(Model model) {
		model.addAttribute("prodotti",this.prodottoService.allProdotti());
		return "admin/prodottiAdmin.html";
	}
	
	@GetMapping("/prodotto/{prodId}/{imageId}")
	public String getProdotto(@PathVariable("prodId") Long prodId,@PathVariable("imageId") Long imageId, Model model) {
		Prodotto prod=this.prodottoService.findProdottoById(prodId);
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		Image image=this.imageService.getImage(imageId);
		
		model.addAttribute("prodotto", prod);
		model.addAttribute("images", prod.getImages());
		model.addAttribute("image", image);
		model.addAttribute("commentiNotUser", this.commentoService.getCommentiNotUtente(authentication,prod));
		model.addAttribute("comUser", this.commentoService.getCommentoUser(authentication,prod));
		
		return "prodotto.html";
	}
	
	@GetMapping("/admin/prodotto/{prodId}/{imageId}")
	public String getProdottoAdmin(@PathVariable("prodId") Long prodId,@PathVariable("imageId") Long imageId, Model model) {
		Prodotto prod=this.prodottoService.findProdottoById(prodId);
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		Image image=this.imageService.getImage(imageId);
		
		model.addAttribute("prodotto", prod);
		model.addAttribute("images", prod.getImages());
		model.addAttribute("image", image);
		model.addAttribute("commentiNotUser", this.commentoService.getCommentiNotUtente(authentication,prod));
		model.addAttribute("comUser", this.commentoService.getCommentoUser(authentication,prod));
		
		return "admin/prodottoAdmin.html";
	}
	
	@GetMapping("/admin/eliminaProdotto/{prodId}")
	public String removeProdotto(@PathVariable("prodId") Long prodId,Model model) {
		Prodotto prod=this.prodottoService.findProdottoById(prodId);
		this.prodottoService.removeProdotto(prod);
		model.addAttribute("prodotti",this.prodottoService.allProdotti());
		return "admin/prodottiAdmin.html";
	}
	
	@GetMapping("/admin/updateProdotto/{prodId}/{imageId}")
	public String updateProdotto(@PathVariable("prodId") Long prodId,@PathVariable("imageId") Long imageId, Model model) {

		Image image = this.imageService.getImage(imageId);
		model.addAttribute("image",image);
		model.addAttribute("images",this.prodottoService.findProdottoById(prodId).getImages());
		List<Fornitore> notFornitori = this.fornitoreService.findFornitoriNotInProdotto(this.prodottoService.findProdottoById(prodId));
		model.addAttribute("notFornitori", notFornitori);
		model.addAttribute("prodotto", this.prodottoService.findProdottoById(prodId));

		return "admin/prodottoAdmin.html";
	}
	
	@PostMapping(value="/admin/addFornitoreToProdotto/{prodId}/{imageId}")
	public String addFornitoreToProdotto(@PathVariable("prodId") Long prodId,@PathVariable("imageId") Long imageId,@RequestParam String nomeF, Model model) {
		Prodotto prod = this.prodottoService.findProdottoById(prodId);
		Fornitore forn = this.fornitoreService.findFornitoreByNome(nomeF);
		List<Fornitore> fornitori = prod.getFornitori();
		if(!fornitori.contains(forn)) {
			fornitori.add(forn);
			forn.setProdotto(prod);
			this.prodottoService.saveProdotto(prod);
			this.fornitoreService.saveFornitore(forn);
		}
		Image image = this.imageService.getImage(imageId);
		model.addAttribute("image",image);
		model.addAttribute("images",this.prodottoService.findProdottoById(prodId).getImages());
		List<Fornitore> notFornitori = this.fornitoreService.findFornitoriNotInProdotto(prod); 
		
		model.addAttribute("prodotto", prod);
		model.addAttribute("notFornitori", notFornitori);

		return "admin/prodottoAdmin.html";
	}
	
	@PostMapping(value="/admin/removeFornitoreToProdotto/{prodId}/{imageId}")
	public String removeFornitoreToProdotto(@PathVariable("prodId") Long prodId,@PathVariable("imageId") Long imageId,@RequestParam String nomeF, Model model) {
		Prodotto prod = this.prodottoService.findProdottoById(prodId);
		Fornitore forn = this.fornitoreService.findFornitoreByNome(nomeF);
		List<Fornitore> fornitori = prod.getFornitori();
		if(fornitori.contains(forn)) {
			fornitori.remove(forn);
			forn.getProdotti().remove(prod);
			this.prodottoService.saveProdotto(prod);
			this.fornitoreService.saveFornitore(forn);
		}
		Image image = this.imageService.getImage(imageId);
		model.addAttribute("image",image);
		model.addAttribute("images",this.prodottoService.findProdottoById(prodId).getImages());
		List<Fornitore> notFornitori = this.fornitoreService.findFornitoriNotInProdotto(prod);
		
		model.addAttribute("prodotto", prod);
		model.addAttribute("notFornitori", notFornitori);

		return "admin/prodottoAdmin.html";
	}
	
	@PostMapping("/admin/updateNome/{prodId}/{imageId}")
	public String modificaNome(Model model, @RequestParam String nome,@PathVariable("prodId") Long prodId, 
			@PathVariable("imageId") Long imageId) {
		
		Prodotto prod = this.prodottoService.findProdottoById(prodId);
		
		prod.setNome(nome);
		this.prodottoService.saveProdotto(prod);

		Image image = this.imageService.getImage(imageId);
		model.addAttribute("image",image);
		model.addAttribute("images",this.prodottoService.findProdottoById(prodId).getImages());
		List<Fornitore> notFornitori = this.fornitoreService.findFornitoriNotInProdotto(prod);
		
		model.addAttribute("prodotto", prod);
		model.addAttribute("notFornitori", notFornitori);
		
		return "admin/prodottoAdmin.html";
	}
	
	@PostMapping("/admin/updatePrezzo/{prodId}/{imageId}")
	public String modificaPrezzo(Model model, @RequestParam Float prezzo,@PathVariable("prodId") Long prodId, 
			@PathVariable("imageId") Long imageId) {
		
		Prodotto prod = this.prodottoService.findProdottoById(prodId);
		
		prod.setPrezzo(prezzo);
		this.prodottoService.saveProdotto(prod);

		Image image = this.imageService.getImage(imageId);
		model.addAttribute("image",image);
		model.addAttribute("images",this.prodottoService.findProdottoById(prodId).getImages());
		List<Fornitore> notFornitori = this.fornitoreService.findFornitoriNotInProdotto(prod);
		
		model.addAttribute("prodotto", prod);
		model.addAttribute("notFornitori", notFornitori);
		
		return "admin/prodottoAdmin.html";
	}
	
	@PostMapping("/admin/updateDescrizione/{prodId}/{imageId}")
	public String modificaDescrizione(Model model, @RequestParam String desc,@PathVariable("prodId") Long prodId, 
			@PathVariable("imageId") Long imageId) {
		
		Prodotto prod = this.prodottoService.findProdottoById(prodId);
		
		prod.setDescrizione(desc);
		this.prodottoService.saveProdotto(prod);

		Image image = this.imageService.getImage(imageId);
		model.addAttribute("image",image);
		model.addAttribute("images",this.prodottoService.findProdottoById(prodId).getImages());
		List<Fornitore> notFornitori = this.fornitoreService.findFornitoriNotInProdotto(prod);
		
		model.addAttribute("prodotto", prod);
		model.addAttribute("notFornitori", notFornitori);
		
		return "admin/prodottoAdmin.html";
	}
	
	@PostMapping("/cercaProdottiNome")
	public String cercaProdottiNome(Model model, @RequestParam String nome) {
		model.addAttribute("prodotti", this.prodottoService.findProdottoByNome(nome));
		return "prodotti.html";
	}
	
	@PostMapping("/cercaProdottiFornitore")
	public String cercaProdottiFornitore(Model model, @RequestParam String nome) {
		model.addAttribute("prodotti", this.prodottoService.findProdottoByFornitore(nome));
		return "prodotti.html";
	}
	
	@PostMapping("/cercaProdottiPrezzo")
	public String cercaProdottiPrezzo(Model model, @RequestParam Float prezzo) {
		model.addAttribute("prodotti", this.prodottoService.findProdottoByPrezzo(prezzo));
		return "prodotti.html";
	}
	
	@PostMapping("/cercaProdotti")
	public String cercaProdottiNomeOFornitore(Model model, @RequestParam String param,@RequestParam Float prezzo) {
		if(param==null)
			model.addAttribute("prodotti", this.prodottoService.findProdottoByPrezzo(prezzo));
		if(prezzo==null)
			model.addAttribute("prodotti", this.prodottoService.findProdottoByNomeOFornitore(param));
		if(param!=null&&prezzo!=null)
			model.addAttribute("prodotti", this.prodottoService.findProdotto(param,prezzo));
		return "prodotti.html";
	}
	
	@PostMapping("/cercaProdottiNomeOFornitore")
	public String cercaProdotti(Model model, @RequestParam String param) {
		model.addAttribute("prodotti", this.prodottoService.findProdottoByNomeOFornitore(param));
		return "prodotti.html";
	}
	
	@GetMapping(value="/admin/removeImage/{prodId}/{imageId}")
	public String removeImage(@PathVariable("prodId") Long prodId, @PathVariable("imageId") Long imageId, Model model) {
		Prodotto prod = this.prodottoService.findProdottoById(prodId);
		Image image = this.imageService.getImage(imageId);
		if(prod!=null && image!=null) {
			prod.getImages().remove(image);
			this.prodottoService.saveProdotto(prod);
			this.imageService.deleteImage(image);
			return "redirect:/admin/UpdateProdotto/" + prod.getId()+"/"+image.getId();
		} 
		else {
			return "admin/prodottoAdmin.html";
		}
	}
	
	@PostMapping(value="/admin/addImage/{prodId}/{imageId}")
	public String addImage(@PathVariable("prodId") Long prodId, @PathVariable("imageId") Long imageId,
			@RequestParam("file") MultipartFile[] file, Model model) throws IOException {
		
		Prodotto prod = this.prodottoService.findProdottoById(prodId);
		this.prodottoService.newImagesProd(file, prod);
		
		Image image = this.imageService.getImage(imageId);
		if(prod!=null && image!=null) {
			return "redirect:/admin/UpdateProdotto/" + prod.getId()+"/"+imageId;
		} 
		else {
			return "admin/prodottoAdmin.html";
		}
	}

}
