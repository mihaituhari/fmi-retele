import java.io.*;
import java.awt.*;
import java.awt.event.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

class PlayModel extends Frame implements Serializable {
    boolean submitted = false;

    Choice[] numberChoices = new Choice[Config.LOTO_CHOICES];
    Label nameLabel;
    TextField nameField;
    Button button;

    PlayModel() {
        setLayout(new GridLayout(4, 1));

        // Heading
        Panel headingPanel = new Panel();
        Label headingLabel = new Label("Bine ai venit la Java Loto 🍀");
        headingLabel.setFont(new Font("Arial", Font.BOLD, 24));
        headingPanel.add(headingLabel);
        add(headingPanel);

        // Name
        Panel namePanel = new Panel();
        nameLabel = new Label("️💁‍ Nume jucator: ");
        namePanel.add(nameLabel);
        nameField = new TextField(25);
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
    public static void main(String[] sss) throws Exception {
        PlayModel play = new PlayModel();

        play.setSize(Config.WINDOW_WIDTH, Config.WINDOW_HEIGHT);
        play.setVisible(true);

        while (!play.submitted) {
            Thread.sleep(10);
        }

        Socket cs = new Socket(Config.HOST, Config.PORT);
        OutputStream os = cs.getOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(os);

        PlayData sPlay = new PlayData(play);
        oos.writeObject(sPlay);

        System.out.println("Date trimise la server:");
        System.out.println("\tNume:\t" + sPlay.name);
        System.out.println("\tNumere:\t" + sPlay.numbers);

        play.dispose();
        os.close();
        oos.close();
        cs.close();
    }
}
