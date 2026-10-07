# PRD: MinhaVoz (MVP)

| Item | Detalhe |
| :--- | :--- |
| Versão | 0.2 |
| Status | Rascunho. Aguarda revisão de fonoaudiólogo e teste com cartões em papel |
| Data | 2026-10-07 |
| Responsável | Igor |
| Plataforma | Android nativo, operação offline |
| Público | Pessoa com afasia expressiva pós-AVC e seus cuidadores |

## Mudanças da v1.0 para a v0.2

* O status voltou para Rascunho. Nenhum usuário real nem profissional de fonoaudiologia validou o texto ainda.
* Cartões urgentes exigem toque longo e repetem o alerta.
* A tela cheia fecha por botão ou por tempo, e toques soltos não fazem nada.
* O catálogo ganhou REMÉDIO e AJUDA. SIM e NÃO saíram da grade e formam uma barra fixa.
* Cor do texto, contraste e tamanhos agora têm valores definidos.
* A voz gravada por um familiar vira a principal. O TTS do Android fica de reserva.
* Entram modo cuidador, métricas de sucesso, limites de segurança e rotina diária.

---

## 1. Problema e solução

### 1.1 Problema

A afasia expressiva após o AVC compromete fala e escrita. O paciente mantém as necessidades do dia a dia, lê palavras comuns e toca na tela do celular. Cada pedido exige gestos e tentativas de adivinhação. O resultado é cansaço, frustração e isolamento para ele, e desgaste contínuo para a família.

### 1.2 Solução

O app MinhaVoz é um comunicador visual e sonoro para Android que funciona sem internet. A tela inicial traz seis cartões em grade fixa e uma barra com SIM e NÃO. O toque abre o cartão em tela cheia, com pictograma e texto grande, e toca a voz de um familiar. Itens urgentes exigem toque longo, tocam um alerta no canal de alarme do Android e repetem o chamado até três vezes ou até o paciente cancelar.

### 1.3 Limites de segurança

* O app MinhaVoz não é dispositivo médico nem sistema de emergência.
* O app não detecta quedas, não monitora o paciente e não liga para ninguém.
* Sinais de novo AVC (rosto caído, fraqueza em um braço, fala alterada de repente) exigem ligação imediata ao SAMU, telefone 192.
* O app depende de bateria, volume e do aparelho estar com o paciente. A seção 8 lista a rotina que reduz essas falhas.
* Cartões impressos com os mesmos pictogramas ficam como plano B ao lado da poltrona e da cama.

---

## 2. Personas

### Paciente

* Afasia expressiva, locomoção preservada, toca no celular, lê palavras comuns.
* Precisa pedir o básico sem esforço e sem depender de adivinhação.
* Anda pela casa com o aparelho na mão, então roça a tela por acidente. Perde a paciência com telas cheias de opções.

### Família e cuidadores

* Fazem tarefas em outros cômodos.
* Precisam reconhecer o alerta urgente e entender o pedido em segundos, mesmo sem ouvir bem o áudio.
* Fazem a rotina diária da seção 8 e configuram o aparelho.

---

## 3. Princípios de design

1. **Posição fixa.** Cada cartão fica sempre no mesmo lugar. A memória motora do paciente vale mais que qualquer otimização.
2. **Um toque, um resultado.** Itens normais ativam ao soltar o dedo dentro do cartão.
3. **Urgência exige intenção.** Itens urgentes pedem toque longo, para o alarme não soar por acidente e a família não aprender a ignorá-lo.
4. **Texto e pictograma juntos**, até a revisão clínica decidir outra coisa.
5. **Nada técnico diante do paciente.** Telas de ajuste ficam no modo cuidador.

---

## 4. Requisitos funcionais

