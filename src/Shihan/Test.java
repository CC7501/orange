package Shihan;

import java.util.ArrayList;
import java.util.Random;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.util.Duration;

public class Test extends Application{
	Rectangle[][] r=new Rectangle[17][17];
	int[][] b=new int[17][17];
	Random R=new Random();
	ArrayList<Integer> listx =new ArrayList<>();
	ArrayList<Integer> listy =new ArrayList<>();
	Button bt1=new Button("单步寻路");
	Button btcreat=new Button("生成迷宫");
	Button bt2=new Button("最短路径");
	Button bt3=new Button("遍历迷宫");
	int k=0;
	int h=0;
	Animation animation = null;
	
	public void start(Stage primaryStage){				
			GridPane pane=new GridPane();
			pane.add(btcreat, 30, 0);		
			pane.add(bt1, 30, 1);			
			pane.add(bt2, 30, 2);		
			pane.add(bt3, 30, 3);
			for(int i=0;i<17;i++){//迷宫初始化
				for(int j=0;j<17;j++){
					r[i][j]=new Rectangle(i,j,30,30);
					r[i][j].setFill(Color.GRAY);
					r[i][j].setStroke(Color.WHITE);
					pane.add(r[i][j],i,j);
				}
			}		
			btcreat.setOnAction(e->{
				//r[15][15].setFill(Color.GRAY);
				for(int i=0;i<17;i++){
					for(int j=0;j<17;j++){
						r[i][j]=new Rectangle(i,j,30,30);
						r[i][j].setFill(Color.GRAY);
						r[i][j].setStroke(Color.WHITE);
						b[i][j]=0;
						pane.add(r[i][j],i,j);
					}
				}
				
				//画出四周墙壁
				for(int i=0;i<17;i++){		
					b[i][16]=1;
					b[i][0]=1;
					for(int j=0;j<17;j++){			
						b[0][j]=1;
						b[16][j]=1;
					}
				}
				getMaze(1,1,15,15);//调用函数，随机分割
				for(int i=0;i<17;i++){
					for(int j=0;j<17;j++){
						if(b[i][j]==1){
							r[i][j].setFill(Color.BLACK);
						}
					}
				}
				step(1,1);
			});						
			bt1.setOnAction(e->{//单步走迷宫寻路
				if(k!=listx.size()-1){
					r[listx.get(k++)][listy.get(h++)].setFill(Color.RED);
					if(k-2>=0)
						r[listx.get(k-2)][listy.get(h-2)].setFill(Color.GRAY);
				}
				else{
					r[listx.get(k)][listy.get(h)].setFill(Color.RED);//当走到终点时不再往下
					r[listx.get(k-1)][listy.get(h-1)].setFill(Color.GRAY);
				}
			});
			bt2.setOnAction(e->{				
				int[][] direction = {{0,1},{0,-1},{1,0},{-1,0}}; 
				int[] x = {1};
				int[] y = {1};
				r[x[0]][y[0]].setFill(Color.BLUE);
				animation = new Timeline(new KeyFrame(Duration.millis(100),event->{
					int i = 0;
					for(i = 0; i < 4; i++) {
						if(b[x[0]+direction[i][0]][y[0]+direction[i][1]] == 3) {
							b[x[0]][y[0]] = 2;
							x[0]+=direction[i][0];
							y[0]+=direction[i][1];
							r[x[0]][y[0]].setFill(Color.BLUE);
							break;
						}
					}
					if(i == 4) {
						animation.stop();
					}
				}));
				animation.setCycleCount(Timeline.INDEFINITE);
				animation.play();
			});
		
			Scene scene=new Scene(pane,700,600);
			primaryStage.setTitle("迷宫演示");
			primaryStage.setScene(scene);
			primaryStage.show();
			
	}
		public static void main(String[] args) {
			Application.launch(args);
		}
		//墙上开洞
		private void open(int x1, int y1, int x2, int y2) {//墙的始末两点坐标
			int pos;
			if (x1 == x2){//竖墙
            			pos = y1 + R.nextInt((y2 - y1 )/2+ 1)*2;//在奇数位置开门           			
            			b[x1][pos]=0;
       			 }		
			else if (y1 == y2){//横墙
            			pos = x1 + R.nextInt((x2 - x1 )/2+ 1)*2;           			
            			b[pos][y1]=0;
        		} 		
			else {
            			System.out.println("wrong");
        		}		
	}
		class note {
			int x;
			int y;
			int s;	 
			note(int x, int y) {
				this.x = x;
				this.y = y;
			}
		}
private void getMaze(int x, int y, int height, int width) {
        int xPos, yPos;
        if (height <= 2 || width <= 2)
            return;
        //横着画线，在偶数位置画线
        xPos=x+R.nextInt(height/2)*2+1;
        for (int i = y; i < y + width; i++) {
            b[xPos][i] = 1;        
        }
        //竖着画一条线，在偶数位置画线
        yPos=y+R.nextInt(width/2)*2+1;
        for (int i = x; i < x + height; i++) {
        	b[i][yPos] =1;       	
        }
        //随机开三扇门，左侧墙壁为1，逆时针旋转
        int isClosed = R.nextInt(4) + 1;
        switch (isClosed) 
        {
        case 1:
            open(xPos + 1, yPos, x + height - 1, yPos);// 2
            open(xPos, yPos + 1, xPos, y + width - 1);// 3
            open(x, yPos, xPos - 1, yPos);// 4
            break;
        case 2:
            open(xPos, yPos + 1, xPos, y + width - 1);// 3
            open(x, yPos, xPos - 1, yPos);// 4
            open(xPos, y, xPos, yPos - 1);// 1
            break;
        case 3:
            open(x, yPos, xPos - 1, yPos);// 4
            open(xPos, y, xPos, yPos - 1);// 1
            open(xPos + 1, yPos, x + height - 1, yPos);// 2
            break;
        case 4:
            open(xPos, y, xPos, yPos - 1);// 1
            open(xPos + 1, yPos, x + height - 1, yPos);// 2
            open(xPos, yPos + 1, xPos, y + width - 1);// 3
            break;
        default:
            break;
        }        
        getMaze(x, y, xPos - x, yPos - y);// 左上角       
        getMaze(x, yPos + 1, xPos - x, width - yPos + y - 1); // 右上角      
        getMaze(xPos + 1, y, height - xPos + x - 1, yPos - y); // 左下角      
        getMaze(xPos + 1, yPos + 1, height - xPos + x - 1, width - yPos + y - 1);// 右下角

    }
	
