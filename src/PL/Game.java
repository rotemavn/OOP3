package PL;

import javax.swing.*;


public class Game extends JFrame{

    public Game(){
        this.setName("Escape Grid");
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.setSize(1409,830);
        this.setResizable(false);
        JPanel mainMenu=new MainMenu();
        this.setContentPane(mainMenu);
        this.setVisible(true);

    }

    /**
     * The function adds panels to the JFrame
     * @param add JPanel we want to add to the frame
     */
    public void newAdd (JPanel add) {
        this.setContentPane(add);
        add.requestFocus();
    }


}
