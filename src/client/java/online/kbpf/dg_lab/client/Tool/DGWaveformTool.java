package online.kbpf.dg_lab.client.Tool;

import online.kbpf.dg_lab.client.entity.Waveform.Waveform;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static online.kbpf.dg_lab.client.Dg_labClient.waveformMap;

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
            for (char ch : Text.toCharArray()) {                // 遍历文本中的每个字符
                if (Character.isDigit(ch)) {                    // 如果字符是数字
                    number.append(ch);                          // 将数字追加到StringBuilder中
                } else if (ch == ',') {                         // 如果遇到逗号，表示时间部分结束
                    waveformPairs1.Time = Integer.parseInt(number.toString()); // 将数字转换为时间
                    number = new StringBuilder();               // 重置StringBuilder以解析下一个数字
                    length += waveformPairs1.Time + 1;          // 更新波形总长度
                } else if (ch == ';') {                         // 如果遇到分号，表示强度部分结束
                    waveformPairs1.Strength = Integer.parseInt(number.toString()); // 设置强度
                    waveformPairs.add(waveformPairs1);          // 添加当前配对到列表
                    waveformPairs1 = new waveformPairs();       // 创建新的配对对象
                    number = new StringBuilder();               // 重置StringBuilder
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

//    public static int GetDuration(String waveform){
//
//        boolean Data = false;
//
//        int length = waveform.length();
//        if(length == 0 || waveform.charAt(0) != '\"') return 0;
//        StringBuilder number = new StringBuilder();
//        int count = 0;
//        int duration = 0;
//        for (int i = 0; i < length; i++){
//
//            char ch = waveform.charAt(i);
//            if(ch == '\"') {
//                if((count > 16 || count == 0) && Data) return 0;
//                Data = !Data;
//
//            }
//
//            else if(isHexCharacter(ch)) {
//                if(Data) {
//                    number.append(ch);
//                    count++;
//                    duration++;
//                    if (count % 2 == 0){
//                        if(Integer.parseInt(number.toString(), 16) > 100) return 0;
//                        number = new StringBuilder();
//
//                    }
//                }
//                else return 0;
//            }
//
//
//            if(!Data) count = 0;
//
//
//
//        }
//
//
//        return duration / 16 * 100;
//    }

    public static void updateDuration(){
        if(!waveformMap.isEmpty()) {
            for (Waveform waveform : waveformMap.values()) {
                waveform.updateDuration();
            }
        }
    }



    public static int checkAndCountValidSubstrings(String input) {
        // 按逗号分割大字符串
        String[] substrings = input.split(",");
        int validCount = 0;


        // 遍历每个小字符串
        for (String substring : substrings) {
            if (!validateSubstring(substring)) { // 如果有一个小字符串不满足条件
                return 0; // 直接返回 0
            }
            validCount++; // 如果满足条件，计数加 1
        }
        if(validCount == StringUtils.countMatches(input, ',') + 1)
            return validCount; // 返回满足条件的小字符串数量
        return 0;
    }


    public static boolean validateSubstring(String substring) {


        // 定义正则表达式，匹配双引号内的 16 个字符
        String regex = "^\"[0-9a-fA-F]{16}\"$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(substring);

        if (!matcher.matches()) {
            return false; // 如果格式不匹配，直接返回 false
        }

        // 提取双引号内的内容
        String content = substring.substring(1, substring.length() - 1);

        // 每两个字符组成一个 16 进制数，并检查是否小于等于 100
        for (int i = 0; i < content.length(); i += 2) {
            String hexPair = content.substring(i, i + 2); // 提取两个字符
            int decimalValue = Integer.parseInt(hexPair, 16); // 转换为 10 进制
            if (decimalValue > 100) {
                return false; // 如果大于 100，返回 false
            }
        }

        return true; // 所有条件都满足
    }











    // 定义waveformPairs类，用于存储时间和强度的配对信息
    private static class waveformPairs {
        int Time, Strength; // 时间和强度
    }

    //判断是否是数字(包括ABCDEF
    public static boolean isHexCharacter(char ch) {
        return String.valueOf(ch).matches("[0-9a-fA-F]");
    }
}









