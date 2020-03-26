/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mx.app.component.view;

import com.mx.app.component.BoxTable;
import com.mx.app.data.FileItem;
import com.mx.app.event.AppEventBus;
import com.mx.app.logic.*;
import com.vaadin.ui.*;

/**
 *
 * @author Edrd
 */
//public class FileListLayout extends VerticalLayout implements View {
public class FileListLayout extends VerticalLayout {

//    private final Components component = new Components();
//    private Grid<Item> table;
    private final Button downloadInvisibleButton = new Button();

    private final FileLogic viewLogicFile;
    private final DirectoryLogic viewLogicDirectory;

//    private final String COL_TAMANIO = "tamanio";
//    private final String COL_MODIFICADO = "modificado";
//    private static final Set<Column<Item, ?>> COLLAPSIBLE_COLUMNS = new LinkedHashSet<>();
//    private final String[] DEFAULT_COLLAPSIBLE = {COL_TAMANIO, COL_MODIFICADO};
    public FileListLayout(FileLogic mosaicoFileLogic, DirectoryLogic mosaicoDirectoryLogic, FileItem file) {
        this.viewLogicFile = mosaicoFileLogic;
        this.viewLogicDirectory = mosaicoDirectoryLogic;

        setSizeFull();
        addStyleName("listView");
        AppEventBus.register(this);   //NECESARIO PARA CONOCER LA ORIENTACION Y RESIZE DEL BROWSER

//        table = buildTable(file);
        BoxTable table = new BoxTable(file, viewLogicFile, viewLogicDirectory, downloadInvisibleButton);
//        addComponentsAndExpand(table);
        addComponent(table);
        setExpandRatio(table, 1); 
        //browserResized();
//        System.out.println("width-->" + Page.getCurrent().getBrowserWindowWidth());
//        System.out.println("height-->" + Page.getCurrent().getBrowserWindowHeight());

        //BUTTON PARA PODER DESCARGAR ARCHIVOS POR MEDIO DEL CONTEXT MENU
        downloadInvisibleButton.setId("DownloadButtonId");
        downloadInvisibleButton.addStyleName("InvisibleButton");
        addComponent(downloadInvisibleButton);
    }

    //METODO NECESARIO PARA CONOCER LA ORIENTACION Y RESIZE DEL BROWSER
//    @Override
//    public void detach() {
//        super.detach();
//        // A new instance of TransactionsView is created every time it's
//        // navigated to so we'll need to clean up references to it on detach.
//        AppEventBus.unregister(this);
//    }
//    private Grid<Item> buildTable(Item file) {
//        table = new Grid<>();
//        table.setDataProvider(crearContenedor(file));
//        table.setSizeFull();
//        addColumns();
//        table.setRowHeight(44);
//        table.setColumnReorderingAllowed(false);
//        table.addItemClickListener((event) -> itemClicked(event));
////        {
////            if (event.getItem() != null) {
////                Item file_ = new Item(event.getItem().getPath());
////                if (event.getMouseEventDetails().isDoubleClick()) {
////                    if (file_.isDirectory()) {
////                        viewLogicFile.cleanAndDisplay(file_);
////                    } else if (file_.isFile()) {
////                        Notification.show("Ver archivo: " + file_.getName());
//////                        Window w = new ViewerWindow(file);;
//////                        UI.getCurrent().addWindow(w);
//////                        w.focus();
////                    }
////                }
////            }
////        });
//
////         HeaderRow groupingHeader = table.prependHeaderRow();
////        groupingHeader.join("icon","nombre").setText("Person");
//        return table;
//    }
//    private void itemClicked(ItemClick<Item> event) {
//         Item itemId = event.getItem();
//        if (itemId != null) {
////                Item file_ = new Item(itemId);
//                if (event.getMouseEventDetails().isDoubleClick()) {
//                    if (itemId.isDirectory()) {
//                        viewLogicFile.cleanAndDisplay(itemId);
//                    } else if (itemId.isFile()) {
//                        Notification.show("Ver archivo: " + itemId.getName());
////                        Window w = new ViewerWindow(file);;
////                        UI.getCurrent().addWindow(w);
////                        w.focus();
//                    }
//                }
//            }
//    }
//    private ListDataProvider<Item> crearContenedor(Item directory) {
//        ListDataProvider<Item> dataProvider = DataProvider.ofCollection(component.directoryContents(directory));
//
////        dataProvider.setSortOrder(File::getName,
////                SortDirection.ASCENDING);
////
//        return dataProvider;
//    }
//    private void addColumns() {
//
//        COLLAPSIBLE_COLUMNS
//                .add(table.addColumn(file -> new ItemProperty(file).buildIcon(false), new ComponentRenderer())
//                        .setCaption(""));
//        COLLAPSIBLE_COLUMNS
//                        .add(table.addColumn(file -> file.getName())
//                        .setCaption("Nombre")
//                        .setExpandRatio(6));
////                .add(table.addColumn(file_ -> new Label(file_.getName()), new ComponentRenderer()).setCaption("Nombre"));     // OTRA OPCION
////                .add(table.addColumn(File::getName).setCaption("Nombre"));                                                                                    // OTRA OPCION
//        COLLAPSIBLE_COLUMNS
//                //                .add(table.addColumn(file_ -> itemProperty.getNumberOfElementsAndFileSize()).setCaption("Tamaño"));   // OTRA OPCION
//                .add(table.addColumn(file -> new ItemProperty(file).getNumberOfElementsAndFileSize())
//                        .setCaption("Tamaño")
//                        .setId(COL_TAMANIO)
//                        .setExpandRatio(2));
//        COLLAPSIBLE_COLUMNS
//                .add(table.addColumn(file -> new ItemProperty(file).getAtributos())
//                        .setCaption("Modificado")
//                        .setId(COL_MODIFICADO)
//                        .setExpandRatio(2)
//                        .setStyleGenerator(item -> "v-align-right"));
//        COLLAPSIBLE_COLUMNS // BUTTONCONTEXTMENU
//                .add(table.addColumn(file -> new ButtonContextMenu(downloadInvisibleButton, file, viewLogicFile, viewLogicDirectory), new ComponentRenderer())
//                        .setCaption("")
//                        .setStyleGenerator(item -> "v-align-center"));
//
//    }
//    private boolean defaultColumnsVisible() {
//        boolean result = true;
//        for (Column<File, ?> column : COLLAPSIBLE_COLUMNS) {
//            if (column.isHidden() == Page.getCurrent()
//                    .getBrowserWindowWidth() < 800) {
//                result = false;
//            }
//        }
//        return result;
//    }
//     @Subscribe
//    public void browserResized(final BrowserResizeEvent event) {
//        browserResized();
//    }
//    private void browserResized() {
//        //Some columns are collapsed when browser window width gets small
//        // enough to make the table fit better.
//        System.out.println("Entra a browserResized: " + Page.getCurrent().getBrowserWindowWidth());
//        List<String> lstColapsibleColumns = Arrays.asList(DEFAULT_COLLAPSIBLE);
////        if (defaultColumnsVisible()) {
//        for (Column<Item, ?> column : COLLAPSIBLE_COLUMNS) {
//            if (lstColapsibleColumns.contains(column.getId())) {
//                column.setHidden(Page.getCurrent().getBrowserWindowWidth() < 800);
//            }
//        }
////        }
//    }
//    @Override
//    public void enter(final ViewChangeListener.ViewChangeEvent event) {
//    }
}
