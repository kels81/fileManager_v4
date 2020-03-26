/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mx.app.component;

import com.google.common.eventbus.Subscribe;
import com.mx.app.data.FileItem;
import com.mx.app.event.AppEvent.BrowserResizeEvent;
import com.mx.app.logic.*;
import com.mx.app.utils.*;
import com.vaadin.data.Item;
import com.vaadin.data.util.IndexedContainer;
import com.vaadin.event.ItemClickEvent;
import com.vaadin.server.Page;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;
import java.util.*;

/**
 *
 * @author ecortesh
 */
public class BoxTable extends Table {

    private FileItem file;
    private IndexedContainer idxCont;
    private final Button downloadInvisibleButton;

    private final FileLogic viewLogicFile;
    private final DirectoryLogic viewLogicDirectory;

    private final String COL_FILE = "file";
    private final String COL_ICON = "icon";
    private final String COL_NOMBRE = "nombre";
    private final String COL_TAMANIO = "tamanio";
    private final String COL_MODIFICADO = "modificado";
    private final String COL_CONTEXT_MENU = "contextMenu";

    private final Object[] COLUMNS_VISIBLES = {COL_ICON, COL_NOMBRE, COL_TAMANIO, COL_MODIFICADO, COL_CONTEXT_MENU};
    private final Object[] COLUMNS_VISIBLES_MOBILES = {COL_ICON, COL_NOMBRE, COL_CONTEXT_MENU};
    private final String[] COLUMNS_HEADERS = {"", "Nombre", "Tamaño", "Modificado", ""};
    private final String[] DEFAULT_COLLAPSIBLE = {COL_TAMANIO, COL_MODIFICADO};

    public BoxTable(FileItem file, FileLogic tableFileLogic, DirectoryLogic tableDirectoryLogic, Button downloadInvisibleButton) {
        super();
        System.out.println("file = " + file.toString());
        this.file = file;
        this.viewLogicFile = tableFileLogic;
        this.viewLogicDirectory = tableDirectoryLogic;
        this.downloadInvisibleButton = downloadInvisibleButton;

        setContainerDataSource(crearContenedor(file));
        setSizeFull();
        setImmediate(true);
        setSelectable(true);
        addStyleName(ValoTheme.TABLE_BORDERLESS);
        addStyleName(ValoTheme.TABLE_NO_STRIPES);
        addStyleName(ValoTheme.TABLE_NO_VERTICAL_LINES);
        addStyleName(ValoTheme.TABLE_SMALL);
        addStyleName("noselect");

        setVisibleColumns(COLUMNS_VISIBLES);
        setColumnHeaders(COLUMNS_HEADERS);

        setSortEnabled(false);
        setColumnAlignment(COL_MODIFICADO, Align.RIGHT);
        setColumnAlignment(COL_CONTEXT_MENU, Align.CENTER);

        //PARA HACER RESPONSIVO LA TABLA
        //setColumnCollapsingAllowed(false);
        //setColumnCollapsible(COL_NOMBRE, false);
        setColumnExpandRatio(COL_NOMBRE, 0.50f);
        setColumnExpandRatio(COL_MODIFICADO, 0.20f);
        setColumnExpandRatio(COL_TAMANIO, 0.20f);
        setColumnExpandRatio(COL_CONTEXT_MENU, 0.10f);
        refreshRowCache();
        //setRowHeaderMode(Table.RowHeaderMode.INDEX);          //PARA ENUMERAR LAS FILAS

        //setContainerDataSource(new BeanItemContainer<>(crearContenedorFile(file)));
        addItemClickListener((event) -> itemClicked(event));

        browserResized();

    }

    private void itemClicked(ItemClickEvent event) {
        if (event.getItem() != null) {
            FileItem file = new FileItem(event.getItem().getItemProperty(COL_FILE).getValue().toString());
            if (event.isDoubleClick()) {
                //table.select(selectedItemInTheRow);
                if (file.isDirectory()) {
                    viewLogicDirectory.cleanAndDisplay(file);
                } else if (file.isFile()) {
                    Notification.show("Ver archivo: " + file.getName());
//                        Window w = new ViewerWindow(file);;
//                        UI.getCurrent().addWindow(w);
//                        w.focus();
                }
            }
        }
    }

    private IndexedContainer crearContenedor(FileItem directory) {
        idxCont = new IndexedContainer();

        idxCont.addContainerProperty(COL_ICON, Image.class, "");
        idxCont.addContainerProperty(COL_NOMBRE, Label.class, "");
        idxCont.addContainerProperty(COL_TAMANIO, String.class, "");
        idxCont.addContainerProperty(COL_MODIFICADO, String.class, "");
        idxCont.addContainerProperty(COL_CONTEXT_MENU, ButtonContextMenu.class, "");
        idxCont.addContainerProperty(COL_FILE, String.class, "");

        List<FileItem> files = directory.getContentDirectory();
        if (!files.isEmpty()) {
            for (FileItem fileRow : files) {
                this.file = fileRow;

                Item item = idxCont.getItem(idxCont.addItem());
                item.getItemProperty(COL_ICON).setValue(new ItemProperty(file).buildIcon(false));
                item.getItemProperty(COL_NOMBRE).setValue(new ItemProperty(file).getFileName());
                item.getItemProperty(COL_TAMANIO).setValue(new ItemProperty(file).getNumberOfElementsAndFileSize());
                item.getItemProperty(COL_MODIFICADO).setValue(new ItemProperty(file).getAtributos());
                item.getItemProperty(COL_CONTEXT_MENU).setValue(new ButtonContextMenu(downloadInvisibleButton, file, viewLogicFile, viewLogicDirectory));
                item.getItemProperty(COL_FILE).setValue(file.getPath());
            }
        }

        return idxCont;
    }

    @Subscribe
    public void browserResized(final BrowserResizeEvent event) {
        browserResized();
    }

    private boolean defaultColumnsVisible() {
        boolean result = true;
        for (String propertyId : DEFAULT_COLLAPSIBLE) {
            if (isColumnCollapsed(propertyId) == Page.getCurrent()
                    .getBrowserWindowWidth() < 800) {
                result = false;
            }
        }
        return result;
    }

    private void browserResized() {
        // Some columns are collapsed when browser window width gets small
        // enough to make the table fit better.
        if (Page.getCurrent().getBrowserWindowWidth() < 800) {
            setVisibleColumns(COLUMNS_VISIBLES_MOBILES);
        } else {
            setVisibleColumns(COLUMNS_VISIBLES);
        }
    }

}
