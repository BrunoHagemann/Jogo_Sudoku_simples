package br.com.jogo;

import br.com.jogo.model.Board;
import br.com.jogo.model.Space;
import br.com.jogo.util.BoardTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

public class Main {

    private final static Scanner scanner = new Scanner(System.in);

    private static Board board;

    private final static int BOARD_LIMIT = 9;

    public static void main(String[] args) {

        final var positions = Stream.of(args).
                collect(Collectors.toMap(
                        k -> k.split(";")[0],
                        v -> v.split(";")[1]
                ));
        var option = -1;
        while (true){
            System.out.println("""
                    Selecione uma das opiçoes
                    1 - iniciar novo jogo
                    2 - Colocar novo numero 
                    3 - Remover umnumero
                    4 - visualisar jogo atual
                    5 - verificar status do jogo
                    6 - limpar jogo 
                    7 - finalizar jogo 
                    8 - sair
                    """);
            option = scanner.nextInt();

            switch (option){

                case 1 -> startGame(positions);
                case 2 -> inputNumber();
                case 3 -> remuveNumber();
                case 4 -> showCurrentGame();
                case 5 -> showGameStatus();
                case 6 -> cleargame();
                case 7 -> finishgame();
                case 8 -> System.exit(0);
                default -> System.out.println("opição invalida");
            }
        }
    }

    private static void startGame(Map<String, String> positions) {
        if (nonNull(board)){
            System.out.println("o jogo ja foi iniciado");
            return;
        }

        List<List<Space>> spaces = new ArrayList<>();
        for (int i = 0; i < BOARD_LIMIT; i++) {
            spaces.add(new ArrayList<>());
            for (int j = 0; j < BOARD_LIMIT; j++) {
                var positionConfig = positions.get("%s,%s".formatted(i,j));
                var expected = Integer.parseInt(positionConfig.split(",")[0]);
                var fixed = Boolean.parseBoolean(positionConfig.split(",")[1]);
                var correntSpace = new Space(expected , fixed);
                spaces.get(i).add(correntSpace);

            }
        }

        board = new Board(spaces);
        System.out.println("o jogo esta pronto para começar");

    }

    private static void inputNumber() {
        if (isNull(board)){
            System.out.println("o jogo aninda não foi iniciado");
            return;
        }
        System.out.println("informe a coluna em que o numero sera incerido");
        var col = runUntilGetValidNumber(0,8);
        System.out.println("informe a linha em que o numero sera incerido");
        var row = runUntilGetValidNumber(0,8);
        System.out.printf("informe o numero que ira entar na posição [%s,%s]\n", col,row);
        var value = runUntilGetValidNumber(1,9);
        if(!board.changeValue(col,row,value)){
            System.out.printf("a posição [%s,%s] tem um valor fixo\n",col,row);
        }

    }

    private static void remuveNumber() {
        if (isNull(board)){
            System.out.println("o jogo aninda não foi iniciado");
            return;
        }
        System.out.println("informe a coluna em que o numero sera incerido");
        var col = runUntilGetValidNumber(0,8);
        System.out.println("informe a linha em que o numero sera incerido");
        var row = runUntilGetValidNumber(0,8);
        if(!board.clearValue(col,row)){
            System.out.printf("a posição [%s,%s] tem um valor fixo\n",col,row);
        }
    }

    private static void showCurrentGame() {
        if (isNull(board)){
            System.out.println("o jogo aninda não foi iniciado");
            return;
        }

        var args = new Object[81];
        var argPos = 0;
        for (int i = 0; i < BOARD_LIMIT; i++) {
            for (var col: board.getSpaces()){
                args[argPos ++] = " " + ((isNull(col.get(i).getActual()))? " " : col.get(i).getActual());
            }
        }
        System.out.println("seu jogo se encotra da segunte forma");
        System.out.printf((BoardTemplate.Bord_Template) + "%n", args);

    }

    private static void showGameStatus() {
        if (isNull(board)){
            System.out.println("o jogo aninda não foi iniciado");
            return;
        }

        System.out.printf("o jogo atualmente se encontra no status %s\n" , board.getStatus().getLabel());
        if(board.hasErrors()){
            System.out.println("o jogo contei erros");
        }else {
            System.out.println("o jogo não contem erros");
        }
    }

    private static void cleargame() {
        if (isNull(board)){
            System.out.println("o jogo aninda não foi iniciado");
            return;
        }

        System.out.println("Tem ceteza que deseja limpar o jogo e perder o progeresso ?");
        var confirm = scanner.next();
        while (!confirm.equalsIgnoreCase("sim") && !confirm.equalsIgnoreCase("não")){
            System.out.println("informe 'sim' ou 'não' ");
            confirm = scanner.next();
        }

        if (confirm.equalsIgnoreCase("sim")){
            board.reset();
        }

    }

    private static void finishgame() {
        if (isNull(board)){
            System.out.println("o jogo aninda não foi iniciado");
            return;
        }

        if (board.gameIsFineshed()){
            System.out.println("parabens voce concluiu o jogo");
            showCurrentGame();
            board = null;
        } else if (board.hasErrors()) {
            System.out.println("seu jogos tem erros , verifique eles");
        }else {
            System.out.println("voce precisa prenxer algum espaço");
        }
    }


    private static int runUntilGetValidNumber(final int min,final int max){
        var current = scanner.nextInt();
        while (current < min || current > max){
            System.out.printf("informe um numero entre %s e %s\n", min, max);
            current = scanner.nextInt();
        }
        return current;
    }

}