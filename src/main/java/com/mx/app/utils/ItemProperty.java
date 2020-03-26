/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mx.app.utils;

import com.mx.app.data.FileItem;
import com.vaadin.server.*;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;
import java.io.*;
import java.nio.file.*;
import java.nio.file.attribute.*;
import java.text.SimpleDateFormat;
import java.util.*;
import org.apache.commons.io.*;
import org.apache.commons.lang.ArrayUtils;

/**
 *
 * @author ecortesh
 */
public class ItemProperty {

    private final FileItem file;
    private ThemeResource iconResource;
    private Image icon;

    public ItemProperty(FileItem file) {
        this.file = file;
    }

    public Image buildIcon(boolean isGridLayout) {
        icon = new Image(null, getIconExtension());
        icon.setWidth(isGridLayout ? (file.isDirectory() ? 43.0f : 40.0f)
                : (file.isDirectory() ? 35.0f : 31.0f), Sizeable.Unit.PIXELS);
        icon.setHeight(isGridLayout ? (file.isDirectory() ? 43.0f : 49.0f)
                : (file.isDirectory() ? 33.0f : 39.0f), Sizeable.Unit.PIXELS);
        return icon;
    }

    public Label getFileName() {
        Label lblName = new Label(file.getName());
        lblName.setSizeFull();
        lblName.addStyleName("labelName");
        lblName.addStyleName("noselect");
        lblName.addStyleName(ValoTheme.LABEL_BOLD);

        return lblName;
    }

    public String getNumberOfElementsAndFileSize() {
        long fileSize = file.length();
        String fileSizeDisplay = FileUtils.byteCountToDisplaySize(fileSize);
        String elementos = (file.isDirectory()
                ? String.valueOf(file.list().length == 0
                        ? "" : file.list().length) + (file.list().length > 1
                ? " elementos" : file.list().length == 0
                ? "vacío" : " elemento")
                : fileSizeDisplay);
        return elementos;
    }

    public Label getLabelNumberOfElementsAndFileSize() {
        String label = getNumberOfElementsAndFileSize();
        Label lblNumberOfElementsAndFileSize = new Label(label);
        lblNumberOfElementsAndFileSize.addStyleName("labelInfo");
        lblNumberOfElementsAndFileSize.addStyleName("noselect");
        lblNumberOfElementsAndFileSize.addStyleName(ValoTheme.LABEL_TINY);

        return lblNumberOfElementsAndFileSize;
    }

    public String getAtributos() {
        String fechaCreacion = "";
        try {
            BasicFileAttributes attr = Files.readAttributes(file.toPath(), BasicFileAttributes.class);
            FileTime date = attr.creationTime();
            SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy   hh:mm:ss a");
            fechaCreacion = df.format(date.toMillis());
        } catch (IOException ex) {
        }
        return fechaCreacion;
    }

    private ThemeResource getIconExtension() {
//        String extension = FilenameUtils.getExtension(file.getPath()).toLowerCase();
        if (file.isDirectory()) {
            iconResource = new ThemeResource("img/file_manager/folder_" + (file.list().length == 0 ? "empty" : "full") + ".png");
        } else {
            //documento
            //iconResource = new ThemeResource("img/file_manager/" + extension + ".png");
            iconResource = findExtension(file.getExtension());
        }
        return iconResource;
    }

    private ThemeResource findExtension(String extension) {
        String formato = "desconocido";

        List<String[]> allFileFormats = new ArrayList<>();
        for (FileFormats fileFormats : FileFormats.values()) {
            allFileFormats.add(fileFormats.getArrayFileFormats());
        }

        for (String[] array : allFileFormats) {
            if (ArrayUtils.contains(array, extension)) {
                formato = FileFormats.values()[allFileFormats.indexOf(array)].toString().toLowerCase();
                break;
            }
        }
        return new ThemeResource("img/file_manager/" + formato + ".png");
    }

}
