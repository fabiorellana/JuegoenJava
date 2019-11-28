import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.JPanel;
import javax.swing.Timer;

public class JuegoPanel extends JPanel implements KeyListener {
    //Variables globales
    public Rectangle2D nave;
    public Timer t,enemigas;
    public int xNave = 240, enemigasIni=20, enemigasAumentoX=0, enemigasAumentoY=0, moviEnemigas=10;
    public int aleatorio1, aleatorio2, contadorCaer=0, disparadas=0, contadorRecargar=0;
    public Ellipse2D [] disparos = new Ellipse2D[15];
    public int [] xDisparos = new int[15];
    public int [] yDisparos = new int[15];
    public boolean derechaNave=false, izquierdaNave=true, derechaEnemigas=true, izquierdaEnemigas=false, segundaFila=false;
    public boolean caer=false, acabado=false, fin=false;
    public Graphics2D g2;
    public Rectangle2D[][] navesEnemigas = new Rectangle2D[2][5];
    public int [][]yEnemigas = new int[2][5];
    public int vidas = 10, nivel = 1, score = 0, balas = 15, puntos = 100, variable = 5;
    public int [][] vivas = new int[2][5];
    public int tiempoNave = 15, tiempoEnemigas = 90;
    public Image espacio, naveImagen;
    public Image []balasImagen = new Image[15];
    public Image [][]imagenEnemiga = new Image[2][5];
    
