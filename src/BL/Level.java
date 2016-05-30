package BL;

import java.io.*;
import java.nio.file.Paths;
import java.util.Stack;
import java.util.Vector;

/**
 * Created by liorbass on 17/05/2016.
 */
public class Level {

    protected final int SIZE = 6;
    private Vector<Piece> _vp;
    private String _bestTime;
    private Stack<Move> _sm;
    private final Position _finish=new Position(2,3);
    private File levelFile;

    //region constractors
    public Level() {
        _vp = new Vector<Piece>();
        _bestTime = "00-00-00";
        _sm = new Stack<Move>();
        _vp = new Vector<Piece>();
    }

    public Level(Vector<Piece> vp) {
        _vp = new Vector<Piece>(vp.size());
        _bestTime = "00-00-00";
        _sm = new Stack<Move>();
        for (Piece p : vp) {
            this.addPiece(new Piece(p));
        }
    }

    public Level(Level l) {
        _vp = new Vector<Piece>();
        this._bestTime = l._bestTime;
        this._sm = l._sm;
        for (Piece p : l._vp) {
            this.addPiece(new Piece(p));
        }
        this.levelFile=l.get_levelFile();
    }


    /**
     *
     * @param levelFile - a file which contains all the details of the level.
     */
    public Level(File levelFile){

        try {
            this.levelFile=levelFile;
            BufferedReader bf = new BufferedReader(new FileReader(levelFile));
            String s = bf.readLine();

            int count=0;
            while(!s.contains("bt_")){
                count++;
                s=bf.readLine();
            }
            bf = new BufferedReader(new FileReader(levelFile));
            String s2 = bf.readLine();
            _vp=new Vector<Piece>(count);
            while(!s2.contains("bt_")){
                _vp.add(new Piece(s2));
                s2=bf.readLine();
            }
            if(s2.contains("bt_")){
                _bestTime=s2.substring(3);
            }
            _sm = new Stack<Move>();

        } catch (Exception e) {

            System.out.println(e.getMessage());
            e.printStackTrace();
        }


    }
    //endregion
    //region moves

    /**
     * Moves a Piece in the board
     *
     * @param pic piece to move
     * @param pos the new position of the piece
     * @return true if succeed, false if impossible move
     */
    public boolean move(Piece pic, Position pos) {
        Move m = new Move(pic, pic.get_start(), pos);
        if (isValidMove(m)) {
            pic.move(pos);
            _sm.push(m);
            return true;
        }
        return false;
    }

    /**
     * Revert the level to the state befor the move
     *
     * @return true if succeed
     */
    public Move undoMove() {
        boolean ans = true;
        if(!_sm.empty()) {
            Move m = _sm.pop();
            m.getPiece().move(m.GetStart());
            return m;
        }
        return null;
    }
    //endregion

    /**
     * Checks if given move is valid
     *
     * @param m move to check
     * @return true if move is valid, false otherwise
     */
    private boolean isValidMove(Move m) {
        boolean ans = false;
        if (m.GetEnd().isValid()) {
            if (m.getPiece().get_orientation() == Orientation.HORIZONTAL) {
                if (m.GetEnd().getX() + m.getPiece().get_size() <= SIZE) {
                    if (m.GetStart().getY() == m.GetEnd().getY())
                        ans = true;
                }


            } else {
                if (m.GetEnd().getY() + m.getPiece().get_size() <= SIZE) {
                    if (m.GetStart().getX() == m.GetEnd().getX())
                        ans = true;
                }
            }
            //start setup for conflicts check
            //Vector<Piece> tvp=new Vector<Piece>(_vp);
            //tvp.remove(m.getPiece());
            //Level tlvl= new Level(tvp);
            //Piece tp=new Piece(m.getPiece());
            //tp.move(m.GetEnd());
            //end setup for conflict check
            //ans=ans&tlvl.AddPiece(tp); // check for pieces conflicts
            ans = ans & canPlace(m.getPiece(), m.GetEnd());
        }
        return ans;
    }

    /**
     * Adds piece to current Level
     *
     * @param p the piece to add
     * @return true if piece was added successfully, false otherwise
     */
    public boolean addPiece(Piece p) {
        if (canPlace(p, p.get_start())) {
            _vp.add(p);
            return true;
        }
        return false;
    }

    /**
     * Checks if it's possible to add or move a piece in the current level
     *
     * @param p the piece to check
     * @return true if will can place the pos, false otherwise
     */
    private boolean canPlace(Piece p, Position pos) {
        //create temp piece with the new position,

        Piece tempPiece = new Piece(pos, p.get_size(), p.get_role(), p.get_orientation());
            //check if it's colliding with any existing piece other than then original
        boolean ans = pos.isValid() &p.get_end().isValid();
        if(ans) {
            for (Piece o : _vp) {
                if (o != p) {
                    if (p.get_start().isValid() && p.get_end().isValid()) {
                        if (tempPiece.areCollaiding(o))
                            ans = false;
                    }
                }
            }
        }
        return ans;
    }

    /**
     * Converts current Level to string
     *
     * @return
     */
    public String toString() {
        String ans = "";
        for (Piece p : _vp) {
            ans = ans + p.toString() + "%";
        }
        ans+="bt_"+_bestTime+"%";

        return ans;
    }


    /**
     * Geter of Pieces
     *
     * @return vector of peices
     */
    public Vector<Piece> get_Pieces() {
        return _vp;
    }

    /**
     * Searches for a piece in a position
     *
     * @param p position to look for
     * @return the piece which start equals to p, null if there is no such piece
     */
    public Piece getPieceByPosition(Position p) {
        for (Piece lp : _vp) {
            if (lp.get_start().equals(p)) {
                return lp;
            }
        }
        return null;
    }
    public Position get_finish()
    {
        return new Position(_finish);
    }

    /**
     * Removes a piece from current level
     * @param p piece to remove
     * @return true if succeded, false otherwise
     */
    public boolean removePiece(Piece p)
    {
        return _vp.removeElement(p);
    }

    public Boolean saveLevel()
    {
        String lev=this.toString();
        String [] levParts=lev.split("%");
        //get level name
        String levName;
        File[] levels;
        File levelsFolder;
        String currPath = Paths.get(".").toAbsolutePath().normalize().toString();
        levelsFolder = new File(currPath + "/Levels");
        levels = levelsFolder.listFiles();
        levName="l"+(levels.length+1)+".txt";
        //end get level name
        try {
            File file = new File(levelsFolder+"\\"+levName);
            FileWriter fileWriter = new FileWriter(file);
            for (String levPart : levParts) {
                fileWriter.write(levPart);
                fileWriter.write((System.getProperty("line.separator")));
            }

            fileWriter.flush();
            fileWriter.close();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public void reSaveLevel(){
        String lev=this.toString();
        String [] levParts=lev.split("%");
        //get level name
        String levName=levelFile.getName();
        levelFile.delete();
        File levelsFolder;
        String currPath = Paths.get(".").toAbsolutePath().normalize().toString();
        levelsFolder = new File(currPath + "/Levels");
        //end get level name
        try {
            File file = new File(levelsFolder+"\\"+levName);
            FileWriter fileWriter = new FileWriter(file);
            for (String levPart : levParts) {
                fileWriter.write(levPart);
                fileWriter.write((System.getProperty("line.separator")));
            }

            fileWriter.flush();
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    public void set_bestTime(String _bestTime) {
        this._bestTime = _bestTime;
    }

    public String get_bestTime() {
        return _bestTime;
    }

    private File get_levelFile(){
        return this.levelFile;
    }
}
