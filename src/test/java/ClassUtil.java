import java.io.File;
public class ClassUtil {
    static File f = new File("./out/artifacts/unnamed/lib");

    public static void main(String[] args) {
        File[] files = f.listFiles();
        for (File ff :
                files) {
            System.out.println("./lib/"+ff.getName());
        }
    }
}
