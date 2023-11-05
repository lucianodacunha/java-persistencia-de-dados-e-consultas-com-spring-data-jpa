# Java: persistência de dados e consultas com Spring Data JPA

## Aula 1. Evoluindo o projeto

- Buscar várias séries na API. Criamos um loop que só pra quando o usuário escolhe sair do menu, sendo capaz de buscar várias séries na API sem parar.

- Métodos privados. Vimos que, se apenas uma classe irá acessar um método, não precisamos deixá-lo público, podemos declará-lo como privado. Isso é essencial para o encapsulamento das nossas classes.

- Adicionar mais informações aos dados buscados. Revisamos como realizar o mapeamento entre atributos da API e atributos da nossa record.

- Converter os dados que vêm da API para a sua própria classe. Criamos nossa própria classe Serie para representar melhor nossos dados. Para isso, foi necessário utilizar vários métodos de conversão.

- Utilizar um “ifreduzido”. Utilizamos a classe OptionalDouble para lidar com valores decimais e seus possíveis erros, utilizando os métodos ofe orElse, que lembram muito o código de um if reduzido, e são muito úteis para evitar que ocorram Exceptions.

- Criar um Enum. Percebemos que seria excelente poder categorizar nossas séries por gênero. Criamos um enum para isso, e vimos como criar métodos personalizados em enums.

- Consumir a API do ChatGPT. Utilizamos a API do ChatGPT para traduzir nossos dados, adicionamos todas as dependências necessárias e configuramos a classe de consumo.


### Modelando com Enuns

- Outra forma de fazer representação de dados
- Utilizado para valores estáticos e prédefinidos
- Agrupamento de dados categóricos
- Adiciona mais segurança, permitindo somente valores predefinidos

#### Utilização

```
public class EnumTests {
    public static void main(String[] args) {
        for (Cor cor : Cor.values()){
            System.out.println(
                    "ENUM: %s, VALOR: %s".formatted(cor, cor.getValor())
            );
        }
    }
}

enum Cor {
    AMARELO("yellow"),
    VERDE("green"),
    RED("vermelho"),
    BLACK("preto");

    private final String valor;

    Cor(String cor) {
        this.valor = cor;
    }

    public String getValor(){
        return this.valor;
    }
}
```

### Trabalhando com métodos estáticos

- Pertence â classe e não a um objeto específico
- Não interage com os ouros membros da classe
- Amplamente utilizados em libs auxiliares, Math, Arrays, etc.
- Possibilita ações sem a necessidade de gerar instâncias/criar objetos

#### Utilização

```
class MathUtils {
    public static int add(int a, int b) {
        return a + b;
    }
}

public class StaticTests{
    public static void main(String args[]){
        int result = MathUtils.add(5, 10);
        System.out.println(result);
    }
}

```

## Aula 2. Persistindo dados da série

 - Configurar seu ambiente Postgres. Fizemos a instalação desse banco de dados relacional e vimos a diferença entre bancos relacionais e outros tipos de bancos de dados, além de criar nosso banco de séries no Postgres.

 - Preparar sua aplicação para trabalhar com banco de dados. Adicionamos a dependência da JPA ao pom.xml e as configurações do banco de dados no application.properties.

 - Utilizar anotações do Hibernate para mapear suas entidades. Usamos anotações como @Entity, @Transient e @Column na classe Serie, indicando como seriam as configurações da tabela correspondente no banco de dados.

 - Manipular interfaces do tipo Repository. Para fazer operações básicas no banco de dados, como um CRUD, precisamos de uma interface do tipo Repository com o nosso tipo de dados. No nosso caso, criamos a SerieRepository.

 - Injetar dependências. Vimos que não podemos instanciar uma interface do tipo Repository em qualquer lugar. Elas precisam ser declaradas em classes gerenciadas pelo Spring, precedidas de um @Autowired, indicando que está sendo realizada uma injeção de dependências.

 - Trabalhar com variáveis de ambiente. Utilizamos variáveis de ambiente para proteger dados sensíveis da conexão com o banco de dados e com a API.


