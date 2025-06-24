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
    int velocityY = 0;
    int gravity = 1 ;

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
    void placePipes(){

        int randomPipeY = (int)(pipey - pipeHeight/4 - Math.random()*(pipeHeight/2));
        int openingSpace = height/4;

        Pipe topPipe = new Pipe(topPipImage);
        topPipe.y = randomPipeY;
        pipes.add(topPipe);

        Pipe bottomPipe = new Pipe(bottompipImage);
        bottomPipe.y = topPipe.y + pipeHeight + openingSpace ;
        pipes.add(bottomPipe);

    }
    @Override
    public void paintComponent(Graphics g){
        super.paintComponent(g);
        draw(g);
    }
    public void draw(Graphics g){

        g.drawImage(backgroundImage, 0,0,width,height,null);

        g.drawImage(birdImage, bird.x, bird.y, bird.width,bird.height, null);

        for(int i =0 ; i < pipes.size(); i++){

            Pipe pipe = pipes.get(i);
            g.drawImage(pipe.img, pipe.x, pipe.y, pipe.width, pipe.height,null);

        }

        g.setColor(Color.white);

        g.setFont(new Font("JetBrains Mono",Font.BOLD,32));
        if(gameover){
            g.drawString("Game Over + "+String.valueOf((int) score) , 10, 35);
        }
        else{
            g.drawString(String.valueOf((int)score), 10, 35);
        }

    }

    public void move(){

        velocityY += gravity;
        bird.y = velocityY;
        bird.y = Math.max(bird.y,0);

        for (int i = 0; i < pipes.size(); i++) {
            Pipe pipe = pipes.get(i);
            pipe.x += velocityX;

            if (!pipe.passed && bird.x > pipe.x + pipe.width) {
                score += 0.5; //0.5 because there are 2 pipes! so 0.5*2 = 1, 1 for each set of pipes
                pipe.passed = true;
            }

            if (collision(bird, pipe)) {
                gameover = true;
            }
        }

        if (bird.y > height) {
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
            
            velocityY = -9;

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
