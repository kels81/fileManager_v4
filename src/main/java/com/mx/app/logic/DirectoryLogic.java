/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mx.app.logic;

import com.mx.app.data.FileItem;
import com.mx.app.utils.*;
import com.mx.app.view.content.ContentView;
import com.vaadin.server.*;
import com.vaadin.ui.*;
import java.io.*;
import java.nio.file.*;
import java.util.*;
import java.util.logging.*;
import java.util.zip.*;
import org.apache.commons.io.FileUtils;

/**
 *
 * @author Edrd
 */
public class DirectoryLogic {

    private final ContentView viewFiles;

    private final Notifications notification = new Notifications();

    private final List<String> filesListInDir = new ArrayList<>();

    public DirectoryLogic(ContentView view) {
        this.viewFiles = view;
    }
    
    public void downloadDirectory(FileItem directory, Button dwnldInvisibleBtn) {
        try {
            File tmp = Files.createTempDirectory("zip").toFile();
            Path sourceTemp = Paths.get(tmp.getAbsolutePath());
            Boolean result = ZipUtils.zipDirectory(sourceTemp, new File(directory.getPath()));
            System.out.println("result = " + result);

            FileDownloader fileDownloader;
            if (!dwnldInvisibleBtn.getExtensions().isEmpty()) {
                fileDownloader = (FileDownloader) dwnldInvisibleBtn.getExtensions().toArray()[0];
                if (dwnldInvisibleBtn.getExtensions().contains(fileDownloader)) {
                    dwnldInvisibleBtn.removeExtension(fileDownloader);
                    File[] files = tmp.getParentFile().listFiles();
                    if (files.length != 0) {
                        for (File file : files) {
                            if (!tmp.getName().equals(file.getName())) {
                                FileUtils.deleteDirectory(file);
                            }
                        }
                    }
                }
            }
            String zipFile = tmp.getPath().concat(File.separator).concat(directory.getName().concat(".zip"));
            fileDownloader = new FileDownloader(new FileResource(new File(zipFile)));
            fileDownloader.extend(dwnldInvisibleBtn);
            Page.getCurrent().getJavaScript().execute("document.getElementById('DownloadButtonId').click();");
            //FileUtils.deleteDirectory(tmp);
        } catch (IOException ex) {
            Logger.getLogger(DirectoryLogic.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void moveDirectory(Path sourceDir, Path targetDir, FileItem directory) {
        try {
            //Files.move(sourceDir, targetDir, StandardCopyOption.REPLACE_EXISTING);  //REEMPLAZAR EXISTENTE
            System.out.println("move directory");
            Files.move(sourceDir, targetDir);
            // SE RECARGA LA PAGINA, PARA MOSTRAR EL ARCHIVO CARGADO
            String dir = sourceDir.getParent().toString();
            cleanAndDisplay(new FileItem(dir));
            notification.createSuccess("Se movio el archivo correctamente: " + directory.getName());
        } catch (FileAlreadyExistsException ex) {
            notification.createFailure("Ya existe un archivo con el mismo nombre en esta carpeta");
        } catch (IOException ex) {
            notification.createFailure("Problemas al mover el archivo");
        }
    }

    public void copyDirectory(Path sourceDir, Path targetDir, FileItem directory) {
        System.out.println("copy directory");
        try {
            //FileUtils.copyDirectoryToDirectory(sourceDir.toFile(), targetDir.toFile());
            FileUtils.copyDirectory(sourceDir.toFile(), targetDir.toFile());
            // SE RECARGA LA PAGINA, PARA MOSTRAR EL ARCHIVO CARGADO
            String dir = sourceDir.getParent().toString();
            cleanAndDisplay(new FileItem(dir));
            notification.createSuccess("Se copio el archivo correctamente: " + directory.getName());
        } catch (IOException ex) {
            notification.createFailure("Problemas al copiar el archivo");
        }
    }
    
    public void deleteDirectory(Path sourceDir, FileItem directory) {
        System.out.println("delete directory");
        try {
            FileUtils.deleteDirectory(new File(directory.getPath()));
            String dir = sourceDir.getParent().toString();
            cleanAndDisplay(new FileItem(dir));
            notification.createSuccess("Se eliminó el archivo correctamente: " + directory.getName());
        } catch (IOException ex) {
            notification.createFailure("No se elimino el archivo");
        }
    }

//    public void favouriteDirectory(Path sourceDir, FileItem directory) {
//        System.out.println("favourite directory");
//        try {
//            Path targetDir = Paths.get(Constantes.FAVOURITES.concat("\\").concat(directory.getName()));
//            //FileUtils.copyDirectoryToDirectory(sourceDir.toFile(), targetDir.toFile());
//            FileUtils.copyDirectory(sourceDir.toFile(), targetDir.toFile());
//            // SE RECARGA LA PAGINA, PARA MOSTRAR EL ARCHIVO CARGADO
//            String dir = sourceDir.getParent().toString();
//            cleanAndDisplay(new FileItem(dir));
//            notification.createSuccess("Se agrego el archivo a favoritos: " + directory.getName());
//        } catch (IOException ex) {
//            notification.createFailure("Problemas al agregar a favoritos el archivo");
//        }
//    }

//    public void deleteDirectory(Path sourceDir, FileItem directory) {
//        System.out.println("favourite directory");
//        try {
//            Path targetDir = Paths.get(Constantes.BIN.concat("\\").concat(directory.getName()));
//            //FileUtils.copyDirectoryToDirectory(sourceDir.toFile(), targetDir.toFile());
//            FileUtils.moveDirectory(sourceDir.toFile(), targetDir.toFile());
//            // SE RECARGA LA PAGINA, PARA MOSTRAR EL ARCHIVO CARGADO
//            String dir = sourceDir.getParent().toString();
//            cleanAndDisplay(new FileItem(dir));
//            notification.createSuccess("Se eliminó el archivo correctamente: " + directory.getName());
//        } catch (IOException ex) {
//            notification.createFailure("No se elimino el archivo");
//        }
//    }

//    public void emptyBinDirectory(Path sourceDir, FileItem directory) {
//        System.out.println("delete directory");
//        try {
//            FileUtils.deleteDirectory(new File(directory.getPath()));
//            String dir = sourceDir.getParent().toString();
//            cleanAndDisplay(new FileItem(dir));
//            notification.createSuccess("Se elimino el archivo correctamente: " + directory.getName());
//        } catch (IOException ex) {
//            notification.createFailure("No se elimino el archivo");
//        }
//    }

    public void renameDirectory(Path sourceDir, FileItem oldDirectory, FileItem newDirectory) {
        System.out.println("rename directory");
        try {
            oldDirectory.renameTo(newDirectory);
            // SE RECARGA LA PAGINA, PARA MOSTRAR EL ARCHIVO CARGADO
            String dir = sourceDir.getParent().toString();
            cleanAndDisplay(new FileItem(dir));
            notification.createSuccess("Se renombró el archivo correctamente: " + oldDirectory.getName());
        } catch (Exception ex) {
            notification.createFailure("No se renombró el archivo");
        }
    }

//    public void zipDirectory(Path sourceDirectory, File directoryToZip) {
//        try {
//            Path source = Paths.get(directoryToZip.getParent());
//            Boolean result = ZipUtils.zipDirectory(source, directoryToZip);
//            // SE RECARGA LA PAGINA, PARA MOSTRAR EL ARCHIVO CARGADO
//            String dir = sourceDirectory.getParent().toString();
//            cleanAndDisplay(new File(dir));
//            if (result) {
//                notification.createSuccess("Se comprimio el archivo correctamente: " + directoryToZip.getName());
//            } else {
//                notification.createFailure("No se comprimio el archivo");
//            }
//        } catch (Exception e) {
//        }
//
//    }
    public void zipDirectory(Path sourceDirectory, FileItem directoryToZip) {
        try {
            String zipDirectoryName = directoryToZip.getName() + ".zip";
            ZipOutputStream out = new ZipOutputStream(new FileOutputStream(directoryToZip.getParent() + "\\" + zipDirectoryName));

            String directoryName = directoryToZip.getName() + File.separator;

            //File[] files = directoryToZip.listFiles();
            List<FileItem> files = directoryToZip.getList();
            if (!files.isEmpty()) {
                for (FileItem file : files) {
                    if (file.isFile()) {
                        zipFile(file, directoryName, out);
                    } else if (file.isDirectory()) {
                        zipFolder(file, directoryName, out);
                    }
                }
            } else {
                //PARA CUANDO SE QUIERE COMPRIMIR UNA CARPETA VACIA
                out.putNextEntry(new ZipEntry((directoryName.substring(0, directoryName.length() - 1)) + "/"));
                out.closeEntry();
            }
            out.close();

            System.out.println("Done");

            // SE RECARGA LA PAGINA, PARA MOSTRAR EL ARCHIVO CARGADO
            String dir = sourceDirectory.getParent().toString();
            cleanAndDisplay(new FileItem(dir));
            notification.createSuccess("Se comprimio el archivo correctamente: " + directoryToZip.getName());
        } catch (IOException e) {
            notification.createFailure("No se comprimio el archivo");
        }
    }

    public void zipFolder(FileItem inputFolder, String parentName, ZipOutputStream zipOutputStream) throws IOException {
        String directoryName = parentName + inputFolder.getName() + "\\";

//        File[] contents = inputFolder.listFiles();
        List<FileItem> contents = inputFolder.getList();
        if (!contents.isEmpty()) {
            for (FileItem file : contents) {
                if (file.isFile()) {
                    zipFile(file, directoryName, zipOutputStream);
                } else if (file.isDirectory()) {
                    zipFolder(file, directoryName, zipOutputStream);
                }
            }
        } else {
            //PARA AGREGAR AL ZIP UNA CARPETA VACIA
            System.out.println("carpeta vacia: " + (directoryName.substring(0, directoryName.length() - 1)) + "/");
            zipOutputStream.putNextEntry(new ZipEntry((directoryName.substring(0, directoryName.length() - 1)) + "/"));
        }
        zipOutputStream.closeEntry();
    }

    public void zipFile(FileItem inputFile, String parentName, ZipOutputStream zipOutputStream) throws IOException {
        // A ZipEntry represents a file entry in the zip archive
        // We name the ZipEntry after the original file's name
        ZipEntry zipEntry = new ZipEntry(parentName + inputFile.getName());
        zipOutputStream.putNextEntry(zipEntry);

        FileInputStream fileInputStream = new FileInputStream(new File(inputFile.getPath()));
        byte[] buffer = new byte[1024];
        int bytesRead;

        // Read the input file by chucks of 1024 bytes
        // and write the read bytes to the zip stream
        while ((bytesRead = fileInputStream.read(buffer)) > 0) {
            zipOutputStream.write(buffer, 0, bytesRead);
        }

        // close ZipEntry to store the stream to the file
        zipOutputStream.closeEntry();

        System.out.println("Regular file :" + parentName + inputFile.getName() + " is zipped to archive :" + zipOutputStream.toString());
    }

    public void createFolder(Path sourceDir, String name) {
        File directory = new File(sourceDir + "\\" + name);
        directory.mkdir();

        // SE RECARGA LA PAGINA, PARA MOSTRAR EL ARCHIVO CARGADO
        String dir = sourceDir.toString();
        cleanAndDisplay(new FileItem(dir));
        notification.createSuccess("Se cargó con éxito");
    }

    public void cleanAndDisplay(FileItem file) {
        // SE RECARGA LA PAGINA, PARA MOSTRAR EL ARCHIVO CARGADO
        this.viewFiles.cleanAndDisplay(file);
    }

}