| ID | Requisito | Detalhamento |
| :--- | :--- | :--- |
| RF-01 | Tela inicial | Grade de 2 colunas por 3 linhas com 6 cartões, e barra fixa SIM e NÃO abaixo. Sem rolagem. Altura mínima de 96 dp por cartão, meta de 140 dp. As posições não mudam por uso, sessão ou reinício. Em paisagem, a grade passa para 3 colunas por 2 linhas, com a ordem e a barra visíveis sem rolagem. |
| RF-02 | Ativação | Cartões normais ativam ao soltar o dedo dentro do cartão. Se o dedo sair do cartão antes de soltar, nada acontece. Cartões urgentes exigem toque longo de 800 ms, ajustável de 500 a 1500 ms, com anel de progresso e vibração curta ao completar. |
| RF-03 | Tela cheia | Cor do cartão, pictograma e rótulo em 72 sp ou mais. Dois botões na base, REPETIR e FECHAR, com altura mínima de 96 dp. Toque fora dos botões não faz nada. Cartões normais fecham sozinhos após 60 s. Cartões urgentes ficam abertos até FECHAR, com limite de 5 minutos. A tela do aparelho permanece acesa enquanto a tela cheia estiver aberta. |
| RF-04 | Voz | Reprodução da frase gravada por um familiar, um arquivo por cartão. Se o arquivo faltar, o app usa o TTS do Android em português do Brasil. |
| RF-05 | Urgência | Sequência de alerta sonoro curto e voz, no canal de alarme. A sequência repete a cada 20 s, até 3 emissões no total. FECHAR cancela as repetições. Antes de tocar, o app eleva o volume de alarme ao mínimo configurado (padrão de 80%) e restaura o valor anterior ao fim. |
| RF-06 | Bloqueio de repetição | Após uma ativação, os cartões da grade ignoram toques por 1,5 s. REPETIR e FECHAR respondem sempre. |
| RF-07 | Retorno tátil | Vibração curta ao ativar qualquer cartão. |
| RF-08 | Modo cuidador | Acesso por toque longo de 3 s no canto superior esquerdo. Itens: teste de som de cada cartão, duração do toque longo, volume mínimo do alarme, estado dos recursos de voz e créditos. Nenhum item edita cartões nesta versão. |
| RF-09 | Verificação ao abrir | O app confere os arquivos de voz e o TTS em português do Brasil. Se algo faltar, mostra um ícone de aviso pequeno na tela inicial e detalha o problema no modo cuidador. O paciente não recebe mensagem técnica. |

---

## 5. Catálogo base de cartões

Todos os rótulos ficam em caixa alta, negrito e 32 sp ou mais. Isso os coloca na categoria de texto grande do WCAG, com mínimo de contraste de 3 para 1. Os valores abaixo foram calculados pela fórmula do WCAG e precisam de conferência no aparelho (TC-11).

| Cartão | Frase falada | Fundo | Texto | Contraste | Urgência | Ativação |
| :--- | :--- | :--- | :--- | :--- | :--- | :--- |
| ÁGUA | "Eu quero água, por favor." | `#1E88E5` | Branco | 3,7:1 | Normal | Toque |
| COMIDA | "Estou com fome." | `#FB8C00` | Preto | 8,9:1 | Normal | Toque |
| REMÉDIO | "Quero meu remédio." | `#8E24AA` | Branco | 7,0:1 | Normal | Toque |
| BANHEIRO | "Preciso ir ao banheiro agora." | `#FDD835` | Preto | 15,0:1 | Urgente | Toque longo |
| DOR | "Estou com dor. Preciso de ajuda." | `#E53935` | Branco | 4,2:1 | Urgente | Toque longo |
| AJUDA | "Preciso de ajuda. Venha aqui." | `#D81B60` | Branco | 4,9:1 | Urgente | Toque longo |
| SIM (barra fixa) | "Sim." | `#43A047` | Branco | 3,3:1 | Normal | Toque |
| NÃO (barra fixa) | "Não." | `#424242` | Branco | 10,0:1 | Normal | Toque |

Branco sobre laranja ou amarelo reprova no contraste, por isso esses dois usam texto preto. A cor nunca carrega o significado sozinha. O pictograma e o texto repetem a informação, para atender também quem tem dificuldade de distinguir cores.

---

## 6. Requisitos não funcionais

| ID | Requisito | Meta |
| :--- | :--- | :--- |
| RNF-01 | Offline e privacidade | O manifesto não declara a permissão de internet. Nenhum dado é coletado ou enviado. O download da voz em português do Brasil, se necessário, acontece uma vez na configuração do aparelho. |
| RNF-02 | Latência | Tela cheia em até 100 ms após soltar o dedo. Início do áudio em até 500 ms com arquivos gravados. O TTS é inicializado ao abrir o app. Metas confirmadas por medição no aparelho do paciente (TC-24). |
| RNF-03 | Bateria | Nenhum serviço em segundo plano. Consumo do app abaixo de 1% da bateria em 24 h de repouso (TC-26). |
| RNF-04 | Acessibilidade | Alvos de toque a partir de 96 dp. Contraste conforme a seção 5. Fonte do sistema ampliada a 200% sem corte de texto. |
| RNF-05 | Custo | R$ 0,00 de infraestrutura. Pictogramas e ferramentas de teste gratuitos. |

