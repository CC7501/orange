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
	boolean again = false;//��ʾ�Ƿ�ʼѰ·
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
		//�����㣬�ĸ������Ϊtrue��ʾ����
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
				//��������յ��򷵻�
				if(p.x == endX&&p.y == endY) {
					System.out.println("stop");
					textArea.setText("( "+p.y+" , "+p.x+" )\n"+textArea.getText());
					System.out.println("( "+p.y+" , "+p.x+" )");
					MazePane.text.setText("Ѱ·������ջ����Ϣ���Ҳ���ʾ��");
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
	
	
	//ǰ��
	public void advance() {
		if(maze == null)
			return;
		for(;;) {
			Point p = list.get(list.size()-1);
			if(p.x == endX&&p.y == endY)
				break;
			int n = action(Color.GAINSBORO);//����һ��ǰ���Ķ��� Color.GAINSBORO
			if(n == 1||n == 0)
				break;
			else
				continue;
		}
	}
	
	public int action(Color breakColor) {//�˻ص���ɫ  �Ա��ڱ����Թ�����ɫ�����
		//��ȡlist�������ӵ�һ��Ԫ�أ�ջ��Ԫ�أ�
		Point p = list.get(list.size()-1);
		
		
		//����ص�ԭ�㣬����againΪtrue������ԭ����ĸ����򶼲������ߣ����ʾû��·���Ե����յ�
		if(again && p.x == fromX && p.y == fromY && !p.down && !p.left && !p.up && !p.right) {
			return 0;
		}
		
		//����˵����ܶ��߹�����·���������һ��
		if(!(p.down||p.up||p.left||p.right)) {
			backout(breakColor);
			return 2;
		}
		
		again = true;
		
		String pxy = "";
		
		//������ ���� ���� ���ϵ�˳���ж��Ƿ���·����
		//���p����ҷ���Ϊtrue�������Ҳ���ǽ�ڣ�������û�б�Ϳ����ɫ�����if
		
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
				//����if�����ҵ�һ�����list������������ֵΪfalse���������
				list.add(new Point(newX,newY,emm[1],emm[3],emm[0],emm[2],p.count+1));
				
				//��Ϊ����ִ�У���rectangle���Ա���ֵ����Ϊֱ��Ѱ��·����rectangleû�и�ֵ
				//����Ѱ·���������ô˷����ʽ����ж�
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
	
	//�жϴ�λ���Ƿ��Ѿ��߹����Ѿ���Ϳ����ɫ
	public boolean have(int x, int y) {
		for(int i = 0; i < list.size(); i++) {
			Point p = list.get(i);
			if(p.x == x && p.y == y) {
				return true;
			}
		}
		return false;
	}
	
	
	//���ˣ���list�����һ��Ԫ��ɾ��
	public void backout(Color color){//������ʾ���˴�Ϳ��ʲô��ɫ
		//ɾ������ȡ���һ����
		Point lastp = list.remove(list.size()-1);
		
		//����ǵ���Ѱ·���˻صĸ�������Ϳ�ɻ�ɫ
		if(rectangle != null)
			rectangle[lastp.x][lastp.y].setFill(color);
		if(textArea != null) {
			String text = textArea.getText();
			String texts[] = text.split("\n");
			String str = text.substring(texts[0].length()+1, text.length());
			textArea.setText(str);
		}
		
		//��ȡ�µ����һ����
		Point p = list.get(list.size()-1);
		//�жϱ�ɾ���ĵ��Ǵ˵���Ǹ����򣬲����˷���ֵΪfalse�������ظ��ж�
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
	//ÿ������Ϊ����е�һ�����ε�λ��
}

enum Direction{
	UP,DOWN,LEFT,RIGHT
}

class Point{
	int x;
	int y;
	
	//�ĸ������Ƿ����
	boolean up = true;
	boolean down = true;
	boolean left = true;
	boolean right = true;
	
	int count = 0;
	//��һ�������ĸ�������
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
