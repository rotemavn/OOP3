package BL;

public class Move {
    private Piece _piece;
    private Position _start;
    private Position _end;

    //constructor
    public Move(Piece p,Position start, Position end)
    {
        _piece=p;
        _start=start;
        _end=end;
    }

    //constructor
    public Move(Move m)
    {
        this._piece=m._piece;
        this._start=m._start;
        this._end=m._end;
    }

    /**
     * @return The start position
     */
    public Position getStart()
    {
        return new Position(_start);
    }

    /**
     * @return The end position
     */
    public Position getEnd()
    {
        return new Position(_end);
    }

    /**
     * @return the piece
     */
    public Piece getPiece()
    {
        return _piece;
    }
}
