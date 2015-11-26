package com.baidu.uaq.pagespeed.util;

import org.apache.commons.io.IOUtils;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * Created by miaohong01 on 15/11/9.
 */
public class FileUtil {
    private static final Logger LOG = LogManager.getLogger(FileUtil.class);
    private final int rStackSize = 1024 * 1024;
    private String rPath;
    private String wPath;

    public FileUtil(String rPath, String wPath) {
        this.rPath = rPath;
        this.wPath = wPath;
    }

    public void writeHar(String har) {
        FileWriter wd = null;
        try {
            wd = new FileWriter(this.wPath);
            wd.write(har);
            wd.close();
        } catch (IOException e) {
            LOG.error("writeHar failed");
            e.printStackTrace();
        } finally {
            try {
                wd.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public String getJson() {
        InputStream in = null;
        try {
            in = new FileInputStream(this.rPath);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        try {
            String result = IOUtils.toString(in);
            return result;
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                in.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return null;
    }



    public void clear(List<String> deleteFileList) {
        for (String f : deleteFileList) {
            deleteFile(f);
        }
    }

    public boolean deleteFile(String sPath) {
        boolean flag = false;
        File file = new File(sPath);
        if (file.isFile() && file.exists()) {
            file.delete();
            flag = true;
        }
        return flag;
    }
}