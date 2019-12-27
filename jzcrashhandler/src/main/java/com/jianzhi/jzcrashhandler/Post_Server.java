package com.jianzhi.jzcrashhandler;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;

public class Post_Server {
    public static Boolean post(String token, String type, String data_) {
        try {
            String data = " {\n" +
                    " \"to\" : \"" + token + "\",\n" +
                    " \n" +
                    " \"data\" : {\n" +
                    " \"type\" : \"" + type + "\",\n" +
                    " \"data\": \"" + data_ + "\"\n" +
                    " \"time\": \"" + getDateTime() + "\"\n" +
                    " }\n" +
                    "}";
            URL url = new URL("https://fcm.googleapis.com/fcm/send");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("Authorization", "key=AAAASxqGk48:APA91bGZojS7sgcw5nUIjjnCFNH9IBLeZL-F7K0-Ex61-JruWB5kEIrz-LWmAEACP2qDyRiS0XbcxWi5zf9ogkrXbVjjFBrI0evb73FOrSPJ0JGpvzidnYgJRl5-auSQj9iAEDPEtfxb");
            conn.setRequestMethod("POST");
            conn.setUseCaches(false);
            conn.setDoInput(true);
            conn.setDoOutput(true);
            DataOutputStream dos = new DataOutputStream(conn.getOutputStream());
            dos.write(data.getBytes("utf-8"));
            dos.flush();
            if (conn.getResponseCode() == 200) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream(), "utf-8"));
                String line = null;
                StringBuffer strBuf = new StringBuffer();
                while ((line = reader.readLine()) != null) {
                    strBuf.append(line);
                }
                System.out.print(strBuf);
                reader.close();
            }
            dos.close();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static String getDateTime() {
        SimpleDateFormat sdFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        java.util.Date date = new java.util.Date();
        String strDate = sdFormat.format(date);
        return strDate;
    }
}
