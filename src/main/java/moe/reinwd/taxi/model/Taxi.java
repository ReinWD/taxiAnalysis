package moe.reinwd.taxi.model;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author ReinWD 张巍 2016214874
 * @e-mail ReinWDD@gmail.com
 */
public class Taxi implements Serializable {
    private static SimpleDateFormat dateFormat;

    private int index;
    private TaxiEvent event;
    private TaxiWorkingState workingState;
    private Date time;
    private LatLng latLng;
    private int speed, towards;
    private byte gpsState;

    static {
        dateFormat = (SimpleDateFormat) SimpleDateFormat.getInstance();
        dateFormat.applyPattern("yyyyMMddhhmmss");
    }

    public static Taxi createFromStr(String str) throws ParseException {
        return new Taxi(str);
    }

    private Taxi(String str) throws ParseException {
        String[] split = str.split(",");
        int i = 0;
        index = Integer.parseInt(split[i++]);
        event = TaxiEvent.get(split[i++]);
        workingState = TaxiWorkingState.get(split[i++]);
        time = dateFormat.parse(split[i++]);
        double lng = Double.parseDouble(split[i++]);
        double lat = Double.parseDouble(split[i++]);
        latLng = new LatLng(lat, lng);
        speed = Integer.parseInt(split[i++]);
        towards = Integer.parseInt(split[i++]);
        gpsState = Byte.parseByte(split[i]);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null)return false;
        if (obj instanceof Taxi){
            return this.index == ((Taxi) obj).index;
        }return false;
    }

    public int getIndex() {
        return index;
    }

    @Override
    public int hashCode() {
        return index*13;
    }

    public LatLng getPositon() {
        return this.latLng;
    }

    enum TaxiEvent{
        TO_EMPTY(0), TO_WORKING(1), TO_PROTECTED(2),
        OUT_PROTECTED(3), OTHERS(4);

        private int event;

        TaxiEvent(int i){
            this.event = i;
        }

        public static TaxiEvent get(String s) {
            int i = Integer.parseInt(s);
            for (TaxiEvent e :
                    values()) {
                if (e.event == i)return e;
            }
            throw new RuntimeException("event not exists.");
        }
    }

    enum TaxiWorkingState{
        EMPTY(0), WORKING(1), HALTING(2), STOPPED(3), OTHERS(4);
        private int state;
        TaxiWorkingState(int state){
            this.state = state;
        }

        public static TaxiWorkingState get(String s) {
            int i = Integer.parseInt(s);
            for (TaxiWorkingState e :
                    values()) {
                if (e.state == i)return e;
            }
            throw new RuntimeException("event not exists.");
        }
    }
}
