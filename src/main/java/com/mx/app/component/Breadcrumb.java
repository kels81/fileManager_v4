/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mx.app.component;

import com.mx.app.data.FileItem;
import com.mx.app.event.AppCleanAndDisplay;
import com.mx.app.utils.*;
import com.vaadin.server.FontAwesome;
import com.vaadin.ui.*;
import java.util.*;

/**
 *
 * @author ecortesh
 */
public final class Breadcrumb extends HorizontalLayout {

    private Button btnPath;
    private MenuBar btnDirectories;
    private Label lblArrow;
    private final AppCleanAndDisplay appCleanDisplay;

    public Breadcrumb(FileItem itemDir, AppCleanAndDisplay cleanDisplay) {
        this.appCleanDisplay = cleanDisplay;

        List<FileItem> listDirectories = getListDirectories(itemDir);

        if (listDirectories.size() > 1) {
            createShortWay(listDirectories);
        } else {
            createNormalWay(listDirectories);
        }

    }

    private void createNormalWay(List<FileItem> listDirectories) {
        int size = listDirectories.size();
        int count = 1;
        for (FileItem directory : listDirectories) {
            btnPath = createButtonPath(directory, size, count);
            addComponent(btnPath);

            if (count != size) {
                lblArrow = Components.createLabelArrow();

                addComponent(lblArrow);
                setComponentAlignment(lblArrow, Alignment.MIDDLE_CENTER);
            }
            count++;
        }
    }

    private void createShortWay(List<FileItem> listDirectories) {
        Collections.reverse(listDirectories);
        
        int lstSize = listDirectories.size();
        btnDirectories = createMenuButton(listDirectories);
        addComponent(btnDirectories);

        lblArrow = Components.createLabelArrow();
        addComponent(lblArrow);
        setComponentAlignment(lblArrow, Alignment.MIDDLE_CENTER);

        //btnPath = createButtonPath(listDirectories.get(lstSize - 1), lstSize, lstSize);
        btnPath = createButtonPath(listDirectories.get(0), lstSize, lstSize);   //TO REVERSE COLLECTION
        addComponent(btnPath);
    }

    private List<FileItem> getListDirectories(FileItem directory) {
        List<FileItem> listDirectories = new ArrayList<>();
        String[] arrayDirectories = directory.getPath().split(Constantes.SEPARADOR);
        int idxArchivos = Arrays.asList(arrayDirectories).indexOf(Constantes.ROOT_DIRECTORY);
        String[] newArrayDirectories = Arrays.copyOfRange(arrayDirectories, idxArchivos, arrayDirectories.length);
        StringBuilder newPath = new StringBuilder();
        int i = 1;
        for (String dirName : newArrayDirectories) {
//            int fin = directory.getPath().indexOf(dirName);
//            listDirectories.add(new File(directory.getPath().substring(0, fin + dirName.length())));
            newPath.append(dirName);
            if (i != newArrayDirectories.length) {
                newPath.append("\\");
            }
            listDirectories.add(new FileItem(Constantes.PATH_BASE + newPath.toString()));
            i++;
        }
        System.out.println("newPath = " + newPath.toString());
        return listDirectories;
    }

    public Button createButtonPath(FileItem itemDir, int listDirectoriesSize, int count) {
        Button btn = Components.createButtonPath(itemDir.getName());
        btn.setEnabled((count != listDirectoriesSize));     //PARA QUE NO TENGA CLICK  EL BUTTON
        btn.addStyleName((count == listDirectoriesSize ? MetroTheme.BUTTON_LABEL_BREADCRUMB : MetroTheme.THEME_NULL));
        btn.addClickListener((event) -> showContentDirectory(itemDir));

        return btn;
    }

    private MenuBar createMenuButton(List<FileItem> listDirectories) {
        MenuBar menuBtn = Components.createMenuButtonPath();
        MenuBar.MenuItem dropdown = menuBtn.addItem("", FontAwesome.FOLDER_O, null);

        //listDirectories = listDirectories.subList(0, (listDirectories.size() - 1));
        listDirectories = listDirectories.subList(1, (listDirectories.size()));     //TO REVERSE COLLECTION

        for (FileItem directory : listDirectories) {
            dropdown.addItem(directory.getName(), (event) -> showContentDirectory(directory));
        }

        return menuBtn;
    }

    private void showContentDirectory(FileItem directory) {
        appCleanDisplay.showContentDirectory(directory);
    }
}
