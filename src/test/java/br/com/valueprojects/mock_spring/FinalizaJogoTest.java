package br.com.valueprojects.mock_spring;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyDouble;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import br.com.valueprojects.mock_spring.builder.CriadorDeJogo;
import br.com.valueprojects.mock_spring.controller.JogoController;
import br.com.valueprojects.mock_spring.model.FinalizaJogo;
import br.com.valueprojects.mock_spring.model.Jogo;
import br.com.valueprojects.mock_spring.model.Juiz;
import infra.JogoDao;

public class FinalizaJogoTest {
	
	@Test
    public void deveFinalizarJogosDaSemanaAnterior() {
        Calendar antiga = Calendar.getInstance();
        antiga.set(1999, 1, 20);

        Jogo jogo1 = new CriadorDeJogo().para("Ca�a moedas")
            .naData(antiga).constroi();
        Jogo jogo2 = new CriadorDeJogo().para("Derruba barreiras")
            .naData(antiga).constroi();

        // mock
        List<Jogo> jogosAnteriores = Arrays.asList(jogo1, jogo2);
        JogoDao daoMock = mock(JogoDao.class);

        when(daoMock.emAndamento()).thenReturn(jogosAnteriores);

        FinalizaJogo finalizador = new FinalizaJogo(daoMock);
        finalizador.finaliza();

        assertTrue(jogo1.isFinalizado());
        assertTrue(jogo2.isFinalizado());
        assertEquals(2, finalizador.getTotalFinalizados());
    }
 
	@Test
	public void deveVerificarSeMetodoAtualizaFoiInvocado() {
		Calendar antiga = Calendar.getInstance();
		antiga.set(1999, 1, 20);

		Jogo jogo1 = new CriadorDeJogo().para("Cata moedas").naData(antiga).constroi();
		Jogo jogo2 = new CriadorDeJogo().para("Derruba barreiras").naData(antiga).constroi();

		// mock 
        List<Jogo> jogosAnteriores = Arrays.asList(jogo1, jogo2);
        JogoDao daoMock = mock(JogoDao.class);

		when(daoMock.emAndamento()).thenReturn(jogosAnteriores);

		FinalizaJogo finalizador = new FinalizaJogo(daoMock);
		finalizador.finaliza();
		
        verify(daoMock, times(1)).atualiza(jogo1);
        verify(daoMock, times(1)).atualiza(jogo2);

        //verifyNoMoreInteractions(daoMock);			
	}
}