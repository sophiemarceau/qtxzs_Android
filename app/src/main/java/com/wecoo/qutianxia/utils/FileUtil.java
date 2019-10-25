package com.wecoo.qutianxia.utils;

import android.text.TextUtils;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DecimalFormat;

/**
 * Created by mwl on 2016/10/19.
 * 文件管理
 */
public class FileUtil {

    private static FileUtil instance;

    public static FileUtil getInstance() {
        if (instance == null) {
            instance = new FileUtil();
        }
        return instance;
    }

    /***
     * 获取文件大小
     ***/
    public long getFileSizes(File f) throws Exception {
        long s = 0;
        if (f.exists()) {
            FileInputStream fis = null;
            fis = new FileInputStream(f);
            s = fis.available();
            fis.close();
        } else {
            f.createNewFile();
        }
        return s;
    }

    /***
     * 获取文件夹大小
     ***/
    public long getFolderSize(String path) throws Exception {
        long size = 0;
        if (path != null) {
            File file = new File(path);
            if (file.exists()) {
                size = getFolderSize(file);
            }
        }
        return size;
    }

    /***
     * 获取文件夹大小
     ***/
    public long getFolderSize(File file) throws Exception {
        long size = 0;
        File[] fileList = file.listFiles();
        if (fileList != null) {
            for (int i = 0; i < fileList.length; i++) {
                if (fileList[i].isDirectory()) {
                    size = size + getFolderSize(fileList[i]);
                } else {
                    size = size + fileList[i].length();
                }
            }
        }
        return size;
    }

    /***
     * (b/kb/mb/gb)
     ***/
    public String formatFileSize(long size) {//
        if (size == 0) return "0B";
        DecimalFormat df = new DecimalFormat("#.00");
        String fileSizeString = "0";
        if (size < 1024) {
            fileSizeString = df.format((double) size) + "B";
        } else if (size < 1048576) {
            fileSizeString = df.format((double) size / 1024) + "KB";
        } else if (size < 1073741824) {
            fileSizeString = df.format((double) size / 1048576) + "M";
        } else {
            fileSizeString = df.format((double) size / 1073741824) + "G";
        }
        return fileSizeString;
    }

    // 获取文件夹下文件数量
    public static long getDirectoryCount(File f) {//
        long size = 0;
        File flist[] = f.listFiles();
        size = flist.length;
        for (int i = 0; i < flist.length; i++) {
            if (flist[i].isDirectory()) {
                size = size + getDirectoryCount(flist[i]);
                size--;
            }
        }
        return size;
    }

    public boolean deleteFolderFiles(String filePath, int count) throws IOException {
        boolean ret = false;
        if (!TextUtils.isEmpty(filePath)) {
            File file = new File(filePath);
            if (file.isDirectory()) {//
                File files[] = file.listFiles();
                int size = count < files.length ? count : files.length;
                for (int i = 0; i < size; i++) {
                    deleteFolderFiles(files[i].getAbsolutePath(), true);
                }
            }
        }
        return ret;
    }

    public boolean deleteFolderFiles(String filePath, boolean deleteThisPath) throws IOException {
        boolean ret = false;
        if (!TextUtils.isEmpty(filePath)) {
            File file = new File(filePath);
            if (file.isDirectory()) {
                File files[] = file.listFiles();
                for (int i = 0; i < files.length; i++) {
                    deleteFolderFiles(files[i].getAbsolutePath(), true);
                }
            }
            if (deleteThisPath) {
                if (!file.isDirectory()) {
                    ret = file.delete();
                } else {// Ŀ¼
                    if (file.listFiles().length == 0) {
                        ret = file.delete();
                    }
                }
            }
        }
        return ret;
    }

    public static void RecursionDeleteFile(File file) {
        if (file.isFile()) {
            file.delete();
            return;
        }
        if (file.isDirectory()) {
            File[] childFile = file.listFiles();
            if (childFile == null || childFile.length == 0) {
                file.delete();
                return;
            }
            for (File f : childFile) {
                RecursionDeleteFile(f);
            }
            file.delete();
        }
    }

    public static File getFileByPath(String path) {
        if (path != null) {
            File file = new File(path);
            if ((file != null) && (file.exists())) {
                return file;
            }
            return null;
        }
        return null;
    }

    public static boolean deleteFile(String path) {
        if (path == null) {
            return false;
        }
        File file = new File(path);
        if (file.exists()) {
            LogUtil.d("FileUtils", "path: " + path);
            return file.delete();
        }
        return false;
    }

    // lcy 20131115
    public boolean deleteFolder(String sPath) {
        try {
            boolean flag = false;
            File file = new File(sPath);
            if (!file.exists()) {
                return flag;
            } else {
                if (file.isFile()) {
                    return deleteFile(sPath);
                } else {
                    return deleteDirectory(sPath);
                }
            }

        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 删除目录（文件夹）以及目录下的文件
     */
    public static boolean deleteDirectory(String sPath) {
        if (!sPath.endsWith(File.separator)) {
            sPath = sPath + File.separator;
        }
        File dirFile = new File(sPath);
        if (!dirFile.exists() || !dirFile.isDirectory()) {
            return false;
        }
        boolean flag = true;
        File[] files = dirFile.listFiles();
        for (int i = 0; i < files.length; i++) {
            if (files[i].isFile()) {
                flag = deleteFile(files[i].getAbsolutePath());
                if (!flag)
                    break;
            } else {
                flag = deleteDirectory(files[i].getAbsolutePath());
                if (!flag)
                    break;
            }
        }
        if (!flag) {
            return false;
        }
        return dirFile.delete();
    }

    // 复制文件
    public static void copyFile(File sourceFile, File targetFile) throws IOException {
        BufferedInputStream inBuff = null;
        BufferedOutputStream outBuff = null;
        try {
            // 新建文件输入流并对它进行缓冲
            inBuff = new BufferedInputStream(new FileInputStream(sourceFile));
            // 新建文件输出流并对它进行缓冲
            outBuff = new BufferedOutputStream(new FileOutputStream(targetFile));
            byte[] b = new byte[1024];
            int len;
            while ((len = inBuff.read(b)) != -1) {
                outBuff.write(b, 0, len);
            }
            outBuff.flush();
        } finally {
            if (inBuff != null)
                inBuff.close();
            if (outBuff != null)
                outBuff.close();
        }
    }

}
