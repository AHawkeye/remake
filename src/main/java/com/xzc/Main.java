package com.xzc;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import de.vandermeer.asciitable.AsciiTable;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.*;

public class Main {
    private static int pre;
    private static Map<Segment, String> map;
    private static List<Segment> segments;
    private static Map<String, Integer> countMap;
    private static int count;

    public static void main(String[] args) {
        getData();
        while (true) {
            String getStr;
            System.out.print("Please input 1 to remake or 0 to exit: ");
            Scanner sc = new Scanner(System.in);
            getStr = sc.nextLine();
            if ("1".equals(getStr)) {
                remake();
            } else if ("0".equals(getStr)) {
                AsciiTable at = new AsciiTable();
                at.addRule();
                at.addRow("Country/Region","Count","Possibility");
                for (String s : countMap.keySet()) {
                    int c = countMap.get(s);
                    double percent = (double) c / count;
                    DecimalFormat df = new DecimalFormat("0.000");
                    String per = df.format(BigDecimal.valueOf(percent));
                    at.addRule();
                    at.addRow(s,String.valueOf(c),per+"%");
                }
                at.addRule();
                String rend = at.render();
                System.out.println(rend);
                break;
            } else {
                System.out.println("Illegal input!Please input again!");
            }
        }
    }

    private static void getData() {

        JSONArray array = JSON.parseArray(Data.getData());

        count = 0;
        pre = 0;
        map = new HashMap<>();
        segments = new ArrayList<>();
        countMap = new HashMap<>();
        if (array != null) {
            for (Object o : array) {
                JSONObject jsonObject = (JSONObject) o;
                Country country = JSON.toJavaObject(jsonObject, Country.class);
                int birthCount = (int) (country.getBirthRate() * country.getPopulation());
                Segment s = new Segment(pre, pre + birthCount);
                segments.add(s);
                pre += birthCount;
                map.put(s, country.getName());
            }
        }
    }

    private static void remake() {
        Random random = new Random();
        //a random integer from 0 to pre
        int i = random.nextInt(pre);
        String name = null;
        if (i < pre) {
            for (Segment s : segments) {
                if (s.inThisSegment(i)) {
                    name = map.get(s);
                    System.out.println("Country/Region: " + name);
                }
            }
        }
        //i == pre
        else {
            name = map.get(segments.get(segments.size() - 1));
            System.out.println("Country/Region: " + name);
        }
        if (countMap.containsKey(name)) {
            countMap.put(name, countMap.get(name) + 1);
        } else {
            countMap.put(name, 1);
        }
        count++;
    }
}
