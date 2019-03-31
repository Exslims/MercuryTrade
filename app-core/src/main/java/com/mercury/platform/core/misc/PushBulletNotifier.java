package com.mercury.platform.core.misc;
import com.mercury.platform.shared.entity.message.NotificationDescriptor;
import com.mercury.platform.shared.store.MercuryStoreCore;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Authenticator;
import java.net.HttpURLConnection;
import java.net.PasswordAuthentication;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

public class PushBulletNotifier {
	
	public PushBulletNotifier() {
        MercuryStoreCore.notificationSettingsSubject.subscribe(data -> setSettings(data.getPushbulletAPIKey(), data.getPushbulletDevice(), data.isPushbulletNotificationEnable()));
	}

	public static final String API_KEY = "api-key", DEVICES = "devices";
	private Map<String, String> settings;
	public Boolean enabled = true;

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


	public void sendNotification(String title, String content) throws IOException {
		if (!enabled)
			return;
		Authenticator.setDefault(new Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(settings.get(API_KEY), "".toCharArray());
			}
		});
        String device = getSettings().get("devices");
		String encodedTitle = URLEncoder.encode(title, "UTF-8");
		String encodedContent = URLEncoder.encode(content, "UTF-8");
		String encodedDevice = URLEncoder.encode("ufhYsa6sjApZfm8TCe", "UTF-8");

		String url = "https://api.pushbullet.com/v2/pushes";
		URL obj = new URL(url);
		HttpURLConnection con = (HttpURLConnection) obj.openConnection();

		// add request header
		con.setRequestMethod("POST");
		con.setRequestProperty("User-Agent", "Mozilla/5.0");
		con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");

		String urlParameters = "type=note&title=" + encodedTitle + "&body=" + encodedContent + "&device_iden=" + encodedDevice;

        // Send post request
        con.setDoOutput(true);
        DataOutputStream wr = new DataOutputStream(con.getOutputStream());
        System.out.println(urlParameters);
        wr.writeBytes(urlParameters);
        wr.flush();
        wr.close();

        int responseCode = con.getResponseCode();
        System.out.println("Response Code : " + responseCode);

        BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuffer response = new StringBuffer();

        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();

        // print result
        System.out.println(response.toString());
	}
	
	public boolean setSettings(String apikey, String device, Boolean enabled) {
		HashMap<String,String> settings = new HashMap<String, String>();
		settings.put("api-key", apikey);
		settings.put("devices", device);
		this.enabled = enabled;
		this.settings = settings;
		return settings.containsKey(API_KEY) && !settings.get(API_KEY).trim().equalsIgnoreCase("");
	}
	
	public boolean setSettings(Map<String, String> settings) {
		this.settings = settings;
		return settings.containsKey(API_KEY) && !settings.get(API_KEY).trim().equalsIgnoreCase("");
	}
	
    public Map<String, String> getSettings() {
        return this.settings;
    }

}