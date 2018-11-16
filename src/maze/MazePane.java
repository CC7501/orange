package maze;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;

public class MazePane extends Application{
	//保存寻找到的所有路径
	ArrayList<int[][]> path = null;
	
	//表示显示的是第几条路径
	int count = 0;
	
	//迷宫大小，默认大小为20
	int size = 20;
	
	//迷宫最大size
	static final int MAXSIZE = 150;
	
	//初始迷宫，0代表路，1代表墙
	int [][] maze = new int[size+1][size+1];
	
	//矩形数组，将初始迷宫反映到面板
	Rectangle[][] rectangle = new Rectangle[size+1][size+1];
	
	//迷宫面板
	GridPane mazePane = new GridPane();
	
	//起点坐标
	int startX = 1;
	int startY = 1;
	//终点坐标
	int endX = size-1;
	int endY = size-1;
	
	TextField tfSize = new TextField(""+size);
	
	//总面板
	BorderPane mainPane = new BorderPane();
	static Text text = new Text("");
	
	@Override
	public void start(Stage stage) throws Exception {
		//初始化数组
		initialize(maze);
		
		//在数组中生成迷宫地图(0,1表示是否可走)
		new GetMaze(maze).getMaze(0,0,size,size);
		
		//背景图片
		Image beijing = new Image("tupian/beijing.png");
		ImageView beijingView = new ImageView(beijing);
		mainPane.getChildren().add(beijingView);
		
		
		//初始化矩形数组，并将矩形添加到面板中
		initializeRectangle();
		
		//将maze数组地图以不同颜色的形式反映到矩形中
		inportToRectangle(rectangle);

		//接收用户输入的起点坐标和终点坐标
		TextField tfStartX = new TextField(startX+"");
		TextField tfStartY = new TextField(startY+"");
		TextField tfEndX = new TextField(endX+"");
		TextField tfEndY = new TextField(endY+"");		
		tfStartX.setPrefColumnCount(2);
		tfStartY.setPrefColumnCount(2);
		tfEndX.setPrefColumnCount(2);
		tfEndY.setPrefColumnCount(2);
		
		Label lsX = new Label("start X:",tfStartX);//label start X
		lsX.setContentDisplay(ContentDisplay.RIGHT);
		Label lsY = new Label("start Y:",tfStartY);//label start Y
		lsY.setContentDisplay(ContentDisplay.RIGHT);
		Label leX = new Label("end X:",tfEndX);//label end X
		leX.setContentDisplay(ContentDisplay.RIGHT);
		Label leY = new Label("end Y:",tfEndY);//label end Y
		leY.setContentDisplay(ContentDisplay.RIGHT);

		//显示堆栈信息
		TextArea taStack = new TextArea();
		taStack.setMaxSize(120, 250);
		taStack.setMinSize(120, 250);
		taStack.setEditable(false);
		
		text.setFont(Font.font("宋体", 20));
		
		//直接获取路径
		Button btFound = new Button("寻找\n路径",new ImageView(new Image("tupian/图片2.png")));
		btFound.setOnAction(e->{
			//在生成迷宫时将path赋空，在点击寻找按钮后将path赋值，若path已有值，就遍历path所有路径，依次在面板显示
			
			//此处判断是否是新生成的迷宫
			if(path == null) {
				Search s = new Search(maze,startX,startY,endX,endY);//生成search对象
				path = s.searchAllPath();
				System.out.println(path.size());
			}
			
			//如果没有路径则不显示
			if(path.size() == 0)
				return;
			text.setText("共"+path.size()+"条路径，这是第"+(count+1)+"条！");
			//maze存暂时要显示的路径
			int[][] maze = path.get(count);
			
			//计数，下次显示另一条路径
			count = count+1;
			if(count == path.size())
				count = 0;
			System.out.println(count);
			
			//将maze路径反映到矩形数组
			for(int i = 0; i < maze.length; i++) {
				for(int j = 0; j < maze[i].length; j++) {
					if(maze[i][j] != 1)
						rectangle[i][j].setFill(Color.GAINSBORO);
					if(maze[i][j] != 1 && maze[i][j] != 0)
						rectangle[i][j].setFill(Color.GREEN);
				}
			}
		});
		
		
		
		//随机生成地图
		Button btNewMap = new Button("生成\n地图",new ImageView(new Image("tupian/图片4.png")));
		btNewMap.setOnAction(e->{
			//生成新迷宫时把已有路径赋空
			path = null;
			count = 0;
			//如果用户重新输入了迷宫大小，就重新生成迷宫数组和矩形数组
			if(maze.length != size+1) {
				maze = new int[size+1][size+1];
				rectangle = new Rectangle[size+1][size+1];
				mazePane.getChildren().clear();
				initializeRectangle();
				endX = size-1;
				endY = size-1;
				tfEndX.setText(endX+"");
				tfEndY.setText(endY+"");
			}
			
			//否则直接重新生成原来大小的迷宫
			initialize(maze);//重新初始化数组
			new GetMaze(maze).getMaze(0,0,size-1,size-1);//重新在数组中生成迷宫地图
			inportToRectangle(rectangle);//再将maze数组中的地图导入矩形数组
			text.setText("已重新随机生成新迷宫！");
//			try {
//				PrintWriter print = new PrintWriter("C:\\Users\\12085\\Desktop\\101.txt");
//				for(int i = 0; i < size+1; i++) {
//					for(int j = 0; j < size+1; j++) {
//						print.print(maze[i][j]+" ");
//					}
//					print.println();
//				}
//				print.close();
//			} catch (FileNotFoundException e1) {
//				e1.printStackTrace();
//			}
			
		});
		
		//显示寻路过程
		Button btoneStepAdvance = new Button("单步\n寻路",new ImageView(new Image("tupian/图片6.png")));
		btoneStepAdvance.setOnAction(e->{
			//将显示栈内元素的textArea赋空
			text.setText("正在寻路请等待...");
			OneStepAdvance s = new OneStepAdvance(maze,startX,startY,endX,endY,taStack,rectangle);
			s.oneStepAdvance();//单步寻路
		});
		
		//遍历迷宫
		Button btTraversal = new Button("遍历\n迷宫",new ImageView(new Image("tupian/图片3.png")));
		btTraversal.setOnAction(e->{
			text.setText("正在遍历请等待...");
			Traversal t = new Traversal(maze, size);
			t.search(rectangle);
		});
		
		Button btManualMaze = new Button("导入\n迷宫",new ImageView(new Image("tupian/图片8.png")));
		btManualMaze.setOnAction(e->{
			//文件选择器
			FileChooser jfc=new FileChooser();
			
			jfc.setTitle("选择迷宫文件");
			
			//文件类型过滤
			ExtensionFilter filter = new ExtensionFilter("文本文件(*.txt)","*.txt");
			//设置过滤器
			jfc.setSelectedExtensionFilter(filter);
			//得到打开的文件
			File file = jfc.showOpenDialog(null);
			try {
				Scanner input = new Scanner(file);
				size = input.nextInt()-1;
				if(size < 8) {
					size = 8;
					new GetMaze(maze).getMaze(0,0,size-1,size-1);					initializeRectangle();
					inportToRectangle(rectangle);
					endX = size-1;
					endY = size-1;
					tfEndX.setText(endX+"");
					tfEndY.setText(endY+"");
					tfSize.setText(size+"");
				}
				maze = new int[size+1][size+1];
				rectangle = new Rectangle[size+1][size+1];
				mazePane.getChildren().clear();
				for(int i = 0; i < size+1; i++) {
					for(int j = 0; j < size+1; j++) {
						maze[i][j] = input.nextInt();
					}
				}
				input.close();
				initializeRectangle();
				inportToRectangle(rectangle);
				endX = size-1;
				endY = size-1;
				tfEndX.setText(endX+"");
				tfEndY.setText(endY+"");
				tfSize.setText(size+"");
			
				text.setText("已成功导入迷宫！");
			} catch (FileNotFoundException e1) {
				e1.printStackTrace();
			}
			path = null;
			count = 0;
		});
		
		Button btDrawMaze = new Button("绘制\n迷宫",new ImageView(new Image("tupian/图片5.png")));
		btDrawMaze.setOnAction(e->{
			//生成新迷宫前将路径赋空
			path = null;
			count = 0;			
			if(btDrawMaze.getText().equals("绘制\n迷宫")) {
				text.setText("请在绘制完成后点击确认！");
				DrawMaze draw = new DrawMaze(size,rectangle);
				draw.drawMaze();
				btDrawMaze.setText("确定");
			}else {
				text.setText("迷宫已保存！");
				//若已经绘制完成，将矩阵颜色反映到maze以便寻路时将maze传给其他方法
				for(int i = 0; i < size+1; i++) {
					for(int j = 0; j < size+1; j++) {
						maze[i][j] = rectangle[i][j].getFill()==Color.GAINSBORO ? 0:1;
						rectangle[i][j].setOnMousePressed(null);
					}
				}
				btDrawMaze.setText("绘制\n迷宫");
			}
		});
		
		Button btShortestPath = new Button("最短\n路径",new ImageView(new Image("tupian/图片7.png")));
		btShortestPath.setOnAction(e->{
			
			text.setText("所示为最短路径！");
			Search s = new Search(maze,startX,startY,endX,endY);//生成search对象
			int[][] maze = s.searchShortPath();
			
			for(int i = 0; i < maze.length; i++) {
				for(int j = 0; j < maze[i].length; j++) {
					if(maze[i][j] != 1)
						rectangle[i][j].setFill(Color.GAINSBORO);
					if(maze[i][j] != 1 && maze[i][j] != 0)
						rectangle[i][j].setFill(Color.color(1, Math.random()/10, Math.random()/10));
				}
			}
		});

		ImageView shezhiView = new ImageView(new Image("tupian/图片1.png"));
		
		//接收用户输入的迷宫大小
		tfSize.setPrefColumnCount(2);
		Label lbSize = new Label("size   :",tfSize);
		lbSize.setContentDisplay(ContentDisplay.RIGHT);
		tfSize.setOnAction(e->{
			int n = Integer.parseInt(tfSize.getText());
			//如果输入迷宫大小太大则显示最大能生成的迷宫大小
			size = n < MAXSIZE ? n : MAXSIZE;
			size = n > 4 ? n : 4;
		}); 
		
		//按钮面板
		HBox buttonPane = new HBox(5);
		buttonPane.setAlignment(Pos.CENTER);
		buttonPane.setPadding(new Insets(0,10,10,10));
		buttonPane.setPadding(new Insets(0,10,10,10));
		buttonPane.getChildren().add(btNewMap);
		buttonPane.getChildren().add(btDrawMaze);
		buttonPane.getChildren().add(btManualMaze);
		buttonPane.getChildren().add(btFound);
		buttonPane.getChildren().add(btoneStepAdvance);
		buttonPane.getChildren().add(btTraversal);
		buttonPane.getChildren().add(btShortestPath);
		buttonPane.getChildren().add(shezhiView);
		Button btControl = new Button("确定");
		
		//生成新的stage放置设置大小和起点终点坐标的文本框
		ControlPane controlPane = new ControlPane();
		controlPane.addNode(lsX,lsY,leX,leY,lbSize,btControl);
		
		btControl.setOnAction(e->{
			setXY(tfStartX, tfStartY, tfEndX, tfEndY);
			int n = Integer.parseInt(tfSize.getText());
			
			//如果输入迷宫大小太大则显示最大能生成的迷宫大小
			size = n < MAXSIZE ? n : MAXSIZE;
			size = n > 4 ? n : 4;
			controlPane.close();
		});
		
		shezhiView.setOnMousePressed(e->{
			controlPane.show();
		});
		
		mazePane.setPadding(new Insets(0,10,10,10));
		mainPane.setPadding(new Insets(5,5,5,5));
		mainPane.setCenter(mazePane);
		mainPane.setRight(taStack);
		mainPane.setTop(buttonPane);
		mainPane.setBottom(text);
		Pane p = new Pane();
		p.setPrefSize(50, 30);
		mainPane.setLeft(p);
		
		Image fengmian = new Image("tupian/fengmian.png");
		ImageView fengmianView = new ImageView(fengmian);
		fengmianView.setOnMousePressed(e->{
			mainPane.getChildren().remove(fengmianView);
		});		
		mainPane.getChildren().add(fengmianView);
		
		stage.setScene(new Scene(mainPane,800,620));
		stage.show();
	}
	
