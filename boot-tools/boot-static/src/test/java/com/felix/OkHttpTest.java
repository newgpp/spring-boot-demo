package com.felix;

import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.*;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author felix
 * @desc some desc
 */
public class OkHttpTest {

    private static final Logger log = LoggerFactory.getLogger(OkHttpTest.class);

    private static OkHttpClient client = new OkHttpClient.Builder()
            .connectTimeout(Duration.ofSeconds(5))
            .readTimeout(Duration.ofSeconds(10))
            .build();


    @Test
    public void do_get_should_success() {
        //given
        String url = "http://127.0.0.1:8300/user/get";
        Map<String, String> params = new HashMap<>();
        params.put("name", "小浣熊");
        params.put("age", "3");
        //when
        String res = doGet(url, params, null);
        //then
        System.out.println(res);
    }

    @Test
    public void do_post_form_should_success() {
        //given
        String url = "http://127.0.0.1:8300/user/save";
        Map<String, String> params = new HashMap<>();
        params.put("name", "小浣熊");
        params.put("age", "3");
        //when
        String res = doPostForm(url, params, null);
        //then
        System.out.println(res);
    }

    @Test
    public void do_post_json_should_success() {
        //given
        String url = "http://127.0.0.1:8300/user/detail/save";
        Map<String, Object> params = new HashMap<>();
        params.put("name", "小浣熊");
        params.put("age", "3");
        //when
        String res = doPostJson(url, params, null);
        //then
        System.out.println(res);
    }

    @Test
    public void do_post_form_file_should_success() {
        //given
        String url = "http://127.0.0.1:8300/user/image/save";
        Map<String, String> params = new HashMap<>();
        params.put("name", "小浣熊");
        params.put("age", "3");
        //when
        String res = doPostFormFile(url, params, "/C:/Users/admin/Pictures/istockphoto-517188688-612x612.jpg", null);
        //then
        System.out.println(res);
    }

    private String doGet(String url, Map<String, String> params, Map<String, String> headers) {
        try {
            log.info("doGet, url={}, params={}, headers={}", url, params, headers);
            Request.Builder builder = new Request.Builder();
            HttpUrl.Builder urlBuilder = Objects.requireNonNull(HttpUrl.parse(url)).newBuilder();
            //url param
            if (params != null && !params.isEmpty()) {
                params.forEach(urlBuilder::addEncodedQueryParameter);
            }
            //header
            if (headers != null && !headers.isEmpty()) {
                headers.forEach(builder::addHeader);
            }
            HttpUrl httpUrl = urlBuilder.build();
            Request request = builder
                    .url(httpUrl)
                    .method("GET", null)
                    .build();
            Response response = client.newCall(request).execute();
            return Objects.requireNonNull(response.body()).string();
        } catch (Exception e) {
            log.error("doGet error: ", e);
            return null;
        }
    }

    private String doPostForm(String url, Map<String, String> params, Map<String, String> headers) {
        try {
            log.info("doPostForm, url={}, params={}, headers={}", url, params, headers);
            Request.Builder builder = new Request.Builder();
            //header
            if (headers != null && !headers.isEmpty()) {
                headers.forEach(builder::addHeader);
            }
            String content = "";
            if (params != null) {
                content = params.entrySet().stream().map(x -> x.getKey() + "=" + x.getValue()).collect(Collectors.joining("&"));
            }
            MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");
            RequestBody body = RequestBody.create(content, mediaType);
            Request request = builder
                    .url(url)
                    .method("POST", body)
                    .addHeader("Content-Type", "application/x-www-form-urlencoded")
                    .build();
            Response response = client.newCall(request).execute();
            return Objects.requireNonNull(response.body()).string();
        } catch (Exception e) {
            log.error("doPostForm error: ", e);
            return null;
        }
    }

    private String doPostFormFile(String url, Map<String, String> params, String filePath, Map<String, String> headers) {
        try {
            log.info("doPostForm, url={}, params={}, headers={}", url, params, headers);
            Request.Builder builder = new Request.Builder();
            //header
            if (headers != null && !headers.isEmpty()) {
                headers.forEach(builder::addHeader);
            }
            //form param
            MultipartBody.Builder bodyBuilder = new MultipartBody.Builder().setType(MultipartBody.FORM);
            if (params != null && !params.isEmpty()) {
                params.forEach(bodyBuilder::addFormDataPart);
            }
            //form file
            RequestBody file = RequestBody.create(new File(filePath), MediaType.parse("application/octet-stream"));
            RequestBody body = bodyBuilder
                    .addFormDataPart("file", filePath, file)
                    .build();
            Request request = builder
                    .url(url)
                    .method("POST", body)
                    .build();
            Response response = client.newCall(request).execute();
            return Objects.requireNonNull(response.body()).string();
        } catch (Exception e) {
            log.error("doPostFormFile error: ", e);
            return null;
        }
    }

    private String doPostJson(String url, Map<String, Object> params, Map<String, String> headers) {
        try {
            log.info("doPostForm, url={}, params={}, headers={}", url, params, headers);
            Request.Builder builder = new Request.Builder();
            //header
            if (headers != null && !headers.isEmpty()) {
                headers.forEach(builder::addHeader);
            }
            String content = new ObjectMapper().writeValueAsString(params);
            MediaType mediaType = MediaType.parse("application/json");
            RequestBody body = RequestBody.create(content, mediaType);
            Request request = builder
                    .url(url)
                    .method("POST", body)
                    .addHeader("Content-Type", "application/json")
                    .build();
            Response response = client.newCall(request).execute();
            return Objects.requireNonNull(response.body()).string();
        } catch (Exception e) {
            log.error("doPostJson error: ", e);
            return null;
        }
    }


}
