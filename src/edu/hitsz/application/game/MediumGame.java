package edu.hitsz.application.game;

import edu.hitsz.application.ImageManager;
import edu.hitsz.application.music.MusicManager;
import edu.hitsz.factory.BossEnemyFactory;
import edu.hitsz.factory.EliteEnemyFactory;
import edu.hitsz.factory.MobEnemyFactory;
import edu.hitsz.strategy.UpdateStraightShootStrategy;

import javax.imageio.ImageIO;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.DecimalFormat;

public class MediumGame extends Game{

    public static boolean isUpdata = false;
    private int MOB_ENEMY_HP = 20;
    private int ELITE_ENEMY_HP = 50;
    private double property = 1;

    public MediumGame(){
        cycleDuration = 480;
        enemyMaxNumber = 6;
        probability = 0.5;
        threshold = 500;
        cycleDurationForIncreaseDifficulty = 9000;
        BOSS_ENEMY_HP = 300;
        System.out.println("普通模式随时间改变难度!");
        System.out.println("普通模式英雄机及敌机射击频率正常!");
        System.out.println("普通模式产生BOSS机!");
        System.out.println("普通模式达到阈值分数后更换背景，并升级BOSS机及英雄机!");
    }

    @Override
    protected void increaseDifficulty() {

        DecimalFormat df = new DecimalFormat( "0.00");

        //控制难度提升上限
        if(probability <0.75){
            probability +=0.01;
        }

        if(property <2.0){
            property +=0.02;
        }

        if(cycleDuration>350){
            setCycleDuration(cycleDuration-10);
        }

        if(probability <=0.75 || property <=2.0 || cycleDuration>=350){
            System.out.println("提升难度！精英机出现概率为"+df.format(probability)+",敌机周期提升倍率"+df.format(1+((480-cycleDuration)/480.0))+"，敌机属性提升倍率为"+df.format(property));
        }else{
            System.out.println("难度已达上限!");
        }

    }

    @Override
    protected void generateBoss() {
        //超过400分才会产生敌机
        if(Game.score >= threshold){
            if(!isBossExist()){
                factory = new BossEnemyFactory();
                enemyAircrafts.add(factory.createAircraft(5,0, BOSS_ENEMY_HP));
                MusicManager.playBossBgm();
                System.out.println("产生boss敌机!普通模式BOSS机血量不随出现次数提升！");
            }
            threshold = threshold + 500;
        }
    }

    @Override
    protected void addEnemy() {
        if (enemyAircrafts.size() < enemyMaxNumber) {
            if (Math.random() <= probability) {
                factory = new EliteEnemyFactory();
                enemyAircrafts.add(factory.createAircraft(0, property * 10, (int) (property * ELITE_ENEMY_HP)));
            } else {
                factory = new MobEnemyFactory();
                enemyAircrafts.add(factory.createAircraft(0, property * 8, (int) (property * MOB_ENEMY_HP)));
            }
        }
    }

    @Override
    protected void upDateVersion() {

        if(Game.score>=1000 && !isUpdata){
            isUpdata = true;

            //分数达到1000分后切换背景
            try {
                ImageManager.BACKGROUND_IMAGE = ImageIO.read(new FileInputStream("src/images/bg8.jpg"));
                ImageManager.HERO_IMAGE = ImageIO.read(new FileInputStream("src/images/hero_update.png"));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            new Thread(()->{
                isChange = true;
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                isChange = false;
            }).start();

            //BOSS机血量增加
            ELITE_ENEMY_HP += 30;
            BOSS_ENEMY_HP += 500;

            //英雄机火力增强
            heroAircraft.setStrategy(new UpdateStraightShootStrategy());


        }
    }
}
