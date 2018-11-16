package maze;

import java.util.ArrayList;
import java.util.Stack;
import javafx.animation.Animation;
import javafx.scene.shape.Rectangle;

public class Search {
	Stack<int[]> stack = new Stack<>();//每个int[]保存一个点的坐标，stack保存int[]
	ArrayList<int[][]> path = new ArrayList<>();//每个int[][]保存一条路径，path保存所有路径
	Animation animation;//用来动态显示
	Rectangle[][] rectangle;
	int[][] maze;
	boolean[][] canGo;//表示是否走过，走过则不可走
	int fromX = 1;
	int fromY = 1;
	int endX = 1;
	int endY = 1;
	
	public Search(int[][] maze){
		//避免影响原始maze，重新生成maze数组
		this.maze = new int[maze.length][maze[0].length];		
		canGo = new boolean[maze.length][maze[0].length];
		for(int i = 0; i < maze.length; i++) {
			for(int j = 0; j < maze[i].length; j++) {
				canGo[i][j] = maze[i][j] == 1 ? false : true;
				this.maze[i][j] = maze[i][j];
			}
		}
	}
	public Search(int[][] maze,int fromX,int fromY,int endX,int endY){
		this(maze);
		this.fromX = fromX;
		this.fromY = fromY;
		this.endX = endX;
		this.endY = endY;
		//添加起点，四个方向均为true表示可走
	}
	
	
//	通过广度遍历寻找最短路径。
//	每个路径都同时往前走一步，最先到达终点的即为最短路径
	public int[][] searchShortPath() {
		stack.push(new int[] {fromX,fromY,0});
		canGo[fromX][fromY] = false;
		maze[fromX][fromY] = 2;
		int[] rp = {0};//作为参数传给shortPath,返回终点前一个元素的在stack中的位置
		shortPath(1,rp);
		
		int[] point = stack.get(rp[0]);
		for(;;) {
			maze[point[0]][point[1]] = 2;
			int n = point[2];
			if(n == 0)
				break;
			point = stack.get(n);
		}
//		maze[endX][endY] = 2;
		return maze;
	}
	
	
	private void shortPath(int n , int[] rp) {
		int[][] point = new int[n][3];//[x坐标, y坐标, 前一个元素在stack中的位置]
		//将栈内最后n个元素取出
		for(int i = 0; i < point.length; i++) {
			point[i] = stack.get(stack.size()-i-1);
			if(point[i][0] == endX && point[i][1] == endY) {
				//如果到达终点则将终点位置点存储的上一个点位置取出以便依次寻找路径上所有点
				rp[0] = stack.indexOf(point[i]);
				return;
			}	
		}
		
		int count = 0;
		//原坐标加上这四个点坐标即为四个方向
		int[][] direction = {{1,0},{0,1},{-1,0},{0,-1}};
		for(int j = 0; j < point.length; j++) {
			int x = point[j][0];
			int y = point[j][1];
			//遍历四个方向
			for(int i = 0; i < 4; i++) {
				int newX = x+direction[i][0];
				int newY = y+direction[i][1];
				if(canGo[newX][newY]) {
					count++;
					//将可走的赋为false以免重复走
					canGo[newX][newY] = false;
					//将可走的方向放入栈
					stack.push(new int[] {newX,newY,stack.indexOf(point[j])});
				}
			}
		}
		//如果四个方向有一个以上可以走则继续递归
		if(count != 0) {
			shortPath(count,rp);
		}
		return;
	}
	
	public ArrayList<int[][]> searchAllPath() {
		stack.push(new int[] {fromX,fromY});
		canGo[fromX][fromY] = false;
		maze[fromX][fromY] = 2;
		allPath();
		return path;
	}
	
	private boolean allPath() {
		int[] point = stack.pop();
		int x = point[0];
		int y = point[1];
		if(x == endX && y == endY) {
			int[][] maze2 = new int[maze.length][maze.length];
			for(int i = 0; i < canGo.length; i++) {
				for(int j = 0; j < canGo.length; j++) {
					//将找到的路径保存下来
					maze2[i][j] = maze[i][j];
					if(maze[i][j] == 0)
						//将除了此路以外的地方赋为true，以便可以寻找其他路径
						canGo[i][j] = true;
				}
			}
			path.add(maze2);
			canGo[x][y] = true;
			return true;
		}
		
		boolean r = false;
		
		int[][] direction = {{1,0},{0,1},{-1,0},{0,-1}};
		for(int i = 0; i < 4; i++) {
			int newX = x+direction[i][0];
			int newY = y+direction[i][1];
			if(canGo[newX][newY]) {
				maze[newX][newY] = 2;
				canGo[newX][newY] = false;
				stack.push(new int[] {newX,newY});
				if(allPath()) {
					r = true;
				}
				canGo[newX][newY] = true;
				maze[newX][newY] = 0;
			}
		}
		return r;
	}
}


