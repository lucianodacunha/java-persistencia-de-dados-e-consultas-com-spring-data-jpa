# Projeto do curso de JPA

Criar uma nova aplicação, onde teremos a persistência dos dados de artistase músicas.

O menu da aplicação será similar a este:
```
1- Cadastrar artistas
2- Cadastrar músicas
3- Listar músicas
4- Buscar músicas por artistas
5- Pesquisar dados sobre um artista
9- Sair
```
Teremos as opções de cadastrar música, cadastrar artista e listar as músicas 
cadastradas. Por exemplo, ao listar, a aplicação mostrará as músicas que 
registrei em meu banco de dados, como uma música da Madonna e outra do Kiss.

O nosso desafio consiste no seguinte: desenvolver um menu similar ao apresentado, 
permitindo que a pessoa usuária cadastre músicas e artistas. É fundamental 
pensar em como será o mapeamento disso nas classes e entidades, quais anotações 
serão usadas e como será a cardinalidade. Aqui, temos artistas e suas músicas. 
Um artista pode cantar muitas músicas, então, é importante pensar bem nesse 
aspecto na hora de mapear os dados.

Uma funcionalidade interessante é a possibilidade de cadastrar o tipo de 
artista utilizando um enum. Isso permitirá categorizá-los como artista solo, 
banda ou dupla. Para cadastrar uma música, é necessário informar quem é o 
artista, e deve-se cadastrar músicas para um artista que já esteja cadastrado.

Ao escolher cadastrar um artista, por exemplo, nos deparamos com o seguinte:
```
Informe o nome desse artista:
Informe o tipo desse artista: (solo, dupla, banda)
Cadastrar outro artista? (S/N)
```

_TODO: A funcionalidade de pesquisar dados de um artista ainda não foi implementada._

## Como o projeto foi desenvolvido:

### Tecnologias

- Java 17
- Spring Boot 3.1.5
- Spring Data JPA
- Lombok
- MySql

### Configuração do projeto

Utilizando o utilitário [`spring initializr`](https://start.spring.io/), defini
todas as tecnologias iniciais, já que pra esse primeiro momento, não 
implementaria funcionalidades extras.

Com o projeto importado, criei o banco:

```sql92
create database musicas;
```

e posteriormente já configurei o arquivo `application.properties`:

```
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
spring.datasource.url=jdbc:mysql://${MYSQL_HOST}:3306/${DB_NAME}
spring.datasource.username=${DB_USER}
spring.datasource.password=${DB_PASSWORD}
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true
spring.jpa.hibernate.ddl-auto=update
```

**Importante:** _As variáveis de ambiente devem ser configuradas também. Pois é 
atráves delas que informaremos as variáveis do `properties`.
No meu caso, utilizo Linux, minhas configurações em ambiente local/teste fiz 
utilizando o arquivo `/etc/environment`. 
É necessário reiniciar o computador após a configuração.
Para saber se as configurações foram aplicadas com sucesso, utilizo o seguinte
código para verificar as variáveis de ambiente:_

```java
public class EnvVarsTests {
    public static void main(String[] args) {
        System.getenv().forEach((k, v) -> {
            System.out.println("k: %s, v: %s".formatted(k, v));
        });
    }
}
```

O Spring já cria a classe `Application`, que conterá o método `main`. Nessa 
classe, criaremos nossos `repositories`, mas para o início do projeto, criamos 
somente o objeto `Main`, que exibirá o menu inicial. 
Esse objeto será instânciado no método `run`, da interface
`CommandLineRunner`, que deve ser implementada na classe Application.

```java
Main main = new Main();
```

A classe `Main`, exibirá o menu com todas as opções previstas. Para deixá-la
mais limpa, adotei o padrão de projeto 
**[Command](https://refactoring.guru/pt-br/design-patterns/command)**, que 
separa em uma nova classe cada operação do menu.
Para utilizarmos esse padrão, é necessário criamos um objeto executor que será
responsável por executar os comandos. Esse objeto, receberá como parâmetro 
objetos do tipo Command, que executam as ações, por isso, implementam a interface
Command.

Nessa etapa, já é possível iniciar a aplicação, que ainda estã sem as 
funcionalidades, mas pronta para desenvolvermos a modelagem. 

```

  .   ____          _            __ _ _
 /\\ / ___'_ __ _ _(_)_ __  __ _ \ \ \ \
( ( )\___ | '_ | '_| | '_ \/ _` | \ \ \ \
 \\/  ___)| |_)| | | | | || (_| |  ) ) ) )
  '  |____| .__|_| |_|_| |_\__, | / / / /
 =========|_|==============|___/=/_/_/_/
 :: Spring Boot ::                (v3.1.5)

