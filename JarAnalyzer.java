package com.example.test;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.*;

public class JarAnalyzer {
    private static final String MODULE_OSS = "oss";
    private static final String MODULE_APP = "app";
    private static final String MODULE_OPEN = "open";
    private static final String JAR_COMMAND = "jar tvf ";
    private static final String JAR_LIBS_FOLDER_NAME = "BOOT-INF/lib/";
    private static final String OSS_JAR_PATH = "E:/qiyuesuo/private/private-java/oss/target/privoss.jar";
    private static final String APP_JAR_PATH = "E:/qiyuesuo/private/private-java/sign/target/privapp.jar";
    private static final String OPEN_JAR_PATH = "E:/qiyuesuo/private/private-java/open/target/privopen.jar";

    public static void main(String[] args) throws Exception {
        JarAnalyzer analyzer = new JarAnalyzer();
        List<String> ossJars = analyzer.loadLibsFromJar(OSS_JAR_PATH,MODULE_OSS);
        List<String> appJars = analyzer.loadLibsFromJar(APP_JAR_PATH,MODULE_APP);
        List<String> openJars = analyzer.loadLibsFromJar(OPEN_JAR_PATH,MODULE_OPEN);

        System.out.println("OSS系统Jar包数===>" + ossJars.size());
        System.out.println("APP系统Jar包数===>" + appJars.size());
        System.out.println("OPEN系统Jar包数===>" + openJars.size());

        Set<String> pubJars = new HashSet<String>();
        List<String> allJars = new ArrayList<String>();
        allJars.addAll(ossJars);
        allJars.addAll(appJars);
        allJars.addAll(openJars);

        for(String cell : allJars){
            pubJars.add(cell);
        }

        System.out.println("并集jar包数量" + pubJars.size());

        Map<String,Integer> jarMap = new HashMap<String,Integer>();
        for(String cell : pubJars){
            Integer count = 0;
            if(ossJars.contains(cell)){
                count ++;
            }
            if(appJars.contains(cell)){
                count ++;
            }
            if(openJars.contains(cell)){
                count ++;
            }
            jarMap.put(cell,count);
        }

        int sum = 0;
        for(Map.Entry<String,Integer> entry : jarMap.entrySet()){
            if(3 == entry.getValue()){
                System.out.println("公共jar包：" + entry.getKey());
                sum++;
            }
        }
        System.out.println("共计" + sum + "个");
    }

    /**
     * 从jar包中，读取libs
     * @param jarPath
     * @return
     */
    private List<String> loadLibsFromJar(String jarPath,String module){
        List<String> jars = null;
        try{
            String command = JAR_COMMAND + jarPath;
            Process process = Runtime.getRuntime().exec(command);
            jars = printInputstream(process.getInputStream(),module);
        }catch (Exception e){
            e.printStackTrace();
        }
        return jars;
    }

    /**
     * 读取控制台打印的文字
     * @param in
     * @return
     * @throws Exception
     */
    private List<String> printInputstream(InputStream in,String module) throws Exception{
        List<String> jars = new ArrayList<String>();
        InputStream inputStream = in;
        InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
        String line = null;
        while((line=bufferedReader.readLine())!=null){
            if(line.contains(JAR_LIBS_FOLDER_NAME)){
                int index = line.lastIndexOf(JAR_LIBS_FOLDER_NAME);
                String jarName = line.substring(index + JAR_LIBS_FOLDER_NAME.length(),line.length());
                if(!"".equals(jarName.trim())){
//                    System.out.println(jarName);
                    jars.add(jarName);
                }
            }
        }
        return jars;
    }


}