### Anotações do hibernate

O Hibernate é uma das especificações mais utilizadas da JPA, e fornece diversas anotações para a utilização do Mapeamento Objeto-Relacional.

Vamos conhecer as principais delas?

#### @Entity

Essa anotação é usada para marcar uma classe como uma entidade que deve ser mapeada para uma tabela de banco de dados. Cada entidade corresponde a uma tabela no banco de dados.

#### @Table

Por padrão, o Hibernate usa o nome da classe como o nome da tabela no banco de dados, fazendo apenas a conversão de padrão de nomenclatura do PascalCase para o SnakeCase, que é o padrão utilizado no banco de dados, no entanto, caso seja necessário que o nome da classe seja diferente do nome da tabela no banco de dados, é possível utilizar esta anotação que permite personalizar o mapeamento entre a classe de entidade e a tabela de banco de dados. Com ela, você pode especificar o nome da tabela, o esquema e as restrições de chave primária.
```
@Entity
@Table(name = "minha_tabela")
public class MinhaEntidade { ... }
```
#### @Id

Marca um campo como a chave primária da entidade. O Hibernate usa essa anotação para identificar exclusivamente os registros no banco de dados.
@GeneratedValue

Usada em conjunto com @Id, essa anotação específica como a chave primária é gerada automaticamente. Pode ser usada com estratégias como AUTO, IDENTITY, SEQUENCE ou TABLE, dependendo do banco de dados.
```
@Id
@GeneratedValue(strategy = GenerationType.IDENTITY)
private Long id;
```
#### @Column

Similar ao que acontece na anotação @Table, o Hibernate utiliza o nome dos atributos e os converte como sendo idênticos aos nomes das colunas no banco de dados, e caso seja necessário utilizar nomes diferentes,você pode configurar o nome da coluna, bem como seu tipo, e se ela é obrigatória.
```
@Column(name = "nome_completo", nullable = false)
private String nome;
```
#### @OneToMany e @ManyToOne

Usadas para mapear relacionamentos de um-para-muitos e muitos-para-um entre entidades. Elas definem as associações entre as tabelas no banco de dados.
```
@Entity
public class Autor {
    @OneToMany(mappedBy = "autor")
    private List<Livro> livros;
}

@Entity
public class Livro {
    @ManyToOne
    @JoinColumn(name = "autor_id")
    private Autor autor;
}
```

#### @ManyToMany

A anotação @ManyToMany é usada para mapear relacionamentos muitos-para-muitos entre entidades.
@OneToOne

A anotação @ManyToMany é usada para mapear relacionamentos um-para-um entre entidades.
@JoinColum

A anotação @JoinColumn é usada para especificar a coluna que será usada para representar um relacionamento entre entidades. É frequentemente usada em conjunto com @ManyToOne e @OneToOne.

```
@ManyToOne
@JoinColumn(name = "autor_id")
private Autor autor;
```

#### @JoinTable

A anotação @JoinTable é usada para mapear tabelas de junção em relacionamentos muitos-para-muitos. Ela especifica a tabela intermediária que liga duas entidades.

@Entity
public class Estudante {
    @ManyToMany
    @JoinTable(name = "inscricao",
               joinColumns = @JoinColumn(name = "estudante_id"),
               inverseJoinColumns = @JoinColumn(name = "curso_id"))
    private List<Curso> cursos;
}

#### @Transient

A anotação @Transient é usada para marcar uma propriedade como não persistente. Isso significa que a propriedade não será mapeada para uma coluna no banco de dados.

```
@Transient
private transientProperty;
```

#### @Enumerated

A anotação @Enumerated é usada para mapear campos enumerados (enum) para colunas do banco de dados.

```
@Enumerated(EnumType.STRING)
private Status status;
```

#### @NamedQuery

Essa anotação é usadas para definir consultas JPQL nomeadas que podem ser reutilizadas em várias partes do código.

