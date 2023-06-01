package edu.hitsz.DAO;

import java.io.IOException;
import java.util.List;

public interface UserDao {


    /**
     * 将新的成绩加入榜单，并对榜单进行由高到低排序
     * @param user
     */
    void addData(User user,int level) throws IOException, ClassNotFoundException;

    /**
     * 删除数据
     * @param row
     */
    void deleteData(int row,int level) throws IOException;

    /**
     * 获取用户可读的榜单文件
     */
    void writeData(int level) throws ClassNotFoundException, IOException;

    void printData(int level) throws IOException;

    /**
     * 获取所有数据
     * @param
     */
    List<User> getAllData(int level) throws IOException, ClassNotFoundException;
}
