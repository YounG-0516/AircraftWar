package edu.hitsz.application.music;

public class MusicManager {
    private static Player bgmPlayer = new Player("src/videos/bgm.wav",true);
    private static Player bgmBossPlayer = new Player("src/videos/bgm_boss.wav",true);
    private static Player hitEnemyPlayer = new Player("src/videos/bullet_hit.wav",false);

    private static Player getSupplyPlayer = new Player("src/videos/get_supply.wav",false);
    private static Player bulletPlayer = new Player("src/videos/bullet.wav",false);
    private static Player bombPlayer = new Player("src/videos/bomb_explosion.wav",false);

    private static Player gameOverPlayer = new Player("src/videos/game_over.wav",false);

    //播放背景音乐
    public static void playBgm(){
        if(bgmPlayer.getStartMusic()){
            bgmPlayer.play();
        }
    }

    //播放BOSS机音乐
    public static void playBossBgm(){
        if(bgmBossPlayer.getStartMusic()){
            if(bgmPlayer.getStartMusic()){
                bgmPlayer.over();
            }
            bgmBossPlayer.play();
        }
    }

    public static void playHitEnemyPlayer(){
        if(hitEnemyPlayer.getStartMusic()){
            hitEnemyPlayer.play();
        }
    }

    //播放补血道具音乐
    public static void playSupply(){
        if(getSupplyPlayer.getStartMusic()){
            getSupplyPlayer.play();
        }
    }

    //播放炸弹道具音乐
    public static void playBomb(){
        if(bombPlayer.getStartMusic()){
            bombPlayer.play();
        }
    }
    //播放火力道具音乐
    public static void playBullet(){
        if(bulletPlayer.getStartMusic()){
            bulletPlayer.play();
        }
    }

    //播放游戏结束音乐
    public static void playOverBgm(){
        if(gameOverPlayer.getStartMusic()){
            gameOverPlayer.play();
        }
    }

    public static void overBgm(){
        bgmPlayer.over();
    }

    public static void overBossBgm(){
        bgmBossPlayer.over();
    }

}
