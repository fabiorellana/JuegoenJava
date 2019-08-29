import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;
import javax.swing.JPanel;
import javax.swing.Timer;

public class JuegoPanel extends JPanel implements KeyListener {
    //Variables globales
    public Rectangle2D nave;
    public Timer t,enemigas;
    public int xNave = 240, enemigasIni=20, enemigasAumentoX=0, enemigasAumentoY=0, moviEnemigas=10;
    public int aleatorio1, aleatorio2, contadorCaer=0, disparadas=0, contadorRecargar=0;
    public Ellipse2D [] disparos = new Ellipse2D[20];
    public int [] xDisparos = new int[20];
    public int [] yDisparos = new int[20];
    public boolean derechaNave=false, izquierdaNave=true, derechaEnemigas=true, izquierdaEnemigas=false, segundaFila=false;
    public boolean caer=false;
    public Graphics2D g2;
    public Rectangle2D[][] navesEnemigas = new Rectangle2D[2][5];
    public int [][]yEnemigas = new int[2][5];
    
    public JuegoPanel(String nombre) {
        this.setBackground(Color.WHITE);
        this.setFocusable(true);
        this.setBounds(300, 100, 500, 500);
        this.setPreferredSize(new Dimension(500, 500));
        this.addKeyListener(this);
        timerNave();
        t.start();
        inicializarDisparos();
        inicializarAltura();
        timerEnemigas();
        enemigas.start();
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
        for(int i = 0; i < 20; i++){
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
        t = new Timer(10, new ActionListener(){
            public void actionPerformed(ActionEvent e){
                if(derechaNave && xNave <= 440){
                    xNave+=5;
                }
                
                if(izquierdaNave && xNave >= 0){
                    xNave-=5;
                }
                
                for(int i = 0; i < disparadas; i++){
                    yDisparos[i]-=3;
                }
                repaint(); 
            }
        });
    }
    
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g2 = (Graphics2D)g;
        g2.setColor(Color.BLACK);
        
        nave = new Rectangle2D.Double(xNave, 410, 60, 90);
        g2.fill(nave);
        
        //Dibujar balas
        for(int i = 0; i < disparadas; i++){
            disparos[i] = new Ellipse2D.Double(xDisparos[i], yDisparos[i], 10, 20);
            g2.fill(disparos[i]);
        }
        inicializarEnemigas();
    }
    
    public void timerEnemigas(){
        enemigas = new Timer(90, new ActionListener(){
            public void actionPerformed(ActionEvent e){
                if(derechaEnemigas){
                    moviEnemigas+=5;
                    if(moviEnemigas >= 45){
                        derechaEnemigas = false;
                        izquierdaEnemigas = true;
                        contadorCaer++;
                    }
                }
                
                if(izquierdaEnemigas){
                    moviEnemigas-=5;
                    if(moviEnemigas <= 20){
                        derechaEnemigas = true;
                        izquierdaEnemigas = false;
                    }
                }
                
                if(contadorCaer >= 3){
                    atacar();
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
                navesEnemigas[filas][columnas] = new Rectangle2D.Double(enemigasIni+enemigasAumentoX+moviEnemigas, yEnemigas[filas][columnas], 60, 90);
                enemigasAumentoX+=80;
                g2.fill(navesEnemigas[filas][columnas]);
            }
            enemigasAumentoX = 0;
            enemigasAumentoY = 100;
        }
        repaint();
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
            if(contadorRecargar < 20){
                xDisparos[disparadas] = (int) nave.getCenterX();
                disparadas++;
                contadorRecargar++;
            }
        }
        
        if(e.getKeyCode() == KeyEvent.VK_R) {
            System.out.println("Recargandos");
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {      
    }
}
