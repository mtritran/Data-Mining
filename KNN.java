import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

class Point {
    double x1, y1;
    String cl;

    public Point(double x1, double y1, String cl) {
        this.x1 = x1;
        this.y1 = y1;
        this.cl = cl;
    }

    @Override
    public String toString() {
        return "Point{x1=" + x1 + ", y1=" + y1 + ", class='" + cl + "'}";
    }
}

public class KNN {
    public static double Euclidean(double x, double y, double x1, double y1) {
        return Math.sqrt(Math.pow((x - x1), 2) + Math.pow((y - y1), 2));
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        String filePath = "D:/DataMining.txt";

        System.out.println("Nhap x: ");
        double x = sc.nextDouble();
        System.out.println("Nhap y: ");
        double y = sc.nextDouble();

        System.out.println("Nhap k: ");
        int k = sc.nextInt();
        List<Map.Entry<Double, Point>> distanceToPoint = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] numbers = line.split(" ");
                double x1 = Double.parseDouble(numbers[0]);
                double y1 = Double.parseDouble(numbers[1]);
                String cl = numbers[2];
                Point p = new Point(x1, y1, cl);

                double distance = Euclidean(x, y, x1, y1);
                distanceToPoint.add(new AbstractMap.SimpleEntry<>(distance, p));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        distanceToPoint.sort(Map.Entry.comparingByKey());

        Map<String, Integer> classCount = new HashMap<>();
        for (int i = 0; i < k; i++) {
            Point nearestPoint = distanceToPoint.get(i).getValue();
            classCount.put(nearestPoint.cl, classCount.getOrDefault(nearestPoint.cl, 0) + 1);
        }

        String predictedClass = Collections.max(classCount.entrySet(), Map.Entry.comparingByValue()).getKey();

        System.out.println("Cac khoang cach va diem:");
        for (Map.Entry<Double, Point> e : distanceToPoint){
            double d = e.getKey();
            Point p = e.getValue();
            System.out.println("Khoang cach: " + d + " " + p);
        }

        System.out.println("Diem (" + x + ", " + y + ") thuoc lop " + predictedClass);

        sc.close();
    }
}
