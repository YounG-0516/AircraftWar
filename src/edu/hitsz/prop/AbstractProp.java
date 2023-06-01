package edu.hitsz.prop;

import edu.hitsz.aircraft.AbstractAircraft;
import edu.hitsz.application.Main;
import edu.hitsz.basic.AbstractFlyingObject;
import edu.hitsz.bullet.BaseBullet;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

/**
 * 所有道具的抽象父类
 *
 * @author hitsz
 */

public abstract class AbstractProp extends AbstractFlyingObject {

    public AbstractProp(int locationX, int locationY, double speedX, double speedY){
        super(locationX, locationY, speedX, speedY);
    }

    @Override
    public void forward() {
        super.forward();

        // 判定 y 轴出界
        if (speedY > 0 && locationY >= Main.WINDOW_HEIGHT ) {
            // 向下飞行出界
            vanish();
        }else if (locationY <= 0){
            // 向上飞行出界
            vanish();
        }
    }

    public abstract void effect(AbstractAircraft aircraft);

}
