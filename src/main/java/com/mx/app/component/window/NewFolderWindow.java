/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mx.app.component.window;

import com.mx.app.data.FileItem;
import com.mx.app.logic.DirectoryLogic;
import com.mx.app.utils.Components;
import com.vaadin.event.*;
import com.vaadin.server.*;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;
import org.apache.commons.lang.StringUtils;
//import org.apache.commons.lang3.StringUtils;

/**
 *
 * @author Edrd
 */
public class NewFolderWindow extends Window {

    private final VerticalLayout content;
    private VerticalLayout body;
    private HorizontalLayout footer;
    private TextField txtNameFolder;

    private Button btnCrear;

    private final Components component = new Components();
    private final DirectoryLogic viewLogicDirectory;

    private final TabSheet detailsWrapper;

    public NewFolderWindow(DirectoryLogic moveCopyFileLogic, FileItem file) {
        viewLogicDirectory = moveCopyFileLogic;

        addStyleName("createfolder-window");
        Responsive.makeResponsive(this);

        setModal(true);
        setResizable(false);
        setClosable(true);
        setHeight(90.0f, Sizeable.Unit.PERCENTAGE);

        content = new VerticalLayout();
        content.setSizeFull();

        detailsWrapper = new TabSheet();
        detailsWrapper.setSizeFull();
        detailsWrapper.addStyleName(ValoTheme.TABSHEET_PADDED_TABBAR);
        detailsWrapper.addComponent(body(file));

        content.addComponent(detailsWrapper);
        content.setExpandRatio(detailsWrapper, 1.0f);
        content.addComponent(buildFooter(file));

        setContent(content);
    }

    private Component body(FileItem file) {
        body = new VerticalLayout();
        body.setCaption("Carpeta");
        body.setSizeFull();
        body.setSpacing(true);
        body.setMargin(true);

        txtNameFolder = new TextField();
        txtNameFolder.setWidth(100.0f, Sizeable.Unit.PERCENTAGE);
        txtNameFolder.setInputPrompt("Escriba el nombre de carpeta");
        txtNameFolder.setTextChangeEventMode(AbstractTextField.TextChangeEventMode.EAGER);          //EAGER, Para que evento no sea lento
        txtNameFolder.setTextChangeTimeout(200);                                  //Duración para iniciar el evento
        txtNameFolder.setImmediate(true);
        txtNameFolder.addTextChangeListener((FieldEvents.TextChangeEvent event) -> {
            btnCrear.setEnabled(StringUtils.isNotBlank(event.getText()));
        });
        //txtNameFolder.focus();

        body.addComponent(txtNameFolder);
        body.setComponentAlignment(txtNameFolder, Alignment.MIDDLE_CENTER);

        return body;
    }

    private Component buildFooter(FileItem file) {
        footer = new HorizontalLayout();
        footer.addStyleName(ValoTheme.WINDOW_BOTTOM_TOOLBAR);
        footer.setWidth(100.0f, Sizeable.Unit.PERCENTAGE);

        btnCrear = Components.createButtonPrimary("Crear");
        btnCrear.setEnabled(false);
        btnCrear.addClickListener((Button.ClickEvent event) -> {
            viewLogicDirectory.createFolder(file.toPath(), txtNameFolder.getValue().trim());
            close();
        });
        btnCrear.setClickShortcut(ShortcutAction.KeyCode.ENTER, null);

        footer.addComponent(btnCrear);
        footer.setComponentAlignment(btnCrear, Alignment.TOP_RIGHT);

        return footer;
    }

}
