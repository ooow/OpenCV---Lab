import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import java.io.File;

public class App extends Application {

    {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
    }

    public static void main(String[] args) {
        launch(args);
    }

    public void canny() {
        Mat image = Imgcodecs.imread("phone.jpg");
        Mat cannyImg = new Mat();
        Imgproc.Canny(image, cannyImg, 10, 100);
        Imgcodecs.imwrite("phone2.jpg", cannyImg);
    }

    @Override
    public void start(Stage stage) throws Exception {
        stage.setTitle("Canny Filter");
        stage.setWidth(1001);
        stage.setHeight(400);
        Pane root = new Pane();
        Scene scene = new Scene(root);
        canny();

        File file = new File("phone.jpg");
        ImageView imageView1 = new ImageView(new Image(file.toURI().toString()));

        File file2 = new File("phone2.jpg");
        ImageView imageView2 = new ImageView(new Image(file2.toURI().toString()));

        imageView1.setLayoutX(0);
        imageView1.setLayoutY(0);
        imageView1.setFitHeight(400);
        imageView1.setFitWidth(500);

        imageView2.setLayoutX(501);
        imageView2.setLayoutY(0);
        imageView2.setFitHeight(400);
        imageView2.setFitWidth(500);


        root.getChildren().add(imageView1);
        root.getChildren().add(imageView2);
        stage.setScene(scene);
        stage.show();
    }
}
