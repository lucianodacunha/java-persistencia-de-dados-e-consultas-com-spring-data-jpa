package io.github.lucianodacunha.screenmatch.main;

import io.github.lucianodacunha.screenmatch.model.dto.DadosSerie;
import io.github.lucianodacunha.screenmatch.model.dto.DadosTemporada;
import io.github.lucianodacunha.screenmatch.model.entity.Categoria;
import io.github.lucianodacunha.screenmatch.model.entity.Episodio;
import io.github.lucianodacunha.screenmatch.model.entity.Serie;
import io.github.lucianodacunha.screenmatch.repository.SerieRepository;
import io.github.lucianodacunha.screenmatch.service.ConsumoApi;
import io.github.lucianodacunha.screenmatch.service.ConverteDados;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

@Component
public class Principal {

    private SerieRepository serieRepository;
    private Scanner leitura = new Scanner(System.in);
    private ConsumoApi consumo = new ConsumoApi();
    private ConverteDados conversor = new ConverteDados();
    private final String ENDERECO = "https://www.omdbapi.com/?t=";
    private final String API_KEY = System.getenv("OMDB_APIKEY");
    private List<DadosSerie> dadosSeries = new ArrayList<>();
    private List<Serie> series = new ArrayList<>();
    private Optional<Serie> serieEncontrada;

    public Principal(SerieRepository serieRepository){
        this.serieRepository = serieRepository;
    }

    public void exibeMenu() {
        var opcao = -1;
        while(opcao != 0) {
            var menu = """
                    1 - Buscar séries
                    2 - Buscar episódios                
                    3 - Listar séries buscadas
                    4 - Buscar série por título
                    5 - Buscar série por ator
                    6 - Buscar Top 5
                    7 - Buscar por categoria
                    8 - Buscar qtde. de temporadas e avalição
                    9 - Buscar episódios por trecho
                    10 - Buscar episódios depois de um data
                    0 - Sair                                 
                    """;

            System.out.println(menu);
            opcao = leitura.nextInt();
            leitura.nextLine();

            switch (opcao) {
                case 1:
                    buscarSerieWeb();
                    break;
                case 2:
                    buscarEpisodioPorSerie();
                    break;
                case 3:
                    listarSeriesBuscadas();
                    break;
                case 4:
                    buscarSeriePorTitulo();
                    break;
                case 5:
                    buscarSeriePorAtor();
                    break;
                case 6:
                    buscarTop5();
                    break;
                case 7:
                    buscarSeriesPorCategoria();
                    break;
                case 8:
                    buscarSeriesPorTemporadaEAValiacao();
                    break;
                case 9:
                    buscarEpisodiosPorTrecho();
                    break;
                case 10:
                    buscarEpisodiosDepoisDeUmaData();
                    break;
                case 0:
                    System.out.println("Saindo...");
                    break;
                default:
                    System.out.println("Opção inválida");
            }
        }
    }

    private void buscarEpisodiosDepoisDeUmaData() {
        buscarSeriePorTitulo();
        if (serieEncontrada.isPresent()){
            System.out.println("Entre com o ano inicial da busca: ");
            Integer ano = Integer.valueOf(leitura.nextLine());
            List<Episodio> episodios = serieRepository.buscarEpisodioPorAnoDeLancamento(serieEncontrada.get(), ano);
            System.out.println("Episódios encontrados");
            episodios.forEach(e -> {
                System.out.println("Serie: %s, Titulo do Episodio: %s, Avaliação: %.2f".formatted(
                        e.getSerie().getTitulo(), e.getTitulo(), e.getAvaliacao()));
            });
        } else {
            System.out.println("Serie não encontrada");
        }
    }

    private void buscarEpisodiosPorTrecho() {
        System.out.println("Entre com o trecho do título procurado: ");
        var trechoTitulo = leitura.nextLine();
        List<Episodio> episodios = serieRepository.buscarEpisodioPorTrecho(trechoTitulo);
        System.out.println("Episódios encontrados");
        episodios.forEach(e -> {
            System.out.println("Serie: %s, Titulo do Episodio: %s, Avaliação: %.2f".formatted(
                    e.getSerie().getTitulo(), e.getTitulo(), e.getAvaliacao()));
        });

    }

