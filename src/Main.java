import java.io.FileNotFoundException;
import java.util.*;
import java.io.File;
import java.io.PrintWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Main {

    public static void main(String[] args) {
        try {
            // Создаем ArrayList, в который будем складывать все файлы
            ArrayList<File> fileList = new ArrayList<>();
            //удаляем result.txt, если он уже существует
            Files.deleteIfExists(Paths.get("\\test\\result.txt"));
            // Получаем файлы из указанной 1-м параметром папки
            getFiles(new File("\\"), fileList);
            // Создаем файл result.txt, куда будет записываться результат
            File fileRes = new File("result.txt");
            PrintWriter pw = new PrintWriter(fileRes);

            //создаем массив, куда будем складывать зависимости в дочерних файлах
            ArrayList<String> res = new ArrayList<>();
            // Получаем имена файлов из массива и записываем их в файл result.txt
            for (File file : fileList) {
                //переменная для названия текстового файла в строке с 'require'
                String nameTxt = fileHasDependency(file);
                if(fileHasDependency(file) == null) {
                    //если файл 'родительский' мы заносим его в result.txt, если его нет в массиве res
                    String fileName = file.getName();
                    if (!res.contains(fileName)) {
                        pw.println(fileName);
                        res.add(fileName);
                    }
                } else {
                    //если в файле есть зависимость, мы заносим название 'родительского' файла и "ребенка"
                    pw.println(nameTxt + " \n" + file.getName());
                    res.add(nameTxt);
                }
            }
            // Закрываем файл result.txt
            pw.close();
        } catch (IOException e) {
            System.out.print("Error: " + e);
        }

    }

    private static void getFiles(File rootFile, List<File> fileList) throws FileNotFoundException {
        // Проверяем, является ли корневой элемент папкой
        if (rootFile.isDirectory()) {
            // Создаем File[] с элементами из корневой папки
            File[] directoryFiles = rootFile.listFiles();

            if (directoryFiles != null) {
                // Для каждого элемента в directoryFiles
                for (File file : directoryFiles) {
                    // Если элемент является директорией, идем вглубь до файлов, которые заносим в fileList
                    if (file.isDirectory()) {
                        getFiles(file, fileList);
                    } else {
                        //добавляем нужные файлы в массив fileList
                        if (file.getName().toLowerCase().endsWith(".txt")) {
                            fileList.add(file);
                        }
                    }
                }
            }
            // Сортируем файлы в directoryFiles через интерфейс Comparator
            if (directoryFiles != null) {
                Arrays.sort(directoryFiles, Comparator.comparing(File::getName));
            }
        }
    }
    //Берем строку с require из файлов,где есть зависимость
    private static String fileHasDependency(File file) throws FileNotFoundException {
        try (Scanner scan = new Scanner(file)) {
            // берем только файлы со строками
            while (scan.hasNextLine()) {
                String line = scan.nextLine();
                //если в строке есть "require"
                if (line.startsWith("require")) {
                    return cutString(line);
                };
            }
        }
        return null;
    }

    //обрезаем строку require до названия текстового файла
    private static String cutString(String name) {
        int start = name.indexOf("require '/") + "require '/".length();
        int endIndex = name.indexOf("'", start);
        return (start >= 0 && endIndex >= start) ? name.substring(start, endIndex) : null;
    }
}
