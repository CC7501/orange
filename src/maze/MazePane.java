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
	//����Ѱ�ҵ�������·��
	ArrayList<int[][]> path = null;
	
	//��ʾ��ʾ���ǵڼ���·��
	int count = 0;
	
	//�Թ���С��Ĭ�ϴ�СΪ20
	int size = 20;
	
	//�Թ����size
	static final int MAXSIZE = 150;
	
	//��ʼ�Թ���0����·��1����ǽ
	int [][] maze = new int[size+1][size+1];
	
	//�������飬����ʼ�Թ���ӳ�����
	Rectangle[][] rectangle = new Rectangle[size+1][size+1];
	
	//�Թ����
	GridPane mazePane = new GridPane();
	
	//�������
	int startX = 1;
	int startY = 1;
	//�յ�����
	int endX = size-1;
	int endY = size-1;
	
	TextField tfSize = new TextField(""+size);
	
	//�����
	BorderPane mainPane = new BorderPane();
	static Text text = new Text("");
	
	@Override
	public void start(Stage stage) throws Exception {
		//��ʼ������
		initialize(maze);
		
		//�������������Թ���ͼ(0,1��ʾ�Ƿ����)
		new GetMaze(maze).getMaze(0,0,size,size);
		
		//����ͼƬ
		Image beijing = new Image("tupian/beijing.png");
		ImageView beijingView = new ImageView(beijing);
		mainPane.getChildren().add(beijingView);
		
		
		//��ʼ���������飬����������ӵ������
		initializeRectangle();
		
		//��maze�����ͼ�Բ�ͬ��ɫ����ʽ��ӳ��������
		inportToRectangle(rectangle);

		//�����û���������������յ�����
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

		//��ʾ��ջ��Ϣ
		TextArea taStack = new TextArea();
		taStack.setMaxSize(120, 250);
		taStack.setMinSize(120, 250);
		taStack.setEditable(false);
		
		text.setFont(Font.font("����", 20));
		
		//ֱ�ӻ�ȡ·��
		Button btFound = new Button("Ѱ��\n·��",new ImageView(new Image("tupian/ͼƬ2.png")));
		btFound.setOnAction(e->{
			//�������Թ�ʱ��path���գ��ڵ��Ѱ�Ұ�ť��path��ֵ����path����ֵ���ͱ���path����·���������������ʾ
			
			//�˴��ж��Ƿ��������ɵ��Թ�
			if(path == null) {
				Search s = new Search(maze,startX,startY,endX,endY);//����search����
				path = s.searchAllPath();
				System.out.println(path.size());
			}
			
			//���û��·������ʾ
			if(path.size() == 0)
				return;
			text.setText("��"+path.size()+"��·�������ǵ�"+(count+1)+"����");
			//maze����ʱҪ��ʾ��·��
			int[][] maze = path.get(count);
			
			//�������´���ʾ��һ��·��
			count = count+1;
			if(count == path.size())
				count = 0;
			System.out.println(count);
			
			//��maze·����ӳ����������
			for(int i = 0; i < maze.length; i++) {
				for(int j = 0; j < maze[i].length; j++) {
					if(maze[i][j] != 1)
						rectangle[i][j].setFill(Color.GAINSBORO);
					if(maze[i][j] != 1 && maze[i][j] != 0)
						rectangle[i][j].setFill(Color.GREEN);
				}
			}
		});
		
		
		
		//������ɵ�ͼ
		Button btNewMap = new Button("����\n��ͼ",new ImageView(new Image("tupian/ͼƬ4.png")));
		btNewMap.setOnAction(e->{
			//�������Թ�ʱ������·������
			path = null;
			count = 0;
			//����û������������Թ���С�������������Թ�����;�������
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
			
			//����ֱ����������ԭ����С���Թ�
			initialize(maze);//���³�ʼ������
			new GetMaze(maze).getMaze(0,0,size-1,size-1);//�����������������Թ���ͼ
			inportToRectangle(rectangle);//�ٽ�maze�����еĵ�ͼ�����������
			text.setText("����������������Թ���");
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
		
		//��ʾѰ·����
		Button btoneStepAdvance = new Button("����\nѰ·",new ImageView(new Image("tupian/ͼƬ6.png")));
		btoneStepAdvance.setOnAction(e->{
			//����ʾջ��Ԫ�ص�textArea����
			text.setText("����Ѱ·��ȴ�...");
			OneStepAdvance s = new OneStepAdvance(maze,startX,startY,endX,endY,taStack,rectangle);
			s.oneStepAdvance();//����Ѱ·
		});
		
		//�����Թ�
		Button btTraversal = new Button("����\n�Թ�",new ImageView(new Image("tupian/ͼƬ3.png")));
		btTraversal.setOnAction(e->{
			text.setText("���ڱ�����ȴ�...");
			Traversal t = new Traversal(maze, size);
			t.search(rectangle);
		});
		
		Button btManualMaze = new Button("����\n�Թ�",new ImageView(new Image("tupian/ͼƬ8.png")));
		btManualMaze.setOnAction(e->{
			//�ļ�ѡ����
			FileChooser jfc=new FileChooser();
			
			jfc.setTitle("ѡ���Թ��ļ�");
			
			//�ļ����͹���
			ExtensionFilter filter = new ExtensionFilter("�ı��ļ�(*.txt)","*.txt");
			//���ù�����
			jfc.setSelectedExtensionFilter(filter);
			//�õ��򿪵��ļ�
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
			
				text.setText("�ѳɹ������Թ���");
			} catch (FileNotFoundException e1) {
				e1.printStackTrace();
			}
			path = null;
			count = 0;
		});
		
		Button btDrawMaze = new Button("����\n�Թ�",new ImageView(new Image("tupian/ͼƬ5.png")));
		btDrawMaze.setOnAction(e->{
			//�������Թ�ǰ��·������
			path = null;
			count = 0;			
			if(btDrawMaze.getText().equals("����\n�Թ�")) {
				text.setText("���ڻ�����ɺ���ȷ�ϣ�");
				DrawMaze draw = new DrawMaze(size,rectangle);
				draw.drawMaze();
				btDrawMaze.setText("ȷ��");
			}else {
				text.setText("�Թ��ѱ��棡");
				//���Ѿ�������ɣ���������ɫ��ӳ��maze�Ա�Ѱ·ʱ��maze������������
				for(int i = 0; i < size+1; i++) {
					for(int j = 0; j < size+1; j++) {
						maze[i][j] = rectangle[i][j].getFill()==Color.GAINSBORO ? 0:1;
						rectangle[i][j].setOnMousePressed(null);
					}
				}
				btDrawMaze.setText("����\n�Թ�");
			}
		});
		
		Button btShortestPath = new Button("���\n·��",new ImageView(new Image("tupian/ͼƬ7.png")));
		btShortestPath.setOnAction(e->{
			
			text.setText("��ʾΪ���·����");
			Search s = new Search(maze,startX,startY,endX,endY);//����search����
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

		ImageView shezhiView = new ImageView(new Image("tupian/ͼƬ1.png"));
		
		//�����û�������Թ���С
		tfSize.setPrefColumnCount(2);
		Label lbSize = new Label("size   :",tfSize);
		lbSize.setContentDisplay(ContentDisplay.RIGHT);
		tfSize.setOnAction(e->{
			int n = Integer.parseInt(tfSize.getText());
			//��������Թ���С̫������ʾ��������ɵ��Թ���С
			size = n < MAXSIZE ? n : MAXSIZE;
			size = n > 4 ? n : 4;
		}); 
		
		//��ť���
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
		Button btControl = new Button("ȷ��");
		
		//�����µ�stage�������ô�С������յ�������ı���
		ControlPane controlPane = new ControlPane();
		controlPane.addNode(lsX,lsY,leX,leY,lbSize,btControl);
		
		btControl.setOnAction(e->{
			setXY(tfStartX, tfStartY, tfEndX, tfEndY);
			int n = Integer.parseInt(tfSize.getText());
			
			//��������Թ���С̫������ʾ��������ɵ��Թ���С
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
	
	//�����û��������ʼ���յ����꣬���жϴ�λ���Ƿ����ϰ���
	private void setXY(TextField tfX1,TextField tfY1,TextField tfX2,TextField tfY2){
		int x1 = Integer.parseInt(tfX1.getText());
		int y1 = Integer.parseInt(tfY1.getText());
		int x2 = Integer.parseInt(tfX2.getText());
		int y2 = Integer.parseInt(tfY2.getText());
		if(maze[x1][y1]==1||maze[x2][y2]==1){
			//������õ��յ�λ����ǽ����ǽ�ϣ������no
			text.setText("����ʧ�ܣ��õ�Ϊǽ��");
			return;
		}
		else{
			text.setText("���óɹ���");
			startX = x1;
			startY = y1;
			endX = x2;
			endY = y2;
		}
	}
	
	//��ʼ��maze����
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
	
	//��maze�����ͼ�����������
	private void inportToRectangle(Rectangle[][] rectangle){
		for(int i = 0; i < maze.length; i++) {
			for(int j = 0; j < maze[i].length; j++) {
				//��Ϊͨ·��Ϳ�ɻ�ɫ��ǽ����Ϳ����ɫ
				rectangle[i][j].setFill(maze[i][j] == 0? Color.GAINSBORO:Color.color(0.2, 0.4, Math.random()/5+0.8));
			}
		}
	}
	
	//��ʼ����������
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