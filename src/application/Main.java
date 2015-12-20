package application;
	
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;


import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.text.Text;


public class Main extends Application {
	static ArrayList<float[]> inputArray = new ArrayList<float[]>();
	static ArrayList<float[]> sortedArray = new ArrayList<float[]>();
	static ArrayList<Color> colorArray = new ArrayList<Color>();
	static ArrayList<ArrayList<neuralCordinate>> neuralArray = new ArrayList<ArrayList<neuralCordinate>>(); ;
	static ArrayList<ArrayList<Float>> distanceArray ;
	static ArrayList<ArrayList<Float>> probArray ;
	
	static Pane canvas = new Pane();
	
	static int sortedNewDesire = 0;
	static int classAmount;
	static int net;
	static int looptimesLimit;
	static float initailStudyRate;
	static float initailArea;
	
	static float layoutX = 800;
	static float layoutY = 800;
	static int dataRatio = 200;
	

	static Color red = Color.RED;
	static Color green = Color.GREEN;
	static Color blue = Color.BLUE;
	static Color black = Color.BLACK;
	static Color white = Color.WHITE;
	
	class neuralCordinate{
		float x;
		float y;
		int p1;
		int p2;
		float disValue;
		
		public neuralCordinate(float iniX,float iniY,int p1,int p2){
			this.x=iniX;
			this.y=iniY;
			this.p1 = p1;
			this.p2 = p2;
			this.disValue=0;
		}
		
		public void setX(float inX){
			x = inX;
		}
		public void setY(float inY){
			y = inY;
		}
		public void doCal(float inX,float inY){
			disValue = (float) Math.abs(Math.hypot(inX-x, inY-y));
		}
		public float getSum(){
			return disValue;
		}
		public float getX(){
			return x;
		}
		public float getY(){
			return y;
		}
		public int getP1(){
			return p1;
		}
		public int getP2(){
			return p2;
		}
	}
	
