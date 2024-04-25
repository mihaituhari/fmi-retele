import java.io.*;
import java.awt.*;
import java.awt.event.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

class PlayModel extends Frame implements Serializable {
    boolean submitted = false;

    Choice[] numberChoices = new Choice[Config.LOTO_CHOICES];
    TextField nameField;
    Label participantsCounter;

    Button button;

    PlayModel() {
        setLayout(new GridLayout(5, 1));

        // Heading
        Panel headingPanel = new Panel();
        Label headingLabel = new Label("Bine ai venit la Java Loto 🍀");
        headingLabel.setFont(new Font("Arial", Font.BOLD, 24));
        headingPanel.add(headingLabel);
        add(headingPanel);

        // Participants
        Panel participantsPanel = new Panel();
        participantsCounter = new Label("Participanti: ------");
        participantsCounter.setFont(new Font("Arial", Font.BOLD, 18));
        participantsCounter.setForeground(new Color(11, 161, 11));
        participantsPanel.add(participantsCounter);
        add(participantsPanel);

        // Name
        Panel namePanel = new Panel();
        Label nameLabel = new Label("️💁‍ Nume jucator: ");
        namePanel.add(nameLabel);
        nameField = new TextField(25);
        nameField.requestFocus();
        namePanel.add(nameField);
        add(namePanel);

        // Numbers choices
        Panel choicesPanel = new Panel(new FlowLayout());
        for (int i = 0; i < Config.LOTO_CHOICES; i++) {
            numberChoices[i] = new Choice();

            for (int j = 0; j <= Config.LOTO_MAX; j++) { // Add 0 as option
                numberChoices[i].addItem(Integer.toString(j));
            }

            choicesPanel.add(numberChoices[i]);
        }
        add(choicesPanel);

        // Button
        button = new Button("Inscrie numerele! SUCCES! 🍀");
        button.setBackground(new Color(11, 161, 11));
        add(button);

        // Handlers
        ClientHandler H = new ClientHandler(this);
        button.addActionListener(H);
    }
}

class ClientHandler implements ActionListener, Serializable {
    PlayModel play;

    ClientHandler(PlayModel play) {
        this.play = play;
    }

    public void actionPerformed(ActionEvent e) {
        play.submitted = true;
    }
}

class PlayData implements Serializable {
    List<Integer> numbers = new ArrayList<>();

    String name;

    PlayData(PlayModel play) {
        for (int i = 0; i < Config.LOTO_CHOICES; i++) {
            this.numbers.add(Integer.parseInt(play.numberChoices[i].getSelectedItem()));
        }

        this.name = play.nameField.getText();
    }
}

class Client {
    private static volatile boolean running = true;

    public static void main(String[] sss) throws Exception {
        PlayModel play = new PlayModel();

        play.setSize(Config.WINDOW_WIDTH, Config.WINDOW_HEIGHT);
        play.setVisible(true);

        Socket cs = new Socket(Config.HOST, Config.PORT);

        new Thread(() -> {
            try {
                ObjectInputStream ois = new ObjectInputStream(cs.getInputStream());

                while (running) {
                    int participants = (int) ois.readObject();
                    play.participantsCounter.setText("Participanti: " + participants);
                }
            } catch (IOException | ClassNotFoundException e) {
                // Do nothing, connection closed
            }
        }).start();

        while (!play.submitted) {
            Thread.sleep(10);
        }

        OutputStream os = cs.getOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(os);

        PlayData sPlay = new PlayData(play);
        oos.writeObject(sPlay);

        System.out.println("Date trimise la server:");
        System.out.println("\tNume:\t" + sPlay.name);
        System.out.println("\tNumere:\t" + sPlay.numbers);

        running = false;

        play.dispose();
        os.close();
        oos.close();
        cs.close();

        System.exit(0);
    }
}
