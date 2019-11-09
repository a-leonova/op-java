import com.leonova.DataGetter;
import com.leonova.FileSearcher;
import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Test;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

import static org.hamcrest.Matchers.containsInAnyOrder;

public class Tests {

    @Test
    public void fileSearching(){
        File startDir = new File("C:\\Users\\Анастасия\\Desktop\\ОП java\\2 lab\\src\\main\\resources\\startDir");
        Predicate<File> predicate = f -> "1984.txt".equals(f.getName());

        FileSearcher searcher = new FileSearcher();

        var files = searcher.searchFiles(startDir, predicate);

        List<File> expectedFiles = List.of(
                new File("C:\\Users\\Анастасия\\Desktop\\ОП java\\2 lab\\src\\main\\resources\\startDir\\chapter1\\1984.txt"),
                new File("C:\\Users\\Анастасия\\Desktop\\ОП java\\2 lab\\src\\main\\resources\\startDir\\chapter2\\1984.txt"),
                new File("C:\\Users\\Анастасия\\Desktop\\ОП java\\2 lab\\src\\main\\resources\\startDir\\chapter3\\1984.txt"),
                new File("C:\\Users\\Анастасия\\Desktop\\ОП java\\2 lab\\src\\main\\resources\\startDir\\chapter3\\end\\1984.txt"));

        Assert.assertThat(files, containsInAnyOrder(expectedFiles.toArray()));
    }

    @Test
    public void StringSearchingOneWorld(){
        String str = "BROTHER";
        File file = new File("C:\\Users\\Анастасия\\Desktop\\ОП java\\2 lab\\src\\main\\resources\\startDir\\chapter1\\1984.txt");
        DataGetter dataGetter = new DataGetter(file, str);
        dataGetter.run();

        Assert.assertTrue(dataGetter.isFoundString());
    }

    @Test
    public void StringSearchingPhrase(){
        String str = "He gazed up at the enormous face. Forty years it had taken him to learn what kind of smile was hidden beneath the dark moustache. O cruel, needless misunderstanding! O stubborn, self-willed exile from the loving breast! Two gin-scented tears trickled down the sides of his nose. But it was all right, everything was all right, the struggle was finished. He had won the victory over himself. He loved Big Brother.";
        File file = new File("C:\\Users\\Анастасия\\Desktop\\ОП java\\2 lab\\src\\main\\resources\\startDir\\chapter3\\end\\1984.txt");
        DataGetter dataGetter = new DataGetter(file, str);
        dataGetter.run();

        Assert.assertTrue(dataGetter.isFoundString());
    }

    @Test
    public void searchStringInSeveralFiles_StringInOneFile_oneTrue(){
        String str = "He gazed up at the enormous face. Forty years it had taken him to learn what kind of smile was hidden beneath the dark moustache. O cruel, needless misunderstanding! O stubborn, self-willed exile from the loving breast! Two gin-scented tears trickled down the sides of his nose. But it was all right, everything was all right, the struggle was finished. He had won the victory over himself. He loved Big Brother.";

        List<DataGetter> dataGetters = new ArrayList<>();
        List<File> files = List.of(
                new File("C:\\Users\\Анастасия\\Desktop\\ОП java\\2 lab\\src\\main\\resources\\startDir\\chapter1\\1984.txt"),
                new File("C:\\Users\\Анастасия\\Desktop\\ОП java\\2 lab\\src\\main\\resources\\startDir\\chapter2\\1984.txt"),
                new File("C:\\Users\\Анастасия\\Desktop\\ОП java\\2 lab\\src\\main\\resources\\startDir\\chapter3\\1984.txt"),
                new File("C:\\Users\\Анастасия\\Desktop\\ОП java\\2 lab\\src\\main\\resources\\startDir\\chapter3\\end\\1984.txt"));

        List<Boolean> results = new ArrayList<>();

        for(File file : files){
            DataGetter dataGetter = new DataGetter(file, str);
            dataGetters.add(dataGetter);
            dataGetter.run();
        }

        dataGetters.forEach(dg -> results.add(dg.isFoundString()));

        List<Boolean> expectedResult = List.of(false, false, false, true);

        Assert.assertThat(results, containsInAnyOrder(expectedResult.toArray()));
    }

    @Test
    public void searchStringInSeveralFiles_StringInTwoFile_oneFalseTwoTrue(){
        String str = "perfesser";

        List<DataGetter> dataGetters = new ArrayList<>();
        List<File> files = List.of(
                new File("C:\\Users\\Анастасия\\Desktop\\ОП java\\2 lab\\src\\main\\resources\\startDir\\chapter1\\Flowers for Algernon.txt"),
                new File("C:\\Users\\Анастасия\\Desktop\\ОП java\\2 lab\\src\\main\\resources\\startDir\\chapter2\\Flowers for Algernon.txt"),
                new File("C:\\Users\\Анастасия\\Desktop\\ОП java\\2 lab\\src\\main\\resources\\startDir\\chapter3\\Flowers for Algernon.txt"));

        List<Boolean> results = new ArrayList<>();

        for(File file : files){
            DataGetter dataGetter = new DataGetter(file, str);
            dataGetters.add(dataGetter);
            dataGetter.run();
        }

        dataGetters.forEach(dg -> results.add(dg.isFoundString()));

        List<Boolean> expectedResult = List.of(true, false, true);

        Assert.assertThat(results, containsInAnyOrder(expectedResult.toArray()));
    }

    @Test
    public void searchStringInSeveralFiles_NoMatches_noTrue(){
        String str = "perfesser";

        List<DataGetter> dataGetters = new ArrayList<>();
        List<File> files = List.of(
                new File("C:\\Users\\Анастасия\\Desktop\\ОП java\\2 lab\\src\\main\\resources\\startDir\\chapter1\\1984.txt"),
                new File("C:\\Users\\Анастасия\\Desktop\\ОП java\\2 lab\\src\\main\\resources\\startDir\\chapter2\\1984.txt"),
                new File("C:\\Users\\Анастасия\\Desktop\\ОП java\\2 lab\\src\\main\\resources\\startDir\\chapter3\\1984.txt"),
                new File("C:\\Users\\Анастасия\\Desktop\\ОП java\\2 lab\\src\\main\\resources\\startDir\\chapter3\\end\\1984.txt"));

        List<Boolean> results = new ArrayList<>();

        for(File file : files){
            DataGetter dataGetter = new DataGetter(file, str);
            dataGetters.add(dataGetter);
            dataGetter.run();
        }

        dataGetters.forEach(dg -> results.add(dg.isFoundString()));

        List<Boolean> expectedResult = List.of(false, false, false, false);

        Assert.assertThat(results, containsInAnyOrder(expectedResult.toArray()));
    }
}