    public JuegoPanel(String nombre) {
        this.setBackground(Color.WHITE);
        this.setFocusable(true);
        this.setBounds(300, 100, 500, 500);
        this.setPreferredSize(new Dimension(500, 500));
        this.addKeyListener(this);
        
        try {
            espacio = ImageIO.read(new File("espacio3.png"));
            naveImagen = ImageIO.read(new File("navephotoshop.png"));
            for(int i = 0; i < 15; i++){
                balasImagen[i] = ImageIO.read(new File("balaphotoshop.png"));
            }
            for(int i = 0; i < 2; i++){
                for(int j = 0; j < 5; j++){
                    imagenEnemiga[i][j] = ImageIO.read(new File("enemigas.png"));
                }
            }
        } catch (IOException ex) {
            Logger.getLogger(JuegoPanel.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        timerNave();
        t.start();
        inicializarDisparos();
        inicializarAltura();
        inicializarVivas();
        timerEnemigas();
        enemigas.start();
        
        if(nombre.equals("Fabian")){
            vidas = 40;
        }
        
        if(nombre.equals("Java")){
            puntos = 1000;
        }
        
    }
    
    public void inicializarVivas(){
        for(int i = 0; i < 2; i++){
            for(int j = 0; j < 5; j++){
              vivas[i][j] = 1;
            }
        }
    }
    
    public void atacar(){
        if(caer == false){
            while(yEnemigas[aleatorio1][aleatorio2] >= 550){
                aleatorio1 = (int)(Math.random()*2);
                aleatorio2 = (int)(Math.random()*5);    
            }
        }
        yEnemigas[aleatorio1][aleatorio2]+=7;
        
        if(yEnemigas[aleatorio1][aleatorio2] >= 550){
            caer = false;
            contadorCaer = 0;
        }
    }
    
    public void inicializarDisparos(){
        for(int i = 0; i < 15; i++){
            yDisparos[i] = 410;
        }
    }
    
    public void inicializarAltura(){
        for(int fila = 0; fila < 2; fila++){
            for(int columna = 0; columna < 5; columna++){
                yEnemigas[fila][columna] = 20;
                
                if(segundaFila){
                    yEnemigas[fila][columna] = 120;
                }
            }
            segundaFila = true;
        }
    }
    
    public void timerNave(){
        t = new Timer(tiempoNave, new ActionListener(){
            public void actionPerformed(ActionEvent e){
                if(derechaNave && xNave <= 440){
                    xNave+=5;
                }
                
                if(izquierdaNave && xNave >= 0){
                    xNave-=5;
                }
                
                for(int i = 0; i < disparadas; i++){
                    if(yDisparos[i] >= -20){
                        yDisparos[i]-=3;
                    }
                }
                
                repaint(); 
            }
        });
    }
    
    public void interseccionBalas(){
        for(int i = 0; i < 2; i++){
            for(int j = 0; j < 5; j++){
                for(int k = 0; k < disparadas; k++){
                    if(disparos[k].intersects(navesEnemigas[i][j])){
                        yDisparos[k] = -40;
                        yEnemigas[i][j] = 600;
                        score+=puntos;
                        vivas[i][j] = 0;
                    }
                }
            }
        }
    }
    
    public int revisarVivas(){
        for(int i = 0; i < 2; i++){
            for(int j = 0; j < 5; j++){
                if(yEnemigas[i][j] >= 530){
                    vivas[i][j] = 0;
                }
                
                if(vivas[i][j] == 1){
                    return 1;
                }
            }
        }
        gameOver();
        return 1;
    }
    
    public void gameOver(){
        acabado = true;
        g2.drawString("Game Over\nPresione Enter para continuar\n", 100, 200);
        t.stop();
        enemigas.stop();
    }
    
    public int restarVidas(){
        for(int i = 0; i < 2; i++){
            for(int j = 0; j < 5; j++){
                if(yEnemigas[i][j] >= 520 && vivas[i][j] == 1){
                    //vidas = vidas - 1     vidas--;
                    vidas-=1;
                    vivas[i][j] = 0;
                    return 1;
                }
            }
        }
        
        return 1;
    }
    
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g2 = (Graphics2D)g;
        g2.drawImage(espacio, 0, 0 , 500, 500, this);
        g2.setColor(Color.WHITE);
        nave = new Rectangle2D.Double(xNave, 410, 60, 90);
        //g2.fill(nave);
        g2.drawImage(naveImagen, xNave-5,410,80,105, this);
        //Dibujar balas
        for(int i = 0; i < disparadas; i++){
            disparos[i] = new Ellipse2D.Double(xDisparos[i], yDisparos[i], 10, 20);
            //g2.fill(disparos[i]);
            g2.drawImage(balasImagen[i], xDisparos[i], yDisparos[i], 15, 20, this);
        }
        
        if(vidas == 0){
           fin = true;
           t.stop();
           enemigas.stop();
        }
        escribir();
        interseccionBalas();
        revisarVivas();
        inicializarEnemigas();
        restarVidas();
    }
    
    public void escribir(){
        g2.setFont(new Font("Tahoma", Font.BOLD, 16));
        g2.drawString("Vidas:  "+vidas, 20, 20);
        g2.drawString("Nivel:  "+nivel, 150, 20);
        g2.drawString("Score:  "+score, 260, 20);
        g2.drawString("Balas:  "+balas, 370, 20);
        if(contadorRecargar == 5 && disparadas < 15){
            g2.drawString("Presiona R para recargar", 250, 300);
        }
        if(fin){
            g2.drawString("Perdiste presiona F para reiniciar", 200, 300);
        }
    }
    
    public void timerEnemigas(){
        enemigas = new Timer(tiempoEnemigas, new ActionListener(){
            public void actionPerformed(ActionEvent e){
                if(derechaEnemigas){
                    moviEnemigas+=5;
                    if(moviEnemigas >= 80){
                        derechaEnemigas = false;
                        izquierdaEnemigas = true;
                        contadorCaer++;
                    }
                }
                
                if(izquierdaEnemigas){
                    moviEnemigas-=5;
                    if(moviEnemigas <= -15){
                        derechaEnemigas = true;
                        izquierdaEnemigas = false;
                    }
                }
                
                if(contadorCaer >= 3){
                    atacar();
                }
                
                for(int i = 0; i < 2; i++){
                    for(int j = 0; j < 5; j++){
                        if(nave.intersects(navesEnemigas[i][j])){
                            System.out.println("Has perdido!!");
                            fin = true;
                            t.stop();
                            enemigas.stop();
                        }
                    }
                }
                
                repaint(); 
            }
        });
    }
    
    public void inicializarEnemigas(){
        enemigasAumentoX = 0;
        enemigasAumentoY = 0;
            
        for(int filas = 0; filas < 2; filas++){
            for(int columnas = 0; columnas < 5; columnas++){
                navesEnemigas[filas][columnas] = new Rectangle2D.Double(enemigasIni+enemigasAumentoX+moviEnemigas, yEnemigas[filas][columnas]+10, 60, 90);
                g2.drawImage(imagenEnemiga[filas][columnas], enemigasIni+enemigasAumentoX+moviEnemigas, yEnemigas[filas][columnas]+10, 80, 100, this);
                enemigasAumentoX+=80;
                //g2.fill(navesEnemigas[filas][columnas]);
            }
            enemigasAumentoX = 0;
            enemigasAumentoY = 100;
        }
        repaint();
    }
    
    public void reiniciar(){
        nivel+=1;
        puntos+=100;
        balas = 15;
        disparadas = 0;
        segundaFila = false;
        contadorRecargar = 0;
        contadorCaer = 0;
        caer = false;
        fin = false;
        acabado = false;
        inicializarDisparos();
        inicializarAltura();
        inicializarVivas();
        timerNave();
        t.start();
        enemigas.start();
        
        if(nivel%2==0 && tiempoNave >= 2){
            tiempoNave--;
        }
        
        if(tiempoEnemigas >= 9){
            tiempoEnemigas-=8;
        }
    }
    
        public void derrota(){
        nivel = 1;
        puntos = 100;
        score = 0;
        balas = 15;
        disparadas = 0;
        segundaFila = false;
        contadorRecargar = 0;
        contadorCaer = 0;
        caer = false;
        fin = false;
        acabado = false;
        inicializarDisparos();
        inicializarAltura();
        inicializarVivas();
        timerNave();
        t.start();
        enemigas.start();
        tiempoNave = 15;
        tiempoEnemigas = 90;
    }


    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if(e.getKeyCode() == KeyEvent.VK_D) {
            derechaNave = true;
            izquierdaNave = false;
        }
        
        if(e.getKeyCode() == KeyEvent.VK_A) {
            derechaNave = false;
            izquierdaNave = true;
        }
          
        if(e.getKeyCode() == KeyEvent.VK_SPACE) {
            System.out.println("Disparar");
            if(contadorRecargar < variable && disparadas < 15){
                xDisparos[disparadas] = (int) nave.getCenterX();
                disparadas++;
                balas--;
                contadorRecargar++;
            }
        }
        
        if(e.getKeyCode() == KeyEvent.VK_R ) {
            System.out.println("Recargandos");
            if(contadorRecargar == 5){
                contadorRecargar = 0;   
            }
        }
        
        if(e.getKeyCode() == KeyEvent.VK_ENTER && acabado == true ) {
            reiniciar();
        }
        
        if(e.getKeyCode() == KeyEvent.VK_F && fin == true ) {
            derrota();
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {      
    }
}