	@Override
	public void start(Stage primaryStage) {
		try {

			GridPane root = new GridPane();
			
			root.setAlignment(Pos.TOP_LEFT);
			root.setHgap(5);
			root.setVgap(5);
			root.setGridLinesVisible(false);
			
			VBox btnVbox = new VBox(20);
			Button chooseFile = new Button("Choose File");
			Button generateRandomNet = new Button("Generate Neurals");
			Button doAlgo = new Button("Go !");
			Text studyRatemsg = new Text("studyrate : ");
			Text omegamsg = new Text("omega : ");
			Text looptimesmsg = new Text("looptimes : ");
			Text neuralmsg = new Text("neural * neural : ");
			TextField studyRateText = new TextField("1");
			TextField omegaText = new TextField("1");
			TextField looptimesText = new TextField("100");
			TextField neuralText = new TextField("5");


			btnVbox.setPadding(new Insets(15,15,15,15));
			btnVbox.getChildren().addAll(chooseFile,generateRandomNet,doAlgo,neuralmsg,neuralText,studyRatemsg,studyRateText,omegamsg,omegaText,looptimesmsg,looptimesText);
			
			canvas.setStyle("-fx-background-color: #AAAAAA");
			
			drawCordinateLine();

			chooseFile.setOnMouseClicked(e ->{
				
				inputArray = new ArrayList<float[]>();
				
				sortedArray = new ArrayList<float[]>();

				colorArray = new ArrayList<Color>();

				neuralArray = new ArrayList<ArrayList<neuralCordinate>>();

				
				canvas.getChildren().remove(2, canvas.getChildren().size());


				try {
					inputFileChoose(null);
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					System.exit(0);
					e1.printStackTrace();
				}
				
				normalizeData(inputArray);
		
				sortInputArray(inputArray);
								
				classAmount = sortedNewDesire + 1;

				System.out.println(classAmount);
				
				colorType();
				
				drawDataPoints();
				
			});
			
			
			generateRandomNet.setOnMouseClicked(e -> {
				
				canvas.getChildren().remove(2, canvas.getChildren().size());
				
				net = Integer.parseInt(neuralText.getText());
				
				neuralArray = new ArrayList<ArrayList<neuralCordinate>>();
								
				for(int i=0;i<net;i++){
					ArrayList<neuralCordinate> an = new ArrayList<neuralCordinate>();
					for(int j=0;j<net;j++){
						neuralCordinate n = new neuralCordinate(generateRandom(),generateRandom(),i,j);
						an.add(n);
					}
					neuralArray.add(an);
				}
								
				drawDataPoints();

				drawNet(Color.SKYBLUE);

			});
	
			
			doAlgo.setOnMouseClicked(e ->{
				
				net = Integer.parseInt(neuralText.getText());
				looptimesLimit = Integer.parseInt(looptimesText.getText());
				initailStudyRate = Float.parseFloat(studyRateText.getText());
				initailArea = Float.parseFloat(omegaText.getText());
				
				initialProbArray();

				int noOfData = 0;
				int looptimes =0;

				while(looptimes<looptimesLimit){

					float studyRate = (float) (initailStudyRate*(Math.exp(-(looptimes/looptimesLimit))));
					float area = (float) (initailArea*(Math.exp(-(looptimes/looptimesLimit))));

					distanceArray = new ArrayList<ArrayList<Float>>();
					int winnerI = 0;
					int winnerJ = 0;
					float sumTemp = Float.MAX_VALUE;
					float dataX = sortedArray.get(noOfData)[0];
					float dataY = sortedArray.get(noOfData)[1];
					
					
					for(int i =0;i<neuralArray.size();i++){
						for(int j=0;j<neuralArray.get(i).size();j++){
							neuralArray.get(i).get(j).doCal(dataX,dataY);
						}
					}
				
					for(int i =0;i<neuralArray.size();i++){
						for(int j=0;j<neuralArray.get(i).size();j++){
							float thisNeuralSum = neuralArray.get(i).get(j).getSum();
							if(thisNeuralSum <= sumTemp){
								sumTemp = thisNeuralSum;
								winnerI = i;
								winnerJ = j;
							}
						}
					}
					
					for(int i =0;i<neuralArray.size();i++){
						for(int j=0;j<neuralArray.get(i).size();j++){
							if(i==winnerI&&j==winnerJ){
								probArray.get(i).set(j, (float) (probArray.get(i).get(j)+0.001*(1-probArray.get(i).get(j))));
							}
							else{
								probArray.get(i).set(j, (float) (probArray.get(i).get(j)+0.001*(0-probArray.get(i).get(j))));
							}
						}
					}
									
					for(int i =0;i<neuralArray.size();i++){
					
						ArrayList<Float> tempArray = new ArrayList<Float>();
						for(int j=0;j<neuralArray.get(i).size();j++){
							
							float dist = 0;
							float b = 10*((1/net*net)-probArray.get(i).get(j));
							float x1 = neuralArray.get(winnerI).get(winnerJ).getP1();
							float y1 =neuralArray.get(winnerI).get(winnerJ).getP2();
							float x2 = neuralArray.get(i).get(j).getP1();
							float y2 = neuralArray.get(i).get(j).getP2();
							dist = (float) Math.hypot(x1-x2, y1-y2);
							dist = dist-b;
							tempArray.add(dist);

						}
						distanceArray.add(tempArray);
					}

					
					for(int i =0;i<neuralArray.size();i++){
						for(int j=0;j<neuralArray.get(i).size();j++){
							float oriX = neuralArray.get(i).get(j).getX();
							float oriY = neuralArray.get(i).get(j).getY();
							neuralArray.get(i).get(j).setX((float)(oriX+(studyRate*Math.exp(-1*Math.pow(distanceArray.get(i).get(j), 2)/(2*(area*area))))*(dataX-oriX)));
							neuralArray.get(i).get(j).setY((float)(oriY+(studyRate*Math.exp(-1*Math.pow(distanceArray.get(i).get(j), 2)/(2*(area*area))))*(dataY-oriY)));
						}
					}		
					
					if(noOfData==sortedArray.size()-1){
						noOfData=0;
						Collections.shuffle(sortedArray);
						looptimes++;
					}
					else{
						noOfData++;
					}
				}
				
				canvas.getChildren().remove(2, canvas.getChildren().size());
								
				drawDataPoints();

				drawNet(Color.YELLOW);
			});
			
			root.add(btnVbox, 0, 0);
			root.add(canvas, 1, 0);

			Scene scene = new Scene(root,layoutX+200,layoutY);
			
			primaryStage.setScene(scene);
			primaryStage.show();
			
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	public static void initialProbArray(){
		probArray = new ArrayList<ArrayList<Float>>();
		
		for(int i =0;i<neuralArray.size();i++){
			ArrayList<Float> tempArray = new ArrayList<Float>();
			for(int j=0;j<neuralArray.get(i).size();j++){
				float dist = 0;
				tempArray.add(dist);
			}
			probArray.add(tempArray);
		}	
	}
	
	public static float generateRandom(){
		Random rand = new Random();
		float a = 0;
		if (Math.random() > 0.5) {
			a = rand.nextFloat() + 0f;
		} else {
			a = rand.nextFloat() - 1f;
		}
		return a;
	}
	
	public static void drawNet(Color color){
		drawNeuralLine(color);
		drawNeurals();
	}
	
	public static void drawNeurals(){
		
		for(int j = 0 ;j<neuralArray.size();j++){
			for(int i = 0;i<neuralArray.get(j).size();i++){
				Circle circle = new Circle();
				circle.setCenterX(neuralArray.get(j).get(i).getX()*dataRatio+(layoutX/2));
				circle.setCenterY((-neuralArray.get(j).get(i).getY())*dataRatio+(layoutY/2));
				circle.setRadius(2);
				circle.setFill(Color.WHITE);
				canvas.getChildren().add(circle);
			}
		}
	}
	
	public static void drawNeuralLine(Color colorType){
		for(int i = 0;i<neuralArray.size();i++){
			for(int j=0;j<neuralArray.get(i).size()-1;j++){
				Line linkH = new Line();
				linkH.setStroke(colorType);
				linkH.setStartX(neuralArray.get(i).get(j).getX()*dataRatio+(layoutX/2));
				linkH.setStartY((-neuralArray.get(i).get(j).getY())*dataRatio+(layoutY/2));
				linkH.setEndX(neuralArray.get(i).get(j+1).getX()*dataRatio+(layoutX/2));
				linkH.setEndY((-neuralArray.get(i).get(j+1).getY())*dataRatio+(layoutY/2)); 
				
				canvas.getChildren().add(linkH);			
			}
		}
		for(int i=0;i<neuralArray.size()-1;i++){
			for(int j=0;j<neuralArray.get(i).size();j++){
				Line linkV = new Line();
				linkV.setStroke(colorType);
				linkV.setStartX(neuralArray.get(i).get(j).getX()*dataRatio+(layoutX/2));
				linkV.setStartY((-neuralArray.get(i).get(j).getY())*dataRatio+(layoutY/2));
				linkV.setEndX(neuralArray.get(i+1).get(j).getX()*dataRatio+(layoutX/2));
				linkV.setEndY((-neuralArray.get(i+1).get(j).getY())*dataRatio+(layoutY/2));
				canvas.getChildren().add(linkV);
			}
		}
	}
	
	public static void drawDataPoints(){
		for(int i = 0;i<classAmount;i++){
			for(int j = 0 ;j<sortedArray.size();j++){
				if(sortedArray.get(j)[sortedArray.get(j).length-1]==i){
					Circle circle = new Circle();
					circle.setCenterX(sortedArray.get(j)[0]*dataRatio+(layoutX/2));
					circle.setCenterY((-sortedArray.get(j)[1])*dataRatio+(layoutY/2));
					circle.setRadius(2);
					circle.setFill(colorArray.get(i));
					canvas.getChildren().add(circle);
				}
			}
		}
	}
	
	public static void drawCordinateLine(){
		Line xLine = new Line();
		xLine.setStroke(black);
		xLine.setStartX(400);
		xLine.setStartY(0);
		xLine.setEndX(400);
		xLine.setEndY(800);
		Line yLine = new Line();
		yLine.setStroke(black);
		yLine.setStartX(0);
		yLine.setStartY(400);
		yLine.setEndX(800);
		yLine.setEndY(400);
		canvas.getChildren().addAll(xLine,yLine);
	}
	
	public static void colorType(){
		colorArray.add(red);
		colorArray.add(blue);
		colorArray.add(green);
		
		
		if(classAmount>3){
			float colorR = 100;
			float colorG = 100;
			float colorB = 100;
	
			for (int i = 3; i < classAmount; i++) {
	
				colorR += 10;
				if (colorR > 255) {
					colorR -= 255;
				}
	
				colorG += 30;
				if (colorG > 255) {
					colorG -= 255;
				}
	
				colorB += 40;
				if (colorB > 255) {
					colorB -= 255;
				}
	
				Color colorType = new Color(colorR, colorG, colorB,1.0f);
				colorArray.add(colorType);
			}
		}
	}
	
	private static void normalizeData(ArrayList<float[]> array) {
		/*
		 * idea: find the biggest number(no matter positive or
		 *       negative ,set it as denominator
		 */
		float maxX = Float.MIN_VALUE;
		float maxY = Float.MIN_VALUE;
		
		for (int i = 0; i < array.size(); i++) {
			if (Math.abs(array.get(i)[0]) > maxX) {
				maxX = Math.abs(array.get(i)[0]);
			}
			if (Math.abs(array.get(i)[1]) > maxY) {
				maxY = Math.abs(array.get(i)[1]);
			}
		}
		for (int i = 0; i < array.size(); i++) {
				array.get(i)[0] /= maxX;
				array.get(i)[1] /= maxY;
		}
	}
	
	public static void sortInputArray(ArrayList<float[]> inputArray) {
		/*
		 * 1. set loop times = inputArray's dataamount 
		 * 2. in while loop we have to dynamic change loop times cause we had
		 *    remove some data in the array to reduce loop times 
		 * 3. set a variable-standardDesire is mean the first data's desire , 
		 *    then use it to check one by one ,if found someone is as same as 
		 *    the standardDesire, put this data to sortedArray, so on ,we can
		 *    get a sorted array which's desire is from 1 to number of class
		 * 4. everytime move a item to sortedArray , raise iRestFlag and set
		 *    i to 0, then it will run loop from beginning 
		 * 5. when inputarray left only 1 item must set as -1, or the last 
		 *    data's desire will be set one more number
		 * 
		 */

		int iRestFlag = 0;
		sortedNewDesire = 0;
		System.out.println("--------- Start sort ---------");

		whileloop: while (true) {

			// set the first one's desire as standard
			int standardDesire = (int) inputArray.get(0)[inputArray.get(0).length - 1];
			System.out.println("Now the standartDesire is  : " + standardDesire);

			for (int i = 0; i < inputArray.size(); i++) {
				if (iRestFlag == 1) {
					i = 0;
				}
				if ((int) inputArray.get(i)[inputArray.get(i).length - 1] == standardDesire) {
					inputArray.get(i)[inputArray.get(i).length - 1] = sortedNewDesire;
					sortedArray.add(inputArray.get(i));
					inputArray.remove(i);
					iRestFlag = 1;
				} else {
					iRestFlag = 0;
				}
				if (inputArray.size() == 1) {// the last data need set i=-1 to
												// prevent after forloop's i++
					i = -1;
				}
			}
			if (inputArray.size() == 0) {
				System.out.println("--------- Sort done! ---------");
				System.out.println("");
				break whileloop;
			} else {
				sortedNewDesire++;// count desire
			}
		}
		System.out.println("The max sorted desire : " + sortedNewDesire);
	}
		
	public static void inputFileChoose(String[] args) throws IOException {

		Stage fileStage = new Stage();
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Open Resource File");
		fileChooser.setInitialDirectory(new File("C:\\Users\\Terry_Lab\\Desktop\\hw3 som"));
		
		File file = fileChooser.showOpenDialog(fileStage);
		System.out.println(file);

		FileReader fr = new FileReader(file);
		BufferedReader br = new BufferedReader(fr);// 在br.ready反查輸入串流的狀況是否有資料

		String txt;
		while ((txt = br.readLine()) != null) {
			/*
			 * If there is space before split(), it will cause the error So, we
			 * could to use trim() to remove the space at the beginning and the
			 * end. Then split the result, which doesn't include the space at
			 * the beginning and the end. "\\s+" would match any of space, as
			 * you don't have to consider the number of space in the string
			 */
			String[] token = txt.trim().split("\\s+");// <-----背起來
			// String[] token = txt.split(" ");//<-----original split
			float[] token2 = new float[token.length];// 宣告float[]

			try {
				for (int i = 0; i < token.length; i++) {
					token2[i] = Float.parseFloat(token[i]);
				} // 把token(string)轉乘token2(float)
				inputArray.add(token2);// 把txt裡面內容先切割過在都讀進array內
			} catch (NumberFormatException ex) {
				System.out.println("Sorry Error...");
			}
		}
		fr.close();// 關閉檔案

	}
	
	public static void main(String[] args){
		
		launch(args);
	}
	
	public static void printArrayData(ArrayList<float[]> showArray) {

		for (int i = 0; i < showArray.size(); i++) {
			for (int j = 0; j < showArray.get(i).length; j++) {
				System.out.print(showArray.get(i)[j] + "\t");
			}
			System.out.println("");
		}
		System.out.println("");
	}
	
	public static void printNeuralXY(){
		System.out.println("--------------------------");

		for(int i =0;i<neuralArray.size();i++){
			for(int j=0;j<neuralArray.get(i).size();j++){
				float showx = neuralArray.get(i).get(j).getX();
				float showy = neuralArray.get(i).get(j).getY();
				System.out.println("x: "+showx+"\t y: "+showy);
			}
		}	
		System.out.println("--------------------------");
	}
}
