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

Esta proposta tem como objetivo expandir o simulador original, que atualmente representa uma interação simples entre raposas e coelhos, para um ecossistema mais complexo e realista. A estrutura base do projeto será mantida, incluindo os componentes principais como o campo, os atores e a visualização. No entanto, serão introduzidos novos elementos como diferentes tipos de seres vivos (animais e caçadores), variações de terreno, condições climáticas e uma cadeia alimentar mais elaborada. Para facilitar as alterações, o entendimento e o envolvimento de todo o grupo, o desenvolvimento do projeto será centralizado no repositório versionado no GitHub.

#### 2. Estrutura Geral do Simulador

O simulador será composto por dois grandes sistemas:
* **Sistema Biológico:** Inclui os seres vivos, divididos em animais e caçadores, cada um com comportamentos e interações específicas.
* **Sistema Ambiental:** Responsável por definir a estação do ano, influenciando diretamente o comportamento dos seres vivos.

A cada ciclo de simulação, o ambiente muda, afetando a reprodução, movimentação e sobrevivência das espécies. Assim, os animais não dependerão apenas de idade e energia, mas também das condições externas.

#### 3. Atores e Cadeia Alimentar

Serão introduzidos cinco tipos de atores, cada um com características distintas:

* **Coelho (Herbívoro Pequeno)**
    * **Comportamento:** Reproduz-se com mais frequência que os demais herbívoros. Pode morrer de fome, velhice, por superlotação (não ter para onde se locomover) ou ser caçado por raposas, leões ou o caçador.
* **Búfalo (Herbívoro Grande)**
    * **Comportamento:** Reproduz-se menos que o coelho, mas fornece mais energia aos predadores quando é caçado. Pode morrer de fome, velhice, por superlotação (não ter para onde se locomover) ou ser caçado por leões ou o caçador.
* **Raposa (Carnívora)**
    * **Comportamento:** Alimenta-se exclusivamente de coelhos. Reproduz-se com base em idade e energia. Pode morrer de fome, velhice ou ser caçada por leões ou o caçador.
* **Leão (Superpredador)**
    * **Comportamento:** Caça coelhos, raposas e búfalos. O ganho de energia varia conforme a presa (búfalo > raposa > coelho). Morre de fome ou velhice.
* **Caçador (Predador Universal)**
    * **Comportamento:** Pode caçar qualquer animal do simulador. Após uma caça, retorna automaticamente para sua célula de origem ("Casa do Caçador"). Possui um comportamento cíclico e não caça durante o inverno.

#### 4. Terreno e Ambiente

O mapa da simulação será composto por diferentes tipos de terreno e será influenciado por um sistema de estações.

* **Tipos de Terreno**
    * **Floresta:** Área principal de movimentação dos seres vivos. Contém árvores, que funcionam como obstáculos intransponíveis.
    * **Água:** Funciona como barreira natural. Nenhum animal pode ocupar ou atravessar essas células. Cerca de 10% do mapa será composto por água em blocos aglomerados.

* **Estações do Ano**
    * As estações surgem de forma cíclica (Primavera -> Verão -> Outono -> Inverno) e cada uma dura 50 ciclos (steps).
        * **Primavera:** Favorece o crescimento das plantas (efeito a ser definido, ex: mais comida para herbívoros).
        * **Verão:** Aumenta a reprodução animal.
        * **Outono:** Reduz a reprodução e a propagação vegetal.
        * **Inverno:** Diminui significativamente a reprodução animal. Os caçadores não caçam nesta estação.

#### 5. Visualização e Controles (GUI)

A interface gráfica seguirá o modelo do simulador original, com melhorias:

* Indicadores de população por espécie e de animais mortos pelo caçador.
* Controles básicos: Iniciar, Pausar, Resetar, Avançar um ciclo.
* Menu inicial para escolher a estação do ano inicial e a quantidade de caçadores.

#### 6. Conclusão

Com esta expansão, o simulador evolui para um ecossistema mais dinâmico. Os carnívoros controlam a população de herbívoros e o caçador atua como um agente de equilíbrio final. O sistema ambiental adiciona variabilidade e desafio, exigindo estratégias adaptativas das espécies. A presença de terrenos variados e obstáculos naturais torna o espaço mais estratégico, enquanto o caçador introduz uma nova camada de imprevisibilidade e controle populacional.