    private void buscarSeriesPorTemporadaEAValiacao() {
        System.out.println("Entre com o valor minimo de temporadas: ");
        var temporadas = Integer.valueOf(leitura.nextLine());
        System.out.println("Entre com o valor minimo de avaliação: ");
        var avaliacao = leitura.nextDouble();
        List<Serie> series = serieRepository.seriesPorTemporadaEAValiacao(temporadas, avaliacao);
        System.out.println("Series encontradoas");
        series.forEach(s -> {
            System.out.println("Serie: %s, Temporadas: %d, Avaliação: %.2f".formatted(
                    s.getTitulo(), s.getTotalTemporadas(), s.getAvaliacao()));
        });
    }

    private void buscarSeriesPorCategoria() {
        System.out.println("Entre com a categoria procurada: ");
        var categoriaBR = leitura.nextLine();
        Categoria categoria = Categoria.fromCategoriaBR(categoriaBR);
        List<Serie> series = serieRepository.findByGenero(categoria);

        System.out.println("Series encontradoas");
        series.forEach(s -> {
            System.out.println("Serie: %s, Avaliação: %.2f".formatted(
                    s.getTitulo(), s.getAvaliacao()));
        });
    }

    private void buscarTop5() {
        List<Serie> top5 = serieRepository.findTop5ByOrderByAvaliacaoDesc();

        if (!top5.isEmpty()){
            System.out.println("Series encontradoas");
            top5.forEach(s -> {
                System.out.println("Serie: %s, Avaliação: %.2f".formatted(
                        s.getTitulo(), s.getAvaliacao()));
            });
        } else {
            System.out.println("Nenhum título encontrado");
        }
    }

    private void buscarSeriePorAtor() {
        System.out.println("Entre com o nome do ator procurado: ");
        var nomeDoAtor = leitura.nextLine();
        List<Serie> seriesEncontradas =  serieRepository.findByAtoresContainingIgnoreCase(nomeDoAtor);

        if (!seriesEncontradas.isEmpty()){
            System.out.println("Series encontradoas");
            seriesEncontradas.forEach(s -> {
                System.out.println("Serie: %s, Avaliação: %.2f".formatted(
                        s.getTitulo(), s.getAvaliacao()));
            });
        } else {
            System.out.println("Nenhum título encontrado");
        }
    }

    private void buscarSeriePorTitulo() {
        System.out.println("Entre com o nome do título procurado: ");
        var nomeDoTitulo = leitura.nextLine();
        serieEncontrada =  serieRepository.findByTituloContainingIgnoreCase(nomeDoTitulo);

        if (!series.isEmpty()){
            System.out.println(serieEncontrada);
        } else {
            System.out.println("Nenhum título encontrado");
        }
    }

    private void buscarSerieWeb() {
        DadosSerie dados = getDadosSerie();
        Serie serie = new Serie(dados);
        this.serieRepository.save(serie);
        System.out.println(dados);
    }

    private DadosSerie getDadosSerie() {
        System.out.println("Digite o nome da série para busca");
        var nomeSerie = leitura.nextLine();
        var json = consumo.obterDados(ENDERECO + nomeSerie.replace(" ", "+") + API_KEY);
        DadosSerie dados = conversor.obterDados(json, DadosSerie.class);
        return dados;
    }

    private void buscarEpisodioPorSerie(){
        listarSeriesBuscadas();
        System.out.println("Escolha uma série pelo nome");
        var nomeSerie = leitura.nextLine();

        Optional<Serie> serie = series.stream()
                .filter(s -> s.getTitulo().toLowerCase().contains(nomeSerie.toLowerCase()))
                .findFirst();

        if(serie.isPresent()) {

            var serieEncontrada = serie.get();
            List<DadosTemporada> temporadas = new ArrayList<>();

            for (int i = 1; i <= serieEncontrada.getTotalTemporadas(); i++) {
                var json = consumo.obterDados(ENDERECO + serieEncontrada.getTitulo().replace(" ", "+") + "&season=" + i + API_KEY);
                DadosTemporada dadosTemporada = conversor.obterDados(json, DadosTemporada.class);
                temporadas.add(dadosTemporada);
            }
            temporadas.forEach(System.out::println);

            List<Episodio> episodios = temporadas.stream()
                    .flatMap(d -> d.episodios().stream()
                            .map(e -> new Episodio(d.numero(), e)))
                    .collect(Collectors.toList());

            serieEncontrada.setEpisodios(episodios);
            serieRepository.save(serieEncontrada);
        } else {
            System.out.println("Série não encontrada!");
        }
    }

    private void listarSeriesBuscadas(){
        series = serieRepository.findAll();
        series.stream()
                .sorted(Comparator.comparing(Serie::getGenero))
                .forEach(System.out::println);
    }
}
