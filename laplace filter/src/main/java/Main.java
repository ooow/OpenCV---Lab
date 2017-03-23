import javafx.application.Application;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.bytedeco.javacpp.opencv_core;
import org.bytedeco.javacv.Frame;
import org.bytedeco.javacv.Java2DFrameConverter;
import org.bytedeco.javacv.OpenCVFrameConverter;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import static org.bytedeco.javacpp.opencv_core.IplImage;
import static org.bytedeco.javacpp.opencv_imgcodecs.cvLoadImage;
import static org.bytedeco.javacpp.opencv_imgproc.cvLaplace;

public class Main extends Application {
    private ImageView imageView;
    private ImageView imageView2;


    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        stage.setTitle("Laplace Filter");
        stage.setWidth(940);
        stage.setHeight(420);
        Pane root = new Pane();

        Button btn = new Button("Load Image");
        btn.setLayoutX(420);
        btn.setLayoutY(2);
        btn.setPrefSize(100, 20);
        Scene scene = new Scene(root);

        btn.setOnAction(event -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Load image");
            FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("PNG or JPG files (*.png)", "*.png", "*.jpg");
            fileChooser.getExtensionFilters().add(extFilter);
            File file = fileChooser.showOpenDialog(scene.getWindow());
            if (file != null) {
                root.getChildren().remove(imageView);
                root.getChildren().remove(imageView2);
                show(root, file);
            }
        });

        root.getChildren().add(btn);
        stage.setScene(scene);
        stage.show();
    }

    public void show(Pane root, File file) {
        try {
            imageView = new ImageView(file.toURL().toString());
            imageView.setLayoutX(30);
            imageView.setLayoutY(40);
            imageView.setFitWidth(400);
            imageView.setFitHeight(310);
            imageView.setPreserveRatio(true);
            imageView.setSmooth(true);
            imageView.setCache(true);
            root.getChildren().add(imageView);
        } catch (IOException e) {
            e.printStackTrace();
        }
        IplImage image1 = cvLoadImage(file.toString());
        cvLaplace(image1, image1);
        BufferedImage image = IplImageToBufferedImage(image1);
        Image image4 = SwingFXUtils.toFXImage(image, null);

        imageView2 = new ImageView();
        imageView2.setImage(image4);
        imageView2.setLayoutX(500);
        imageView2.setLayoutY(40);
        imageView2.setFitWidth(400);
        imageView2.setFitHeight(310);
        imageView2.setSmooth(true);
        imageView2.setCache(true);
        root.getChildren().add(imageView2);
    }

    private BufferedImage IplImageToBufferedImage(opencv_core.IplImage src) {
        OpenCVFrameConverter.ToIplImage iplImage = new OpenCVFrameConverter.ToIplImage();
        Java2DFrameConverter bimConverter = new Java2DFrameConverter();
        Frame frame = iplImage.convert(src);
        return bimConverter.convert(frame);
    }
}