package edu.hitsz.application.game;

import edu.hitsz.aircraft.*;
import edu.hitsz.application.HeroController;
import edu.hitsz.application.ImageManager;
import edu.hitsz.application.Main;
import edu.hitsz.application.frame.RankingList;
import edu.hitsz.application.music.MusicManager;
import edu.hitsz.application.music.Player;
import edu.hitsz.basic.Observer;
import edu.hitsz.bullet.BaseBullet;
import edu.hitsz.basic.AbstractFlyingObject;
import edu.hitsz.factory.*;
import edu.hitsz.prop.AbstractProp;
import edu.hitsz.prop.BombProp;
import org.apache.commons.lang3.concurrent.BasicThreadFactory;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.RGBImageFilter;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.*;
import java.util.List;
import java.util.concurrent.*;


/**
 * 游戏主面板，游戏启动
 *
 * @author hitsz
 */
public abstract class Game extends JPanel {

    private int backGroundTop = 0;

    /**
     * Scheduled 线程池，用于任务调度
     */
    private final ScheduledExecutorService executorService;

    /**
     * 时间间隔(ms)，控制刷新频率
     */
    protected int timeInterval = 40;

    //敌机工厂
    protected AircraftFactory factory;

    //游戏难度设置
    private static int gameDifficulty;

    protected final HeroAircraft heroAircraft;
    protected final List<AbstractAircraft> enemyAircrafts;
    protected final List<BaseBullet> heroBullets;
    protected final List<BaseBullet> enemyBullets;
    protected final List<AbstractProp> props;
    protected BossEnemy boss;

    protected int enemyMaxNumber;
    protected double probability;
    protected boolean isChange;
    protected int threshold;
    protected int cycleDuration;
    protected int BOSS_ENEMY_HP;

    /**
     * 当前得分
     */
    protected static int score = 0;

    /**
     * 当前时刻
     */
    private int time = 0;

    /**
     * 周期（ms)
     * 指示子弹的发射、敌机的产生频率
     */

    protected int cycleTime = 0;

    /**
     * 周期（ms)
     * 指示提升难度的频率
     */
    protected int cycleTimeForIncreaseDifficulty = 0;
    protected int cycleDurationForIncreaseDifficulty;

    /**
     * 游戏结束标志
     */
    private boolean gameOverFlag = false;

    public Game() {

        heroAircraft = HeroAircraft.getHeroAircraft();

        enemyAircrafts = new LinkedList<>();
        heroBullets = new LinkedList<>();
        enemyBullets = new LinkedList<>();
        props = new LinkedList<>();


        /**
         * Scheduled 线程池，用于定时任务调度
         * 关于alibaba code guide：可命名的 ThreadFactory 一般需要第三方包
         * apache 第三方库： org.apache.commons.lang3.concurrent.BasicThreadFactory
         */
        this.executorService = new ScheduledThreadPoolExecutor(1,
                new BasicThreadFactory.Builder().namingPattern("game-action-%d").daemon(true).build());

        //启动英雄机鼠标监听
        new HeroController(this, heroAircraft);

    }

    public static void setGameDifficulty(int gameDifficulty) {
        Game.gameDifficulty = gameDifficulty;
    }


    public static void setMusic(int musicSelect){
        if(musicSelect==0){
            Player.setStartMusic(true);
            System.out.println("音效开启");
        }else if(musicSelect==1){
            Player.setStartMusic(false);
            System.out.println("音效关闭");
        }
    }

    public void setCycleDuration(int cycleDuration){
        this.cycleDuration=cycleDuration;
    }


    /**
     * 增加游戏难度
     */
    protected abstract void increaseDifficulty();

    /**
     * 控制Boss敌机是否出现
     */
    protected abstract void generateBoss();


    protected boolean isBossExist(){
        boolean isInstance = false;
        for (AbstractAircraft enemyAircraft : enemyAircrafts){
            if(enemyAircraft instanceof BossEnemy){
                isInstance = true;
                boss = (BossEnemy) enemyAircraft;
                break;
            }
        }
        return isInstance;
    }

    /**
     * 添加敌机
     */
    protected abstract void addEnemy();

    /**
     * 英雄机增强
     */
    protected abstract void upDateVersion();

    /**
     * 钩子方法：控制敌机在每周期是否射击的
     * @return
     */
    protected boolean canEnemyShoot() {
        return true;
    }

    /**
     * 钩子方法：控制英雄机在每周期是否射击的钩子
     * @return
     */
    protected boolean canHeroShoot() {
        return true;
    }

