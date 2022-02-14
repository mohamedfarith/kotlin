package com.app.kotlin.hackerrankSolutions;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

class Solution {

    public static void main(String[] args){
        System.out.println(firstUniqChar("leetcode"));
    }
    public static int firstUniqChar(String s) {

        List<String> arrayList = new ArrayList<>(Arrays.asList(s.split("")));
        int position = -1;
        for (int i = 0; i < arrayList.size(); i++) {
            int equalCount = 0;
            for (int j = 0; j < arrayList.size(); j++) {
                if (arrayList.get(i).equals(arrayList.get(j))) {
                    equalCount++;
                }
            }
            if (equalCount == 0) {
                position = i;
                break;
            }
        }
        return position;

    }



}