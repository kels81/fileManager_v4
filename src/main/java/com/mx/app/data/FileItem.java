/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mx.app.data;

import java.io.*;
import java.nio.file.Path;
import java.util.*;
import org.apache.commons.io.*;
import org.apache.commons.io.filefilter.*;

/**
 *
 * @author ecortesh
 */
public class FileItem {

    private final File file;
    private String path;

    public FileItem(File file) {
        this.file = file;
    }

    public FileItem(String pathname) {
        this.file = new File(pathname);
    }

    public String getName() {
        return file.getName();
    }

    public String getExtension() {
        return FilenameUtils.getExtension(file.getPath()).toLowerCase();
    }

    public String getPath() {
        return file.getPath();
    }

    public Path toPath() {
        return file.toPath();
    }

    public String getParent() {
        return file.getParent();
    }

    public boolean isDirectory() {
        return file.isDirectory();
    }

    public boolean isFile() {
        return file.isFile();
    }

    public boolean isEmpty() {
        boolean empty = file.listFiles().length == 0;
        return empty;
    }

    public long length() {
        return file.length();
    }

    public String[] list() {
        return file.list();
    }

    public boolean renameTo(FileItem destino) {
        return file.renameTo(new File(destino.getPath()));
    }

    public List<FileItem> getContentDirectory() {
        // ARRAY QUE VA A ACONTENER TODOS LOS ARCHIVOS ORDENADOS POR TIPO Y ALFABETICAMENTE
        List<FileItem> allDocsLst = new ArrayList<>();
        List<FileItem> fileLst = new ArrayList<>();
        List<FileItem> directoryLst = new ArrayList<>();
        for (FileItem file : new FileItem(file).getList()) {
            if (file.isDirectory()) {
                directoryLst.add(file);
                //directoryContents(file);   //para conocer los archivos de las subcarpetas
            } else {
                fileLst.add(file);
            }
        }
        allDocsLst.addAll(directoryLst);
        allDocsLst.addAll(fileLst);

        return allDocsLst;
    }

    public List<FileItem> getListDirectories() {
        List<FileItem> list = new ArrayList<>();
        for (File f : file.listFiles((FileFilter) DirectoryFileFilter.DIRECTORY)) {
            list.add(new FileItem(f));
        }
        return list;
    }

    public List<FileItem> getList() {
        List<FileItem> list = new ArrayList<>();
        if (file.isDirectory()) {
            for (File f : file.listFiles()) {
                list.add(new FileItem(f));
            }
        }
        return list;
    }

    public boolean hasChildrensDirectories() {
        List<FileItem> list = new ArrayList<>();
        for (File f : file.listFiles((FileFilter) DirectoryFileFilter.DIRECTORY)) {
            list.add(new FileItem(f));
        }
        return !list.isEmpty();
    }
    
    @Override
    public String toString() {
//        return "Item{" + "file=" + file + '}';
        return file.toString();
    }

}
