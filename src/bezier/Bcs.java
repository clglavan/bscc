/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bezier;

import java.awt.Color;
import java.awt.Graphics;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.ByteBuffer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JPanel;

/**
 *
 * @author Cristi
 */
public class Bcs extends JPanel {

    public Punct[] puncte = new Punct[1000000];
    ArrayList<Punct[]> curbe = new ArrayList<>();
    Punct p0 = new Punct();
    Punct p1 = new Punct();
    Punct p2 = new Punct();
    double finete = 0.001;
    int maxim = 600;
    int semafor = 0;
    int nr_curbe = 100;
    public int counter = 10;
    public boolean draw = false;
    public boolean generate = false;
    public boolean export = false;
    boolean picolor = false;
    Random rnd;
    String fileName = "binary";
    BufferedOutputStream bs = null;
    File binary = null;
 

    public int getMaxim() {
        return maxim;
    }

    public Punct[] getPuncte() {
        return puncte;
    }

    public void setPuncte(Punct[] puncte) {
        this.puncte = puncte;
    }

    public ArrayList<Punct[]> getCurbe() {
        return curbe;
    }

    public void setCurbe(ArrayList<Punct[]> curbe) {
        this.curbe = curbe;
    }

    public void import_control_points(Punct[] puncte, ArrayList<Punct[]> curbe) {
        this.puncte = puncte;
        this.curbe = curbe;
    }

    public void generate_control_points(int max, int ncurbe, double nfinete) {
        this.maxim = max;
        this.nr_curbe = ncurbe;
        this.finete = nfinete;
       
        

        for (int c = 0; c < nr_curbe; c++) {
            curbe.add(new Punct[10]);
            for (int i = 0; i < 10; i++) {
                curbe.get(c)[i] = new Punct();
            }
        }

        int nr_points = 0;
        Random generator = new Random();
        Random generator2 = new Random();
        curbe.get(0)[0].x = generator.nextInt(maxim) + 1;
        curbe.get(0)[0].y = generator2.nextInt(maxim) + 1;
        for (int c = 0; c < nr_curbe; c++) {
            for (int i = 0; i < 10; i++) {

                if (i == 0 && c > 0) {
                    curbe.get(c)[i] = curbe.get(c - 1)[9];
                } else {
                    generator = new Random();
                    curbe.get(c)[i].x = generator.nextInt(maxim) + 1;
                    generator2 = new Random();
                    curbe.get(c)[i].y = generator2.nextInt(maxim) + 1;

                }
                puncte[nr_points] = curbe.get(c)[i];
                nr_points++;
            }
            System.out.println("Terminat curba " + c);
        }

    }// generate generating points

    Bm module = new Bm();

    public String encrypt(String message) {
        Integer[] returned = new Integer[2];
        int nr = 0;

        for (int z = 0; z < nr_curbe; z++) {  // System.out.println("starting one");

            for (double t = 0; t <= 1; t += finete) {
                p2.x = 0;
                p2.y = 0;

                for (int i = 0; i <= 9; i++) {

                    p2.x += bernstein(i, 10 - 1, t) * curbe.get(z)[i].x;
                    p2.y += bernstein(i, 10 - 1, t) * curbe.get(z)[i].y;

                }
                nr++;
                if (nr == counter && t != 0) {
                    try {

                        String[] tmp = (p2.x + "").split("\\.");
                        String tp = tmp[1].substring(0, 4);

                        returned[0] = Integer.parseInt(tp);
                        System.out.println(returned[0]);

                        String[] tmp2 = (p2.y + "").split("\\.");
                        String tp2 = tmp2[1].substring(0, 4);

                        returned[1] = Integer.parseInt(tp2);
                        System.out.println(returned[1]);

                        counter++;
                        String encrypted = module.crypt_text(message, returned[0], returned[1]);
                        return encrypted;
                    } catch (Exception ex) {
                        Logger.getLogger(Bcs.class.getName()).log(Level.SEVERE, null, ex);
                    }

                }

            }

        }

        return null;
    }

