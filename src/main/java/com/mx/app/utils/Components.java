/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mx.app.utils;

import com.vaadin.event.FieldEvents;
import com.vaadin.server.FontAwesome;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;
import java.util.Date;
import pl.exsio.plupload.Plupload;

/**
 *
 * @author Eduardo
 */
public class Components {
    
    // [ BUTTONS ]

    public static final Button createButtonPrimary(String caption) {
        Button btn = new Button(caption);
        btn.addStyleName(ValoTheme.BUTTON_SMALL);
        btn.addStyleName(ValoTheme.BUTTON_PRIMARY);
        return btn;
    }

    public static final Button createButtonIconTiny() {
        Button btn = new Button();
        btn.addStyleName(ValoTheme.BUTTON_TINY);
        btn.addStyleName(ValoTheme.BUTTON_ICON_ONLY);
        return btn;
    }
    
    public static final Button createButtonIconNormal() {
        Button btn = new Button();
        btn.addStyleName(ValoTheme.BUTTON_SMALL);
        btn.addStyleName(ValoTheme.BUTTON_ICON_ONLY);
        return btn;
    }

    public static final Button createButtonNormal(String caption) {
        Button btn = new Button(caption);
        btn.addStyleName(ValoTheme.BUTTON_SMALL);
        //btn.addStyleName("mybutton");
        return btn;
    }

    public static final Button createButtonPath(String caption) {
        Button btn = new Button(caption);
        btn.addStyleName(ValoTheme.BUTTON_LARGE);
        btn.addStyleName("btnPath");
        return btn;
    }
    
    public static final Button createButtonAddDirectory(String caption) {
        Button btn = new Button(caption);
        btn.setIcon(FontAwesome.PLUS);
        //btn.addStyleName(ValoTheme.BUTTON_SMALL);
        btn.addStyleName(ValoTheme.BUTTON_TINY);
        btn.addStyleName(ValoTheme.BUTTON_PRIMARY);
        return btn;
    }
    
    // [ CHECKBOX ]
    
    public static final CheckBox createCheckBoxSmall(String caption, boolean checked) {
        CheckBox chb = new CheckBox(caption, checked);
        chb.addStyleName(ValoTheme.CHECKBOX_SMALL);
        return chb;
    }
    
    // [ DATEFIELD ]
    
    public static final DateField createDateFieldNac(String caption) {
        DateField f = new DateField(caption);
        f.setDateFormat("dd MMM yyyy");
        f.setRangeEnd(new Date());
//        f.setTextFieldEnabled(false);
        return f;
    }

    public static final DateField createDateField(String caption) {
        DateField f = new DateField(caption);
        f.setDateFormat("dd MMM yyyy");
//        f.setTextFieldEnabled(false);
        return f;
    }

    // [ LABEL ]
    
    public static final Label createLabelArrow() {
        Label lbl = new Label(FontAwesome.ANGLE_RIGHT.getHtml(), ContentMode.HTML);
        lbl.addStyleName(ValoTheme.LABEL_COLORED);
        return lbl;
    }
    
    public static final Label createLabelHeader(String caption) {
        Label lbl = new Label(caption);
        lbl.setSizeUndefined();
        lbl.addStyleName(ValoTheme.LABEL_H1);
        lbl.addStyleName(ValoTheme.LABEL_NO_MARGIN);
        return lbl;
    }

    public static final Label createLabelSection(String caption) {
        Label lbl = new Label(caption);
        lbl.setSizeUndefined();
        lbl.addStyleName(ValoTheme.LABEL_H4);
        lbl.addStyleName(ValoTheme.LABEL_COLORED);
        return lbl;
    }

    public static final Label createLabelEmptyBin() {
        Label lbl = new Label("Papelera vacía");
        lbl.setSizeUndefined();
        lbl.addStyleName(ValoTheme.LABEL_LIGHT);
        return lbl;
    }

    public static final Label createLabelEmptyDirectory() {
        Label lbl = new Label("La carpeta no contiene items");
        lbl.setSizeUndefined();
        lbl.addStyleName(ValoTheme.LABEL_LIGHT);
        lbl.addStyleName(ValoTheme.LABEL_H3);
        return lbl;
    }
    
    // [ MENUBAR ]
    
    public static final MenuBar createMenuButtonPath() {
        MenuBar menubtn = new MenuBar();
        menubtn.setStyleName(ValoTheme.MENUBAR_BORDERLESS);
        return menubtn;
    }
    
    // [ PUPLOAD ]
    
    public static final Plupload createPluploadPrimary(String caption){
        Plupload uploader = new Plupload(caption, FontAwesome.UPLOAD);
        //uploader.addStyleName(ValoTheme.BUTTON_SMALL);
        uploader.addStyleName(ValoTheme.BUTTON_TINY);
        uploader.addStyleName(ValoTheme.BUTTON_PRIMARY);
        return uploader;
    }
    
     

    
    // [ TEXTFIELD ]
    
    public static final TextField createTextSmall(String caption, String value) {
        TextField txt = new TextField(caption);
        txt.addStyleName(ValoTheme.TEXTFIELD_SMALL);
        txt.setWidth("260px");
        txt.setValue(value);
        return txt;
    }
    
    public static final TextField createTextField(String caption) {
        TextField f = new TextField(caption);
        f.setNullRepresentation("");
        f.addFocusListener(focusListener);
        f.addBlurListener(blurListener);
        return f;
    }

    
    static FieldEvents.FocusListener focusListener = new FieldEvents.FocusListener() {

        public void focus(FieldEvents.FocusEvent event) {
            event.getComponent().addStyleName("blue-caption");
        }
    };

    static FieldEvents.BlurListener blurListener = new FieldEvents.BlurListener() {

        public void blur(FieldEvents.BlurEvent event) {
            event.getComponent().removeStyleName("blue-caption");
        }
    };
}
