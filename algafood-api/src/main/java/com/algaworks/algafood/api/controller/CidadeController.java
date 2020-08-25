package com.algaworks.algafood.api.controller;

import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import com.algaworks.algafood.domain.exeption.EntidadeEmUsoExeption;
import com.algaworks.algafood.domain.exeption.EntidadeNaoEncontradaException;
import com.algaworks.algafood.domain.model.Cidade;
import com.algaworks.algafood.domain.model.Restaurante;
import com.algaworks.algafood.domain.service.CadastroCidadeService;

@RestController
@RequestMapping("/cidades")
public class CidadeController {

	@Autowired
	private CadastroCidadeService cadastroCidadeService;
	
	@GetMapping
	public List<Cidade> listar() {
		return cadastroCidadeService.listar();
	}

	@GetMapping(value = "/{cidadeId}")
	public ResponseEntity<Cidade> buscar(@PathVariable Long cidadeId) {
		Cidade cidade = cadastroCidadeService.buscar(cidadeId);

		if (cidade != null) {
			return ResponseEntity.ok(cidade);
		}
		return ResponseEntity.notFound().build();
	}

	@PostMapping
	public ResponseEntity<?> salvar(@RequestBody Cidade cidade) {
		try {
			cidade = cadastroCidadeService.salvar(cidade);
			return ResponseEntity.status(HttpStatus.CREATED).body(cidade);
		} catch (EntidadeNaoEncontradaException e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}

	@PutMapping("/{cidadeId}")
	public ResponseEntity<?> atualizar(@PathVariable Long cidadeId,
			@RequestBody Cidade cidade) {
		try {
			Cidade cidadeAtual = cadastroCidadeService.buscar(cidadeId);
			if (cidadeAtual != null) {
//				restauranteAtual.setNome(restaurante.getNome());
				BeanUtils.copyProperties(cidade, cidadeAtual, "id");
				cadastroCidadeService.salvar(cidadeAtual);
				return ResponseEntity.ok(cidadeAtual);
			}
			return ResponseEntity.notFound().build();
		} catch (EntidadeNaoEncontradaException e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}
	
	@DeleteMapping("/{cidadeId}")
	public ResponseEntity<Restaurante> remover(@PathVariable Long cidadeId){
		try {
			cadastroCidadeService.remover(cidadeId);
			return ResponseEntity.noContent().build();
		}catch (EntidadeNaoEncontradaException e){
			return ResponseEntity.notFound().build();
		}catch (EntidadeEmUsoExeption e) {
			return ResponseEntity.status(HttpStatus.CONFLICT).build();
		}
	}
}
