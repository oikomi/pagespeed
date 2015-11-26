package com.baidu.uaq.pagespeed.util;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.io.BufferedReader;
import java.io.InputStreamReader;

/**
 * Created by miaohong01 on 15/11/9.
 */
public class Shell {
    private static final Logger LOG = LogManager.getLogger(Shell.class);
    private String cmd;

    public Shell(String cmd) {
        this.cmd = cmd;
    }

    public String runCmd() {
        try {
            Process ps = Runtime.getRuntime().exec(this.cmd);
            ps.waitFor();

            BufferedReader br = new BufferedReader(new InputStreamReader(ps.getInputStream()));
            StringBuffer sb = new StringBuffer();
            String line;
            while ((line = br.readLine()) != null) {
                // sb.append(line).append("\n");
            }
            String result = sb.toString();
            // System.out.println(result);

            return result;
        }
        catch (Exception e) {
            LOG.error("run cmd failed | " + this.cmd);
            e.printStackTrace();
        }
        return null;
    }
}