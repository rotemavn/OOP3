package PL.PlayLevel;

import BL.Level;
import PL.Game;
import PL.MainMenu;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;

public class GameWindow extends JPanel implements ActionListener{
    private javax.swing.Timer _t;
    private JLabel _lblTime;
    private BoardGrid _bg;
    private JButton _undo;
    private JButton goToMenu;
    private Clock _c;
    private BufferedImage background;
    private Level level;

    public GameWindow(Level l) throws IOException {
        super();
        removeAll();
        this.setLayout(new BoxLayout(this,BoxLayout.LINE_AXIS));
        String currPath= Paths.get(".").toAbsolutePath().normalize().toString();

        background=ImageIO.read(new File(currPath+"/Images/background.png"));
        ImageIcon menu = new ImageIcon(ImageIO.read(new File(currPath+"/Images/back.png")));

        goToMenu = new JButton(menu);
        goToMenu.setBorder(null);
        goToMenu.addActionListener(this);
        add(Box.createHorizontalStrut(25));
        Box left=Box.createVerticalBox();
        left.add(goToMenu);
        left.add(Box.createVerticalGlue());
        add(left);

        Box center=Box.createVerticalBox();
        Box right=Box.createHorizontalBox();


        this.level=new Level(l);
        _bg=new BoardGrid(l,this);


        add(center);
        add(right);

        //start timer initialization
        _t=new Timer(1000,this); //move every second

        _c=new Clock();
        _lblTime=new JLabel();
        _lblTime.setText(_c.toString());
        _lblTime.setFont(new Font("Candara",Font.BOLD,30));
        center.add(_lblTime);
        center.add(_bg);
         _t.start();
        //end timer initialization
        //undo initialization
        _undo=new JButton();
        ImageIcon undo = new ImageIcon(ImageIO.read(new File(currPath+"/Images/undo2.png")));
        _undo.setIcon(undo);
        _undo.setBackground(null);
        _undo.setBorder(null);
        _undo.addActionListener(this);
        right.add(_undo);

        //undo initialization
        this.setVisible(true);
      //  this.revalidate();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource()==_t) {
            _c.add();
            _lblTime.setText(_c.toString());
            //this.repaint();

        }
        else if(e.getSource()==_undo)
        {
            _bg.undoMove();
            this.repaint();
        }

        else if(e.getSource()== goToMenu){
            this.setVisible(false);
            super.removeAll();
            ((Game)this.getRootPane().getParent()).newAdd(new MainMenu(this));
        }

    }

    /**
     * What to do if the player finished the game
     */
    public void finished()
    {
        _t.stop();
        _t.removeActionListener(this);
        JOptionPane.showMessageDialog(this,"You Have Won!");
        String bestTime=this.level.get_bestTime();
        String[] t= bestTime.split("-");
        int h=Integer.parseInt(t[0]);
        int m=Integer.parseInt(t[1]);
        int s=Integer.parseInt(t[2]);
        if(h<_c.get_hours())
            level.set_bestTime(_c.toString());
        else if(h==_c.get_mins() && m<_c.get_mins())
            level.set_bestTime(_c.toString());
        else if(h==_c.get_hours() && m== _c.get_mins() && s<_c.get_secs())
            level.set_bestTime(_c.toString());
        level.reSaveLevel();
        this.setVisible(false);
        _t.stop();
        removeAll();
        getParent().add(new MainMenu(null));
    }

    public void paintComponent(Graphics g){
        g.drawImage(background,0,0,null);

    }
}