```
@Entity
@NamedQuery(name = "Cliente.findAll", query = "SELECT c FROM Cliente c")
public class Cliente { ... }
```

#### @Cascade

A anotação @Cascade é usada para especificar o comportamento de cascata das operações de persistência, como salvar e excluir, em relacionamentos. Por exemplo, você pode configurar para que as operações de salvar em cascata afetem entidades relacionadas.

```
@OneToMany(mappedBy = "departamento")
@Cascade(CascadeType.SAVE_UPDATE)
private List<Funcionario> funcionarios;
```

#### @Embeddable e @Embedded

Essas anotações são usadas para representar tipos incorporados (embeddable types) que podem ser usados como componentes em entidades.

```
@Embeddable
public class Endereco { ... }

@Entity
public class Cliente {
    @Embedded
    private Endereco endereco;
}
```

Além dessas anotações, há muitas outras que podem ser consultadas na documentação de anotações do Hibernate, e que facilitam muito o dia a dia de pessoas desenvolvedoras que usam o ORM.

## Aula 3. Mapeamento e relacionamentos

- Mapear relacionamentos entre entidades da JPA. Conhecemos o uso das anotações @OneToMany e @ManyToOne para identificar o relacionamento ”um para muitos” de séries e episódios.

- Conhecer diversos tipos de relacionamento: Identificamos qual era o relacionamento presente na nossa aplicação, além de ter conhecimento dos vários tipos de relacionamento em banco de dados.

- Associar chaves estrangeiras. Entendemos o conceito de chave estrangeira, que é como o banco de dados identifica e configura relacionamentos.

- Trabalhar com os tipos de Cascade. Como o nosso fluxo de salvamento de dados era salvar séries e depois episódios, foi preciso configurar isso utilizando o atributo Cascade.

- Identificar como os dados são carregados. Trabalhamos também com o atributo fetch, que fala sobre carregar os dados de forma “preguiçosa” (lazy) ou “ansiosa” (eager).

- Configurar relacionamentos bidirecionais. Vimos a importância de relacionamentos bidirecionais e deixamos as modificações aparecendo dos dois lados da relação, fazendo tanto setEpisodios() na Série quanto setSerie() nos Episodios.


### Relacionamentos Uni e Bidirecionais com JPA

Ao trabalharmos com banco de dados, existem relacionamentos com diferentes tipos de direção. Existem relacionamentos unidirecionais e bidirecionais. Os unidirecionais deixam a relação visível apenas em um lado, enquanto relacionamentos bidirecionais permitem que os objetos de ambos os lados acessem e/ou alterem o objeto do outro lado. Isso é muito útil quando você quer ter um controle maior sobre seus objetos e as operações que você pode executar neles, como no caso visto em vídeo.

Vamos dar uma olhada em como podemos definir, configurar e controlar esses relacionamentos.
Definindo um Relacionamento Bidirecional

Para definir um relacionamento bidirecional, precisamos ter duas entidades que estão de alguma forma conectadas. Por exemplo, vamos considerar um simples sistema de blog onde temos posts e comentários. Cada post pode ter vários comentários.
```
@Entity
public class Post {
    @Id
    @GeneratedValue
    private Long id;
    private String title;
    private String content;

    @OneToMany(mappedBy = "post")
    private List<Comment> comments = new ArrayList<>();
}

@Entity
public class Comment {
    @Id
    @GeneratedValue
    private Long id;
    private String content;

    @ManyToOne
    @JoinColumn(name = "post_id")
    private Post post;
}
```
No exemplo acima, definimos um relacionamento "OneToMany" de Post para Comentários e um relacionamento "ManyToOne" inverso de Comentários para Post. mappedBy = "post" no Post se refere ao campo post na classe Comentário.

Erros comuns ao configurarmos mapeamentos bidirecionais

### Erro 1: Problemas de mapeamento bidirecional

Quando se tem uma relação bidirecional entre duas entidades, como, por exemplo, uma relação entre os objetos "Aluno" e "Disciplina", em que um aluno pode se matricular em diversas disciplinas e uma disciplina pode ter vários alunos, é necessário atentar para o mapeamento de ambos os lados da relação.

