package moe.reinwd.taxi;

import moe.reinwd.taxi.analysis.TaxiAnalysis;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaSparkContext;

import java.io.File;
import java.io.FileNotFoundException;

/**
 * @author ReinWD 张巍 2016214874
 * @e-mail ReinWDD@gmail.com
 */
public class Main {
    public static void main(String[] args) {
         if (args.length==2) {
             File texiGps = new File(args[0]);
             File regions = new File(args[1]);
             checkExistence(texiGps, regions);
             JavaSparkContext sc = initSpark();
             TaxiAnalysis analysis = new TaxiAnalysis(sc, texiGps, regions);
             analysis.start();
         }else {
             displayError(null);
         }
    }

    private static JavaSparkContext initSpark() {
        String appName = "texi_analysis";
        SparkConf sparkConf = new SparkConf().setAppName(appName)
                .setMaster("local");
        return new JavaSparkContext(sparkConf);
    }

    private static void checkExistence(File ... files) {
        if (files.length>0){
            for (File f :
                    files) {
                if (!f.exists()){
                    displayError( new FileNotFoundException("file "+f.getName() + " not found"));
                    System.exit(-1);
                }
            }
        }
    }

    private static void displayError(Exception e) {
        System.out.println("error occurred");
        if (e!= null){
            System.out.println(e.getMessage());
        }
        System.out.println("usage:");
        System.out.println("texiAnalysis <Taxi_GPS.txt> <district.txt>");
        System.out.println("please replace things in  \"<>\"s to it's actual path.");
    }
}
