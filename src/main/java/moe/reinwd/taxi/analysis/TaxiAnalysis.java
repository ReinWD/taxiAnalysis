package moe.reinwd.taxi.analysis;

import moe.reinwd.taxi.log.Log;
import moe.reinwd.taxi.model.Region;
import moe.reinwd.taxi.model.Taxi;
import moe.reinwd.taxi.util.ConcurrentUtil;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.util.LongAccumulator;

import java.io.File;
import java.io.Serializable;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author ReinWD 张巍 2016214874
 * @e-mail ReinWDD@gmail.com
 */
public class TaxiAnalysis implements Serializable {
    private transient JavaSparkContext sc;
    private JavaRDD<Taxi> taxiDatas;
    private JavaRDD<Region> regionList;


    public TaxiAnalysis(JavaSparkContext sc, File texiGps, File regions) {
        this.sc = sc;
        JavaRDD<String> texiGpsLines = sc.textFile(texiGps.getPath());
        taxiDatas = texiGpsLines.map((Function<String, Taxi>) str -> {
                    int firstComma = str.indexOf(",");
                    int index = Integer.parseInt(str.substring(0, firstComma));
                    Taxi taxi = Taxi.createFromStr(str);
                    return taxi;
                }
        );
        JavaRDD<String> regionStrs = sc.textFile(regions.getPath());
        this.regionList = regionStrs.map((Function<String, Region>) Region::createFromStr);
    }

    public void start() {
        //count taxis
        Set<Integer> indexSet = new HashSet<>();
        long count = taxiDatas.count();
        LongAccumulator accumulator = sc.sc().longAccumulator();
        taxiDatas.foreach(taxiData-> {
            if (!indexSet.contains(taxiData.getIndex())){
                accumulator.add(1);
            }
            indexSet.add(taxiData.getIndex());
        });
        Log.i("A: there're "+accumulator.value()+ " cars.");
        final LongAccumulator longAccumulator = sc.sc().longAccumulator();
//        regionList.foreach(new VoidFunction<Region>() {
//            @Override
//            public void call(Region region) throws Exception {
//                longAccumulator.setValue(0);
//                taxiDatas.foreach(taxi -> {
//                    if (region.isInRegion(taxi.getPositon())){
//                        longAccumulator.add(1);
//                    }
//                });
//                System.out.println("Region \""+region.getName()+"\" has "+ longAccumulator.value()+ " cars.");
//            }
//        });
        long nums= regionList.count();
        int step = 100;
        List<Region> regions;
        Set<Integer> quchong;
        for (int i = 0; i < nums; i+=step) {
            long remains = nums - i;
            if (remains < step){
                regions = regionList.take((int) remains);
            }else {
                regions = regionList.take(step);
            }
//            Set<Integer> finalQuchong = quchong;
            regions.forEach(region -> {
                longAccumulator.setValue(0);
                taxiDatas.foreach(taxi -> {
//                    if (!finalQuchong.contains(taxi.getIndex())){
                        if (region.isInRegion(taxi.getPositon())){
                            longAccumulator.add(1);
                        }
//                    }
//                    finalQuchong.add(taxi.getIndex());
                });
                Log.i("Region \"" + region.getName() + "\" has " + longAccumulator.value() + " cars.");
            });
        }
        ConcurrentUtil.stop();
    }
}
