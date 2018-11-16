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
				rectangle[i][j].setFill(Color.GAINSBORO);//将面板中所有矩形涂成灰色
			}
		}
		for(int i = 1; i < size; i++) {
			for(int j = 1; j < size; j++) {
				Rectangle r = rectangle[i][j];
				//为每个矩形设置动作：点击变色
				r.setOnMousePressed(e->{
					if(r.getFill()!=Color.GAINSBORO)//如果矩形颜色为灰色，就在点击时涂成黑色，
						r.setFill(Color.GAINSBORO);
					else
						r.setFill(Color.color(0.2, 0.4, Math.random()/5+0.8));//否则将矩形涂成灰色
				});
			}
		}
	}
}
