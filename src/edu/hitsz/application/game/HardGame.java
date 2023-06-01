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

public class HardGame extends Game{

    public static boolean isUpdata = false;
    private int MOB_ENEMY_HP = 30;
    private int ELITE_ENEMY_HP = 60;
    private double property = 1;
    private int BossNum=0;
    private int increaseThreshold=300;
    private boolean canHeroShootFlag = false;

    public HardGame(){
        cycleDuration = 400;
        enemyMaxNumber = 8;
        probability = 0.60;
        threshold = 300;
        cycleDurationForIncreaseDifficulty=6000;
        BOSS_ENEMY_HP = 500;
        System.out.println("困难模式随时间改变难度!");
        System.out.println("困难模式英雄机前期射击频率降低！敌机射击频率正常!");
        System.out.println("困难模式产生BOSS机!且每次产生的BOSS机血量增加100！");
        System.out.println("困难模式达到阈值分数后更换背景，并升级BOSS机及英雄机!");
    }



    @Override
    protected void increaseDifficulty() {

        DecimalFormat df = new DecimalFormat( "0.00");

        //控制难度提升上限
        if(probability <0.80){
            probability +=0.01;
        }

        if(property <2.0){
            property +=0.02;
        }
        if(cycleDuration>=300){
            setCycleDuration(cycleDuration-10);
            System.out.println(cycleDuration);
        }

        if(probability <=0.8 || property <=2.0 || cycleDuration>=300){
            System.out.println("提升难度！精英机出现概率为"+df.format(probability)+",敌机周期提升倍率"+df.format(1+((400-cycleDuration)/400.0))+"，敌机属性提升倍率为"+df.format(property));
        }else{
            System.out.println("难度已达上限!");
        }

    }

    @Override
    protected void generateBoss() {

        if(Game.score >= threshold){
            if(!isBossExist()){
                factory = new BossEnemyFactory();
                BOSS_ENEMY_HP = (BossNum==0)?BOSS_ENEMY_HP:BOSS_ENEMY_HP+100;
                enemyAircrafts.add(factory.createAircraft(7,0,BOSS_ENEMY_HP));
                MusicManager.playBossBgm();
                BossNum++;
                System.out.println("第"+BossNum+"次产生boss敌机!当前总血量："+BOSS_ENEMY_HP);
            }
            threshold = threshold + increaseThreshold;
        }
    }

    @Override
    protected void addEnemy() {
        if (enemyAircrafts.size() < enemyMaxNumber) {
            if (Math.random() <= probability) {
                factory = new EliteEnemyFactory();
                enemyAircrafts.add(factory.createAircraft(0, property * 12, (int) (property * ELITE_ENEMY_HP)));
            } else {
                factory = new MobEnemyFactory();
                enemyAircrafts.add(factory.createAircraft(0, property * 10, (int) (property * MOB_ENEMY_HP)));
            }
        }
    }

    @Override
    protected void upDateVersion() {

        if(Game.score>=1200 && !isUpdata){
            isUpdata = true;
            System.out.println("游戏升级！");

            //分数达到1200分后切换背景及飞机
            try {
                ImageManager.HERO_IMAGE = ImageIO.read(new FileInputStream("src/images/hero_update.png"));
                ImageManager.BACKGROUND_IMAGE = ImageIO.read(new FileInputStream("src/images/bg5.jpg"));
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

            //BOSS机出现间隔减少
            ELITE_ENEMY_HP += 70;
            increaseThreshold=200;

            //英雄机火力增强
            heroAircraft.setStrategy(new UpdateStraightShootStrategy());
        }
    }

    public boolean canHeroShoot() {
        if(Game.score<=300){
            return canHeroShootFlag = !canHeroShootFlag;
        }else{
            return true;
        }
    }

}
