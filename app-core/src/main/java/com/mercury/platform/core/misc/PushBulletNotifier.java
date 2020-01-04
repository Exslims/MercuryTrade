package com.mercury.platform.core.misc;

import com.mercury.platform.shared.config.Configuration;
import com.mercury.platform.shared.config.configration.PlainConfigurationService;
import com.mercury.platform.shared.config.descriptor.NotificationSettingsDescriptor;
import com.mercury.platform.shared.entity.message.NotificationDescriptor;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class PushBulletNotifier {

    private PlainConfigurationService<NotificationSettingsDescriptor> settings;

    public PushBulletNotifier() {
        settings = Configuration.get().notificationConfiguration();
    }

    public void sendNotification(NotificationDescriptor notificationDescriptor) {
        String subject = "Buying request from: " + notificationDescriptor.getWhisperNickname();
        String content = notificationDescriptor.getSourceString();
        try {
            this.sendNotification(subject, content);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    private String getDeviceID() {
        try {
            StringBuilder result = new StringBuilder();
            URL urld = new URL("https://api.pushbullet.com/v2/devices");
            HttpURLConnection conn = (HttpURLConnection) urld.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Access-Token", settings.get().getPushbulletAPIKey());
            BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line;
            while ((line = rd.readLine()) != null) {
                result.append(line);
            }
            rd.close();
            JSONParser parser = new JSONParser();
            JSONObject jsonresponse;
            jsonresponse = (JSONObject) parser.parse(result.toString());
            JSONArray arr = (JSONArray) jsonresponse.get("devices");
            for (Object object : arr) {
                JSONObject tmp = (JSONObject) object;
                String nickname = (String) tmp.get("nickname");
                if (nickname.equals("htc_10")) {
                    return (String) tmp.get("iden");
                }
            }
        } catch (ParseException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }

    private void sendNotificationPOST(String title, String content, String device_id) {
        try {
            String token = settings.get().getPushbulletAPIKey();

            String encodedTitle = URLEncoder.encode(title, "UTF-8");
            String encodedContent = URLEncoder.encode(content, "UTF-8");
            String encodedDevice = URLEncoder.encode(device_id, "UTF-8");
            String encodedToken = URLEncoder.encode(token, "UTF-8");

            String rawData = "type=note&title=" + encodedTitle + "&body=" + encodedContent + "&device_iden=" + encodedDevice;
            URL u = new URL("https://api.pushbullet.com/v2/pushes");
            HttpURLConnection conn = (HttpURLConnection) u.openConnection();
            conn.setDoOutput(true);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Access-Token", encodedToken);
            conn.setRequestProperty("Content-Length", String.valueOf(rawData.length()));
            OutputStream os = conn.getOutputStream();

            os.write(rawData.getBytes());
            os.flush();

            int responseCode = conn.getResponseCode();
            System.out.println("Response Code : " + responseCode);
            BufferedReader reader = new BufferedReader( new InputStreamReader(conn.getInputStream()) );

            for ( String line; (line = reader.readLine()) != null; ) {
                System.out.println( line );
            }

            reader.close();
            os.close();

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendNotification(String title, String content) throws IOException {
        if (!settings.get().isPushbulletNotificationEnable()) {
            return;
        }

        String device_id = getDeviceID();
        sendNotificationPOST(title, content, device_id);
    }

}