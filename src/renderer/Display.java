/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package renderer;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferStrategy;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;
import renderer.input.ClickType;
import renderer.input.Mouse;
import renderer.point.MyPoint;
import renderer.point.PointConverter;
import renderer.shapes.MyPolygon;
import renderer.shapes.Tetrahedron;

/**
 *
 * @author Salvador Rombe
 */
public class Display extends Canvas implements Runnable{
    private static final long serialVersionUID = 1L;

    private Thread thread;
    private JFrame frame;
    private static String title = "Renderizador 3D";
    public static final int WIDTH = 800;
    public static final int HEIGHT = 600;
    private static boolean running = false;
    
    public MyPolygon poly;
    private Tetrahedron tetra;
    
    private Mouse mouse;
    
    public Display() {
        this.frame = new JFrame();
        
        Dimension size = new Dimension(WIDTH, HEIGHT);
        this.setPreferredSize(size);
        
        this.mouse = new Mouse();
        
        this.addMouseListener(this.mouse);
        this.addMouseMotionListener(this.mouse);
        this.addMouseWheelListener(this.mouse);
    }
    
    public static void main(String[] args) {
        Display display = new Display();
        display.frame.setTitle(title);
        display.frame.add(display);
        display.frame.pack();
        display.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        display.frame.setLocationRelativeTo(null);
        display.frame.setResizable(false);
        display.frame.setVisible(true);
        
        display.start();
    }
    
    public synchronized void start() {
        running = true;
//        System.out.println("java.library.path: " + System.getProperty("java.library.path"));
        this.thread = new Thread(this, "Display");
        this.thread.start();
    }
    public synchronized void stop() {
        running = false;
        try {
            this.thread.join();
        } catch (InterruptedException ex) {
//            Logger.getLogger(Display.class.getName()).log(Level.SEVERE, null, ex);
            ex.printStackTrace();
        }
    }

    @Override 
    public void run() {
//        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        long lastTime = System.nanoTime();
        long timer = System.currentTimeMillis();
        final double ns = 1000000000.0 / 60;
        double delta = 0;  
        int frames = 0;
        
        init();
        
        while (running) {
            long now = System.nanoTime();
            delta += (now - lastTime) / ns;
            lastTime = now;
            while (delta >=1) {
                update();
                delta--;
                render();
                frames++;
            }
//            render();
//            frames++;
            if(System.currentTimeMillis() - timer > 1000) {
                timer += 1000;
                this.frame.setTitle(title + " | " + frames + "fps");
                frames = 0;
            }
        }
        stop();
    }
    
    private void init() {
        // Create a cube, it has 8 points
        int s = 100;
        MyPoint p1 = new MyPoint(s/2, -s/2, -s/2);
        MyPoint p2 = new MyPoint(s/2, s/2, -s/2);
        MyPoint p3 = new MyPoint(s/2, s/2, s/2);
        MyPoint p4 = new MyPoint(s/2, -s/2, s/2);
        MyPoint p5 = new MyPoint(-s/2, -s/2, -s/2);
        MyPoint p6 = new MyPoint(-s/2, s/2, -s/2);
        MyPoint p7 = new MyPoint(-s/2, s/2, s/2);
        MyPoint p8 = new MyPoint(-s/2, -s/2, s/2);
        this.tetra = new Tetrahedron(
            new MyPolygon(Color.BLUE, p5, p6, p7, p8),
            new MyPolygon(Color.WHITE, p1, p2, p6, p5), //Bottom
            new MyPolygon(Color.YELLOW, p1, p5, p8, p4),
            new MyPolygon(Color.GREEN, p2, p6, p7, p3),
            new MyPolygon(Color.ORANGE, p4, p3, p7, p8), //Top
            new MyPolygon(Color.RED ,p1, p2, p3, p4) // Front
        );
//        this.tetra.rotate(true, 10, 0, 0);
    }
    
    private void render() {
        BufferStrategy bs = this.getBufferStrategy();
        if (bs == null) {
            this.createBufferStrategy(3);
            return;
        }
        
        Graphics g = bs.getDrawGraphics();
        
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, WIDTH * 2, HEIGHT * 2);
        
        tetra.render(g);
        
        g.dispose();
        bs.show();
    }
    
    ClickType prevMouse = ClickType.Unknown;
    int initialX, initialY;    
    double mouseSensitivity = 7;
    private void update() {
        int x = this.mouse.getX();
        int y = this.mouse.getY();
        if(this.mouse.getButton() == ClickType.LeftClick) {            
            int xDif = x - initialX;
            int yDif = y - initialY;
            this.tetra.rotate(true, 0, -yDif/mouseSensitivity, -xDif/mouseSensitivity);
//            this.tetra.rotate(true, 0, 0, -xDif/mouseSensitivity);
//            this.tetra.rotate(true, 0, 0, -yDif/mouseSensitivity);

        }
        else if(this.mouse.getButton() == ClickType.RightClick) {
            int xDif = x - initialX;
            
            this.tetra.rotate(true,- xDif/mouseSensitivity, 0, 0);
        }
        
        if(this.mouse.isScrollingUp()) {
            PointConverter.zoomIn();

        }
        else if(this.mouse.isScrollingDown()) {
            PointConverter.zoomOut();
        }
        
        this.mouse.resetScroll();
        initialX = x;
        initialY = y;
        
//        Comentar a linha abaixo para parar a rotacao automatica
//        this.tetra.rotate(true, 0, 0, 1);
//        System.out.println(this.mouse.getX() + "x" + this.mouse.getY());
//        System.out.println(this.mouse.getButton());
//        this.mouse.resetButton();
    }
}
