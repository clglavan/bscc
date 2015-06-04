/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bezier;

import bezier.Bcs;
import bezier.Interfata;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Cristi
 */
public class Pigraphic {

    public static void main(String[] args) throws Exception {

        Bcs bc = new Bcs();
        Interfata ui = new Interfata(bc,600);
        bc.generate_control_points(100, 10000, 0.001);
//        try {
//            bc.import_pi("C:\\Users\\Cristi\\Dropbox\\Licenta\\Application\\src\\constants\\Pi", 100, 0.001, true);
//        } catch (FileNotFoundException ex) {
//            Logger.getLogger(Pigraphic.class.getName()).log(Level.SEVERE, null, ex);
//        } catch (IOException ex) {
//            Logger.getLogger(Pigraphic.class.getName()).log(Level.SEVERE, null, ex);
//        }
        
        bc.draw = true;
        bc.generate = true;
        bc.export=true;
        bc.repaint();
       // ui.piThread(1000, 50);
        
//        ui.grabScreenShot();
    }
}
