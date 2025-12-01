# GAC106-Simulador-POO

Repositório para o trabalho prático da disciplina GAC106 (UFLA). Expansão do simulador "Raposas e Coelhos" para um ecossistema mais complexo e realista, aplicando conceitos de Programação Orientada a Objetos em Java.

## PROPOSTA DE EXPANSÃO DO SIMULADOR “RAPOSAS E COELHOS”

**Tema:** Ecossistema

**Curso:** GAC106 – Práticas de Programação Orientada a Objetos

**Número do Grupo:** TP_Grupo 08

**Integrantes:**
* Daniel Silva Ferraz Neto
* Gustavo Alessandro de Souza Sabino
* Jose Victor Miranda de Oliveira
* Wesley Filipe Rocha da Silva

---

#### 1. Introdução

Esta proposta tem como objetivo expandir o simulador original, que atualmente representa uma interação simples entre raposas e coelhos, para um ecossistema mais complexo e realista. A estrutura base do projeto foi mantida, incluindo os componentes principais como o campo, os atores e a visualização. No entanto, foram introduzidos novos elementos como diferentes tipos de seres vivos (animais e caçadores), carregamento de mapas via arquivo, condições climáticas e uma cadeia alimentar mais elaborada. O desenvolvimento do projeto está centralizado no repositório versionado no GitHub.

#### 2. Estrutura Geral do Simulador

O simulador é composto por dois grandes sistemas:
* **Sistema Biológico:** Inclui os seres vivos, divididos em animais (Herbívoros e Predadores) e caçadores, cada um com comportamentos e interações específicas.
* **Sistema Ambiental:** Responsável por controlar a estação do ano, influenciando diretamente as taxas de reprodução e o comportamento dos caçadores.

A cada ciclo de simulação, o ambiente evolui, afetando a reprodução e a movimentação das espécies.

#### 3. Atores e Cadeia Alimentar

Foram implementados cinco tipos de atores, cada um com características distintas:

* **Coelho (Herbívoro Pequeno)**
    * **Comportamento:** Reproduz-se com mais frequência que os demais herbívoros. Pode morrer de velhice, por superlotação (não ter para onde se locomover) ou ser caçado por raposas, leões ou o caçador.
* **Búfalo (Herbívoro Grande)**
    * **Comportamento:** Reproduz-se menos que o coelho, mas fornece mais energia aos predadores quando é caçado. Pode morrer de velhice, por superlotação (não ter para onde se locomover) ou ser caçado por leões ou o caçador.
* **Raposa (Carnívora)**
    * **Comportamento:** Alimenta-se exclusivamente de coelhos. Reproduz-se com base em idade e energia. Pode morrer de fome, velhice ou ser caçada por leões ou o caçador.
* **Leão (Superpredador)**
    * **Comportamento:** Caça coelhos, raposas e búfalos. O ganho de energia varia conforme a presa (búfalo > raposa > coelho). Pode morrer de fome ou velhice.
* **Caçador (Predador Universal)**
    * **Comportamento:** Pode caçar qualquer animal do simulador. Após uma caça, retorna automaticamente para sua célula de origem ("Casa do Caçador"). Possui um comportamento cíclico e interrompe as atividades de caça durante o inverno.

#### 4. Terreno e Ambiente

O mapa da simulação é composto por diferentes tipos de terreno lidos a partir de arquivos e é influenciado por um sistema de estações.

* **Tipos de Terreno**
    * **Floresta/Grama:** Área principal de movimentação dos seres vivos.
    * **Água:** Funciona como barreira natural. Nenhum animal pode ocupar ou atravessar essas células (definido nos arquivos de mapa na pasta `Mapas`).
    * **Árvore:** Obstáculo físico intransponível.

* **Estações do Ano**
    * As estações surgem de forma cíclica (Primavera -> Verão -> Outono -> Inverno) e cada uma dura **100 ciclos** (steps).
        * **Primavera:** Aumenta a taxa de reprodução dos animais (fator 1.2x).
        * **Verão:** Taxa de reprodução normal.
        * **Outono:** Reduz ligeiramente a taxa de reprodução (fator 0.8x).
        * **Inverno:** Reduz drasticamente a reprodução (fator 0.4x). Além disso, os **caçadores não saem para caçar** e se movem mais lentamente se estiverem fora de casa.

#### 5. Visualização e Controles (GUI)

A interface gráfica segue o modelo do simulador original, com as seguintes melhorias:

* **Menu Inicial:** Permite escolher o mapa a ser carregado (ou usar apenas grama) e definir a quantidade inicial de caçadores.
* **Estatísticas em Tempo Real:** Indicadores de população por espécie, estação atual e contagem de animais abatidos pelo caçador.
* **Visualização:** Cores distintas para cada espécie e representação visual das casas dos caçadores.

#### 6. Conclusão

Com esta expansão, o simulador evolui para um ecossistema mais dinâmico. Os carnívoros controlam a população de herbívoros e o caçador atua como um agente de equilíbrio final. O sistema ambiental adiciona variabilidade, exigindo que as populações resistam a períodos de baixa reprodução (inverno). A presença de mapas personalizados permite criar cenários estratégicos com obstáculos naturais.