    /**
     * 游戏启动入口，执行游戏逻辑
     */
    public final void action() {

        MusicManager.playBgm();

        // 定时任务：绘制、对象产生、碰撞判定、击毁及结束判定
        Runnable task = () -> {

            try{
                time += timeInterval;

                // 周期性执行（控制频率）
                if (timeCountAndNewCycleJudge()) {
                    System.out.println(time);

                    // 新敌机产生
                    addEnemy();

                    // 飞机射出子弹
                    shootAction();

                }

                if(Game.gameDifficulty!=1){
                    if(timeCountAndNewCycleJudgeForIncreaseDifficulty()){
                        //提升游戏难度
                        increaseDifficulty();
                    }
                }

                //产生Boss机
                generateBoss();

                //切换背景，提升英雄机火力
                upDateVersion();

                // 子弹移动
                bulletsMoveAction();

                // 飞机移动
                aircraftsMoveAction();

                // 道具移动
                propsMoveAction();

                // 撞击检测
                crashCheckAction();

                // 后处理
                postProcessAction();

                //每个时刻重绘界面
                repaint();

                // 游戏结束检查英雄机是否存活
                if (heroAircraft.getHp() <= 0) {
                    // 游戏结束
                    MusicManager.overBgm();
                    MusicManager.overBossBgm();
                    MusicManager.playOverBgm();
                    executorService.shutdown();
                    gameOverFlag = true;
                    System.out.println("Game Over!");

                    //切换至榜单界面
                    RankingList rankingList = new RankingList(Game.gameDifficulty);
                    Main.cardPanel.add(rankingList.getMainPanel());
                    Main.cardLayout.last(Main.cardPanel);
                    rankingList.inputUser(score,Game.gameDifficulty);
                }

            }catch (Exception e){
                System.out.println(e);
            }

        };

        /**
         * 以固定延迟时间进行执行
         * 本次任务执行完成后，需要延迟设定的延迟时间，才会执行新的任务
         */
        executorService.scheduleWithFixedDelay(task, timeInterval, timeInterval, TimeUnit.MILLISECONDS);

    }

    //***********************
    //      Action 各部分
    //***********************

    private boolean timeCountAndNewCycleJudge() {
        cycleTime += timeInterval;
        if (cycleTime >= cycleDuration && cycleTime - timeInterval < cycleTime) {
            // 跨越到新的周期
            cycleTime %= cycleDuration;
            return true;
        } else {
            return false;
        }
    }

    private boolean timeCountAndNewCycleJudgeForIncreaseDifficulty() {
        cycleTimeForIncreaseDifficulty += timeInterval;
        if (cycleTimeForIncreaseDifficulty >= cycleDurationForIncreaseDifficulty && cycleTimeForIncreaseDifficulty - timeInterval < cycleTimeForIncreaseDifficulty) {
            // 跨越到新的周期
            cycleTimeForIncreaseDifficulty %= cycleDurationForIncreaseDifficulty;
            return true;
        } else {
            return false;
        }
    }

    private void shootAction() {
        // 敌机射击
        if(canEnemyShoot()) {
            for (AbstractAircraft enemyAircraft : enemyAircrafts) {
                enemyBullets.addAll(enemyAircraft.shoot());
            }
        }

        // 英雄射击
        if(canHeroShoot()){
            heroBullets.addAll(heroAircraft.shoot());
        }

    }

    private void bulletsMoveAction() {
        for (BaseBullet bullet : heroBullets) {
            bullet.forward();
        }
        for (BaseBullet bullet : enemyBullets) {
            bullet.forward();
        }
    }

    private void aircraftsMoveAction() {
        for (AbstractAircraft enemyAircraft : enemyAircrafts) {
            enemyAircraft.forward();
        }
    }

    private void propsMoveAction() {
        for (AbstractProp prop : props) {
            prop.forward();
        }
    }

    public static void addScore(int num){
        score = score + num;
    }

