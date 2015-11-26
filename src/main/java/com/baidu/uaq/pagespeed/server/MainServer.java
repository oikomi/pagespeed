package com.baidu.uaq.pagespeed.server;

import com.alibaba.fastjson.JSON;
import com.baidu.uaq.pagespeed.config.Config;
import com.baidu.uaq.pagespeed.config.Const;
import com.baidu.uaq.pagespeed.db.Redis;
import com.baidu.uaq.pagespeed.po.ReqTask;
import com.baidu.uaq.pagespeed.po.RespCmd;
import com.baidu.uaq.pagespeed.util.FileUtil;
import com.baidu.uaq.pagespeed.util.Shell;
import com.google.gson.Gson;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Created by miaohong01 on 15/11/9.
 */
public class MainServer extends HttpServlet {
    private static final Logger LOG = LogManager.getLogger(MainServer.class);
    private Config config = Config.getInstance();

    private ClassLoader classLoader = MainServer.class.getClassLoader();

    @Override
    public void init() throws ServletException {
        File f = new File(Const.BASE_PATH);
        f.mkdirs();

        Shell shell = new Shell("chmod 777 " + classLoader.getResource(Const.PAGE_SPEED_BIN).getFile());
        shell.runCmd();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        PrintWriter pw = resp.getWriter();
        pw.print("ok");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        List<String> deleteFileList = new ArrayList<String>();
        Map parameterMap = req.getParameterMap();
        PrintWriter pw = resp.getWriter();
        RespCmd respCmd = new RespCmd();
        Redis redis = new Redis(config.getRedisAddr(), config.getRedisPort());

//        String[] id = (String[]) parameterMap.get("id");
//        String[] har = (String[]) parameterMap.get("har");
        StringBuilder buffer = new StringBuilder();
        BufferedReader reader = req.getReader();
        String line;
        while ((line = reader.readLine()) != null) {
            buffer.append(line);
        }
        String reqBody = buffer.toString();
        ReqTask reqTask = null;

        Gson gson = new Gson();
        reqTask = gson.fromJson(reqBody, ReqTask.class);

        UUID uuid = UUID.randomUUID();

        String harFile = Const.BASE_PATH + Const.HAR_PREFIX + uuid;
        String resultFile = Const.BASE_PATH + Const.RESULT_PREFIX + uuid;
        deleteFileList.add(harFile);
        deleteFileList.add(resultFile);

        FileUtil fileUtil = new FileUtil(resultFile, harFile);

        fileUtil.writeHar(reqTask.getHar());

        // Shell shell = new Shell("chmod 777 " + classLoader.getResource(Const.PAGE_SPEED_BIN).getFile());
        // shell.runCmd();

        String cmd = classLoader.getResource(Const.PAGE_SPEED_BIN).getFile() + "  " + "-input_file=" + harFile + "  "
                + "-output_format=formatted_json" + "  " + "-output_file=" + resultFile;

        System.out.println(cmd);
        Shell shell = new Shell(cmd);
        shell.runCmd();

        String resultStr = fileUtil.getJson();
        // pw.print(resultStr);
        if (resultStr != null) {
            redis.addKV(reqTask.getId() + Const.STORE_SUFFIX, resultStr);
            respCmd.setCode(Const.SUCCESS);
            respCmd.setInfo("success");
        } else {
            respCmd.setCode(Const.FAILED);
            respCmd.setInfo("failed");
        }

        pw.print(JSON.toJSONString(respCmd));

        fileUtil.clear(deleteFileList);
    }
}
