package application;
	
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Random;
import javafx.application.Application;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;


public class Main extends Application {
	static ArrayList<float[]> inputArray = new ArrayList<float[]>();
	static ArrayList<float[]> sortedArray = new ArrayList<float[]>();
	static ArrayList<ArrayList<neuralCordinate>> neuralArray = new ArrayList<ArrayList<neuralCordinate>>();
	static ArrayList<ArrayList<Float>> distanceArray ;
	static ArrayList<Color> colorArray = new ArrayList<Color>();

	static int sortedNewDesire = 0;
	static int classAmount;
	static float initailStudyRate = 1f;
	static float initailArea = 1f;
	static float layoutX = 800;
	static float layoutY = 800;
	static int dataRatio = 80;
	
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
		float sumValue;
		public neuralCordinate(float iniX,float iniY,int p1,int p2){
			this.x=iniX;
			this.y=iniY;
			this.p1 = p1;
			this.p2 = p2;
			this.sumValue=0;
		}
		
		public void setX(float inX){
			x = inX;
		}
		public void setY(float inY){
			y = inY;
		}
		public void doSum(float inX,float inY){
//			System.out.println("inX: "+inX+"\t inY: "+inY);
//			sumValue = (float) Math.abs(Math.hypot(inX-x, inY-y));
			sumValue = inX*x+inY*y;
		}
		public float getSum(){
			return sumValue;
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
		
			inputFileChoose(null);
			
			normalizeData(inputArray);
	
			sortInputArray(inputArray);
			
			Collections.shuffle(sortedArray);

			printArrayData(sortedArray);
			
			classAmount = sortedNewDesire + 1;
			
			
			Pane canvas = new Pane();
			canvas.setStyle("-fx-background-color: #AAAAAA");
			
			canvas = drawLine(canvas);
			
			colorType();
			
			canvas = drawDataPoints(canvas);

			neuralCordinate n1 = new neuralCordinate(0,0,0,0);
			neuralCordinate n2 = new neuralCordinate(0,1,1,0);
			neuralCordinate n3 = new neuralCordinate(1,0,2,0);
			neuralCordinate n4 = new neuralCordinate(3,3,0,1);
			neuralCordinate n5 = new neuralCordinate(0, -1,1,1);
			neuralCordinate n6 = new neuralCordinate(2,4,2,1);
			neuralCordinate n7 = new neuralCordinate(-1, 4,0,2);
			neuralCordinate n8 = new neuralCordinate(2, -3,1,2);
			neuralCordinate n9 = new neuralCordinate(-2, 4,2,2);
			
			ArrayList<neuralCordinate> an1 = new ArrayList<neuralCordinate>();
			an1.add(n1);
			an1.add(n2);
			an1.add(n3);
			neuralArray.add(an1);
			ArrayList<neuralCordinate> an2 = new ArrayList<neuralCordinate>();
			an2.add(n4);
			an2.add(n5);
			an2.add(n6);
			neuralArray.add(an2);
			ArrayList<neuralCordinate> an3 = new ArrayList<neuralCordinate>();
			an3.add(n7);
			an3.add(n8);
			an3.add(n9);
			neuralArray.add(an3);
			
			canvas = drawNeurals(canvas);
			System.out.println(neuralArray.size());
			System.out.println(neuralArray.get(0).size());
			
			
			canvas = drawNeuralLine(canvas,Color.BROWN);
			
			printNeuralXY();
			
			int noOfData = 0;
			int looptimes =0;

			while(looptimes<4000){
				float studyRate = (float) (initailStudyRate*(Math.exp(-(looptimes/4000))));
				float area = (float) (initailArea*(Math.exp(-(looptimes/4000))));

				distanceArray = new ArrayList<ArrayList<Float>>();
				int winnerI = 0;
				int winnerJ = 0;
				float sumTemp = Float.MIN_VALUE;
				float dataX = sortedArray.get(noOfData)[0];
				float dataY = sortedArray.get(noOfData)[1];
				
				System.out.println("inputX: "+dataX+"\t inputY: "+dataY);
				
				for(int i =0;i<neuralArray.size();i++){
					for(int j=0;j<neuralArray.get(i).size();j++){
						neuralArray.get(i).get(j).doSum(dataX,dataY);
					}
				}
			
				for(int i =0;i<neuralArray.size();i++){
					for(int j=0;j<neuralArray.get(i).size();j++){
						float thisNeuralSum = neuralArray.get(i).get(j).getSum();
						System.out.println("i: "+i+"\t j: "+j+"\t som : "+thisNeuralSum);
						if(thisNeuralSum >= sumTemp){
							sumTemp = thisNeuralSum;
							winnerI = i;
							winnerJ = j;
						}
					}
				}
				
				System.out.println("winner info: ");
				System.out.println("winner i: "+winnerI+"\t winnder j:"+winnerJ);
				
				for(int i =0;i<neuralArray.size();i++){
					ArrayList<Float> tempArray = new ArrayList<Float>();
					for(int j=0;j<neuralArray.get(i).size();j++){
						float dist = 0;
						float x1 = neuralArray.get(winnerI).get(winnerJ).getP1();
						float y1 =neuralArray.get(winnerI).get(winnerJ).getP2();
						float x2 = neuralArray.get(i).get(j).getP1();
						float y2 = neuralArray.get(i).get(j).getP2();
						dist = (float) Math.hypot(x1-x2, y1-y2);
						tempArray.add(dist);
					}
					distanceArray.add(tempArray);
				}
				
				for(int i=0;i<distanceArray.size();i++){
					for(int j=0;j<distanceArray.get(i).size();j++){
						System.out.println("distance : "+Math.round(distanceArray.get(i).get(j)*1000)/1000f);
					}
				}
				
				for(int i =0;i<neuralArray.size();i++){
					for(int j=0;j<neuralArray.get(i).size();j++){
						float oriX = neuralArray.get(i).get(j).getX();
						float oriY = neuralArray.get(i).get(j).getY();
						neuralArray.get(i).get(j).setX((float)(oriX+(studyRate*Math.exp(Math.pow(distanceArray.get(i).get(j), 2)/-2*(area*area)))*(dataX-oriX)));
						neuralArray.get(i).get(j).setY((float)(oriY+(studyRate*Math.exp(Math.pow(distanceArray.get(i).get(j), 2)/-2*(area*area)))*(dataY-oriY)));
					
					}
				}		
				
				printNeuralXY();
				
				float weightMaxX = Float.MIN_VALUE;
				float weightMaxY = Float.MIN_VALUE;
				
				for(int i=0;i<neuralArray.size();i++){
					for(int j=0;j<neuralArray.get(i).size();j++){
						if (Math.abs(neuralArray.get(i).get(j).getX()) > weightMaxX) {
							weightMaxX = Math.abs(neuralArray.get(i).get(j).getX());
						}
						if (Math.abs(neuralArray.get(i).get(j).getY()) > weightMaxY) {
							weightMaxY = Math.abs(neuralArray.get(i).get(j).getY());
						}
					}
				}
				
				for(int i=0;i<neuralArray.size();i++){
					for(int j=0;j<neuralArray.get(i).size();j++){
						neuralArray.get(i).get(j).setX(neuralArray.get(i).get(j).getX()/weightMaxX);
						neuralArray.get(i).get(j).setY(neuralArray.get(i).get(j).getY()/weightMaxY);

					}
				}
				
				if(noOfData==sortedArray.size()-1){
					noOfData=0;
//					Collections.shuffle(sortedArray);
					looptimes++;
				}
				else{
					noOfData++;
				}
			}
			
//			canvas = drawNeurals(canvas);
			
			
			//remove all obj 
			//canvas.getChildren().remove(0, canvas.getChildren().size());
			//redraw new neural location
			canvas = drawNeurals(canvas);
			
			canvas = drawNeuralLine(canvas,Color.YELLOW);
			
			Scene scene = new Scene(canvas,layoutX,layoutY);
			
			primaryStage.setScene(scene);
			primaryStage.show();
			
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static float generateRandom(){
		Random rand = new Random();
		float a = 0;
		if (Math.random() > 0.5) {
			a = rand.nextFloat() + 0f;
			// System.out.println("weight : " + token[j]);
		} else {
			a = rand.nextFloat() - 1f;
		}
		return a;
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
	
	public static Pane drawNeuralLine(Pane canvas,Color colorType){
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
		return canvas;
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
	
	public static Pane drawLine(Pane canvas){
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
		return canvas;
	}
	
	public static Pane drawNeurals(Pane canvas){
		
		for(int j = 0 ;j<neuralArray.size();j++){
			for(int i = 0;i<neuralArray.get(j).size();i++){
				Circle circle = new Circle();
				circle.setCenterX(neuralArray.get(j).get(i).getX()*dataRatio+(layoutX/2));
				circle.setCenterY((-neuralArray.get(j).get(i).getY())*dataRatio+(layoutY/2));
				circle.setRadius(2);
				circle.setFill(Color.PURPLE);
				canvas.getChildren().add(circle);
			}
		}
		return canvas;
	}
	
	public static Pane drawDataPoints(Pane canvas){
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
		return canvas;
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
//		fileChooser.setInitialDirectory(new File("C:\\Users\\Terry\\Desktop\\nnhw3 SOM dataset"));
		
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
}