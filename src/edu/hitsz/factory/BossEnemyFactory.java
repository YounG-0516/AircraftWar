package edu.hitsz.factory;

import edu.hitsz.aircraft.AbstractAircraft;
import edu.hitsz.aircraft.BossEnemy;
import edu.hitsz.aircraft.EliteEnemy;
import edu.hitsz.application.ImageManager;
import edu.hitsz.application.Main;
import edu.hitsz.strategy.ScatterShootStrategy;

public class BossEnemyFactory implements AircraftFactory{

    @Override
    public AbstractAircraft createAircraft(double speedX, double speedY, int hp) {
        int rand = Math.random()>0.5? 1: -1;
        AbstractAircraft bossEnemy =new BossEnemy(
                Main.WINDOW_WIDTH/2,
                (int) (Main.WINDOW_HEIGHT * 0.13),
                rand * speedX,
                speedY,
                hp
        );
        bossEnemy.setStrategy(new ScatterShootStrategy());
        return bossEnemy;
    }
}
