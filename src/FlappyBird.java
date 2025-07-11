import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
import javax.swing.*;
import javax.imageio.ImageIO;

public class FlappyBird extends JPanel implements ActionListener,KeyListener{
    
    int height = 640 ;
    int width = 360 ;
    
    Image backgroundImage;
    Image birdImage;
    Image topPipImage;
    Image bottompipImage;

    int birdx = width/8;
    int birdy = height/2;
    int birdWidth = 34 ;
    int birdHeight = 24 ;

    class Bird{

        int x = birdx;
        int y = birdy;
        int width = birdWidth;
        int height = birdHeight;
        Image img;

        Bird(Image img){
            this.img = img;
        }
    }
    int pipex = width;
    int pipey = 0;
    int pipeWidth = 64;
    int pipeHeight = 512;

    class Pipe{

        int x = pipex;
        int y = pipey;
        int width = pipeWidth;
        int height = pipeHeight;
        Image img ;
        boolean passed = false ;

        Pipe(Image img){
            this.img = img ;
        }
    }

    Bird bird;
    int velocityX = -4;
    double velocityY = 0;      
    double gravity = 0.3;      

    ArrayList<Pipe> pipes;
    Random random = new Random();

    Timer gameloop;
    Timer placePipeTimer;
    boolean gameover = false;
    double score = 0;

    FlappyBird(){

        setPreferredSize(new Dimension(width,height));
        setFocusable(true);
        addKeyListener(this);

        try{

            backgroundImage = ImageIO.read(getClass().getResource("/images/flappybirdbg.png"));
            birdImage = ImageIO.read(getClass().getResource("/images/flappybird.png"));
            topPipImage = ImageIO.read(getClass().getResource("/images/toppipe.png"));
            bottompipImage = ImageIO.read(getClass().getResource("/images/bottompipe.png"));

        }catch(IOException e){
            System.out.println("Failed loading image");
            e.printStackTrace();

        }
        bird = new Bird(birdImage);
        pipes = new ArrayList<Pipe>();

        placePipeTimer = new Timer(1500,new ActionListener() {
            public void actionPerformed(ActionEvent e){
                placePipes();
            }            
        });
        placePipeTimer.start();
        
        gameloop = new Timer(1000/60,this);
        gameloop.start();
        
    }
    void placePipes() {
        int openingSpace = height / 4;
        int minY = -pipeHeight + 50;
        int maxY = height - openingSpace - 50 - pipeHeight;
        int randomPipeY = minY + random.nextInt(maxY - minY + 1);

        Pipe topPipe = new Pipe(topPipImage);
        topPipe.x = width;
        topPipe.y = randomPipeY;
        pipes.add(topPipe);

        Pipe bottomPipe = new Pipe(bottompipImage); 
        bottomPipe.x = width;
        bottomPipe.y = topPipe.y + pipeHeight + openingSpace;
        pipes.add(bottomPipe);
    }

    @Override
    public void paintComponent(Graphics g){
        super.paintComponent(g);
        draw(g);
    }
    public void draw(Graphics g){

        g.drawImage(backgroundImage, 0, 0, width, height, null);
        g.drawImage(birdImage, bird.x, bird.y, bird.width, bird.height, null);

        for (Pipe pipe : pipes) {
            g.drawImage(pipe.img, pipe.x, pipe.y, pipe.width, pipe.height, null);
        }

        g.setColor(Color.white);
        g.setFont(new Font("JetBrains Mono", Font.BOLD, 32));
        if (gameover) {
            g.drawString("Game Over: " + (int) score, 10, 35);
        } else {
            g.drawString(String.valueOf((int) score), 10, 35);
        }
    }

    public void move(){

        velocityY += gravity;
        bird.y += velocityY;
        bird.y = Math.max(bird.y,0);

        for (Pipe pipe : pipes) {
            pipe.x += velocityX;

            if (!pipe.passed && bird.x > pipe.x + pipe.width) {
                score += 0.5; 
                pipe.passed = true;
            }

            if (collision(bird, pipe)) {
                gameover = true;
            }
        }

        if (bird.y + bird.height > height) {
            bird.y = height - bird.height;
            gameover = true;
        }
    }
    boolean collision(Bird a, Pipe b) {
    return a.x < b.x + b.width &&   
           a.x + a.width > b.x &&  
           a.y < b.y + b.height && 
           a.y + a.height > b.y;    
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        move();
        repaint();
        if(gameover){
            placePipeTimer.stop();
            gameloop.stop();
        }

    }
    @Override
        public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_SPACE) {
            velocityY = -7; // make jump a bit less strong for smoother feel
            if (gameover) {
                bird.y = birdy;
                velocityY = 0;
                pipes.clear();
                gameover = false;
                score = 0;
                gameloop.start();
                placePipeTimer.start();
            }
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {}

    @Override
    public void keyReleased(KeyEvent e) {}
    

}
