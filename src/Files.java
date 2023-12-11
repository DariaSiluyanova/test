import java.util.Scanner;
import java.io.File;
import java.io.PrintWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Files {

    public static void main(String[] args) {
        try {
            //создаем ArrayList в который будем складывать все файлы
            ArrayList<File> fileList = new ArrayList<>();
            //получаем файлы из указанной 1-м параметром папки
            getFiles(new File("C:\\Users\\Public\\Downloads\\"), fileList);
            //создаем файл result.txt
            File fileRes = new File("result.txt");
            if (!fileRes.exists()) {
                fileRes.createNewFile();
            }
            PrintWriter pw = new PrintWriter(fileRes);
            //получаем имена файлов в arr и записываем их в файл result.txt
            for(File file: fileList) {
                String fileName = file.getName();
                pw.println(fileName);
            }
            //закрываем файл result.txt
            pw.close();
        } catch(IOException e) {
            System.out.print("Error: " + e);
        }

    }


    private static void getFiles(File rootFile, List<File> fileList) {
        //проверяем папка ли очередной элемент
        if(rootFile.isDirectory()) {
            //создаем File[] с элементами из корневой папки
            File[] directoryFiles = rootFile.listFiles();

            if (directoryFiles != null) {
                //для элемента в arr directoryFiles
                for(File file: directoryFiles) {
                    //если элемент папка, идем вглубь до файлов, их уже заносим в fileList
                    if (file.isDirectory()) {
                        getFiles(file, fileList);
                    } else {
                        if (file.getName().toLowerCase().endsWith(".txt")) {
                            fileList.add(file);
                        }
                    }
                }
            }

        }
    }
}
