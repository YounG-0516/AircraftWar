package edu.hitsz.DAO;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class UserDaoImpl implements UserDao{

    //模拟数据库数据
    private List<User> scoreList = new ArrayList<>();
    private File file;

    public UserDaoImpl(int level) throws IOException, ClassNotFoundException{

        //根据级别选择
        if(level==1){
            file = new File("src\\edu\\hitsz\\DAO\\userImformation.txt");
        }else if(level==2){
            file = new File("src\\edu\\hitsz\\DAO\\userImformation_medium.txt");
        }else if(level==3){
            file = new File("src\\edu\\hitsz\\DAO\\userImformation_hard.txt");
        }

        /**如果之前已经有榜单存在
        if(file.length() != 0){
            //从文件中读出之前的榜单
            ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file));
            scoreList = (List<User>)ois.readObject();
            ois.close();
        }
         */
    }

    public void writeData(int level) throws IOException {
        //将榜单写回文件
        ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file));
        oos.writeObject(scoreList);
        oos.close();
    }

    @Override
    public void addData(User user,int level) throws IOException, ClassNotFoundException {
        //添加当前数据
        scoreList.add(user);
        writeData(level);
    }

    @Override
    public void deleteData(int row,int level) throws IOException {
        scoreList.remove(scoreList.get(row));
        writeData(level);
    }

    public void printData(int level) throws IOException {
        //如果之前已经有榜单存在,则可以打印
        if(file.exists()){

            //通过字符输出流实现可读文件
            FileWriter fileWriter = null;
            if(level==1){
                fileWriter = new FileWriter("src\\edu\\hitsz\\DAO\\RankingList_easy.txt");
            }else if(level==2){
                fileWriter = new FileWriter("src\\edu\\hitsz\\DAO\\RankingList_medium.txt");
            }else if(level==3){
                fileWriter = new FileWriter("src\\edu\\hitsz\\DAO\\RankingList_hard.txt");
            }

            int i = 1;
            System.out.println("""
                **************************************
                               得分排行榜
                **************************************""");
            for (User userPrint : scoreList) {
                System.out.println("第"+i+"名： "+userPrint.getUserName()+", "+userPrint.getScore()+", "+userPrint.getTime());
                fileWriter.write("第"+i+"名： "+userPrint.getUserName()+", "+userPrint.getScore()+", "+userPrint.getTime()+"\n");
                i++;
            }
            fileWriter.close();

        }
    }

    @Override
    public List<User> getAllData(int level) throws IOException, ClassNotFoundException {

        if(file.exists() && file.length() != 0) {
            //从文件中读出之前的榜单
            ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file));
            scoreList = (List<User>) ois.readObject();
            ois.close();

            //根据分数排序
            Collections.sort(scoreList, new Comparator<User>() {
                @Override
                public int compare(User o1, User o2) {
                    return o2.getScore()-o1.getScore();
                }
            });

            return scoreList;

        }else{
            return new ArrayList<>();
        }

    }
}