	boolean judge(int x,int y){//判断下一步是否可走
		if(x>0&&x<16&&y>0&&y<16&&b[x][y]==0)
			return true;
		else
			return false;
	}
	
	boolean step(int x,int y){
		boolean	pass=false;		
		if(judge(x,y)){
			b[x][y]=2;//走过的标记为2
			//r[x][y].setFill(Color.RED);
			listx.add(x);
			listy.add(y);
			if(x==15&&y==15){
				pass=true;
			}
			else{
				pass=step(x,y+1);//go right
				if(!pass)
					pass=step(x+1,y);//go down
				if(!pass)
					pass=step(x,y-1);//go left
				if(!pass)
					pass=step(x-1,y);//go up				
			}
			if(pass){
				b[x][y]=3;
				//r[x][y].setFill(Color.RED);
			}
		}
		return pass;
	}
	/*void move(int i, int j) {
        if (i == 17 - 2 && j == 17 - 2) {
           return;
        }
        if (canmove(i, j, i, j+1)) {
            b[i][j] = 8;
            r[i][j+1].setFill(Color.RED);
            move(i, j + 1);
            b[i][j] = 0;
        }

        if (canmove(i, j, i + 1, j)) {
            b[i][j] = 8;
            r[i+1][j].setFill(Color.RED);
            move(i + 1, j);
            b[i][j] = 0;
        }

        if (canmove(i, j, i, j - 1)) {
        	b[i][j] = 8;
            r[i][j-1].setFill(Color.RED);
            move(i, j - 1);
            b[i][j] = 0;
        }

        if (canmove(i, j, i - 1, j+1)) {
            b[i][j] = 8;
            r[i-1][j].setFill(Color.RED);
            move(i - 1, j);
            b[i][j] = 0;
        }
    }

	boolean canmove(int i,int j,int targetX,int targetY){
	        // 碰到左边
	        if (targetX < 0 || targetY < 0) {
	            return false;
	        }
	        // 碰到右边
	        if (targetX>=17 || targetY >=17) {
	            return false;
	        }
	        // 不能走
	        if (b[targetX][targetY] == 1 || b[targetX][targetY] == 8) {
	            return false;
	        }
	        return true;
	    }*/
}
