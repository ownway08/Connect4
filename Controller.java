package com.internshala.connect4;

import javafx.animation.TranslateTransition;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Point2D;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.util.Duration;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Controller  implements Initializable {

    private static final int columns=7;
    private static final int rows=6;
    private static final int circledi=80;
    private static final String disccolor1="red";
    private static final String disccolor2="green";

    public static   String player1 ;
    public static  String player2;
    private boolean isplayeroneturn=true;
    private Disc[][] inserteddiscsArray=new Disc[rows][columns];

    @FXML
    public GridPane rootgridpane;
    @FXML
    public Pane inserteddiscspane;
    @FXML
    public Label playernamelabel;
    @FXML
    public  TextField nametextfield;
    @FXML
    public TextField name2textfield;
    @FXML
    public Button setbutton;



    private boolean isallowedtoinsert=true;

    public void createplayground(){
        Shape rectanglewithholes=creategamestructuralgrid();

        rootgridpane.add(rectanglewithholes,0,1);
        List<Rectangle> rectangleList=createclickablecolumns();
        for (Rectangle rectangle:rectangleList ){
            rootgridpane.add(rectangle,0,1);
            setbutton.setOnAction(event -> {
                player1=nametextfield.getText();
                player2=name2textfield.getText();
            });

        }

    }
    private Shape creategamestructuralgrid(){
        Shape rectanglewithholes= new Rectangle((columns+1)*circledi,(rows+1)*circledi);
        for (int row=0;row<rows;row++){
            for (int col=0;col<columns;col++){
                Circle circle=new Circle();
                circle.setRadius(circledi/2);
                circle.setCenterX(circledi/2);
                circle.setCenterY(circledi/2);
                circle.setSmooth(true);
                circle.setTranslateX(col*(circledi+5) +circledi/4);
                circle.setTranslateY(row*(circledi+5)+circledi/4);
                rectanglewithholes=Shape.subtract(rectanglewithholes,circle);
            }
        }
        rectanglewithholes.setFill(Color.WHITE);
        return rectanglewithholes;
    }
    private List<Rectangle> createclickablecolumns() {
        List<Rectangle> rectanglelist = new ArrayList<>();
        for (int col = 0; col < columns; col++) {
            Rectangle rectangle = new Rectangle(circledi, (rows + 1) * circledi);
            rectangle.setFill(Color.TRANSPARENT);
            rectangle.setTranslateX(col * (circledi + 5) + circledi / 4);
            rectangle.setOnMouseEntered(event -> rectangle.setFill(Color.valueOf("#eeeeee26")));
            rectangle.setOnMouseExited(event -> rectangle.setFill(Color.TRANSPARENT));
            final int column=col;
            rectangle.setOnMouseClicked(event -> {
                if (isallowedtoinsert) {
                    isallowedtoinsert = false;
                    insertdisc(new Disc(isplayeroneturn), column);
                }

            });
            rectanglelist.add(rectangle);
        }

        return rectanglelist;
    }
        private  void insertdisc( Disc disc,int column){
        int row=rows-1;
        while (row>=0){
             if (getDiscifpresent(row,column)==null)
                break;
            row--;
        }
        if (row<0)
            return;
        inserteddiscsArray[row][column]=disc;
        inserteddiscspane.getChildren().add(disc);
        disc.setTranslateX(column * (circledi + 5) + circledi / 4);
        int currentRow=row;
            TranslateTransition translateTransition=new TranslateTransition(Duration.seconds(0.5),disc);
        translateTransition.setToY(row*(circledi+5)+circledi/4);
        translateTransition.setOnFinished(event -> {
            isallowedtoinsert=true;
            if (gameended(currentRow,column)){
                gameover();
                return;

            }
            isplayeroneturn=!isplayeroneturn;
            playernamelabel.setText(isplayeroneturn?player1:player2);
        });
        translateTransition.play();

        }
        private boolean gameended(int row,int column){
          List<Point2D> verticalpoints=  IntStream.rangeClosed(row-3,row+3)
                  .mapToObj(r-> new Point2D(r,column))
                  .collect(Collectors.toList());
            List<Point2D> horizontalpoints=  IntStream.rangeClosed(column-3,column+3)
                    .mapToObj(col-> new Point2D(row,col))
                    .collect(Collectors.toList());
            Point2D startpoint1=new Point2D(row-3,column+3);
            List<Point2D> diagonalpoints1=IntStream.rangeClosed(0,6)
                    .mapToObj(i-> startpoint1.add(i,-i))
                    .collect(Collectors.toList());
            Point2D startpoint2=new Point2D(row-3,column-3);
            List<Point2D> diagonalpoints2=IntStream.rangeClosed(0,6)
                    .mapToObj(i-> startpoint2.add(i,i))
                    .collect(Collectors.toList());
              boolean isended=checkcombinations(verticalpoints )   || checkcombinations(horizontalpoints)|| checkcombinations(diagonalpoints1)||checkcombinations(diagonalpoints2);

        return isended;
        }
        private boolean checkcombinations(List<Point2D> points){
            int chain=0;
            for (Point2D point:points ){

                int rowindexForArray=(int) point.getX();
                int columnindexForArray= (int) point.getY();
                Disc disc=getDiscifpresent(rowindexForArray,columnindexForArray);
                if (disc!=null&& disc.isplayer1move==isplayeroneturn){
                    chain++;
                    if (chain==4){
                        return true;
                    }
                }else {
                    chain=0;
                }

            }
            return false;
        }
        private Disc getDiscifpresent(int row,int column){
       if (row>=rows||row<0||column>=columns||column<0)
           return null;

           return inserteddiscsArray[row][column];
        }

        private void gameover(){
        String winner=isplayeroneturn?player1:player2;
            System.out.println("WINNER IS :    "+winner);
            Alert alert=new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("CONNECT FOUR");
            alert.setHeaderText("THE WINNER IS : "+winner);
            alert.setContentText("WANT TO PLAY AGAIN?");
            ButtonType yesbtn=new ButtonType("YES");
            ButtonType nobtn=new ButtonType("NO,EXIT");
            alert.getButtonTypes().setAll(yesbtn,nobtn);
            Platform.runLater(()->{
                Optional<ButtonType> btnclicked=alert.showAndWait();
                if (btnclicked.isPresent()&& btnclicked.get()==yesbtn){
                    resetgame();
                }else{
                    Platform.exit();
                    System.exit(0);
                }


            });


        }

    public void resetgame() {
        inserteddiscspane.getChildren().clear();
        nametextfield.clear();
        name2textfield.clear();
        for (int row = 0; row <inserteddiscsArray.length ; row++) {
            for (int col=0;col<inserteddiscsArray[row].length;col++){
                inserteddiscsArray[row][col]=null;
            }

        }
        isplayeroneturn=true;
        playernamelabel.setText(player1);
        createplayground();
    }

    private static    class Disc extends Circle{
            private  final boolean isplayer1move;
            public Disc(boolean isplayer1move){
                this.isplayer1move=isplayer1move;
                setRadius(circledi/2);
                setFill(isplayer1move? Color.valueOf(disccolor1):Color.valueOf(disccolor2));
                setCenterY(circledi/2);
                setCenterX(circledi/2);

            }

        }


    @Override
    public void initialize(URL location, ResourceBundle resources)
    {


    }


}
