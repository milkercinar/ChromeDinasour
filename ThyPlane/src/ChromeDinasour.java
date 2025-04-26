import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import javax.swing.*;

public class ChromeDinasour extends JPanel implements ActionListener, KeyListener {

    int boardWidth = 750;
    int boardHeight = 250;


    Image dinasaurImg;
    Image dinasaurDeadImg;
    Image dinasaurJumpImg;
    Image cactus1Img;
    Image cactus2Img;
    Image cactus3Img;

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_SPACE){
            if (dinasour.y == dinasourY){
                velocityY = -17;
                dinasour.img = dinasaurJumpImg;
            }
            if (gameOver){
                //restart game by resetting conditions
                dinasour.y = dinasourY;
                dinasour.img = dinasaurImg;
                velocityY = 0;
                cactusArray.clear();
                score = 0;
                gameOver = false;
                gameLoop.start();
                placeCactusTimer.start();
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }

    class Block{
        int x;
        int y;
        int width;
        int height;
        Image img;

        Block(int x, int y, int width, int height, Image img){
            this.x= x;
            this.y=y;
            this.width=width;
            this.height=height;
            this.img=img;
        }
    }

    //dinasour
    int dinasourWidth = 88;
    int dinosaurHeight= 94;
    int dinasourX = 50;
    int dinasourY = boardHeight - dinosaurHeight;

    Block dinasour;

    //cactus
    int cactus1Width = 34;
    int cactus2Width = 69;
    int cactus3Width = 102;

    int cactusHeight = 70;
    int cactusX = 700;
    int cactusY = boardHeight - cactusHeight;
    ArrayList<Block> cactusArray;



    //physics
    int velocityX = -12;//cactus moving left speed
    int velocityY = 0; // dinasour jump speed
    int gravity = 1;

    boolean gameOver = false;
    int score = 0;

    Timer gameLoop;
    Timer placeCactusTimer;



    public ChromeDinasour(){
        setPreferredSize(new Dimension(boardWidth,boardHeight));
        setBackground(Color.lightGray);
        setFocusable(true);
        addKeyListener(this);


        dinasaurImg = new  ImageIcon(getClass().getResource("./img/dino-run.gif")).getImage();
        dinasaurDeadImg = new ImageIcon(getClass().getResource("./img/dino-dead.png")).getImage();
        dinasaurJumpImg = new ImageIcon(getClass().getResource("./img/dino-jump.png")).getImage();
        cactus1Img = new ImageIcon(getClass().getResource("./img/cactus1.png")).getImage();
        cactus2Img = new ImageIcon(getClass().getResource("./img/cactus2.png")).getImage();
        cactus3Img = new ImageIcon(getClass().getResource("./img/cactus3.png")).getImage();



        //dinasour
        dinasour = new Block(dinasourX,dinasourY,dinasourWidth,dinosaurHeight,dinasaurImg);
        //cactus
        cactusArray = new ArrayList<Block>();
        // game time
        gameLoop = new Timer(1000/60, this);
        gameLoop.start();


        //place cactus timer
        placeCactusTimer = new Timer(1500, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                 placeCactus();
            }
        });
        placeCactusTimer.start();
    }

    void placeCactus(){
        if (gameOver){
            return;
        }
        double placeCactusChance = Math.random();//0-0.9999
        if (placeCactusChance > .90){// 10% you get cactus3
            Block cactus = new Block(cactusX,cactusY,cactus3Width,cactusHeight,cactus3Img);
            cactusArray.add(cactus);
        } else if (placeCactusChance > .70) {// 20% you get cactus2
            Block cactus = new Block(cactusX,cactusY,cactus2Width,cactusHeight,cactus2Img);
            cactusArray.add(cactus);
        } else if (placeCactusChance > .50) {
            Block cactus = new Block(cactusX,cactusY,cactus3Width,cactusHeight,cactus3Img);
            cactusArray.add(cactus);
        }

        if (cactusArray.size()>10){
            cactusArray.remove(0);//remove the first cactus from ArrayList
        }
    }

    public void paintComponent(Graphics g){
        super.paintComponent(g);
        draw(g);
    }
    public void  draw(Graphics g){
        //dinasour
        g.drawImage(dinasour.img, dinasour.x, dinasour.y, dinasour.width, dinasour.height, null);
        //cactus
        for (int i =0; i<cactusArray.size(); i++){
            Block cactus = cactusArray.get(i);
            g.drawImage(cactus.img,cactus.x,cactus.y,cactus.width,cactus.height,null);

        }
        g.setColor(Color.black);
        g.setFont(new Font("Courier",Font.PLAIN,32));
        if (gameOver) {
            g.drawString("Game Over: " + String.valueOf(score), 10, 35);
        }
        else {
            g.drawString(String.valueOf(score), 10, 35);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        move();
        repaint();
        if (gameOver){
            placeCactusTimer.stop();
            gameLoop.stop();
        }

    }
    boolean collision(Block a, Block b) {
        return a.x < b.x + b.width &&   //a's top left corner doesn't reach b's top right corner
                a.x + a.width > b.x &&   //a's top right corner passes b's top left corner
                a.y < b.y + b.height &&  //a's top left corner doesn't reach b's bottom left corner
                a.y + a.height > b.y;    //a's bottom left corner passes b's top left corner
    }


    public void move(){
        velocityY += gravity;
        dinasour.y += velocityY;

        if (dinasour.y > dinasourY){
            dinasour.y = dinasourY;
            velocityY = 0;
            dinasour.img = dinasaurImg;
        }

        for (int i = 0;i<cactusArray.size();i++){
            Block cactus = cactusArray.get(i);
            cactus.x += velocityX;

            if (collision(dinasour,cactus)){
                gameOver = true;
                dinasour.img = dinasaurDeadImg;
            }
        }
        //score
        score++;
    }
}
