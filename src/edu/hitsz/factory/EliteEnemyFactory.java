package edu.hitsz.factory;

import edu.hitsz.aircraft.AbstractAircraft;
import edu.hitsz.aircraft.EliteEnemy;
import edu.hitsz.application.ImageManager;
import edu.hitsz.application.Main;
import edu.hitsz.strategy.StraightShootStrategy;

public class EliteEnemyFactory implements AircraftFactory{
    @Override
    public AbstractAircraft createAircraft(double speedX, double speedY, int hp) {
        AbstractAircraft eliteEnemy = new EliteEnemy(
                (int) (Math.random() * (Main.WINDOW_WIDTH - ImageManager.ELITE_IMAGE.getWidth())),
                (int) (Math.random() * Main.WINDOW_HEIGHT * 0.05),
                speedX,
                speedY,
                hp
        );
        eliteEnemy.setStrategy(new StraightShootStrategy());
        return eliteEnemy;
    }
}
