package edu.hitsz.aircraft;

import edu.hitsz.bullet.BaseBullet;
import edu.hitsz.bullet.EnemyBullet;
import edu.hitsz.bullet.HeroBullet;
import edu.hitsz.factory.AircraftFactory;
import edu.hitsz.factory.EliteEnemyFactory;
import edu.hitsz.factory.MobEnemyFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

class HeroAircraftTest {


    static HeroAircraft heroAircraft;
    static AircraftFactory mobEnemyFactory;
    AbstractAircraft mobEnemy;
    //BaseBullet enemyBullet;
    BaseBullet heroBullet;

    @BeforeAll
    static void setUp() {
        mobEnemyFactory = new MobEnemyFactory();
        heroAircraft = HeroAircraft.getHeroAircraft();
    }

    @BeforeEach
    void create() {
        //确定英雄机位置
        heroAircraft.setLocation(200,200);
        mobEnemy = mobEnemyFactory.createAircraft(0,10,20);
        //enemyBullet = new EnemyBullet(0,0,0,10,10);
        heroBullet = new HeroBullet(0,0,0,20,20);
    }

    @ParameterizedTest
    @DisplayName("Test HeroAircraft decreaseHp method")
    @ValueSource(ints = {10,100,1000,2000})
    void decreaseHp(int decrease) {
        //获取当前血量
        int beforeHp = heroAircraft.getHp();
        heroAircraft.decreaseHp(decrease);
        //获取受到子弹攻击后的血量
        int afterHp = heroAircraft.getHp();
        assertEquals(Math.max(beforeHp - decrease, 0), afterHp);
    }

    @ParameterizedTest
    @DisplayName("Test HeroAircraft crash method")
    @CsvSource({"200,200,200,200","190,210,190,210","210,190,210,190"})
    void crash(int aircraftX, int aircraftY, int hBulletX, int hBulletY) {
        /**
        //检测敌机子弹是否碰撞英雄机
        enemyBullet.setLocation(eBulletX,eBulletY);
        assertTrue(heroAircraft.crash(enemyBullet) || enemyBullet.crash(heroAircraft));
         */

        heroBullet.setLocation(hBulletX,hBulletY);
        mobEnemy.setLocation(aircraftX,aircraftY);

        //检测英雄机子弹是否碰撞敌机
        assertTrue(heroBullet.crash(mobEnemy));
        //检测英雄机是否碰撞敌机
        assertTrue(heroAircraft.crash(mobEnemy));
    }
}