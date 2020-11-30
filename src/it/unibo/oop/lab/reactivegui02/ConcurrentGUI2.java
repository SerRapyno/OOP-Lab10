package it.unibo.oop.lab.reactivegui02;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.lang.reflect.InvocationTargetException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;





public final class ConcurrentGUI2 extends JFrame {

    private static final long serialVersionUID = 1L;
    private static final double WIDTH_PERC = 0.2;
    private static final double HEIGHT_PERC = 0.1;
    private final JLabel display = new JLabel();
    private final JButton stop = new JButton("stop");
    private final JButton up = new JButton("up");
    private final JButton down = new JButton("down");

    public ConcurrentGUI2() {
        super();
        final Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        this.setSize((int) (screenSize.getWidth() * WIDTH_PERC), (int) (screenSize.getHeight() * HEIGHT_PERC));
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        final JPanel panel = new JPanel();
        panel.add(display);
        panel.add(up);
        panel.add(down);
        panel.add(stop);
        this.getContentPane().add(panel);
        this.setVisible(true);

        final Agent agent = new Agent();
        new Thread(agent).start();

        stop.addActionListener(t -> {
            agent.stopCounting();
            up.setEnabled(false);
            down.setEnabled(false);
        });
        up.addActionListener(t -> agent.direction(1));
        down.addActionListener(t -> agent.direction(-1));
    }

    private class Agent implements Runnable {

        private volatile boolean stop;
        private volatile int counter;
        private volatile int increase = 1;

        @Override
        public void run() {
            while (!this.stop) {
                try {
                    SwingUtilities.invokeAndWait(() -> ConcurrentGUI2.this.display.setText(Integer.toString(Agent.this.counter)));
                    this.counter += increase;
                    Thread.sleep(1000);
                } catch (InvocationTargetException | InterruptedException ex) {
                    ex.printStackTrace();
                }
            }
        } 

        public void stopCounting() {
            this.stop = true;
        }

        public void  direction(final int d) {
            this.increase = d;
        }

    }
}
