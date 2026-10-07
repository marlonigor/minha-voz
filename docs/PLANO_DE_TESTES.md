# Plano de Testes: MinhaVoz (MVP)

| Item | Detalhe |
| :--- | :--- |
| Versão | 0.2 |
| Data | 2026-10-07 |
| Documentos relacionados | PRD v0.2 e ADR-001 a ADR-003 |
| Responsável | Igor |

## 1. Objetivo

Validar três coisas, nesta ordem: o funcionamento técnico, o desempenho do áudio na casa e o uso real pelo paciente. Cada caso de teste aponta o requisito do PRD que ele cobre.

## 2. Ambiente

* **Aparelho:** o mesmo modelo e a mesma versão do Android do paciente. Sem esse aparelho, os testes de áudio e latência não valem como aprovação.
* **Configuração:** a lista da seção 8 do PRD aplicada antes de cada rodada.
* **Ferramentas:** Accessibility Scanner (Google), decibelímetro em outro celular, `adb` com `monkey` e `logcat`, gravação de tela.
* **Cômodos:** quarto, sala e cozinha da casa, com portas abertas e fechadas.
* **Severidade dos defeitos:** alta quando o alerta urgente ou a voz falham, média quando o uso fica difícil, baixa quando o problema é estético.

---

## 3. Testes de interface e interação

| ID | Requisito | Procedimento | Resultado esperado | Status |
| :--- | :--- | :--- | :--- | :--- |
| TC-01 | RF-02, RF-06 | Tocar duas vezes no cartão ÁGUA com intervalo menor que 300 ms. | Uma única abertura da tela cheia e uma única fala. | [ ] |
| TC-02 | RF-02 | Pressionar COMIDA, arrastar o dedo para fora do cartão e soltar. Repetir soltando dentro do cartão. | Fora do cartão não ativa. Dentro do cartão ativa. | [ ] |
| TC-03 | RF-02, RF-07 | Tocar DOR por 300 ms. Depois manter por 800 ms. | Com 300 ms nada acontece. Com 800 ms o anel de progresso completa, o aparelho vibra e o cartão ativa. | [ ] |
| TC-04 | RF-02 | Segurar DOR e arrastar para fora antes de completar o anel. | O anel zera e o cartão não ativa. | [ ] |
| TC-05 | RF-03 | Com a tela cheia aberta, tocar em 10 pontos fora dos botões. | A tela cheia permanece aberta. | [ ] |
| TC-06 | RF-03 | Tocar REPETIR e depois FECHAR. | REPETIR repete a fala. FECHAR volta à tela inicial. | [ ] |
| TC-07 | RF-03 | Ajustar o tempo de tela do sistema para 15 s. Abrir ÁGUA e não tocar. | A tela permanece acesa e a tela cheia fecha sozinha após 60 s. | [ ] |
| TC-08 | RF-01 | Abrir e fechar o app 10 vezes e reiniciar o aparelho. | Os 6 cartões e a barra SIM e NÃO voltam sempre nas mesmas posições. | [ ] |
| TC-09 | RF-01 | Girar o aparelho na tela inicial e durante uma fala. | Grade em 3 colunas por 2 linhas, sem rolagem. A fala não reinicia. | [ ] |
| TC-10 | RNF-04 | Ampliar a fonte do sistema para 200%. | Nenhum rótulo é cortado e todos os cartões continuam visíveis. | [ ] |
| TC-11 | RNF-04 | Rodar o Accessibility Scanner nas telas inicial e cheia, e conferir os contrastes da tabela do PRD. | Nenhum item reprovado. Tamanhos de alvo a partir de 96 dp. | [ ] |
| TC-27 | RF-08 | Tocar por 1 s no canto superior esquerdo. Depois manter por 3 s. | Com 1 s nada acontece. Com 3 s abre o modo cuidador. | [ ] |

---

## 4. Testes de áudio e alcance na casa

| ID | Requisito | Procedimento | Resultado esperado | Status |
| :--- | :--- | :--- | :--- | :--- |
| TC-12 | RF-05 | Ajustar o volume de alarme em 10% e o de mídia em 100%. Acionar DOR. Conferir o volume de alarme depois. | O app eleva o volume ao mínimo configurado, toca o chamado e restaura os 10%. | [ ] |
| TC-13 | RF-05 | Repetir o chamado em modo silencioso, vibrar e nos três níveis do Não Perturbe (só prioridade, só alarmes, silêncio total). | Toca em silencioso, vibrar, só prioridade e só alarmes. Em silêncio total o bloqueio é registrado como limite conhecido da rotina diária. | [ ] |
| TC-14 | RF-05 | Aparelho no quarto, decibelímetro a 5 m na cozinha, com uma parede e a porta aberta. Acionar BANHEIRO. | Nível medido igual ou maior que o valor-alvo. Ponto de partida: 60 dB. Ajustar após a primeira medição. | [ ] |
| TC-15 | M4 | Teste cego com 3 familiares: 10 sons em ordem aleatória, 5 notificações comuns e 5 alertas do MinhaVoz. | Pelo menos 90% de identificação correta. | [ ] |
| TC-16 | RF-05 | Acionar DOR e não tocar em nada. Repetir e tocar FECHAR após a primeira emissão. | Sem toque, 3 emissões com 20 s de intervalo. Com FECHAR, as repetições param. | [ ] |
| TC-17 | ADR-003 | Conectar um fone Bluetooth e acionar um cartão urgente. | Registrar por onde o som sai. O resultado define a regra no item 4 da rotina diária. | [ ] |
| TC-18 | ADR-003 | Tocar música em outro app e acionar ÁGUA e DOR. | A música pausa durante a fala e a voz fica audível. | [ ] |
| TC-19 | RF-03 | Receber uma ligação com a tela cheia aberta e encerrar a chamada. | O app volta em estado consistente, sem fala duplicada. | [ ] |