Exemplo prático:

```
@Entity
public class Aluno {
    @OneToMany(mappedBy = "aluno")
    private List<Disciplina> disciplinas;
}

@Entity
public class Disciplina {
    @ManyToOne
    private Aluno aluno;
}
```
Neste exemplo, a entidade "Disciplina" está mapeando para a entidade "Aluno". No entanto, a entidade "Aluno" também precisa mapear de volta para a "Disciplina". A falta desse mapeamento bidirecional é uma causa comum de erros.

Para resolver, inclua o mapeamento no lado "Aluno" da relação:
```
@Entity
public class Aluno {
    @OneToMany(mappedBy = "aluno")
    private List<Disciplina> disciplinas;
    
    // inclua este método
    public void addDisciplina(Disciplina disciplina) {
        this.disciplinas.add(disciplina);
        disciplina.setAluno(this);
    }
}
```
#### Erro 2: Falha ao escolher o lado de posse corretamente

Em uma associação bidirecional, um lado é o proprietário, e o outro é o lado invertido. Na JPA, o lado do proprietário é sempre usado quando se atualiza a relação no banco de dados.

Por exemplo:
```
@Entity
public class Carro {
    @ManyToOne
    private Dono dono;
}

@Entity
public class Dono {
    @OneToMany(mappedBy = "dono")
    private List<Carro> carros;
}
```
Neste caso, Carro é a entidade proprietária. Se esquecermos de fato de atualizar o lado do proprietário, a JPA não poderá sincronizar corretamente a associação com o banco de dados.

Para corrigir, você deve atualizar sempre o lado proprietário do relacionamento:
```
Dono dono = new Dono();
Carro carro = new Carro();
carro.setDono(dono); // Carro é a entidade proprietária, então atualizamos este lado
dono.getCarros().add(carro);
entityManager.persist(dono);
```
Estes são apenas dois exemplos de erros comuns que podem ocorrer ao configurar os mapeamentos na JPA. Outros erros também podem ocorrer, mas a chave para resolvê-los é entender bem como a JPA funciona e sempre analisar e testar cuidadosamente o seu código. 


### Tipos de busca ("eager" ou "lazy")

#### Estudo aprofundado sobre Fetch Types: Lazy e Eager

Aprendemos que geralmente encontramos dois tipos de carregamento de dados: "lazy" e "eager". Esses dois conceitos são essencialmente sobre quando e como os dados são recuperados do banco de dados para serem usados em nossas aplicações.

#### O que é Fetch Type?

De modo bem simples, Fetch Type define qual a estratégia será utilizada para carregar os dados do banco para sua aplicação.

Para facilitar nosso entendimento, gosto de usar a analogia do café da manhã. Imagine que você tem uma mesa de café da manhã e pode haver vários itens nela, como pão, café, leite, frutas, etc.

A forma como você vai pegar esses itens, quando e quantos de uma vez, é basicamente o conceito por trás do fetch type, que veremos a seguir.

#### Lazy Fetch Type

Lazy, em inglês, significa preguiçoso. Em termos de programação, Lazy Fetch Type é quando você pega apenas o que precisa, no momento em que precisa.

Se voltarmos para a nossa analogia do café da manhã, seria como pegar somente o café primeiro. Quando se sentir pronto para comer algo, você então vai e pega uma fruta ou um pão. Ou seja, você só busca os outros itens quando realmente vai utilizá-los.

Vamos tomar como exemplo a relação entre um usuário e seus posts em uma aplicação de blog. Se optarmos pelo fetch type lazy, ao carregarmos o usuário, seus posts não serão carregados automaticamente. Eles serão postergados até que explicitamente solicitado, conforme a seguir:
```
@Entity
public class User {
    @OneToMany(fetch = FetchType.LAZY)
    private List<Post> posts;
}
```

#### Eager Fetch Type

