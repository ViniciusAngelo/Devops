package br.com.valueprojects.mock_spring.controller;

import org.springframework.web.bind.annotation.*;

import br.com.valueprojects.mock_spring.model.Jogo;
import br.com.valueprojects.mock_spring.model.Juiz;

import java.util.ArrayList;
import java.util.List;

@RestController

@RequestMapping("/jogos")
public class JogoController {

    private List<Jogo> jogos = new ArrayList<>();
    private Juiz juiz = new Juiz();
    private SMSService sms = new SMSService();

    // Criar um novo jogo
    @PostMapping("/criar")
    public Jogo criarJogo(@RequestBody String descricao) {
        Jogo jogo = new Jogo(descricao);
        jogos.add(jogo);
        return jogo;
    }

    // Julgar um jogo
    @PostMapping("/{id}/julgar")
    public String julgarJogo(@PathVariable int id) {
        Jogo jogo = jogos.get(id);
        juiz.julga(jogo);
        double primeiroColocado = juiz.getPrimeiroColocado();
        double ultimoColocado = juiz.getUltimoColocado();
        if(armazenarNoBancoDeDados()) {
        	sms.Enviar(ultimoColocado);
        }
        return "Primeiro colocado: " + primeiroColocado + ", Último colocado: " + ultimoColocado;
    }
    
    public Boolean armazenarNoBancoDeDados() {
    	//caso seja armazenado corretamente ou não esta pendente, retorna true
    	return true; //implementação da lógica de enviar para o BDD
    }

    // Listar todos os jogos
    @GetMapping
    public List<Jogo> listarJogos() {
        return jogos;
    }

}