    public String decrypt(String message) {
        Integer[] returned = new Integer[2];
        int nr = 0;

        for (int z = 0; z < nr_curbe; z++) {  // System.out.println("starting one");

            for (double t = 0; t <= 1; t += finete) {
                p2.x = 0;
                p2.y = 0;

                for (int i = 0; i <= 9; i++) {

                    p2.x += bernstein(i, 10 - 1, t) * curbe.get(z)[i].x;
                    p2.y += bernstein(i, 10 - 1, t) * curbe.get(z)[i].y;

                }
                nr++;
                if (nr == counter && t != 0) {
                    try {

                        String[] tmp = (p2.x + "").split("\\.");
                        String tp = tmp[1].substring(0, 4);

                        returned[0] = Integer.parseInt(tp);
                        //System.out.println(returned[0]);

                        String[] tmp2 = (p2.y + "").split("\\.");
                        String tp2 = tmp2[1].substring(0, 4);

                        returned[1] = Integer.parseInt(tp2);
                          //  System.out.println(returned[1]);

                        counter++;
                        String decrypted = module.decrypt_text(message, returned[0], returned[1]);
                        return decrypted;
                    } catch (Exception ex) {
                        Logger.getLogger(Bcs.class.getName()).log(Level.SEVERE, null, ex);
                    }

                }

            }

        }

        return null;
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        int nr = 0;
        FileOutputStream fs;
        if (export) {
            try {
                binary = new File(fileName);
                fs = new FileOutputStream(binary);
                bs = new BufferedOutputStream(fs);
            } catch (FileNotFoundException ex) {
                Logger.getLogger(Bcs.class.getName()).log(Level.SEVERE, null, ex);
            }

        }
        if (generate) {
            g.setColor(Color.BLACK);
            for (int z = 0; z < nr_curbe; z++) {  // System.out.println("starting one");

                if (picolor) {
                    rnd = new Random();
                    int x = rnd.nextInt(9);
                    g.setColor(picol(x));
                }

                for (double t = 0; t <= 1; t += finete) {
                    p2.x = 0;
                    p2.y = 0;

                    for (int i = 0; i <= 9; i++) {

                        p2.x += bernstein(i, 10 - 1, t) * curbe.get(z)[i].x;
                        p2.y += bernstein(i, 10 - 1, t) * curbe.get(z)[i].y;

                    }

                    if (draw) {
                        // if (picolor) {
//                            int x = Double.valueOf(p2.x).intValue() % 10 + Double.valueOf(p2.x).intValue() % 100 + Double.valueOf(p2.x).intValue() % 1000;
//                            int y = Double.valueOf(p2.y).intValue() % 10 + Double.valueOf(p2.y).intValue() % 100 + Double.valueOf(p2.y).intValue() % 1000;

                        //    }
                        nr++;
                        if (nr < counter) {
                            g.setColor(Color.RED);
                            g.fillRect((int) p2.x, (int) p2.y, 1, 1);
                        } else {
                            g.setColor(Color.BLUE);
                            g.fillRect((int) p2.x, (int) p2.y, 1, 1);
                        }
                        // g.setColor(Color.black);
                    }
                    if (export && t != 0) {
                        
                        try {

                            // System.out.println("Exporting...");
                            String[] tmp = (p2.x + "").split("\\.");
                            String tp = tmp[1].substring(0, 4);
                            // System.out.println(Integer.parseInt(tp));
                            bs.write(Integer.parseInt(tp));

                            String[] tmp2 = (p2.y + "").split("\\.");
                            String tp2 = tmp2[1].substring(0, 4);
                            //  System.out.println(Integer.parseInt(tp2));
                            bs.write(Integer.parseInt(tp2));

                        } catch (Exception ex) {
                            Logger.getLogger(Bcs.class.getName()).log(Level.SEVERE, null, ex);
                        }

                    }

                }
                System.out.println("Just drew curve " + z);
            }
        }
        if (export) {
            try {
                bs.close();
                System.out.println("File size "+binary.length());
                System.out.println("Exported.");
            } catch (IOException ex) {
                Logger.getLogger(Bcs.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public static int factorial(int n) {
        if (n <= 1) // base case
        {
            return 1;
        } else {
            return n * factorial(n - 1);
        }
    }

    public int comb(int i, int n) {
        int x = 0;
        int p = factorial(n);
        int r = factorial(i);
        int k = factorial(n - i);
        x = (int) factorial(n) / ((int) (factorial(i)) * (int) (factorial(n - i)));
        return x;
    }

    ;
  public double bernstein(int i, int n, double t) {
        double y = 1;
        double z = 1;
        double p = 1;
        y = Math.pow(t, i);
        //   for(int j=0;j<=i;j++)
        //     y*=t;
        z = Math.pow((1 - t), (n - i));
        //  for(int j=0;j<=n-i;j++)
        //      z*=(1-t);
        p = comb(i, n);

        return y * z * p;
    }

    public void import_pi(String path, int number, double fin, boolean color) throws FileNotFoundException, IOException {
        this.maxim = 999;
        this.nr_curbe = number;
        this.finete = fin;
        this.picolor = color;
        BufferedReader br = new BufferedReader(new FileReader(path));

        char[] temp = new char[3];

        for (int c = 0; c < nr_curbe; c++) {
            curbe.add(new Punct[10]);
            for (int i = 0; i < 10; i++) {
                curbe.get(c)[i] = new Punct();
            }
        }
        try {
            br.read(temp);
        } catch (IOException ex) {
            Logger.getLogger(Bcs.class.getName()).log(Level.SEVERE, null, ex);
        }
        curbe.get(0)[0].x = Integer.parseInt("" + temp[0] + temp[1] + temp[2]);
        curbe.get(0)[0].y = Integer.parseInt("" + temp[2] + temp[1] + temp[0]);

        int nr_points = 0;
        for (int c = 0; c < nr_curbe; c++) {
            for (int i = 0; i < 10; i++) {

                if (i == 0 && c > 0) {
                    curbe.get(c)[i] = curbe.get(c - 1)[9];
                } else {
                    try {
                        br.read(temp);
                    } catch (IOException ex) {
                        Logger.getLogger(Bcs.class.getName()).log(Level.SEVERE, null, ex);
                    }

                    curbe.get(c)[i].x = Integer.parseInt(("" + temp[0] + temp[1] + temp[2]).trim());
                    try {
                        br.read(temp);
                    } catch (IOException ex) {
                        Logger.getLogger(Bcs.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    curbe.get(c)[i].y = Integer.parseInt(("" + temp[0] + temp[1] + temp[2]).trim());

                }
                puncte[nr_points] = curbe.get(c)[i];
                nr_points++;
            }
            System.out.println("Terminat curba " + c);
        }
        br.close();
    }

    private Color picol(int x) {

        switch (x) {
            case 1:
                return Color.yellow;
            case 2:
                return Color.red;
            case 3:
                return Color.BLUE;
            case 4:
                return Color.GREEN;
            case 5:
                return Color.orange;
            case 6:
                return Color.cyan;
            case 7:
                return Color.pink;
            case 8:
                return Color.magenta;
            case 9:
                return Color.darkGray;
            case 0:
                return Color.black;
        }
        return Color.BLACK;
    }

    public static byte[] toByteArray(double value) {
        byte[] bytes = new byte[8];
        ByteBuffer.wrap(bytes).putDouble(value);
        return bytes;
    }

    void writeSmallBinaryFile(byte[] aBytes, String aFileName) throws IOException {
        Path path = Paths.get(aFileName);
        Files.write(path, aBytes); //creates, overwrites
    }

    double getDecimalAsDouble(double x) {
        return BigDecimal.valueOf(2.65d).divideAndRemainder(BigDecimal.ONE)[1].floatValue();
    }

   
}