Eager, em inglês, pode ser traduzido como ansioso. Em programação, Eager Fetch Type é mais rápido, pois vai pegar todos os dados relacionados ao mesmo tempo.

Back to the café da manhã analogia, Eager Fetch Type seria como se você pegasse tudo que há na mesa de uma vez só. Você pega o café, pão, leite, frutas, tudo de uma única vez.

Na relação usuário/posts, se optarmos pelo fetch type eager, ao carregar o usuário, todos os seus posts serão carregados simultaneamente.
```
@Entity
public class User {
    @OneToMany(fetch = FetchType.EAGER)
    private List<Post> posts;
}
```
#### Impacto no desempenho da aplicação

A estratégia de carregamento afeta diretamente o desempenho da aplicação. Um carregamento Eager pode parecer eficiente, pois tudo já está carregado de uma vez. No entanto, se a relação envolver muitos dados, isso pode causar problemas de desempenho, além de consumir muito mais memória, uma vez que estamos carregando mais dados do que realmente precisamos.

Por outro lado, Lazy Fetch Type pode parecer uma estratégia mais eficaz, pois carrega os dados sob demanda. No entanto, se não administrado cuidadosamente, pode acabar resultando em múltiplas chamadas ao banco de dados, aumentando o tempo de resposta.

Escolher entre Lazy e Eager não é uma decisão trivial e deve ser baseada na necessidade da aplicação. Um bom ponto de partida é começar com Lazy Fetch Type e optar por Eager onde o carregamento completo é muitas vezes necessário.

O escopo da aplicação, a quantidade de dados, a frequência de acesso e muitos outros fatores serão decisivos para essa escolha. É importante sempre analisar o contexto e testar o desempenho para alcançar a melhor estratégia.

Entender sobre os conceitos de Fetch Types, lazy e eager, é um passo muito importante para melhorar o desempenho do seu aplicativo. Portanto, sempre busque melhorar e aperfeiçoar seu conhecimento nesta área.

## Aula 4. Buscando informações no banco

- Criar queries derivadas com a JPA. Conhecemos o recurso padrão da JPA para fazer buscas utilizando palavras-chave em métodos na classe Repository.

- Comparar streams e buscas no banco de dados. Percebemos as mudanças em utilizar streams e as derived queries na nossa aplicação.

- Conhecer diversas palavras-chave para criar seus métodos. Aprofundamos nas palavras-chave e em como utilizá-las, reforçando a prática.

- Discutir os vários tipos de retorno ao realizar as buscas. Conversamos sobre as diferenças entre retornar uma série, uma lista de séries ou um Optional de séries.

- Ler dados dinamicamente e armazenar em um Enum. Vimos como fazer a correspondência entre o que está sendo digitado e um campo no enum.


###  Consultas derivadas ("derived queries")

A JPA tem diversos recursos, e um dos mais legais que podemos utilizar são as derived queries, em que trabalhamos com métodos específicos que consultam o banco de forma personalizada. Esses métodos são criados na interface que herda de JpaRepository. Neles, utilizaremos palavras-chave (em inglês) para indicar qual a busca que queremos fazer.

A estrutura básica de uma derived query na JPA consiste em:
```
verbo introdutório + palavra-chave “By” + critérios de busca
```
Como verbos introdutórios, temos find, read, query, count e get. Já os critérios são variados. Veremos alguns exemplos em vídeo, mas você pode explorar bastante a prática para entendê-los melhor.

Os critérios mais simples envolvem apenas os atributos da classe mapeada no Repository. No nosso caso, um exemplo desse critério seria o findByTitulo, em que fazemos uma busca por séries com um atributo específico da classe Serie. Mas podemos acrescentar condições a esses critérios. É aí que surge o findByTituloContainingIgnoreCase(). Para fazer os filtros, poderíamos utilizar várias outras palavras. Dentre elas, podemos citar:

