package edu.hitsz.application.frame;

import edu.hitsz.DAO.User;
import edu.hitsz.DAO.UserDao;
import edu.hitsz.DAO.UserDaoImpl;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class RankingList {
    private JPanel topPanel;
    private JPanel bottomPanel;
    private JLabel rankingLabel;
    private JLabel levelLabel;
    private JButton deleteButton;
    private JTable scoreTable;
    private JPanel mainPanel;
    private JScrollPane tableScrollPanel;
    private UserDao userDao;
    private List<User> userList;

    String[] columnName = {"名次", "玩家名", "得分", "游戏结束时间"};
    String[][] tableData = null;

    public RankingList(int level) {

        //界面左上角展示文字
        if(level==1){
            levelLabel.setText("难度：EASY");
        }else if (level==2){
            levelLabel.setText("难度：MEDIUM");
        }else if (level==3){
            levelLabel.setText("难度：HARD");
        }

        try {
            userDao = new UserDaoImpl(level);
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

        try {
            userList = userDao.getAllData(level);
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

        getScoreTable();

        DefaultTableModel model = new DefaultTableModel(tableData, columnName){
            @Override
            //用户不能编辑
            public boolean isCellEditable(int row, int col){
                return false;
            }
        };

        //JTable并不存储自己的数据，而是从表格模型那里获取它的数据
        scoreTable.setModel(model);
        tableScrollPanel.setViewportView(scoreTable);

        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int row = scoreTable.getSelectedRow();
                //System.out.println(row);
                if(row!=-1){
                    int result = JOptionPane.showConfirmDialog(deleteButton, "是否确定中删除？");
                    if (JOptionPane.YES_OPTION == result ) {
                        try {
                            userDao.deleteData(row,level);
                        } catch (IOException ex) {
                            throw new RuntimeException(ex);
                        }
                    }
                }else{
                    JOptionPane.showMessageDialog(null, "没有选中删除选项", "warning",JOptionPane.WARNING_MESSAGE);
                }
                try {
                    refresh(level);
                } catch (IOException | ClassNotFoundException ex) {
                    throw new RuntimeException(ex);
                }

            }
        });
    }

    public void inputUser(int score, int level) throws IOException, ClassNotFoundException {

        // 弹出一个带有输入框的对话框
        String input = null;
        input = JOptionPane.showInputDialog(null, "游戏结束，您的分数为"+score+"\n"+"请输入名字记录得分：");

        if (input == null || input.isEmpty()) {
            JOptionPane.showMessageDialog(null, "未成功保存数据", "warning",JOptionPane.WARNING_MESSAGE);
            userDao.printData(level);
        }else{
            //获取当前时间
            Date date = new Date();
            SimpleDateFormat formatter = new SimpleDateFormat("MM-dd HH:mm");

            User user = new User();
            user.setUserName(input);
            user.setScore(score);
            user.setOverTime(formatter.format(date));

            //记录数据
            userDao.addData(user,level);
            refresh(level);
        }
    }

    /**
     * 显示表格
     */
    private void getScoreTable(){
        if(userList != null){
            int userNum = userList.size();
            tableData = new String[userNum][4];
            for (int i = 0; i < userNum; i++) {
                User user = userList.get(i);
                tableData[i][0]=(i+1)+"";
                tableData[i][1]=user.getUserName();
                tableData[i][2]=user.getScore()+"";
                tableData[i][3]=user.getTime();
            }

            DefaultTableModel model = new DefaultTableModel(tableData, columnName){
                @Override
                //用户不能编辑
                public boolean isCellEditable(int row, int col){
                    return false;
                }
            };
            //JTable并不存储自己的数据，而是从表格模型那里获取它的数据
            scoreTable.setModel(model);
            tableScrollPanel.setViewportView(scoreTable);
        }
    }

    public void refresh(int level) throws IOException, ClassNotFoundException {
        userList = userDao.getAllData(level);
        getScoreTable();
        userDao.printData(level);
    }

    public JPanel getMainPanel() {
        return mainPanel;
    }

    /**
    public static void main(String[] args) {
        JFrame frame = new JFrame("RankingList");
        frame.setContentPane(new RankingList(1).mainPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }
     */
}
