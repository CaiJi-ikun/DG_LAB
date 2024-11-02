package online.kbpf.dg_lab.client.Tool;

import java.util.ArrayList;

public class DGWaveformTool {
    // 定义私有字段，用来存储时间、文本、频率数组、长度和波形字符串

    /**
     * 将文本转换为频率强度。根据文本内容解析出时间和强度的配对，
     * 然后将其转换为频率数组。
     * 将频率数组转换为波形字符串表示形式。每4个频率组成一个十六进制表示的"0A0A0A0A"格式的块。
     */
    public static String TextToWaveform(String Text) {

        int length = 0; // 初始化长度
        double[] Frequency;
        ArrayList<waveformPairs> waveformPairs = new ArrayList<>(); // 存储时间-强度的配对
        StringBuilder number = new StringBuilder(); // 用于存储当前解析的数字
        {
            waveformPairs waveformPairs1 = new waveformPairs(); // 创建一个新的waveformPairs对象
            for (char ch : Text.toCharArray()) { // 遍历文本中的每个字符
                if (Character.isDigit(ch)) { // 如果字符是数字
                    number.append(ch); // 将数字追加到StringBuilder中
                } else if (ch == ',') { // 如果遇到逗号，表示时间部分结束
                    waveformPairs1.Time = Integer.parseInt(number.toString()); // 将数字转换为时间
                    number = new StringBuilder(); // 重置StringBuilder以解析下一个数字
                    length += waveformPairs1.Time + 1; // 更新波形总长度
                } else if (ch == ';') { // 如果遇到分号，表示强度部分结束
                    waveformPairs1.Strength = Integer.parseInt(number.toString()); // 设置强度
                    waveformPairs.add(waveformPairs1); // 添加当前配对到列表
                    waveformPairs1 = new waveformPairs(); // 创建新的配对对象
                    number = new StringBuilder(); // 重置StringBuilder
                }
            }
        }
        // 初始化频率数组，大小为波形长度+1
        Frequency = new double[length + 1];
        Frequency[0] = 0; // 起始频率设置为0
        int j = 1; // 用于跟踪频率数组的索引
        for (waveformPairs waveformPairs1 : waveformPairs) { // 遍历所有时间-强度配对
            double max = waveformPairs1.Strength; // 当前强度的最大值
            double min = Frequency[j - 1]; // 前一个频率值的最小值
            System.out.println(max + " " + min); // 输出当前强度与前一个频率值的差异
            double Difference = (max - min) / (waveformPairs1.Time + 1); // 计算频率的增量
            for (int i = 0; i <= waveformPairs1.Time; i++) { // 根据时间增量填充频率数组
                Frequency[j] = Frequency[j - 1] + Difference; // 计算每个时间点的频率
                j++; // 增加索引
            }
        }

        StringBuilder frequency = new StringBuilder(); // 存储生成的波形字符串
        for (int i = 0; i <= length; i++) { // 遍历所有频率值
            if (i % 4 == 0) { // 每4个频率值组合为一个块
                if (i != 0) frequency.append("\",\"0A0A0A0A"); // 添加分隔符并开始新的块
                else frequency.append("\"0A0A0A0A"); // 第一个块的开头
            }
            frequency.append(String.format("%02X", (int) Frequency[i])); // 将频率值转换为16进制格式
        }
        // 根据波形长度，处理最后一个块中的空白位
        switch (length % 4) {
            case 0 -> frequency.append("000000"); // 需要填充3个空白位
            case 1 -> frequency.append("0000");   // 需要填充2个空白位
            case 2 -> frequency.append("00");     // 需要填充1个空白位
        }
        System.out.println((length - 1) % 4); // 输出最后一个块的空白位数
        frequency.append('\"'); // 添加波形结束符
        return frequency.toString(); // 设置生成的波形字符串
    }




}

// 定义waveformPairs类，用于存储时间和强度的配对信息
class waveformPairs {
    int Time, Strength; // 时间和强度
}
