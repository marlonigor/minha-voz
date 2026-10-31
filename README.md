# MinhaVoz: Comunicador Aumentativo e Alternativo (CAA)

Ferramenta de comunicacao assistiva vocal e visual, desenvolvida para restaurar a autonomia expressiva de pessoas com afasia motora ou expressiva pos-AVC (Acidente Vascular Cerebral) em ambiente domestico.

---

## 1. O Problema e a Solucao

### A Dor Real
Apos um AVC, a afasia expressiva compromete a capacidade de articular fala e escrita, enquanto a cognicao, os desejos e as necessidades basicas cotidianas permanecem presentes. Cada pedido basico (como sede, fome, dor ou banheiro) passa a depender de gestos dificeis e tentativas exaustivas de adivinhacao por parte dos familiares, gerando frustracao, ansiedade e isolamento para o paciente, alem de sobrecarga emocional para os cuidadores.

### A Resposta do MinhaVoz
O MinhaVoz transforma o celular ou tablet Android em um comunicador direto e deterministico:
* **Grade Fixa de Alta Visibilidade:** Seis cartoes prioritarios em grade 2x3 invariante e barra inferior fixa com SIM e NAO, preservando a memoria motora do usuario.
* **Voz Imediata e Garantida:** Sintese de fala nativa em portugues do Brasil (TTS) e suporte a arquivos sonoros locais, comunicando a necessidade em voz alta para qualquer comodo da casa.
* **Seguranca em Urgencias:** Chamados criticos (Banheiro, Dor, Ajuda) exigem toque consciente de 800 ms com retorno tátil, elevam o volume do canal de alarme e permanecem em alerta ate atendimento manual.
* **100% Offline e Privado:** Sem coleta de telemetria, sem necessidade de conexao com a internet e sem risco de vazamento de dados clinicos.

---

## 2. Mapa Visual da Interface

A interface foi projetada para minimizar erros motores, eliminando rolagem e garantindo alvos de toque com dimensoes generosas:

```text
+-------------------------------------------------------+
|  [Modo Cuidador - Toque 3s]                           |
|                                                       |
|   +-----------------------+   +-------------------+   |
|   |        AGUA           |   |      COMIDA       |   |
|   |  (Azul / Toque)       |   | (Laranja / Toque) |   |
|   +-----------------------+   +-------------------+   |
|   |       REMEDIO         |   |     BANHEIRO      |   |
|   |  (Roxo / Toque)       |   | (Amarelo / 800ms) |   |
|   +-----------------------+   +-------------------+   |
|   |         DOR           |   |       AJUDA       |   |
|   |  (Vermelho / 800ms)   |   |  (Rosa / 800ms)   |   |
|   +-----------------------+   +-------------------+   |
|                                                       |
|   +-----------------------------------------------+   |
|   |  [ SIM - Verde ]     |    [ NAO - Cinza ]     |   |
|   +-----------------------------------------------+   |
+-------------------------------------------------------+
```

Ao acionar qualquer cartao, a tela expande para visualizacao cheia em alto contraste com botoes grandes de `REPETIR` e `FECHAR`.

---

## 3. Guia de Inicio Rapido (Quick Start)

### 3.1. Instalacao no Aparelho do Paciente
1. Baixe o pacote `.apk` gerado pelo pipeline de release ou compile o artefato com `./bin/deploy`.
2. Transfira e instale o APK no dispositivo Android (Android 8.0 Oreo / API 26 ou superior).

### 3.2. Configuracao Recomendada no Aparelho
Para garantir que o comunicador opere com maxima confiabilidade:
* **Fixacao de Tela:** Ative a fixacao de tela do Android (Screen Pinning) para impedir que toques acidentais fechem o aplicativo ou naveguem para a gaveta do sistema.
* **Nao Perturbe:** Configure a regra do sistema para permitir que toques no canal de Alarme soem livremente mesmo em horarios de descanso.
* **Volume de Alarme:** Defina o volume inicial de alarme para ao menos 80%. O aplicativo ajusta automaticamente esse volume em cartoes de emergencia e restaura o estado anterior ao fechar.

---

## 4. Ambiente de Desenvolvimento e Scripts Operacionais

O projeto adota scripts padronizados e idempotentes na pasta `bin/`:

### 4.1. Pre-requisitos
* Java Development Kit (JDK) versao 17.
* Android SDK com API 35 (Android 15) instalada.
* Bash / Shell Unix ou emulador de terminal compatível no Windows.

### 4.2. Preparar o Ambiente
Executa a verificacao de pre-requisitos de runtime e pre-aquece os daemons e dependencias do Gradle:
```bash
./bin/setup
```

### 4.3. Executar Testes e Validacoes
Roda a suite completa de testes unitarios e de integracao headless com Robolectric e executa a analise estatica de linter:
```bash
./bin/test
```

### 4.4. Gerar Artefatos de Distribuicao
Compila o APK otimizado e o pacote App Bundle de producao:
```bash
./bin/deploy
```
Os arquivos gerados estarao disponiveis em `app/build/outputs/apk/release/` e `app/build/outputs/bundle/release/`.

---

## 5. Seguranca e Cadeia de Suprimentos

* **Chave de Assinatura (Keystore):** A assinatura de release deve ser preservada em pelo menos dois locais geograficos distintos e seguros (ex: cofre de senhas protegido e backup offline criptografado). A perda da keystore impede a publicacao de atualizacoes para o aparelho ja configurado do usuario.
* **Monitoramento de Dependencias:** O repositorio utiliza o Dependabot (`.github/dependabot.yml`) com varreduras semanais automatizadas para atualizacao de bibliotecas Gradle e GitHub Actions.
* **Integridade da Branch Main:** A branch principal e protegida por pipeline de integracao continua (`.github/workflows/ci.yml`), exigindo aprovacao de todos os testes unitarios e linters antes da mesclagem.

---

## 6. Documentacao Tecnica Detalhada

Para aprofundamento nos padroes arquiteturais, requisitos clinicos e planejamento de testes, consulte os documentos na pasta `docs/`:
* [PRD (Documento de Requisitos do Produto)](docs/PRD.md): Requisitos funcionais, personas, limites de seguranca e catalogo base.
* [ADR (Architecture Decision Records)](docs/ADR.md): Justificativas tecnicas para Kotlin/Compose (ADR-001), Room/SQLite (ADR-002) e Sintese TTS com resiliencia de audio (ADR-003).
* [Plano de Testes](docs/PLANO_DE_TESTES.md): Casos de teste funcionais, de interface, alcance sonoro e bateria.
* [Changelog](CHANGELOG.md): Registro historico de versoes e funcionalidades.

---

## 7. Licenca e Creditos

* **Software:** Distribuido sob licenca MIT. Consulte o arquivo [LICENSE](LICENSE) para maiores detalhes.
* **Pictogramas:** Os simbolos pictograficos utilizados sao propriedade do Governo de Aragao (Espanha) e foram criados por Sergio Palao para o [ARASAAC](http://www.arasaac.org), distribuidos sob Licenca Creative Commons (BY-NC-SA).

