package maze;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class DrawMaze{
	private int size = 20;
	private Rectangle[][] rectangle;
	
	public DrawMaze(int size,Rectangle[][] r) {
		this.size = size;
		this.rectangle = r;
	}
	
	
	public void drawMaze() { 
		for(int i = 1; i < size; i++) {
			for(int j = 1; j < size; j++) {
				rectangle[i][j].setFill(Color.GAINSBORO);//����������о���Ϳ�ɻ�ɫ
			}
		}
		for(int i = 1; i < size; i++) {
			for(int j = 1; j < size; j++) {
				Rectangle r = rectangle[i][j];
				//Ϊÿ���������ö����������ɫ
				r.setOnMousePressed(e->{
					if(r.getFill()!=Color.GAINSBORO)//���������ɫΪ��ɫ�����ڵ��ʱͿ�ɺ�ɫ��
						r.setFill(Color.GAINSBORO);
					else
						r.setFill(Color.color(0.2, 0.4, Math.random()/5+0.8));//���򽫾���Ϳ�ɻ�ɫ
				});
			}
		}
	}
}
