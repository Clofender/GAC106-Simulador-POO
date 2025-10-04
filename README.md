# GAC106-Simulador-POO

Repositório para o trabalho prático da disciplina GAC106 (UFLA). Expansão do simulador "Raposas e Coelhos" para um ecossistema mais complexo e realista, aplicando conceitos de Programação Orientada a Objetos em Java.

## Proposta de Expansão do Simulador – Tema: Ecossistema

**Curso:** GAC106 – Práticas de Programação Orientada a Objetos

**Número do Grupo:** TP_Grupo 08

**Integrantes:**
* Daniel Silva Ferraz Neto
* Gustavo Alessandro de Souza Sabino
* Jose Victor Miranda de Oliveira
* Wesley Filipe Rocha da Silva

---

#### 1. Introdução

Esta proposta tem como objetivo expandir o simulador original, que atualmente representa uma interação simples entre raposas e coelhos, para um ecossistema mais complexo e realista. A estrutura base do projeto será mantida, incluindo os componentes principais como o campo, os atores e a visualização. No entanto, serão introduzidos novos elementos como diferentes tipos de seres vivos (plantas e animais), variações de terreno, condições climáticas e uma cadeia alimentar mais elaborada. Além disso, será incluído um novo personagem: o Caçador, que desempenha um papel único como predador de todas as espécies. Também, para facilitar as alterações, o entendimento e o envolvimento de todo o grupo, o desenvolvimento do projeto será centralizado no repositório versionado no GitHub.

#### 2. Componentes do Ecossistema

O simulador será composto por um sistema biológico com plantas e animais, cada um com comportamentos e interações específicas, formando uma cadeia alimentar.

* **Plantas**
    * **Grama**
        * **Comportamento:** É a base da cadeia alimentar. Surge em células livres, se espalha probabilisticamente para células vizinhas e serve de alimento para herbívoros. Quando consumida, a célula volta a ser solo exposto.

* **Animais e Cadeia Alimentar**
    * **Coelho (Herbívoro Pequeno)**
        * **Comportamento:** Alimenta-se de grama e possui alta taxa de reprodução. É caçado por raposas, leões e pelo caçador. Morre de fome ou velhice.
    * **Búfalo (Herbívoro Grande)**
        * **Comportamento:** Alimenta-se de grama, reproduz-se mais lentamente que o coelho e fornece mais energia aos predadores. É caçado por leões e pelo caçador. Morre de fome ou velhice.
    * **Raposa (Carnívoro)**
        * **Comportamento:** Alimenta-se exclusivamente de coelhos. É caçada por leões e pelo caçador. Morre de fome ou velhice.
    * **Leão (Superpredador)**
        * **Comportamento:** Predador de topo, caça coelhos, búfalos e raposas, com ganho de energia variável. Não é caçado por outros animais. Morre de fome ou velhice.
    * **Caçador (Predador Universal)**
        * **Comportamento:** Um ator especial que pode caçar qualquer animal. Possui um comportamento cíclico: sai de sua "Casa" para caçar, e ao abater uma presa, retorna para a casa. Não caça durante o inverno e não pode atravessar a água.

#### 3. Sistema Ambiental e Terreno

O ambiente do simulador será dinâmico, influenciado pelo terreno, clima e estações do ano.

* **Tipos de Terreno**
    * **Grama:** Permite a movimentação e serve de alimento.
    * **Solo Exposto:** Permite a movimentação, mas não possui recursos.
    * **Água:** Funciona como uma barreira natural intransponível para todos os atores. Ocupará cerca de 10% do mapa em blocos aglomerados.

* **Sistema de Clima e Estações**
    * **Climas (Duração de 5 ciclos):**
        * **Ensolarado:** Aumenta a taxa de reprodução animal e de propagação da grama.
        * **Chuvoso:** Reduz a reprodução animal, mas acelera o crescimento da grama.
        * **Nublado:** Condições padrão, sem alterações significativas.
    * **Estações (Ciclo Fixo):**
        * **Primavera:** Favorece o crescimento de plantas.
        * **Verão:** Aumenta a reprodução animal.
        * **Outono:** Reduz a reprodução e o crescimento vegetal.
        * **Inverno:** Reduz drasticamente a reprodução de todos os seres vivos. O caçador não caça nesta estação.

#### 4. Interface e Controles (GUI)

A interface gráfica será aprimorada para refletir a maior complexidade do ecossistema.

* Indicadores de população para cada espécie.
* Controles básicos: Iniciar, Pausar, Resetar e Avançar um ciclo.
* Menu inicial para permitir que o jogador escolha a estação do ano em que a simulação se inicia.

#### 5. Conclusão

Com esta expansão, o simulador evolui de uma simples interação predador-presa para um ecossistema dinâmico. As interdependências entre plantas, animais, terreno e clima criam um ambiente complexo e realista. O caçador, em particular, introduz uma camada de imprevisibilidade, atuando como um agente de controle populacional que afeta toda a cadeia alimentar, tornando a simulação mais estratégica e desafiadora.