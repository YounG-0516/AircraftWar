package edu.hitsz.application.game;

import edu.hitsz.factory.EliteEnemyFactory;
import edu.hitsz.factory.MobEnemyFactory;

public class EasyGame extends Game{

    private boolean canEnemyShootFlag = false;

    public EasyGame(){
        cycleDuration = 600;
        enemyMaxNumber = 5;
        probability = 0.3;
        System.out.println("简单模式不随时间改变难度!");
        System.out.println("简单模式精英敌机射击频率降低!");
        System.out.println("简单模式不产生BOSS机!");
        System.out.println("简单模式不更换背景!");
    }

    @Override
    protected void addEnemy() {
        if (enemyAircrafts.size() < enemyMaxNumber){
            if(Math.random() <= probability){
                factory = new EliteEnemyFactory();
                enemyAircrafts.add(factory.createAircraft(0,8,40));
            }else{
                factory = new MobEnemyFactory();
                enemyAircrafts.add(factory.createAircraft(0,6,20));
            }
        }
    }

    @Override
    protected void upDateVersion() {}

    @Override
    protected void increaseDifficulty() {}

    @Override
    protected void generateBoss() {}

    @Override
    protected boolean canEnemyShoot() {
        return canEnemyShootFlag = !canEnemyShootFlag;
    }

}
