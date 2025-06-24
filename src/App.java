import javax.swing.*;

public class App {
    public static void main(String[] args) throws Exception {
         
        int height = 640 ;
        int width = 360 ;

        JFrame frame = new JFrame("Flappy Bird");
        frame.setSize(width, height);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);

        FlappyBird flappyBird = new FlappyBird();
        frame.add(flappyBird);
        frame.pack();



        frame.setVisible(true);

    }
}
