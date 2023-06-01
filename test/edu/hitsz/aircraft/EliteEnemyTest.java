package edu.hitsz.aircraft;

import edu.hitsz.application.Main;
import edu.hitsz.bullet.BaseBullet;
import edu.hitsz.factory.AircraftFactory;
import edu.hitsz.factory.EliteEnemyFactory;
import edu.hitsz.prop.AbstractProp;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.LinkedList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assumptions.assumeFalse;

class EliteEnemyTest {

    static AircraftFactory eliteEnemyFactory;
    private AbstractAircraft eliteEnemy;

    @BeforeAll
    static void setUp() {
        //enemyFactory = new AircraftFactory();无法实例化
        eliteEnemyFactory = new EliteEnemyFactory();
    }

    @BeforeEach
    void init() {
        eliteEnemy = eliteEnemyFactory.createAircraft(0,5,50);
    }

    @ParameterizedTest
    @DisplayName("Test EliteEnemy forward method")
    @ValueSource(ints =  {5,10,20})
    void forward(int steps) {
        //获取当前位置
        int beforeY = eliteEnemy.getLocationY();
        int beforeX = eliteEnemy.getLocationX();

        //移动:50
        for (int i = 0; i < steps; i++) {
            eliteEnemy.forward();
        }

        //获取移动后位置
        int afterY = eliteEnemy.getLocationY();
        int afterX = eliteEnemy.getLocationX();

        //判断：X位置不变且不超出屏幕，Y位置移动steps
        assertTrue((beforeX == afterX) && (afterX>0) && (afterX< Main.WINDOW_WIDTH));
        assertEquals(beforeY + steps * 5, afterY);
    }

    @DisplayName("Test EliteEnemy shoot method")
    @Test
    void shoot() {
        List<BaseBullet> bullets = eliteEnemy.shoot();

        //检测子弹集合是否为空
        assertNotNull(bullets);

        //判断子弹产生数量
        assertTrue(bullets.size() == 1);

        //判断每颗子弹是否具有向下的速度
        for(BaseBullet bullet : bullets){
            assertTrue(bullet.getSpeedY() > 0);
        }
    }

    @DisplayName("Test EliteEnemy dropProp method")
    @Test
    void dropProps() {
        List<AbstractProp> resprops = eliteEnemy.dropProps();

        //如果道具集合不为空，执行后面的测试
        assumeFalse(resprops == null);

        //判断道具是否具有向下的速度
        for(AbstractProp resprop : resprops){
            assertTrue(resprop.getSpeedY() > 0);
        }

        //判断道具产生数量
        assertTrue(resprops.size() == 1);
    }

}