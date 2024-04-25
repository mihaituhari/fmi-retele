import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;

class Server {
    public static void main(String[] sss) throws Exception {
        System.out.println("🍀 Java Loto Server 🍀");

        System.out.println("\nServerul porneste 🚀");
        System.out.println(" → Host: " + Config.HOST);
        System.out.println(" → Port: " + Config.PORT);
        System.out.println(" → Conexiuni: " + Config.ALLOWED_CONNECTIONS);

        Game.generateWinningNumbers();
        System.out.println("\nNumerele castigatoare sunt: " + Game.winningNumbers);

        System.out.println("\nSe asteapta conexiuni de la jucatori ...");

        Game game = new Game();
        ServerSocket ss = new ServerSocket(Config.PORT);
        Socket cs;

        for (int i = 0; i < Config.ALLOWED_CONNECTIONS; i++) {
            cs = ss.accept();
            new Connection(cs, game);
        }

        ss.close();
    }
}

class Connection extends Thread {
    Game game;
    Socket cs;

    Connection(Socket cs, Game game) throws Exception {
        this.cs = cs;
        this.game = game;

        start();
    }

    public void run() {
        try {
            InputStream is = cs.getInputStream();
            ObjectInputStream ois = new ObjectInputStream(is);

            PlayData data = (PlayData) ois.readObject();
            game.submitPlayer(data);

            cs.close();
            is.close();
            ois.close();
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}

class Game {
    public static List<Integer> winningNumbers = new ArrayList<>();

    public static int winners = 0;

    public static void generateWinningNumbers() {
        Random rand = new Random();
        while (winningNumbers.size() < Config.LOTO_CHOICES) {
            int num = rand.nextInt(Config.LOTO_MAX) + 1;

            if (!winningNumbers.contains(num)) {
                winningNumbers.add(num);
            }
        }
    }

    synchronized void submitPlayer(PlayData data) {
        Collections.sort(data.numbers);
        Collections.sort(winningNumbers);

        boolean isWinner = data.numbers.equals(winningNumbers);
        boolean isCheater = Objects.equals(data.name, Config.CHEAT_WINNER_NAME);

        System.out.println("\n → Jucator [" + data.name + "] cu numerele " + data.numbers);

        if (isWinner || isCheater) {
            winners++;
            System.out.println("   Castigator (" + winners + ") " + (isCheater ? "🤡" : "🎉"));
        } else {
            System.out.println("   Necastigator 😢");
        }
    }
}