	public static void main(String[] args) {
		Application.launch(args);
	}
	
	//接收用户输入的起始和终点坐标，并判断此位置是否有障碍物
	private void setXY(TextField tfX1,TextField tfY1,TextField tfX2,TextField tfY2){
		int x1 = Integer.parseInt(tfX1.getText());
		int y1 = Integer.parseInt(tfY1.getText());
		int x2 = Integer.parseInt(tfX2.getText());
		int y2 = Integer.parseInt(tfY2.getText());
		if(maze[x1][y1]==1||maze[x2][y2]==1){
			//如果设置的终点位置有墙，在墙上，则输出no
			text.setText("设置失败：该点为墙壁");
			return;
		}
		else{
			text.setText("设置成功！");
			startX = x1;
			startY = y1;
			endX = x2;
			endY = y2;
		}
	}
	
	//初始化maze数组
	private void initialize(int[][] maze){
		for(int i = 0; i < maze.length; i++){
			for(int j = 0; j < maze[i].length; j++){
				maze[i][j] = 0;
			}
		}
		for(int i = 0; i < maze.length; i++) {
			maze[0][i] = 1;
			maze[i][0] = 1;
			maze[i][size] = 1;
			maze[size][i] = 1;
		}
	}
	
	//将maze数组地图导入矩形数组
	private void inportToRectangle(Rectangle[][] rectangle){
		for(int i = 0; i < maze.length; i++) {
			for(int j = 0; j < maze[i].length; j++) {
				//若为通路则涂成灰色，墙壁则涂成蓝色
				rectangle[i][j].setFill(maze[i][j] == 0? Color.GAINSBORO:Color.color(0.2, 0.4, Math.random()/5+0.8));
			}
		}
	}
	
	//初始化矩形数组
	private void initializeRectangle() {
		for(int i = 0; i < rectangle.length; i++){
			for(int j = 0; j < rectangle[i].length; j++){
				rectangle[i][j] = new Rectangle(0,0,500/(size+1),500/(size+1));
				rectangle[i][j].setStroke(Color.ANTIQUEWHITE);
				rectangle[i][j].setStrokeWidth(20.0/size);
				mazePane.add(rectangle[i][j],j,i);
			}	
		}
	}
}