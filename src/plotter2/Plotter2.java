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
            data=new NewData(args[0]);
        else
            data=new NewData("meas_poll.in.txt");
        data.process();
        data.plot();
    }

    private static class Data {
DataInputStream r=null;
List<Long> valuesx=new LinkedList<Long>();
List<Long> valuesy=new LinkedList<Long>();

             double[] x = null;
  double[] y = null;
 
  
        private Data(String string) {
            //InputStreamReader r=null;
            try {
                r=new DataInputStream(new FileInputStream(string));
            } catch (FileNotFoundException ex) {
                ex.printStackTrace();
                System.err.println("no such file:(");
            }
            
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
            valuesx.add(n);
        }
        protected void postprocess_values() //hogy ne forudljon korbe
        {
            List<Long> new_valuesx=new LinkedList<Long>();
            long sum_v=0;
            long last= valuesx.get(0);
            for (Long l:valuesx)
            {
                new_valuesx.add(l+sum_v);
                if (l<last)
                    sum_v+=0x1<<24;
                last=l;
            }
            valuesx=new_valuesx;
  
        }

        private void process() {
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
            postprocess_values();  
              int v=0;
              long sum_v=0;
              
              x = new double[valuesx.size()*2];
  y = new double[valuesx.size()*2];
 
  int i=0;
            for (Long l:valuesx)
            {
                System.err.println(Long.toString((l+sum_v)/1)+" "+v);
           //     if (values_y_empty) valuesy.add(v);
                x[i]=l+sum_v;
                y[i]=v;
                i++;
                //v=(v+1)%4;
                v=(v+1)%2;
                System.err.println(Long.toString((l)/1)+" "+v);
             //   if (values_y_empty) valuesy.add(v);
                //System.err.println(Long.toString((l+sum_v)/1+1 )+" "+v);
                x[i]=l+1+sum_v;
                y[i]=v;
                i++;
            }
          
        }
protected void generate_x_and_y_doubles()
{
    x=new double[valuesx.size()];
    y=new double[valuesy.size()];
    for (int i=0;i<valuesx.size();i++)
        x[i]=valuesx.get(i);
    for (int i=0;i<valuesy.size();i++)
        y[i]=valuesy.get(i);
    
}
        private void plot() {
            if (x==null || y==null) generate_x_and_y_doubles();
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
  
        }
    }
    private static class NewData extends Data
    {
        private NewData(String string) {
            super(string);
        }
        public void process()
        {
            try {
                while (r.available()!=0)
                {
                    byte[] c=new byte[bytenum];
                    byte state=0;
             try {
                 //System.err.println( r.read(c));
                 state=r.readByte();//nem kell a 0f
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
             if ( (state &0xFF) == 0x0F)
                 valuesy.add((long)0);
             else valuesy.add((long)1);
             
             valuesx.add(n);
             
                }
            } catch (IOException ex) {
                Logger.getLogger(Plotter2.class.getName()).log(Level.SEVERE, null, ex);
            }
            postprocess_values();
        }
    }
}