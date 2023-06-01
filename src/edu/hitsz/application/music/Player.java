package edu.hitsz.application.music;

/**
 * 音乐播放器
 */

public class Player {
    private MusicThread musicThread;
    private boolean isRound;
    private String path;
    private static boolean isStartMusic;

    public static boolean getStartMusic() {
        return isStartMusic;
    }
    public static void setStartMusic(boolean startMusic) {
        Player.isStartMusic = startMusic;
    }

    public Player(String path,boolean isRound){
        this.musicThread = new MusicThread(path);
        this.isRound = isRound;
        this.musicThread.setIsRound(isRound);
        this.path=path;
    }

    /**
     * 开始播放音乐
     */
    public void play(){
        if(isStartMusic){
            //如果当前线程已经在播放音乐
            if(musicThread.isAlive()){
                //打断之前的音乐
                musicThread.setIsStop(true);
            }

            this.musicThread = new MusicThread(path);
            musicThread.setIsRound(isRound);
            musicThread.setIsStop(false);
            musicThread.start();
        }
    }

    /**
     * 停止播放音乐
     */
    public void over(){
        if(musicThread.isAlive()){
            musicThread.setIsStop(true);
        }
    }

}
