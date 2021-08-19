/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package banksystem.view;

import banksystem.controller.BankSystem;
import banksystem.modal.DBConnect;
import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.Arc2D;
import java.awt.geom.Area;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JPanel;

/**
 *
 * @author killer
 */
public class GraphicPanel extends JPanel implements Runnable{
        public static boolean  updatePage = true;
        public static boolean deatalied=false;
        private int angel,liraangel,dollarangel,euroangel,filsangel;
        private final int x = 375;
        private final int  y = 75;
        private final int  outerradious =200;
        private final int  insiderradious =160;
        private int x0,y0;
        private double l,d,e,f;
        private Thread t;
        private Area a,b,la,da,ea,fa;
        GraphicPanel(){
            super();
            
        }
        @Override
        public void paint(Graphics g){
            paintComponent(g);
        }
        @Override
        public void paintComponent(Graphics g){
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D)g;
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
            g2d.setColor(new Color(0,47,87));
            g2d.fillOval(x, y, outerradious, outerradious);
            g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.6f));
            x0 = x + outerradious/2 - insiderradious/2;
            y0 = y + outerradious/2 - insiderradious/2;
            if(deatalied){
                countratio(angel);
                la = new Area(new Arc2D.Float(x, y, outerradious, outerradious,90,liraangel,2));
                    b = new Area(new Arc2D.Float(x0, y0, insiderradious, insiderradious,90,liraangel,2));
                    la.subtract(b);
                    g2d.setColor(Color.black);
                    g2d.fill(la);
                da = new Area(new Arc2D.Float(x, y, outerradious, outerradious,liraangel+90,dollarangel,2));
                    b = new Area(new Arc2D.Float(x0, y0, insiderradious, insiderradious,liraangel+90,dollarangel,2));
                    da.subtract(b);
                    g2d.setColor(Color.green);
                    g2d.fill(da);
                ea = new Area(new Arc2D.Float(x, y, outerradious, outerradious,dollarangel+liraangel+90,euroangel,2));
                    b = new Area(new Arc2D.Float(x0, y0, insiderradious, insiderradious,dollarangel+liraangel+90,euroangel,2));
                    ea.subtract(b);
                    g2d.setColor(Color.red);
                    g2d.fill(ea);
                fa = new Area(new Arc2D.Float(x, y, outerradious, outerradious,euroangel+dollarangel+liraangel+90,filsangel,2));
                    b = new Area(new Arc2D.Float(x0, y0, insiderradious, insiderradious,euroangel+dollarangel+liraangel+90,filsangel,2));
                    fa.subtract(b);
                    g2d.setColor(Color.cyan);
                    g2d.fill(fa);
                }
            else {
                g2d.setColor(Color.cyan);
                a = new Area(new Arc2D.Float(x, y, outerradious, outerradious,90,angel,2));
                b = new Area(new Arc2D.Float(x0,y0, insiderradious, insiderradious,90,angel,2));
                a.subtract(b);
                g2d.fill(a);
            }
            if(angel <= -360){
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_DEFAULT);
                g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC,1f));
                g.setColor(new Color(0,153,255));
                g.setFont(new Font("Arial",Font.BOLD,36));
                g2d.drawString(String.valueOf(getTotalMoney()),x+insiderradious/2-30,y+insiderradious/2+30);
            }
            else {
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_DEFAULT);
                g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC, 1f));
                g.setColor(new Color(0,153,255));
                g.setFont(new Font("Arial",Font.BOLD,36));
                g2d.drawString("00,00",x+insiderradious/2-20,y+insiderradious/2+20);
            }
            g.dispose();
        }
        @Override
        public void addNotify() {
            super.addNotify();
            t = new Thread(this);
            t.start();
        }
        @Override
        public void run() {
           while(true){
                if(updatePage){
                    for(int i=0;i<361;i++){
                        try {
                            Thread.sleep(1);
                            angel=-i;
                            repaint();
                        } catch (InterruptedException ex) {
                            
                        }
                    }
                    updatePage=false;    
                }
               try {
                   Thread.sleep(100);
               } catch (InterruptedException ex) {
                   Logger.getLogger(GraphicPanel.class.getName()).log(Level.SEVERE, null, ex);
               }
           }
        }

    private void countratio(int angel) {
        double totalMoney = getTotalMoney();
        liraangel =(int) ((double)(l/totalMoney)*angel);
    }
    private double getTotalMoney(){
        BankSystem bankSystem = new BankSystem();
        long id = bankSystem.getID();
        double res=0; 
        try {
                DBConnect.Connect();
                DBConnect.res = DBConnect.resultStatment("select lira from bank.lira_account where id = "+id+";");
                if(DBConnect.res.next()){
                   res=DBConnect.res.getDouble("lira");
                }
        } catch (SQLException ex) {
                    
        }
        finally{
            DBConnect.close();
        }
        return res;
    }
}
