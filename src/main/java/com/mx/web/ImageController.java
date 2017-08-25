package com.mx.web;

import com.mx.domain.User;
import com.mx.util.ImageUtil;
import com.mx.util.ReturnMessage;
import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpSession;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * @aother zcl
 * @date 2017/8/23
 */
@RestController
@RequestMapping("/image")
public class ImageController {

    private Logger logger = LoggerFactory.getLogger(ImageController.class);

    @Value("${image.headPath}")
    private String headPath;

    private final ResourceLoader resourceLoader;

    @Autowired
    public ImageController(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }

    @PostMapping("/uploadHeadImage")
    public ReturnMessage handleFileUpload(@RequestParam(name = "file",required = true) MultipartFile file,
                                          HttpSession session) {

        User user = (User) session.getAttribute("user");
        if (user == null){
            return new ReturnMessage("9999","用户未登录");
        }
        Long fileSize = file.getSize();
        logger.info("用户" + user.getUserCode() + "上传头像,大小" + fileSize);
        String path = headPath + "/" + user.getUserCode() + "/head";
        try {
            File pathdir = new File(path);
            if(!pathdir.exists()){
                pathdir.mkdirs();
            }
            Files.copy(file.getInputStream(),
                    Paths.get(path, "head.png"));
            String smallHead = path + "/head_small.png";
            ImageUtil.reduceImg(path +"/head.png" , smallHead, 80, 80,null);
        } catch (Exception e) {
            logger.error("上传失败",e);
            return new ReturnMessage("9999",e.getMessage());
        }

        return new ReturnMessage("9999","上传成功");
    }

    @GetMapping(value = "/{fineName:.+}")
    @ResponseBody
    public ResponseEntity<?> getFile(@PathVariable("fineName") String fineName) {

        try {
            String[] fileNames = fineName.split("-");
            String path = fileNames[0];
            String file = fileNames[1];

            if(Base64.isBase64(path)){
                path = headPath + "/" + new String(Base64.decodeBase64(path));
            }else{
                throw new RuntimeException();
            }
            logger.info("fineName:" + path + "/" + file );
            return ResponseEntity.ok(resourceLoader.getResource("file:" + Paths.get(path, file).toString()));
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }
    @GetMapping(value = "/{fineName:.+}/isExit")
    public String isExit(@PathVariable("fineName") String fineName){
        try{
            String[] fileNames = fineName.split("-");
            String path = fileNames[0];

            if(Base64.isBase64(path)){
                path = new String(Base64.decodeBase64(path));
            }else{
                throw new RuntimeException();
            }

            String file = headPath + "/" + path + "/" + fileNames[1];
            logger.info("fineName:"  + file );
            File localFile = new File(file);
            if(localFile.exists()){
                return "true";
            }else{
                return "false";
            }
        }catch(Exception e){
            return "false";
        }

    }
}
