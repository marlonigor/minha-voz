package br.com.minhavoz.ui.cartoes

import br.com.minhavoz.core.model.Cartao

/**
 * Representa o estado imutavel da tela principal de cartoes de comunicacao.
 *
 * @property cartoesGrade Lista dos cartoes que compoem a grade principal (RF-01).
 * @property cartoesBarraFixa Lista dos cartoes de acao rapida na barra fixa inferior (SIM/NAO).
 * @property cartaoEmDetalhe Cartao atualmente aberto em tela cheia, ou null se fechado (RF-03).
 * @property bloqueadoPorRepeticao Indica se toques na grade estao temporariamente suspensos (RF-06).
 * @property erro Mensagem de falha no carregamento dos cartoes, ou null se operacional.
 */
data class GradeCartoesUiState(
    val cartoesGrade: List<Cartao> = emptyList(),
    val cartoesBarraFixa: List<Cartao> = emptyList(),
    val cartaoEmDetalhe: Cartao? = null,
    val bloqueadoPorRepeticao: Boolean = false,
    val erro: String? = null
)
