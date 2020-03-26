package com.mx.app.view.content;

import com.mx.app.component.*;
import com.mx.app.component.view.*;
import com.mx.app.component.window.NewFolderWindow;
import com.mx.app.data.FileItem;
import com.mx.app.logic.*;
import com.mx.app.utils.*;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.*;
import com.vaadin.ui.*;
import pl.exsio.plupload.*;

@SuppressWarnings("serial")
//public final class ContentView extends CssLayout implements View {
public final class ContentView extends VerticalLayout implements View {

    public static final String VIEW_NAME = "archivos";

    private final FileItem origenPath;
    private HorizontalLayout header;
    private HorizontalLayout breadcrumb;
    //private HorizontalLayout mainButtons;

    private Component directoryContent;
    private Button btnListView;
    private Button btnGridView;

    private final DirectoryLogic viewLogicDirectory = new DirectoryLogic(this);
    private final FileLogic viewLogicFile = new FileLogic(this);

    private Boolean selected = true;
    private Boolean isVisible = true;

    private UploadLayout sideBar;

    public ContentView() {
        this.origenPath = new FileItem(Constantes.ALL_FILES);

        setSizeFull();
        addStyleName("content");
        setMargin(false);
        setSpacing(false);

        cleanAndDisplay(origenPath);

        Responsive.makeResponsive(this);
//        Page.getCurrent().getStyles().add(".v-verticallayout {border: 1px solid blue;} .v-verticallayout .v-slot {border: 1px solid red;}");
//        Page.getCurrent().getStyles().add(".v-horizontallayout {border: 1px solid green;} .v-horizontallayout .v-slot {border: 1px solid violet;}");
    }

//    private Component buildHeader(FileItem directory) {
//        header = new HorizontalLayout();
//        header.setWidth(100.0f, Sizeable.Unit.PERCENTAGE);
//        header.addStyleName("viewbar");
//
//        Component mainButtons = buildMainButtons(directory);
//
//        header.addComponents(mainButtons);
//        header.setComponentAlignment(mainButtons, Alignment.MIDDLE_RIGHT);
//
//        return header;
//    }

    private Component buildBreadcrumb(FileItem directory) {
        breadcrumb = new HorizontalLayout();
        breadcrumb.setWidth(100.0f, Sizeable.Unit.PERCENTAGE);
        breadcrumb.addStyleName("viewbar");

        Breadcrumb pathDirectory = new Breadcrumb(directory, this::cleanAndDisplay);
        Component mainButtons = buildMainButtons(directory);

        breadcrumb.addComponents(pathDirectory, mainButtons);
        //breadcrumb.setExpandRatio(mainButtons, 1.0f);
        breadcrumb.setComponentAlignment(mainButtons, Alignment.MIDDLE_RIGHT);

        return breadcrumb;
    }

    private Component buildMainButtons(FileItem directory) {
        HorizontalLayout mainButtons = new HorizontalLayout();
        mainButtons.setSpacing(true);

        // CARGAR ARCHIVO
//        Plupload btnUploader = viewLogicFile.uploadFile(directory);
        Plupload btnUploader = Components.createPluploadPrimary("Cargar");
        //btnUploader.addFilter(new PluploadFilter("music", "mp3, flac"));
        btnUploader.setPreventDuplicates(true);
        btnUploader.setUploadPath(directory.getPath());
        btnUploader.setMaxFileSize("15mb");
        btnUploader.addClickListener((event) -> {
            viewLogicFile.uploadFile(btnUploader);
        });

        Button btnAddDirectory = Components.createButtonAddDirectory("Carpeta");
        btnAddDirectory.addClickListener((event) -> {
            NewFolderWindow newFolderWindow = new NewFolderWindow(viewLogicDirectory, directory);
            Window w = newFolderWindow;
            UI.getCurrent().addWindow(w);
            w.focus();
        });

        Component typesViews = buildViewsButtons(directory);

        mainButtons.addComponents(btnUploader, btnAddDirectory, typesViews);
        //mainButtons.setComponentAlignment(typesViews, Alignment.MIDDLE_CENTER);

        return mainButtons;
    }

    private Component buildViewsButtons(FileItem directory) {
        HorizontalLayout viewsButtons = new HorizontalLayout();

        btnListView = Components.createButtonIconTiny();
        //btnListView = Components.createButtonIconNormal();
        btnListView.setId("listView");
        btnListView.setIcon(FontAwesome.BARS);
        //btnListView.addStyleName(setStyle(selected));
        //btnListView.setEnabled(selected);
        btnListView.setDescription("Vista Lista");
        btnListView.setVisible(isVisible);
        btnListView.addClickListener((Button.ClickEvent event) -> {
            selected = false;
            isVisible = false;
            cleanAndDisplay(directory);
        });

        btnGridView = Components.createButtonIconTiny();
        //btnGridView = Components.createButtonIconNormal();
        btnGridView.setIcon(FontAwesome.TH_LARGE);
        //btnGridView.addStyleName(setStyle(!selected));
        //btnGridView.setEnabled(!selected);
        btnGridView.setDescription("Vista Grid");
        btnGridView.setVisible(!isVisible);
        btnGridView.addClickListener((Button.ClickEvent event) -> {
            selected = true;
            isVisible = true;
            cleanAndDisplay(directory);
        });

        viewsButtons.addComponents(btnListView, btnGridView);

        //viewBar.setComponentAlignment(viewsButtons, Alignment.MIDDLE_RIGHT);
        return viewsButtons;
    }

    private Component selectView(Boolean selected, FileItem pathDirectory) {
        Component viewSelected;
        if (selected) {
            viewSelected = new FileMosaicoLayout(viewLogicFile, viewLogicDirectory, pathDirectory);
        } else {
            viewSelected = new FileListLayout(viewLogicFile, viewLogicDirectory, pathDirectory);
        }

        return viewSelected;
    }

    public void cleanAndDisplay(FileItem directory) {
        removeAllComponents();
//        addComponents(buildHeader(directory), buildBreadcrumb(directory));
        addComponents(buildBreadcrumb(directory)); 
        if (directory.isEmpty()) {
            Label message = Components.createLabelEmptyDirectory();
            addComponent(message);
            setComponentAlignment(message, Alignment.MIDDLE_CENTER);
            setExpandRatio(message, 1);
        } else {
            directoryContent = selectView(selected, directory);
            addComponent(directoryContent);
            setExpandRatio(directoryContent, 1);
        }
    }

//    private String setStyle(Boolean selected) {
//        return selected ? "borderButton" : "noBorderButton";
//    }
    @Override
    public void enter(final ViewChangeEvent event) {
    }
}