---

## 5. Testes de voz, resiliência e desempenho

| ID | Requisito | Procedimento | Resultado esperado | Status |
| :--- | :--- | :--- | :--- | :--- |
| TC-20 | RF-04, RF-09 | Remover o arquivo de voz de um cartão. Depois remover também o pacote de voz em português do Brasil do TTS. | Com o arquivo ausente, o TTS fala a frase. Sem os dois, o ícone de aviso aparece e o modo cuidador detalha o problema. | [ ] |
| TC-21 | RNF-01 | Inspecionar o manifesto do APK. Operar os 8 cartões em modo avião. | Nenhuma permissão de internet no manifesto. Todas as funções operam normalmente. | [ ] |
| TC-22 | RF-01, RF-04 | Reiniciar o aparelho e abrir o app. Acionar um cartão normal e um urgente. | O app abre e fala sem etapas extras. | [ ] |
| TC-23 | RF-05 | Operar com bateria abaixo de 15% e economia de bateria ativada. | Interface e áudio funcionam sem travamentos. | [ ] |
| TC-24 | RNF-02 | Registrar por `logcat` os horários do toque, da tela cheia e do início do áudio. 20 amostras. | Tela cheia em até 100 ms e áudio em até 500 ms, em mediana e no percentil 95. | [ ] |
| TC-25 | Estabilidade geral | Rodar `adb shell monkey` com 5.000 eventos e fazer 200 ativações manuais seguidas. | Nenhum fechamento por erro. | [ ] |
| TC-26 | RNF-03 | Deixar o app instalado em repouso por 24 h e consultar o consumo na ferramenta de bateria do Android. | Consumo abaixo de 1% da bateria. | [ ] |

---

## 6. Protocolo de uso com o paciente

O paciente participa porque quer. Antes de cada fase, explique o objetivo e pergunte se ele aceita. Ele não está sendo avaliado. O app está.

### Fase 0: cartões em papel (antes do código)

* Imprimir 8 cartões com os pictogramas ARASAAC e as cores do PRD.
* Usar por uma semana no dia a dia, com o cuidador anotando quais cartões ele escolhe, confusões e tempo até o entendimento.
* Saídas: decisão sobre 4 ou 6 cartões (OQ-01), texto e pictograma (OQ-05) e revisão do vocabulário com a fonoaudióloga.

### Fase 1: sessões com o app

* Pré-requisito: seção 7 aprovada.
* Duas sessões por dia por 5 dias, de 10 minutos, no horário de menor cansaço.
* Cada sessão tem 5 a 8 cenários combinados (por exemplo, "o copo está vazio" ou "você quer ir ao banheiro"). O paciente escolhe o cartão.
* Registrar: cartão correto ou não, tempo até a escolha, toques acidentais, ajuda do cuidador e sinais de frustração.
* Regra de parada: encerrar a sessão se o paciente recusar, mostrar irritação ou cansaço. Terminar de modo positivo e anotar o motivo.
* Usar esta fase para decidir a duração do toque longo (OQ-02).

### Fase 2: uma semana em casa

* O cuidador mantém um diário: usos por dia, toques acidentais por dia, pedidos que o app não cobriu e alertas não ouvidos.
* Medir M1, M2, M3 e M5 do PRD.

---

## 7. Critérios de aprovação para cada etapa

| Etapa | Critério |
| :--- | :--- |
| Antes da Fase 1 | TC-01 a TC-27 executados, sem defeito de severidade alta aberto. |
| Antes da Fase 1 | TC-14 e TC-15 aprovados: alerta audível a 5 m e identificado por 90% dos familiares. |
| Antes da Fase 1 | `monkey` e ativações manuais sem fechamento por erro (TC-25). |
| Antes da Fase 1 | Fonoaudióloga revisou o vocabulário, ou a decisão de seguir sem a revisão está registrada no PRD. |
| Antes da Fase 1 | Paciente concordou em participar. |
| Fim da Fase 1 | M1 e M2 dentro da meta, sem sessão encerrada por frustração em mais de 1 dia. |
| Fim da Fase 2 | M3 e M5 dentro da meta. Com isso, o PRD muda de Rascunho para Aprovado. |

---

## 8. Cobertura

Todos os requisitos têm ao menos um caso: RF-01 (TC-08, TC-09), RF-02 (TC-01 a TC-04), RF-03 (TC-05 a TC-07, TC-19), RF-04 (TC-20), RF-05 (TC-12 a TC-16), RF-06 (TC-01), RF-07 (TC-03), RF-08 (TC-27), RF-09 (TC-20), RNF-01 (TC-21), RNF-02 (TC-24), RNF-03 (TC-26), mais estabilidade geral (TC-22, TC-23, TC-25), RNF-04 (TC-10, TC-11) e RNF-05 (custo, sem teste técnico).
