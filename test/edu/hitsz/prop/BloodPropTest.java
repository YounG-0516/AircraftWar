package edu.hitsz.prop;

import edu.hitsz.aircraft.HeroAircraft;
import edu.hitsz.application.Main;
import edu.hitsz.factory.BloodPropFactory;
import edu.hitsz.factory.PropFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

class BloodPropTest {

    static PropFactory bloodPropFactory;
    static HeroAircraft heroAircraft;
    private AbstractProp bloodProp;

    @BeforeAll
    static void setUp() {
        bloodPropFactory = new BloodPropFactory();
        heroAircraft = HeroAircraft.getHeroAircraft();
    }

    @BeforeEach
    void init() {
        bloodProp = bloodPropFactory.createProp(100,100);
    }

    @DisplayName("Test BloodProp vanish method")
    @Test
    void vanish() {
        bloodProp.vanish();
        assertTrue(bloodProp.notValid());
    }

    @ParameterizedTest
    @DisplayName("Test BloodProp effect method")
    @ValueSource(ints =  {0,10,100,999})
    void effect(int blood) {
        heroAircraft.decreaseHp(blood);

        //获取当前血量
        int beforeHp = heroAircraft.getHp();
        bloodProp.effect(heroAircraft);

        //获取加血后的血量
        int afterHp = heroAircraft.getHp();
        //System.out.println(Math.min(beforeHp+20, heroAircraft.getMaxHp()));
        assertEquals(Math.min(beforeHp+20, heroAircraft.getMaxHp()), afterHp);
    }

    /**
    @ParameterizedTest
    @DisplayName("Test BloodProp forward method")
    @ValueSource(ints =  {1,5,15,25})
    void forward(int steps) {
        //获取当前位置
        int beforeY = bloodProp.getLocationY();
        int beforeX = bloodProp.getLocationX();

        //移动:50
        for (int i = 0; i < steps; i++) {
            bloodProp.forward();
        }

        //获取移动后位置
        int afterY = bloodProp.getLocationY();
        int afterX = bloodProp.getLocationX();

        //判断：X位置不变且不超出屏幕，Y位置移动100
        assertTrue((beforeX == afterX) && (afterX>0) && (afterX< Main.WINDOW_WIDTH));
        assertEquals(beforeY + steps*5, afterY);
    }
    */

}