package utils;

import jdk.nashorn.internal.runtime.linker.LinkerCallSite;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import pojo.Data;

import javax.swing.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * @author sxy
 * @create 2021-03-30-14:19
 */
public class Utils {


	/**
	 * 获取数据保存在data中
	 * @param fileName
	 * @param index
	 * @param data
	 */
    public static void getData(String fileName, int index, Data data){
        int index1 = 0;
        int n = 0;
        Integer[][] profit = null;
        Integer[][] weight = null;
        Integer[] profit1 = null;
        Integer[] weight1 = null;
        List<Float> list = new ArrayList<>();
        if (index == 1) {
            index = 5;
            index1 = index + 2;
            n = 10;
        } else if (index == 2) {
            index = 13;
            index1 = index + 2;
            n = 100;
        } else if (index == 3) {
            index = 21;
            index1 = index + 2;
            n = 200;
        } else if (index == 4) {
            index = 29;
            index1 = index + 2;
            n = 300;
        } else if (index == 5) {
            index = 37;
            index1 = index + 2;
            n = 400;
        } else if (index == 6) {
            index = 45;
            index1 = index + 2;
            n = 500;
        } else if (index == 7) {
            index = 53;
            index1 = index + 2;
            n = 600;
        } else if (index == 8) {
            index = 61;
            index1 = index + 2;
            n = 700;
        } else if (index == 9) {
            index = 69;
            index1 = index + 2;
            n = 800;
        } else if (index == 10) {
            index = 77;
            index1 = index + 2;
            n = 900;
        } else if (index == 11) {
            index = 85;
            index1 = index + 2;
            n = 1000;
        } else {
            System.out.println("输入有误！");
        }
        profit = new Integer[n+1][3];
        weight = new Integer[n+1][3];
        profit1 = new Integer[n*30];
        weight1 = new Integer[n*30];
        FileInputStream fis = null;
        InputStreamReader isr = null;
        BufferedReader br = null; 

        try {
            String str = "";

            String str1 = "";
            String str2 = "";

            fis = new FileInputStream(fileName);// FileInputStream

            // 从文件系统中的某个文件中获取字节

            isr = new InputStreamReader(fis);// InputStreamReader 是字节流通向字符流的桥梁,

            br = new BufferedReader(isr);// 从字符输入流中读取文件中的内容
            int i = 0;
            while ((str = br.readLine()) != null) {
            	//取出价值字符串
                if (index == i) {
                    str1 = str;
                }
                //取出重量字符串
                if (index1 == i) {
                    str2 = str;
                }
                i++;
            }
            //取出字符串中的'.'
            str1 = str1.replace(".", "");
            str2 = str2.replace(".", "");
            //通过','分割获得String数组
            String[] split = str1.split(",");
            String[] split1 = str2.split(",");
            
            //把String数组转化为整型二位数组
            profit = getTwoDimaensionalArray(split);
            weight = getTwoDimaensionalArray(split1);
            
            //获取价值重量比排序
            for (int j = 1; j <= split1.length; j++) {
                if (j % 3 == 0 && j != 1){
                    list.add((Float.parseFloat(split[j-1])/Float.parseFloat(split1[j-1])));
                }
            }
            list.sort((Float f1, Float f2) -> f2.compareTo(f1));
            //字符串数组转换为整型数组
            for (int j = 0; j < split.length; j++) {
                profit1[j] = Integer.parseInt(split[j]);
                weight1[j] = Integer.parseInt(split1[j]);
            }
            //保存数据
            data.setList(list);
            data.setProfit(profit);
            data.setWeight(weight);
            data.setProfit1(profit1);
            data.setWeight1(weight1);
        } catch (FileNotFoundException e) {
            System.out.println("找不到指定文件");

        } catch (IOException e) {
            System.out.println("读取文件失败");

        } finally {
            try {
                br.close();
                isr.close();
                fis.close();
                // 关闭的时候最好按照先后顺序关闭最后开的先关闭所以先关s,再关n,最后关m

            } catch (IOException e) {
                e.printStackTrace();

            }

        }
    }

