# ADR: MinhaVoz

Cada decisão tem registro próprio e pode mudar sem afetar as outras. Todos os registros estão como **Proposto** até o piloto com o paciente confirmar ou corrigir as escolhas.

| ADR | Decisão | Status |
| :--- | :--- | :--- |
| ADR-001 | Android nativo com Kotlin e Jetpack Compose | Proposto |
| ADR-002 | Cartões em JSON nos assets, atrás de uma interface de repositório | Proposto |
| ADR-003 | Text-to-Speech pt-BR garantido, verificação de voz e evolução para gravações da família | Aceito |

Data de todos os registros: 2026-10-07. Decisor: Equipe MinhaVoz.

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

## ADR-003: Text-to-Speech pt-BR garantido, verificação de voz e evolução para gravações da família

### Contexto

O paciente precisa ser entendido e ouvido em outros cômodos mesmo sem a presença física imediata de um cuidador. A versão 0.1.0 e os testes iniciais apontaram a necessidade de síntese sonora imediata e determinística, sem depender de arquivos de gravação pré-existentes para frases padrão ou editadas. Ao mesmo tempo, gravações da voz de familiares agregam identidade afetiva e conforto ao paciente.

### Decisão

1. **Motor Garantido de Síntese:** Text-to-Speech (TTS) nativo do Android configurado obrigatoriamente para pt-BR (`Locale("pt", "BR")`), garantindo voz imediata ao abrir o aplicativo.
2. **Verificação de Recursos de Voz:** Inspecção na inicialização (`VerificadorRecursosVoz`) para checar a presença do pacote de idioma pt-BR e integridade dos recursos, com diagnóstico visual no modo cuidador.
3. **Plano B com Gravações nos Assets:** Suporte a arquivos `.ogg` nos assets para reprodução opcional ou fallback quando o motor TTS apresentar degradação.
4. **Visão de Futuro (Voz Familiar):** Suporte à gravação e substituição da voz sintética por áudios gravados por parentes e cuidadores diretamente nos cartões personalizados, conferindo identidade própria à fala do aplicativo.
5. **Canais e Urgência:** Cartões normais em `USAGE_MEDIA`. Cartões urgentes em `USAGE_ALARM`, elevando o volume antes da emissão sonoro e restaurando o patamar prévio ao término.
6. **Comportamento com Volume Mudo:** O aplicativo monitora o estado de volume de mídia e emite alertas diagnósticos caso o som esteja mutado.

### Alternativas consideradas

* **Apenas áudio gravado.** Rejeitada. Inviabiliza a edição dinâmica de frases e a criação ágil de novos cartões.
* **Notificação do sistema.** Rejeitada. Fica sujeita a filtros de Não Perturbe e requer telas adicionais.

### Consequências

* Positivas: funcionamento imediato "out of the box", independência de gravação humana para vocabulário inicial ou frases editadas, e caminho evolutivo claro para voz familiar nos cartões customizados.
* Negativas: dependência do mecanismo de TTS do Android estar com dados em português do Brasil instalados (mitigado pela verificação defensiva ao iniciar).
