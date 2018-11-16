package maze;

public class GetMaze {
	private int[][] maze;
	public GetMaze(int[][] maze){
		this.maze = maze;
	}
	public void getMaze(int x, int y, int high, int wide) {
		//�����ȸ߶�С��2������ݹ�
		if(high <= 2 || wide <= 2) {
			return;
		}
		
		//���������ȡ����ż�����ϵ��ߣ���Ϊ����ǽ��
		int PosX = x+(int)(Math.random()*((high)/2.0)-1)*2+2;
		int PosY = y+(int)(Math.random()*((wide)/2.0)-1)*2+2;

		//������ǽ���
		for(int i = y+1; i <= y+wide; i++) {
			maze[PosX][i] = 1;
		}
		
		for(int i = x+1; i <= x+high; i++) {
			maze[i][PosY] = 1;
		}
		
		//�������һ������ȷ���������������Ͽ���
		int n = (int)(Math.random()*4);
		
		switch(n) {
		//����door��������������ǽ�Ͽ���
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
		
		//������ǽ�ָ����Ŀ�������Ϊ�µĴ����ɵ��Թ�������еݹ�
		getMaze(x, y, PosX-x-1, PosY-y-1);
		getMaze(PosX, y, high-PosX+x, PosY-y-1);
		getMaze(x, PosY, PosX-x-1, wide-PosY+y);
		getMaze(PosX, PosY, high-PosX+x, wide-PosY+y);
		
	}
	
	//��ǽ�Ͽ���
	private void door(int x1, int y1, int x2, int y2) {
		//�������ǽ�ľ���С��1���˳�
		if(x2-x1 <= 1&&y2-y1 <= 1) {
			return;
		}
		
		int x = 0;
		int y = 0;
		//���x1 == x2 �ͱ�ʾ��ֱ��Ϊ���ŵģ����ѡȡy�������
		if(x1 == x2) {
			//��֤�������������ǽ��λ�ô�������������ɵ�ǽ���ŵ�λ�ö�ס
			y = (int)(Math.random()*((y2-y1-1)/2.0))*2+1;
			x = x1;
			y = y + y1;
		}
		//����Ϊ��ֱ�ģ����ѡȡx�������
		else if(y1 == y2) {
			x = (int)(Math.random()*((x2-x1-1)/2.0))*2+1;
			x = x + x1;
			y = y1;
		}
		
		//�����ѡȡ�ĵ��λ�ø�Ϊ0����ͨ·
		maze[x][y] = 0;
	}
}

