# Projeto para Desafio Avanade/Ada Tech

## Descrição

- Junte-se à batalha épica estilo Advanced Dungeons & Dragons (AD&D) 
- O jogo, como todo bom RPG (Role Playing Game), será duelado em turnos.
- Escolha o seu Personagem Favorito (herói ou monstro).
- O seu oponente sempre será um monstro, você pode escolher ou deixar aleatório.

## Configuração do Ambiente

Antes de começar, você deve configurar seu ambiente de desenvolvimento:

1. **Java**: Certifique-se de ter o Java JDK instalado na sua máquina.

2. **PostgreSQL**: Instale o PostgreSQL e configure uma base de dados para a aplicação.

3. **Gradel**: Instale o Gradel para gerenciar as dependências e compilar o projeto.

## Configuração da Base de Dados

1. Crie uma base de dados PostgreSQL chamada `rpg_api`.

2. Atualize as configurações de conexão com o banco de dados no arquivo `application.properties` no projeto Spring Boot.

   ```properties
   spring.datasource.url=jdbc:postgresql://localhost:5432/rpg_api
   spring.datasource.username=seu_usuario
   spring.datasource.password=sua_senha
   ```

## Executando a Aplicação

1. Clone este repositório para a sua máquina.
2. Abra um terminal na pasta raiz do projeto e execute o seguinte comando:

     ```
     gradle bootRun
     ```
     *Isso iniciará o servidor Spring Boot.*


3. Acesse a API da aplicação em http://localhost:8080.
4. 

## Endpoints da API
A aplicação oferece os seguintes endpoints da API:

### Personagens
- GET /characters: Obtém todos os personagens.
- GET /characters/{id}: Obtém um personagem pelo ID.
- GET /characters/category/{category}: Obtém personagens por categoria.
- POST /characters: Cria um novo personagem.
- PUT /characters/{id}: Atualiza um personagem pelo ID.
- DELETE /characters/{id}: Exclui um personagem pelo ID.

### Batalhas
- GET /battles: Obtém todas as batalhas.
- GET /battles/{id}/history: Obtém o histórico de uma batalha pelo ID.
- POST /battles/start: Inicia uma nova batalha.
- POST /battles/{id}/attack: Realiza um ataque em um turno de uma batalha.
- POST /battles/{id}/defense: Realiza uma defesa em um turno de uma batalha.
- POST /battles/{id}/calculate-damage: Calcula o dano em um turno de uma batalha.

### Exemplo de Uso da Aplicação

Obter todos os personagens:
```
 GET http://localhost:8080/characters
```
Criar um novo personagem:
```
 POST http://localhost:8080/characters
 
 Body:
   {
   "name": "Nome do Personagem",
   "category": "HERO",
   "species": "Mage",
   "life": 100,
   "strength": 50,
   "defense": 30,
   "agility": 40,
   "diceQuantity": 2,
   "diceFaces": 12
   }
```
Iniciar uma nova batalha:
```
POST http://localhost:8080/battles/start

Body:
{
    "myCharacterId": 1,
    "opponentId": 2
}
```
### Exemplo de Personagens

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

### Dados do Jogo

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


#### 5) Fim do turno e Fim do Jogo ou Não
   - Se no fim do turno nenhum personagem ficou com zero ou menos PV então a luta continua e o próximo turno se inicia
   imediatamente