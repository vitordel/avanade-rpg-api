# Projeto para Desafio Avanade/Ada Tech

### Descrição

- Junte-se à batalha épica estilo Advanced Dangeous & Dragons (AD&D) !! 
- Nos ajude a criar a melhor API Rest usando Java SpringBoot com banco de dados Postgres.
- O jogo, como todo bom RPG (Role Playing Game), será duelado em turnos.
- Escolha o seu nome e personagem favorito (herói ou monstro).
- O seu oponente sempre será um monstro, você pode escolher ou deixar aleatório.
- Sim, também teremos que “jogar” dados.

### Algumas regras:
- Ao iniciar, será necessário escolher um personagem (herói ou monstro);
- Cada personagem tem seus atributos únicos. Escolha com calma o seu personagem;
- Os personagens possuem Pontos de Vida (PV);
- Se um personagem ficar com PV igual ou abaixo de zero então o oponente será o vencedor;
- O dano causado por um ataque depende da força do atacante e da defesa do defensor, enquanto o dano recebido depende da força do atacante e da resistência do defensor e da eficácia de sua defesa;
- Banco de Dados Postgres;
- Necessário criar o CRUD (Create, Read, Update e Delete) de cadastro de Personagem;

### Personagens

| Heros     | Life | Strength | Defense | Agility | Number of Dices | Dice Faces | 
|-----------|------|----------|---------|---------|-----------------|------------|
| Warrior   | 20   | 7        | 5       | 6       | 1               | 12         |
| Barbarian | 21   | 10       | 2       | 5       | 2               | 8          |
| Knight    | 26   | 6        | 8       | 3       | 2               | 6          |

| Monsters | Life | Strength | Defense | Agility | Number of Dices | Dice Faces | 
|----------|------|----------|---------|---------|-----------------|------------|
| Orc      | 42   | 7        | 1       | 2       | 3               | 4          |
| Giant    | 34   | 10       | 4       | 4       | 2               | 6          |
| Werewolf | 34   | 7        | 4       | 7       | 2               | 4          |

### Dados
- Os dados em jogos de RPG podem ter diferentes números de faces, indicado pelo número após a letra "d". Por
exemplo, 1d12 indica que você deve jogar um dado de 12 faces, ou seja, o resultado será um número aleatório entre
1 e 12.
- Quando a notação inclui mais de um dado, o número antes do "d" indica quantos dados devem ser jogados. Por
exemplo, 2d8 significa que você deve jogar dois dados de 8 faces e somar os resultados.
- Portanto, para jogar 2d20, você deve jogar dois dados de 20 faces e somar os resultados. O resultado final será um
número aleatório entre 2 e 40.

### Fluxo do Jogo

#### 1) Iniciativa
   - Precisamos definir quem vai começar o jogo atacando ou como chamamos no RPG, quem terá a iniciativa.
   - Para isso, jogue um dado de 20 faces (1d20 → número possível de 1 a 20).
   - Não temos empates e quem tirar o maior valor terá a iniciativa.


#### 2) Turno
   - O turno é dividido em 2 partes. Ataque e defesa.
   - a) Ataque
     - O ataque é bem simples. Precisará jogar um dado de 12 faces (1d12 → número possível de 1 a 12) somar com
     a Força e com a Agilidade.
   - b) Defesa
     - A defesa é calculada também jogando um dado de 12 faces (1d12 → número possível de 1 a 12) somar com a
     - Defesa e com a Agilidade.
     - Se o valor do ataque for maior do que a defesa, então o dano será calculado (próximo tópico).
     - Se o valor do ataque for menor ou igual ao valor da defesa, então o defensor conseguiu realizar a defesa e não
     receberá nenhum dano..


#### 3) Dano
   - Se a defesa foi menor do que o ataque então será necessário calcular o dano.
   - O cálculo é bem simples.
   - Jogue o(s) dado(s) de acordo com o Dano que o personagem possui e some o valor da Força do personagem.
   - Exemplo:
     - Bárbaro → quantidade de dados x faces do dado, ou seja, 2 números aleatórios que variam de 1 a 8 onde a soma
     será no mínimo 2 e no máximo 16.
     - Orc → quantidade de dados x faces do dado, ou seja, 3 números aleatórios que variam de 1 a 4 onde a soma será no
     mínimo 2 e no máximo 8.


#### 4) Pontos de Vida
   - Por fim, temos os pontos de vida do personagem.
   - Ao sofrer o dano, devemos subtrair o valor do dano dos PV do personagem.
   - O personagem que ficar com zero ou menos de PV então a luta terminará instantaneamente.


#### 5) Fim do turno
   - Se no fim do turno nenhum personagem ficou com zero ou menos PV então a luta continua e o próximo turno se inicia
   imediatamente


#### 6) Histórico
   - Todos os detalhes das batalhas deverão ser salvas em tabela de LOG para futura conferência.
   - Dados: qual heroi, qual monstro, quem iniciou a batalha, dados de cada turno (número do turno, dado de ataque, defesa,
   dano, etc)


#### 7) Endpoints
   - Todos referentes ao CRUD de Personagem (Create, Read, Update e Delete)
   - Você é livre para criar os endpoints, porém esperamos ao menos alguns deles, tais como:
   - Ataque
   - Defesa
   - Cálculo do dano
   - Histórico

#### 9) Readme
   Uma boa API Rest sempre possui um arquivo Readme bem escrito.