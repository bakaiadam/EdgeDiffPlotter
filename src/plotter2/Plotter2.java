/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package plotter2;

import java.awt.Toolkit;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;
import org.math.plot.Plot2DPanel;

/**
 *
 * @author adam
 */
public class Plotter2 {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        Data data=null;
        if (args.length>0)
            data=new Data(args[0]);
        else
            data=new Data("/munka/hobbip/java/plotolas/meas_33.in.txt");
        
    }

    private static class Data {
DataInputStream r=null;
List<Long> values=new LinkedList<Long>();
        private Data(String string) {
            //InputStreamReader r=null;
            try {
                r=new DataInputStream(new FileInputStream(string));
            } catch (FileNotFoundException ex) {
                ex.printStackTrace();
                System.err.println("no such file:(");
            }
            try {
                while (r.available()!=0)
                  read_value();
            } catch (IOException ex) {
                //Logger.getLogger(Plotter2.class.getName()).log(Level.SEVERE, 
            }
/*            int v=0;
            for (Long l:values)
            {
                System.err.println(Long.toString(l)+" "+v/2);
                v=(v+1)%4;
            }
             */
              int v=0;
              long sum_v=0;
              long last=values.get(0);
              
              double[] x = new double[values.size()*2];
  double[] y = new double[values.size()*2];
 
  int i=0;
            for (Long l:values)
            {
                System.err.println(Long.toString((l+sum_v)/10000)+" "+v);
                x[i]=l+sum_v;
                y[i]=v;
                i++;
                //v=(v+1)%4;
                v=(v+1)%2;
                System.err.println(Long.toString((l+sum_v)/10000+1 )+" "+v);
                x[i]=l+1+sum_v;
                y[i]=v;
                i++;
                
                if (l<last)
                    sum_v+=0x1<<24;
            }
             JFrame frame = new JFrame("a plot panel");
              Plot2DPanel plot = new Plot2DPanel();
 
  // add a line plot to the PlotPanel
              System.err.println("start");
  plot.addLinePlot("my plot", x, y);
  System.err.println("stop");
  frame.setContentPane(plot);
  int w = Toolkit.getDefaultToolkit().getScreenSize().width;
  int h = Toolkit.getDefaultToolkit().getScreenSize().height;
frame.setSize(w,h);
  frame.setVisible(true);
  
     //       throw new UnsupportedOperationException("Not yet implemented")
        }
final static int bytenum=3;
        private void read_value() {
            
            byte[] c=new byte[bytenum];
            try {
                //System.err.println( r.read(c));
                r.readByte();//nem kell a 0f
                r.read(c);
            } catch (IOException ex) {
                ex.printStackTrace();
                System.err.println("error with reading "+bytenum+"bytes");
            }
            long n=0;
            for (int i=0;i<bytenum;i++)
            {
                //System.err.println(Long.toHexString( c[i] &0xff  )  );
                n+=c[i] & 0xff;
                //System.err.println(Long.toHexString( n   )  );
                n=n<<8;
                //System.err.println(Long.toHexString( n   )  );
            }
            n=n>>8;
            values.add(n);
        }
    }
}