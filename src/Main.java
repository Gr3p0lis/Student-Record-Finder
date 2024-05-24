import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Scanner;

public class Main {
    private static final Map<Long,Long> indexedIds  = new LinkedHashMap<>();
    private static int recordsInFile = 0;

    public static void main(String[] args) {
        /* Creo il file .dat utilizzando i dati salvati nel file .json */
        BuildStudentData.Build("studentData");

        try(RandomAccessFile randomAccessFile = new RandomAccessFile("studentData.dat", "r")){
            /* インデックスをマップにロードします */
            loadIndex(randomAccessFile,0);
            Scanner scanner = new Scanner(System.in);
            System.out.println("Enter a student id or 0 to quit");
            while(scanner.hasNext()){
                long studentId = Long.parseLong(scanner.nextLine());
                if(studentId < 1){
                    break;
                }
                randomAccessFile.seek(indexedIds.get(studentId));
                String targetRecord = randomAccessFile.readUTF();
                System.out.println(targetRecord);
                System.out.println("Enter another Student ID or 0 to quit.");
            }
        }catch (IOException e){
            throw new RuntimeException(e);
        }
    }

    /* マップにインデックスをロードします。 */
    private static void loadIndex(RandomAccessFile randomAccessFile, int indexPosition){
        try{
            randomAccessFile.seek(indexPosition);
            recordsInFile = randomAccessFile.readInt();
            System.out.println(recordsInFile);
            for(int i = 0; i < recordsInFile; i++){
                /* 最初のreadLong() = キー, ファイルポインタの位置から8バイトを読み取り、キーを見つけます
                 *  2番目のreadLong() = 格納されたファイルの位置を取得するために、ファイルポインタの位置から8バイトを読み取ります。 */
                indexedIds.put(randomAccessFile.readLong(), randomAccessFile.readLong());
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}