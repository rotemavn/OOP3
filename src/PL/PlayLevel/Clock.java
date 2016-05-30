package PL.PlayLevel;

/**
 * Created by liorbass on 30/05/2016.
 */
public class Clock {
    private int _secs;
    private int _mins;
    private int _hours;
    public Clock(){
        _secs=0;
        _mins=0;
        _hours=0;
    }

    /**
     * adds one second to the clock
     */
    public void add(){
        _secs++;
        if (_secs == 60) {
            _secs = 0;
            _mins++;
            if (_mins == 60) {
                _mins = 0;
                _hours++;
            }
        }
    }

    /**
     * returns string representing the time
     * @return string of the time
     */
    public String toString() {
        String ans="";
        char sepatator='-';
        if(_hours<10)
            ans+="0"+_hours;
        else
            ans+=_hours;
        ans+=sepatator;
        if(_mins<10)
            ans+="0"+_mins;
        else
            ans+=_mins;
        ans+=sepatator;
        if(_secs<10)
            ans+="0"+_secs;
        else
            ans+=_secs;
        return ans;
    }
    public int get_hours(){
        return  _hours;
    }
    public int get_secs(){
        return _secs;
    }
    public int get_mins(){
        return _mins;
    }

}
