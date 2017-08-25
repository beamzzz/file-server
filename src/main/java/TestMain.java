import java.io.File;
import java.io.IOException;  
import java.nio.charset.Charset;

import org.apache.http.Consts;
import org.apache.http.HttpEntity;  
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;  
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;  
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils; 


public class TestMain {
	
	public static void main(String[] args) {
		String url = "http://localhost:8004/file/image/uploadHeadImage";
		String fileName = "E:\\home\\20170818164840.jpg";
		upload(fileName,url);
		
	}
	
	public static void upload(String localFile,String url){
        CloseableHttpClient httpClient = null;
        CloseableHttpResponse response = null;
        try {
            httpClient = HttpClients.createDefault();
            
            HttpPost httpPost = new HttpPost(url);
            
            // 把文件转换成流对象FileBody
            FileBody bin = new FileBody(new File(localFile));
            
            StringBody userName = new StringBody("Scott", ContentType.create(
                    "text/plain", Consts.UTF_8));

            HttpEntity reqEntity = MultipartEntityBuilder.create()
                    .addPart("file", bin)
                    .addPart("userName", userName)
                    .build();
            httpPost.setHeader("x-auth-token","6a118c6b-6104-48c2-9484-6d884f115bb9");
            httpPost.setEntity(reqEntity);

            // 发起请求 并返回请求的响应
            response = httpClient.execute(httpPost);
                
            // 获取响应对象
            HttpEntity resEntity = response.getEntity();
            if (resEntity != null) {
                // 打印响应长度
                System.out.println("Response content length: " + resEntity.getContentLength());
                // 打印响应内容
                System.out.println(EntityUtils.toString(resEntity, Charset.forName("UTF-8")));
            }
            // 销毁
            EntityUtils.consume(resEntity);
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            try {
                if(response != null){
                    response.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            
            try {
                if(httpClient != null){
                    httpClient.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}	
