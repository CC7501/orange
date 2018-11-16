package maze;

import java.util.ArrayList;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.control.TextArea;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

public class OneStepAdvance {
	
	ArrayList<Point> list = new ArrayList<>();
	Animation animation;
	Rectangle[][] rectangle;
	int[][] maze;
	int fromX = 1;
	int fromY = 1;
	int endX = 1;
	int endY = 1;
	boolean again = false;//表示是否开始寻路
	TextArea textArea;

	
	public OneStepAdvance(int[][] maze,int fromX,int fromY,int endX,int endY,TextArea textArea,Rectangle[][] rectangle){
		this.maze = maze;		
//		p = new P[maze.length][maze[0].length];//
		this.fromX = fromX;
		this.fromY = fromY;
		this.endX = endX;
		this.endY = endY;
		this.textArea = textArea;
		textArea.setText("");
		this.rectangle = rectangle;
		//添加起点，四个方向均为true表示可走
		list.add(new Point(fromX,fromY,true,true,true,true,0));
	}
	
	
	public void oneStepAdvance(){
		for(int i = 0; i < maze.length; i++) {
			for(int j = 0; j < maze[i].length; j++) {
				if(maze[i][j] != 1)
					rectangle[i][j].setFill(Color.GAINSBORO);
			}
		}

		rectangle[fromX][fromY].setFill(Color.LIMEGREEN);
		
		if(animation == null){
			animation = new Timeline(new KeyFrame(Duration.millis(100),e->{
				Point p = list.get(list.size()-1);
				//如果到达终点则返回
				if(p.x == endX&&p.y == endY) {
					System.out.println("stop");
					textArea.setText("( "+p.y+" , "+p.x+" )\n"+textArea.getText());
					System.out.println("( "+p.y+" , "+p.x+" )");
					MazePane.text.setText("寻路结束！栈内信息在右侧显示！");
					animation.stop();
					return;
				}
				
				int n = action(Color.GAINSBORO);//Color.GAINSBORO
				if(n == 0||n == 1){
					System.out.println("stop");
					animation.stop();
				}
			}));
			animation.setCycleCount(Timeline.INDEFINITE);
			animation.play();
		}
		else{
			animation.play();
		}
	}
	
	
	//前进
	public void advance() {
		if(maze == null)
			return;
		for(;;) {
			Point p = list.get(list.size()-1);
			if(p.x == endX&&p.y == endY)
				break;
			int n = action(Color.GAINSBORO);//调用一个前进的动作 Color.GAINSBORO
			if(n == 1||n == 0)
				break;
			else
				continue;
		}
	}
	
	public int action(Color breakColor) {//退回的颜色  以便于遍历迷宫以蓝色做标记
		//获取list中最后添加的一个元素（栈顶元素）
		Point p = list.get(list.size()-1);
		
		
		//如果回到原点，并且again为true，并且原点的四个方向都不能再走，则表示没有路可以到达终点
		if(again && p.x == fromX && p.y == fromY && !p.down && !p.left && !p.up && !p.right) {
			return 0;
		}
		
		//如果此点四周都走过，无路可走则回退一步
		if(!(p.down||p.up||p.left||p.right)) {
			backout(breakColor);
			return 2;
		}
		
		again = true;
		
		String pxy = "";
		
		//按先右 再下 再左 再上的顺序判断是否有路可走
		//如果p点的右方向为true，且向右不是墙壁，且向右没有被涂成绿色则进入if
		
		int[][] direction = {{0,1},{1,0},{0,-1},{-1,0}};
		boolean[] dir = {p.right,p.down,p.left,p.up};
		Direction[] d = {Direction.RIGHT,Direction.DOWN,Direction.LEFT,Direction.UP};
		int i = 0;
		for(i = 0; i < 4; i++) {
			int newX = p.x+direction[i][0];
			int newY = p.y+direction[i][1];
		
			if(i == 1)
				p.right = false;
			else if(i == 2)
				p.down = false;
			else if(i == 3)
				p.left = false;
			boolean[] emm = {true,true,true,true};
			//               left,up  ,right,down
			if(dir[i] && maze[newX][newY]==0 && !have(newX,newY)) {
				emm[i] = false;
				//进入if将向右的一点加入list，并将反方向赋值为false，避免回退
				list.add(new Point(newX,newY,emm[1],emm[3],emm[0],emm[2],p.count+1));
				
				//若为单步执行，则rectangle属性被赋值，若为直接寻找路径则rectangle没有赋值
				//两种寻路方法均调用此方法故进行判断
				if(rectangle!=null){
					rectangle[newX][newY].setFill(Color.color(Math.random()/5+0.2, Math.random()/5+0.8, 0.1));
				}
				pxy = "( "+p.y+" , "+p.x+" )\n";

				p.nextDirection = d[i];
				break;
			}
		}
		if(i == 4)
			p.up = false;

		if(textArea!=null) {
			textArea.setText(pxy+textArea.getText());
		}
		return 2;
	}
	
	//判断此位置是否已经走过，已经被涂成绿色
	public boolean have(int x, int y) {
		for(int i = 0; i < list.size(); i++) {
			Point p = list.get(i);
			if(p.x == x && p.y == y) {
				return true;
			}
		}
		return false;
	}
	
	
	//回退，将list中最后一个元素删除
	public void backout(Color color){//参数表示回退处涂成什么颜色
		//删除并获取最后一个点
		Point lastp = list.remove(list.size()-1);
		
		//如果是单步寻路则将退回的格子重新涂成灰色
		if(rectangle != null)
			rectangle[lastp.x][lastp.y].setFill(color);
		if(textArea != null) {
			String text = textArea.getText();
			String texts[] = text.split("\n");
			String str = text.substring(texts[0].length()+1, text.length());
			textArea.setText(str);
		}
		
		//获取新的最后一个点
		Point p = list.get(list.size()-1);
		//判断被删除的点是此点的那个方向，并将此方向赋值为false，避免重复判断
		Direction n = p.nextDirection;
		switch(n) {
		case UP:
			p.up = false;
			break;
		case DOWN:
			p.down = false;
			break;
		case LEFT:
			p.left = false;
			break;
		case RIGHT:
			p.right = false;
			break;
		}
	}
	//每个对象为面板中的一个矩形的位置
}

enum Direction{
	UP,DOWN,LEFT,RIGHT
}

class Point{
	int x;
	int y;
	
	//四个方向是否可行
	boolean up = true;
	boolean down = true;
	boolean left = true;
	boolean right = true;
	
	int count = 0;
	//下一个点在哪个方向上
	Direction nextDirection;//up-1,down-2,left-3,right-4
	
	Point(int x,int y){
		this.x = x;
		this.y = y;
	}
	
	Point(int x,int y,boolean up,boolean down,boolean left,boolean right,int count){
		this.x = x;
		this.y = y;
		this.down = down;
		this.up = up;
		this.left = left;
		this.right = right;
		this.count = count;
	}
}