- Palavras relativas à igualdade:
    - Is, para ver igualdades
    - Equals, para ver igualdades (essa palavra-chave e a anterior têm os mesmos princípios, e são mais utilizadas para a legibilidade do método).
    - IsNot, para checar desigualdades
    - IsNull, para verificar se um parâmetro é nulo

- Palavras relativas à similaridade:
    - Containing, para palavras que contenham um trecho
    - StartingWith, para palavras que comecem com um trecho
    - EndingWith, para palavras que terminem com um trecho
    - Essas palavras podem ser concatenadas com outras condições, como o ContainingIgnoreCase, para não termos problemas de Case Sensitive.

- Palavras relacionadas à comparação:
    - LessThan, para buscar registros menores que um valor
    - LessThanEqual, para buscar registros menores ou iguais a um valor
    - GreaterThan, para identificar registros maiores que um valor
    - GreaterThanEqual, para identificar registros maiores ou iguais a um valor
    - Between, para saber quais registros estão entre dois valores

Essas são apenas algumas das palavras que podemos utilizar, e podemos combiná-las de muitas formas! Ao longo dos próximos vídeos, vamos exercitar nossos conhecimentos fazendo várias buscas com essas palavras-chave, mas também te convidamos a testar com vários exemplos para ver na prática como funciona!


### Ordenações e outras palavras chaves das consultas derivadas

Para trabalhar com a ordenação de registros pesquisados, também existem algumas palavras-chave. Podemos utilizar o OrderBy para ordenar os registros por algum atributo deles, como a série pela avaliação. Também podemos encadear atributos. Se uma Série tem um Ator e queremos ordenar pelo nome do ator, podemos utilizar OrderByAtorNome, por exemplo.

Além do OrderBy, ainda existem alguns outros recursos de filtros que podem ser utilizados:

- `Distinct`, para remover dados duplicados
- `First`, para pegar o primeiro registro
- `Top`, para limitar o número de dados

## Aula 5. Utilizando a linguagem de consulta da JPA: JPQL

- Diferenciar os tipos de consulta da JPA. Vimos que podemos trabalhar com derived queries, com queries nativas usando o nativequery e a JPQL, que é a linguagem de buscas da JPA.
- Criar métodos totalmente personalizados e mais legíveis. Vimos que utilizar a JPQL pode auxiliar na escrita de métodos mais legíveis. Para isso, basta escrever o nome do método e anotá-lo com @Query.
- Aprofundar em linguagem SQL. Conhecemos várias expressões utilizadas em SQL, como LIKE, ORDER e LIMIT.
- Recuperar informações secundárias. Conseguimos buscar informações relacionadas a episódios a partir da série, utilizando o recurso das junções (JOIN).
- Comparar recursos SQL e Java. Percebemos que, assim como o Java tem uma API de datas, o SQL também tem sua forma de lidar com datas. No nosso caso, utilizamos a função YEAR do SQL.

## Aula 6. Desafio

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

Teremos as opções de cadastrar música, cadastrar artista e listar as músicas cadastradas. Por exemplo, ao listar, a aplicação mostrará as músicas que registrei em meu banco de dados, como uma música da Madonna e outra do Kiss.

O nosso desafio consiste no seguinte: desenvolver um menu similar ao apresentado, permitindo que a pessoa usuária cadastre músicas e artistas. É fundamental pensar em como será o mapeamento disso nas classes e entidades, quais anotações serão usadas e como será a cardinalidade. Aqui, temos artistas e suas músicas. Um artista pode cantar muitas músicas, então, é importante pensar bem nesse aspecto na hora de mapear os dados.

Uma funcionalidade interessante é a possibilidade de cadastrar o tipo de artista utilizando um enum. Isso permitirá categorizá-los como artista solo, banda ou dupla. Para cadastrar uma música, é necessário informar quem é o artista, e deve-se cadastrar músicas para um artista que já esteja cadastrado.

Ao escolher cadastrar um artista, por exemplo, nos deparamos com o seguinte:
```
Informe o nome desse artista:
Informe o tipo desse artista: (solo, dupla, banda)
Cadastrar outro artista? (S/N)
```