2023-11-05T11:39:36.875-03:00  INFO 10062 --- [  restartedMain] ...
2023-11-05T11:39:36.877-03:00  INFO 10062 --- [  restartedMain] ...
2023-11-05T11:39:36.909-03:00  INFO 10062 --- [  restartedMain] ...
2023-11-05T11:39:37.150-03:00  INFO 10062 --- [  restartedMain] ...
2023-11-05T11:39:37.163-03:00  INFO 10062 --- [  restartedMain] ...
2023-11-05T11:39:37.363-03:00  INFO 10062 --- [  restartedMain] ...
2023-11-05T11:39:37.393-03:00  INFO 10062 --- [  restartedMain] ...
2023-11-05T11:39:37.394-03:00  INFO 10062 --- [  restartedMain] ...
2023-11-05T11:39:37.556-03:00  INFO 10062 --- [  restartedMain] ...
2023-11-05T11:39:37.572-03:00  INFO 10062 --- [  restartedMain] ...
2023-11-05T11:39:37.827-03:00  INFO 10062 --- [  restartedMain] ...
2023-11-05T11:39:37.828-03:00  INFO 10062 --- [  restartedMain] ...
2023-11-05T11:39:38.139-03:00  INFO 10062 --- [  restartedMain] ...
2023-11-05T11:39:38.142-03:00  INFO 10062 --- [  restartedMain] ...
2023-11-05T11:39:38.232-03:00  INFO 10062 --- [  restartedMain] ...
2023-11-05T11:39:38.248-03:00  INFO 10062 --- [  restartedMain] ...
    Escolha uma opção abaixo:
    1: Cadastrar artistas
    2: Cadastrar músicas
    3: Listar artistas
    4: Listar músicas
    5: Buscar músicas por artistas
    6: Pesquisar dados sobre um artista
    9: Sair

```


### Mapeamento das classes

Foram criadas duas classes de entidades:

- Artista
- Musica

Além do enum representando o tipo do artista

- TipoArtista (solo, dupla ou banda)

#### Artista

Inicialmente a entidade Artista foi criada de forma padrão, com suas anotações
da JPA. 

Criei também um campo chamado TipoArtista, um enum, que informa se o artista
é solo, dupla ou banda. 

A entidade tem também um campo chamado `musicas`, que referencia suas músicas, 
tendo na tabela musicas um campo chamado artista_id, representando seu mapeamento.

#### Musica

Entidade também criada inicialmente de forma padrão, com anotações JPA e Lombok.

Possui um campo do tipo Artista, utilizado no mapeamento com o campo `musicas`, 
da classe `Artista`.

#### TipoArtista

Enum criado para representar o tipo do Artista: solo, dupla ou banda.

Criei um método auxiliar para buscar o valor do enum, caso o usuário digitar as
letras em minúsculas. 

### Repositórios

Essas são nossas camadas de persistência. 

Os repositórios no Spring Data JPA, são representados por interfaces que `extendem`
a JPARepository.

Inicialmente criei um repositório para Artista, que será responsável por gravar
o Artista no banco. Para isso, criei um método

O método save é um dos métodos que já temos por padrão na interface.

Os scripts de acesso a essa camada são colocados nos Command's. Desta forma, eles
devem receber um objeto repository para executar essas ações. 

Esses objetos são gerenciados pelo Spring, desta forma, injetamos ele lá na classe 
Application e passamos o objeto para a classe Main que delega para cada Command.

Criei também um repositório para Musica, que é utilizado para salvar as músicas
no banco além de oferecer a funcionalidade de pesquisar músicas por artista.

### Atualizações

Apliquei algumas atualizações, seguindo orientações do curso:

- anottation `unique` no campo nome da entidade `Artista`
- não necessário criar o método de conversão no enum, como alternativa podemos
utilizar o método valueOf que converte uma String em um Enum.

## Como utilizar o projeto

### Requisitos

- Java 17 
- MySQL

### Configuração

Com o requisitos satisfeitos, configure o MySQL e cria um banco de dados.

Importante também configurar corretamente as variáveis de ambiente de acordo
com o ambiente utilizado
 
- MYSQL_HOST
- DB_NAME
- DB_USER}
- DB_PASSWORD}

Importe o projeto, utilizando IntelliJ e execute a aplicação.