/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mx.app.component.window;

import com.mx.app.data.FileItem;
import com.mx.app.logic.*;
import com.mx.app.utils.*;
import com.vaadin.data.Item;
import com.vaadin.data.util.*;
import com.vaadin.event.ItemClickEvent;
import com.vaadin.server.Responsive;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.*;
import com.vaadin.server.ThemeResource;
import com.vaadin.ui.themes.ValoTheme;
import java.io.File;
import java.nio.file.*;
import java.util.*;
import org.omg.CORBA.SystemException;

/**
 *
 * @author Edrd
 */
public class DirectoryTreeWindow extends Window {

    private final VerticalLayout content;
    private VerticalLayout body;
    private HorizontalLayout footer;
    private Tree tree;
    private HierarchicalContainer container;
    private final TabSheet detailsWrapper;

    private Label lblFileName;
    private final FileItem sourceFile;
    private FileItem targetDir;

    private Button btnCancelar;
    private Button btnMover;
    private Button btnCopiar;

    private final FileLogic viewLogicFile;
    private final DirectoryLogic viewLogicDirectory;

    public DirectoryTreeWindow(FileLogic moveCopyFileLogic, DirectoryLogic moveCopyDirectoryLogic, FileItem sourceFile) {
        this.viewLogicFile = moveCopyFileLogic;
        this.viewLogicDirectory = moveCopyDirectoryLogic;

        this.sourceFile = sourceFile;

        Responsive.makeResponsive(this);

        addStyleName("directorywindow");
        setModal(true);
        setResizable(false);
        setClosable(true);
        center();

        setHeight(90.0f, Unit.PERCENTAGE);

        content = new VerticalLayout();
        content.setSizeFull();
        content.setMargin(new MarginInfo(true, false, false, false));
        //content.setSpacing(false);

        detailsWrapper = new TabSheet();
        detailsWrapper.setSizeFull();
        detailsWrapper.addStyleName(ValoTheme.TABSHEET_PADDED_TABBAR);
        detailsWrapper.addTab(body());

        content.addComponent(detailsWrapper);
        content.setExpandRatio(detailsWrapper, 1.0f);
        content.addComponent(buildFooter());

        setContent(content);

        //Page.getCurrent().getStyles().add(".v-verticallayout {border: 1px solid blue;} .v-verticallayout .v-slot {border: 1px solid red;}");
    }

    private VerticalLayout body() {
        body = new VerticalLayout();
        //body.setCaption("Mover o Copiar" + "  \""+fileTo.getName()+"\"");
        body.setCaption("Mover o Copiar");
        body.setMargin(new MarginInfo(true, true, false, true));
        body.setSpacing(true);
        body.addComponent(buildFileName());
        body.addComponent(new Label("Selecciona folder destino:"));

        tree = buildTree();
        body.addComponent(tree);
        body.setExpandRatio(tree, 1.0f);

        return body;
    }

    private Label buildFileName() {
        lblFileName = new Label("\"" + sourceFile.getName() + "\"");
        lblFileName.addStyleName(ValoTheme.LABEL_COLORED);
        lblFileName.addStyleName(ValoTheme.LABEL_BOLD);
        return lblFileName;
    }

    private Tree buildTree() {
        tree = new Tree();
        tree.setSizeFull();
        tree.setContainerDataSource(createTreeContent(rootParent()));
        tree.setItemCaptionPropertyId("caption");
        tree.setItemIconPropertyId("icon");
        tree.setImmediate(true);
        tree.setSelectable(false);
        tree.addCollapseListener((event) -> doItemCollapsed(event));
        tree.addItemClickListener((event) -> doItemClicked(event));

        //ESTA VALIDACION ES PARA CUANDO SE QUIERE MOSTRAR CARPETA ROOT Y CARPETAS 
        //DENTRO DE ELLA 1ER NIVEL
        tree.expandItem(container.rootItemIds().iterator().next());     //PARA OBTENER EL PRIMER ELEMENTO DE LA COLECCION
        return tree;
    }

