// $Id: StatusBar.java 14225 2008-03-24 16:21:20Z tfmorris $
// Copyright (c) 1996-2006 The Regents of the University of California. All
// Rights Reserved. Permission to use, copy, modify, and distribute this
// software and its documentation without fee, and without a written
// agreement is hereby granted, provided that the above copyright notice
// and this paragraph appear in all copies.  This software program and
// documentation are copyrighted by The Regents of the University of
// California. The software program and documentation are supplied "AS
// IS", without any accompanying services from The Regents. The Regents
// does not warrant that the operation of the program will be
// uninterrupted or error-free. The end-user understands that the program
// was developed for research purposes and is advised not to rely
// exclusively on the program for any reason.  IN NO EVENT SHALL THE
// UNIVERSITY OF CALIFORNIA BE LIABLE TO ANY PARTY FOR DIRECT, INDIRECT,
// SPECIAL, INCIDENTAL, OR CONSEQUENTIAL DAMAGES, INCLUDING LOST PROFITS,
// ARISING OUT OF THE USE OF THIS SOFTWARE AND ITS DOCUMENTATION, EVEN IF
// THE UNIVERSITY OF CALIFORNIA HAS BEEN ADVISED OF THE POSSIBILITY OF
// SUCH DAMAGE. THE UNIVERSITY OF CALIFORNIA SPECIFICALLY DISCLAIMS ANY
// WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF
// MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE. THE SOFTWARE
// PROVIDED HEREUNDER IS ON AN "AS IS" BASIS, AND THE UNIVERSITY OF
// CALIFORNIA HAS NO OBLIGATIONS TO PROVIDE MAINTENANCE, SUPPORT,
// UPDATES, ENHANCEMENTS, OR MODIFICATIONS.

package org.argouml.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.border.EtchedBorder;
import javax.swing.Timer;

import org.tigris.gef.ui.IStatusBar;

/**
 * The status bar.
 *
 */
public class StatusBar extends JPanel implements Runnable, IStatusBar {

    private JLabel msg = new JLabel();
    private JProgressBar progress = new JProgressBar();
    private String statusText;

    /**
     * The constructor.
     *
     */
    public StatusBar() {
	progress.setMinimum(0);
	progress.setMaximum(100);
	progress.setMinimumSize(new Dimension(100, 20));
	progress.setSize(new Dimension(100, 20));

	msg.setMinimumSize(new Dimension(300, 20));
	msg.setSize(new Dimension(300, 20));
	msg.setFont(new Font("Dialog", Font.PLAIN, 10));
	msg.setForeground(Color.black);

	setLayout(new BorderLayout());
	setBorder(new EtchedBorder(EtchedBorder.LOWERED));
	add(msg, BorderLayout.CENTER);
	add(progress, BorderLayout.EAST);
    }

    /**
     * @param s the status string to show
     */
    public void showStatus(String s) {
	msg.setText(s);
	paintImmediately(getBounds());
    }

    
    public void showStatusBlink(String s) 
    {
    	this.showStatusBlink(s,Color.red);
    }
    
    public void showStatusBlink(String s,Color cor) 
    {
    	// this.showStatus(s);
    	msg.setText(s);
    	msg.setOpaque(true);

    	paintImmediately(getBounds());
    	
    	Blinker blinker = new Blinker( msg,cor );
        blinker.start();
      
        
    }

    /**
     * @param percent the percentage of the progress bar to be shown
     */
    public void showProgress(int percent) {
	progress.setValue(percent);
    }

    /**
     * @param delataPercent an increment for the progrss bar
     */
    public void incProgress(int delataPercent) {
	progress.setValue(progress.getValue() + delataPercent);
    }

    /**
     * @param s the status bar text
     * @param work the work that has to be done,
     *        i.e. the maximum value for the progress
     */
    public synchronized void doFakeProgress(String s, int work) {
	statusText = s;
	showStatus(statusText + "... not implemented yet ...");
	progress.setMaximum(work);
	progress.setValue(0);
	Thread t = new Thread(this);
	t.start();
    }

    /*
     * @see java.lang.Runnable#run()
     */
    public synchronized void run() {
	int work = progress.getMaximum();
	for (int i = 0; i < work; i++) {
	    progress.setValue(i);
	    repaint();
	    try { wait(10); }
	    catch (Exception ex) { }
	}
	showStatus(statusText + "... done.");
	repaint();
	try { wait(1000); }
	catch (Exception ex) { }
	progress.setValue(0);
	showStatus("");
	repaint();
    }

}

//Nesta classe foi fazer a mensagem da status bar piscar
class Blinker extends Thread 
{
    public Blinker(JLabel label,Color c) 
    {
        this.label = label;
        color1 = label.getBackground();
        // color2 = Color.red;
        
        color2 = c;
    }
    
    public Blinker(JLabel label) 
    {
        this.label = label;
        color1 = label.getBackground();
        color2 = Color.red;
    }

  public void run ()  
  {
	   try 
	   {
		    int i = 1;
		    
		   	while(i<=4)
		   	{
			    highlight = !highlight;
			    
		        if(highlight){
		            label.setBackground(color2);
		        }else{
		            label.setBackground(color1);
		        }
		        
		        this.sleep(500);
		        
		        i++;
		   	}
	        
	        label.setBackground(color1);
	        
	   }
	   catch (Exception e) 
	   {
		   e.printStackTrace();
	   }    	
  }
    
  private JLabel label;
  private Color color1, color2;
  private boolean highlight;
}
