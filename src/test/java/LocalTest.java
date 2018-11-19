
import moe.reinwd.taxi.model.Region;
import moe.reinwd.taxi.model.Taxi;

import java.io.*;
import java.text.ParseException;
import java.util.*;

public class LocalTest {
    public static void main(String[] args) throws IOException, ParseException {
        File file = new File("./data/taxi_gps.txt");
        BufferedReader reader = new BufferedReader(new FileReader(file));
        String s ;
        List<Taxi> list = new ArrayList<>();
        Set<Taxi> set = new HashSet<>();
        while ((s = reader.readLine())!=null) {
            Taxi fromStr = Taxi.createFromStr(s);
            list.add(fromStr);
            set.add(fromStr);
        }
        HashMap<Integer, Integer> countMap = new HashMap<>();
        list.stream().forEach(taxi -> {
            Integer integer = countMap.get(taxi.getIndex());
            if (integer !=null){
                countMap.put(taxi.getIndex(),integer+1);
            }else countMap.put(taxi.getIndex(),1);
        });
        System.out.println();
    }

    static class RegionTest{
        public static void main(String[] args) throws IOException, ParseException {
            File file = new File("./data/taxi_gps.txt");
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String s ;
            List<Taxi> list = new ArrayList<>();
            Set<Taxi> set = new LinkedHashSet<>();
            while ((s = reader.readLine())!=null) {
                Taxi fromStr = Taxi.createFromStr(s);
                list.add(fromStr);
                set.add(fromStr);
            }
            File regionFile = new File("./data/district.txt");
            reader = new BufferedReader(new FileReader(regionFile));
            List<Region> regions = new ArrayList<>();
            while ((s = reader.readLine())!=null) {
                regions.add(Region.createFromStr(s));
            }
            regions.forEach(region -> {
                int i = 0;
                Set<Integer> quchong = new HashSet<>();
                for (Taxi t :
                        list) {
                    if (!quchong.contains(t.getIndex())){
                    if (region.isInRegion(t.getPositon())){
                        i++;
                    }}
                    quchong.add(t.getIndex());
                }
                System.out.println(region.getName()+" "+i);
           });
            System.out.println();
            set.forEach(taxi -> {
//                System.out.println(taxi.getTime());
            });
        }
    }
}