    /**
     * 碰撞检测：
     * 1. 敌机攻击英雄
     * 2. 英雄攻击/撞击敌机
     * 3. 英雄获得补给
     */
    private void crashCheckAction() {
        // TODO 敌机子弹攻击英雄
        for (BaseBullet bullet : enemyBullets) {
            if (bullet.notValid()) {
                continue;
            }
            if (heroAircraft.crash(bullet)) {
                heroAircraft.decreaseHp(bullet.getPower());
                bullet.vanish();
            }
        }

        /**
        if(isBossExist()){
            if(boss.notValid()){
                props.addAll(boss.dropProps());
                MusicManager.overBossBgm();
                if(!gameOverFlag){
                    MusicManager.playBgm();
                }
            }
        }
         */


        // 英雄子弹攻击敌机
        for (BaseBullet bullet : heroBullets) {
            if (bullet.notValid()) {
                continue;
            }
            for (AbstractAircraft enemyAircraft : enemyAircrafts) {
                if (enemyAircraft.notValid()) {
                    // 已被其他子弹击毁的敌机，不再检测
                    // 避免多个子弹重复击毁同一敌机的判定
                    continue;
                }
                if (enemyAircraft.crash(bullet)) {
                    // 敌机撞击到英雄机子弹
                    // 敌机损失一定生命值
                    MusicManager.playHitEnemyPlayer();
                    enemyAircraft.decreaseHp(bullet.getPower());
                    bullet.vanish();
                    if (enemyAircraft.notValid()) {
                        if(enemyAircraft instanceof BossEnemy){
                            MusicManager.overBossBgm();
                            if(!gameOverFlag){
                                MusicManager.playBgm();
                            }
                        }
                        props.addAll(enemyAircraft.dropProps());

                        //分数增加
                        if(enemyAircraft instanceof MobEnemy){
                            addScore(10);
                        }else if(enemyAircraft instanceof EliteEnemy){
                            addScore(20);
                        }else if(enemyAircraft instanceof BossEnemy){
                            addScore(50);
                        }

                    }
                }
                // 英雄机 与 敌机 相撞，均损毁
                if (enemyAircraft.crash(heroAircraft) || heroAircraft.crash(enemyAircraft)) {
                    enemyAircraft.vanish();
                    heroAircraft.decreaseHp(Integer.MAX_VALUE);
                }
            }
        }

        //我方获得道具，道具生效
        for(AbstractProp prop : props){
            if(prop.notValid()){
                continue;
            }
            if (heroAircraft.crash(prop) || prop.crash(heroAircraft)){
                if(prop instanceof BombProp){
                    for(AbstractAircraft enemy: enemyAircrafts){
                        ((BombProp) prop).addObserver((Observer) enemy);
                    }
                    for(BaseBullet bullet: enemyBullets){
                        ((BombProp) prop).addObserver((Observer) bullet);
                    }

                }

                prop.effect(heroAircraft);
                prop.vanish();
            }
        }

    }


    /**
     * 后处理：
     * 1. 删除无效的子弹
     * 2. 删除无效的敌机
     * <p>
     * 无效的原因可能是撞击或者飞出边界
     */
    private void postProcessAction() {
        enemyBullets.removeIf(AbstractFlyingObject::notValid);
        heroBullets.removeIf(AbstractFlyingObject::notValid);
        enemyAircrafts.removeIf(AbstractFlyingObject::notValid);
        props.removeIf(AbstractFlyingObject::notValid);
    }


    //***********************
    //      Paint 各部分
    //***********************

    /**
     * 重写paint方法
     * 通过重复调用paint方法，实现游戏动画
     *
     * @param  g
     */
    @Override
    public void paint(Graphics g) {
        super.paint(g);

        // 绘制背景,图片滚动
        g.drawImage(ImageManager.BACKGROUND_IMAGE, 0, this.backGroundTop - Main.WINDOW_HEIGHT, null);
        g.drawImage(ImageManager.BACKGROUND_IMAGE, 0, this.backGroundTop, null);
        this.backGroundTop += 1;
        if (this.backGroundTop == Main.WINDOW_HEIGHT) {
            this.backGroundTop = 0;
        }

        // 先绘制子弹，后绘制飞机
        // 这样子弹显示在飞机的下层
        // 绘制道具，显示在最下层
        paintImageWithPositionRevised(g, props);

        paintImageWithPositionRevised(g, enemyBullets);
        paintImageWithPositionRevised(g, heroBullets);

        paintImageWithPositionRevised(g, enemyAircrafts);

        g.drawImage(ImageManager.HERO_IMAGE, heroAircraft.getLocationX() - ImageManager.HERO_IMAGE.getWidth() / 2,
                heroAircraft.getLocationY() - ImageManager.HERO_IMAGE.getHeight() / 2, null);

        if(isChange){
            try {
                g.drawImage(ImageIO.read(new FileInputStream("src/images/levelup.png")),100,170,null);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        //绘制得分和生命值
        paintScoreAndLife(g);

    }

    private void paintImageWithPositionRevised(Graphics g, List<? extends AbstractFlyingObject> objects) {
        if (objects.size() == 0) {
            return;
        }

        for (AbstractFlyingObject object : objects) {
            BufferedImage image = object.getImage();
            assert image != null : objects.getClass().getName() + " has no image! ";
            g.drawImage(image, object.getLocationX() - image.getWidth() / 2,
                    object.getLocationY() - image.getHeight() / 2, null);
        }
    }

    private void paintScoreAndLife(Graphics g) {
        int x = 10;
        int y = 25;
        g.setColor(new Color(16711680));
        g.setFont(new Font("SansSerif", Font.BOLD, 22));
        g.drawString("SCORE:" + this.score, x, y);
        y = y + 20;
        g.drawString("LIFE:" + this.heroAircraft.getHp(), x, y);
        if(MediumGame.isUpdata || HardGame.isUpdata){
            g.setColor(new Color(0xFFFFFF));
            y = y + 30;
            g.drawString("游戏难度升级！", x, y);
        }

        if(isBossExist()){
            g.setColor(Color.white);
            g.fillRect(boss.getLocationX()-50, boss.getLocationY()+100, 100,10);
            g.setColor(Color.red);
            g.fillRect(boss.getLocationX()-50,boss.getLocationY()+100, boss.getHp()*100/BOSS_ENEMY_HP, 10);
        }
    }


}
