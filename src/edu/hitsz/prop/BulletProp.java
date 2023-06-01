package edu.hitsz.prop;

import edu.hitsz.aircraft.AbstractAircraft;
import edu.hitsz.application.game.HardGame;
import edu.hitsz.application.game.MediumGame;
import edu.hitsz.application.music.MusicManager;
import edu.hitsz.strategy.ScatterShootStrategy;
import edu.hitsz.strategy.StraightShootStrategy;
import edu.hitsz.strategy.UpdateStraightShootStrategy;

public class BulletProp extends AbstractProp{

    private boolean effectCalled = false;
    public BulletProp(int locationX, int locationY, double speedX, double speedY){
        super(locationX, locationY, speedX, speedY);
    }

    @Override
    public synchronized void effect(AbstractAircraft aircraft){
        MusicManager.playBullet();
        System.out.println("BulletSupply active!");
        if(!effectCalled){
            effectCalled = true;
            //定义新线程
            Runnable r = ()->{
                aircraft.setStrategy(new ScatterShootStrategy());

                //System.out.println("Bullet change!");
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                if(MediumGame.isUpdata | HardGame.isUpdata){
                    aircraft.setStrategy(new UpdateStraightShootStrategy());
                }else{
                    aircraft.setStrategy(new StraightShootStrategy());
                }

            };

            //启动新线程
            new Thread(r).start();
        }
    }
}
