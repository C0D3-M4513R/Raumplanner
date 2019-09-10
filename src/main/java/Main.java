import Reader.DataReader;
import javafx.application.Application;
import javafx.stage.Stage;

import java.util.ArrayList;

import static javafx.application.Application.launch;

public class Main extends Application {

    public static void main(String[] args)
    { launch(args); }

    @Override
    public void start(Stage primaryStage) {
        DataReader dr = new DataReader("test.txt");
        ArrayList<String> st = dr.getContent();
        st.forEach(System.out::println);
        System.out.println("-------------------------------------------------------");
        st.add(""+(Math.random()*1000));
        dr.setContent(st);
        dr.getContent().forEach(System.out::println);
        System.exit(0);
    }

}
