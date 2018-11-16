package maze;

import java.util.ArrayList;
import java.util.Stack;
import javafx.animation.Animation;
import javafx.scene.shape.Rectangle;

public class Search {
	Stack<int[]> stack = new Stack<>();//ÿ��int[]����һ��������꣬stack����int[]
	ArrayList<int[][]> path = new ArrayList<>();//ÿ��int[][]����һ��·����path��������·��
	Animation animation;//������̬��ʾ
	Rectangle[][] rectangle;
	int[][] maze;
	boolean[][] canGo;//��ʾ�Ƿ��߹����߹��򲻿���
	int fromX = 1;
	int fromY = 1;
	int endX = 1;
	int endY = 1;
	
	public Search(int[][] maze){
		//����Ӱ��ԭʼmaze����������maze����
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
		//�����㣬�ĸ������Ϊtrue��ʾ����
	}
	
	
//	ͨ����ȱ���Ѱ�����·����
//	ÿ��·����ͬʱ��ǰ��һ�������ȵ����յ�ļ�Ϊ���·��
	public int[][] searchShortPath() {
		stack.push(new int[] {fromX,fromY,0});
		canGo[fromX][fromY] = false;
		maze[fromX][fromY] = 2;
		int[] rp = {0};//��Ϊ��������shortPath,�����յ�ǰһ��Ԫ�ص���stack�е�λ��
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
		int[][] point = new int[n][3];//[x����, y����, ǰһ��Ԫ����stack�е�λ��]
		//��ջ�����n��Ԫ��ȡ��
		for(int i = 0; i < point.length; i++) {
			point[i] = stack.get(stack.size()-i-1);
			if(point[i][0] == endX && point[i][1] == endY) {
				//��������յ����յ�λ�õ�洢����һ����λ��ȡ���Ա�����Ѱ��·�������е�
				rp[0] = stack.indexOf(point[i]);
				return;
			}	
		}
		
		int count = 0;
		//ԭ����������ĸ������꼴Ϊ�ĸ�����
		int[][] direction = {{1,0},{0,1},{-1,0},{0,-1}};
		for(int j = 0; j < point.length; j++) {
			int x = point[j][0];
			int y = point[j][1];
			//�����ĸ�����
			for(int i = 0; i < 4; i++) {
				int newX = x+direction[i][0];
				int newY = y+direction[i][1];
				if(canGo[newX][newY]) {
					count++;
					//�����ߵĸ�Ϊfalse�����ظ���
					canGo[newX][newY] = false;
					//�����ߵķ������ջ
					stack.push(new int[] {newX,newY,stack.indexOf(point[j])});
				}
			}
		}
		//����ĸ�������һ�����Ͽ�����������ݹ�
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
					//���ҵ���·����������
					maze2[i][j] = maze[i][j];
					if(maze[i][j] == 0)
						//�����˴�·����ĵط���Ϊtrue���Ա����Ѱ������·��
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