    /**
     * 获取组数
     * @param index
     * @return
     */
    public static int getClassNumber(int index){
        if (index == 1){
            return 10;
        }else if (index == 2){
            return 100;
        }else if (index == 3){
            return 200;
        }else if (index == 4){
            return 300;
        }else if (index == 5){
            return 400;
        }else if (index == 6){
            return 500;
        }else if (index == 7){
            return 600;
        }else if (index == 8){
            return 700;
        }else if (index == 9){
            return 800;
        }else if (index == 10){
            return 900;
        }else if (index == 11){
            return 1000;
        }else {
            return 0;
        }
    }

    /**
     * 动态规划求解分组背包问题
     * @param N
     * @param C
     * @param dp
     * @param profit
     * @param weight
     */
    public static void Dynamicprogramming(int N,int C,int[][] dp,Integer[][] profit,Integer[][] weight){
        long currentTimeMillis = System.currentTimeMillis();
        for (int i = 1; i <= N; i++) {
            for (int j = 1; j <= C; j++) {
                dp[i][j] = dp[i-1][j];           // 不选第i组物品
                for (int k = 0; k < 3; k++) { // 第i组物品中选一件
                    if (j >= weight[i][k]) {
                    	//在不放和放了之中选最大的，公式dp[i-1][j-weight[i][k]] + profit[i][k]表示：如果要放第i组其中1个，那么应该用可选前i-1组的情况下背包容量为j-第i组其中1个的重量的最大价值加上第i组其中1个的价值
                        dp[i][j] = Math.max(dp[i][j], dp[i-1][j-weight[i][k]] + profit[i][k]);
                    }
                }
            }
        }
        long currentTimeMillis1 = System.currentTimeMillis();
        long l = currentTimeMillis1 - currentTimeMillis;
        System.out.println("求解时间：" + l +"毫秒");
        System.out.println(dp[N][C]);
        //结果写入TXT文件
        try {
            BufferedWriter out = new BufferedWriter(new FileWriter("结果.txt"));
            out.write("最优解为："+dp[N][C]+",时间为：" + l +"毫秒");
            System.out.println("文件写入成功");
            out.close();
        } catch (IOException e) {
        }
    }

    /**
     * 显示散点图
     * @param profit
     * @param weight
     */
    public static void getChart(Integer[] profit,Integer[] weight){
        if (profit == null){
            System.out.println("请先选择文件和组索引");
            return;
        }
        XYSeries data = new XYSeries("data");
        for (int i = 1; i < profit.length; i++) {
            if(weight[i]!=null && profit[i]!=null) {
                data.add(weight[i], profit[i]);
            }
        }
        //添加到数据集
        XYSeriesCollection dataset = new XYSeriesCollection();
        dataset.addSeries(data);

        //实现简单的散点图，设置基本的数据
        JFreeChart freeChart = ChartFactory.createScatterPlot(
                "Data scatter plot",// 图表标题
                "weight",//x轴方向数据标签
                "profit",//y轴方向数据标签
                dataset,//数据集，即要显示在图表上的数据
                PlotOrientation.VERTICAL,//设置方向
                true,//是否显示图例
                true,//是否显示提示
                false//是否生成URL连接
        );

        //以面板显示
        ChartPanel chartPanel = new ChartPanel(freeChart);
        chartPanel.setPreferredSize(new java.awt.Dimension(560, 400));

        //创建一个主窗口来显示面板
        JFrame frame = new JFrame("散点图");
        frame.setLocation(500, 400);
        frame.setSize(600, 500);

        //将主窗口的内容面板设置为图表面板
        frame.setContentPane(chartPanel);

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }

    /**
     * 把字符串数组转化为整型二维数组
     * @param strArr
     * @return
     */
    public static Integer[][] getTwoDimaensionalArray(String[] strArr){
    	int k = 1;
        int l = 0;
        int x = 0;
        Integer[][] arr = new Integer[strArr.length/3 + 1][3];
        for (int j = 1; j <= strArr.length; j++) {
            arr[k][x++] = Integer.parseInt(strArr[j - 1]);
            if (j % 3 == 0 && j != 1) {
                k++;
                x = 0;
            }
        }
        return arr;
    }
}
