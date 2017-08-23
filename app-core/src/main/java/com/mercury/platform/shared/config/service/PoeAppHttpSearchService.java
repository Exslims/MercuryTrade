package com.mercury.platform.shared.config.service;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

public class PoeAppHttpSearchService extends HttpItemSearchService {
    public static void main(String[] args) {
        PoeAppHttpSearchService searchService = new PoeAppHttpSearchService();
        searchService.test();
    }

    public static List<NameValuePair> getFormData() {
        List<NameValuePair> nvp = new ArrayList<>();
        nvp.add(new BasicNameValuePair("league", "Legacy"));
        nvp.add(new BasicNameValuePair("type", ""));
        nvp.add(new BasicNameValuePair("base", ""));
        nvp.add(new BasicNameValuePair("name", ""));
        nvp.add(new BasicNameValuePair("dmg_min", ""));
        nvp.add(new BasicNameValuePair("dmg_max", ""));
        nvp.add(new BasicNameValuePair("aps_min", ""));
        nvp.add(new BasicNameValuePair("aps_max", ""));
        nvp.add(new BasicNameValuePair("crit_min", ""));
        nvp.add(new BasicNameValuePair("crit_max", ""));
        nvp.add(new BasicNameValuePair("dps_min", ""));
        nvp.add(new BasicNameValuePair("dps_max", ""));
        nvp.add(new BasicNameValuePair("edps_min", ""));
        nvp.add(new BasicNameValuePair("edps_max", ""));
        nvp.add(new BasicNameValuePair("pdps_min", ""));
        nvp.add(new BasicNameValuePair("pdps_max", ""));
        nvp.add(new BasicNameValuePair("armour_min", ""));
        nvp.add(new BasicNameValuePair("armour_max", ""));
        nvp.add(new BasicNameValuePair("evasion_min", ""));
        nvp.add(new BasicNameValuePair("evasion_max", ""));
        nvp.add(new BasicNameValuePair("shield_min", ""));
        nvp.add(new BasicNameValuePair("shield_max", ""));
        nvp.add(new BasicNameValuePair("block_min", ""));
        nvp.add(new BasicNameValuePair("block_max", ""));
        nvp.add(new BasicNameValuePair("sockets_min", ""));
        nvp.add(new BasicNameValuePair("sockets_max", ""));
        nvp.add(new BasicNameValuePair("link_min", ""));
        nvp.add(new BasicNameValuePair("link_max", ""));
        nvp.add(new BasicNameValuePair("sockets_r", ""));
        nvp.add(new BasicNameValuePair("sockets_g", ""));
        nvp.add(new BasicNameValuePair("sockets_b", ""));
        nvp.add(new BasicNameValuePair("sockets_w", ""));
        nvp.add(new BasicNameValuePair("linked_r", ""));
        nvp.add(new BasicNameValuePair("linked_g", ""));
        nvp.add(new BasicNameValuePair("linked_b", ""));
        nvp.add(new BasicNameValuePair("linked_w", ""));
        nvp.add(new BasicNameValuePair("rlevel_min", ""));
        nvp.add(new BasicNameValuePair("rlevel_max", ""));
        nvp.add(new BasicNameValuePair("rstr_min", ""));
        nvp.add(new BasicNameValuePair("rstr_max", ""));
        nvp.add(new BasicNameValuePair("rdex_min", ""));
        nvp.add(new BasicNameValuePair("rdex_max", ""));
        nvp.add(new BasicNameValuePair("rint_min", ""));
        nvp.add(new BasicNameValuePair("rint_max", ""));
        nvp.add(new BasicNameValuePair("mod_name", ""));
        nvp.add(new BasicNameValuePair("mod_min", ""));
        nvp.add(new BasicNameValuePair("mod_max", ""));
        nvp.add(new BasicNameValuePair("group_type", "And"));
        nvp.add(new BasicNameValuePair("group_min", ""));
        nvp.add(new BasicNameValuePair("group_max", ""));
        nvp.add(new BasicNameValuePair("group_count", "1"));
        nvp.add(new BasicNameValuePair("q_min", ""));
        nvp.add(new BasicNameValuePair("q_max", ""));
        nvp.add(new BasicNameValuePair("level_min", ""));
        nvp.add(new BasicNameValuePair("level_max", ""));
        nvp.add(new BasicNameValuePair("mapq_min", ""));
        nvp.add(new BasicNameValuePair("mapq_max", ""));
        nvp.add(new BasicNameValuePair("rarity", ""));
        nvp.add(new BasicNameValuePair("seller", "exslims"));
        nvp.add(new BasicNameValuePair("thread", ""));
        nvp.add(new BasicNameValuePair("identified", ""));
        nvp.add(new BasicNameValuePair("corrupted", ""));
        nvp.add(new BasicNameValuePair("online", ""));
        nvp.add(new BasicNameValuePair("buyout", ""));
        nvp.add(new BasicNameValuePair("altart", ""));
        nvp.add(new BasicNameValuePair("capquality", ""));
        nvp.add(new BasicNameValuePair("buyout_min", ""));
        nvp.add(new BasicNameValuePair("buyout_max", ""));
        nvp.add(new BasicNameValuePair("buyout_currency", ""));
        nvp.add(new BasicNameValuePair("crafted", ""));
        nvp.add(new BasicNameValuePair("ilvl_min", ""));
        nvp.add(new BasicNameValuePair("buyout_currency", ""));
        nvp.add(new BasicNameValuePair("ilvl_max", ""));
        return nvp;
    }

    @Override
    public String test() {
        DefaultHttpClient httpClient = new DefaultHttpClient();
        HttpPost request = new HttpPost("http://poe.trade/search");
        setRequestHeaders(request);
        try {
            request.setEntity(new UrlEncodedFormEntity(getFormData(), HTTP.UTF_8));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        HttpResponse response = null;
        try {
            response = httpClient.execute(request);
            if (response != null) {
                String url = response.getFirstHeader("Location").getValue();
                try {
                    response.getEntity().consumeContent();

                    HttpGet httpGet = new HttpGet(url);
                    response = httpClient.execute(httpGet);
                    HttpEntity entity = response.getEntity();

                    System.out.println(EntityUtils.toString(entity));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private void setRequestHeaders(HttpPost request) {
        request.addHeader(new BasicHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8"));
        request.addHeader(new BasicHeader("Accept-Encoding", "gzip, deflate"));
        request.addHeader(new BasicHeader("Accept-Language", "ru-RU,ru;q=0.8,en-US;q=0.6,en;q=0.4"));
        request.addHeader(new BasicHeader("Cache-Control", "max-age=0"));
        request.addHeader(new BasicHeader("Connection", "keep-alive"));
        request.addHeader(new BasicHeader("Content-Type", "application/x-www-form-urlencoded"));
        request.addHeader(new BasicHeader("Cookie", "color=dark; live_notify_sound=0; live_notify_browser=0; live_frequency=5; _gat=1; _ga=GA1.2.1401167521.1452005585; league=Legacy"));
        request.addHeader(new BasicHeader("Host", "poe.trade"));
        request.addHeader(new BasicHeader("Origin", "http://poe.trade"));
        request.addHeader(new BasicHeader("Referer", "http://poe.trade/"));
        request.addHeader(new BasicHeader("Upgrade-Insecure-Requests", "1"));
        request.addHeader(new BasicHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/49.0.2623.110 Safari/537.36"));
    }
}
