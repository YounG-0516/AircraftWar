package edu.hitsz.factory;

import edu.hitsz.aircraft.AbstractAircraft;
import edu.hitsz.aircraft.MobEnemy;
import edu.hitsz.application.ImageManager;
import edu.hitsz.application.Main;
import edu.hitsz.strategy.NoneShootStrategy;

public class MobEnemyFactory implements AircraftFactory{
    @Override
    public AbstractAircraft createAircraft(double speedX, double speedY, int hp) {
        AbstractAircraft mobEnemy = new MobEnemy(
                (int) (Math.random() * (Main.WINDOW_WIDTH - ImageManager.MOB_ENEMY_IMAGE.getWidth())),
                (int) (Math.random() * Main.WINDOW_HEIGHT * 0.05),
                speedX,
                speedY,
                hp
        );
        mobEnemy.setStrategy(new NoneShootStrategy());
        return mobEnemy;
    }
}
