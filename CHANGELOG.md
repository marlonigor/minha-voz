# Changelog

Todas as alterações notáveis neste projeto serão documentadas neste arquivo.

O formato é baseado no [Keep a Changelog](https://keepachangelog.com/pt-BR/1.1.0/),
e este projeto adere ao [Versionamento Semântico](https://semver.org/lang/pt-BR/).

## [Unreleased]

### Adicionado
- Persistencia local duravel com Room e SQLite para o catalogo de cartoes (ADR-002 V2).
- Entidade CartaoEntity, DAO CartaoDao e RoomCartaoRepository com pre-povoamento transparente a partir de assets JSON.
- Contrato estendido em CartaoRepository com suporte a atualizarFrase, salvar e remover.
- Testes unitarios isolados com FakeCartaoDao cobrindo persistencia, mutacao e povoamento inicial.

## [0.1.0] - 2026-10-08

### Adicionado
- Scaffold inicial Android com Kotlin e Jetpack Compose (ADR-001).
- Catálogo de 8 cartões em JSON nos assets com desserialização defensiva imutável (ADR-002).
- Grade principal 2x3 fixa e barra inferior com cartões SIM e NÃO sem rolagem (RF-01).
- Visualização de cartão em tela cheia com rótulo em 72 sp e botões REPETIR e FECHAR de 96 dp (RF-03).
- Bloqueio de repetição de 1,5 s (RF-06) e temporizador de fechamento automático de 60 s para cartões normais (RF-03).
- Ativação de cartões urgentes por toque longo de 800 ms com barra de progresso visual (RF-02) e feedback tátil (RF-07).
- Pictogramas vetoriais SVG de alto contraste para todos os 8 cartões do MVP.
- Scripts operacionais padronizados e idempotentes: bin/setup, bin/test e bin/deploy.
- Pipeline de integração contínua (CI) no GitHub Actions com execução de testes e linter headless.
