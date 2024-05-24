import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class BuildStudentData {

    public static void Build(String dataFileName) {
        Path studentJsonFile = Path.of("students.json"); /* json file */
        String dataFile = dataFileName + ".dat";
        Map<Long, Long> indexedIdsMap = new LinkedHashMap<>();/* レコード(ID,位置) */

        try {
            Files.deleteIfExists(Path.of(dataFile));
            String data = Files.readString(studentJsonFile); /* ファイルを読み込んで、内容を文字列として保存します */
            data = data.replaceFirst("^(\\[)", "")/* [と]を削除します */
                    .replaceFirst("(\\])$", "");
            var records = data.split(System.lineSeparator());  /* 改行文字でデータを分割します */
            System.out.println("# of records = " + records.length);

            /* 初期位置は、レコードの数に16倍を加え、改行のための4バイトを加えたものになります */
            long startingPos = 4 + (16L * records.length);
            Pattern idPattern = Pattern.compile("studentId\":([0-9]+)"); /* studentId:(数字1個以上) */
            try (RandomAccessFile randomAccessFile = new RandomAccessFile(dataFile, "rw")) {
                randomAccessFile.seek(startingPos);
                for (String record : records) {
                    Matcher matcher = idPattern.matcher(record); /* マップに保存するIDを見つけます */
                    if (matcher.find()) {
                        long studentId = Long.parseLong(matcher.group(1));
                        indexedIdsMap.put(studentId, randomAccessFile.getFilePointer());
                        randomAccessFile.writeUTF(record);
                    }
                }
                /* ファイルの先頭にインデックスを書き込みます */
                writeIndex(randomAccessFile, indexedIdsMap);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /* ファイルの先頭 */
    private static void writeIndex(RandomAccessFile randomAccessFile, Map<Long, Long> indexMap) {
        try {
            /* ファイルの先頭から開始します */
            /* ファイルの先頭に、レコードの数（つまりデータの数）を書き込みます */
            randomAccessFile.seek(0);
            randomAccessFile.writeInt(indexMap.size());

            /* マップ内のすべてのデータをID-位置の形式で書き込みます */
            for (var studentIdx : indexMap.entrySet()) {
                randomAccessFile.writeLong(studentIdx.getKey());
                randomAccessFile.writeLong(studentIdx.getValue());
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
