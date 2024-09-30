import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

class Point {
    double x1,y1;
    String cl;

    public Point(double x, double y, String cl) {
        this.x1 = x1;
        this.y1 = y1;
        this.cl = cl;
    }

    @Override
    public String toString() {
        return "Point{x1=" + x1 + ", y1=" + y1 + ", class='" + cl + "'}";
    }
}

public class ConfusionMatrix {
    public static double Euclidean(double x, double y, double x1, double y1){
        return Math.sqrt((Math.pow((x - x1), 2)) + (Math.pow((y - y1), 2)));
    }

    public static String KNN (List<Point> points, double x, double y, int k){
        List<Map.Entry<Double, Point>> distanceToPoint = new ArrayList<>();

        for (Point point : points){
            double distance = Euclidean(x,y,point.x1, point.y1);
            distanceToPoint.add(new AbstractMap.SimpleEntry<>(distance, point));
        }

        distanceToPoint.sort(Map.Entry.comparingByKey());

        Map<String, Integer> classCount = new HashMap<>();
        for (int i = 0; i < k; i++){
            Point nearestPoint = distanceToPoint.get(i).getValue();
            classCount.put(nearestPoint.cl, classCount.getOrDefault(nearestPoint.cl, 0) + 1);
        }

        return Collections.max(classCount.entrySet(), Map.Entry.comparingByValue()).getKey();
    }

    public static void evaluateModel(List<Point> Training, List<Point> Testing, int k){
        int TP = 0, FP = 0, TN = 0, FN = 0;

        for (Point testPoint : Testing){
            String actuallyClass = testPoint.cl;
            String predictedClass = KNN(Training, testPoint.x1, testPoint.y1, k);

            if (actuallyClass.equals("+")){
                if (predictedClass.equals("+")){
                    TP++;
                }else {
                    FN++;
                }
            }else {
                if (predictedClass.equals("-")){
                    TN++;
                }else {
                    FP++;
                }
            }
        }

        System.out.println("Confusion Matrix:");
        System.out.println("TP: " + TP + ", FP: " + FP);
        System.out.println("TN: " + TN + ", FN: " + FN);

        double accuracy = (TP + TN) * 1.0 / (TP + FP + TN + FN);
        System.out.println("Accuracy: " + accuracy);
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        String path = "D:/Data.txt";

        List<Point> allPoints = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(path))){
            String line;
            while ((line = br.readLine()) != null){
                String[] numbers = line.split(" ");
                double x1 = Double.parseDouble(numbers[0]);
                double y1 = Double.parseDouble(numbers[1]);
                String cl = numbers[2];
                allPoints.add(new Point(x1,y1,cl));
            }
        } catch (IOException e){
            e.printStackTrace();
        }

        Collections.shuffle(allPoints);

        int TrainingSize = (int) (0.8 * allPoints.size());

        List<Point> TrainingDataSet = allPoints.subList(0, TrainingSize);
        List<Point> TestingDataSet = allPoints.subList(TrainingSize, allPoints.size());

        System.out.println("Nhap x: ");
        double x = sc.nextDouble();
        System.out.println("Nhap y: ");
        double y = sc.nextDouble();

        System.out.println("Nhap k: ");
        int k1 = sc.nextInt();



        String predictedClass = KNN(allPoints, x, y, k1);

        System.out.println("Diem (" + x + ", " + y + ") thuoc lop " + predictedClass);

        System.out.println("Nhap k de danh gia mo hinh: ");
        int k2 = sc.nextInt();
        evaluateModel(TrainingDataSet, TestingDataSet, k2);

        sc.close();
    }
}