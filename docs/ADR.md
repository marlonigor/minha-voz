# ADR: MinhaVoz

Cada decisão tem registro próprio e pode mudar sem afetar as outras. Todos os registros estão como **Proposto** até o piloto com o paciente confirmar ou corrigir as escolhas.

| ADR | Decisão | Status |
| :--- | :--- | :--- |
| ADR-001 | Android nativo com Kotlin e Jetpack Compose | Proposto |
| ADR-002 | Cartões em JSON nos assets, atrás de uma interface de repositório | Proposto |
| ADR-003 | Voz gravada como principal, TTS de reserva e canal de alarme para urgentes | Proposto |

Data de todos os registros: 2026-10-07. Decisor: Igor.

---

## ADR-001: Android nativo com Kotlin e Jetpack Compose

### Contexto

O app MinhaVoz é uma ferramenta de apoio à comunicação de uma pessoa com afasia expressiva após AVC, em ambiente doméstico. O app depende de controle direto do áudio do sistema (canais, volume, foco) e deve funcionar sem internet. Não há autenticação, sincronização nem servidor.

### Decisão

Desenvolver em Kotlin com Jetpack Compose, apenas para Android. O app não declara permissão de internet. O `minSdk` será definido pelo aparelho do paciente (questão OQ-03 do PRD). Modelo e versão do Android do aparelho entram neste registro assim que forem conhecidos.

### Alternativas consideradas

* **Flutter ou React Native.** Rejeitadas. O áudio exige acesso direto a `AudioManager`, `AudioAttributes` e `TextToSpeech`. Plugins de terceiros acrescentam uma camada que pode quebrar com novas versões do Android.
* **Aplicativo web (PWA).** Rejeitada. O controle de canais de áudio e a fixação de tela ficam limitados.

### Consequências

* Positivas: acesso determinístico às APIs de áudio, interface adaptável com pouco código, custo zero de infraestrutura.
* Negativas: sem versão para iOS. Aceitável, porque o aparelho do paciente é Android.
* O app depende de bateria, volume e do aparelho estar ao alcance. Esses riscos são tratados pela rotina diária do PRD (seção 8) e pelo plano B em papel, não por software.

### Condição de revisão

Revisar se o paciente trocar de aparelho para um sistema diferente do Android.

---

## ADR-002: Cartões em JSON nos assets, atrás de uma interface de repositório

### Contexto

O MVP tem 8 cartões fixos (6 na grade e 2 na barra). O cadastro de cartões pela família fica para a V2. A v1.0 da documentação adotava Room com banco pré-populado. Para 8 registros imutáveis, isso traz schema, migrações e arquivo de banco sem benefício no MVP.

### Decisão

Guardar os cartões em `cartoes.json` nos assets, lido na abertura com `kotlinx.serialization`. Cada cartão tem: `id`, `rotulo`, `frase`, `pictograma`, `corFundo`, `corTexto`, `urgencia`, `arquivoVoz` e `posicao`.

O app acessa os cartões por uma interface `CartaoRepository`. No MVP a implementação lê o JSON. Na V2, uma implementação com Room importa o JSON como dados iniciais, sem alterar a interface nem as telas.

### Alternativas consideradas

* **Room desde o MVP.** Rejeitada por ora. O custo de schema e migrações só se paga com o cadastro de cartões.
* **SharedPreferences.** Rejeitada. Não representa bem dados estruturados.

### Consequências

* Positivas: menos código, menos pontos de falha, estrutura de dados legível e revisável pela fonoaudióloga.
* Negativas: a V2 exige uma implementação nova do repositório. O custo fica pequeno, porque a interface já existe.

### Condição de revisão

Adotar Room quando o cadastro de cartões entrar no escopo.

---

## ADR-003: Voz gravada como principal, TTS de reserva e canal de alarme para urgentes

### Contexto

O paciente precisa ser entendido e ouvido em outros cômodos. A síntese de voz do Android depende do motor instalado e do pacote de português do Brasil, soa genérica e tem latência variável na primeira fala. O canal de alarme ignora o modo silencioso, mas obedece ao volume de alarme e é bloqueado pelo Não Perturbe em silêncio total.

### Decisão

1. **Voz principal:** um arquivo `.ogg` gravado por um familiar para cada cartão, reproduzido com `MediaPlayer` e `AudioAttributes` definidos pelo nível de urgência.
2. **Reserva:** `TextToSpeech` em português do Brasil, com os mesmos `AudioAttributes`, quando faltar o arquivo.
3. **Cartões normais:** `USAGE_MEDIA`, com sinal curto de confirmação e voz.
4. **Cartões urgentes:** `USAGE_ALARM`, com `alerta_urgente.ogg` seguido da voz. A sequência repete a cada 20 s, até 3 emissões, e o botão FECHAR cancela.
5. **Volume:** antes de um chamado urgente, o app eleva o volume de alarme ao mínimo configurado (padrão de 80%) e restaura o valor anterior ao fim. A validação acontece no aparelho do paciente (TC-12). Se o sistema recusar o ajuste, o app toca no volume atual e registra o aviso no modo cuidador.
6. **Foco de áudio:** pedir foco transitório antes de cada emissão, para pausar outros sons durante a fala.
7. **Bluetooth e fones:** o MVP não resolve a rota de áudio por software. O resultado de TC-17 define a regra, e o item 4 da rotina diária do PRD cobre o risco.

### Alternativas consideradas

* **Apenas TTS.** Rejeitada como principal. Depende do motor do sistema, e uma voz familiar comunica melhor a urgência.
* **Notificação com canal de importância alta.** Rejeitada. Fica sujeita às configurações do usuário e ao Não Perturbe.

### Consequências

* Positivas: não depende do motor de voz para o fluxo principal, voz conhecida pelo paciente, alerta audível mesmo com o aparelho no modo silencioso.
* Negativas: exige gravar e manter 8 arquivos de voz. O Não Perturbe em silêncio total ainda bloqueia o alarme, e o volume de alarme baixo reduz o alcance. A configuração do aparelho e os testes TC-12 a TC-17 tratam desses riscos.
* A promessa do app é reduzir falhas, e não eliminá-las. O plano B em papel permanece.

### Condição de revisão

Voltar ao TTS como voz principal se o piloto mostrar que a gravação é inviável ou que o paciente entende melhor a voz sintética.
