package maze;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

public class Traversal extends Search{
	
	public Traversal(int [][]maze,int size) {
		super(maze,1,1,size-1,size-1);
		
	}
	public int[][] search(Rectangle[][] rectangle) {
		rectangle[fromX][fromY].setFill(Color.color(Math.random()/4+0.75, 0.6, 0.8));
		
		stack.push(new int[] {fromX,fromY,2});
		canGo[fromX][fromY] = false;
		maze[fromX][fromY] = 2;
		animation = new Timeline(new KeyFrame(Duration.millis(100),e->{
			int[][] point = new int[stack.size()][3];
			if(stack.size() == 0) {
				MazePane.text.setText("±éÀúÍê³É£¡");
				animation.stop();
			}
			int m = stack.size();
			for(int n = 0; n < m; n++) {
				int[] p = stack.pop();
				point[n][0] = p[0];
				point[n][1] = p[1];
				point[n][2] = p[2];
			}
			for(int n = 0; n < point.length; n++) {
				int x = point[n][0];
				int y = point[n][1];
				int[][] direction = {{1,0},{-1,0},{0,1},{0,-1}};
				for(int i = 0; i < 4; i++) {
					int newX = x+direction[i][0];
					int newY = y+direction[i][1];
					if(canGo[newX][newY]) {
						canGo[newX][newY] = false;
						stack.push(new int[] {newX,newY,point[n][2]+1});
						rectangle[newX][newY].setFill(Color.color(Math.random()/4+0.75, 0.6, 0.8));
						maze[newX][newY] = point[n][2]+1;
					}
				}
			}
		})) ;
		animation.setCycleCount(Timeline.INDEFINITE);
		animation.play();
		return maze;

	}
}