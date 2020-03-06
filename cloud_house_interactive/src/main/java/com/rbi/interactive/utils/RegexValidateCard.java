package com.rbi.interactive.utils;

import com.rbi.interactive.abnormal.CustomerException;

public class RegexValidateCard {
    public static String validateIdCard(String idCard) throws CustomerException {
//        String card = idCard.replaceAll("[\\(|\\)]", "");
//        if (card.length() != 8 && card.length() != 9 && idCard.length() != 10) {
//            return null;
//        }
        String sGender = "3";
        if(idCard.matches("(^\\d{17}([0-9]|[A-Z]|[a-z])$)|(^\\d{14}([0-9]|[A-Z]|[a-z])$)")){
            String sCardNum = idCard.substring(16, 17);
            if (Integer.parseInt(sCardNum) % 2 != 0) {
                sGender = "1";
            } else {
                sGender = "2";
            }
        } else if (idCard.matches("^[a-zA-Z][0-9]{9}$")) { // 台湾
            String char2 = idCard.substring(1, 2);
            if (char2.equals("1")) {
                sGender = "1";
            } else if (char2.equals("2")) {
                sGender = "2";
            } else {
                sGender="3";
            }
        }else if (idCard.matches("^[1|5|7][0-9]{6}\\(?[0-9A-Z]\\)?$")) { // 澳门
            sGender="3";
        }else if (idCard.matches("^[A-Z]{1,2}[0-9]{6}\\(?[0-9A]\\)?$")) { // 香港
            sGender="3";
        }else{
            throw new CustomerException();
        }
        return sGender;
    }

    public static void main(String[] args) {
        try {
            System.out.println(validateIdCard("522633198204105040"));
//            validateIdCard("A222433006");
//            validateIdCard("F120373153");
//            validateIdCard("A1234560");
//            System.out.println(validateIdCard("C668668E"));
//            validateIdCard("52152998");
        } catch (CustomerException e) {
            e.printStackTrace();
        }
    }
}