    private void doItemCollapsed(Tree.CollapseEvent event) {
        // Remove all children of the collapsing node
        Object children[] = tree.getChildren(event.getItemId()).toArray();
        for (Object childrenItem : children) {
            tree.collapseItemsRecursively(childrenItem);
        }
    }

    private void doItemClicked(ItemClickEvent event) {
        Object itemId = event.getItemId();
        targetDir = new FileItem(itemId.toString());
        tree.select(itemId);
        btnCopiar.setEnabled(true);
        btnMover.setEnabled(true);
        //VALIDACION PARA EXPANDIR NODE DESDE EL LABEL
        if (event.isDoubleClick()) {
            if (tree.isExpanded(itemId)) {
                tree.collapseItem(itemId);
            } else {
                tree.expandItem(itemId);
            }
        }
    }

    

    public HierarchicalContainer createTreeContent(List<FileItem> items) throws SystemException {

        container = new HierarchicalContainer();
        container.addContainerProperty("icon", ThemeResource.class, null);
        container.addContainerProperty("caption", String.class, null);
        container.addContainerProperty("path", String.class, null);
        container.addContainerProperty("type", String.class, null);

        new Object() {
            @SuppressWarnings("unchecked")
            public void put(List<FileItem> listChildrens, FileItem parent, HierarchicalContainer container) throws SystemException {

                for (FileItem item : listChildrens) {

                    container.addItem(item);
                    container.getItem(item).getItemProperty("caption").setValue(item.getName());
                    container.getItem(item).getItemProperty("icon").setValue(new ThemeResource("img/file_manager/folder_24.png"));
                    container.getItem(item).getItemProperty("path").setValue(item.getPath());
                    container.getItem(item).getItemProperty("type").setValue("folder");

                    Boolean hasChildrens = item.hasChildrensDirectories();

                    container.setChildrenAllowed(item, hasChildrens);
                    container.setParent(item, parent);

                    if (hasChildrens) {
                        List<FileItem> childrens = item.getListDirectories();
                        put(childrens, item, container);
                    }
                }
            }
        }.put(items, null, container);

        return container;
    }

    public List<FileItem> rootParent() {
        List<FileItem> list = new ArrayList<>();
        list.add(new FileItem(Constantes.ALL_FILES));
        return list;
    }

    private Component buildFooter() {
        footer = new HorizontalLayout();
        footer.setSpacing(true);
        footer.addStyleName(ValoTheme.WINDOW_BOTTOM_TOOLBAR);
        footer.setWidth(100.0f, Unit.PERCENTAGE);

        btnCancelar = Components.createButtonNormal("Cancelar");
        btnCancelar.addClickListener((Button.ClickEvent event) -> {
            close();
        });

        btnMover = Components.createButtonPrimary("Mover");
        btnMover.setEnabled(false);
        btnMover.addClickListener((Button.ClickEvent event) -> {
            Path source = Paths.get(sourceFile.getPath());
            Path target = Paths.get(targetDir.getPath().concat("\\").concat(sourceFile.getName()));

            if (sourceFile.isDirectory()) {
                viewLogicDirectory.moveDirectory(source, target, sourceFile);
            } else {
                //DOCUMENTO
                viewLogicFile.moveFile(source, target, sourceFile);
            }

            close();
        });

        btnCopiar = Components.createButtonPrimary("Copiar");
        btnCopiar.setEnabled(false);
        btnCopiar.addClickListener((Button.ClickEvent event) -> {
            Path source = Paths.get(sourceFile.getPath());
            Path target = Paths.get(targetDir.getPath().concat("\\").concat(sourceFile.getName()));

            if (sourceFile.isDirectory()) {
                viewLogicDirectory.copyDirectory(source, target, sourceFile);
            } else {
                viewLogicFile.copyFile(source, target, sourceFile);
            }
            close();
        });

        footer.addComponents(btnCancelar, btnMover, btnCopiar);
        footer.setExpandRatio(btnCancelar, 1.0f);
        footer.setComponentAlignment(btnCancelar, Alignment.TOP_RIGHT);
        footer.setComponentAlignment(btnMover, Alignment.TOP_RIGHT);
        footer.setComponentAlignment(btnCopiar, Alignment.TOP_RIGHT);
        return footer;
    }

}
