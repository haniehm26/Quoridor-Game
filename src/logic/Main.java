package logic;

import java.util.Scanner;

public class Main {
    public static void main(String[] args){
        Scanner scanner = new Scanner(System.in);
        System.out.println("Let's play game!");
        System.out.println("Enter number of players: 2/4");
        int n = scanner.nextInt();
        if (n == 2 || n == 4) {
            Player[] p = new Player[n];
            for (int i = 0; i < p.length; i++) {
                p[i] = new Player();
            }
            Game game = new Game(p);
            game.startGame();
        } else {
            System.out.println("Invalid input!");
        }
    }
}
