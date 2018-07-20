import javafx.application.Application;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.*;
import javafx.scene.paint.*;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.scene.canvas.*;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.MenuBar;
import javafx.scene.control.TextField;

/* Classe de entrada do programa */
public class Main extends Application {

    private static double screenWidth = Screen.getPrimary().getVisualBounds().getWidth();
    private static double screenHeight = Screen.getPrimary().getVisualBounds().getHeight();

    @Override
    public void start(Stage primaryStage) {

        try {

            Parent root = FXMLLoader.load(getClass().getResource("Main.fxml"));
            Scene scene = new Scene(root);
            definePrimaryStage(scene, primaryStage);
            
            final Canvas canvas = (Canvas) root.getChildrenUnmodifiable().get(0);
            canvas.setWidth(screenWidth);
            canvas.setHeight(screenHeight);
            GraphicsContext context = canvas.getGraphicsContext2D();
            Button buttonClear = (Button) root.getChildrenUnmodifiable().get(1);
            TextField factorField = (TextField) root.getChildrenUnmodifiable().get(2);
            CheckBox R = (CheckBox) root.getChildrenUnmodifiable().get(3); 
            CheckBox G = (CheckBox) root.getChildrenUnmodifiable().get(4);    
            CheckBox B = (CheckBox) root.getChildrenUnmodifiable().get(5);        
            
            double randomnessFactor = 0;
            boolean RChannel = false;
            boolean GChannel = false;
            boolean BChannel = false;
            execute(context, randomnessFactor, RChannel, GChannel, BChannel);
            buttonClear.setOnAction(new EventHandler<ActionEvent>() {
                @Override public void handle(ActionEvent e) {
                    System.out.println("Clear invocado!");
                    context.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
                    double randomnessFactor = Double.parseDouble(factorField.getText());
                    boolean RChannel = R.isSelected();
                    boolean GChannel = G.isSelected();
                    boolean BChannel = B.isSelected();
                    execute(context, randomnessFactor, RChannel, GChannel, BChannel);
                }
            });
            
            primaryStage.show();


        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static void execute(GraphicsContext context, double f, boolean R, boolean G, boolean B) {
        /* Carregamento dos arquivos e projeção para coordenadas de vista. */
        Loader loader = new Loader("../iluminacao.txt", "../camera.cfg", "../objeto.byu");
        Camera camera = loader.loadCamera();
        Illumination illumination = loader.loadScene(camera.worldToView(), camera.getCoordinates(), f, R, G, B);
        SceneObject object = loader.loadObject(camera.worldToView(), camera.getCoordinates());
        
        /* Cálculo das normais e normalização. */
        object.normalize();

        /* Inicialização do Z-Buffer. */
        double[][] zBuffer = initializeZBuffer();

        /* Projeção para a tela, rasterização e iluminação. */
        object.finalSteps(camera, illumination, screenWidth, screenHeight, context, zBuffer);
    }

    public static void main(String[] args) {
        launch(args);
    }

    public void definePrimaryStage(Scene scene, Stage primaryStage) {
        primaryStage.setScene(scene);
        primaryStage.setWidth(screenWidth);
        primaryStage.setHeight(screenHeight);
        primaryStage.addEventHandler(KeyEvent.KEY_RELEASED, (KeyEvent event) -> {
            if(KeyCode.ESCAPE == event.getCode())
                primaryStage.close();
        });
    }

    public static double[][] initializeZBuffer() {
        double[][] buffer = new double[(int) Math.round(screenWidth)][(int) Math.round(screenHeight)];
        for (int i = 0; i < buffer.length; i++)
            for (int j = 0; j < buffer[i].length; j++) 
                buffer[i][j] = Double.MAX_VALUE;

        return buffer;
    }
}