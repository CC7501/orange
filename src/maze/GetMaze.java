package maze;

public class GetMaze {
	private int[][] maze;
	public GetMaze(int[][] maze){
		this.maze = maze;
	}
	public void getMaze(int x, int y, int high, int wide) {
		//如果宽度高度小于2则结束递归
		if(high <= 2 || wide <= 2) {
			return;
		}
		
		//在面板中任取两条偶数点上的线，作为两道墙，
		int PosX = x+(int)(Math.random()*((high)/2.0)-1)*2+2;
		int PosY = y+(int)(Math.random()*((wide)/2.0)-1)*2+2;

		//将两道墙填充
		for(int i = y+1; i <= y+wide; i++) {
			maze[PosX][i] = 1;
		}
		
		for(int i = x+1; i <= x+high; i++) {
			maze[i][PosY] = 1;
		}
		
		//随机生成一个数，确定在哪三个方向上开门
		int n = (int)(Math.random()*4);
		
		switch(n) {
		//调用door方法在任意三堵墙上开洞
		case 0:
			door(PosX,PosY,PosX,wide+y+1);//2
			door(x,PosY,PosX,PosY);//3
			door(PosX,PosY,x+high+1,PosY);//4
			break;
		case 1:
			door(PosX,y,PosX,PosY);//1
			door(x,PosY,PosX,PosY);//3
			door(PosX,PosY,x+high+1,PosY);//4
			break;
		case 2:
			door(PosX,y,PosX,PosY);//1
			door(PosX,PosY,PosX,wide+y+1);//2
			door(PosX,PosY,x+high+1,PosY);//4
			break;
		case 3:
			door(PosX,y,PosX,PosY);//1
			door(PosX,PosY,PosX,wide+y+1);//2
			door(x,PosY,PosX,PosY);//3
			break;
		}
		
		//将两堵墙分隔的四块区域作为新的待生成的迷宫区域进行递归
		getMaze(x, y, PosX-x-1, PosY-y-1);
		getMaze(PosX, y, high-PosX+x, PosY-y-1);
		getMaze(x, PosY, PosX-x-1, wide-PosY+y);
		getMaze(PosX, PosY, high-PosX+x, wide-PosY+y);
		
	}
	
	//在墙上开洞
	private void door(int x1, int y1, int x2, int y2) {
		//如果两堵墙的距离小于1则退出
		if(x2-x1 <= 1&&y2-y1 <= 1) {
			return;
		}
		
		int x = 0;
		int y = 0;
		//如果x1 == x2 就表示此直线为横着的，随机选取y在上面打洞
		if(x1 == x2) {
			//保证是奇数，与后面墙的位置错开，避免后面生成的墙将门的位置堵住
			y = (int)(Math.random()*((y2-y1-1)/2.0))*2+1;
			x = x1;
			y = y + y1;
		}
		//否则为竖直的，随机选取x在上面打洞
		else if(y1 == y2) {
			x = (int)(Math.random()*((x2-x1-1)/2.0))*2+1;
			x = x + x1;
			y = y1;
		}
		
		//将最后选取的点的位置赋为0，即通路
		maze[x][y] = 0;
	}
}