---

## 7. Conteúdo de áudio

* **Vozes:** um familiar de voz conhecida pelo paciente grava as frases do catálogo, a mesma pessoa em todos os cartões. Gravação em ambiente silencioso, um arquivo por cartão, formato `.ogg` mono, volume normalizado entre os arquivos.
* **Alerta urgente:** arquivo `alerta_urgente.ogg` de até 1,5 s, com três pulsos curtos na faixa de 2 a 4 kHz. O padrão precisa ser diferente de toques e notificações comuns do aparelho. A validação acontece nos testes TC-14 e TC-15.
* **Reserva:** o TTS do Android cobre cartões sem arquivo de voz.

---

## 8. Configuração do aparelho e rotina do cuidador

### Antes do primeiro uso

* Não Perturbe configurado para permitir alarmes.
* Otimização de bateria desativada para o MinhaVoz.
* Fixação de tela do Android ativada, com o app fixado.
* Ícone do app na primeira tela do aparelho.
* Volume de alarme ajustado e testado com um cartão urgente.

### Rotina diária (menos de 2 minutos)

1. Bateria acima de 50%, com carga durante a noite.
2. Aparelho com o paciente ou ao alcance dele.
3. Volume de alarme no nível definido.
4. Nenhum fone ou caixa Bluetooth conectado.
5. Um cartão urgente testado no modo cuidador.

---

## 9. Métricas de sucesso

As metas valem como ponto de partida e serão revisadas após o piloto.

| ID | Métrica | Meta | Como medir |
| :--- | :--- | :--- | :--- |
| M1 | Pedidos corretos | 90% de acerto nas 5 necessidades básicas (água, comida, banheiro, dor, ajuda) | Sessão observada com cenários combinados |
| M2 | Tempo até o entendimento | Até 10 s do toque ao cuidador entender, em 80% dos pedidos | Cronômetro do observador |
| M3 | Toques acidentais | No máximo 1 por dia | Diário do cuidador durante uma semana |
| M4 | Reconhecimento do alerta | 90% de identificação entre 3 familiares | Teste cego TC-15 |
| M5 | Uso espontâneo | Pelo menos 1 pedido por dia, por iniciativa do paciente, na segunda semana | Diário do cuidador |

---

## 10. Validação clínica e questões em aberto

### Revisões pendentes

1. Fonoaudiólogo revisa vocabulário, quantidade de cartões e o uso de texto, pictograma ou ambos.
2. Equipe de saúde informa se há perda de visão de um dos lados ou negligência espacial, para ajustar o layout.
3. Teste de uma semana com cartões em papel, antes de qualquer linha de código.

### Questões em aberto

| ID | Questão | Quem decide |
| :--- | :--- | :--- |
| OQ-01 | Grade de 4 ou de 6 cartões | Teste em papel |
| OQ-02 | Duração do toque longo | Teste com o paciente |
| OQ-03 | Modelo e versão do Android do aparelho do paciente | Igor |
| OQ-04 | Quem grava as vozes | Família |
| OQ-05 | Texto e pictograma juntos, ou apenas um deles | Fonoaudiólogo |

---

## 11. Fora do escopo do MVP

* Cadastro e edição de cartões (V2, com Room).
* Reconhecimento de voz e inteligência artificial.
* Mensagens por WhatsApp, SMS ou notificações em nuvem.
* Login, termos de uso e telemetria.
* Aviso direto ao celular da família pela rede Wi-Fi da casa, sem internet externa (candidato à V3).

---

## 12. Créditos

Pictogramas: ARASAAC (arasaac.org), propriedade do Governo de Aragão, autoria de Sergio Palao, distribuídos sob licença Creative Commons BY-NC-SA. O uso é pessoal e sem fins comerciais. O crédito aparece no modo cuidador.
