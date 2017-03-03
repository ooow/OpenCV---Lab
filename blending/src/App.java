import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.opencv.core.*;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.opencv.photo.Photo;

import java.io.File;
import java.util.Collections;
import java.util.List;

import static org.opencv.photo.Photo.NORMAL_CLONE;

public class App extends Application {
    private ImageView imageView1;
    private ImageView imageView2;
    private ImageView imageViewR;

    {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
    }

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        stage.setTitle("Blending");
        stage.setWidth(590);
        stage.setHeight(700);
        Pane root = new Pane();

        Button btn = new Button("Load Image 1");
        btn.setLayoutX(170);
        btn.setLayoutY(2);
        btn.setPrefSize(110, 20);

        Button btn2 = new Button("Load Image 2");
        btn2.setLayoutX(310);
        btn2.setLayoutY(2);
        btn2.setPrefSize(110, 20);
        Scene scene = new Scene(root);

        final File[] files = new File[2];
        btn.setOnAction(event -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Load image");
            FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("PNG or JPG files (*.png)", "*.png", "*.jpg");
            fileChooser.getExtensionFilters().add(extFilter);
            files[0] = fileChooser.showOpenDialog(scene.getWindow());
            if (files[0] != null) {
                root.getChildren().remove(imageView1);
                root.getChildren().remove(imageView2);
                root.getChildren().remove(imageViewR);
            }
        });

        btn2.setOnAction(event -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Load image");
            FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("PNG or JPG files (*.png)", "*.png", "*.jpg");
            fileChooser.getExtensionFilters().add(extFilter);
            files[1] = fileChooser.showOpenDialog(scene.getWindow());
            if (files[1] != null) {
                show(root, files);
            }
        });

        root.getChildren().add(btn);
        root.getChildren().add(btn2);
        stage.setScene(scene);
        stage.show();
    }

    public void show(Pane root, File[] files) {

        Mat in1 = Imgcodecs.imread(files[0].toString());
        Mat in2 = Imgcodecs.imread(files[1].toString());

        double sizeFactor = 1.15;
        Size sz = in2.size();
        sz.width = sz.width / sizeFactor;
        sz.height = sz.height / sizeFactor;
        Imgproc.resize(in2, in2, sz);

        Point poly[] = new Point[12];
        poly[0] = new Point(352 / sizeFactor, 33 / sizeFactor);
        poly[1] = new Point(243 / sizeFactor, 60 / sizeFactor);
        poly[2] = new Point(169 / sizeFactor, 126 / sizeFactor);
        poly[3] = new Point(134 / sizeFactor, 196 / sizeFactor);
        poly[4] = new Point(123 / sizeFactor, 272 / sizeFactor);
        poly[5] = new Point(128 / sizeFactor, 335 / sizeFactor);
        poly[6] = new Point(156 / sizeFactor, 419 / sizeFactor);
        poly[7] = new Point(235 / sizeFactor, 488 / sizeFactor);
        poly[8] = new Point(352 / sizeFactor, 522 / sizeFactor);
        poly[9] = new Point(352 / sizeFactor, 395 / sizeFactor);
        poly[10] = new Point(352 / sizeFactor, 266 / sizeFactor);
        poly[11] = new Point(352 / sizeFactor, 155 / sizeFactor);

        MatOfPoint point = new MatOfPoint(poly);
        List<MatOfPoint> list = Collections.singletonList(point);

        Mat mask = Mat.zeros(in2.rows(), in2.cols(), in2.depth());
        Imgproc.fillPoly(mask, list, new Scalar(255, 255, 255));

        Mat result = new Mat();
        Photo.seamlessClone(in2, in1, mask, new Point(233, 320), result, NORMAL_CLONE);
        Imgcodecs.imwrite("out.png", result);

        imageView1 = new ImageView(files[0].toURI().toString());
        imageView2 = new ImageView(files[1].toURI().toString());
        imageViewR = new ImageView(new File("out.png").toURI().toString());

        imageView1.setLayoutX(5);
        imageView1.setLayoutY(35);
        imageView1.setFitHeight(200);
        imageView1.setFitWidth(270);

        imageView2.setLayoutX(314);
        imageView2.setLayoutY(35);
        imageView2.setFitHeight(200);
        imageView2.setFitWidth(270);

        imageViewR.setLayoutX(5);
        imageViewR.setLayoutY(250);
        imageViewR.setFitHeight(420);
        imageViewR.setFitWidth(580);

        root.getChildren().add(imageView1);
        root.getChildren().add(imageView2);
        root.getChildren().add(imageViewR);
    }
}